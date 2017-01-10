package com.timerchina.spider.service.utils;

public class HtmlMarkCleanner {
	private static final String HTML_MARK_LT = "&lt;";
	private static final String HTML_MARK_GT = "&gt;";
	private static final String HTML_MARK_AMP = "&amp;";
	private static final String HTML_MARK_QUOT = "&quot;";
	private static final String HTML_MARK_REG = "&reg;";
	private static final String HTML_MARK_COPY = "&copy;";
	private static final String HTML_MARK_TRADE = "&trade;";
	private static final String HTML_MARK_ENSP = "&ensp;";
	private static final String HTML_MARK_EMSP = "&emsp;";
	private static final String HTML_MARK_NBSP = "&nbsp;";
	private static final String HTML_MARK_SEPARATOR = "------------------------------------------------------------------";

	private static String processSpicalMark(String content) {
		content = content.replace(HTML_MARK_AMP, "&");
		content = content.replace(HTML_MARK_COPY, "【M版权】");
		content = content.replace(HTML_MARK_EMSP, "__");
		content = content.replace(HTML_MARK_ENSP, "_");
		content = content.replace(HTML_MARK_GT, ">");
		content = content.replace(HTML_MARK_LT, "<");
		content = content.replace(HTML_MARK_NBSP, " ");
		content = content.replace(HTML_MARK_QUOT, "''");
		content = content.replace(HTML_MARK_REG, "【M注册】");
		content = content.replace(HTML_MARK_TRADE, "【M商标】");
		content = content.replace(HTML_MARK_SEPARATOR, "【M分隔符】");
		//处理特殊符号
//		content = content.replaceAll("[^\u4e00-\u9fa5\\d\\w\\p{P}]+", "");

		return content;
	}
	


	public static String cleanHtmlMark(String content){
		
		if(CommonTool.isEmpty(content)) return content;
		
		content = postProcessHtmlMark(content);
		
		content = processSpicalMark(content);
		
		return content.trim();
	}

	public static String cleanHtmlMarkForSpecial(String content){
		
		if(CommonTool.isEmpty(content)) return content;

		content = postProcessHtmlMark(content);
		
		content = processSpicalMark(content);
		
		content = content.replaceAll("[^\u4e00-\u9fa5\\d\\w\\p{P}]+", "");
		
		return content.trim();
	}

	private static String postProcessHtmlMark(String content) {
		content = content.replaceAll("<br[^>]*?>", "\r\n");
		content = content.replaceAll("<p>", "\r\n");
		content = content.replaceAll("</p>", "\r\n");
		content = content.replaceAll("<script.*?>.*?</script>", "");
		content = content.replaceAll("<style.*?>.*?</style>", "");
//		content = content.replaceAll("<table.*?>.*?</table>", "");
		content = content.replaceAll("=[\"'](.*?)[\"']", "");//处理标签中含有的脚本比如含>符号
		content = content.replaceAll("<[^>]*?>", "");// 处理 <*>
		content = content.replaceAll("'", "''");
		return content;
	}


}
