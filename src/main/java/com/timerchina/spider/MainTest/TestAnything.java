package com.timerchina.spider.MainTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSON;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.factory.SpiderFactory;
import com.timerchina.spider.proxy.netty.base.utils.ConvertJsonAndObjectUtil;
import com.timerchina.spider.service.utils.PropertiesTool;

/**
 * @author jason.lin@timerchina.com 2014-11-12 下午6:12:40
 *
 */
public class TestAnything implements Runnable{

	public static boolean is_proxy = true;
	
	public static void main(String[] args) throws InterruptedException {
		
		test2();
	}
	
	public void run() {

	}
	
	public static void spider(){
		
		CloseableHttpClient httpClient = HttpClients.custom().build();
		
		HttpGet get = new HttpGet("http://localhost/TestSpider/servlet/Test");
		
		try {
			HttpResponse rs = httpClient.execute(get);
			
			HttpEntity entity = rs.getEntity();
			
			Charset charset = Charset.forName("GBK");
			
			System.out.println(EntityUtils.toString(entity, charset ));
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void test2(){
		while (true) {

			String url = PropertiesTool.getSpiderInstance().getPropertiesOutOfJar(Constant.PROPS_SPIDER, "url");

			SpiderParams sp = new SpiderParams(url);
			sp.setProxy(true);
			sp.setlogger(true);
			sp.setHttpConnectionTimeout(120000);
			sp.setHttpReadTimeout(120000);
//			sp.setCharset("iso-8859-1");
//			sp.setReferer("http://detail.tmall.com/item.htm?id=18470861845&amp;user_id=779817454");
//			sp.setCookie("thw=cn; cna=VWJdDyhi4icCAW/LIg0enlY0; miid=7965501456962342323; _tb_token_=SsTukY4swoue; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; whl=-1%260%260%261458561926339; v=0; uc1=cookie14=UoWyhus3UyXvfQ%3D%3D&existShop=false&cookie16=W5iHLLyFPlMGbLDwA%2BdvAGZqLg%3D%3D&cookie21=VT5L2FSpccLuJBrf3T8q&tag=7&cookie15=WqG3DMC9VAQiUQ%3D%3D&pas=0; uc3=sg2=W5oAcgME9Q%2BcrrWo%2B1b119rXWc%2BXdW1BgMtUvNgM8fg%3D&nk2=FaGIvohMAPUuXA%3D%3D&id2=UUwVZWm2NciIsQ%3D%3D&vt3=F8dASm2wMjoT29Ia0jo%3D&lg2=VT5L2FSpMGV7TQ%3D%3D; existShop=MTQ1ODU2MTk3NA%3D%3D; uss=ACJbRFqFvnKi05am53PjqYRGPuKsOWT7Yvb7Mv%2FWpz47E1VoA243c%2FZYgA%3D%3D; lgc=vv%5Cu88AB%5Cu6FC0%5Cu53D1%5Cu548C; tracknick=vv%5Cu88AB%5Cu6FC0%5Cu53D1%5Cu548C; cookie2=1c1bf87a940665d0461afd7f81d42a65; sg=%E5%92%8C85; mt=np=&ci=0_0; cookie1=WqGhc6g9pmc9fd4KdJvNmyZauMWbPnftIXpkaHbAzA8%3D; unb=2451491548; skt=087d42de904d28e3; t=1c503a84324e18b1d19b39d469d0fb9e; _cc_=WqG3DMC9EA%3D%3D; tg=0; _l_g_=Ug%3D%3D; _nk_=vv%5Cu88AB%5Cu6FC0%5Cu53D1%5Cu548C; cookie17=UUwVZWm2NciIsQ%3D%3D; l=AnR0oG1HCrrJfz9LwO1mgNHRxDjn9pg1");
			SpiderResult rs = (SpiderResult)SpiderFactory.execute(sp);

			System.out.println(rs.getHtml().length() + "--" + rs.getResponseCode() + "--" + rs.getERRORMsg());

			saveFile(rs.getHtml());

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
