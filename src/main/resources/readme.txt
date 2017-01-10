版本：spider 1.0.0
更新时间：2014-12-25 16:04:38
作者：jason.lin@timerchina.com
版本说明：该版本是适配HeadQuaters的爬虫，支持以下功能：
		1.提供四个接口，分别是默认接口，本地抓取接口，海外抓取接口和访问Captain的接口
		2.提供对Https自签名的网站访问
		3.提供对需要Http Basic验证的网站的访问
		4.添加Netty访问模块，由该模块作为基础socket请求
		5.提供自定义的域名添加和对应的域名的soldierType添加，具体添加在spider.properties
		6.提供新浪官方api错误信息解析和错误代码返回
		7.SpiderParams提供username和password两个属性，主要用来对需要Http验证的网站使用
		8.提供http clean功能，即可清除源码中除了正文以外的所有http标签
		

		
更新时间：2015-01-05 11:44:32
作者：jason.lin@timerchina.com		
更新说明：
		1.该版本更新了配置文件的读取方式，直接读取jar包内部的配置文件，外部不再需要配置文件
		2.添加搜狗微信的cooki自动解析功能
		3.提供获取头信息的接口属性，属性名为headers
	

更新时间：2015-01-22 17:08:32
作者：jason.lin@timerchina.com
更新说明：
		1.本次修改了微博抓取重定向问题，只能重定向一次。
		2.修改编码识别的bug，由原来的通过前40000个字节来识别编码，改成用全文识别的方式，由此解决了汽车之间抓取乱码问题。
		3.修改了获取用户url中的protocol和host的方式，原来的是用正则获取，现在使用java内部的URL类。
		
		
更新时间：2015-02-12 11:15:02
作者：jason.lin@timerchina.com
更新说明：
		1.本次修改了输出log的方式，默认不输出log，给定setLogger参数，用来设置是否输出日志
		

更新时间：2015-03-03 16:36:10
作者：jason.lin@timerchina.com
更新说明：
		1.本次更新修改了编码解析模块的乱码问题，一旦网页编码识别成iso8859，则系统会直接将网页编码成GBK
		2.修改编码识别默认的编码为GBK，原来的默认编码是iso8859-1。
		

版本：1.1.4-snapshot
更新时间：2015-03-09 10:50:23
作者：jason.lin@timerchina.com
更新说明：
		1.修改了一个bug：具体是，原本通过URL类获取请求的url的域名和协议，但是针对weibo.com:weiboPort是
		  会出现异常，所以增加了一个判断，当url包含weibo.com:weiboPort时，将域名设定为weibo.com:weiboPort，
		  而不通过URL进行解析。
		  
		  
版本：1.1.4-snapshot
更新时间：2015-03-13 17:13:39
作者：jason.lin@timerchina.com
更新说明：
		1.本次修改添加了对url的编码操作，解决百度指数抓取的url中出现%的问题。程序会对输入的url
		强制用utf-8编码。
		
		
版本：1.1.4-snapshot
更新时间：2015-03-20 10:47:43
作者：jason.lin@timerchina.com
更新说明：
		1.本次修改主要针对url中存在的特殊字符，包括中文字符或者url要求的需要转义的特殊字符。
		2.添加了对错误url的异常的捕获，一旦检测到系统无法处理的url，则会报相应的异常状态码。
		
		
版本：1.1.5-snapshot
更新时间：2015-03-25 10:30:56
作者：jason.lin@timerchina.com
更新说明：
		1.更新系统存在的一个bug，即没有对用户自定义的charset进行编码检测，会导致程序异常，获取源码为空。
		2.修复了对url进行编码，导致url无法抓取的情况。
		
		
版本：1.1.4-snapshot
更新时间：2015-04-03 11:10:00
作者：jason.lin@timerchina.com
更新说明：
		1.修改了之前出现的配置文件读取不到的问题。
		2.修改了访问新浪微博，无法获取的错误信息的bug。
		
		
版本：1.1.6-snapshot
更新时间：2015-04-17 15:39:00
作者：jason.lin@timerchina.com
更新说明：
		1.修改微博API接口可能会有返回809的问题， 在不是API返回的错误状态的时候，可能会报809系统错误。
		

版本：1.1.6-snapshot
更新时间：2015-04-23 13:59:00
作者：jason.lin@timerchina.com
更新说明：
		1.修改因为针对搜狗微信进行特殊处理，而导致源码没有压缩就直接返回，客户端因此解压失败，报805错误的bug。
		
		
版本：1.1.6-snapshot
更新时间：2015-05-05
作者：jason.lin@timerchina.com
更新说明：
		1.修改百度指数抓取合成的URL中存在特殊字符，导致请求保存，具体的错误可用URI类进行测试。
		
		
版本：1.1.6-snapshot
更新时间：2015-05-11
作者：jason.lin@timerchina.com
更新说明：
		1.修改抓取搜狗微信时，爬虫司令部返回源码中的ASCII无法转换成本地编码。
		
		
版本：1.1.6-snapshot
更新时间：2015-05-14
作者：jason.lin@timerchina.com
更新说明：
		1.修改搜狗微信抓取的bug，由于搜狗需要输入验证码或者返回的源码长度小于100同时又大于10的时候，可能结果是正常的，但程序以为是需要cookie，则会报错。
		
		
版本：1.1.6-snapshot
更新时间：2015-05-14
作者：jason.lin@timerchina.com
更新说明：
		1.针对新浪微博发现页面，因为没有在spider.properties配置soldierType，所以导致无法抓取，现在在配置文件中配置d.weibo.com的soldierType为3，使用cookie
		  level为2的微博账号。	
		  
		  
版本：1.1.6-snapshot
更新时间：2015-05-15
更新说明：
		1.搜狗微信，请求后，发现返回的源码没有解压缩，目前这个已经解决了。	
		
		
版本：1.1.7-snapshot
更新时间：2015-06-17
更新说明：
		
		
版本：1.1.8-snapshot
更新时间：2015-06-26
更新说明：
		1.添加淘宝，天猫，新浪微博的屏蔽规则
		2.修改屏蔽检测的判断逻辑
		
		
版本：1.1.8-snapshot
更新时间：2015-06-30
更新说明：
		1.重新修改spider关于天猫的屏蔽规则
		
		
版本：1.1.8-snapshot
更新时间：2015-07-06
更新说明：
		1.替换系统原有的正则匹配的类，新的正则解析类由庞一文提供
		
		
版本：1.1.8-snapshot	
更新时间：2015-07-23
更新说明：
		1.由于上次百度指数url存在特殊字符，spider进行特殊字符替换之后。发现对于新浪api访问存在问题，因为{}这两个东西也会被识别成特殊字符，然后被替换。
		所以现在做了特殊处理，只对百度指数进行处理。
		
		
版本：1.1.9-snapshot	
更新时间：2015-08-24
更新说明：
		1.添加huati.weibo.com的域名，同时增加这个的soldierType为2
		

版本：1.1.9-snapshot	
更新时间：2015-09-23
更新说明：
		1.针对url中存在特殊字符的时候，原来只处理三个特殊字符，现在由于可能存在三个以上的特殊字符，
		所以修改处理特殊字符的上限个数为30个	
		
		
版本：1.1.10-snapshot	
更新时间：2015-12-23
更新说明：
		1.修改bug：在下载源码成功后，转换源码中存在的unicode码的时候，如果是下载文件的话，则会出现错误信息，目前已经删除该段代码
		
		
版本：1.1.11-SNAPSHOT
spider-zh
更新时间：2016-01-06
更新说明：
		1.增加中证特殊处理的模块，包括去除Native2ASCI等方法，方便文件下载。这个版本在zh的分支中
		
		
版本：1.1.10-SNAPSHOT
spider-zh
更新时间：2016-01-18
更新说明：
		1.修改新浪微博请求后，返回的源码包含unicode，导致抓取失败。
		2.增加对StringEntity的支持
		
		
版本：1.1.10-SNAPSHOT
spider-zh
更新时间：2016-01-20
更新说明：
		1.修改输出的log（原来会输出爬虫司令部返回的源码）。
		2.修改stringEntity为Map
		
		
版本：1.1.10-SNAPSHOT
spider
更新时间：2016-01-25
更新说明：
		1.解决post请求需要StringEntity问题，原来的设置StringEntity.setContentType(),会与HttpHeader.set(ContentType,vale);冲突
		导致返回结果错误。
		2.解决post请求中，设置了ContentType，导致所有post请求失败的bug
		
		
版本：1.1.10-SNAPSHOT
spider
更新时间：2016-01-26
更新说明：
		1.重新部署post请求失败的bug
		
		
版本：1.1.10-RELEASE
spider
更新时间：2016-02-02
更新说明：
		1.发布稳定版本
		
		
版本：1.1.11-snapshot
spider
更新时间：2016-03-16
更新说明：
		1.因为api.weibo.com这个，我们内部会替换token，但是spider检测出url存在特殊字符，会替换掉，导致url无效
		目前这个bug已经修改好