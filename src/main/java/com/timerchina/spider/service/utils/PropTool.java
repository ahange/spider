package com.timerchina.spider.service.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * 提供读取jar包外面的配置文件的方式，暂时没有融合进PropKit中，使用方式
 * 
 * PropTool.use(propertiesName);
 * PropTool.getValueOutOfJar(key);
 * 
 * @author jason.lin
 */
public class PropTool {
	
	//获取配置文件中的信息
	private static PropTool properties = new PropTool();
	private static Logger logger = Logger.getLogger(PropTool.class);
	private static int count = 0;
	private static Map<String, Map<String, String>> propertiesMap = new HashMap<String,Map<String, String>>();
	private static Map<String, String> prop = null;
	public static final String DEFAULT_CHARTSET = "UTF-8";
	public static final long FILE_UPDATE_INTERVAL_TIME = 1 * 60 * 1000;				//一分钟更新一次配置文件
	public static long last_prop_update_time = 0;									//记录配置文件最后一次更新的时间
	public static final String WINDOWS = "Windows"; 
	public static final String OS_NAME = "os.name";
	public static final String USER_DIR = "user.dir";
	
	private PropTool(){}
	
	/**
	 * 读取配置文件，使用默认的编码utf-8
	 * @param propertiesName
	 */
	public static void use(String propertiesName){
		use(propertiesName, DEFAULT_CHARTSET);
	}
	
	/**
	 * 读取配置文件，需要文件的编码
	 * @param propertiesName
	 * @param charset
	 */
	public static void use(String propertiesName, String charset){
		Map<String, String> rs = propertiesMap.get(propertiesName);
		boolean isUpdate = (System.currentTimeMillis() - last_prop_update_time) >= FILE_UPDATE_INTERVAL_TIME;
		if(rs == null || isUpdate){
			synchronized (PropTool.class) {
				if(rs == null || isUpdate){
					rs = readProp2Map(propertiesName, charset);
					if(rs != null){
						prop = rs;
					}
					last_prop_update_time = System.currentTimeMillis();
				}
			}
		}else{
			prop = rs;
		}
	}
	
	/**
	 * 将配置文件读取到map中，展现形式为  propsName为Map1的key，propsName中的key和value作为Map2，成为Map1的value值
	 * @param propsName
	 * @param charset
	 */
	public static Map<String,String> readProp2Map(String propsName,String charset){
		ResourceBundle bundle = properties.getPropsASBundle(propsName, charset);
		if(bundle != null){
			Enumeration<String> keys = bundle.getKeys();
			Map<String, String> proDetail = new HashMap<String, String>();
			while (keys.hasMoreElements()) {
				String ele = keys.nextElement();
				proDetail.put(ele, bundle.getString(ele));
			}
			propertiesMap.put(propsName, proDetail);
			return proDetail;
		}else{
			logger.error("配置文件读取失败." + propsName);
		}
		return null;
	}
	
	/**
	 * 读取jar包外面的配置文件（暂时没有测试apache环境下的配置文件读取问题）
	 * @param key
	 * @return
	 */
	public static String getValueOutOfJar(String key){
		if(prop == null){
			logger.error("请先使用PropTool.user()初始化配置文件.");
		}
		return prop.get(key);
	}
	
	/**
	 * 以ResourceBundle的方式读取配置文件
	 * @param propsName
	 * @return
	 */
	private ResourceBundle getPropsASBundle(String propsName, String charset){
		ResourceBundle bundle = null;
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(properties.getPropsPath(propsName)));
			bundle = new PropertyResourceBundle(new InputStreamReader(in, charset));
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
	 * 读取配置文件的绝对地址
	 * @param propertiesName
	 * @return
	 */
	private String getPropsPath(String propsName){
		count++;
		String OS = System.getProperties().getProperty(OS_NAME);
    	String propertiesfile = "";
    	if(OS.contains(WINDOWS)){
    		propertiesfile = System.getProperty(USER_DIR) + "\\" + propsName; 
    	}else{
    		propertiesfile = System.getProperty(USER_DIR) + "/" + propsName; 
    	}
    	if(count<2){
    		logger.info("Props Path " + propertiesfile);
    	}
		return propertiesfile;
	}
	
	public static void main(String[] args) {
		
		long now = System.currentTimeMillis();
		
		while (true) {
			
			System.out.println("时间过了：" + (now - System.currentTimeMillis()));
			
			PropTool.use("mysql.properties");
			
			System.out.println(PropTool.getValueOutOfJar("dburl"));
			
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
}
