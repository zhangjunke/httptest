package com.junker.autotest.core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * http响应信息过长时，链接到新页面展示
 * @author zhangjunke
 *
 */
public class ResponsePage {

	public static String wholeResponse(String name,String response,String rootpath) throws FileNotFoundException{
		StringBuilder sb = new StringBuilder();   
		long time=System.currentTimeMillis();
		String filePath=rootpath+"Report/wholeResponse/"+time+".html";
		PrintStream printStream = new PrintStream(new FileOutputStream(filePath));
/*		sb.append("<html>");
        sb.append("<head>");  
        sb.append("<title>"+name+"</title>");  
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");  
        sb.append("<style type=\"text/css\">");  
        sb.append("TABLE{border-collapse:collapse;border-left:solid 1 #000000; border-top:solid 1 #000000;padding:5px;}");  
        sb.append("TH{border-right:solid 1 #000000;border-bottom:solid 1 #000000;}");  
        sb.append("TD{font:normal;border-right:solid 1 #000000;border-bottom:solid 1 #000000;}"); 	*/

		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
				"<head>\n" +
				"    <title>测试开放平台</title>\n" +
				"    <!--<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />-->\n" +
				"    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
				"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
				"    <meta charset=\"utf-8\">\n" +
				"    <link href=\"style/adminStyle.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
				"    <link rel=\"stylesheet\" href=\"css/style.css\" media=\"screen\" type=\"text/css\" />\n" +
				"    <link rel=\"shortcut icon\" href=\"../favicon.ico\">\n" +
				"    <link rel=\"stylesheet\" type=\"text/css\" href=\"style/default.css\" />\n" +
				"    <link rel=\"stylesheet\" type=\"text/css\" href=\"style/component.css\" />\n" +
				"    <script src=\"js/modernizr.custom.js\"></script>\n" +
				"    <script src=\"js/jquery.js\"></script>\n" +
				"</head>");
		sb.append("<body>\n" +
				"<link href='http://fonts.googleapis.com/css?family=Open+Sans:300,400' rel='stylesheet' type='text/css'>\n" +
				"<link href=\"http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css\" rel=\"stylesheet\">\n" +
				"<div class=\"container\">\n" +
				"    <ul class=\"cbp-vimenu\">\n" +
				"        <li><a href=\"#\" class=\"icon-logo\">HOME</a></li>\n" +
				"        <li><a href=\"http://10.152.4.26:8080/httpmock/html/indexPage.html#\" target=\"_self\" class=\"icon-archive\" >HTTPMOCK</a></li>\n" +
				"        <li><a href=\"http://10.152.4.26:8080/dubbomock/html/indexPage.html#\" target=\"_self\" class=\"icon-search\">DUBBOMOCK</a></li>\n" +
				"        <li><a href=\"http://10.152.4.26:8080/httptest/\" target=\"_self\" class=\"icon-pencil\">HTTPTEST</a></li>\n" +
				"    </ul>\n" +
				"</div>\n" +
				"<style>\n" +
				"    body{ text-align:start}\n" +
				"    .div{ margin:0 auto; width:1250px; height:85px; border:3px solid #FFFFFF}\n" +
				"</style>");
        sb.append("<div class=\"div\">\n<font color=#000000><font size=\"5\"><center><strong>"+name+"<br></strong></center></font></font></head>");
		sb.append(response);
		sb.append("</div></body></html>");
		printStream.println(sb.toString()); 
		printStream.close();
		return filePath;
	}
}
