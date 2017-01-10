package com.timerchina.spider.proxy.netty.exception;
/**
 * @author jason.lin@timerchina.com 2014-11-27 下午2:33:58
 * 读取Captain超时
 */
public class ReadCaptainTimeoutException extends Exception{
	private static final long serialVersionUID = 1L;

	public ReadCaptainTimeoutException() {
		super();
	}

	public ReadCaptainTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReadCaptainTimeoutException(String message) {
		super(message);
	}

	public ReadCaptainTimeoutException(Throwable cause) {
		super(cause);
	}
}
