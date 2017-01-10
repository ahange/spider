package com.timerchina.spider;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.db.DBUtil;
import com.timerchina.spider.factory.SpiderFactory;
import com.timerchina.spider.proxy.netty.base.client.NettyClient;
import com.timerchina.spider.proxy.netty.base.client.NettyClientConfig;

/**
 * @author jason.lin@timerchina.com 2014-8-18 下午4:11:55
 *
 */
public class TestProperties2 implements Runnable{

	private static Logger logger = Logger.getLogger(TestProperties2.class);
	static NettyClientConfig config = new NettyClientConfig();
	String request = "I'm client! lin";
	static NettyClient client = null;
	CountDownLatch latch;
    private int num;
	private static int sleepTime = 15000;
	private static List<Map<String, String>> ListMap = null;
	
	
	public TestProperties2(int num, CountDownLatch latch) {
        this.num = num;
        this.latch = latch;
    }
	
	/**
	 * @param args
	 * @throws UnsupportedEncodingException
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {

		DBUtil util = new DBUtil("test","weibotools1");
		ListMap = util.executeQuery("select * from tb_weibo_cookies where machine_ip = 'lls' and id>909 and id<920");
		
		int threadNum = 10;
		while(true){
			// 开4个线程
	        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(threadNum);
	        // 每个线程一个阀门
	        CountDownLatch latch = new CountDownLatch(threadNum);
	        try {
	            for (int i = 0; i < threadNum; i++) {
	            	TestProperties2 test = new TestProperties2(i, latch);
	                newFixedThreadPool.execute(test);
	            }
	            latch.await();
	            newFixedThreadPool.shutdown();
	        } catch (Exception e) {
	            // InterruptedException e
	            e.printStackTrace();
	        }
		}
		
	}
	
	public static void testname() throws InterruptedException{
		
		SpiderParams sp = new SpiderParams();
		
		sp.setUrl("http://www.baidu.com");
		SpiderResult sr = (SpiderResult) SpiderFactory.executeLocal(sp);
		System.out.println(sr.getHtml());
	}
	
	
	public static void saveFile(String html){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		File file = new File("log/"+sdf.format(date)+".html");
		if(file.isFile()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
		   OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"utf-8");
		   BufferedWriter writer = new BufferedWriter(write);  
		   writer.write(html);
		   writer.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void run() {
		
		long starttime = System.currentTimeMillis();
		String cookie = ListMap.get(num).get("cookie");
		String username = ListMap.get(num).get("username");
		String ip = ListMap.get(num).get("machine_ip");
		String password = ListMap.get(num).get("keyword");
		
		SpiderParams sp = new SpiderParams();
		sp.setUrl("http://s.weibo.com/weibo/KFC&Refer=index");
		sp.setCookie(cookie);
		SpiderResult rs = (SpiderResult) SpiderFactory.executeLocal(sp);
		
		logger.info("状态码  ：" + rs.getResponseCode() + "用户名：" + username
				+ "  密码：" + password + "  ip：" + ip + "  抓取次数：" + num + "-"
				+ rs.getHtml().length());
		if(rs.getHtml().contains("验证码错误")){
			logger.info("**********************");
			logger.info("屏蔽 ："+username+"-"+password+"-"+rs.getResponseCode());
			logger.info("**********************");
		}
		
		if(rs.getResponseCode()!=200){
			logger.info("----------------------");
			logger.info("状态码错误 : "+username+"-"+password+"-"+rs.getResponseCode());
			logger.info("----------------------");
		}
		
		if(rs.getHtml().length()<5000){
			logger.info("^0^ ^0^ ^0^ ^0^ ^0^ ^0^");
			logger.info("长度异常 : "+username+"-"+password+"-"+rs.getResponseCode()+"-"+rs.getHtml().length());
			logger.info("^0^ ^0^ ^0^ ^0^ ^0^ ^0^");
		}
		
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			
		}
		long endtime = System.currentTimeMillis();
		logger.info("抓取时间："+(endtime - starttime)+"ms"+"\n"+"  抓取间隔："+sleepTime+"ms");
		latch.countDown();
	}
}
