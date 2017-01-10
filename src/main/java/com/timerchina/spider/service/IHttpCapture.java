package com.timerchina.spider.service;

import java.io.IOException;
import java.net.MalformedURLException;
import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;

/**
 * @author jason.lin@timerchina.com
 * @time 2014-7-18 上午11:26:34
 */
public interface IHttpCapture {
	public static final String SET_COOKIE = "Set-Cookie";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String GZIP = "gzip";
	public static final String USER_AGENT = "User-Agent";
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String ACCEPT_LANGUAGE = "Accept-Language";
	public static final String CONNECTION = "Connection";
	public static final String ACCEPT = "Accept";
	
	/**
	 * 初始化Http连接
	 * @param spiderParams
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public abstract Object initHTTP(SpiderParams spiderParams,Object object) throws MalformedURLException, IOException;

	/**
	 * 获取源码
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public abstract SpiderResult captureWebPage(SpiderParams spiderParams) throws IOException;
}