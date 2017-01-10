package com.timerchina.spider.proxy;

import io.netty.channel.ChannelFuture;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;

import javax.net.ssl.SSLException;

import com.timerchina.spider.bean.CaptainParams;
import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.bean.InfoMsg;
import com.timerchina.spider.bean.SCharSet;
import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.proxy.netty.base.client.NettyClient;
import com.timerchina.spider.proxy.netty.base.client.NettyClientConfig;
import com.timerchina.spider.proxy.netty.handler.NettyClientChannelInitializer;
import com.timerchina.spider.proxy.netty.exception.ReadCaptainTimeoutException;
import com.timerchina.spider.service.CryptogramProcessor;
import com.timerchina.spider.service.LoggerHandler;
import com.timerchina.spider.service.exception.UnGZIPFailException;
import com.timerchina.spider.service.utils.Native2AsciiUtils;
import com.timerchina.spider.service.utils.ZipUtils;

/**
 * @author jason.lin@timerchina.com 2014-10-27 上午10:39:09
 * @statement 针对App端的netty client类，主要配置在Spider2014上，去请求manager的server，获取网页源码
 */
public class NettySpider implements IProxySpider{
	
	private static NettyClientConfig config = new NettyClientConfig();
	private static CryptogramProcessor encrypt = null;
	private static int retrySleepTimes = 1000;
	private static CaptainParams captainparams = null;
	private static int port = 0;
	private static String ip = "";
	private static Object lock = new Object();
	private static boolean isCaptainUseful = false;
	
	/**
	 * 获取由代理中心进行抓取访问的网页信息，结果还是组合成SpiderResult对象
	 * @throws ConnectException 
	 */
	public SpiderResult getProxyResult(SpiderParams spiderParams){
		SpiderResult spiderResult = new SpiderResult();
		try {
			if(spiderParams.isOverseaURL()){
				spiderParams = callOversea(spiderParams);
			}
			//对IP和端口进行初始化
			initIpAndPort(spiderParams);
			if(!isCaptainUseful){//20160514
//				synchronized(lock){
					if(!isCaptainUseful){
						initNettyClientConfig(ip,port);
						//建立连接
						spiderResult = openClient(spiderParams);
						return spiderResult;
					}
//				}
			}
			initNettyClientConfig(ip,port);
			spiderResult = openClient(spiderParams);
		} catch(Exception e){
			LoggerHandler.getInstance().writeLogger(InfoMsg.E_UNKNOW_ERROR+e.getLocalizedMessage(), Constant.LOG_LEVEL_INFO);
			spiderResult.spiderResultError(SpiderResult.UNKNOW_ERROR,InfoMsg.E_UNKNOW_ERROR + e.getMessage() + "  Method:getProxyResult()",spiderResult);
		}
		return spiderResult;
	}
	
	/**
	 * 初始化Ip和Port
	 * @param spiderParams
	 */
	public void initIpAndPort(SpiderParams spiderParams){
		if(spiderParams.getCaptainIP()!=null && spiderParams.getCaptainIP().length()>0){
			port = spiderParams.getCaptainPORT();
			ip = spiderParams.getCaptainIP();
		}else{
			captainparams = CaptainMap.getInstance().getOneNode(
					spiderParams.isOverseaURL());
			port = captainparams.getPort();
			ip = captainparams.getCaptain_IP();
		}
		LoggerHandler.getInstance().writeLogger("#INFO-请求 "+ip+"/"+port+" 的Netty Server", Constant.LOG_LEVEL_INFO);
	}
	
	/**
	 * 针对海外抓取的URL进行加密
	 * @param spiderParams
	 * @return
	 */
	public SpiderParams callOversea(SpiderParams spiderParams){
		try {
			encrypt = new CryptogramProcessor();
			spiderParams.setUrl(encrypt.encrypt(spiderParams.getUrl()));
		} catch (Exception e) {
			LoggerHandler.getInstance().writeLogger(InfoMsg.E_ENCRIPT_URL + e.getCause(), Constant.LOG_LEVEL_ERROR);
		}
		return spiderParams;
	}
	
	/**
	 * 开启客户端，获得结果
	 * @param spiderParams
	 * @return
	 * @throws InterruptedException 
	 * @throws SSLException 
	 */
	private SpiderResult openClient(SpiderParams spiderParams)
			throws SSLException, InterruptedException {
		NettyClient client = null;
		SpiderResult spiderResult = new SpiderResult();
		Exception exception = new Exception();
		try{
			client = new NettyClient(config);
			ChannelFuture channelFuture = null;
			//重试连接爬虫中心，默认重试次数为2次
			int i = 0;
			for (i = 0; i < spiderParams.getConnCaptainTimes(); i++) {
				try{
					channelFuture = client.connect();
				}catch (Exception e) {
					LoggerHandler.getInstance().writeLogger("连接 "+config.getIp()+" 失败，重试第  "+(i+1)+" 次", Constant.LOG_LEVEL_ERROR);
					exception = e;
					Thread.sleep(retrySleepTimes);
					continue;
				}
				if(channelFuture!=null && channelFuture.isSuccess()){
					isCaptainUseful = true;
					break;
				}
			}
			if(i == spiderParams.getConnCaptainTimes()){
				if(CaptainMap.getCaptainIpCount(spiderParams.isOverseaURL())==1){
					LoggerHandler.getInstance().writeLogger(InfoMsg.E_REQ_CAPTAIN_MAXRTIMES+ "，当前爬虫中心IP "+ CaptainMap.getCaptainIpCount(spiderParams.isOverseaURL())+ " 个，无其他可用IP，请检查爬虫中心是否开启", Constant.LOG_LEVEL_ERROR);
				}else{
					LoggerHandler.getInstance().writeLogger(InfoMsg.E_REQ_CAPTAIN_MAXRTIMES+ "爬虫中心IP "+ CaptainMap.getCaptainIpCount(spiderParams.isOverseaURL())+ " 个，当前使用第 "+ (CaptainMap.getCaptainIndex(spiderParams.isOverseaURL())+1) + " 个，准备切换到下一个，下次请求起效", Constant.LOG_LEVEL_ERROR);
				}
				executeException(exception, spiderResult, spiderParams);
				return spiderResult;
			}
			spiderResult = (SpiderResult) client.start(spiderParams,
					channelFuture, spiderParams.getHttpConnectionTimeout()
							+ spiderParams.getHttpReadTimeout() + 7000);
			LoggerHandler.getInstance().writeLogger(InfoMsg.I_SOURCECODE_LENGTH+spiderResult.getHtml().length(), Constant.LOG_LEVEL_INFO);
		}catch (ReadCaptainTimeoutException e) {
			LoggerHandler.getInstance().writeLogger(InfoMsg.E_READ_CAPTAIN_TIMEOUT, Constant.LOG_LEVEL_ERROR);
			spiderResult.setResponseCode(SpiderResult.READ_CAPTAIN_TIMEOUT);
			return spiderResult;
		}
		spiderResult = handleSpiderResult(spiderResult, spiderParams);
		return spiderResult;
	}
	
	/**
	 * 处理异常，组装异常信息到SpiderResult中
	 * @param e,spiderResult
	 * @return
	 */
	public SpiderResult executeException(Exception e,SpiderResult spiderResult, SpiderParams spiderParams){
		//这里主要处理两个异常(因为无法直接catch),ConnectException和SocketException
		String errorMsg = e.getMessage();
		if(errorMsg!=null){
			if(errorMsg.contains(Constant.NETWORD_UNREACHABLE)){
				LoggerHandler.getInstance().writeLogger(InfoMsg.E_NETWORD_UNREACHABLE, Constant.LOG_LEVEL_ERROR);
				spiderResult.spiderResultError(
						SpiderResult.NETWORD_UNREACHABLE,
						InfoMsg.E_NETWORD_UNREACHABLE, spiderResult);
				return spiderResult;
			}else if(errorMsg.contains(Constant.CONN_REFUSED)){
				CaptainMap.setCaptainIndex(ip);
				initIpAndPort(spiderParams);
//				logger.error(InfoMsg.E_REQUEST_REJECT + config.getIp() + "/" + config.getPort() );
				spiderResult.spiderResultError(
						SpiderResult.CANNOT_CONN_CAPTAIN,
						InfoMsg.E_REQUEST_REJECT + config.getIp() + "/" + config.getPort(),
						spiderResult);
			}else{
				LoggerHandler.getInstance().writeLogger(InfoMsg.E_UNKNOW_ERROR+e.getCause(), Constant.LOG_LEVEL_ERROR);
				spiderResult.spiderResultError(SpiderResult.UNKNOW_ERROR,InfoMsg.E_UNKNOW_ERROR + e.getMessage() + "  Method：executeException",spiderResult);
			}
		}
		return spiderResult;
	}
	
	
	/**
	 * 对返回回来的源码和一些信息进行处理(源码解压)，主要针对海外抓取的源码解密
	 * @param spiderResult
	 * @param spiderParams
	 * @return
	 */
	public SpiderResult handleSpiderResult(SpiderResult spiderResult, SpiderParams spiderParams){
		if(spiderResult!=null && spiderResult.getHtml().length() > 0){
			try {
				spiderResult.setHtml(ZipUtils.unGzip(spiderResult.getHtml()));
				if(spiderParams.isOverseaURL()){
						encrypt = new CryptogramProcessor();
						spiderResult.setHtml(encrypt.decrypt(spiderResult.getHtml()));
						spiderResult.setRealUrl(encrypt.decrypt(spiderResult.getRealUrl()));
				}
				if(!spiderParams.getCharset().equalsIgnoreCase(SCharSet.ISO_8859_1)){
					spiderResult.setHtml(Native2AsciiUtils.ascii2Native(spiderResult.getHtml()));
				}
			}catch(EOFException e){
				LoggerHandler.getInstance().writeLogger(InfoMsg.E_UNGZIP_ERROR, Constant.LOG_LEVEL_ERROR);
				spiderResult.spiderResultError(SpiderResult.UNGZIP_FAIL,InfoMsg.E_UNGZIP_ERROR, spiderResult);
				return spiderResult;
			}catch (UnGZIPFailException e) {
				LoggerHandler.getInstance().writeLogger(InfoMsg.E_UNGZIP_ERROR, Constant.LOG_LEVEL_ERROR);
			} catch(IOException e){
				LoggerHandler.getInstance().writeLogger(InfoMsg.E_UNGZIP_ERROR, Constant.LOG_LEVEL_ERROR);
				spiderResult.spiderResultError(SpiderResult.UNGZIP_FAIL,InfoMsg.E_UNGZIP_ERROR, spiderResult);
				return spiderResult;
			}catch (NumberFormatException e) {
				e.printStackTrace();
				LoggerHandler.getInstance().writeLogger(InfoMsg.E_SOURCECODE_NATIVE2ASCIIU + e.getLocalizedMessage(), Constant.LOG_LEVEL_ERROR);
			} catch (Exception e) {
				LoggerHandler.getInstance().writeLogger(InfoMsg.E_OVERSEA_DECRIPT_ERROR+e.getCause(), Constant.LOG_LEVEL_ERROR);
				spiderResult.spiderResultError(SpiderResult.ENCRIPTION_ERROR,
						InfoMsg.E_OVERSEA_DECRIPT_ERROR + e.getMessage(),spiderResult);
				return spiderResult;
			}
		}
		return spiderResult;
	}
	
	/**
	 * 初始化NettyConfig配置对象，包括IP和端口的获取
	 */
	private synchronized void initNettyClientConfig(String ip, int port){
		config.setPort(port);
		config.setIp(ip);
		config.setChannelInitializer(new NettyClientChannelInitializer());
		config.setDirSendRes(true);
	}
}
