package com.timerchina.spider.dao;

import com.timerchina.spider.bean.SpiderResult;

/**
 * 返回不同的结果类型
 * @author jason.lin@timerchina.com
 * @time 2014-7-18 上午09:46:29
 */
public interface SpiderCrawlerResultDao {

	/**
	 * 返回结果(HTML|JSON|SpiderResult)
	 * @param spiderResult
	 * @return
	 */
	public Object getSpiderResult(SpiderResult spiderResult);
}
