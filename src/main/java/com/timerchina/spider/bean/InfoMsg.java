package com.timerchina.spider.bean;
/**
 * @author jason.lin@timerchina.com 2014-12-9 上午11:26:55
 *
 */
public class InfoMsg {

	/*
	 * 跟请求Captain相关的错误信息
	 */
	public static final String E_READ_CAPTAIN_TIMEOUT = "从Captain获取抓取结果超时 .";
	public static final String E_NETWORD_UNREACHABLE = "网络不可用，请检查本地网络. ";
	public static final String E_REQUEST_REJECT = "请求被拒绝,查看Captain是否开启或者检查IP和端口：";
	public static final String E_UNKNOW_ERROR = "发生未知错误 .";
	public static final String E_CAPTURE_SOURCODE_FAIL = "获取源码失败或者连接超时，请检查网络或者URL是否正确";
	public static final String E_UNKNOW_HOST = "UnKnow Host错误,请检查URL : ";
	public static final String E_REQ_CAPTAIN_MAXRTIMES = "请求爬虫中心达到最大重试次数，";
	public static final String E_CONN_CAPTAIN_ERROR = "连接Captain失败.";
	public static final String E_UNEXPECTED_EXCEPTION_FROM_DOWNSTREAM = "未知错误的下行字节流.";
	public static final String E_UNSUPPORT_ENCODING = "不支持的编码类型.";
	public static final String E_CHECK_CAPTAIN_OR_NETWORK = "所有节点已经轮询一次，请检查爬虫中心节点或者网络是否正常.";
	public static final String E_GETIP_BY_HOST = "通过域名获取IP地址失败,请检查域名格式.";
	public static final String E_URL_ILLEGAL_CHARACTER = "url中包含非法字符(中文或者url特殊字符)，请先转码.";
	public static final String E_URL_ANALYZE_ERROR = "对URL解析错误.";
	
	/*
	 * 请求参数等错误
	 */
	public static final String E_URL_ERROR = "URL为空或者格式不正确，请检查URL是否正确 : ";
	
	/*
	 * 源码处理出错
	 */
	public static final String E_OVERSEA_ENCRYPT = "海外抓取加密出错.";
	public static final String E_SOURCECODE_FAIL_URL = "获取的源码为空，请检查URL是否可访问 : ";
	public static final String E_UNGZIP_ERROR = "解压字符串失败，原因可能是源码未压缩.";
	public static final String E_OVERSEA_DECRIPT_ERROR = "解密海外抓取的源码出错;";
	public static final String E_ENCODING_DETECT_ERROR = "编码识别失败，启用默认编码返回源码.";
	public static final String E_ENCRIPT_URL = "加密URL出错，错误原因：";
	public static final String E_SOURCECODE_NATIVE2ASCIIU = "转换源码中的ASCII码为本地编码出错.";
	
	/*
	 * 外层请求SpiderFactory时的错误信息
	 */
	public static final String E_LOCALSPIDER_OVERSEA = "这是本地抓取，isOverseaURL不能为true.";
	public static final String E_LOCALSPIDER_PROXY = "这是海外抓取，isProxy不能为true.";
	public static final String E_LOCALSPIDER_P_O = "isProxy和isOversea不能同时为true，因为这个还没实现.";
	public static final String E_URL_MALFORM_ERROR = "url编码出错，错误原因：";
	public static final String E_CAPTURE_GZIP_ERROR = "抓取流GZIP解压出错.";
	public static final String E_CAPTURE_READ_BYTE_ERROR = "读取字节流出错.";
	
	
	/*
	 * 屏蔽错误信息
	 */
	public static final String I_SHIELD = "IP或者Cookie被屏蔽.";
	
	/*
	 * 一些通知信息
	 */
	public static final String I_REQUEST_URL = "#INFO-抓取的URL地址为：";
	public static final String I_BEGIN_LOCAL = "#INFO-开启本地抓取.";
	public static final String I_BEGIN_OVERSEA = "#INFO-开始海外抓取   URL : ";
	public static final String I_BEGIN_PROXY = "#INFO-用代理中心抓取    URL : ";
	public static final String I_CHARSET_AFTER = "源码解析后的编码是.";
	public static final String I_SOURCECODE_LENGTH = "获取的源码的长度：";
	public static final String I_SPIDER_TYPE = "爬虫类型：";
	public static final String I_START_NETTY_SERVER = "#开启Netty服务端，等待请求...";
	public static final String I_SUCCESS_CONN_NETTYSERVER = "成功连接到Netty Server端.";
	public static final String I_CLIENT_DISCONNECT_NO_INBOUND = "长时间无数据传输，连接被断开.";
	
	/*
	 * Http请求（包括抓取，源码解析，编码识别这些的通知信息和错误信息）
	 */
	public static final String E_HTTP_GZIP_FAIL = "http请求成功，GZip解压缩源码失败.";
	
	
	/*
	 * Http请求超时
	 */
	public static final String I_HTTP_CONN_TIMEOUT = "Http连接超时，";
	public static final String I_HTTP_READ_TIMEOUT = "Http读取超时.";
	
	
	/*
	 * 登录验证网站
	 */
	public static final String I_USERNAME_OR_PASSWORD_ISNULL = "输入的用户名或者密码为空.";
	
	/*
	 * 新浪官方API信息
	 */
	public static final String E_ACCESS_TOKEN_LIMIT = "访问新浪微博API 用户请求频次超过上限.";
	public static final String E_API_IP_LIMIT = "访问新浪微博API IP请求频次超过上限.";
	public static final String E_API_SYSTEM_ERROR = "系统错误：";
	
	/*
	 * sogou微信抓取相关的信息
	 */
	public static final String I_SOGOU_IS_NEW_COOKIE = "cookie是最新生成的，但是还是无法获取到搜狗页面，请检查搜狗是否改版.";
	public static final String I_GENERATE_SOGOU_COOKIE = "生成搜狗微信二次请求cookie.";
	
	/*
	 * 处理Http请求头时候需要的信息
	 */
	public static final String I_HTTP_REDIRECT = "请求重定向，";
	
	/*
	 * 使用代理抓取的信息
	 */
	public static final String I_HTTP_PROXY = "设置代理信息：";
	
	/*
	 * 配置文件中URL特殊字符获取失败，请检查
	 */
	public static final String URL_CHARACTOR_ERROR = "从配置文件中获取url特殊字符失败，请检查配置文件是否正确.";
	
	/*
	 * 处理新浪微博抓取，关于用户页面抓取的二次请求结果
	 */
	public static final String WEIBO_SECOND_REQUEST = "新浪微博用户页面抓取二次请求url： ";
	
}
