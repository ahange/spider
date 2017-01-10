package com.timerchina.spider.proxy.netty.base;

/**
 * @author jason.lin@timerchina.com 2014-11-3 下午5:59:50
 * 作为服务端和客户端配置类的父类
 */
public abstract class ANettyConfig {

	private boolean keep_alive = false;
	private boolean tcp_nodelay = true;
	private int readTimeOut = 3000;
	public boolean isKeep_alive() {
		return keep_alive;
	}
	public void setKeep_alive(boolean keep_alive) {
		this.keep_alive = keep_alive;
	}
	public boolean isTcp_nodelay() {
		return tcp_nodelay;
	}
	public void setTcp_nodelay(boolean tcp_nodelay) {
		this.tcp_nodelay = tcp_nodelay;
	}
	public int getReadTimeOut() {
		return readTimeOut;
	}
	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}
}