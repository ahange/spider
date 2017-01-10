package com.timerchina.spider.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.service.utils.PropertiesTool;

/**
 * 数据库操作
 * 
 * @statement 因为爬虫访问数据库的频率不高，同时取一次后直接保存到内存中，所以使用这种简易的连接方式
 */
public class Conn {
	public static String user_name = "";
	public static String user_pwd = "";
	public static String url = "";
	public static String driver = Constant.CONN_DRIVER;

	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	public static Connection getConn() {
		try {
			Connection conn = null;
			initConnMsg();
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user_name, user_pwd);
			if (!conn.isClosed()) {
				;
			}
			return conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 初始化连接数据的username,password,url
	 * 需要读取mysql.properties这个配置文件
	 */
	private static void initConnMsg() {
		PropertiesTool props = PropertiesTool.getSpiderInstance();
		Map<String, String> proxyMsg = props.getProps2Map(Constant.PROPS_SPIDER);
		user_name = proxyMsg.get(Constant.CONN_USERNAME);
		user_pwd = proxyMsg.get(Constant.CONN_PASSWORD);
		url = proxyMsg.get(Constant.CONN_URL);
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param conn
	 */
	public static void closeConn(Connection conn) {
		try {
			if (!conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
