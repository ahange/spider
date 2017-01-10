package com.timerchina.spider.bean;
/**
 * 	@author jason.lin@timerchina.com 2014-9-3 下午7:03:43
 *	保存爬虫中心分配给的信息
 *	@proxyIp 代理中心分配给的ip
 *	@proxyPort 代理服务器的端口
 *	@username 登陆代理的账号
 *	@password 登陆代理的密码
 *	@waittime 等待时间
 *	@scheduleId 爬虫中心的任务id
 */
public class ProxyParams {

//	private String spiderId = "";				//爬虫中心分配的一个id号
	private String ip = "";						//代理中心分配给的ip
	private String username = "";				//登陆代理的账号
	private String password = "";				//登陆代理的密码
	private int port = 0;					//代理服务器的端口
//	private String weiboport = "";				//新浪微博端口
//	private String scheduleId ="";				//爬虫中心的任务id
//	private String waitTime = "";				//爬虫中心返回等待时间
	public ProxyParams() {}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
