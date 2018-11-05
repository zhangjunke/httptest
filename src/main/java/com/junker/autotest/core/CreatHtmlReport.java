package com.junker.autotest.core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.junker.autotest.util.ExcelAnalyze;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * 生成html报告
 * @author zhangjunke
 *
 */
public class CreatHtmlReport {
	public static String reportContent="";
	public static String wholeResponsePath="";
	public static String reportPath="";
	public static StringBuilder sb = new StringBuilder();  
	public static void writeResult(String testPlat, String resultpath){
		ArrayList<Object> testMapGroup=ResultAssert.resultMapGroup;
		System.out.println("testMapGroupsize:"+testMapGroup.size()+"  value:"+testMapGroup);
		List<Integer> rownum=new ArrayList<Integer>();
		List<Integer> coloumnum=new ArrayList<Integer>();
		List<String> value=new ArrayList<String>();
				
        for(int i=0;i<testMapGroup.size();i++){
			ArrayList<String> testGroups=(ArrayList<String>) testMapGroup.get(i);
        	String result=testGroups.get(7);
        	String response=testGroups.get(8);
        	String timeConsuming=testGroups.get(9);      	
			for(int j=7;j<10;j++){
				rownum.add(i+1);
	            coloumnum.add(j);
            	switch(j){
            	case 7:
            		value.add(result);break;
            	case 8:
            		value.add(response);break;
            	case 9:
            		value.add(timeConsuming);break;
            	}	            	
	            }
        }
        ExcelAnalyze ea=new ExcelAnalyze();
        try {
			ea.writeTestResult(resultpath, rownum, coloumnum, value);
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void creatReport(String testPlat, String testEnvironmentUrl,String rootpath) throws FileNotFoundException{			
	           
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");  
        Date date = new Date();
        @SuppressWarnings("resource")
        String reportPath1=rootpath+"Report/testReport_"+testPlat+"_"+bartDateFormat.format(date)+".html";
        reportPath=reportPath1;
        PrintStream printStream = new PrintStream(new FileOutputStream(reportPath));  
        
        sb.append("<html>");  
        sb.append("<head>");  
        sb.append("<title>"+testPlat+"_"+"接口自动测试报告</title>");  
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");  
        sb.append("<style type=\"text/css\">");  
        sb.append("TABLE{border-collapse:collapse;border-left:solid 1 #000000; border-top:solid 1 #000000;padding:5px;}");  
        sb.append("TH{border-right:solid 1 #000000;border-bottom:solid 1 #000000;}");  
        sb.append("TD{font:normal;border-right:solid 1 #000000;border-bottom:solid 1 #000000;}"); 	        
        sb.append("</style><font color=#000000><font size=\"8\"><center><strong>"+testPlat+"_"+"接口自动测试报告<br></strong></center></font></font></head>");  
        sb.append("</div></body></html>"); 

        sb.append("被测环境地址："+testEnvironmentUrl);
        sb.append("<br>");
        sb.append("不通过数/总数："+ResultAssert.reportResult+"/"+ResultAssert.resultMapGroup.size());
        sb.append("<br>");
        
        sb.append("<table  border=1 cellspacing=0  width=100% bordercolorlight=#333333 bordercolordark=#efefef>");
        sb.append(" <tr bgcolor=#84C1FF>");
        sb.append("<td><strong>接口名</strong></td>");
        sb.append("<td><strong>接口相对路径</strong></td>");
        sb.append("<td><strong>接口类型</strong></td>");
        sb.append("<td><strong>测试点</strong></td>");
        sb.append("<td><strong>测试参数</strong></td>");
        sb.append("<td><strong>期望返回码</strong></td>");
        sb.append("<td><strong>期望断言</strong></td>");
        sb.append("<td><strong>测试结果</strong></td>");
        sb.append("<td><strong>实测响应信息</strong></td>");
        sb.append("<td><strong>测试耗时(ms)</strong></td>");
        sb.append("</tr>"); 
        ArrayList<Object> testMapGroup=ResultAssert.resultMapGroup;
        for(int i=0;i<testMapGroup.size();i++){
            @SuppressWarnings("unchecked")
			ArrayList<String> testGroups=(ArrayList<String>) testMapGroup.get(i);
    		sb.append(" <tr bgcolor=#FFFFFF>");
    		
            for(int j=0;j<testGroups.size();j++){
            	String value=testGroups.get(j); 
            	if(j==6||j==8){
            		value = value.replace("&ldquo;", "“");
            		value = value.replace("&rdquo;", "”");
            		value = value.replace("&nbsp;", " ");
            		value = value.replace("&", "&amp;");
            		value = value.replace("&#39;", "'");
            		value = value.replace("&rsquo;", "’");
            		value = value.replace("&mdash;", "—");
            		value = value.replace("&ndash;", "–");
            	}
            	if(j<8){
            		if(value.length()>50){
                		sb.append("<td>"+"<div style=\"width:300px;word-wrap:break-word;\" >"+value+"</td>");
                	}
            		else if(value.equals("未通过")){
            			sb.append("<td bgColor=red> <div style=\"width:40px;word-wrap:break-word;\" >"+value+"</td>");//未通过时字体加红
            		}
            		else{
            		sb.append("<td>"+value+"</td>"); 
            		}
            	}
            	if(j==8){
            		if(value.length()>200){
            			wholeResponsePath=ResponsePage.wholeResponse(testGroups.get(0)+"|"+testGroups.get(2),value,rootpath);
                		value=value.substring(0,50);  
                		sb.append("<td>"+"<div style=\"width:300px;word-wrap:break-word;\" >"+value+"<a href="+"\""+wholeResponsePath+"\""+"target=\"_self\">"+"<br>查看更多..."+"</a></td>");
                	}//超长返回值，只显示部分内容。点击可查看response完整内容。
            		else{
            			sb.append("<td>"+"<div style=\"width:300px;word-wrap:break-word;\" >"+value+"</td>");                  	                   	
            		}
            		} 
            	if(j>8){
            		sb.append("<td>"+value+"</td>"); 
            	}
            }
            sb.append("</tr>"); 
        }
        sb.append("<table border=\"1\"><tr>");
    	reportContent=sb.toString();
        printStream.println(reportContent);
        printStream.close();
	}
}
