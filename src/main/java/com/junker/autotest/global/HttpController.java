package com.junker.autotest.global;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.junker.autotest.servlet.Log4jInitServlet;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;


/**
 * 封装get和post请求，当接口包含login时提取cookies并传递到下一接口中
 * @author zhangjunke
 *
 */
public class HttpController {
	private static Logger logger =Logger.getLogger(Log4jInitServlet.class);
	private static String cookie="init";
	public static HashMap<String,Object> httpget(String theurl,String theheader,String theparameter) throws IOException {
		URL getUrl = new URL(theurl);
		if(theparameter.length()!=0&&!theparameter.equals("")){
			getUrl = new URL(theurl+"?"+theparameter);
		}
		HttpURLConnection ct = (HttpURLConnection) getUrl.openConnection();
		if(theheader.length()!=0&&!theheader.equals("")){
			String[] headerList=theheader.split("&");
			for(int i=0;i<headerList.length;i++){
				String everyHeader=headerList[0];
				String[] headerBody=everyHeader.split(":");
				String headerName=headerBody[0];
				String headerValue=headerBody[1];
				ct.setRequestProperty(headerName,headerValue);
			}
		}
		if(!cookie.equals("init")){
			ct.setRequestProperty("Cookie","sessionId="+cookie);
			ct.setRequestProperty("Cookie","ccat="+cookie);
		}
		long start = System.currentTimeMillis();
		ct.connect();		
		BufferedReader reader = new BufferedReader(new InputStreamReader(ct.getInputStream(), StandardCharsets.UTF_8));
		HashMap<String,Object> result=new HashMap<String,Object>();		
		result.put("statusCode", Integer.toString(ct.getResponseCode()));		
		String lines;
		String linev="";
		try{
        while ((lines = reader.readLine()) != null){
        	linev +=lines;
        }
		}catch(EOFException e){			
		}
		//提取普通登录cookie
		if(theurl.toLowerCase().contains("/Login".toLowerCase())){
			String key="";
			for(int i = 1; (key = ct.getHeaderFieldKey(i)) != null; i++ ){
				 if (key.equalsIgnoreCase("set-cookie")) {
						String cookieS=ct.getHeaderField(i);						
						if(cookieS.contains("sessionId")){
							cookie=cookieS.split(";")[0].replace("sessionId=", "");
						}
				 }
			}
		}
		//提取理财登录cookie
		String[] linevSt=linev.split(",");
		for(int i=0;i<linevSt.length;i++){
			if(linevSt[i].contains("access_token")){
				String[] tmp=linevSt[i].split(":");
				cookie=tmp[1];
			}				
		}
		long end = System.currentTimeMillis();
		long timeConsuming=end-start;
		logger.debug("the reponse:\n【"+linev+"】");
		result.put("response", linev);
		result.put("timeConsuming", Long.toString(timeConsuming));
        reader.close();
        ct.disconnect();
        return result;
	}	
	public static HashMap<String,Object> httpost(String theurl,String theheader,String theparameter) throws IOException{
		HashMap<String,Object> result=new HashMap<String,Object>();
		URL url=new URL(theurl);
		HttpURLConnection ct=(HttpURLConnection)url.openConnection();
		ct.setDoOutput(true);
		ct.setDoInput(true);
		ct.setRequestMethod("POST");
		ct.setUseCaches(false);
		ct.setInstanceFollowRedirects(true);
		ct.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		if(theheader.length()!=0&&!theheader.equals("")){
			String[] headerList=theheader.split("&");
			for(int i=0;i<headerList.length;i++){
				String everyHeader=headerList[i];
				String[] headerBody=everyHeader.split(":");
				String headerName=headerBody[0];
				String headerValue=headerBody[1];
				ct.setRequestProperty(headerName,headerValue);
			}
		}
		if(!cookie.equals("init")){
			ct.setRequestProperty("Cookie","sessionId="+cookie);
			ct.setRequestProperty("Cookie","ccat="+cookie);
		}
		long start = System.currentTimeMillis();
		DataOutputStream out=new DataOutputStream(ct.getOutputStream());
		if(theparameter.length()!=0&&!theparameter.equals("")){
			out.writeBytes(theparameter);
		}		
		out.flush();
		out.close();
		BufferedReader rd=new BufferedReader(new InputStreamReader(ct.getInputStream(), StandardCharsets.UTF_8));
		long end = System.currentTimeMillis();
		long timeConsuming=end-start;
		
		String line;
		String linev="";
		try{
			while((line=rd.readLine())!=null){
				linev +=line;	
			}
			}catch(EOFException e){
				
			}
		//提取普通登录cookie
		if(theurl.toLowerCase().contains("/Login".toLowerCase())){
			String key="";
			for(int i = 1; (key = ct.getHeaderFieldKey(i)) != null; i++ ){
				 if (key.equalsIgnoreCase("set-cookie")) {
						String cookieS=ct.getHeaderField(i);						
						if(cookieS.contains("sessionId")){
							cookie=cookieS.split(";")[0].replace("sessionId=", "");
						}
				 }
			}
		}
		//提取理财登录cookie
		String[] linevSt=linev.split(",");
		for(int i=0;i<linevSt.length;i++){
			if(linevSt[i].contains("access_token")){
				String[] tmp=linevSt[i].split(":");
				cookie=tmp[1];
			}				
		}
		logger.debug("the reponse:\n【"+linev+"】");
		result.put("statusCode", Integer.toString(ct.getResponseCode()));
		result.put("response", linev);	
		result.put("timeConsuming", Long.toString(timeConsuming));
		rd.close();
		ct.disconnect();
		return result;
		}		
}