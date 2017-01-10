package com.timerchina.spider.proxy;

import java.net.ConnectException;

import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;

/**
 * @author jason.lin@timerchina.com 2014-9-18 下午3:09:50
 * 使用代理进行抓取
 */
public interface IProxySpider {

	/**
	 * 返回用代理抓取的结果
	 * @param spiderParams
	 * @return
	 * @throws ConnectException 
	 */
	public abstract SpiderResult getProxyResult(SpiderParams spiderParams);
	
}