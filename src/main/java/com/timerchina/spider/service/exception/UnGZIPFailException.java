package com.timerchina.spider.service.exception;

import java.io.IOException;

/**
 * @author jason.lin@timerchina.com 2014-12-9 下午2:52:24
 *
 */
public class UnGZIPFailException extends IOException{
	
	private static final long serialVersionUID = -7566407402963379672L;

	public UnGZIPFailException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnGZIPFailException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UnGZIPFailException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UnGZIPFailException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
