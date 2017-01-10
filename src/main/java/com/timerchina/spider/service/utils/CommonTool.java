package com.timerchina.spider.service.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;
import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.service.LoggerHandler;

public class CommonTool {
	public static final int PARSE_INT_ERROR = 0;
	public static final double PARSE_DOUBLE_ERROR = -100d;
	public static final int SECONDS = 0;
	public static final int MINUTES = 1;
	public static final int MILLIONSECONDS = 2;
	
	public static final int KEY_NUM_TYPE = 0;
	public static final int KEY_CHAR_TYPE = 1;
	
	public static final int FILLIN_LEFT = 0;
	public static final int FILLIN_RIGHT = 1;
	
	public static String generateKeyNumType () {
		return String.valueOf(System.currentTimeMillis());
	}
	
	private static String generateKeyCharType () {
		return "";
	}

	public static String keywordEncoding(String keyword){
  		try {
  			keyword = URLEncoder.encode(keyword, "utf-8");
  		} catch (UnsupportedEncodingException e) {
  			LoggerHandler.getInstance().writeLogger("URL编码错误!" + e.getCause(), Constant.LOG_LEVEL_ERROR);
  		}
  		return keyword;
      }
	  
	public static String getAbsoluteUrl(String baseUrl, String url){
		try {
			URL rootUrl = new URL(baseUrl);
			URL newUrl = new URL(rootUrl, url);
			url = newUrl.toString();
			url = url.replace("&amp;", "&");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return url;
	}
	
	public static String generateKey (int type) {
		String key = "";
		if (type == KEY_NUM_TYPE) {
			key = generateKeyNumType ();
		} else if (type == KEY_CHAR_TYPE) {
			key = generateKeyCharType ();
		}
		
		return key;
	}
	
	public static void sleep (long timeout, int timeUnit) {
		if (timeUnit == MINUTES) {
			timeout *= 60;
		}
		if (timeUnit != MILLIONSECONDS) {
			timeout *= 1000;
		}
		
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static int parseInt (Object num) {
		try {
			return Integer.parseInt((String) num);
		} catch (Exception e) {
			return PARSE_INT_ERROR;
		}
	}
	
	public static double parseDouble (Object num) {
		try {
			return Double.parseDouble((String)num);
		} catch (Exception e) {
			return PARSE_INT_ERROR;
		}
	}
	
	public static boolean parseBoolean (Object adjudge) {
		try {
			return Boolean.parseBoolean((String) adjudge);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String parseString (Object num) {
		try {
			return String.valueOf(num);
		} catch (Exception e) {
			return "";
		}
	}
	
	public static int convertBooleanToInt (boolean adjudge) {
		if (adjudge) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public static boolean isEmpty (String value) {
		return value == null || value.trim().length() <= 0;
	}
	
	public static boolean isEmpty (Object object) {
		return object == null ;
	}
	
	public static boolean isEmpty (Object[] object){
		return object.length>0;
	}
	
	public static String fillBlank (String text, int leftOrRight, int totalLen) {
		return fillText (text, ' ', leftOrRight, totalLen);
	}
	
	public static String fillZero (String text, int leftOrRight, int totalLen) {
		return fillText (text, '0', leftOrRight, totalLen);
	}
	
	public static String fillText (String text, char fillChar, int leftOrRight, int totalLen) {
		if (totalLen <= text.length()) {
			return text;
		}
		
		StringBuffer fillinTextBuf = new StringBuffer ();
		for (int i = text.length(); i < totalLen; i ++) {
			fillinTextBuf.append(fillChar);
		}
		
		if (leftOrRight == FILLIN_LEFT) {
			return fillinTextBuf.append(text).toString();
		} else {
			return text.concat(fillinTextBuf.toString());
		}
	}
	
	public static int random(int digit) {
		Random random = new Random();
		int num = Math.abs(random.nextInt())%digit;
		return num;
	}
	
	public static int random1 (int digit) {
		int numMaxDigit = (int) Math.pow(10, digit);
		Random random = new Random();
		int num = random.nextInt(numMaxDigit);
		return num > 100 ? num : 100 + num;
	}
	
	public static double round(double v,int scale){
		if(scale<0){
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static int addInCircle (int num, int increament, int range, int initNum) {
		return (num + increament) > range ? initNum : (num + increament);
	}
}
