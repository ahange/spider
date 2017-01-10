package com.timerchina.spider.bean;
/**
 * @author jason.lin@timerchina.com 2014-12-19 上午11:23:09
 * @statement 这是针对新浪微博API的错误信息
 * @example {"error":"invalid_access_token","error_code":21332,"request":"/2/account/rate_limit_status.json"}
 */
public class WeiboAPIBean {

	private String error = "";
	private String error_code = "";
	private String request = "";
	public WeiboAPIBean(String error, String error_code, String request) {
		super();
		this.error = error;
		this.error_code = error_code;
		this.request = request;
	}
	public WeiboAPIBean() {
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
}
