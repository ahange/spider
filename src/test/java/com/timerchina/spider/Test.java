package com.timerchina.spider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import com.timerchina.spider.bean.SpiderParams;
import com.timerchina.spider.bean.SpiderResult;
import com.timerchina.spider.db.DBUtil;
import com.timerchina.spider.factory.SpiderFactory;


/**
 * @author jason.lin@timerchina.com 2014-12-2 下午2:16:35
 *
 */
public class Test{

	public static void writeFile(String in,String outName){
		File file=new File(outName);//("log/"+name+".txt");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (file.isFile() && file.exists()){
		    FileOutputStream fos = null;
		    try {
                fos = new FileOutputStream(file);
                fos.write(in.getBytes("ISO-8859-1"));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally{
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
		}
		else {
			System.out.println("找不到所写文件！");
		}
	}
	
	
	public static void main(String[] args) {
		
		byte[] b = "联通".getBytes(Charset.forName("gbk"));
		
		StringBuffer sb = new StringBuffer();
		for (byte c : b) {
			sb.append(b);
		}
		
		System.out.println(sb);
		
	}
}
