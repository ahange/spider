package com.timerchina.spider.proxy.netty.base.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * @author jason.lin@timerchina.com 2014-11-5 上午10:38:57
 * @statement 生成每一个通道唯一的ID的工具类
 */
public class GenerateSessionIDUtil {

	private static final int SESSION_ID_BYTES = 6;

	/**
	 * 生成session id的值
	 * 
	 * @return
	 */
	public static synchronized String generateSessionId() {
		
		Random random = new SecureRandom(); // 取随机数发生器, 默认是SecureRandom
		byte bytes[] = new byte[SESSION_ID_BYTES];
		random.nextBytes(bytes); // 产生8字节的byte，也就是64位算法
		bytes = getDigest().digest(bytes); // 取摘要,默认是"MD5"算法

		StringBuffer result = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) { // 转化为16进制字符串
			byte b1 = (byte) ((bytes[i] & 0xf0) >> 4);
			byte b2 = (byte) (bytes[i] & 0x0f);

			if (b1 < 10)
				result.append((char) ('0' + b1));
			else
				result.append((char) ('A' + (b1 - 10)));
			if (b2 < 10)
				result.append((char) ('0' + b2));
			else
				result.append((char) ('A' + (b2 - 10)));
		}
		return (result.toString());
	}

	
	public static synchronized long generateLongId(){
		
		Random random = new SecureRandom();
		byte bytes[] = new byte[SESSION_ID_BYTES];
		random.nextBytes(bytes); // 产生8字节的byte，也就是64位算法
		StringBuffer result = new StringBuffer();
		
		for (int i = 0; i < bytes.length; i++) { // 转化为16进制字符串
			byte b1 = (byte) ((bytes[i] & 0xf0) >> 4);
			byte b2 = (byte) (bytes[i] & 0x0f);
			if (b1 < 10)
				result.append((char) ('0' + b1));
			else
				result.append((char) ('0' + (b1 - 10)));
			if (b2 < 10)
				result.append((char) ('0' + b2));
			else
				result.append((char) ('0' + (b2 - 10)));
		}
		return Long.parseLong(result.toString());
	}
	
	/**
	 * MD5算法获取摘要
	 * @return
	 */
	private static MessageDigest getDigest() {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return md;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 利用java自带的随机数生成器，利用及其网卡，当地时间和一个
	 * @return
	 */
	public static String getUUID(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	public static void main(String[] args) throws InterruptedException {
		int count = 0;
		while (true) {
			count++ ;
			System.out.println(GenerateSessionIDUtil.getUUID() + "-- " + count);
//			Thread.sleep(500);
		}
	}
	
	
}
