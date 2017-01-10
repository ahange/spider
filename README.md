# Spider爬虫软件说明

@(spider)[爬虫客户端|Httpclient|Netty|编码识别]

**Spider**是基于java开发的分布式爬虫，爬虫与管理机之间采用Master+slave的模式，使用httpclient作为模拟浏览器的技术，通信框架采用netty，具备以下功能：
 
- **屏蔽检测** ：根据爬虫爬取返回结果，检测文本的长度、是否包含自定义的某些屏蔽词（比如404，500，请输入验证码等），来检测当前请求是否被源网站屏蔽；
- **编码识别** ：目前使用的编码识别的算法网友提供的一个算法，然后经过算法优化，目前针对中文编码问题，准确率已经到达99%以上；
- **海外网站抓取功能** ：支需要提供海外服务器ip，支持数据传输的加密解密功能。
- **代理抓取功能**：需要提供代理ip

-------------------

[TOC]

## Spider架构说明

> 网络爬虫（又被称为网页蜘蛛，网络机器人，在FOAF社区中间，更经常的称为网页追逐者），是一种按照一定的规则，自动地抓取万维网信息的程序或者脚本。    —— [百度百科](http://baike.baidu.com/link?url=FDkL-fxwyZAYh5Wev565CSr1nseEyX1YQqLhUZKpu9pxsdkfHf4vgLDbjsDGggvn)
 

### 简单使用说明
``` java
#所有请求参数都要封装到SpiderParams[具体字段说明，请看最后的说明1]里面
SpiderParams sp = new SpiderParams();

#需要抓取的url
sp.setUrl("http://www.baidu.com");

#通过SpiderFactory执行抓取程序
SpiderResult rs = (SpiderResult)SpiderFactory.execute(sp);

#所有的抓取结果保存在SpiderResult[具体字段说明，请看最后的说明2]中，输出的结果编码已经统一成utf-8
System.out.println(rs.getHtml());
```

### 架构图
![Alt text](./微信截图_20170110112925.png)


### 流程图
![Alt text](./微信截图_20170110113130.png)

### 接口类说明SpiderFactory

 **1.对外接口** 
 
- 默认对外接口execute（SpiderParams spiderParams）;
- 本地抓取接口executeLocal（SpiderParams spiderParams）;
- 爬虫司令部接口executeCaptain（SpiderParams spiderParams）;
- 海外抓取接口executeOversea（SpiderParams spiderParams）;

**2.输入SpiderParams(具体的详细信息见说明1)** 

**3.输出SpiderResult(具体的详细信息见说明2)** 

**4.对外接口** 

**5.该接口做三件事** 
- 对请求的SpiderParams进行参数验证
- 组装内部请求的参数(比如domain，soldierType，isLocal)
- 调用相应的内部抓取程序接口进行抓取

#### 接口类流程图
![Alt text](./微信截图_20170110114100.png)



### 说明1：Spiderparam
| 属性       |说明      | 默认值|
| :-------- | :--------| :-- |
|url	|抓取源码的地址(必须的)	|空|
|method	|请求方式(POST|GET)	|get|
|cookie	|网站的cookie(必须的)	|空|
|charset	|网页的编码	|空|
|referer	|定位网页来向连接	|空|
|httpConnectionTimeout	|http链接超时时间	|2000ms|
|isOverseaURL	|是否境外(目前不开放接口)	|false|
|httpReadTimeout	|http读取超时	|5000ms|
|isProxy	|是否需要使用代理	|false|
|isAjax	|是否Ajax(RTM)	|false|
|user_agent	|浏览器信息(针对需要设置的)	|Mozilla/5.0 (Windows NT 5.1; rv:7.0.1) Gecko/20100101 Firefox/7.0.1|
|accept_language	|接收的请求头Accept_Language	|zh-CN,zh;q=0.8,zh-TW;q=0.6,en;q=0.4|
|methodValues	|POST请求传递的参数	|空|
|domain	|请求的URL抽取出来的DOMAIN(内部使用，外部无须传递)	|空|
|channelID	|针对Netty使用的通道ID(内部使用)	|空|
|soldierType	|爬虫请求的类型，目前只分普通爬虫和微博爬虫(1：普通爬虫；2：新浪微博搜索页；3：新浪微博个人信息页)	|1|
|connCaptainTimes	|请求captain的连接次数	|2|
|captainIP	|captain的IP(如果有，会优先访问)	|空|
|captainPORT	|captain的端口(如果有，会优先访问)	|空|
|isLocal	|是否本地抓取(程序内部使用)	|false|
|isGzip	|是否对请求的源码进行压缩(内部)	|false|
|requestMap	|额外需要的请求参数（这个和下面那个是为了适应以后更多的网站类型和更多的参数，所有额外需要添加的参数都放置在这里面，这样避免修改接口）	|空|
|responseMap	|返回额外的结果参数	|空|


### 说明2：SpiderResult
| 属性      |    说明 | 默认值  |
| :-------- |:--------| :-- |
|html	|网页源码	|空|
|responseCode	|返回的状态码	|0|
|realUrl	|真正访问的URL地址	|空|
|charSet	|网页的编码	|空|
|cookies	|网站返回的Cookie	|空|
|channelID	|每个Channel的唯一标识符	|空|
|ERRORMsg	|保存所有的错误信息	|空|
|isShield	|是否被屏蔽	|false|

### 说明3：状态码
**说明**：状态码对应的常量保存在SpiderResult结果集中
| 命名      |    说状态码 | 说明  |
| :-------- |:--------| :-- |
|ISPROXY_ISOVERSEA_EQUALS_TRUE_ERROR	|600	|本地抓取的时候，设置了两个参数为true|
|ISPROXY_EQUALS_TRUE_ERROR	|601	|海外抓取的时候，设置了isProxy为true|
|ISOVSESEA_EQUALS_TRUE_ERROR	|602	|解压出错(请求Captain时候，返回的源码是压缩的，需要解压)|
|URL_ILLEGAL	|603	|URL验证不合法，检查URL|
|UNKNOW_HOST_ERROR	|604	|未知的主机名，检查URL|
|SOURCECODE_IS_NULL	|605	|获取的源码为空|
|UNKNOW_ERROR	|606	|发生未知错误|
|IP_OR_COOKIE_SHIELD	|800	|被屏蔽(IP屏蔽或者Cookie屏蔽)|
|HTTP_CONN_TIMEOUT	|801	|Http请求连接超时(可能网站问题)|
|HTTP_READ_TIMEOUT	|802	|Http读取超时，网络或者网站问题|
|ENCODING_DETECT_ERROE	|803	|编码识别失败|
|ENCRIPTION_ERROR	|804	|海外抓取相关(解密出错)|
|UNGZIP_FAIL	|805	|解压出错(请求Captain时候，返回的源码是压缩的，需要解压)|

