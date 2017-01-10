package com.timerchina.spider.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回给调用者结果，可封装成JSON或者XML等等
 * @author jimy.jiang@timerchina.com
 * @author jason.lin@timerchina.com
 * @time 2014-7-16 下午01:46:37
 * @html			返回的网页源码
 * @responseCode			返回状态码
 * @realUrl		返回实际抓取的地址
 * @startTime		返回抓取的开始时间
 * @endTime		返回抓取的结束时间
 * @charSet		返回网页源码的编码
 * @graspMode 	抓取方式(0：本地抓取；1：主调度中心抓取；2：备用调度中心抓取；)
 */
public class SpiderResult implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String html = "";				//返回的网页源码
    private int responseCode = 0;			//返回状态码 901代表建立HTTP连接失败
    private String realUrl = "";			//返回实际抓取的地址
    private String charSet = "";			//返回网页源码的编码
    private String cookies = "";			//返回网站的cookie
    private String channelID = "";			//保存ChannelId
    private String ERRORMsg = "";			//保存错误信息
    private boolean isShield = false;		//是否被屏蔽
    private Map<String,String> headers = new HashMap<String, String>();				//保存头信息
    
    //定义状态码,6开头都都是参数错误
    public static final int ISPROXY_ISOVERSEA_EQUALS_TRUE_ERROR = 600;	//本地抓取的时候，设置了两个参数为true
    public static final int ISPROXY_EQUALS_TRUE_ERROR = 601;		//海外抓取的时候，设置了isProxy为true
    public static final int ISOVSESEA_EQUALS_TRUE_ERROR = 602;		//调用Captain或者本地抓取isOversea不能为true
    public static final int URL_ILLEGAL = 603;						//URL验证不合法，检查URL
    public static final int UNKNOW_HOST_ERROR = 604;				//未知的主机名，检查URL
    public static final int SOURCECODE_IS_NULL = 605;				//获取的源码为空
    public static final int UNKNOW_ERROR = 606;						//发生未知错误
    
    public static final int IP_OR_COOKIE_SHIELD = 800;				//被屏蔽
    //下面是超时
    public static final int HTTP_CONN_TIMEOUT = 801;				//HttpClient连接超时
    public static final int HTTP_READ_TIMEOUT = 802;				//读取超时，获取的源码就为空
    
    public static final int ENCODING_DETECT_ERROE = 803;			//编码识别失败
    public static final int ENCRIPTION_ERROR = 804;					//加密解密错误
    public static final int UNGZIP_FAIL = 805;						//解压源码出错
    public static final int CAPTURE_SOURCECODE_FAILED = 806;		//获取源码失败
    public static final int WEIBOAPI_IP_RESTRACTED = 807;			//新浪微博API IP请求频次超过上限
    public static final int WEIBOAPI_TOKEN_RESTRACTED = 808;		//新浪微博API access_token限制
    public static final int WEIBOAPI_SYSTEM_ERROR = 809;			//新浪微博API 系统错误(除了上面以外的其他错误)
    public static final int URL_ILLEGAL_CHARACTER = 810;			//url中包含特殊字符（中文或者url特殊字符）
    public static final int URL_ANALYZE_ERROR = 811;				//url解析错误
    
    
    //9开头，访问爬虫中心的问题
    public static final int NETWORD_UNREACHABLE = 900;
    public static final int CANNOT_CONN_CAPTAIN = 901;
    public static final int READ_CAPTAIN_TIMEOUT = 902;
    public static final int CAPTURE_SOURCECODE_FAIL = 903;
    
    //定义HTTP自带的状态码
    public static final int HTTP_SC = 200;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;
    
	public SpiderResult() {
	}
	public SpiderResult(String html, int responseCode, String realUrl,
			long startTime, long endTime, String charSet) {
		super();
		this.html = html;
		this.responseCode = responseCode;
		this.realUrl = realUrl;
		this.charSet = charSet;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getRealUrl() {
		return realUrl;
	}
	public void setRealUrl(String realUrl) {
		this.realUrl = realUrl;
	}
	public String getCharSet() {
		return charSet;
	}
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}
	public String getCookies() {
		return cookies;
	}
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}
	public String getChannelID() {
		return channelID;
	}
	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}
	public String getERRORMsg() {
		return ERRORMsg;
	}
	public void setERRORMsg(String eRRORMsg) {
		ERRORMsg = eRRORMsg;
	}
	public boolean isShield() {
		return isShield;
	}
	public void setShield(boolean isShield) {
		this.isShield = isShield;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	/**
	 * 设置SpiderResult错误信息
	 * @param responseCode
	 * @param errorMsg
	 * @return
	 */
	public void spiderResultError(int responseCode, String errorMsg,
			SpiderResult spiderResult) {
		spiderResult.setResponseCode(responseCode);
		spiderResult.setERRORMsg(errorMsg);
	}
}
