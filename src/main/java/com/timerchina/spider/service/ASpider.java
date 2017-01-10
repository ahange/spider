package com.timerchina.spider.service;

import java.io.IOException;
import java.net.UnknownHostException;

import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.bean.InfoMsg;
import com.timerchina.spider.bean.SCharSet;
import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.proxy.IProxySpider;
import com.timerchina.spider.service.utils.Native2AsciiUtils;

/**
 * 所有爬虫的抽象父类，实现公用的方法
 * @author jason.lin@timerchina.com
 * @time 2014-7-17 下午03:54:46
 */
public abstract class ASpider {

	private IEncodeDetection encodeDetect; // 编码识别
	private IShieldDetection shieldDetection; // 屏蔽检测
	private IHttpCapture httpCaptureService; // 网页抓取
	private ICryptogramProcessor cryptogramProcessor; // 加密解密
	private IProxySpider iProxySpider; // 使用爬虫调度中心

	public ASpider() {
	}

	public ASpider(IEncodeDetection encodeDetect,
			IShieldDetection shieldDetection, IHttpCapture httpCaptureService,
			ICryptogramProcessor cryptogramProcessor, IProxySpider iProxySpider) {
		super();
		this.encodeDetect = encodeDetect;
		this.shieldDetection = shieldDetection;
		this.httpCaptureService = httpCaptureService;
		this.cryptogramProcessor = cryptogramProcessor;
		this.iProxySpider = iProxySpider;
	}

	public ICryptogramProcessor getCryptogramProcessor() {
		return cryptogramProcessor;
	}

	public void setCryptogramProcessor(ICryptogramProcessor cryptogramProcessor) {
		this.cryptogramProcessor = cryptogramProcessor;
	}

	public IEncodeDetection getEncodeDetect() {
		return encodeDetect;
	}

	public void setEncodeDetect(IEncodeDetection encodeDetect) {
		this.encodeDetect = encodeDetect;
	}

	public IShieldDetection getShieldDetection() {
		return shieldDetection;
	}

	public void setShieldDetection(IShieldDetection shieldDetection) {
		this.shieldDetection = shieldDetection;
	}

	public IHttpCapture getHttpCaptureService() {
		return httpCaptureService;
	}

	public void setHttpCaptureService(IHttpCapture httpCaptureService) {
		this.httpCaptureService = httpCaptureService;
	}

	public IProxySpider getiProxySpider() {
		return iProxySpider;
	}

	public void setiProxySpider(IProxySpider iProxySpider) {
		this.iProxySpider = iProxySpider;
	}

	/**
	 * 爬虫的父类主方法
	 * 
	 * @return Object
	 */
	public SpiderResult execute(SpiderParams spiderParams) {
		LoggerHandler.getInstance().writeLogger(InfoMsg.I_REQUEST_URL + spiderParams.getUrl(), Constant.LOG_LEVEL_INFO);
		SpiderResult spiderResult = new SpiderResult();
		// 建立HTTP连接，获取网页源码，状态码等信息
		spiderResult = gainSpiderResult(spiderParams);
		if(spiderResult.getHtml().length() > 10){
			;
		}else if (!(spiderResult.getResponseCode() == SpiderResult.HTTP_SC)) {
			//如果不成功的话，有可能源码为空也有可能不为空，所以需要直接返回
			return spiderResult;
		}
		try {
			if (spiderParams.getCharset() != null && !spiderParams.getCharset()
					.equalsIgnoreCase(SCharSet.ISO_8859_1)) {
				// 转换代码中的ASCII
				spiderResult.setHtml(Native2AsciiUtils.ascii2Native(spiderResult.getHtml()));
			}
		} catch (NumberFormatException e) {
			LoggerHandler.getInstance().writeLogger(e.getCause()+"", Constant.LOG_LEVEL_ERROR);
		}
		
		// 屏蔽检测
		shieldDetection.isShield(spiderResult.getHtml(),
				spiderParams.getDomain(), spiderResult);
		if (spiderResult.isShield()) {
			LoggerHandler.getInstance().writeLogger(InfoMsg.I_SHIELD, Constant.LOG_LEVEL_INFO);
			spiderResult.spiderResultError(SpiderResult.IP_OR_COOKIE_SHIELD, InfoMsg.I_SHIELD, spiderResult);
		}
		LoggerHandler.getInstance().writeLogger("网页的编码是："+spiderResult.getCharSet(), Constant.LOG_LEVEL_INFO);
		return spiderResult;
	};

	/**
	 * 建立Http连接，并将源码，状态码信息保存到SpiderResult对象中
	 * @return
	 */
	public SpiderResult gainSpiderResult(SpiderParams spiderParams) {
		SpiderResult spiderResult = new SpiderResult();
		try {
			// 获取源码，里面进行源码解压
			spiderResult = httpCaptureService.captureWebPage(spiderParams);
		} catch (UnknownHostException e) {
			LoggerHandler.getInstance().writeLogger(InfoMsg.E_UNKNOW_HOST + spiderParams.getUrl(), Constant.LOG_LEVEL_ERROR);
			spiderResult.spiderResultError(SpiderResult.UNKNOW_HOST_ERROR,
							InfoMsg.E_UNKNOW_HOST + spiderParams.getUrl(),
							spiderResult);
		} catch (IOException e) {
			LoggerHandler.getInstance().writeLogger(InfoMsg.E_CAPTURE_SOURCODE_FAIL + e.getCause(), Constant.LOG_LEVEL_ERROR);
			spiderResult.spiderResultError(
					SpiderResult.CAPTURE_SOURCECODE_FAIL,
					InfoMsg.E_CAPTURE_SOURCODE_FAIL + "." + e.getCause(), spiderResult);
		}
		return spiderResult;
	}

	/**
	 * 所有子类执行程序的主方法
	 * 
	 * @param spiderParams
	 * @return
	 */
	public abstract Object executeSpider(SpiderParams spiderParams);

	/**
	 * 获取要抓取的网站的URL
	 * 
	 * @param url
	 * @return
	 */
	public abstract String fetchRealURL(String url);
}