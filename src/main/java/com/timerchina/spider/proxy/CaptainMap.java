package com.timerchina.spider.proxy;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.timerchina.spider.bean.CaptainParams;
import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.bean.InfoMsg;
import com.timerchina.spider.service.LoggerHandler;
import com.timerchina.spider.service.utils.PropertiesTool;

/**
 * @author jason.lin@timerchina.com 2014-11-18 下午1:26:08
 * 保存所有Captain节点或者Domain的Map
 */
public class CaptainMap {

	//多线程锁
	private static ReadWriteLock lock = new ReentrantReadWriteLock();
	private static CaptainMap captainMap = new CaptainMap();
	private static List<CaptainParams> captainList = new ArrayList<CaptainParams>();
	private static int captainIndex = 0;
	private static List<CaptainParams> overseaList = new ArrayList<CaptainParams>();
	private static int overseaIndex = 0;
	private static String lastIP = "";
	private CaptainMap(){}
	
	/**
	 * 初始化节点信息到List中
	 */
	static{
		initCaptain();
		initOverSea();
	}
	
	/**
	 * 单例，所有对象共享一个Captain节点Map
	 * @return
	 */
	public static CaptainMap getInstance(){
		if(captainMap == null){
			Lock writelock = null;
			try{
				//写锁，所有对象共享，每次只能一个线程进行
				writelock = lock.writeLock();
				writelock.lock();
				if(captainMap == null){
					return captainMap = new CaptainMap();
				}
			}finally{
				if(writelock!=null){
					writelock.unlock();
				}
			}
		}
		return captainMap;
	}
	
	/**
	 * 初始化海外抓取的节点信息
	 */
	private static void initOverSea() {
		Lock readlock = lock.readLock();
		try{
			readlock.lock();
			if(overseaList == null || overseaList.size() < 1){
				String[] ip = getMsgFromProps(Constant.PROPS_SPIDER, Constant.OVERSEA_IP,",");
				String[] port = getMsgFromProps(Constant.PROPS_SPIDER, Constant.OVERSEA_PORT,",");
				for (int i = 0;i<ip.length;i++) {
					CaptainParams captainParams = new CaptainParams();
					captainParams.setPort(Integer.parseInt(port[i]));
					captainParams.setCaptain_IP(ip[i]);
					overseaList.add(captainParams);
				}
			}
		}finally{
			readlock.unlock();
		}
	}

	/**
	 * 初始化Captain的节点信息
	 */
	private static void initCaptain() {
		Lock writelock = lock.writeLock();
		try{
			writelock.lock();
			if(captainList == null || captainList.size() < 1){
//				String[] domain = getMsgFromProps(Constant.PROPS_PROXY, Constant.CAPTAIN_DOMAIN, ",");
				String[] domain = getMsgFromProps(Constant.PROPS_SPIDER, Constant.CAPTAIN_IP, ",");
				String[] port = getMsgFromProps(Constant.PROPS_SPIDER, Constant.CAPTAIN_PORT,",");
				for (int i = 0;i<domain.length;i++) {
					CaptainParams captainParams = new CaptainParams();
					captainParams.setDomain(domain[i]);
					captainParams.setPort(Integer.parseInt(port[i]));
//					captainParams.setCaptain_IP(getIPByHostName(domain[i]));
					captainParams.setCaptain_IP(domain[i]);
					captainList.add(captainParams);
				}
			}
		}finally{
			writelock.unlock();
		}
	}

	/**
	 * 读取配置文件内容，然后将内容保存到Map中
	 * @param propsName
	 * @param columName
	 * @param splitStr
	 * @return
	 */
	private static String[] getMsgFromProps(String propsName, String columName, String splitStr){
//		PropertiesTool.getSpiderInstance()
//		.getPropertiesOutOfJar(propsName, columName).split(splitStr);
		return PropertiesTool.getSpiderInstance()
				.getPropertiesOutOfJar(propsName, columName).split(splitStr);
	}
	
	
	/**
	 * 获取一个节点，由这个方法自动分配一个
	 * @return
	 */
	private CaptainParams getOneNode(List<CaptainParams> nodeList, int index ){
		Lock readLock = lock.readLock();
		try{
			readLock.lock();
			if(index > nodeList.size()){
				index = 0;
				LoggerHandler.getInstance().writeLogger(InfoMsg.E_CHECK_CAPTAIN_OR_NETWORK, Constant.LOG_LEVEL_ERROR);
			}
		}finally{
			readLock.unlock();
		}
		return nodeList.get(index);
	}
	
	/**
	 * 获取一个节点信息，是否需要海外抓取
	 * @param isOversea
	 * @return
	 */
	public CaptainParams getOneNode(boolean isOversea){
		if(isOversea){
			return getOneNode(overseaList, overseaIndex);
		}
		return getOneNode(captainList, captainIndex);
	}
	
	/**
	 * 通过域名来获取IP地址，格式必须是www.xxx.(com|cn|...);如果获取失败，则返回null
	 * @param hostName
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String getIPByHostName(String hostName){
		InetAddress address = null;
		try {
			address = InetAddress.getByName(hostName);
		} catch (UnknownHostException e) {
			LoggerHandler.getInstance().writeLogger(InfoMsg.E_GETIP_BY_HOST, Constant.LOG_LEVEL_ERROR);
			return null;
		}
		return address.getHostAddress().toString();
	}

	/**
	 * 设置captain的节点获取下标
	 * @param captainIndex
	 */
	public static void setCaptainIndex(String lastIP) {
		Lock writeLock = lock.writeLock();
		try{
			writeLock.lock();
			LoggerHandler.getInstance().writeLogger("captainIndex:"+captainIndex, Constant.LOG_LEVEL_INFO);
			//在多线程情况下，针对可能多次修改下标导致下标又回到原点
			if(lastIP.equalsIgnoreCase(CaptainMap.lastIP)){
				LoggerHandler.getInstance().writeLogger("请求的ip"+lastIP+"  上次请求的IP"+CaptainMap.lastIP, Constant.LOG_LEVEL_INFO);
			}else if(captainIndex < captainList.size() - 1){
				CaptainMap.lastIP = lastIP;
				captainIndex++;
			}
		}finally{
			writeLock.unlock();
		}
	}

	/**
	 * 设置海外抓取的节点获取下标
	 * @param overseaIndex
	 */
	public static void setOverseaIndex() {
		Lock writeLock = lock.writeLock();
		try{
			writeLock.lock();
			overseaIndex++;
		}finally{
			writeLock.unlock();
		}
	}
	
	/**
	 * 获取当前的节点总数
	 * @param isOversea
	 * @return
	 */
	public static int getCaptainIpCount(boolean isOversea){
		if(isOversea){
			return overseaList.size();
		}
		return captainList.size();
	}
	
	/**
	 * 获取当前captain 队列的下标(取到第几个)
	 * @param isOversea
	 * @return
	 */
	public static int getCaptainIndex(boolean isOversea){
		if(isOversea){
			return overseaIndex;
		}
		return captainIndex;
	}
}
