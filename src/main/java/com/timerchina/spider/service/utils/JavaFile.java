package com.timerchina.spider.service.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 生成文件，管理文件
 * @author jason.lin@timerchina.com 
 * @date 2015-5-26 上午09:52:52
 */
public class JavaFile {

//	private static String os = System.getProperties().getProperty("os.name");
//	private static String filename_split = "";
//	
//	static{
//		if(os.contains("Windows")){
//			filename_split = "\\";
//		}else{
//			filename_split = "/";
//		}
//	}
	
	/**
	 * 组装Aut请求的文件
	 * @param weiboList
	 * @return
	 */
	public static String packageAutFile(String html, String name){
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		
		SimpleDateFormat daysdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String fileName = "log" + File.separator + "AutFile" + File.separator + daysdf.format(date) + File.separator + sdf.format(date) + "_" + name + ".txt";
		String path = "log" + File.separator + "AutFile" + File.separator + daysdf.format(date);
		File filePath = new File(path);
		if(!filePath.exists()){
			filePath.mkdirs();
		}
		
		
		File file = new File(fileName);
		file.setWritable(true,false);

		if(file.isFile()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
		   OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"utf-8");
		   BufferedWriter writer = new BufferedWriter(write);  
		   writer.write(html);
		   writer.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}
	
	/**
	 * 检测一个文件是否存在
	 * @param path
	 * @return
	 */
	public static boolean isFileExist(String path){
		File file = null;
		try {
			file = new File(path);
			if(file.isFile()){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	public static void main(String[] args) throws InterruptedException {
		
		String text = "This is a test file. Do not open it.";
		
		for (int i = 0; i < 10; i++) {
			System.out.println(packageAutFile(text, "test" + i));
			System.out.println("The separator of linux file system:"+File.separator);
			Thread.sleep(20000);
			
		}
		
	}
}
