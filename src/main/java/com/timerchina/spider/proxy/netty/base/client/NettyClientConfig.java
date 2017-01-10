package com.timerchina.spider.proxy.netty.base.client;

import com.timerchina.spider.proxy.netty.base.ANettyConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author jason.lin@timerchina.com 2014-11-3 上午11:27:48
 * 启动Netty需要的配置文件
 */
public class NettyClientConfig extends ANettyConfig {

	private int port;					//访问服务端的主机duankou
	private String ip;					//访问服务端的IP地址
	private ChannelInitializer<SocketChannel> channelInitializer = null;	//各种事件处理类
	private NettyClientSSLChannelInit channelPipeline;		//当需要SSL协议的时候，子类可继承这个抽象类，初始化各种事件处理类
	private boolean isBlock = false;						//是否使用阻塞方式获得结果
	private int connectTimeOut = 3000;						//连接超时
	private boolean isDirSendRes = false;					//当客户captain接收到结果后，是否直接返回结果
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public ChannelInitializer<SocketChannel> getChannelInitializer() {
		return channelInitializer;
	}
	public void setChannelInitializer(ChannelInitializer<SocketChannel> channelInitializer) {
		this.channelInitializer = channelInitializer;
	}
	public NettyClientSSLChannelInit getChannelPipeline() {
		return channelPipeline;
	}
	public void setChannelPipeline(NettyClientSSLChannelInit channelPipeline) {
		this.channelPipeline = channelPipeline;
	}
	public boolean isBlock() {
		return isBlock;
	}
	public void setBlock(boolean isBlock) {
		this.isBlock = isBlock;
	}
	public int getConnectTimeOut() {
		return connectTimeOut;
	}
	public void setConnectTimeOut(int connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
	}
	public boolean isDirSendRes() {
		return isDirSendRes;
	}
	public void setDirSendRes(boolean isDirSendRes) {
		this.isDirSendRes = isDirSendRes;
	}
}
