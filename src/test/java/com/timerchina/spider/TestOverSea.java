package com.timerchina.spider;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.factory.SpiderFactory;

/**
 * @author jason.lin@timerchina.com 2014-11-19 下午2:27:39
 *
 */
public class TestOverSea implements Runnable{

	private static Logger logger = Logger.getLogger(TestOverSea.class);
	
	public static int count = 0;
	
	CountDownLatch latch;
    private int    num;

    
    public TestOverSea(int num, CountDownLatch latch) {
        this.num = num;
        this.latch = latch;

    }
	
	public TestOverSea(){}
	
	public static void main(String[] args) throws InterruptedException {
		
		TestOverSea s = new TestOverSea();
		
		int count = 4;
		
		// 开4个线程
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(count);
        // 每个线程一个阀门
        CountDownLatch latch = new CountDownLatch(count);
        
        try {

//        	while(true){
        		
        		for (int i = 0; i < count; i++) {
                    newFixedThreadPool.execute(new TestProperties(i, latch));
                }
                System.out.println("waiting!! for" + latch);
                System.out.println(" main thread over!!");
                newFixedThreadPool.shutdown();
//        	}
        	 
        } catch (Exception e) {
            // InterruptedException e
            e.printStackTrace();
        }
	}
	
	public void run() {
		 
//		 latch.countDown();
		long starttime = System.currentTimeMillis();
			
		System.out.println("这是开始时间："+starttime);
		SpiderParams spiderParams = new SpiderParams();
		
//		spiderParams.setProxy(true);
		
		spiderParams.setUrl("http://s.weibo.com/wb/kfc&xsort=time&timescope=custom:2014-11-19-0:2014-11-20-12&nodup=1");
		
		spiderParams.setCookie("SINAGLOBAL=754742126446.2173.1407834837336; myuid=1923406841; svr=s4.5; un=18758327040; wvr=6; SWB=usrmdinst_3; SSOLoginState=1416533109; NSC_wjq_txfjcp_mjotij=ffffffff094113d945525d5f4f58455e445a4a423660; _s_tentry=sports.sina.com.cn; Apache=7070213106926.53.1416533273310; ULV=1416533274332:52:14:6:7070213106926.53.1416533273310:1416460890585; WBStore=e73cbc1241619139|undefined; SUS=SID-1923406841-1416800573-XD-87rn1-80ebcacf2f30670e2a25c3bfd4d240e4; SUE=es%3Dd2c017ffa0ae3e0cc675fc1d6c51639f%26ev%3Dv1%26es2%3Dd8267eb75dbec95f9dafcda7c3743ad3%26rs0%3D4nAQM3cSmDM%252Fx2%252Fts%252FDTO4f8ib0UYA2%252B45%252FdGT7bO663w2LMDaZJeEKOjbA2b3iZbkiaQeEVgftukm%252FEWn%252FU5rShWoOd3RMiofremc%252Btp88FX%252BCA%252F0MuC2FJgLm8hADVZABZ84XzhwkcgqpnnJ9bkmjxHXiaZ8GVhx6bUt%252FQ6lo%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1416800573%26et%3D1416886973%26d%3Dc909%26i%3D40e4%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D0%26st%3D0%26uid%3D1923406841%26name%3D18758327040%2540sina.cn%26nick%3D%25E5%2593%2593%25E5%2587%25A1%25E6%2580%259D%25E5%25AF%2586%25E8%25BE%25BE%26fmp%3D%26lcp%3D2011-02-08%252014%253A11%253A47; SUB=_2A255dtltDeTxGedH6VEV8CjEzz2IHXVaBPslrDV8PUJbvNAPLWKgkW8EuVv-8Vnx1S45HxEsfTehRNjWFQ..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhVFq_Oqr2Vz1eupi9r3lKJ5JpX5K-t; ALF=1448328697; UOR=developer.51cto.com,widget.weibo.com,login.sina.com.cn");
		
		spiderParams.setCaptainIP("125.94.215.188");
		
		spiderParams.setCaptainPORT(10087);
		
		SpiderResult spiderResult = (SpiderResult) SpiderFactory.executeLocal(spiderParams);
		
		try{
			System.out.println(spiderResult.getHtml());
		}catch (Exception e) {
			System.out.println("获取源码失败");
		}
		long endTime = System.currentTimeMillis();
		
		System.out.println("总共耗时："+(endTime - starttime));
	 
//        latch.countDown();
        System.out.println("Task1 " + num + "complete!!"+count);
        
//        Thread.currentThread().interrupted();
		count++;
	}
	
	
	
	
	public static void test(){
		
		long starttime = System.currentTimeMillis();
		
		SpiderParams spiderParams = new SpiderParams();
		spiderParams.setUrl("http://www.baidu.com");
		
		spiderParams.setProxy(true);
		
		spiderParams.setCaptainIP("127.0.0.1");
		spiderParams.setCaptainPORT(10087);
		
		SpiderResult spiderResult = (SpiderResult) SpiderFactory.executeLocal(spiderParams);
		
		logger.info(spiderResult.getHtml().substring(0,1000));
		
		
		System.out.println(spiderResult.getRealUrl());
		
		long endTime = System.currentTimeMillis();
		
		logger.info("总共耗时-----------："+(endTime - starttime));
		
		
		
		
	}
	
}
