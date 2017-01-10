package com.timerchina.spider;

import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.factory.SpiderFactory;


/**
 * @author jason.lin@timerchina.com 2014-12-17 上午10:24:56
 *
 */
public class TestForWeibo {

	public static void main(String[] args) {
		
		SpiderParams spiderParams = new SpiderParams();
		
		spiderParams.setCookie("Cookie:SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5aSaXLpQGDFam28bb51xbD5JpX5K2t; SINAGLOBAL=9953512743687.322.1398838148469; ULV=1420526288415:5592:54:54:9117695859485.912.1420526288388:1420526273134; wvr=6; SUS=SID-3822628793-1420526473-GZ-vkpen-2f6592277232929b97e0afc73f017505; SUE=es%3D8d0e9926000ca0778ee73d0a0e99b2b6%26ev%3Dv1%26es2%3Daac353729daa7a849cab71f077cc58b2%26rs0%3DOB0Wkjn4iqVGWvPNe5gWZYbX%252FRSlkQcExv%252BcQeqd2TGqhhJaBoO1sjl%252BtYugXMosQG9V08aiF5qkTEr9Raaah7w7YVQFdQrq67gzRxqV%252FRQGJXtt08pPnN5EJ24rrVs7WhkBxkGWDfivS8j5ypCCipHBa%252FSFmro65oyuIZteesc%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1420526473%26et%3D1420612873%26d%3Dc909%26i%3D7505%26us%3D1%26vf%3D1%26vt%3D3%26ac%3D18%26st%3D0%26uid%3D3822628793%26name%3D13153429425%26nick%3D%25E7%2582%25B8%25E9%2585%25B1%25E9%259D%25A2%25E8%25BF%2587%25E5%2585%25AD%25E7%25BA%25A7%26fmp%3D1%26lcp%3D2013-09-30%252020%253A35%253A47; SUB=_2A255r_PZDeTxGeVG6VAX8ibLwj-IHXVa35GRrDV8PUNbuNBeLWjzkW8ZllaBXcUvW5vk-TQYMX9l-3L9mw..; ALF=1452062473; SSOLoginState=1420526473; YF-Ugrow-G0=57484c7c1ded49566c905773d5d00f82; YF-Page-G0=f9cf428f7c30a82f1d66fab847fbb873");
		
		spiderParams.setUrl("http://s.weibo.com/weibo/%E4%B8%8A%E6%B5%B7%E5%A4%96%E6%BB%A9%E8%B8%A9%E8%B8%8F%E4%BA%8B%E6%95%85?topnav=1&wvr=6&topsug=1");
		
		SpiderResult sr = (SpiderResult)SpiderFactory.executeLocal(spiderParams);
		
		System.out.println(sr.getHtml());
		
	}
	
	
	
}
