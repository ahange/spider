package com.timerchina.spider.proxy.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.UnsupportedEncodingException;

import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.bean.InfoMsg;
import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.factory.SpiderFactory;
import com.timerchina.spider.proxy.netty.base.utils.ConvertJsonAndObjectUtil;
import com.timerchina.spider.service.LoggerHandler;

/**
 * @author jason.lin@timerchina.com 2014-11-3 下午8:07:33
 * @param <String>
 * @param <Channel>
 * @statement 服务端的事件处理类，接收客户端请求，转发请求参数，写客回户端结果
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<Object>{

	/**
	 * 在NettyServerChannelInitializer的pipeline通道中已经添加了String处理器，已经把所有的ByteBuf
	 * 装换为String字符串，所以这里的msg已经是String，这里要做的就是把
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		String params = (String) msg;
		params = params.replaceAll("\\\\", "");
		params = params.substring(1,params.length()-1);
		
		SpiderParams spdierParams = (SpiderParams) ConvertJsonAndObjectUtil
				.Json2Object(SpiderParams.class, params);
		//将唯一标识符和客户端请求的信息保存到一个专门放请求的请求集合中
		SpiderResult spiderResult = new SpiderResult();
		spiderResult = (SpiderResult) SpiderFactory.execute(spdierParams);
		ChannelFuture channelFuture = ctx.channel().writeAndFlush(
				ConvertJsonAndObjectUtil.object2Json(spiderResult));
		channelFuture.addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 当每个客户端请求的结果读取完成后，且获取爬取结果，则返回结果，关闭连接
	 */
//	@Override
	public void channelReadComplete123(ChannelHandlerContext ctx) throws Exception {
		
	}
	
	/**
	 * 爬取超时，回写自定义结果
	 * @param channel
	 */
	public void readSpiderResultTimeout(Channel channel, SpiderParams spiderParam){
		SpiderResult spiderResult = new SpiderResult();
		spiderResult.setRealUrl(spiderParam.getUrl());
		spiderResult.setChannelID(spiderParam.getChannelID());
		spiderResult.setResponseCode(800);
		try {
			spiderResult.setHtml(new String("爬虫中心访问超时!".getBytes("utf-8")));
		} catch (UnsupportedEncodingException e) {
			LoggerHandler.getInstance().writeLogger(InfoMsg.E_UNSUPPORT_ENCODING, Constant.LOG_LEVEL_ERROR);
		}
		//不管回写成功还是失败，都强制关闭通道
//		if(!writebackResult(channel, spiderResult)){
//			closeMapAndChannel(spiderParam.getChannelID());
//		}
	}
	
	/**
	 * 往客户端回写数据
	 * @param channel
	 * @param spiderResult
	 * @return
	 */
//	public boolean writebackResult(Channel channel, SpiderResult spiderResult){
//		ChannelFuture channelFuture = channel.writeAndFlush(ConvertJsonAndObjectUtil
//						.object2Json(spiderResult));
//		if(channelFuture.isSuccess()){
//			//关闭通道，移除所有Map
//			closeMapAndChannel();
//			return true;
//		}
//		return false;
//	}

	/**
	 * 异常捕捉
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
        cause.printStackTrace();
        ctx.close();
	}
}
