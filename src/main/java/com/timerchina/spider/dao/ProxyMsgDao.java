package com.timerchina.spider.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.db.Conn;
import com.timerchina.spider.service.LoggerHandler;

/**
 * @author jason.lin@timerchina.com 2014-10-23 下午5:31:30
 * @statement 爬虫中少有的访问数据的类
 */
public class ProxyMsgDao {

	private final static String CONNECT_CLOSE_ERROR = "数据库连接关闭失败！";
	private final static String SELECT_ERROR = "数据库查询失败！";
	
	/**
	 * 获取代理中心的信息
	 */
	public Map<String, String> getProxyMsg(String sql){
		Map<String, String> resultMap = new HashMap<String, String>();
		Connection conn = Conn.getConn();
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {
			rs = conn.createStatement().executeQuery(sql);
			rsmd = rs.getMetaData();
			while (rs.next()) {
				HashMap<String, String> rowMap = new HashMap<String, String>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					rowMap.put(rsmd.getColumnLabel(i), rs.getString(i));
				}
				resultMap.putAll(rowMap);
			}
		} catch (SQLException e) {
			close(conn);
		}
		return resultMap;
	}
	
	/**
	 * 获取键值对的所有结果
	 * @param sql
	 * @return
	 */
	public List<Map<String, String>> executeQuery(String sql) {
		List<Map<String, String>> resultMap = new ArrayList<Map<String, String>>();
		Connection con = null;
		try {
			con = Conn.getConn();
			ResultSet rs = con.createStatement().executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				HashMap<String, String> rowMap = new HashMap<String, String>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					rowMap.put(rsmd.getColumnLabel(i), rs.getString(i));
				}
				resultMap.add(rowMap);
			}
			return resultMap;
		} catch (SQLException e) {
			LoggerHandler.getInstance().writeLogger(SELECT_ERROR + " sql:" + sql + e, Constant.LOG_LEVEL_ERROR);
		} finally {
			close(con);
		}
		return resultMap;
	}
	
	/**
	 * 对数据库开启的资源进行关闭操作
	 * @param con
	 */
	private void close(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			LoggerHandler.getInstance().writeLogger(CONNECT_CLOSE_ERROR + e, Constant.LOG_LEVEL_ERROR);
		}
	}
}
