package com.timerchina.spider.service.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.bean.InfoMsg;
import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.service.IHttpCapture;
import com.timerchina.spider.service.LoggerHandler;

/**
 * @author jason.lin@timerchina.com 2014-12-4 下午3:08:08
 * @statement HttpClient访问类，负责Http请求，获取源码和源码解析
 */
public class CloseableHttpClientCapture implements IHttpCapture {

	private static DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(
			0, false);
	private volatile BasicCookieStore cookieStore;
	private static CloseableHttpClient httpclient = null;
//	public static int redirectCount = 1;
//	private static PoolingHttpClientConnectionManager connManager;
//	private static HttpAuthentication httpAuthen = new HttpAuthentication();
	
	public CloseableHttpClientCapture(){
		//指定cookie存储对象
		cookieStore = new BasicCookieStore();
		//设置连接管理器,针对数字证书问题
//		connManager = httpAuthen.selfSignedCertificate();
		initCloseableHttpClient();
	}
	
	/**
	 * 对HttpClient初始化，内部实现单例
	 */
	private void initCloseableHttpClient(){
		httpclient = HttpClients.custom()
			.setDefaultCookieSpecRegistry(initCookieSpecRegistry())
			.setDefaultRequestConfig(setGlobalRequestConfig())
			.setRetryHandler(retryHandler)
			.setDefaultCookieStore(cookieStore)
//			.setConnectionManager(connManager)
			.build();
	}
	
	public Object initHTTP(SpiderParams spiderParams, Object object)
			throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 初始化HttpGet请求
	 * @param spiderParams
	 * @return
	 */
	public HttpGet initHttpGet(SpiderParams spiderParams) throws IllegalArgumentException{
		HttpGet httpGet = new HttpGet(spiderParams.getUrl());
		httpGet.setConfig(setRequestConfig(spiderParams));
		if(spiderParams.isAjax()){
			//针对Ajax的请求，需要设置的参数
			httpGet.setHeader(initHttpHeader(Constant.AJAX_KEY, Constant.AJAX_VALUE));
		}
		for(Map.Entry<String, String> entry : spiderParams.getRequestHeader().entrySet()) {
			httpGet.addHeader(entry.getKey(), entry.getValue());
		}
		httpGet.addHeader(initHttpHeader(Constant.REFERER, spiderParams.getReferer()));
		httpGet.addHeader(initHttpHeader(Constant.COOKIE, spiderParams.getCookie()));
		httpGet.addHeader(initHttpHeader(Constant.USER_AGENT, spiderParams.getUser_agent()));
		for(Map.Entry<String, String> entry : spiderParams.getRequestHeader().entrySet()) {
			httpGet.addHeader(entry.getKey(), entry.getValue());
		}
//		httpGet.addHeader(initHttpHeader(Constant.ACCEPT_ENCODING, spiderParams.getAccept_encoding()));
//		httpGet.addHeader(initHttpHeader(Constant.ACCEPT_LANGUAGE, spiderParams.getAccept_language()));
		return httpGet;
	}

	/**
	 * 初始化HttpPost请求
	 * @param spiderParams
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public HttpPost initHttpPost(SpiderParams spiderParams)
			throws UnsupportedEncodingException, IllegalArgumentException {
		HttpPost httpPost = new HttpPost(spiderParams.getUrl());
		httpPost.setConfig(setRequestConfig(spiderParams));
		Map<String, String> paramsMap = spiderParams.getMethodValues();
		if ((paramsMap != null && paramsMap.size() > 0)) {
			//设置POST请求需要的参数
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (paramsMap != null && paramsMap.size() > 0) {
				Set<Entry<String, String>> paramsEntry = paramsMap.entrySet();
				for (Entry<String, String> entry : paramsEntry) {
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		}
		
		for(Map.Entry<String, String> entry : spiderParams.getRequestHeader().entrySet()) {
			httpPost.addHeader(entry.getKey(), entry.getValue());
		}
		
		if(spiderParams.getStringEntity() != null && spiderParams.getStringEntity().size() > 0){
			for (Entry<String, String> entity: spiderParams.getStringEntity().entrySet()) {
				StringEntity entityStr = new StringEntity(entity.getKey());
				entityStr.setContentType(entity.getValue());
				httpPost.setEntity(entityStr);
				break;
			};
		}
		//下面设置请求的cookie和referer
		httpPost.addHeader(initHttpHeader(Constant.COOKIE, spiderParams.getCookie()));
		httpPost.addHeader(initHttpHeader(Constant.REFERER, spiderParams.getReferer()));
		if(spiderParams.isAjax()){
			//针对Ajax的请求，需要设置的参数
			httpPost.setHeader(initHttpHeader(Constant.AJAX_KEY, Constant.AJAX_VALUE));
		}
		httpPost.setHeader(initHttpHeader(Constant.ACCEPT_LANGUAGE, spiderParams.getAccept_language()));
		httpPost.addHeader(initHttpHeader(Constant.USER_AGENT, spiderParams.getUser_agent()));
		httpPost.addHeader(initHttpHeader(Constant.ACCEPT_ENCODING, spiderParams.getAccept_encoding()));
		return httpPost;
	}
	
	/**
	 * 设置HttpHeader请求头信息
	 * @param key
	 * @param value
	 * @return
	 */
	public static Header initHttpHeader(String key, String value){
		if(value!=null && value.length()>0){
			Header header = new BasicHeader(key, value);
			return header;
		}
		return null;
	}

	/**
	 * 初始化一个HttpClient对象，通过HttpClientBuilder创建
	 * @return
	 */
	public static Registry<CookieSpecProvider> initCookieSpecRegistry() {
		// 适应新的Cookie协议
		CookieSpecProvider easySpecProvider = new CookieSpecProvider() {
			public CookieSpec create(HttpContext context) {
				return new BrowserCompatSpec() {
					@Override
					public void validate(Cookie cookie, CookieOrigin origin)
							throws MalformedCookieException {
						//Do nothing. Just for Cookie Reject Error.
					}
				};
			}
		};

		Registry<CookieSpecProvider> registry = RegistryBuilder
				.<CookieSpecProvider> create()
				.register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
				.register(CookieSpecs.BROWSER_COMPATIBILITY,
						new BrowserCompatSpecFactory())
				.register("easy", easySpecProvider).build();
		return registry;
	}

	/**
	 * 设置所有Http请求公用的平配置对象
	 * @return
	 */
	public static RequestConfig setGlobalRequestConfig(){
		RequestConfig defaultRequestConfig = RequestConfig
				.custom()
				.setCookieSpec("easy")
				.setExpectContinueEnabled(true)
				.setStaleConnectionCheckEnabled(true)
				.setTargetPreferredAuthSchemes(
						Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
				.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
				.build();
		return defaultRequestConfig;
	} 
	
	/**
	 * 针对每次Http请求需要设置的一些参数
	 * @param spiderParams
	 * @return
	 */
	public RequestConfig setRequestConfig(SpiderParams spiderParams) {
		RequestConfig requestConfig = RequestConfig.copy(setGlobalRequestConfig())
				//连接成功后，读取IO超时或者两次读写之间的间隔时间超时
                .setSocketTimeout(spiderParams.getHttpReadTimeout())
                //一个请求到与服务器建立连接的超时时间
                .setConnectTimeout(spiderParams.getHttpConnectionTimeout())
                .setConnectionRequestTimeout(5000)
                .setRedirectsEnabled(spiderParams.isRedirect())
                .setCircularRedirectsAllowed(true)
                .setMaxRedirects(100)
                .build();
		return requestConfig;
	}
	
	/**
	 * 对网站需要http basic认证的
	 */
	public void httpBasic(){
//		if ((spiderParams.getUsername() != null && spiderParams.getUsername()
//		.length() > 1)
//		&& spiderParams.getPassword() != null
//		&& spiderParams.getPassword().length() > 1) {
//	
//	HttpClientDealWithCredit credit = new HttpClientDealWithCredit();
//	return credit.returnResult(spiderParams);
//}
	}
	
	/**
	 * 主调度方法
	 */
	public SpiderResult captureWebPage(SpiderParams spiderParams)
			throws IOException {
		CloseableHttpResponse response = null;
		InputStream inputStream = null;
		SpiderResult spiderResult = new SpiderResult();
		HttpClientContext httpClientContext = new HttpClientContext();
		PackagingSpiderResult packageing = new PackagingSpiderResult();
		try {
			initHttpClientContext(httpClientContext, spiderParams);
			if (spiderParams.getMethod().equalsIgnoreCase(Constant.POST_METHOD)) {
				response = httpclient.execute(initHttpPost(spiderParams),
						httpClientContext);
			} else {
				response = httpclient.execute(initHttpGet(spiderParams),httpClientContext);
			}
			
			int responseCode = response.getStatusLine().getStatusCode();
			spiderResult.setHeaders(packageing.packagingHeader(response));
			if (spiderResult.getHeaders().get(Constant.SET_COOKIE) != null) {
				spiderResult.setCookies(spiderResult.getHeaders().get(Constant.SET_COOKIE));
			}
			spiderResult = packageing.packagingSpiderResult(response, httpClientContext,
							spiderParams, responseCode, spiderResult);
			return spiderResult;
		} catch (ConnectTimeoutException e) {
			LoggerHandler.getInstance().writeLogger(
					InfoMsg.I_HTTP_CONN_TIMEOUT + e.getCause(),
					Constant.LOG_LEVEL_ERROR);
			spiderResult.spiderResultError(SpiderResult.HTTP_CONN_TIMEOUT,
					InfoMsg.I_HTTP_CONN_TIMEOUT
							+ spiderParams.getHttpConnectionTimeout() + "s, 原因："
							+ e.getCause(), spiderResult);
			return spiderResult;
		} catch (SocketTimeoutException e) {
			LoggerHandler.getInstance().writeLogger(
					InfoMsg.I_HTTP_READ_TIMEOUT + e.getCause(),
					Constant.LOG_LEVEL_ERROR);
			spiderResult.spiderResultError(SpiderResult.HTTP_READ_TIMEOUT,
					InfoMsg.I_HTTP_READ_TIMEOUT
							+ spiderParams.getHttpConnectionTimeout() + ", "
							+ spiderParams.getUrl() + ";  原因：" + e.getCause(),spiderResult);
			return spiderResult;
		} catch (IllegalArgumentException e) {
			LoggerHandler.getInstance().writeLogger(
					InfoMsg.E_URL_ILLEGAL_CHARACTER + e.getCause(),
					Constant.LOG_LEVEL_ERROR);
			spiderResult.spiderResultError(SpiderResult.URL_ILLEGAL_CHARACTER,
					InfoMsg.E_URL_ILLEGAL_CHARACTER + "  原因：" + e.getCause(),spiderResult);
			return spiderResult;
		} catch (Exception e) {
			return spiderResult;
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	/**
	 * 对HttpClientContext进行初始化，主要是设置代理抓取信息，或者HttpBasic验证等信息
	 * @author jason.lin@timerchina.com
	 * @date 2015-01-28
	 * @param 
	 */
	public void initHttpClientContext(HttpClientContext httpClientContext, SpiderParams spiderParams){
		if(spiderParams.getProxyParams()!=null){
			HttpAuthentication authentication = new HttpAuthentication();
			authentication.requestWithProxy(httpClientContext, spiderParams.getProxyParams());
		}
	}
	
	/**
	 * 对URL进行转码，包括内部存在的中文和需要转义的字符，默认转码为utf-8
	 * @param url
	 * @param urlCharset
	 * @return
	 */
	public static String recombinationUrl(String url, String urlCharset){
		URIBuilder builder = null;
		try {
			builder = new URIBuilder(url);
		} catch (Exception e) {
			LoggerHandler.getInstance().writeLogger(InfoMsg.E_URL_ANALYZE_ERROR, Constant.LOG_LEVEL_ERROR);
			return url;
		}
		return builder.toString();
	}
}