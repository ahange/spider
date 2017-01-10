package com.timerchina.spider.service;

import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.bean.InfoMsg;
import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.proxy.IProxySpider;
import com.timerchina.spider.service.ASpider;
import com.timerchina.spider.service.utils.ZipUtils;

/**
 * @author jason.lin@timerchina.com
 * 2014-7-16 上午11:27:06
 */
public class OverseasSpider extends ASpider {
	
	public OverseasSpider() {
		super();
	}

	public OverseasSpider(IEncodeDetection encodeDetect,
			IShieldDetection shieldDetection, IHttpCapture httpCaptureService,
			ICryptogramProcessor secretData, IProxySpider iProxySpider) {
		super(encodeDetect, shieldDetection, httpCaptureService, secretData,iProxySpider);
	}
	
	/**
	 * 境外抓取的主程序
	 * @see com.timerchina.spider.service.ASpider#executeSpider(com.timerchina.spider.bean.SpiderParams)
	 */
	public Object executeSpider(SpiderParams spiderParams) {
		
		//初始化接口的实现类
		SpiderResult spiderResult = this.execute(spiderParams);
		String htmlEncrypt = spiderResult.getHtml();
		try {
			//转换为本地GBK编码
			htmlEncrypt = this.getCryptogramProcessor().encrypt(htmlEncrypt);
			//回传结果的时候，URL也需要加密
			spiderResult.setRealUrl(this.getCryptogramProcessor().encrypt(spiderResult.getRealUrl()));
		} catch (Exception e) {
			LoggerHandler.getInstance().writeLogger(InfoMsg.E_OVERSEA_ENCRYPT, Constant.LOG_LEVEL_ERROR);
			spiderResult.spiderResultError(SpiderResult.ENCRIPTION_ERROR,
					InfoMsg.E_OVERSEA_ENCRYPT, spiderResult);
			return spiderResult;
		}
//		ZipUtils zip = new ZipUtils();
		//源码进行压缩
		spiderResult.setHtml(ZipUtils.gZip(htmlEncrypt));
		return spiderResult;
	}
	
	/**
	 * 获取解密后的URL地址
	 * @see com.timerchina.spider.service.ASpider#getURL(java.lang.String)
	 */
	public String fetchRealURL(String url) {
		
		return url;
	}
}
