package com.timerchina.spider;

import java.util.List;
import java.util.Map;

import com.timerchina.spider.db.DBUtil;

/**
 * @author jason.lin@timerchina.com 2014-12-30 上午11:22:33
 *
 */
public class CopyOneDB2Another {

	public static void copy2AnotherDB(){
		
//		DBUtil util1 = new DBUtil("1","weibotools1");
		
		DBUtil util2 = new DBUtil("2","weibotools1");
		
//		String toSql = "INSERT ignore INTO tb_weibo_cookies(captain_id,cookie,username,keyword,machine_ip,is_available,update_time,`check`,cookie_level) values(1,?,?,?,'u',1,NOW(),4,1);";
		
		String toSql = "update tb_weibo_cookies set cookie=?,cookie_level=2 where username=?;";
		
		String fromSql = "select * from tb_weibo_cookies_lin where useful=2;";
		
		List<Map<String, String>> result = util2.executeQuery(fromSql);
		
		for (Map<String, String> map : result) {
			System.out.println(map.get("username"));
			String[] params = new String[2];
			params[0] = map.get("cookie");
			params[1] = map.get("username");
			String count = util2.prepareExecuteInsertReturnKey(toSql, params);
			System.out.println("更新成功"+count);
		}
	}
	
	
	public static void main(String[] args) {
		
		copy2AnotherDB();
		
	}
	
}
