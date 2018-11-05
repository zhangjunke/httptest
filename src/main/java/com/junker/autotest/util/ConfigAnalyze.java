package com.junker.autotest.util;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.xml.sax.SAXException;

/**
 * 解析平台和接口两个xml，分别存储在两个多级map中
 * @author zhangjunke
 *
 */
public class ConfigAnalyze {	
	public static HashMap<String,String> HG=new HashMap<String,String>();
	public static LinkedHashMap<String,Object> featureMap=new LinkedHashMap<String,Object>();
	public static void platConfig(String platconfigurl) throws ConfigurationException, ParserConfigurationException, SAXException, IOException{	
		Configuration PlatConfig = new XMLConfiguration(platconfigurl); 
		int PlatCount= XMLAnalyze.groupsCount(platconfigurl, "/span/content/plat/platname");
		//遍历plat节点
		for(int i=0;i<PlatCount;i++){		
			String platname=PlatConfig.getString("content.plat("+i+").platname");
			String header1=PlatConfig.getString("content.plat("+i+").headers");
			String globalparameter1=PlatConfig.getString("content.plat("+i+").globalparameter");
			String header="";
			String globalparameter="";
			if(header1.length()!=0){
				header=header1;
			}
			if(globalparameter1.length()!=0){
				globalparameter=globalparameter1;
			}
			HG.put(platname+"header", header);
			HG.put(platname+"globalparameter", globalparameter);
		}
	}
	
	public static void APIConfig(String apiconfigurl) throws ConfigurationException, ParserConfigurationException, SAXException, IOException{
		Configuration APIConfig = new XMLConfiguration(apiconfigurl);
		int featureCount=XMLAnalyze.groupsCount(apiconfigurl, "/span/content/feature");//�õ�xml��feature����
		//遍历plat节点
		for(int i=0;i<featureCount;i++){
			LinkedHashMap<String,String> caseMap=new LinkedHashMap<String,String>();
			String featureName=APIConfig.getString("content.feature("+i+").featurename");
			String featureUrl=APIConfig.getString("content.feature("+i+").featureurl");
			String featureType=APIConfig.getString("content.feature("+i+").featuretype");
			featureMap.put(featureName+"_"+"featureName",featureName);
			featureMap.put(featureName+"_"+"featureUrl",featureUrl);
			featureMap.put(featureName+"_"+"featureType",featureType);
			int caseCount=XMLAnalyze.groupsCount(apiconfigurl, "/span/content/feature["+(i+1)+"]/case");//�õ�xml��case����
			for(int j=0;j<caseCount;j++){
				String casename=APIConfig.getString("content.feature("+i+").case("+j+").casename");	
				String APIparameter=APIConfig.getString("content.feature("+i+").case("+j+").APIparameter");
				String assertstatusCode=APIConfig.getString("content.feature("+i+").case("+j+").assertstatusCode");
				String assertmessage=APIConfig.getString("content.feature("+i+").case("+j+").assertmessage");
				String assertmore=APIConfig.getString("content.feature("+i+").case("+j+").assertmore");					
				caseMap.put(casename+"_casename",casename);
				caseMap.put(casename+"_APIparameter",APIparameter);
				caseMap.put(casename+"_assertstatusCode",assertstatusCode);
				caseMap.put(casename+"_assertmessage",assertmessage);
				caseMap.put(casename+"_assertmore",assertmore);	
				featureMap.put(featureName+"_"+"caseMap",caseMap);
					}					
				}			
		}
}
