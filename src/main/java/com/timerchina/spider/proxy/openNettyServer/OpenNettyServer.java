package com.timerchina.spider.proxy.openNettyServer;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import com.timerchina.spider.proxy.netty.base.server.NettyServer;
import com.timerchina.spider.proxy.netty.base.server.NettyServerConfig;
import com.timerchina.spider.proxy.netty.handler.NettyServerChannelInitializer;


/**
 * @author jason.lin@timerchina.com 2014-11-19 下午1:35:18
 * 开启Netty的服务端，接受请求
 */
public class OpenNettyServer {

	public static void main(String[] args) {
		
		NettyServerConfig config = new NettyServerConfig();
		config.setChannelInitializer(new NettyServerChannelInitializer());
		config.setPort(11110);
		
		NettyServer server = null;
		try {
			server = new NettyServer(config);
			server.start();
		} catch (CertificateException e) {
			
		} catch (SSLException e) {
			
		} catch (InterruptedException e) {
			
		}
	}
}
