package com.timerchina.spider.service.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Utils {
	//记录基础值
	public static final int STATIC_NUM = 0;
	private static Logger logger = getLogger();

	public static Logger getLogger() {
		return Logger.getLogger(Thread.currentThread().getClass().getName());
	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public static String getSnapshotAddress() {
		try {
			String configXmlPath = Utils.getProjectAbsolutePath() + "config.xml";
			InputStream source = new FileInputStream(configXmlPath);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(source);
			Node root = doc.getFirstChild();
			NodeList list = root.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node n = list.item(i);
				if (n.hasAttributes()) {
					NamedNodeMap attr = n.getAttributes();
					Node addressNode = attr.getNamedItem("address");
					if (addressNode == null)
						continue;
					else if (!addressNode.getNodeValue().isEmpty()) {
						return addressNode.getNodeValue();
					}
				}
			}
			return "";
		} catch (Exception e) {
			logger.error("#数据配置文件解析错误！", e);
			return null;
		}
	}

	/**
	 * 自动映射
	 * @param <T>
	 * @param cl
	 * @param fieldMap
	 * @return
	 */
	public static <T> T map2Obj(Class<T> cl, Map<String, String> fieldMap) {
		Method[] methods = cl.getDeclaredMethods();
		T obj;
		try {
			obj = cl.newInstance();
		} catch (Exception e) {
			logger.error("#对象创建出错：初始化失败！class:" + cl, e);
			return null;
		}
		for (Method method : methods) {
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length == 1) {
				String fieldName = method.getName().replaceAll("^set", "").replaceAll("^is", "");
				fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
				String valueStr = fieldMap.get(fieldName);
				if (valueStr != null) {
					try {
						Object value = Utils.convertType(valueStr, parameterTypes[0]);
						if (value != null) {
							method.invoke(obj, value);
						}else if(value == null){
							Set<Entry<String, String>> enrtySet = fieldMap.entrySet();
							for (Entry<String, String> entry : enrtySet) {
								logger.error("转换出错"+entry.getKey()+"--"+ entry.getValue());
							}
						}
					} catch (Exception e) {
						logger.error("#对象创建出错：方法执行失败！", e);
					}
				}
			}
		}
		return obj;
	}

	public static int str2Int(String str) {
		Object obj = convertType(str, Integer.class);
		return obj == null ? 0 : (Integer) obj;
	}

	public static double str2Double(String str) {
		Object obj = convertType(str, Double.class);
		return obj == null ? 0 : (Double) obj;
	}
	
	public static String int2Str(Object obj){
		return obj == null ? "0" : String.valueOf(obj);
	}

	/**
	 * 获取匹配数组
	 * 
	 * @param regex
	 * @param input
	 * @return
	 */
	public static List<String[]> getMatchArrayList(String regex, String input) {
		Matcher matcher = getMatcher(regex, input);
		List<String[]> fieldsList = new ArrayList<String[]>();
		while (matcher.find()) {
			String[] fields = new String[matcher.groupCount()];
			for (int i = 0; i < fields.length; i++) {
				fields[i] = matcher.group(i + 1);
			}
			fieldsList.add(fields);
		}
		return fieldsList;
	}

	/**
	 * 获取第一个匹配项
	 * 
	 * @param regex
	 * @param input
	 * @return
	 */
	public static String getFirstMatchGroup(String regex, String input) {
		Matcher matcher = getMatcher(regex, input);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			logger.info("结果为空！regex: " + regex);
			return null;
		}
	}

	/**
	 * 获取匹配项
	 * 
	 * @param regex
	 * @param input
	 * @return
	 */
	private static Matcher getMatcher(String regex, String input) {
		if (input == null) {
			input = "";
		}
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		return matcher;
	}

	/**
	 * 根据类型进行转换
	 * 
	 * @param valueStr
	 * @param cl
	 * @return
	 */
	private static Object convertType(String valueStr, Class<?> cl) {
		if (valueStr == null || cl == null) {
			logger.error("#对象转换类型出错：对象值或类型为空！valueStr:'" + valueStr + "',cl:'" + cl + "'");
			return valueStr;
		}
		try {
			if (cl == String.class) {
				return valueStr;
			} else if (cl == Integer.class || cl == int.class) {
				return Integer.parseInt(valueStr);
			} else if (cl == Long.class || cl == long.class) {
				return Long.parseLong(valueStr);
			} else if (cl == Double.class || cl == double.class) {
				return Double.parseDouble(valueStr);
			}
		} catch (Exception e) {
			logger.error("#对象转换类型出错！valueStr:'" + valueStr + "',cl:'" + cl + "'", e);
			return null;
		}
		logger.error("#对象转换类型出错：无法识别的类型！valueStr:'" + valueStr + "',cl:'" + cl + "'");
		return valueStr;
	}

	/**
	 * 获取文件的相对路劲
	 * 
	 * @return
	 */
	public static String getProjectAbsolutePath() {
		String path = Utils.class.getResource("/").getPath();
		try {
			path = URLDecoder.decode(path, "utf-8");
			if (path.startsWith("/C:/") || path.startsWith("/D:/") || path.startsWith("/F:/")) {
				path = path.substring(1);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("#获取项目绝对路径出错：转码失败！", e);
		}
		return path;
	}
	
	public static String list2String(List<?> list, String seperator) {
		StringBuilder res = new StringBuilder();
		for (Object object : list) {
			res.append(object + seperator);
		}
		return res.substring(0, res.length() - seperator.length());
	}
	
	public static boolean isEmpty(String str){
		
		return str==null||str.isEmpty();
	}
}
