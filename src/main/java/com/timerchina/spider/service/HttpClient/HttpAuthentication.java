package com.timerchina.spider.service.HttpClient;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.bean.InfoMsg;
import com.timerchina.spider.bean.ProxyParams;
import com.timerchina.spider.service.LoggerHandler;

/**
 * @author jason.lin@timerchina.com 2014-12-23 下午3:42:25
 * 主要处理网页登陆验证，HttpClient支持的认证方式：
 * 	1：NTLM 
 * 	2：SPNEGO
 * 	3：其余的所有HTTP定义的认证方式(基本，摘要...)
 * 		(1)基本认证 Basic：由Http 1.0提出的，即输入用户名密码，采用非加密的明文传送
 * 		(2)摘要认证：由Http 1.1提出的替代1.0中的基本认证
 * 详细信息：http://blog.csdn.net/hotnet522/article/details/5824716
 */
public class HttpAuthentication {
	
	private ConnectionConfig connConfig;
	private ConnectionSocketFactory plainSF;
	private KeyStore trustStore;
	private SSLContext sslContext;
	private LayeredConnectionSocketFactory sslSF;
	private Registry<ConnectionSocketFactory> registry;
	private PoolingHttpClientConnectionManager connManager;
	public static String defaultEncoding= "utf-8";
	
	/**
	 * 对于需要登录验证的网站，因为有些网站网页还没加载的时候就需要登录验证(apache弹框验证)
	 * 这个验证是Basic认证，所有账号密码都是明文传送
	 * @example https://servicebox.peugeot.com/
	 * @param httpClientContext
	 * @param username
	 * @param password
	 */
	public void credentialsWithNameAndPassword(HttpClientContext httpClientContext, String username, String password){
		if (username == null || username.length() < 1 || password == null
				|| password.length() < 1) {
			//If null, do nothing
			;
		}else{
			//认证提供类
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			//设置用户名和密码
			UsernamePasswordCredentials credential = new UsernamePasswordCredentials(
					username, password);
			credentialsProvider.setCredentials(AuthScope.ANY, credential);
			httpClientContext.setCredentialsProvider(credentialsProvider);
		}
	}
	
	/**
	 * 使用代理进行抓取
	 * @param ProxyParam
	 * @param context
	 * @param spiderParams
	 */
	public void requestWithProxy(HttpClientContext context, ProxyParams proxyParams){
		LoggerHandler.getInstance().writeLogger(InfoMsg.I_HTTP_PROXY+proxyParams.getIp()+"/"+proxyParams.getPort()+"  [ "+proxyParams.getUsername()+" : "+proxyParams.getPassword()+" ]", Constant.LOG_LEVEL_INFO);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(
						proxyParams.getUsername(),
						proxyParams.getPassword()));
		int port = proxyParams.getPort();
		HttpHost targetHost = new HttpHost(proxyParams.getIp(),
				port, "http");
		AuthCache authCache = new BasicAuthCache();
		authCache.put(targetHost, new BasicScheme());
		context.setCredentialsProvider(credsProvider);
		context.setAuthCache(authCache);
	}
	
	/**
	 * @statement 这个方法的作用是实现访问自签名https网站
	 * 参考：http://blog.csdn.net/chaijunkun/article/details/40145685
	 * @example 12306余票查询网站
	 */
	public PoolingHttpClientConnectionManager selfSignedCertificate(){
		//下面建立一个密钥存储容器
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
		plainSF = new PlainConnectionSocketFactory();
		registryBuilder.register("http", plainSF);
		//指定信任密钥存储对象和连接套接字工厂
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, new AnyTrustStrategy()).build();
			sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			registryBuilder.register("https", sslSF);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
//		connConfig = ConnectionConfig.custom().setCharset(Charset.forName(defaultEncoding)).build();
		registry = registryBuilder.build();
		//设置连接管理器
		connManager = new PoolingHttpClientConnectionManager(registry);
		connManager.setDefaultConnectionConfig(connConfig);
		return connManager;
	}
}
