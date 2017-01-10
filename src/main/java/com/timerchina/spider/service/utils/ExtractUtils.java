package com.timerchina.spider.service.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ExtractUtils {
	/*
	 * regexRule 正则表达式
	 * content 正则匹配内容
	 * level 正则匹配层次
	 * 只匹配单条语句，如果有多条，返回最后一条匹配结果
	 */
	public static String regexMatchStart(String regexRule,String content,int level){
		String result="";
		Pattern regex = Pattern.compile(regexRule);
		Matcher regexMatcher = regex.matcher(content);
		while (regexMatcher.find()) {
			result = regexMatcher.group(level);
			break;
		}
		return result;
	}
	public static String regexMatchEnd(String regexRule,String content,int level){
		String result="";
		Pattern regex = Pattern.compile(regexRule);
		Matcher regexMatcher = regex.matcher(content);
		while (regexMatcher.find()) {
			result = regexMatcher.group(level);
		}
		return result;
	}
	public static List<String> regexMatchAll(String regexRule,String content,int level){
		List<String> resultList=new ArrayList<String>();
		String result="";
		Pattern regex = Pattern.compile(regexRule);
		Matcher regexMatcher = regex.matcher(content);
		while (regexMatcher.find()) {
			result = regexMatcher.group(level);
			resultList.add(result);
		}
		return resultList;
	}
	public static boolean IsMatch(String regexRule,String content){
		Pattern regex = Pattern.compile(regexRule);
		Matcher regexMatcher = regex.matcher(content);
		while (regexMatcher.find()) {
			return true;
		}
		return false;
	}
	public static void main(String[] args) {
		String content = "http://weibo.com:weiboPort/weiboajax/statuses/search.json?keyword=%BB%A2%BA%BD%C7%EC%C9%FA&starttime=2014-11-25%2000:00:01&endtime=2014-11-27%2000:00:01&page=1&ishtml=1";
		System.out.println(regexMatchEnd("(http|https)://(.+?(com|cn|net|org|biz|info|cc|tv))",content,2));
	}
}
