package com.timerchina.spider;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.sf.json.JSON;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.db.DBUtil;
import com.timerchina.spider.factory.SpiderFactory;
import com.timerchina.spider.proxy.netty.base.utils.ConvertJsonAndObjectUtil;
import com.timerchina.spider.service.utils.JavaFile;
import com.timerchina.spider.service.utils.RegexTools;

/**
 * @author jason.lin@timerchina.com
 * @date 2014-11-12
 * @statement 
 */
public class TestAnything implements Runnable{

	private static Logger logger = Logger.getLogger(TestAnything.class);
	private static BlockingQueue<String> bq = new LinkedBlockingQueue<String>();
	
	public void run() {
		testzz();
	}
	
	
	public static void testzz(){
		
//		while (true) {
			
			long start = System.currentTimeMillis();
			String url = "http://192.169.7.33:8080/unified-information-disclosure-platform/ExchangedData/exchange?param=FB040004";
			SpiderParams sp = new SpiderParams(url);
			
			String data = "{\"UID\":\"0000108696\",\"BIZ_TYPE\":\"FUND\",\"RPT_TYPE\":\"FB040020\",\"STATUS\":\"2\",\"SEND_TIME\":\"20160520105706795\",\"FUND_CODE\":\"202301\",\"CLASS_CODE\":\"202301\",\"JJ_JC\":\"南方现金增利货币A\",\"FJJJ_JC\":\"南方现金增利货币A\",\"PL_DATE\":\"2015-04-14\",\"FE_JZ\":\"\",\"LJ_JZ\":\"\",\"ZC_JZ\":\"\",\"BF_SY\":\"\",\"WF_SY\":2.12466,\"BW_SY\":\"\",\"QT_HYL\":4.547,\"GZ_DATE\":\"2015-04-12\",\"F7010_0990\":\"A\"}";
			
 			sp.setlogger(true);
 			sp.setMethod("POST");
			sp.setHttpConnectionTimeout(120000);
			sp.setHttpReadTimeout(120000);
//			Map<String, String> methodValues = new HashMap<String, String>();
//			methodValues.put("param", data);
//			sp.setMethodValues(methodValues );
			
			SpiderResult rs = (SpiderResult)SpiderFactory.executeLocal(sp);
			
			long end = System.currentTimeMillis();
			logger.info(rs.getHtml().length()+ "  请求状态码：" + rs.getResponseCode() + "  错误信息：" + rs.getERRORMsg() + "  耗时："  + (end - start));
			System.out.println(rs.getHtml());
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		testzz();
	}
	
	static void download(){
		
		SpiderParams sp = new SpiderParams("http://guba.eastmoney.com/news,uscxdc,112176739.html");
		
		sp.setlogger(true);
		SpiderFactory.executeLocal(sp);
		
	}
	
	/**
	 * @statement ashdahsjdaksdjasdhk
	 * @param html
	 * @changedBy jason 2015-03-24 
	 */
	public static void saveFile(String html){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		
		File file = new File("log/"+sdf.format(date)+".png");
		if(file.isFile()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
		   OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"iso-8859-1");
		   BufferedWriter writer = new BufferedWriter(write);  
		   writer.write(html);
		   writer.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
