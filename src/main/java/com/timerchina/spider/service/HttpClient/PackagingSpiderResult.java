package com.timerchina.spider.service.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.util.EntityUtils;

import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.bean.InfoMsg;
import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.service.LoggerHandler;
import com.timerchina.spider.service.encoding.BytesEncodingDetect;

/**
 * @author jason.lin@timerchina.com 2015-1-22 下午2:39:16
 *
 */
public class PackagingSpiderResult {
	
	public static final int HTTP_CODE_SUCCESS = 200;
	
	/**
	 * 对获取的结果进行封装处理，这是结果处理
	 * @param response
	 * @param httpClientContext
	 * @param spiderParams
	 * @return
	 * @throws IOException 
	 */
	public SpiderResult packagingSpiderResult(CloseableHttpResponse response,
			HttpClientContext httpClientContext, SpiderParams spiderParams,
			int responseCode, SpiderResult spiderResult) throws IOException {
		InputStream inputStream = null;
		//获取请求结果,解码解压
		HttpEntity httpEntity = response.getEntity();
		try{
			inputStream = httpEntity.getContent();
			if(inputStream != null){
				spiderResult = getSpiderReusltStr(spiderParams,inputStream,response,spiderResult);
			}else{
				LoggerHandler.getInstance().writeLogger(InfoMsg.E_SOURCECODE_FAIL_URL, Constant.LOG_LEVEL_INFO);
				spiderResult.spiderResultError(SpiderResult.SOURCECODE_IS_NULL,
						InfoMsg.E_SOURCECODE_FAIL_URL + spiderParams.getUrl(),spiderResult);
				return spiderResult;
			}
			
			//如果重定向，则可以获取重定向之后的URL地址
			List<URI> url = httpClientContext.getRedirectLocations();
			if(url != null && url.size() > 0){
				spiderResult.setRealUrl(url.get(url.size()-1).toString());
			}else{
				spiderResult.setRealUrl(spiderParams.getUrl());
			}
			spiderResult.setResponseCode(responseCode);
		}catch (IOException e) {
			LoggerHandler.getInstance().writeLogger(e.getCause().toString(), Constant.LOG_LEVEL_ERROR);
			spiderResult.spiderResultError(SpiderResult.HTTP_READ_TIMEOUT, e.getCause().toString(), spiderResult);
		}finally{
			if(inputStream!=null){
				inputStream.close();
			}
		}
		return spiderResult;
	}
	
	/**
	 * 封装header头信息
	 * @return
	 */
	public Map<String, String> packagingHeader(CloseableHttpResponse response){
		Map<String, String> headerMap = new HashMap<String, String>();
		Header[] headers = response.getAllHeaders();
		if(headers != null && headers.length > 0){
			StringBuffer cookie = new StringBuffer();
			for (Header header : headers) {
				if(header.getName().equalsIgnoreCase(Constant.SET_COOKIE)){
					cookie.append(header.getValue()+";");
				}else{
					headerMap.put(header.getName(), header.getValue());
				}
			}
			headerMap.put(Constant.SET_COOKIE, cookie.toString());
		}
		return headerMap;
	}
	
	/**
	 * 获取IO流，之后进行编码解析，返回正确的编码和源码
	 * @param spiderParams
	 * @param in
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private SpiderResult getSpiderReusltStr(SpiderParams spiderParams,
			InputStream in, CloseableHttpResponse response, SpiderResult spiderResult) throws IOException {
		//Content-Encoding
		Header[] headers = response.getHeaders(Constant.CONTENT_ENCODING);
		StringBuffer sb = new StringBuffer();
		for (Header header : headers) {
			sb.append(header.getValue());
		}
		//判断网页源码是否是GZip压缩的
		if (sb != null && sb.toString().toLowerCase().indexOf(Constant.GZIP) > -1) {
			GZIPInputStream gzin = new GZIPInputStream(in);
			spiderResult = encodingDetect(gzin, spiderParams,spiderResult);
		} else {
			spiderResult = encodingDetect(in, spiderParams,spiderResult);
		}
		return spiderResult;
	}
	
	/**
	 * 编码识别，jasonlin 2015-01-13 14:12:53
	 * @param sourceCode
	 * @param spiderParams
	 * @param spiderResult
	 * @return
	 */
	public SpiderResult encodingDetect(InputStream sourceCode,
			SpiderParams spiderParams, SpiderResult spiderResult){
//		if(sourceCode != null){
//			try {
//				System.out.println(sourceCode.available());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		byte[] byteArrays = new byte[40000];
		boolean isCharsetUseful = false;
		int offerSet = -1;
		int actArrayLength = 0;
		String charset = spiderParams.getCharset();
		List<byte[]> byteList = new ArrayList<byte[]>();
		byte[] byteActArrays = null;
		try {
			while((offerSet = sourceCode.read(byteArrays)) != -1){
				byte[] byteCopy = Arrays.copyOf(byteArrays, offerSet);
				byteList.add(byteCopy);
			}
			for (byte[] b : byteList) {
				actArrayLength += b.length;
			}
			byteActArrays = new byte[actArrayLength];
			actArrayLength = 0;
			for (byte[] b : byteList) {
				System.arraycopy(b, 0, byteActArrays, actArrayLength, b.length);
				actArrayLength += b.length;
			}
			
			if(charset != null && charset.length() > 0){
				if(Charset.isSupported(charset)){
					isCharsetUseful = true;
				}
			}
			
			if(!isCharsetUseful){
				BytesEncodingDetect encodingDetect = new BytesEncodingDetect();
				charset = BytesEncodingDetect.javaname[encodingDetect.detectEncoding(byteActArrays)];
			}
			LoggerHandler.getInstance().writeLogger(InfoMsg.I_CHARSET_AFTER + charset + "\t" + spiderParams.getUrl(), Constant.LOG_LEVEL_INFO);
			spiderResult.setHtml(new String(byteActArrays, charset));
			spiderResult.setCharSet(charset);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			spiderResult.spiderResultError(SpiderResult.ENCODING_DETECT_ERROE, InfoMsg.E_ENCODING_DETECT_ERROR + e.getCause(), spiderResult);
		}catch (IOException e) {
			
			
			
			e.printStackTrace();
			LoggerHandler.getInstance().writeLogger(InfoMsg.E_ENCODING_DETECT_ERROR + e.getCause(), Constant.LOG_LEVEL_ERROR);
			spiderResult.spiderResultError(SpiderResult.ENCODING_DETECT_ERROE, InfoMsg.E_ENCODING_DETECT_ERROR + e.getCause(), spiderResult);
		}
		return spiderResult;
	}
	
	public static void main(String[] args) {
		
		InputStream in = null;
		
		new PackagingSpiderResult().encodingDetect(in , new SpiderParams(), new SpiderResult());
	}
	
	
}
