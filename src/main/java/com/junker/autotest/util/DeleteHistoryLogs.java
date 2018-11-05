package com.junker.autotest.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author zhangjunke
 * dateString:20170227
 * 删除修改日期早于指定日期的文件：Report\*.html、Report\wholeResponse\*.html、Logs\*.*
 */
public class DeleteHistoryLogs {
	public static void deleteFile(String dateString,String rootpath) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		long specialDate = df.parse(dateString).getTime();
		
		//清理Report\*.html
		File reportUrl = new File(rootpath+"Report/");
		File[] reportFile=reportUrl.listFiles();
		if(null!=reportFile){
			for(File file1:reportFile){
				if(file1.isFile()){
					long fileTime=file1.lastModified();
					String ctime = new SimpleDateFormat("yyyyMMdd").format(new Date(fileTime));
					long cDate = df.parse(ctime).getTime();
					if(cDate-specialDate<0){
						file1.delete();
					}
				}
			}
		}

		
		//清理Report\wholeResponse\*.html
		File wholeResponseUrl = new File(rootpath+"Report/wholeResponse/");
		File[] wholeResponseFile=wholeResponseUrl.listFiles();
		if(null!=wholeResponseFile){
			for(File file2:wholeResponseFile){
				if(file2.isFile()){
					long fileTime=file2.lastModified();
					String ctime = new SimpleDateFormat("yyyyMMdd").format(new Date(fileTime));
					long cDate = df.parse(ctime).getTime();
					if(cDate-specialDate<0){
						file2.delete();
					}
				}
			}
		}

		
		//清理Logs\*.*
		File LogsUrl = new File(rootpath+"Logs/");
		File[] logsFile=LogsUrl.listFiles();
		if(null!=logsFile){
			for(File file3:logsFile){
				if(file3.isFile()){
					long fileTime=file3.lastModified();
					String ctime = new SimpleDateFormat("yyyyMMdd").format(new Date(fileTime));
					long cDate = df.parse(ctime).getTime();
					if(cDate-specialDate<0){
						file3.delete();
					}
				}
			}
		}
	}
}
