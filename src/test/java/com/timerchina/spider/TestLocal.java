package com.timerchina.spider;

import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.factory.SpiderFactory;

/**
 * @author jason.lin@timerchina.com 2014-12-9 下午1:41:17
 *
 */
public class TestLocal {

	public static void main(String[] args) {
		
		
		testStr();
		
	}
	
	public static void testStr(){
		String code = "Hi Superman, you have cancelled concerning a lot of people, have a think of how to make other people concern about you! ! If you have any questions, please contact Sina customer service: 400 690 0000";
		
		String str = "{'error':'Hi Superman, you have cancelled concerning a lot of people, have a think of how to make other people concern about you! ! If you have any questions, please contact Sina customer service: 400 690 0000','error_code':21332,'request':'/2/account/rate_limit_status.json'}";
		
		System.out.println(str.length());
		
	}
	
	
	/**
	 * 本地抓取的示例代码
	 */
	public static void testLocal(){
		//所有的请求参数都在这里面
		SpiderParams sp = new SpiderParams();
		
		sp.setUrl("http://www.baidu.com");
		
		//local是执行本地抓取
		SpiderResult spiderResult = (SpiderResult) SpiderFactory.executeLocal(sp);
		
		//输出获取的源码
		System.out.println(spiderResult.getHtml());
	}
	
	/**
	 * 请求爬虫中心的示例代码
	 */
	public static void testCaptain(){
		//所有的请求参数都在这里面
		SpiderParams sp = new SpiderParams();
		
		sp.setUrl("http://www.baidu.com");
		
		//local是执行本地抓取
		SpiderResult spiderResult = (SpiderResult) SpiderFactory.executeCaptain(sp);
		
		//输出获取的源码
		System.out.println(spiderResult.getHtml());
	}
	
	/**
	 * 请求海外抓取的示例代码
	 */
	public static void testOversea(){
		//所有的请求参数都在这里面
		SpiderParams sp = new SpiderParams();
		
		sp.setUrl("http://www.baidu.com");
		
		//local是执行本地抓取
		SpiderResult spiderResult = (SpiderResult) SpiderFactory.executeOversea(sp);
		
		//输出获取的源码
		System.out.println(spiderResult.getHtml());
	}
	
	/**
	 * 这是通用接口，这个类会自行根据传递的isProxy,isOversea参数去请求不同的方法
	 */
	public static void testGeneral(){
		//所有的请求参数都在这里面
		SpiderParams sp = new SpiderParams();
		
		sp.setUrl("http://www.baidu.com");
		
		//local是执行本地抓取
		SpiderResult spiderResult = (SpiderResult) SpiderFactory.execute(sp);
		
		//输出获取的源码
		System.out.println(spiderResult.getHtml());
	}
}
