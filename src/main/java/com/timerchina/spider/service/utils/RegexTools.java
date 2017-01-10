package com.timerchina.spider.service.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;


public class RegexTools {
	
	private static Logger logger = Logger.getLogger(RegexTools.class);
	
	public static String getRegexResult(String html, String regex, Class<?> type, int index) throws Exception {
		StringBuffer sb = new StringBuffer();
		int count = 1;
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher match = pattern.matcher(html);
		if (type == List.class) {
			while (match.find()) {
				String result = match.group(1);
				if (count > 1) {
					sb.append("##");
				}
				sb.append(result);
				count++;
			}
		} else {
			while (match.find()) {
				if (count < index) {
					count++;
					continue;
				}
				String result = match.group(1);
				sb.append(result);
				break;
			}
		}
		return sb.toString();
	}
	
	public static String getRegexResult(String content, String regex) {
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher match = pattern.matcher(content);
		if (match.find()) {
			return match.group(1);
		}
		return "";
	}
	
	public static List<String> getAllFirstGroupMatches(String regex, String input) {
		Matcher matcher = getMatcher(regex, input);
		List<String> fieldsList = new ArrayList<String>();
		while (matcher.find()) {
			fieldsList.add(matcher.group(1));
		}
		return fieldsList;
	}
	
	public static List<String[]> getAllMatches(String regex, String input) {
		Matcher matcher = getMatcher(regex, input);
		List<String[]> fieldsList = new ArrayList<String[]>();
		while (matcher.find()) {
			String[] fields = new String[matcher.groupCount()];
			for (int i = 0; i < matcher.groupCount(); i++) {
				fields[i] = matcher.group(i);
			}
			fieldsList.add(fields);
		}
		return fieldsList;
	}
	
	public static String getFirstMatchGroupOfFirstMatch(String regex, String input) {
		return getFirstMatch(regex, input, 1);
	}

	public static String getFirstMatch(String regex, String input, int matchGroupIndex) {
		Matcher matcher = getMatcher(regex, input);
		if (matcher.find()) {
			if (matchGroupIndex < 0 || matchGroupIndex > matcher.groupCount()) {
				logger.error("无效的Match Gounp Index!");
				return null;
			}
			return matcher.group(matchGroupIndex);
		} else {
//			logger.info("结果为空！regex: " + regex);
			return null;
		}
	}
	
	public static boolean isMatched(String regex, String input) {
		Matcher matcher = getMatcher(regex, input);
		return matcher.find();
	}
	
	private static Matcher getMatcher(String regex, String input) {
		if (input == null) {
			input = "";
		}
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		return matcher;
	}
	
	public static void main(String[] args) {
		String inutString = "document.cookie = \"SNUID=9C8EA6B80B0912BA4C14C9B90C67D50F; path=/; expires=\"+date.toGMTString()+\";domain=\"+domain;";
		System.err.println(RegexTools.getFirstMatchGroupOfFirstMatch("document.cookie = \"(SNUID[^\"]*?)\"", inutString));
	}
	
}
