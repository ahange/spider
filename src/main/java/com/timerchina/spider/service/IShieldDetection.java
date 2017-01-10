package com.timerchina.spider.service;

import com.timerchina.spider.bean.SpiderResult;

/**
 * @author jason.lin@timerchina.com
 * @time 2014-7-16 下午02:36:13
 */
public interface IShieldDetection {
	
	/**
	 * 通过检测源码，判断是否被屏蔽
	 * @return boolean
	 */
	public void isShield(String sourceCode, String url, SpiderResult spiderResult);
	
	/**
	 * 针对特殊屏蔽的方法
	 * @param sourceCode
	 * @return
	 */
	public boolean UnusualShield(String sourceCode,String url);
}
