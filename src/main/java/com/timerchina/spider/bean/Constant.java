package com.timerchina.spider.bean;
/**
 * @author jason.lin@timerchina.com
 * @time 2014-7-23 下午04:35:04
 */
public final class Constant {
	
	/*
	 * Http头设置的常量
	 */
	public static final String USER_AGENT_V = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36";
	public static final String ACCEPT_ENCODING_V = "gzip, deflate, sdch";
	public static final String CONNECTION_V = "close";
	public static final String ACCEPT_LANGUAGE_V = "zh-CN,zh;q=0.8,zh-TW;q=0.6,en;q=0.4";
	public static final String ACCEPT_V = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
	public static final String GZIP = "gzip";
	public static final String CONTENT_TYPE = "Content-Type";
	
	public static final String SET_COOKIE = "Set-Cookie";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String REFERER = "Referer";
	public static final String COOKIE = "Cookie";
	public static final String USER_AGENT = "User-Agent";
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String ACCEPT_LANGUAGE = "Accept-Language";
	public static final String CONNECTION = "Connection";
	public static final String ACCEPT = "Accept";
	
	public static final String HTTP_LOCATION = "Location";
	public static final String HTTP_SLASH = "://";
	
	/*
	 * 网页相关的
	 */
	public static final String URL_CHARACTER_AND = "&";				//url中的&符号
	
	/*
	 * 获取编码
	 */
	public static final String WEB_CODE = "web_code";
	public static final String CHARSET_BY_META = "<meta.*?charset=(?:\"|)(.*?)\".*?>";
	public static final String CHARSET_BY_HEAD = "(?:gbk|gb2312|utf-8|utf-7|ascii|Unicode|big5|utf8|utf7)";

	/*
	 * 获取主机名
	 */
	public static final String URL_GETDOMAIN_REGEX = "domain";
	public static final String URL_DOMAIN2 = "(?<\\\\=http\\://|\\\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)";
	public static final String URL_DOMAIN = "(http|https)://(.+?(com:weiboPort|com|cn|net|org|biz|info|cc|tv|de))";
	public static final String URL_VALIDATE = "^(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$";
	public static final String SIMPLE_URL_VALIDATE = "^((http|https)://.*?)$"; 
	public static final String URL_REDIRECT_LOCATION = "Location";
	
	/*
	 * 默认屏蔽检测
	 */
	public static final String SHIELD_DEFAULTDOMAIN = "default.com";
	public static final String WEIBO_API_DOMAIN = "api.weibo.com";
	public static final String HOST_BAIDU = "baidu.com";
	
	/*
	 * 几个配置文件的名字
	 */
	public static final String PROXY_PROPS = "proxy.properties";
	public static final String SPIDER = "spider.properties";
	
	public static final String SOGOU_DOMAIN = "weixin.sogou.com";
	public static final String SOGOU_REGEX = "document.cookie = \"(SNUID[^\"]*?)\""; 
//	public static final String SOGOU_REGEX = "document.cookie=\"SUV=\".*?\"(;path.*?)\"";
	public static final int SOGOU_LENGTH = 1000;
	
	/*
	 * Ajax抓取的时候需要设置的
	 */
	public static final String AJAX_KEY = "X-Requested-With";
	public static final String AJAX_VALUE = "XMLHttpRequest";
	
	/*
	 * 判断URL是否是公司内部的新浪微博端口
	 */
	public static final String APIURL = "http://weibo.com:weiboPort";
	public static final String WEIBO_DOMAIN_INNER = "weibo.com:weiboPort";
	public static final String WEIBO_DOMAIN = "weibo.com";
	public static final String S_WEIBO_DOMAIN = "s.weibo.com";
	public static final String WEIBO_PORT = "weiboPort";
	public static final String WEIBO_DOMAIN_IN_PROPERTIES = "weibodomain";
	public static final String SOLDIER_IN_PROPERTIES = "soldierType";
	
	/*
	 * 配置文件读取的常量
	 */
	public static final String WINDOWS = "Windows"; 
	public static final String OS_NAME = "os.name";
	public static final String USER_DIR = "user.dir";
	public static final String PROPS_SHIELD = "shieldFeature.properties";
	public static final String PROPS_SPIDER = "spider.properties";
	
	/*
	 * 数据库连接的参数
	 */
	public static final String CONN_URL = "url";
	public static final String CONN_USERNAME = "user";
	public static final String CONN_PASSWORD = "password";
	public static final String CONN_DRIVER = "com.mysql.jdbc.Driver";
	public static final String PROPS_NAME = "mysql.properties";
	
	/*
	 * 代理中心相关的参数
	 */
	public static final String CAPTAIN_DOMAIN = "captain_domain";
	public static final String CAPTAIN_PORT = "captain_port";
	public static final String PROPS_PROXY = "proxy.properties";
	public static final String CAPTAIN_IP = "captain_ip";
	
	
	/*
	 * 连接captain的各种错误
	 */
	public static final String NETWORD_UNREACHABLE = "Network is unreachable";
	public static final String CONN_REFUSED = "Connection refused";
	
	/*
	 * 海外抓取相关
	 */
	public static final String OVERSEA_IP = "oversea_ip";
	public static final String OVERSEA_PORT = "oversea_port";
	
	/*
	 * 请求的方法(Post或者Get)
	 */
	public static final String POST_METHOD = "post";
	
	/*
	 * 新浪微博官方API的error code
	 */
	public static final int WEIBO_API_IP_LIMIT = 10022;
	public static final int WEIBO_API_ACCESS_TOKEN_LIMIT = 10023;
	public static final String WEIBO_API_ERROR_ACCESS = "error_code";
	
	/*
	 * 打log的等级
	 */
	public static final int LOG_LEVEL_DEBUG = 1;
	public static final int LOG_LEVEL_INFO = 2;
	public static final int LOG_LEVEL_WARN = 3;
	public static final int LOG_LEVEL_ERROR = 4;
	public static final int LOG_LEVEL_FATAL = 5;
	public static final int LOG_LEVEL_TRACE = 6;
	
	/*
	 * url中需要转义的字符
	 */
	public static final String SPECIAL_CHARACTOR = "special_charactor";	//配置文件spider.properties文件中的特殊字符名
	public static final String TRANS_CHARACTOR = "trans_charactor";		//配置文件spider.properties文件中的特殊字符对应的编码的名
	
	public static final String URL_MALFORM_PLUS_SIGN = "%2B";			//表示url中的+号
	public static final String URL_MALFORM_SPACE = "%20";				//空格
	public static final String URL_MALFORM_SLASH = "%2F";				//表示斜杠/
	public static final String URL_MALFORM_QUESTION = "%3F";			//表示问号?
	public static final String URL_MALFORM_PER_CENT = "%25";			//表示百分号%
	public static final String URL_MALFORM_POUND_SIGN = "%23";			//表示井号#
	public static final String URL_MALFORM_AND_SIGN = "%26";			//表示与符号&
	public static final String URL_MALFORM_EQUAL_SIGN = "%3D";			//表示等号=
	public static final String URL_MALFORM_Vertical_bar = "";			//表示|线
	
	public static final String URL_ROOT_PLUS_SIGN = "+";				
	public static final String URL_ROOT_SPACE = " ";
	public static final String URL_ROOT_SLASH = "//";
	public static final String URL_ROOT_QUESTION = "?";
	public static final String URL_ROOT_PER_SIGN = "%";
	public static final String URL_ROOT_POUND_SIGN = "#";
	public static final String URL_ROOT_AND_SIGN = "&";
	public static final String URL_ROOT_EQUAL_SIGN = "=";
	public static final String URL_ROOT_VERTICAL_BAR = "|";
	
	/*
	 * 字符串拆分字符
	 */
	public static final String SPLIT_COMMA = ",";
	public static final String SPLIT_DOT = "\\.";
	
	public static final String INDEX_DOT = ".";
	
	
	/*
	 * HTTP状态码
	 */
	//定义HTTP自带的状态码
    public static final int HTTP_SC = 200;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;
	
    
    //新浪微博特殊处理
    public static final String REGEX_WEIBO = "var back = function\\(\\) \\{\\s*var url = \"(.*?)\"";
    
	
}
