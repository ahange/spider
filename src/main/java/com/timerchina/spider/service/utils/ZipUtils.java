package com.timerchina.spider.service.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.timerchina.spider.bean.SCharSet;
import com.timerchina.spider.service.exception.UnGZIPFailException;

public class ZipUtils {
//	private static Base64 base64 = new Base64();
	private static Logger logger = Logger.getLogger(ZipUtils.class);
	
	/**
	 * 使用gzip进行压缩
	 */
	public static String gZip(String primStr) {
		if (primStr == null || primStr.length() == 0) {
			return primStr;
		}
		Base64 base64 = new Base64();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(primStr.getBytes());
		} catch (IOException e) {
			logger.error("压缩字符串出错");
		} finally {
			try {
				if(out!=null)
					out.close();
				if (gzip != null)
					gzip.close();
			} catch (IOException e) {
				logger.error("关闭输入输出流失败");
			}
		}
		return base64.encodeToString(out.toByteArray());
	}

	/**
	 * <p>
	 * Description:使用gzip进行解压缩
	 * </p>
	 * 
	 * @param compressedStr
	 * @return
	 * @throws UnGZIPFailException 
	 */
	public static String unGzip(String compressedStr) throws UnGZIPFailException {
		if (compressedStr == null) {
			return null;
		}
		Base64 base64 = new Base64();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = null;
		GZIPInputStream ginzip = null;
		byte[] compressed = null;
		String decompressed = null;
		try {
			compressed = base64.decode(compressedStr);
			in = new ByteArrayInputStream(compressed);
			ginzip = new GZIPInputStream(in);
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = ginzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString(SCharSet.UTF_8);
		} catch (IOException e) {
			throw new UnGZIPFailException();
		} finally {
			try {
				if (ginzip != null) 
					ginzip.close();
				if (in != null) 
					in.close();
				if (out != null) 
					out.close();
			} catch (IOException e) {
				logger.error("输入输出流关闭失败");
			}
		}

		return decompressed;
	}

	/**
	 * 以输入流的方法进行GZip解压缩
	 * @param inputStream
	 * @return
	 */
	public static String unGzip(InputStream inputStream){
		if (inputStream == null) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPInputStream ginzip = null;
		String newHTML = null;
		try {
			ginzip = new GZIPInputStream(inputStream);
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = ginzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			newHTML = out.toString(SCharSet.UTF_8);
		} catch (IOException e) {
			logger.error("解压缩Inputstream出错");
		} finally {
			try {
				if (ginzip != null)
					ginzip.close();
				if (out != null) 
					out.close();
			} catch (IOException e) {
				logger.error("输入输出流关闭失败");
			}
		}
		return newHTML;
	}
	
	/**
	 * 用指定的编码解压缩
	 * @param inputStream
	 * @param charset
	 * @return
	 * @throws IOException 
	 */
	public static String unGzip(InputStream inputStream,String charset) throws IOException{
		StringBuffer resultBuffer = new StringBuffer();
        GZIPInputStream gzinputStream = new GZIPInputStream(inputStream);
        BufferedReader bin = new BufferedReader(new InputStreamReader(gzinputStream, charset));
        String tmpStr;
        try{
        	while ((tmpStr = bin.readLine()) != null) {
            	resultBuffer.append(tmpStr);
            }
        }finally{
        	if(bin!=null)
        		bin.close();
        	if(gzinputStream!=null)
        		gzinputStream.close();
        }
		return resultBuffer.toString();
	}
	
	/**
	 * 使用zip进行压缩
	 * @param str
	 *            压缩前的文本
	 * @return 返回压缩后的文本
	 */
	public static final String zip(String str) {
		if (str == null)
			return null;
		byte[] compressed;
		ByteArrayOutputStream out = null;
		ZipOutputStream zout = null;
		String compressedStr = null;
		Base64 base64 = new Base64();
		try {
			out = new ByteArrayOutputStream();
			zout = new ZipOutputStream(out);
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(str.getBytes());
			zout.closeEntry();
			compressed = out.toByteArray();
			compressedStr = base64.encodeToString(compressed);
		} catch (IOException e) {
			compressed = null;
			logger.error("压缩字符串失败");
		} finally {
			try {
				if (zout != null)
					zout.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				logger.error("输入输出流关失败");
			}
		}
		return compressedStr;
	}

	/**
	 * 使用zip进行解压缩
	 * @param compressed
	 *            压缩后的文本
	 * @return 解压后的字符串
	 */
	public static final String unZip(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}
		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed = null;
		Base64 base64 = new Base64();
		try {
			byte[] compressed = base64.decode(compressedStr);
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new ZipInputStream(in);
			zin.getNextEntry();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString(SCharSet.UTF_8);
		} catch (IOException e) {
			decompressed = null;
			logger.error("zip压缩字符串失败");
		} finally {
			try {
				if (zin != null) 
					zin.close();
				if (in != null) 
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				logger.error("关闭输入输出流失败");
			}
		}
		return decompressed;
	}
}