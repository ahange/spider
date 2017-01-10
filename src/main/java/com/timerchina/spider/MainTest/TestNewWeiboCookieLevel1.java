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
public class TestNewWeiboCookieLevel1 {

	private static Logger logger = Logger.getLogger(TestNewWeiboCookieLevel1.class);
	private static long sleepTime = 1000;
	private static String url = "http://s.weibo.com/weibo/%E4%B8%8A%E6%B5%B7%E5%A4%96%E6%BB%A9%E8%B8%A9%E8%B8%8F%E4%BA%8B%E6%95%85?topnav=1&wvr=6&topsug=1";
	private static String sql = "SELECT * FROM tb_weibo_cookies where cookie_level=1;";
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
//		while(true){
		int count = 0;
			DBUtil util = new DBUtil("2","weibotools1");
			List<Map<String, String>> ListMap = util.executeQuery(sql);
			for (int num = 0; num < ListMap.size(); num++) {
				boolean isUseFul = true;
				long starttime = System.currentTimeMillis();
				String cookie = ListMap.get(num).get("cookie");
//				String cookie = "SINAGLOBAL=3439486855641.0073.1419828268026; _s_tentry=passport.weibo.com; Apache=5671698122750.968.1420355893010; ULV=1420355893043:1:1:1:5671698122750.968.1420355893010:; SUS=SID-1923406841-1420355969-XD-7md5n-64c7e0db247eb5eaccd492fc29c5d676; SUE=es%3Dfe4aca8c5f1b498db5a54ad9d04bc5d2%26ev%3Dv1%26es2%3D87e1be99fd1b68f084a0807a4210a49c%26rs0%3DbuNz%252BAlL2CeUnOWP8tDtgWI1jAwilgUppRcxyFBaY2O%252BA2m%252F08W4bjkScGediRVkQtFjf3QZy627T4jLvFNzQAoK13ve1hVoeJCmdvuM%252F2etNLSdMHHMwd6CRZkzsW6JD%252B%252BSEV9d3O274G31nTRGjkJ4OrCb6Et7uxF2Y%252FhZj7A%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1420355969%26et%3D1420442369%26d%3Dc909%26i%3Dd676%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D2%26st%3D0%26uid%3D1923406841%26name%3D18758327040%2540sina.cn%26nick%3D%25E5%2593%2593%25E5%2587%25A1%25E6%2580%259D%25E5%25AF%2586%25E8%25BE%25BE%26fmp%3D%26lcp%3D2011-02-08%252014%253A11%253A47; SUB=_2A255rJnRDeTxGedH6VEV8CjEzz2IHXVa2juZrDV8PUNbuNAPLVj2kW-UZbkG6lulDCpvgI6zDS65DXY0pg..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhVFq_Oqr2Vz1eupi9r3lKJ5JpX5K2t; ALF=1420960719; SSOLoginState=1420355969";
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
				
				String weiboAllCount = null;
				
				logger.info("当前获取到  "+weiboAllCount+" 条微博");
				
				if(weiboAllCount==null || weiboAllCount.length() < 1){
					logger.info("**********************");
					logger.info("未登录，cookie无效 ："+username+"-"+password+"-"+rs.getResponseCode());
					logger.info("**********************");
					count++;
					isUseFul = false;
				}
				
				else if(rs.getHtml().contains("验证码错误")){
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
					String sql = "update tb_weibo_cookies set is_available=0 where username="+username;
					util.executeUpdate(sql);
				}else{
					String sql = "update tb_weibo_cookies set is_available=1 where username="+username;
					util.executeUpdate(sql);
				}
				
				if(rs.getHtml().length()>= 1000){
					logger.info(rs.getHtml().substring(0,1000));
				}else{
					logger.info(rs.getHtml());
				}
				
				try {
					Thread.sleep(sleepTime );
				} catch (InterruptedException e) {
					
				}
				long endtime = System.currentTimeMillis();
				logger.info("抓取时间："+(endtime - starttime)+"ms"+"\n"+"  抓取间隔："+sleepTime+"ms");
			}
//		}
			logger.info("存在异常的账号个数："+count);
	}
}
