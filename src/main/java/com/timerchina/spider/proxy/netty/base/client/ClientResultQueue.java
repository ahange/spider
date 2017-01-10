package com.timerchina.spider.proxy.netty.base.client;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author jason.lin@timerchina.com 2014-11-21 上午10:59:23
 *
 */
public class ClientResultQueue {
	private static BlockingQueue<Object> responseRes = new ArrayBlockingQueue<Object>(1);
	
	/**
	 * 当用户选择阻塞Netty模式的时候，通过这个方法获取结果
	 * @return
	 * @throws InterruptedException 
	 */
	public static Object getFactorial(int timeout) throws InterruptedException{
		return responseRes.poll(timeout,TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 往结果队列中插入一条数据
	 * @param object
	 */
	public static void setFactorial(Object object){
		responseRes.add(object);
	}
}
