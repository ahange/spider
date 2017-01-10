package com.timerchina.spider.service;

import org.apache.log4j.Logger;

/**
 * @author jason.lin@timerchina.com 2015-1-28 上午11:52:42
 * @date 2015-01-28 
 * @statement 这个类是用来输出日志的，用户可选择是否输出日志，由SpiderParams里面的
 * 			  一个参数isLogger来决定是否打印Logger
 */
public class LoggerHandler {

	private LoggerHandler(){};
	private Logger logger = Logger.getLogger(LoggerHandler.class);
	private static LoggerHandler loggerHandler = new LoggerHandler();
	private static Object lock = new Object();
	public static boolean LOG_ON = false;
	
	/**
	 * 对象初始化
	 * @return
	 */
	public static LoggerHandler getInstance(){
		if(loggerHandler == null ){
			synchronized (lock) {
				if(loggerHandler == null ){
					loggerHandler = new LoggerHandler();
					return loggerHandler;
				}
			}
		}
		return loggerHandler;
	}
	
	/**
	 * 打印log，根据传递过来的打印log的等级来选择打印方式
	 * @param logger
	 * @param msg
	 * @param loglevel
	 */
	public void writeLogger(String msg, int loglevel){
		if(!LOG_ON){
//			Level level = new Level();
//			logger.setLevel(level );
		}else{
			switch (loglevel) {
			case 1:
				logger.debug(msg);
				break;
			case 2:
				logger.info(msg);	
				break;
			case 3:
				logger.warn(msg);
				break;
			case 4:
				logger.error(msg);
				break;
			case 5:
				logger.fatal(msg);
				break;
			default:
				logger.info(msg);
				break;
			}
		}
	}
	
	/**
	 * 打印log，根据传递过来的打印log的等级来选择打印方式
	 * @param logger
	 * @param msg
	 * @param loglevel
	 */
	public void writeLogger(String msg, int loglevel, Exception e){
		if(!LOG_ON){

		}else{
			StringBuffer output = new StringBuffer(msg + "  ");
			StackTraceElement[] ex = e.getStackTrace();
			for (StackTraceElement stackTraceElement : ex) {
				output.append(stackTraceElement.getClassName())
						.append("(" + stackTraceElement.getFileName())
						.append(":" + stackTraceElement.getLineNumber() + ")");
			}
			switch (loglevel) {
			case 1:
				logger.debug(output.toString());
				break;
			case 2:
				logger.info(output.toString());	
				break;
			case 3:
				logger.warn(output.toString());
				break;
			case 4:
				logger.error(output.toString());
				break;
			case 5:
				logger.fatal(output.toString());
				break;
			default:
				logger.info(output.toString());
				break;
			}
		}
	}
}
