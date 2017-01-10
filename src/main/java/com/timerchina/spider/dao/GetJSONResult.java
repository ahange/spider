package com.timerchina.spider.dao;

import net.sf.json.JSONArray;

import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.dao.SpiderCrawlerResultDao;

/**
 * 返回JSON格式的结果
 * @author jason.lin@timerchina.com
 * @time 2014-7-18 上午09:51:13
 */
public class GetJSONResult implements SpiderCrawlerResultDao {

	/*
	 * 返回JSON格式的结果
	 */
	public Object getSpiderResult(SpiderResult spiderResult) {
		return JSONArray.fromObject(spiderResult);
	}
}
