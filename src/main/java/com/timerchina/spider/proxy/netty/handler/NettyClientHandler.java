package com.timerchina.spider.proxy.netty.handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.bean.InfoMsg;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.proxy.netty.base.utils.ConvertJsonAndObjectUtil;
import com.timerchina.spider.service.LoggerHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author jason.lin@timerchina.com 2014-11-3 下午8:07:33
 * @statement 客户端请求服务器类，向服务端发送请求参数，获取服务器返回的结果
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<Object>{

	private BlockingQueue<Object> responseRes = new ArrayBlockingQueue<Object>(1);
	/**
	 * 当用户选择阻塞Netty模式的时候，通过这个方法获取结果
	 * @return
	 * @throws InterruptedException 
	 */
	public Object getFactorial(int readTime) throws InterruptedException{
		return responseRes.poll(readTime,TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 从服务器读取结果,如果异常，直接关闭连接，释放资源
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		SpiderResult spiderResult = (SpiderResult) ConvertJsonAndObjectUtil
				.Json2Object(SpiderResult.class, (String) msg);
		responseRes.offer(spiderResult);
		ctx.channel().close();
	}

	/**
	 * 关闭Channel的一些操作
	 * @param ctx
	 */
	public void closeChannel(ChannelHandlerContext ctx){
		ctx.channel().close();
		ctx.channel().disconnect();
	}
	
	/**
	 * 当前通道被激活
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LoggerHandler.getInstance().writeLogger(InfoMsg.I_SUCCESS_CONN_NETTYSERVER, Constant.LOG_LEVEL_INFO);
	}

	/**
	 * 读取完成
	 */
	@Override
	public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
//		closeChannel(ctx);
	}

	/**
	 * 连接服务器超时检测
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (!(evt instanceof IdleStateEvent)) {
            return;
        }
        IdleStateEvent e = (IdleStateEvent) evt;
        if (e.state() == IdleState.READER_IDLE) {
        	LoggerHandler.getInstance().writeLogger(InfoMsg.I_CLIENT_DISCONNECT_NO_INBOUND, Constant.LOG_LEVEL_INFO);
            ctx.close();
        }
	}

	/**
	 * 异常捕捉
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		LoggerHandler.getInstance().writeLogger(InfoMsg.E_UNEXPECTED_EXCEPTION_FROM_DOWNSTREAM, Constant.LOG_LEVEL_ERROR);
        ctx.close();
	}
}
