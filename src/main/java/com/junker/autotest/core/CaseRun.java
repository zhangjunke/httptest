package com.junker.autotest.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import com.junker.autotest.servlet.Log4jInitServlet;
import com.junker.autotest.util.ConfigAnalyze;
import com.junker.autotest.util.ExcelAnalyze;
import org.apache.commons.lang.exception.NestableException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * 解析xml后会获得多级map，解析map将传递各项参数到执行用例函数和生成报告函数
 * @author zhangjunke
 *
 */
public class CaseRun {
	private static Logger logger =Logger.getLogger(Log4jInitServlet.class);
	public static String reportPath="";
	public static void runCase(String testEnvironmentUrl,String testPlat,String platconfigurl,String caseFile,String rootpath) throws ParserConfigurationException, SAXException, IOException, NestableException{
		if(testEnvironmentUrl.length()==0||testPlat.length()==0){
			logger.error("testEnvironment或testPlat未设置！");
		}else{
			ConfigAnalyze.platConfig(platconfigurl);
			HashMap<String,String> HG=ConfigAnalyze.HG;
			LinkedHashMap<String,Object> featureMap= ExcelAnalyze.featureMap;
			logger.debug(featureMap);
			String header="";
			String globalParameter="";
			String featureName="";
			String featureUrl="";
			String featureType="";
			String casename="";
			String APIparameter="";
			String assertstatusCode="";
			String assertmessage="";
			//String assertmore="";
			//获取testplat&header&globalParameter
			header=HG.get(testPlat+"header");
			globalParameter=HG.get(testPlat+"globalparameter");
			if(null==header){
				header="";
			}
			if(null==globalParameter){
				globalParameter="";
			}
			System.out.println("CaseRun:"+"line52");
			//获取testplat&feature
			Iterator<Entry<String, Object>> iter = featureMap.entrySet().iterator();
			ArrayList<String>  featureNamelist=new ArrayList<String>();
			while (iter.hasNext()) {
				Map.Entry entry = iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				if(key.toString().contains("featureName")){
					featureNamelist.add(val.toString());
				}
			}
			for( Iterator it =featureNamelist.iterator();it.hasNext();){
				featureName=it.next().toString();
				featureUrl=(String) featureMap.get(featureName+"_featureUrl");
				featureType=(String) featureMap.get(featureName+"_featureType");
				LinkedHashMap<String,String> caseMap=(LinkedHashMap<String, String>) featureMap.get(featureName+"_"+"caseMap");
				Iterator iter1 = caseMap.entrySet().iterator();
				ArrayList<String>  casenamelist=new ArrayList<String>();
				while (iter1.hasNext()) {
					Map.Entry entry1 = (Map.Entry) iter1.next();
					Object key1 = entry1.getKey();
					Object val1 = entry1.getValue();
					if(key1.toString().contains("casename")){
						casenamelist.add(val1.toString());
					}
				}
				for( Iterator it1 =casenamelist.iterator();it1.hasNext();){
					casename=it1.next().toString();
					APIparameter=caseMap.get(casename+"_APIparameter");
					assertstatusCode=caseMap.get(casename+"_assertstatusCode");
					assertmessage=caseMap.get(casename+"_assertmessage");
					//assertmore=caseMap.get(casename+"_assertmore");
					//校验必填项
					if(testEnvironmentUrl.length()==0||featureName.length()==0||featureType.length()==0||featureUrl.length()==0||
							casename.length()==0||assertstatusCode.length()==0||assertmessage.length()==0){
						logger.error("必填参数为空！！！");
					}else{
						logger.debug("【\nserverUrl:"+testEnvironmentUrl);
						logger.debug("featureName:"+featureName);
						logger.debug("featureType:"+featureType);
						logger.debug("featureUrl:"+featureUrl);
						logger.debug("casename:"+casename);
						logger.debug("APIparameter:"+APIparameter);
						logger.debug("assertmessage:"+assertmessage+"\n】");
						ResultAssert.assertResult(testPlat, testEnvironmentUrl, testEnvironmentUrl, header, globalParameter, featureName,
								featureType, featureUrl, casename, APIparameter, assertstatusCode, assertmessage);
					}
				}
			}
			CreatHtmlReport.creatReport(testPlat,testEnvironmentUrl,rootpath);
			CreatHtmlReport.writeResult(testPlat, caseFile);
		}
	}
}