package com.timerchina.spider.proxy.netty.base;

import io.netty.channel.Channel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author jason.lin@timerchina.com 2014-11-10 上午10:33:03
 * 保存所有请求参数的类，包括所有请求的Channel，SpiderParams，SpiderResult
 */
public class NettyChannelMap {
	
	private NettyChannelMap(){}
	//保存每个通道的map
	private static Map<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();
	private static NettyChannelMap nettyResultMap = new NettyChannelMap();
	private static ReadWriteLock lock = new ReentrantReadWriteLock();
	
	/**
	 * 使用单例，保证所有线程共享一个Map对象
	 * @return
	 */
	public static NettyChannelMap getInstance(){
		if(nettyResultMap==null){
			synchronized (NettyChannelMap.class) {
				if(nettyResultMap==null){
					return new NettyChannelMap();
				}
			}
		}
		return nettyResultMap;
	}
	
	/**
	 * 获取一个Channel对象
	 * @param channelID
	 * @return
	 */
	public Channel getOneChannel(String channelID){
		if(channelMap.containsKey(channelID)){
			return channelMap.get(channelID);
		}
		return null;
	}
	
	/**
	 * 插入一个Channel
	 * @param channelID
	 * @param channel
	 * @return
	 */
	public boolean addChannel(String channelID, Channel channel){
		if(channelMap.size()<2000){
			channelMap.put(channelID, channel);
			return true;
		}
		return false;
	}
	
	/**
	 * 移除一个channel
	 * @param channelID
	 * @return
	 */
	public boolean removeChannel(String channelID){
		if(channelMap.containsKey(channelID)){
			channelMap.remove(channelID);
			return true;
		}
		return false;
	}
	
	/**
	 * 关闭相应ChannelID的Channel，并且从Map中移除该Channel
	 * @param channelID
	 * @return
	 */
	public boolean removeChannelAndClose(String channelID){
		Lock writeLock = lock.writeLock();
		writeLock.lock();
		try{
			if(channelMap.containsKey(channelID)){
				Channel channel = channelMap.get(channelID);
				channel.close();
				channelMap.remove(channelID);
				return true;
			}
		}finally{
			writeLock.unlock();
		}
		return false;
	}
	
}
