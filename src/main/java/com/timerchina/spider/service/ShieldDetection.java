package com.timerchina.spider.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.bean.InfoMsg;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.bean.WeiboAPIBean;
import com.timerchina.spider.proxy.netty.base.utils.ConvertJsonAndObjectUtil;
import com.timerchina.spider.service.IShieldDetection;
import com.timerchina.spider.service.utils.PropertiesTool;
import com.timerchina.spider.service.utils.RegexTools;

/**
 * 屏蔽检测的类
 * @author jason.lin@timerchina.com
 * @time 2014-7-16 下午02:40:16
 */
public class ShieldDetection implements IShieldDetection {
	
	private static Map<String, String> shieldMap = new HashMap<String, String>();
	private static ReadWriteLock lock = new ReentrantReadWriteLock();
	
	/**
	 * 初始化所有的屏蔽词到内存中
	 */
	public ShieldDetection(){
		getShieldMap();
	}
	
	//将所有的屏蔽网站的信息加载到内存中
	private static void getShieldMap(){
		if(shieldMap==null || shieldMap.size() < 1){
			Lock readLock = lock.readLock();
			try{
				readLock.lock();
				if(shieldMap==null || shieldMap.size() < 1){
					shieldMap = PropertiesTool.getSpiderInstance().getProps2Map(Constant.PROPS_SHIELD);
				}
			}finally{
				readLock.unlock();
			}
		}
	}
	
	/**
	 * 进行屏蔽检测的方法
	 * @return 是否被屏蔽
	 */
	public void isShield(String sourceCode, String domain, SpiderResult spiderResult) {
		//如果内存中不存在屏蔽词，则先去配置文件中获取
		getShieldMap();
		boolean isShield = false;
		if(shieldMap.get(domain)==null){
			if(domain.split(Constant.SPLIT_DOT).length > 2){
				domain = domain.substring(domain.indexOf(Constant.INDEX_DOT) + 1, domain.length());
			}
		}
		
		String shield = shieldMap.get(domain);
		if(shield==null || shield.isEmpty()){
			domain = Constant.SHIELD_DEFAULTDOMAIN;
		}
		String[] shieldFeature = shieldMap.get(domain).split("@");
		
		//针对微博API接口的访问，因为这个的屏蔽规则跟普通的规则不一样，所以做特殊处理
		if(domain.equalsIgnoreCase(Constant.WEIBO_API_DOMAIN)){
			isShield = weiboApiShield(sourceCode, shieldFeature, spiderResult);
			return;
		}else{
			//下面是针对大多数的屏蔽检测方法
			isShield = shieldForCommon(sourceCode, shieldFeature, spiderResult);
		}
		if(isShield){
			spiderResult.setShield(isShield);
		}
	}

	/**
	 * 针对普通的页面的屏蔽检测
	 * @return
	 */
	public boolean shieldForCommon(String sourceCode, String[] shieldFeature, SpiderResult spiderResult){
		if(sourceCode.length() < Integer.parseInt(shieldFeature[0])){
			if(shieldFeature.length==1){
				return true;
			}
			return RegexTools.isMatched(shieldFeature[1], sourceCode);
		}
		return false;
	}
	
	/**
	 * 新浪微博API的屏蔽检测
	 * @param sourceCode
	 * @param shieldFeature
	 * @return
	 */
	public boolean weiboApiShield(String sourceCode, String[] shieldFeature, SpiderResult spiderResult){
		if(sourceCode.length() < Integer.parseInt(shieldFeature[0])){
			try{
				if(sourceCode.contains(Constant.WEIBO_API_ERROR_ACCESS)){
					//将json转换成对象
					WeiboAPIBean weibo = (WeiboAPIBean) ConvertJsonAndObjectUtil
							.Json2Object(WeiboAPIBean.class, sourceCode);
					if(weibo.getError_code().equals(Constant.WEIBO_API_IP_LIMIT)){
						spiderResult.spiderResultError(
								SpiderResult.WEIBOAPI_IP_RESTRACTED,
								InfoMsg.E_API_IP_LIMIT, spiderResult);
						LoggerHandler.getInstance().writeLogger(InfoMsg.E_API_IP_LIMIT, Constant.LOG_LEVEL_ERROR);
						return true;
					}else if(weibo.getError_code().equals(Constant.WEIBO_API_ACCESS_TOKEN_LIMIT)){
						spiderResult.spiderResultError(
								SpiderResult.WEIBOAPI_TOKEN_RESTRACTED,
								InfoMsg.E_ACCESS_TOKEN_LIMIT, spiderResult);
						LoggerHandler.getInstance().writeLogger(InfoMsg.E_ACCESS_TOKEN_LIMIT, Constant.LOG_LEVEL_ERROR);
						return true;
					}else{
						spiderResult.spiderResultError(
								SpiderResult.WEIBOAPI_SYSTEM_ERROR,
								InfoMsg.E_API_SYSTEM_ERROR + weibo.getError_code()
										+ " : " + weibo.getError(), spiderResult);
						LoggerHandler.getInstance().writeLogger(
								InfoMsg.E_API_SYSTEM_ERROR + weibo.getError_code()
										+ " " + weibo.getError(),
								Constant.LOG_LEVEL_ERROR);
						return true;
					}
				}
			}catch (Exception e) {
				spiderResult.spiderResultError(SpiderResult.UNKNOW_ERROR, InfoMsg.E_UNKNOW_ERROR + e.getCause(), spiderResult);
				LoggerHandler.getInstance().writeLogger(InfoMsg.E_UNKNOW_ERROR+"--"+e.getCause() + ";" +e.getClass() , Constant.LOG_LEVEL_ERROR);
			}
		}
		return false;
	}
	
	public boolean UnusualShield(String sourceCode,String url) {
		return false;
	}
}
