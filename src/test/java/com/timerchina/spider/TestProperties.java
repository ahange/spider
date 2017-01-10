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

import org.apache.log4j.Logger;

import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.dao.ProxyMsgDao;
import com.timerchina.spider.db.DBUtil;
import com.timerchina.spider.factory.SpiderFactory;

/**
 * @author jason.lin@timerchina.com 2014-8-18 下午4:11:55
 *
 */
public class TestProperties implements Runnable{

	public static int count = 0;
	
	CountDownLatch latch;
    private int    num;

    private static Logger logger = Logger.getLogger(TestProperties.class);
    
    public TestProperties(int num, CountDownLatch latch) {
        this.num = num;
        this.latch = latch;

    }
	
	/**
	 * @param args
	 * @throws UnsupportedEncodingException
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) {
		
		test9090090();
		
//		// 开4个线程
//        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(3);
//        // 每个线程一个阀门
//        CountDownLatch latch = new CountDownLatch(3);
//        
//        try {
//
//            for (int i = 0; i < 3; i++) {
//                newFixedThreadPool.execute(new TestProperties(i, latch));
//            }
//            System.out.println("waiting!! for" + latch);
//            latch.await();
//            System.out.println(" main thread over!!");
//            newFixedThreadPool.shutdown();
//        } catch (Exception e) {
//            // InterruptedException e
//            e.printStackTrace();
//        }
		
//		while(true){
//			TestProperties t1 = new TestProperties();
//			
//			for (int i = 0; i < 3; i++) {
//				Thread thread1 = new Thread(t1);
//				thread1.start();
//			}
//		}
	}
	
	
	public void run() {
		
		
		
		 try {
	            Thread.sleep(2000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

	        latch.countDown();
	        System.out.println("Task1 " + num + "complete!!"+count);
		count++;
		
//		System.out.println(count+"--"+Thread.currentThread().getName());
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
	
	public void catchResult(String cookie, String username, int count){
		
		long starttime = System.currentTimeMillis();
		
//		String cookie = "login_sid_t=7ce3180daba3119509efbc50d4b9c614; YF-Ugrow-G0=9b1c05a2e1e1e84dd3e500248ab16d62; _s_tentry=www.baidu.com; UOR=www.baidu.com,weibo.com,login.sina.com.cn; Apache=5175454141240.008.1415279348662; SINAGLOBAL=5175454141240.008.1415279348662; ULV=1415279348679:1:1:1:5175454141240.008.1415279348662:; SUB=_2AkMjB_nAa8NlrABTkfwSxGLhaopH-jyQ1_82An7uJhIyHRgv7nkTqSW1JAXu0FumyPUx_j-Qd9q-z30Dbg..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WFIVHzQlGJK7l0QmS6-.cL55JpX5K2t; un=13411034249; YF-Page-G0=091b90e49b7b3ab2860004fba404a078; YF-V5-G0=bd9e74eeae022c6566619f45b931d426; ULOGIN_IMG=14152801911409; myuid=3187298814; SUS=SID-3294789367-1415280375-GZ-wrl59-b94c7110053456aabe7fc75a19d294ef; SUE=es%3Dc4737a23048393e5191344418c168917%26ev%3Dv1%26es2%3D920b25dbbef561216ad152fa3a2c905f%26rs0%3DBAPMpFyUABIx7DA5FqhNHcA8WXVZQETjdnW18fRKx%252BXm5jlxe1HNcOAIv8mwEpqw89RgYFmdyhgX5UabylsNdXEW8Rd1XaB%252BrmdYAgR6R80lCwUDYyktcMjkvICRwJAg7t%252FXKgfVXxXW8ib8rt4gdS2V%252BrLANpuJyEK2BZasvFQ%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1415280375%26et%3D1415366775%26d%3Dc909%26i%3D94ef%26us%3D1%26vf%3D2%26vt%3D1%26ac%3D2%26st%3D0%26uid%3D3294789367%26name%3D13411034249%2540sina.cn%26nick%3D%25E7%2594%25A8%25E6%2588%25B73294789367%26fmp%3D%26lcp%3D2013-02-27%252021%253A23%253A37; ALF=1415885136; SSOLoginState=1415280375";
		SpiderParams sp = new SpiderParams();
		sp.setUrl("http://s.weibo.com/weibo/KFC&Refer=index");
		sp.setCookie(cookie);
		SpiderResult rs = (SpiderResult) SpiderFactory.executeLocal(sp);
		
		long endtime = System.currentTimeMillis();
		
		System.out.println("count----"+count);
//		System.out.println(rs.getHtml().substring(0,1000));
		
		if(rs.getHtml().contains("验证码错误")){
			System.out.println("**********************");
			System.out.println("---------屏蔽---------");
			System.out.println("**********************");
		}
		count++;
	}
	
	
	
	
	public static void testname() throws InterruptedException{
		
		SpiderParams sp = new SpiderParams();
		
//		sp.setUrl("http://s.weibo.com/wb/%25E9%25A6%2596%25E5%2585%2588%25E6%258E%25A8%25E5%2587%25BA%25E5%25A4%25B4%25E5%2583%258F%25E4%25B9%258B%25E9%25B3%2597%25E9%25B1%25BC%25E9%25A5%25AD&xsort=time&userscope=custom%253AFelp-%25E9%259C%258F%25E9%259C%258F%25E7%258B%25AC%25E5%25AE%25B6%25E6%25B4%259B%25E6%259D%2589%25E7%259F%25B6%25E5%2590%2583%25E8%25B4%25A7%25E4%25BF%25B1%25E4%25B9%2590%25E9%2583%25A8&Refer=g");
//		sp.setCookie("SINAGLOBAL=754742126446.2173.1407834837336; SUS=SID-1923406841-1409200778-GZ-ots5g-3ae8f940d5933d5a313c4ec2006a16b0; SUE=es%3Dfed3966e9c611b822d828cbaa31d7ef1%26ev%3Dv1%26es2%3D6b0c30b22b60d4a060aadc226156f397%26rs0%3DevS1ibrmMtKW%252FdnqdNSrYKPmCcyW8yZQr534PiiJ5TIkRNdbeZc5jj%252F4Yq3kQwE%252Bs7jV1dWPwBmuLPxMh%252BAyxMfUFC8qC%252BHSYZjH4K4AQ%252BnWhJCUIwWnGqQzFe0dxYcBJnGCSWdG1SgcqlDdAQwjlrnOTemoJLxyqEFTDqIs8wQ%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1409200778%26et%3D1409287178%26d%3Dc909%26i%3D16b0%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D2%26st%3D0%26uid%3D1923406841%26name%3D18758327040%2540sina.cn%26nick%3D%25E5%2593%2593%25E5%2587%25A1%25E6%2580%259D%25E5%25AF%2586%25E8%25BE%25BE%26fmp%3D%26lcp%3D2011-02-08%252014%253A11%253A47; SUB=_2AkMkoj29a8NlrAJYmvsRzG3qaIxH-jyQiLNLAn7uJhIyGxgv7nsRqSW7l6-8kVMYFcT3WdXgiGKKamE1BQ..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhVFq_Oqr2Vz1eupi9r3lKJ5JpX5KMt; ALF=1440736777; SSOLoginState=1409200778; _s_tentry=blog.sina.com.cn; Apache=9705799513030.797.1409200891301; ULV=1409200891335:10:10:4:9705799513030.797.1409200891301:1409102147251; YF-Ugrow-G0=169004153682ef91866609488943c77f; wvr=5; YF-V5-G0=9717632f62066ddd544bf04f733ad50a; UOR=developer.51cto.com,widget.weibo.com,spr_sinamktbd_bd_baidub_weibo_t001");
		sp.setUrl("http://www.baidu.com");
//		sp.setCookie("");
//		sp.setCookie("BAIDUID=2B24C314A5AE6DA56F67BB08430D2FBE:FG=1; BDUSS=lluR0ZxM0lFdzBicVF4YmM4WTFoM3NlaHhYZC14SlBHZWR-czl0Zld5RU1rU1pVQVFBQUFBJCQAAAAAAAAAAAEAAAAjyU0~0NLUy7XEQWNoZXJvbgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAwE~1MMBP9TU; CHKFORREG=65f39ab9e6c6a7ba33c0768cb687925c; bdshare_firstime=1409221707285; Hm_lvt_d101ea4d2a5c67dab98251f0b5de24dc=1409221557; Hm_lpvt_d101ea4d2a5c67dab98251f0b5de24dc=1409225688Host:index.baidu.com");
		SpiderResult sr = (SpiderResult) SpiderFactory.executeLocal(sp);
//		System.out.println("Cookie="+sr.getCookies());
		System.out.println(sr.getHtml());
//		System.out.println(sr.getHtml());
		
//		String url = "http://weixin.sogou.com/gzhjs?cb=sogou.weixin.gzhcb&openid=oIWsFt_hzfMQrBEwA3IIEI3csMlA&page=1";
		
	}
	
	/**
	 * 测试账号是否被屏蔽
	 */
	public static void test9090090(){
		ProxyMsgDao dao = new ProxyMsgDao();
		
		DBUtil util = new DBUtil("test","weibotools1");
		
//		List<Map<String, String>> ListMap = util.executeQuery("select cookie,username,machine_ip from tb_weibo_cookies where machine_ip='115.29.251.9'");
//		List<Map<String, String>> ListMap = util.executeQuery("select cookie,username,machine_ip from tb_weibo_cookies where machine_ip='115.29.248.186'");
		List<Map<String, String>> ListMap = util.executeQuery("select cookie,username,machine_ip from tb_weibo_cookies where machine_ip='218.244.129.80'");
		
		
		for (int i = 0; i < ListMap.size(); i++) {
			
			String cookie = ListMap.get(i).get("cookie");
			String username = ListMap.get(i).get("username");
			String ip = ListMap.get(i).get("machine_ip");
//		while(true){
//			String cookie = "login_sid_t=7ce3180daba3119509efbc50d4b9c614; YF-Ugrow-G0=9b1c05a2e1e1e84dd3e500248ab16d62; _s_tentry=www.baidu.com; UOR=www.baidu.com,weibo.com,login.sina.com.cn; Apache=5175454141240.008.1415279348662; SINAGLOBAL=5175454141240.008.1415279348662; ULV=1415279348679:1:1:1:5175454141240.008.1415279348662:; SUB=_2AkMjB_nAa8NlrABTkfwSxGLhaopH-jyQ1_82An7uJhIyHRgv7nkTqSW1JAXu0FumyPUx_j-Qd9q-z30Dbg..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WFIVHzQlGJK7l0QmS6-.cL55JpX5K2t; un=13411034249; YF-Page-G0=091b90e49b7b3ab2860004fba404a078; YF-V5-G0=bd9e74eeae022c6566619f45b931d426; ULOGIN_IMG=14152801911409; myuid=3187298814; SUS=SID-3294789367-1415280375-GZ-wrl59-b94c7110053456aabe7fc75a19d294ef; SUE=es%3Dc4737a23048393e5191344418c168917%26ev%3Dv1%26es2%3D920b25dbbef561216ad152fa3a2c905f%26rs0%3DBAPMpFyUABIx7DA5FqhNHcA8WXVZQETjdnW18fRKx%252BXm5jlxe1HNcOAIv8mwEpqw89RgYFmdyhgX5UabylsNdXEW8Rd1XaB%252BrmdYAgR6R80lCwUDYyktcMjkvICRwJAg7t%252FXKgfVXxXW8ib8rt4gdS2V%252BrLANpuJyEK2BZasvFQ%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1415280375%26et%3D1415366775%26d%3Dc909%26i%3D94ef%26us%3D1%26vf%3D2%26vt%3D1%26ac%3D2%26st%3D0%26uid%3D3294789367%26name%3D13411034249%2540sina.cn%26nick%3D%25E7%2594%25A8%25E6%2588%25B73294789367%26fmp%3D%26lcp%3D2013-02-27%252021%253A23%253A37; ALF=1415885136; SSOLoginState=1415280375";
			SpiderParams sp = new SpiderParams();
			sp.setUrl("http://s.weibo.com/weibo/KFC&Refer=index");
			sp.setCookie(cookie);
			SpiderResult rs = (SpiderResult) SpiderFactory.executeLocal(sp);
			System.out.println("count----"+count);
//			System.out.println(rs.getHtml().substring(0,1000));
			System.out.println("username : "+username+" ip : "+ip+"--"+count);
			if(rs.getHtml().contains("验证码错误")){
				System.out.println("**********************");
				System.out.println("---------屏蔽---------");
				System.out.println("**********************");
			}else{
				System.out.println("账号正常.......");
			}
			count++;
			
//			Thread.sleep(10000);
			
//		}
		}
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


	
	
}
