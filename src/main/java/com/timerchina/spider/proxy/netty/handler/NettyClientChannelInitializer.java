package com.timerchina.spider.proxy.netty.handler;

import com.timerchina.spider.proxy.netty.base.client.NettyClient;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

/**
 * @author jason.lin@timerchina.com 2014-11-4 下午2:06:49
 * pipeline工厂，初始化各种Handler
 */
public class NettyClientChannelInitializer extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel socket) throws Exception {
		ChannelPipeline pipeline = socket.pipeline();
		
		NettyClientHandler handler = new NettyClientHandler();
		pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));  
        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));  
        pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));  
        pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
        //设置超时时间
		pipeline.addLast(new IdleStateHandler(NettyClient.readTimeOut, 0, 0));
		pipeline.addLast(handler);
	}
}
