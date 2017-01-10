package com.timerchina.spider.service.HttpClient;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.conn.ssl.TrustStrategy;

/**
 * @author jason.lin@timerchina.com 2014-12-23 下午5:50:16
 *
 */
class AnyTrustStrategy implements TrustStrategy{
	
	/**
	 * 信任任何密钥的策略，不去考虑证书链和授权类型，均认为是受信任的
	 */
	public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		return true;
	}
	
}
