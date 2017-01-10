package com.timerchina.spider.proxy.netty.base.server;

import java.security.cert.CertificateException;
import javax.net.ssl.SSLException;

import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.bean.InfoMsg;
import com.timerchina.spider.proxy.netty.base.NettyChannelFutureListener;
import com.timerchina.spider.service.LoggerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * @author jason.lin@timerchina.com 2014-11-3 上午11:27:30
 * Netty服务端启动类
 */
public class NettyServer {

	private static int port = 0;
	static final boolean SSL = System.getProperty("ssl") != null;
	private static EventLoopGroup bossGroup = null;
    private static EventLoopGroup workerGroup = null;
    private static ServerBootstrap serverBoot = null;
    protected static SslContext sslCtx;
    private static NettyServerConfig config = null;
    
    /**
     * 带参数的构造方法，需要传入NettyConfig参数
     * @param nettyconfig
     * @throws CertificateException
     * @throws SSLException
     */
	public NettyServer(NettyServerConfig nettyconfig) throws CertificateException, SSLException{
		NettyServer.config = nettyconfig;
		init();
	}
	
	/**
	 * 服务端初始化
	 * @throws CertificateException
	 * @throws SSLException
	 */
	private void init() throws CertificateException, SSLException {
		//Configure SSL.
		initSSL();
		port = config.getPort();
		bossGroup = new NioEventLoopGroup(config.getBossGroupCount());
		workerGroup = new NioEventLoopGroup();
		serverBoot = new ServerBootstrap();
		serverBoot.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.INFO))
			.option(ChannelOption.SO_REUSEADDR, true)
			.childHandler(config.getChannelInitializer());
	}
	
	/**
	 * 需要SSl认证
	 * @throws CertificateException
	 * @throws SSLException
	 */
	private synchronized void initSSL() throws CertificateException, SSLException{
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
        } else {
            sslCtx = null;
        }
	}
	
	/**
	 * 启动netty服务端
	 * @throws InterruptedException 
	 */
	public void start() throws InterruptedException{
		LoggerHandler.getInstance().writeLogger(InfoMsg.I_START_NETTY_SERVER, Constant.LOG_LEVEL_INFO);
		NettyChannelFutureListener futureListener = new NettyChannelFutureListener();
		ChannelFuture channelFuture = serverBoot.bind(port).sync();
		channelFuture.addListener(futureListener);
	}
	
	/**
	 * 关闭Netty服务端
	 * @throws InterruptedException 
	 */
	public synchronized void stop() throws InterruptedException{
		if(workerGroup!=null && bossGroup!=null){
			if(!workerGroup.isShutdown()){
				workerGroup.shutdownGracefully();
			}
			if(!bossGroup.isShutdown()){
				bossGroup.shutdownGracefully();
			}
		}
	}
}
