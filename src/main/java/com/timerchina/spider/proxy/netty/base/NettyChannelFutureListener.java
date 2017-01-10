package com.timerchina.spider.proxy.netty.base;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;


/**
 * @author jason.lin@timerchina.com 2014-11-3 下午3:07:01
 *
 */
public class NettyChannelFutureListener implements GenericFutureListener<Future<? super Void>>{

	public void operationComplete(Future<? super Void> arg0) throws Exception {
		
	}
}
