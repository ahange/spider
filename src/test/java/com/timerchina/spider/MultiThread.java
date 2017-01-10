package com.timerchina.spider;

import org.apache.log4j.Logger;

import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.factory.SpiderFactory;

/**
 * Created by abu on 16/5/9.
 */
public class MultiThread {

	private static Logger LOG = Logger.getLogger(TestAnything.class);

    public void multi(){

        for (int i = 0; i < 100; i++) {
            new Thread(new t()).start();
        }
    }

    public static void main(String[] args) {
        new MultiThread().multi();
    }

    class t implements Runnable{

        @Override
        public void run() {
            while (true){

                SpiderParams sp = new SpiderParams();
                sp.setUrl("https://www.taobao.com");
                SpiderResult spiderResult = (SpiderResult) SpiderFactory.executeLocal(sp);

                LOG.info(spiderResult.getResponseCode()+"");

            }
        }
    }
}
