package com.timerchina.spider.service;

import java.util.Date;
import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.bean.InfoMsg;
import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.proxy.IProxySpider;
import com.timerchina.spider.service.utils.HtmlMarkCleanner;
import com.timerchina.spider.service.utils.RegexTools;
import com.timerchina.spider.service.utils.ZipUtils;

/**
 * 普通爬虫抓取程序
 * @author jason.lin@timerchina.com
 * 2014-7-16 上午11:24:59
 */
public class GeneralSpider extends ASpider{

	//搜狗专用cookie
	private static String SOGOU_COOKIE = "";
	
	public GeneralSpider() {
		super();
	}

	public GeneralSpider(IEncodeDetection encodeDetect,
			IShieldDetection shieldDetection, IHttpCapture httpCaptureService,
			ICryptogramProcessor secretData, IProxySpider iProxySpider) {
		super(encodeDetect, shieldDetection, httpCaptureService, secretData,iProxySpider);
	}

	/**
	 * 每个抓取的主程序
	 * @see com.timerchina.spider.service.ASpider#executeSpider(com.timerchina.spider.bean.SpiderParams)
	 */
	public Object executeSpider(SpiderParams spiderParams) {
		
		//执行父类的主方法，获取SpiderResult结果
		SpiderResult spiderResult = this.execute(spiderParams);
		if(spiderParams.getDomain().equalsIgnoreCase(Constant.SOGOU_DOMAIN)){
			if(spiderResult.getHtml().length() < Constant.SOGOU_LENGTH && spiderResult.getHtml().length() > 0){
				handleSogou(spiderParams, spiderResult);
				return spiderResult;
			}
		}else if (spiderParams.getDomain().equalsIgnoreCase(Constant.WEIBO_DOMAIN)
				&& spiderResult.getResponseCode() == SpiderResult.IP_OR_COOKIE_SHIELD) {
			executeWeibo(spiderResult, spiderParams);
			return spiderResult;
		}
		handleSourceCode(spiderParams, spiderResult);
		return spiderResult;
	}
	

	/**
	 * 处理新浪微博屏蔽，针对微博用户信息页面抓取
	 * @param spiderResult
	 * @param spiderParams
	 * @return
	 */
	public SpiderResult executeWeibo(SpiderResult spiderResult, SpiderParams spiderParams){
		String newUrl = RegexTools.getFirstMatchGroupOfFirstMatch(Constant.REGEX_WEIBO, spiderResult.getHtml());
		if(newUrl != null && newUrl.length() > 0){
			spiderParams.setUrl(newUrl);
			LoggerHandler.getInstance().writeLogger(InfoMsg.WEIBO_SECOND_REQUEST + newUrl, Constant.LOG_LEVEL_INFO);
			return this.execute(spiderParams);
		}
		return spiderResult;
	}
	
	/**
	 * 对源码进行处理，包括清除源码中的标签，压缩源码以便在网络中传输
	 * @param spiderParams
	 * @param spiderResult
	 */
	public void handleSourceCode(SpiderParams spiderParams, SpiderResult spiderResult){
//		//转换代码中的ASCII
//		spiderResult.setHtml(Native2AsciiUtils.ascii2Native(spiderResult.getHtml()));
		if(spiderParams.isCleanHttp()){
			//清除网页源码中的Http标签
			spiderResult.setHtml(HtmlMarkCleanner.cleanHtmlMark(spiderResult.getHtml()));
		}
		//如果用户选择要压缩，则不管是否请求Captain，都进行压缩。但是如果是请求爬虫中心的，直接压缩
		if(spiderParams.isGzip() || spiderParams.isProxy()){
			spiderResult.setHtml(ZipUtils.gZip(spiderResult.getHtml()));
		}
	}
	
	/**
	 * 针对搜狗微信进行特殊处理，因为搜狗微信需要本地生成一个cookie
	 * Annotation at 2015-07-06
	 * @return
	 */
//	public SpiderResult handleSogou(SpiderParams spiderParams,
//			SpiderResult spiderResult) {
////		boolean isNewCookie = false;
//		// 如果源码为空，则直接返回结果
//		if (spiderResult.getHtml().length() < 10) {
//			return spiderResult;
//		}
//		// 如果当前的SOGOU_COOKIE为空，则先从源码中获取cookie
//		System.out.println("这是最外面的cookie：" + SOGOU_COOKIE);
//		if (SOGOU_COOKIE.length() < 1) {
//			SOGOU_COOKIE = generateSogouCookie(spiderResult.getHtml());
////			isNewCookie = true;
//		}
//		
//		if(SOGOU_COOKIE != null){
//			spiderParams.setCookie(SOGOU_COOKIE);
//			spiderResult = this.execute(spiderParams);
////			if (isNewCookie == true) {
////				handleSourceCode(spiderParams, spiderResult);
////				return spiderResult;
////			} else 
////				
//				
//			if (spiderResult.getHtml().length() < Constant.SOGOU_LENGTH) {
//				SOGOU_COOKIE = generateSogouCookie(spiderResult.getHtml());
//				spiderParams.setCookie(SOGOU_COOKIE);
//				handleSourceCode(spiderParams, spiderResult);
//				return this.execute(spiderParams);
//			}
//		}
//		handleSourceCode(spiderParams, spiderResult);
//		return spiderResult;
//	}
	
	public SpiderResult handleSogou(SpiderParams spiderParams,
			SpiderResult spiderResult) {
		boolean isNewCookie = false;
		// 如果源码为空，则直接返回结果
		if (spiderResult.getHtml().length() < 10) {
			return spiderResult;
		}

		// 如果当前的SOGOU_COOKIE为空，则先从源码中获取cookie
		if (SOGOU_COOKIE != null && SOGOU_COOKIE.length() < 1) {
			SOGOU_COOKIE = generateSogouCookie(spiderResult.getHtml(),
					Constant.SOGOU_REGEX);
			isNewCookie = true;
		}
		if(SOGOU_COOKIE != null){
			spiderParams.setCookie(SOGOU_COOKIE);
			spiderResult = this.execute(spiderParams);
			if (isNewCookie == true) {
				handleSourceCode(spiderParams, spiderResult);
				return spiderResult;
			} else if (spiderResult.getHtml().length() < Constant.SOGOU_LENGTH) {
				SOGOU_COOKIE = generateSogouCookie(spiderResult.getHtml(),
						Constant.SOGOU_REGEX);
				spiderParams.setCookie(SOGOU_COOKIE);
				handleSourceCode(spiderParams, spiderResult);
				return this.execute(spiderParams);
			}
		}
		handleSourceCode(spiderParams, spiderResult);
		return spiderResult;
	}
	
	
	public String generateSogouCookie(String sourcecode, String regex){
		LoggerHandler.getInstance().writeLogger(InfoMsg.I_GENERATE_SOGOU_COOKIE, Constant.LOG_LEVEL_INFO);
		String prefixCookie = RegexTools.getFirstMatchGroupOfFirstMatch(regex, sourcecode);
		if(prefixCookie == null ){
			return null;
		}
		prefixCookie = prefixCookie.replace("document.cookie = \"", "").replace("\"", "");
		String prefixCookie2 = "SUV="+((new Date().getTime())*1000+Math.round(Math.random()*1000));
		String cookie = prefixCookie2+";"+prefixCookie+""+(new Date().getTime()+5*24*3600*1000);
		return cookie;
	}
	
	/**
	 * 当请求的域名是搜狗的时候，主动生成cookie
	 * @return
	 */
//	@SuppressWarnings("deprecation")
//	public String generateSogouCookie(String sourcecode){
//		LoggerHandler.getInstance().writeLogger(InfoMsg.I_GENERATE_SOGOU_COOKIE, Constant.LOG_LEVEL_INFO);
//		Date date = new Date();
//		String cookie = null;
//		if(SOGOU_COOKIE.indexOf("SUV=")<0){
//			System.out.println("这是index中的suv的位置："+SOGOU_COOKIE.indexOf("SUV="));
//			String regexStr = RegexTools.getFirstMatchGroupOfFirstMatch("document.cookie=\"SUV=.*?(;path.*?)\"", sourcecode);
//			cookie = "SUV="+(date.getTime())*1000+Math.round(Math.random()*1000)+regexStr+".sogou.com; ";
//		}
//		String rsString = RegexTools.getFirstMatchGroupOfFirstMatch("document.cookie = \"(SNUID[^\"]*?)\"", sourcecode);
//		date.setTime(date.getTime()+5*24*3600*1000);
//		if(cookie!=null && cookie.length() > 0){
//			cookie = cookie + rsString + date.toGMTString()+";domain=.sogou.com";
//		}else{
//			cookie = rsString + date.toGMTString()+";domain=.sogou.com";
//		}
//		return cookie;
//	}
	
	/**
	 * 微博抓取获取URL的方法
	 * @see com.timerchina.spider.service.ASpider#getURL()
	 */
	public String fetchRealURL(String url) {
		return url;
	}
}
