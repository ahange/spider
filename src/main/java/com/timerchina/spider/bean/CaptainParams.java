package com.timerchina.spider.bean;
/**
 * @author jason.lin@timerchina.com 2014-11-18 下午1:27:20
 * 保存captain地址对象信息，包括海外抓取的信息
 */
public class CaptainParams {

	private String domain = "";					//Captain的域名
	private String captain_IP = "";				//Captain的ip地址
	private int port = 0;						//Captain的端口
	private int type = 0;						//节点的类型，0表示captain，1表示海外节点，2表示其他-未定
	private int failCount = 0;					//该节点访问失败的次数
	public CaptainParams(String domain, String captain_IP, int port) {
		this.domain = domain;
		this.captain_IP = captain_IP;
		this.port = port;
	}
	public CaptainParams() {
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getCaptain_IP() {
		return captain_IP;
	}
	public void setCaptain_IP(String captain_IP) {
		this.captain_IP = captain_IP;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getFailCount() {
		return failCount;
	}
	public void setFailCount() {
		this.failCount++;
	}
}
