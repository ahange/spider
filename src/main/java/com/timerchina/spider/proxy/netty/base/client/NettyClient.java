package com.timerchina.spider.proxy.netty.base.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.net.ssl.SSLException;

import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.bean.InfoMsg;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.proxy.netty.base.utils.ConvertJsonAndObjectUtil;
import com.timerchina.spider.proxy.netty.exception.ReadCaptainTimeoutException;
import com.timerchina.spider.proxy.netty.handler.NettyClientHandler;
import com.timerchina.spider.service.LoggerHandler;

/**
 * @author jason.lin@timerchina.com 2014-11-3 上午11:27:41
 * @statement netty客户端
 */
public class NettyClient {
	static final boolean SSL = System.getProperty("ssl") != null;
	private static NettyClientConfig config = null;
	private static EventLoopGroup group = new NioEventLoopGroup(20);;
	private static Bootstrap bootstrap = null;
	public static int readTimeOut = 3000;
	static SslContext sslCtx;
	protected String host;
	protected int port;
	private static ReadWriteLock lock = new ReentrantReadWriteLock();
	
	/**
	 * 带参数的构造方法传入一个配置类
	 * 
	 * @param config
	 * @throws InterruptedException
	 * @throws SSLException
	 */
	public NettyClient(NettyClientConfig config) throws InterruptedException,
			SSLException {
		NettyClient.config = config;
		this.host = config.getIp();
		this.port = config.getPort();
		init();
	}

	/**
	 * 初始化SSL
	 * 
	 * @throws SSLException
	 */
	private static void initSSL() throws SSLException {
		if (SSL) {
			sslCtx = SslContext
					.newClientContext(InsecureTrustManagerFactory.INSTANCE);
		} else {
			sslCtx = null;
		}
	}

	/**
	 * 初始化客户端程序，只初始化一次
	 * 
	 * @throws InterruptedException
	 * @throws SSLException
	 */
	private static void init() throws InterruptedException, SSLException {
		initSSL();
		Lock writeLocak = lock.writeLock();
		writeLocak.lock();
		if (bootstrap == null) {
			bootstrap = new Bootstrap();
			bootstrap.group(group)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.SO_KEEPALIVE, config.isKeep_alive())
					.option(ChannelOption.TCP_NODELAY, config.isTcp_nodelay())
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
							config.getConnectTimeOut())
					.handler(config.getChannelInitializer());
		}
		writeLocak.unlock();
	}

	/**
	 * 开启客户端的方法，直接传递参数，请求同步，获取结果异步
	 * 
	 * @throws InterruptedException
	 */
	public void start(Object object) throws InterruptedException {
		ChannelFuture channelFuture = connect();
		if (channelFuture.isSuccess()) {
			channelFuture.channel().writeAndFlush(
					ConvertJsonAndObjectUtil.object2Json(object));
		}
	}

	/**
	 * 开启客户端，连接服务器，这里只负责连接，异步请求方式
	 * 
	 * @throws InterruptedException
	 */
	public void start() throws InterruptedException {
		connect();
	}

	/**
	 * 阻塞请求方式，直接返回结果，请求与获取结果都同步
	 * 
	 * @param isBlock
	 * @return
	 * @throws InterruptedException
	 * @throws ReadCaptainTimeoutException
	 */
	public Object start(Object object, ChannelFuture channelFuture, int readTimeout)
			throws InterruptedException, ReadCaptainTimeoutException,
			IllegalArgumentException {
		SpiderResult spiderResult = new SpiderResult();
		try{
			//连接成功的时候，开始传递数据
			if(channelFuture.isSuccess()){
				channelFuture.channel().writeAndFlush(
						ConvertJsonAndObjectUtil.object2Json(object));
				NettyClientHandler handler = (NettyClientHandler) channelFuture
						.channel().pipeline().last();
				spiderResult = (SpiderResult) handler.getFactorial(readTimeout);
			}else{
				LoggerHandler.getInstance().writeLogger(InfoMsg.E_CONN_CAPTAIN_ERROR, Constant.LOG_LEVEL_ERROR);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			//关闭channel通道
			if (channelFuture.channel().isActive()) {
				channelFuture.channel().close();
				channelFuture.channel().disconnect();
			}
		}
		return spiderResult;
	}

	/**
	 * 打开连接
	 * @return
	 * @throws InterruptedException
	 */
	public ChannelFuture connect() throws InterruptedException,
			IllegalArgumentException {
		ChannelFuture channelFuture = null;
		String host = config.getIp();
		int port = config.getPort();
		channelFuture = bootstrap.connect(host, port).sync();
		return channelFuture;
	}
	
	/**
	 * 关闭客户端所有链接和资源
	 */
	public static void stop() {
		if (group != null && !group.isShutdown()) {
			group.shutdownGracefully();
		}
	}
}
