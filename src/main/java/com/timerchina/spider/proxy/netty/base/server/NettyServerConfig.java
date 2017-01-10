package com.timerchina.spider.proxy.netty.base.server;

import com.timerchina.spider.proxy.netty.base.ANettyConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author jason.lin@timerchina.com 2014-11-3 上午11:27:48
 * 启动Netty需要的配置文件
 */
public class NettyServerConfig extends ANettyConfig {

	private int port;					//访问服务端的主机duankou
	private ChannelInitializer<SocketChannel> channelInitializer = null;	//各种事件处理类
	private NettyServerSSLChannelInit channelPipeline;		//当需要SSL协议的时候，子类可继承这个抽象类，初始化各种事件处理类
	private int bossGroupCount = 1;			//服务端定义需要多少Boss线程去接收客户端请求，work线程是Netty自动分配的
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public ChannelInitializer<SocketChannel> getChannelInitializer() {
		return channelInitializer;
	}
	public void setChannelInitializer(ChannelInitializer<SocketChannel> channelInitializer) {
		this.channelInitializer = channelInitializer;
	}
	public NettyServerSSLChannelInit getChannelPipeline() {
		return channelPipeline;
	}
	public void setChannelPipeline(NettyServerSSLChannelInit channelPipeline) {
		this.channelPipeline = channelPipeline;
	}
	public int getBossGroupCount() {
		return bossGroupCount;
	}
	public void setBossGroupCount(int bossGroupCount) {
		this.bossGroupCount = bossGroupCount;
	}
}
