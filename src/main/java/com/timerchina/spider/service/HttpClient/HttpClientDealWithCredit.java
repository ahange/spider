package com.timerchina.spider.service.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.zip.GZIPInputStream;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;

/**
 * @author jason.lin@timerchina.com 2014-12-24 下午2:21:37
 * 这里类的目的就是用来需要登录验证的网站访问的
 */
public class HttpClientDealWithCredit {
//	private static CloseableHttpClientCapture capture = new CloseableHttpClientCapture();
	private static PackagingSpiderResult packaging = new PackagingSpiderResult();
	
	/**
	 * 返回结果
	 * @param spiderParams
	 * @return
	 */
	public SpiderResult returnResult(SpiderParams spiderParams){
		
		SpiderResult spiderResult = new SpiderResult();
		HttpClient client = new HttpClient();
		try {
			client.getHttpConnectionManager().getParams()
					.setSoTimeout(spiderParams.getHttpConnectionTimeout());
			
			//验证信息和登录的账号密码,参考：http://blog.csdn.net/yawinstake/article/details/6730207
			Credentials defaultcreds = new UsernamePasswordCredentials(
					spiderParams.getUsername(), spiderParams.getPassword());
			client.getState().setCredentials(AuthScope.ANY, defaultcreds);
			client.getParams().setAuthenticationPreemptive(true);
			
			//目前只提供Get方法
			GetMethod get = new GetMethod(spiderParams.getUrl());
			
			//下面设置读取超时时间
			HttpMethodParams params = new HttpMethodParams();
			params.setSoTimeout(spiderParams.getHttpReadTimeout());
			get.setParams(params);
			get.setDoAuthentication(true);
			initRequestProperties(Constant.COOKIE, spiderParams.getCookie(),get);
			initRequestProperties(Constant.REFERER, spiderParams.getReferer(),get);
			if(spiderParams.isAjax()){
				initRequestProperties(Constant.AJAX_KEY, Constant.AJAX_VALUE,get);
			}
		
			//执行程序，获取源码
			int responseCode = client.executeMethod(get);
			spiderResult = getSpiderResult(get, client, spiderParams);
			spiderResult.setResponseCode(responseCode);
		} catch (Exception e) {
			spiderResult.setERRORMsg("获取源码失败："+e.getMessage());
			spiderResult.setResponseCode(SpiderResult.CAPTURE_SOURCECODE_FAILED);
		}
		return spiderResult;
	}
	
	/**
	 * 设置cookie或者referer
	 * @param propertiesKey
	 * @param propertiesValue
	 * @param object
	 */
	private void initRequestProperties(String propertiesKey,String propertiesValue, HttpMethod method) {
		if(propertiesValue !=null && propertiesValue.length()>1){
			method.setRequestHeader(propertiesKey, propertiesValue);
		}
	}
	
	/**
	 * 建立链接,获取源码
	 * @param method
	 * @param httpClient
	 * @param spiderParams
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private SpiderResult getSpiderResult(HttpMethod method, HttpClient httpClient, SpiderParams spiderParams) throws MalformedURLException,IOException{
		SpiderResult spiderResult = new SpiderResult();
		InputStream inputStream = null;
		try{
			if(method==null){
				return spiderResult;
			}else if(method.getResponseHeader(Constant.SET_COOKIE) != null){
				String cookie = method.getResponseHeader(Constant.SET_COOKIE).getValue();
				spiderResult.setCookies(cookie);
			}
			inputStream = method.getResponseBodyAsStream();
			if(inputStream != null){
				//判断网页源码是否是GZip压缩的
				if (method.getResponseHeader(Constant.CONTENT_ENCODING) != null && 
						method.getResponseHeader(Constant.CONTENT_ENCODING).getValue().toLowerCase().indexOf(Constant.GZIP) > -1) {
					GZIPInputStream gzin = new GZIPInputStream(inputStream);
					packaging.encodingDetect(gzin, spiderParams, spiderResult);
				} else {
					packaging.encodingDetect(inputStream, spiderParams,spiderResult);
				}
				spiderResult.setResponseCode(method.getStatusCode());
			}
		}finally{
			try {
				if(inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				
			}
		}
		return spiderResult;
	}
}
