package com.junker.autotest.global;

import java.io.IOException;
import java.util.HashMap;

import com.junker.autotest.global.HttpController;
import org.apache.commons.configuration.ConfigurationException;


/**
 * 返回post和get相应信息
 * @author zhangjunke
 *
 */
public class HttpRequest {
	public static HashMap<String,Object> httpRequestPost(String theurl,String theheader,String theparameter) throws IOException{
		HashMap<String,Object> httpReponse=new HashMap<String,Object>();
		httpReponse= HttpController.httpost(theurl,theheader,theparameter);
		return httpReponse;
	}
	
	public static HashMap<String,Object> httpRequestGet(String theurl,String theheader,String theparameter) throws IOException {
		HashMap<String,Object> httpReponse=new HashMap<String,Object>();
		httpReponse=HttpController.httpget(theurl,theheader,theparameter);		
		return httpReponse;
	}
}
