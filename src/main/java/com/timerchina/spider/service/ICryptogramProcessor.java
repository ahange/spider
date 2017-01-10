package com.timerchina.spider.service;

/**
 * @author jason.lin@timerchina.com
 * @time 2014-7-17 下午04:53:45
 */
public interface ICryptogramProcessor {

	/**
	 * 加密字节数组
	 * @param arrB  需加密的字节数组
	 * @return 加密后的字节数组
	 */
	public byte[] encrypt(byte[] arrB) throws Exception;

	/**
	 * 加密字符串
	 * @param strIn  需加密的字符串
	 * @return 加密后的字符串
	 */
	public String encrypt(String strIn) throws Exception;

	/**
	 * 解密字节数组
	 * @param arrB  需解密的字节数组
	 * @return 解密后的字节数组
	 */
	public byte[] decrypt(byte[] arrB) throws Exception;

	/**
	 * 解密字符串
	 * @param strIn  需解密的字符串
	 * @return 解密后的字符串
	 */
	public String decrypt(String strIn) throws Exception;

}