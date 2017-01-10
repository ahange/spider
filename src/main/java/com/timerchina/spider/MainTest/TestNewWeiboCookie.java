package com.timerchina.spider.MainTest;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.db.DBUtil;
import com.timerchina.spider.factory.SpiderFactory;

/**
 * @author jason.lin@timerchina.com 2014-11-14 上午11:37:16
 *
 */
public class TestNewWeiboCookie {

	@SuppressWarnings("unused")
	private String[] ip = new String[]{
			"115.28.221.114",
			"115.28.142.175",
			"115.28.7.153",
			"120.27.41.72",
			"115.28.141.137",
			"115.28.77.26",
			"115.28.143.221",
			"115.28.141.221",
			"115.28.80.59",
			"115.28.7.196"
	};
	
	
	private static Logger logger = Logger.getLogger(TestNewWeiboCookie.class);
	private static long sleepTime = 1000;
	private static String url = "http://weibo.com/kfcchina";
	private static String sql = "select * from tb_weibo_cookies WHERE machine_ip IN('115.29.248.186','218.244.129.80','115.29.251.9','112.124.26.20','218.244.130.138','115.29.225.93','218.244.129.97','218.244.142.66');";
	
	public static void main(String[] args) {
		
//		while(true){
		int count = 0;
			DBUtil util = new DBUtil("1","weibotools1");
			List<Map<String, String>> ListMap = util.executeQuery(sql);
			for (int num = 0; num < ListMap.size(); num++) {
				boolean isUseFul = true;
				long starttime = System.currentTimeMillis();
				String cookie = ListMap.get(num).get("cookie");
				String username = ListMap.get(num).get("username");
				String ip = ListMap.get(num).get("machine_ip");
				String password = ListMap.get(num).get("keyword");
				
				SpiderParams sp = new SpiderParams();
				sp.setUrl(url);
				sp.setCookie(cookie);
				SpiderResult rs = (SpiderResult) SpiderFactory.executeLocal(sp);
				
				logger.info("状态码  ：" + rs.getResponseCode() + "用户名：" + username
						+ "  密码：" + password + "  ip：" + ip + "  抓取次数：" + num + "-"
						+ rs.getHtml().length());
				if(rs.getHtml().contains("验证码错误")){
					logger.info("**********************");
					logger.info("屏蔽 ："+username+"-"+password+"-"+rs.getResponseCode());
					logger.info("**********************");
					count++;
					isUseFul = false;
				}
				
				else if(rs.getHtml().contains("是否走恢复身份")){
					logger.info("----------------------");
					logger.info("cookie失效 : "+username+"-"+password+"-"+rs.getResponseCode());
					logger.info("----------------------");
					count++;
					isUseFul = false;
				}
				
				else if(rs.getResponseCode()!=200){
					logger.info("----------------------");
					logger.info("状态码错误 : "+username+"-"+password+"-"+rs.getResponseCode());
					logger.info("----------------------");
					count++;
					isUseFul = false;
				}
				
				else if(rs.getHtml().length()<5000){
					logger.info("^0^ ^0^ ^0^ ^0^ ^0^ ^0^");
					logger.info("长度异常 : "+username+"-"+password+"-"+rs.getResponseCode()+"-"+rs.getHtml().length());
					logger.info("^0^ ^0^ ^0^ ^0^ ^0^ ^0^");
					count++;
					isUseFul = false;
				}
				
				if(!isUseFul){
					String sql = "update tb_weibo_cookies set useful=0 where username="+username;
					util.executeUpdate(sql);
				}
//				
				
//				String sql = "update tb_weibo_cookies set useful=1 where username="+username;
//				util.executeUpdate(sql);
				
				if(rs.getHtml().length()>= 1000){
					System.out.println(rs.getHtml().substring(0,1000));
				}else{
					System.out.println(rs.getHtml());
				}
				
				try {
					Thread.sleep(sleepTime );
				} catch (InterruptedException e) {
					
				}
				long endtime = System.currentTimeMillis();
				logger.info("抓取时间："+(endtime - starttime)+"ms"+"\n"+"  抓取间隔："+sleepTime+"ms");
			}
//		}
		System.out.println("存在异常的账号个数："+count);
	}
}
