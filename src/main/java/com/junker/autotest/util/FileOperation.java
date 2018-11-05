package com.junker.autotest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileOperation {

	public static void test(){
        for(int i=0;i<2;i++){
        	System.out.println(i);
			 for(int j=0;j<8;j++){
	            	String result="result";
	            	String response="response";
	            	String timeConsuming="timeConsuming";
	            	System.out.println(j);
			}
        }
	}
	public  static void copy(String oldPath, String newPath) throws IOException { 
		int bytesum = 0; 
		int byteread = 0; 
		File oldfile = new File(oldPath); 
		if (oldfile.exists()) { //文件存在时 
			InputStream inStream = new FileInputStream(oldPath); //读入原文件 
			FileOutputStream fs = new FileOutputStream(newPath); 
			byte[] buffer = new byte[1444]; 
			while ( (byteread = inStream.read(buffer)) != -1) { 
				bytesum += byteread; //字节数 文件大小 
				fs.write(buffer, 0, byteread); 
				} 
			inStream.close(); 
			fs.close();
			} 
		} 

}
