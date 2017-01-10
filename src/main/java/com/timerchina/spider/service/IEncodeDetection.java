package com.timerchina.spider.service;

import java.io.UnsupportedEncodingException;

/**
 * @author jason.lin@timerchina.com
 * @time 2014-7-17 下午05:28:54
 */
public interface IEncodeDetection {

	/**
	 * 外部调用的方法，获取网页的正确编码
	 * @param sourceCode
	 * @param userCharset
	 * @param charList
	 * @return 正确的编码
	 * @throws UnsupportedEncodingException 
	 */
	public String getCorrectEncoding(String sourceCode, String charsetByhead) throws UnsupportedEncodingException;
	
	/**
	 * 验证编码之后的网页源码是否存在乱码
	 * @param sourceCode
	 * @return true|false
	 */
	public boolean isCorrectEncoding(String sourceCode);
	
	/**
	 * 猜编码，通过在配置文件中获取的编码类型，一个一个的去猜编码
	 * @param sourceCode
	 * @param charList
	 * @return 最终的编码|如果猜不出编码，则返回null
	 * @throws UnsupportedEncodingException 
	 */
	public String guessEncoding(String sourceCode,String userCharset) throws UnsupportedEncodingException;
}
