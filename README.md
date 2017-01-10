# spider

1.使用说明
	
	#所有请求参数都要封装到SpiderParams[具体字段说明，请看最后的说明1]里面
	SpiderParams sp = new SpiderParams();
	
	#需要抓取的url
	sp.setUrl("http://www.baidu.com");
	
	#通过SpiderFactory执行抓取程序
	SpiderResult rs = (SpiderResult)SpiderFactory.execute(sp);
	
	#所有的抓取结果保存在SpiderResult[具体字段说明，请看最后的说明2]中，输出的结果编码已经统一成utf-8
	System.out.println(rs.getHtml());



	
说明1：SpiderParams

	属性	说明	默认值
	url							抓取源码的地址(必须的)					空
	method						请求方式(POST|GET)						get
	cookie						网站的cookie(必须的)					空
	charset						网页的编码								空
	referer						定位网页来向连接						空
	httpConnectionTimeout		http链接超时时间						2000ms
	isOverseaURL				是否境外(目前不开放接口)				false
	httpReadTimeout				http读取超时							5000ms
	isProxy						是否需要使用代理						false
	isAjax						是否Ajax(RTM)							false
	user_agent					浏览器信息(针对需要设置的)				Mozilla/5.0 (Windows NT 5.1; rv:7.0.1) Gecko/20100101 Firefox/7.0.1
	accept_language				接收的请求头							Accept_Language	zh-CN,zh;q=0.8,zh-TW;q=0.6,en;q=0.4
	methodValues				POST请求传递的参数						空
	domain						请求的URL抽取出来的DOMAIN(内部使用，外部无须传递)	空
	channelID					针对Netty使用的通道ID(内部使用)	空
	soldierType					爬虫请求的类型，目前只分普通爬虫和微博爬虫(1：普通爬虫；2：新浪微博搜索页；3：新浪微博个人信息页)	1
	connCaptainTimes			请求captain的连接次数					2
	captainIP					captain的IP(如果有，会优先访问)			空
	captainPORT					captain的端口(如果有，会优先访问)		空
	isLocal						是否本地抓取(程序内部使用)				false
	isGzip						是否对请求的源码进行压缩(内部)			false
	requestMap					额外需要的请求参数
								（这个和下面那个是为了适应
								以后更多的网站类型和更多的参数，
								所有额外需要添加的参数都放置在这里面，
								这样避免修改接口）						空
	responseMap					返回额外的结果参数						空


说明2：SpiderResult
	属性	说明	默认值
	html						网页源码								空
	responseCode				返回的状态码							0
	realUrl						真正访问的URL地址						空
	charSet						网页的编码								空
	cookies						网站返回的Cookie						空
	channelID					每个Channel的唯一标识符					空
	ERRORMsg					保存所有的错误信息						空
	isShield					是否被屏蔽								false


