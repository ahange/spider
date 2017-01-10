package com.timerchina.spider.service.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

import com.timerchina.spider.bean.Constant;

public class PropertiesTool {
	
	//获取配置文件中的信息
	private static PropertiesTool properties = new PropertiesTool();
	private static Logger logger = Logger.getLogger(PropertiesTool.class);
	private static Properties props = new Properties();
	private static int count = 0;
	
	private PropertiesTool(){}
	
	/**
	 * 获取對象
	 * @param PropertiesName
	 * @return
	 */
	public static PropertiesTool getSpiderInstance(){
		if(properties == null){
			synchronized(PropertiesTool.class){
				if(properties == null){
					properties = new PropertiesTool();
					return properties;
				}
			}
		}
		return properties;
	}
	
	/**
	 * 读取jar包内部配置文件的接口
	 * @param propertiesName
	 * @param key
	 * @return
	 * @throws MissingResourceException
	 */
	public String getProperties (String propsName, String key) 
			throws MissingResourceException {
		return properties.getPropsAbsolutely(propsName).getString(key);
	}
	
	/**
	 * 读取jar外面的配置文件的接口
	 * @param propsName
	 * @param key
	 * @return
	 * @throws MissingResourceException
	 */
	public String getPropertiesOutOfJar (String propsName, String key) 
			throws MissingResourceException {
		return properties.getPropsASBundle(propsName).getString(key);
	}
	
	/**
	 * 将jar包中的配置文件读取到Map集合中
	 * @param propsName
	 * @return
	 */
	public Map<String, String> getProps2Map(String propsName){
		Map<String, String> propsMap = new HashMap<String, String>();
		ResourceBundle bundle = properties.getPropsAbsolutely(propsName);
		if(bundle != null){
			for (String key : bundle.keySet()) {
				propsMap.put(key, bundle.getString(key));
			}
		}
		return propsMap;
	}
	
	/**
	 * 读取jar包外的配置文件读取到Map集合中
	 * @param propsName
	 * @return
	 */
	public Map<String, String> getProps2MapOutOfJar(String propsName){
		Map<String, String> propsMap = new HashMap<String, String>();
		ResourceBundle bundle = properties.getPropsASBundle(propsName);
		if(bundle != null){
			for (String key : bundle.keySet()) {
				propsMap.put(key, bundle.getString(key));
			}
		}
		return propsMap;
	}
	
	/**
	 * 更新配置文件中的键值对,键值存在的会更新value,不存在的插入新的值
	 * @param key
	 * @param value
	 */
	@SuppressWarnings("deprecation")
	public void writeKV2Properties(String propsName, String key, String value){
		OutputStream out = null;
		String filePath = properties.getPropsPath(propsName);
		try {
			props.load(new FileInputStream(filePath));
			out = new FileOutputStream(filePath);
			props.setProperty(key, value);
			props.store(out, "Change time" + new Date(System.currentTimeMillis()).toLocaleString());
		} catch (FileNotFoundException e) {
			logger.info("配置文件不存在"+filePath);
		} catch (IOException e) {
			logger.info("配置文件读取IO异常");
		}finally{
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.info("写入配置文件错误");
			}
		}
	}
	
	/**
	 * 以ResourceBundle的方式读取配置文件
	 * @param propsName
	 * @return
	 */
	public ResourceBundle getPropsASBundle(String propsName){
		ResourceBundle bundle = null;
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(properties.getPropsPath(propsName)));
			bundle = new PropertyResourceBundle(in);
		} catch (FileNotFoundException e) {
			logger.info("配置文件找不到" + properties.getPropsPath(propsName));
		}catch (IOException e) {
			logger.info("读取配置文件IO异常");
		}finally{
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
		return bundle;
	}
	
	/**
	 * jar包读取jar包中的配置文件
	 * @param propsName
	 * @return
	 */
	public ResourceBundle getPropsAbsolutely(String propsName){
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream in = classloader.getResourceAsStream(propsName);
		ResourceBundle bundle = null;
		try {
			bundle = new PropertyResourceBundle(in);
		} catch (FileNotFoundException e) {
			logger.info("配置文件找不到 " + classloader.toString());
		}catch (IOException e) {
			logger.info("读取配置文件IO异常");
		}finally{
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
		return bundle;
	}
	
	/**
	 * 读取配置文件的绝对地址
	 * @param propertiesName
	 * @return
	 */
	public String getPropsPath(String propsName){
		count++;
		String OS = System.getProperties().getProperty(Constant.OS_NAME);
    	String propertiesfile = "";
    	if(OS.contains(Constant.WINDOWS)){
    		propertiesfile = System.getProperty(Constant.USER_DIR) + "\\" + propsName; 
    	}else{
    		propertiesfile = System.getProperty(Constant.USER_DIR) + "/" + propsName; 
    	}
    	if(count<2){
    		logger.info("Props Path " + propertiesfile);
    	}
		return propertiesfile;
	}
	
	public static void main(String[] args) {
		PropertiesTool p = new PropertiesTool();
		
		ResourceBundle r = p.getPropsAbsolutely("spider.properties");
		
		System.out.println(r.getString("url"));
		
	}
	
}
