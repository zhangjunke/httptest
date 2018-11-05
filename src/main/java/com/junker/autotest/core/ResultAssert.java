package com.junker.autotest.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.junker.autotest.global.HttpController;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;

/**
 * 将接口执行结果信息存储在arraylist中
 * @author zhangjunke
 *
 */
public class ResultAssert {
	
		
	public static ArrayList<Object> resultMapGroup=new ArrayList<Object>();	
	public static int reportResult=0;//0代表接口测试全部通过
	public static void assertResult(String plat,String environment,String serverUrl,String header,
			String globalParameter,String featureName,String featureType,String featureUrl,String caseName,
			String APIparameter,String assertCode,String assertMessage) throws IOException {
		ArrayList<String> resultMap=new ArrayList<String>();
		resultMap.add(featureName);
		resultMap.add(featureUrl);
		resultMap.add(featureType);
		resultMap.add(caseName);
		resultMap.add(APIparameter);
		resultMap.add(assertCode);
		resultMap.add(assertMessage);
		HashMap<String,Object> result=new HashMap<String,Object>(); 

		String url=serverUrl+featureUrl;
		String paratemer="";
		if(globalParameter.equals("")){
			paratemer=APIparameter;
		}
		else if(APIparameter.equals("")){
			paratemer=globalParameter;
		}else{
			paratemer=APIparameter+"&"+globalParameter;
		}
		if(featureType.equals("get")){
			result= HttpController.httpget(url,header,paratemer);
		}else{
			result=HttpController.httpost(url,header,paratemer);
		}
		String response=String.valueOf(result.get("response"));	
		//替换断言里的特殊字符
		if (StringUtils.isNotBlank(assertMessage)) {  
	        String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };  
	        for (String key : fbsArr) {  
	            if (assertMessage.contains(key)) {  
	            	assertMessage = assertMessage.replace(key, "\\" + key);  
	            }  
	        }  
	    }
		
		boolean isEqual=false;
		Pattern pt= Pattern.compile(assertMessage);
		Matcher mt=pt.matcher(response);
		if(null!=mt&&mt.find()){
			isEqual=true;
		}
		if(!result.get("statusCode").equals(assertCode)||!isEqual){		
			resultMap.add("未通过");
			reportResult++;
		}else{
			resultMap.add("通过");
		}	
		resultMap.add(response);
		resultMap.add(result.get("timeConsuming").toString());				
		resultMapGroup.add(resultMap);
	}
	
	/** 
	 * 转义正则特殊字符 （$()*+.[]?\^{},|） 
	 *  
	 * @param keyword 
	 * @return 
	 */  
	public static String escapeExprSpecialWord(String keyword) {  
	    if (StringUtils.isNotBlank(keyword)) {  
	        String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };  
	        for (String key : fbsArr) {  
	            if (keyword.contains(key)) {  
	                keyword = keyword.replace(key, "\\" + key);  
	            }  
	        }  
	    }  
	    return keyword;  
	}  

}

