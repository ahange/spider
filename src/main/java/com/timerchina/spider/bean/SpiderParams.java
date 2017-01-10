package com.timerchina.spider.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.timerchina.spider.service.LoggerHandler;

/**
 * 外部请求Spider的所有参数设置
 * @author jasonlin@timerchina.com 2014-12-15 11:07:56
 * @version 4.0
 */
public class SpiderParams implements Serializable, Cloneable{
	
	private static final long serialVersionUID = 1L;
	private String url = "";					//抓取的网页的地址
	private boolean isOverseaURL = false;		//是否是境外抓取
	private String method = "GET";				//HTTP请求的方式(POST|GET)
	private String cookie = "";					//如果该网站需要cookie，则传进来值
	private String charset = "";				//网页的编码，默认值
	private String referer = "";				//定位网页来向连接
	private int httpConnectionTimeout = 2000;	//建立Http连接超时时间
	private int httpReadTimeout = 5000;			//http读取时间超时时间
	private boolean isProxy = false;			//是否需要使用代理抓取
	private boolean isAjax = false;				//用来进行ajax请求的
	private String user_agent = "";				//可配置化的浏览器客户端信息
	private String accept_language = "";		//设置请求头中Accept参数
	private String accept_encoding = "";		//设置请求头
	private Map<String,String> methodValues = null;	//post或者get请求需要的参数
//	private String stringentity = "";			//post请求时，有些请求直接需要String的请求body
	private Map<String, String> stringEntity = new HashMap<String, String>();			//使用StringEntity的请求
	private String domain = "";					//获取URL中的domain
	private String channelID;					//作为每个Channel的唯一标识
	private int soldierType = 1;				//爬虫请求的类型，1：默认普通爬虫；2：公司内部新浪微博接口；3：新浪URL
	private int connCaptainTimes = 2;			//请求连接Captain次数
	private String captainIP = "";				//请求爬虫中心的IP
	private int captainPORT = 0;				//请求爬虫中心的端口
	private boolean isLocal = false; 			//是否本地抓取
	private boolean isGzip = false;				//用户可选择是否压缩
	private Map<String,String> requestMap = null;//额外需要的请求参数
	private Map<String,String> responseMap = null;//返回额外的结果参数
	private String username = "";				//登录验证需要的用户名
	private String password = "";				//登录验证需要的密码
	private boolean isCleanHttp = false;		//是否清除http源码中的http标签
	private String httpProtocol = "";			//获取请求协议
	private ProxyParams proxyParams = null;		//用代理抓取的对象(这个代理就是跟VPN一样的功能)
	private boolean islogger = false;			//用户选择是否输出日志的类
	private Map<String, String> requestHeader = new HashMap<String, String>();	//针对特殊情况的请求头设置(奇葩网站)
	private boolean isRedirect = true;			//用户选择是否自动重定向
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	public SpiderParams(String url){
		handlerUrl(url);
	}
	public SpiderParams(String url,String cookie){
		handlerUrl(url);
		this.cookie = cookie;
	}
	public SpiderParams(ProxyParams proxyParams){
		this.proxyParams = proxyParams;
	}
	public SpiderParams() {
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		handlerUrl(url);
	}
	private void handlerUrl(String url){
		this.url = url;
	}
	public boolean isOverseaURL() {
		return isOverseaURL;
	}
	public void setOverseaURL(boolean isOverseaURL) {
		this.isOverseaURL = isOverseaURL;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public int getHttpConnectionTimeout() {
		return httpConnectionTimeout;
	}
	public void setHttpConnectionTimeout(int httpConnectionTimeout) {
		this.httpConnectionTimeout = httpConnectionTimeout;
	}
	public int getHttpReadTimeout() {
		return httpReadTimeout;
	}
	public void setHttpReadTimeout(int httpReadTimeout) {
		this.httpReadTimeout = httpReadTimeout;
	}
	public boolean isProxy() {
		return isProxy;
	}
	public void setProxy(boolean isProxy) {
		this.isProxy = isProxy;
	}
	public boolean isAjax() {
		return isAjax;
	}
	public void setAjax(boolean isAjax) {
		this.isAjax = isAjax;
	}
	public String getUser_agent() {
		if(user_agent.length() < 1){
			user_agent = Constant.USER_AGENT_V;
		}
		return user_agent;
	}
	public void setUser_agent(String user_agent) {
		this.user_agent = user_agent;
	}
	public Map<String, String> getMethodValues() {
		return methodValues;
	}
	public void setMethodValues(Map<String, String> methodValues) {
		this.methodValues = methodValues;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getChannelID() {
		return channelID;
	}
	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}
	public int getSoldierType() {
		return soldierType;
	}
	public void setSoldierType(int soldierType) {
		this.soldierType = soldierType;
	}
	public int getConnCaptainTimes() {
		return connCaptainTimes;
	}
	public void setConnCaptainTimes(int connCaptainTimes) {
		this.connCaptainTimes = connCaptainTimes;
	}
	public String getCaptainIP() {
		return captainIP;
	}
	public void setCaptainIP(String captainIP) {
		this.captainIP = captainIP;
	}
	public int getCaptainPORT() {
		return captainPORT;
	}
	public void setCaptainPORT(int captainPORT) {
		this.captainPORT = captainPORT;
	}
	public boolean isLocal() {
		return isLocal;
	}
	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}
	public boolean isGzip() {
		return isGzip;
	}
	public void setGzip(boolean isGzip) {
		this.isGzip = isGzip;
	}
	public String getAccept_language() {
		if(accept_language.length() < 1){
			accept_language = Constant.ACCEPT_LANGUAGE_V;
		}
		return accept_language;
	}
	public void setAccept_language(String accept_language) {
		this.accept_language = accept_language;
	}
	public Map<String, String> getRequestMap() {
		return requestMap;
	}
	public void setRequestMap(Map<String, String> requestMap) {
		this.requestMap = requestMap;
	}
	public Map<String, String> getResponseMap() {
		return responseMap;
	}
	public void setResponseMap(Map<String, String> responseMap) {
		this.responseMap = responseMap;
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
	public boolean isCleanHttp() {
		return isCleanHttp;
	}
	public void setCleanHttp(boolean isCleanHttp) {
		this.isCleanHttp = isCleanHttp;
	}
	public String getHttpProtocol() {
		return httpProtocol;
	}
	public ProxyParams getProxyParams() {
		return proxyParams;
	}
	public void setProxyParams(ProxyParams proxyParams) {
		this.proxyParams = proxyParams;
	}
	public boolean isIslogger() {
		return islogger;
	}
	public void setlogger(boolean islogger) {
		if(islogger){
			LoggerHandler.LOG_ON = true;
			this.islogger = islogger;
		}
	}
	public Map<String, String> getRequestHeader() {
		return requestHeader;
	}
	public void setRequestHeader(Map<String, String> requestHeader) {
		this.requestHeader = requestHeader;
	}
	public boolean isRedirect() {
		return isRedirect;
	}
	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}
//	public String getStringentity() {
//		return stringentity;
//	}
//	public void setStringentity(String stringentity) {
//		this.stringentity = stringentity;
//	}
	public Map<String, String> getStringEntity() {
		return stringEntity;
	}
	public String getAccept_encoding() {
		return accept_encoding;
	}
	public void setAccept_encoding(String accept_encoding) {
		this.accept_encoding = accept_encoding;
	}
	public void setStringEntity(Map<String, String> stringEntity) throws Exception {
		if(stringEntity != null && stringEntity.size() <= 1){
			this.stringEntity = stringEntity;
		}else{
			throw new Exception("stringEntity out of range, which are limited to the size of 1.");
		}
	}
}
