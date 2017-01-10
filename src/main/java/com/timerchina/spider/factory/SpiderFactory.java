package com.timerchina.spider.factory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.bean.InfoMsg;
import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.proxy.IProxySpider;
import com.timerchina.spider.proxy.NettySpider;
import com.timerchina.spider.proxy.netty.exception.URLIllegalException;
import com.timerchina.spider.service.CryptogramProcessor;
import com.timerchina.spider.service.GeneralSpider;
import com.timerchina.spider.service.LoggerHandler;
import com.timerchina.spider.service.OverseasSpider;
import com.timerchina.spider.service.ShieldDetection;
import com.timerchina.spider.service.HttpClient.CloseableHttpClientCapture;
import com.timerchina.spider.service.utils.PropTool;
import com.timerchina.spider.service.utils.PropertiesTool;

/**
 * 主调用方法的工厂类
 * @author jason.lin@timerchina.com
 * @timer 2014-7-16 上午11:29:49
 */
public class SpiderFactory {

	private static Map<String, String> domainAndSoldierType = new HashMap<String, String>();
	public static final int RETRY_TRANS_TIMES = 50;												//对URI进行特殊字符转码的重试次数，解析失败三次后，则返回解析失败
	public static Map<String, String> urlSpecialCharMap = null;		//保存URL需要转换的特殊字符
	public static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	public static int specialCharCount = 0;							//记录配置文件中特殊字符的数量
	private SpiderFactory(){};
	/**
	 * 本地抓取的接口,内层抓取
	 * @return Object
	 */
	public static Object executeLocal(SpiderParams spiderP) {
		SpiderResult spiderResult = new SpiderResult();
		SpiderParams spiderParams = null;
		try {
			spiderParams = (SpiderParams) spiderP.clone();
		} catch (CloneNotSupportedException e1) {
			spiderParams = spiderP;
			e1.printStackTrace();
		}
		try {
			if(spiderParams.isOverseaURL()){
				LoggerHandler.getInstance().writeLogger(InfoMsg.E_LOCALSPIDER_OVERSEA, Constant.LOG_LEVEL_INFO);
				spiderResult.spiderResultError(
						SpiderResult.ISPROXY_ISOVERSEA_EQUALS_TRUE_ERROR,
						InfoMsg.E_LOCALSPIDER_OVERSEA, spiderResult);
				return spiderResult;
			}
			SpiderFactory sf = new SpiderFactory();
			spiderParams = sf.validateParams(spiderParams);
			spiderP.setDomain(spiderParams.getDomain());
		} catch (URLIllegalException e) {
			spiderResult.spiderResultError(SpiderResult.URL_ILLEGAL,
					InfoMsg.E_URL_ERROR + spiderParams.getUrl(), spiderResult);
			spiderParams.setDomain(spiderParams.getUrl());
		}
		LoggerHandler.getInstance().writeLogger(InfoMsg.I_BEGIN_LOCAL, Constant.LOG_LEVEL_INFO);
		//本地抓取和爬虫中心的请求
		GeneralSpider generalSpider = new GeneralSpider(
				null, new ShieldDetection(),
				new CloseableHttpClientCapture(), null, new NettySpider());
		return generalSpider.executeSpider(spiderParams);
	}
	
	/**
	 * 海外抓取的接口,参数为true，执行海外抓取；为false，执行本地抓取
	 * @param spiderParams
	 * @param isOverseaSpider
	 * @return Object
	 */
	public static Object executeOversea(SpiderParams spiderP) {
		SpiderResult spiderResult = new SpiderResult();
		SpiderParams spiderParams = null;
		try {
			spiderParams = (SpiderParams) spiderP.clone();
		} catch (CloneNotSupportedException e1) {
			spiderParams = spiderP;
			e1.printStackTrace();
		}
		if(spiderParams.isProxy()){
			LoggerHandler.getInstance().writeLogger(InfoMsg.E_LOCALSPIDER_PROXY, Constant.LOG_LEVEL_ERROR);
			spiderResult.spiderResultError(
					SpiderResult.ISPROXY_EQUALS_TRUE_ERROR,
					InfoMsg.E_LOCALSPIDER_PROXY, spiderResult);
			return spiderResult;
		}
		//如果isOverseaSpider和isProxy都为空，则默认执行本地抓取
		spiderParams.setOverseaURL(true);
		if(spiderParams.isLocal() == true){
			OverseasSpider overseaSpider = null;
			try {
				overseaSpider = new OverseasSpider(null, new ShieldDetection(),
						new CloseableHttpClientCapture(), new CryptogramProcessor(), new NettySpider());
				return overseaSpider.executeSpider(spiderParams);
			} catch (Exception e) {
				LoggerHandler.getInstance().writeLogger(InfoMsg.E_OVERSEA_ENCRYPT, Constant.LOG_LEVEL_ERROR);
				spiderResult.spiderResultError(SpiderResult.ENCRIPTION_ERROR,
						InfoMsg.E_OVERSEA_ENCRYPT, spiderResult);
				return spiderResult;
			}
		}
		try {
			SpiderFactory sf = new SpiderFactory();
			spiderParams = sf.validateParams(spiderParams);
			spiderP.setDomain(spiderParams.getDomain());
		} catch (URLIllegalException e1) {
			spiderResult.spiderResultError(SpiderResult.URL_ILLEGAL,
					InfoMsg.E_URL_ERROR, spiderResult);
			spiderParams.setDomain(spiderParams.getUrl());
		}
		LoggerHandler.getInstance().writeLogger(InfoMsg.I_BEGIN_OVERSEA + spiderParams.getUrl(), Constant.LOG_LEVEL_INFO);
		IProxySpider proxySpider = new NettySpider();
		spiderParams.setLocal(true);
		spiderResult = proxySpider.getProxyResult(spiderParams);
		return spiderResult;
	}
	
	/**
	 * 请求爬虫中心的接口，参数为true，请求爬虫中心；参数为false，执行本地抓取
	 * @param spiderParams
	 * @param isOverseaSpider
	 * @return
	 */
	public static Object executeCaptain(SpiderParams spiderP) {
		SpiderResult spiderResult = new SpiderResult();
		SpiderParams spiderParams = null;
		try {
			spiderParams = (SpiderParams) spiderP.clone();
		} catch (CloneNotSupportedException e1) {
			spiderParams = spiderP;
			e1.printStackTrace();
		}
		try {
			if(spiderParams.isOverseaURL()){
				LoggerHandler.getInstance().writeLogger(InfoMsg.E_LOCALSPIDER_OVERSEA, Constant.LOG_LEVEL_ERROR);
				spiderResult.spiderResultError(
						SpiderResult.ISOVSESEA_EQUALS_TRUE_ERROR,
						InfoMsg.E_LOCALSPIDER_OVERSEA, spiderResult);
				return spiderResult;
			}
			SpiderFactory sf = new SpiderFactory();
			spiderParams = sf.validateParams(spiderParams);
			spiderP.setDomain(spiderParams.getDomain());
		} catch (URLIllegalException e1) {
			spiderResult.spiderResultError(SpiderResult.URL_ILLEGAL,
					InfoMsg.E_URL_ERROR, spiderResult);
			spiderParams.setDomain(spiderParams.getUrl());
			return spiderResult;
		}
		if(spiderParams.isLocal()){
			return executeLocal(spiderParams);
		}
		else{
			spiderParams.setProxy(true);
			LoggerHandler.getInstance().writeLogger(InfoMsg.I_BEGIN_PROXY + spiderParams.getUrl(), Constant.LOG_LEVEL_INFO);
			IProxySpider proxySpider = new NettySpider();
			spiderParams.setLocal(true);
			spiderResult = proxySpider.getProxyResult(spiderParams);
		}
		return spiderResult;
	}
	
	/**
	 * 当不确定是海外还是爬虫中心还是本地抓取，则直接调用该方法
	 * @param spiderParams
	 * @return Object
	 */
	public static Object execute(SpiderParams spiderP){
		SpiderParams spiderParams = null;
		try {
			spiderParams = (SpiderParams) spiderP.clone();
		} catch (CloneNotSupportedException e1) {
			spiderParams = spiderP;
			e1.printStackTrace();
		}
		if(spiderParams.isProxy() && spiderParams.isOverseaURL()){
			LoggerHandler.getInstance().writeLogger(InfoMsg.E_LOCALSPIDER_P_O, Constant.LOG_LEVEL_ERROR);
			return null;
		}
		if(spiderParams.isOverseaURL()){
			return executeOversea(spiderParams);
		}else if(spiderParams.isProxy()){
			return executeCaptain(spiderParams);
		}
		return executeLocal(spiderParams);
	}
	
	/**
	 * 对传进来的参数进行验证，主要针对URL
	 * @param spiderParams
	 * @throws URLIllegalException 
	 * @throws URLValidateException 
	 */
	private SpiderParams validateParams(SpiderParams spiderParams) throws URLIllegalException {
		//加载各种domain和对应的soldierType
		if(domainAndSoldierType == null || domainAndSoldierType.size() < 1){
			getDomainAndSoldierType();
		}
		
		try{
			String host = null;
			if(spiderParams.getUrl().contains(Constant.WEIBO_DOMAIN_INNER)){
				host = Constant.WEIBO_DOMAIN_INNER;
			}else{
				URL userUrl = new URL(spiderParams.getUrl());
				host = userUrl.getHost();
			}
			
			/*处理惠普问题
			 * transCharactorFromProps();
					int count = 0;
					while(true){
						if(count >= 30){
							break;
						}
						@SuppressWarnings("unused")
						URI uri = null;
						try {
							uri = new URI(spiderParams.getUrl());
							break;
						}catch (URISyntaxException e) {
							transURL(spiderParams, e);
							count++;
						}
					}
			 */
			
			if(host.contains(Constant.WEIBO_API_DOMAIN)){
				;
			}else{
				for (int i = 0; i < SpiderFactory.RETRY_TRANS_TIMES; i++) {
					transCharactorFromProps();
					@SuppressWarnings("unused")
					URI uri = null;
					try {
						uri = new URI(spiderParams.getUrl());
						break;
					}catch (URISyntaxException e) {
						transURL(spiderParams, e);
					}
				}
			}
			
			//如果对应的soldierType在配置文件中有定义，则设置对应的爬虫类型，否则默认为1
			if (domainAndSoldierType.get(host) != null) {
				spiderParams.setSoldierType(Integer
						.parseInt(domainAndSoldierType.get(host)));
				LoggerHandler.getInstance().writeLogger(InfoMsg.I_SPIDER_TYPE + spiderParams.getSoldierType(), Constant.LOG_LEVEL_INFO);
			}
			spiderParams.setDomain(host);
		}catch (MalformedURLException e) {
			LoggerHandler.getInstance().writeLogger(InfoMsg.E_URL_ERROR + spiderParams.getUrl(), Constant.LOG_LEVEL_ERROR);
			throw new URLIllegalException();
		}catch(Exception e){
			LoggerHandler.getInstance().writeLogger(InfoMsg.E_UNKNOW_ERROR+" "+e.getCause(), Constant.LOG_LEVEL_ERROR);
		}
		return spiderParams;
	}
	
	/**
	 * 从配置文件中获取domain和对应的soldierType
	 */
	public void getDomainAndSoldierType(){
		String[] domain = PropertiesTool
				.getSpiderInstance()
				.getPropertiesOutOfJar(Constant.SPIDER,
						Constant.WEIBO_DOMAIN_IN_PROPERTIES).split(",");
		String[] slodierType = PropertiesTool.getSpiderInstance()
				.getPropertiesOutOfJar(Constant.SPIDER, Constant.SOLDIER_IN_PROPERTIES)
				.split(",");
		
		for (int i = 0; i < domain.length; i++) {
			domainAndSoldierType.put(domain[i], slodierType[i]);
		}
	}
	
	/**
	 * 处理URL中存在的特殊字符问题
	 */
	public void transURL(SpiderParams spiderParams, URISyntaxException e){
		int index = e.getIndex();
		String begainStr = spiderParams.getUrl().substring(0, index);
		String specialChar = spiderParams.getUrl().substring(index, index + 1);
		String endStr = spiderParams.getUrl().substring(index + 1, spiderParams.getUrl().length());
		spiderParams.setUrl(begainStr + SpiderFactory.urlSpecialCharMap.get(specialChar) + endStr);
	}
	
	/**
	 * 从配置文件中获取URL中需要转换的特殊字符
	 * @param args
	 */
	public void transCharactorFromProps(){
		try {
			lock.readLock().lock();
			if(SpiderFactory.urlSpecialCharMap != null && SpiderFactory.urlSpecialCharMap.size() == specialCharCount){
				return;
			}else{
				SpiderFactory.urlSpecialCharMap = new HashMap<String, String>();
				String specialChar = PropertiesTool.getSpiderInstance().getPropertiesOutOfJar(Constant.PROPS_SPIDER, Constant.SPECIAL_CHARACTOR);
				String transChar = PropertiesTool.getSpiderInstance().getPropertiesOutOfJar(Constant.PROPS_SPIDER, Constant.TRANS_CHARACTOR);
//				String specialChar = PropertiesTool.getSpiderInstance().getProperties(Constant.PROPS_SPIDER, Constant.SPECIAL_CHARACTOR);
//				String transChar = PropertiesTool.getSpiderInstance().getProperties(Constant.PROPS_SPIDER, Constant.TRANS_CHARACTOR);
				if(specialChar != null && transChar != null){
					String[] specialCharArray = specialChar.split(Constant.SPLIT_COMMA);
					String[] transCharArray = transChar.split(Constant.SPLIT_COMMA);
					if(specialCharArray.length == transCharArray.length){
						specialCharCount = specialCharArray.length;
					}else{
						LoggerHandler.getInstance().writeLogger(InfoMsg.URL_CHARACTOR_ERROR, Constant.LOG_LEVEL_ERROR);
					}
					for (int i = 0; i < specialCharArray.length; i++) {
						SpiderFactory.urlSpecialCharMap.put(specialCharArray[i], transCharArray[i]);
					}
				}
			}
		} finally{
			lock.readLock().unlock();
		}
	}
}
