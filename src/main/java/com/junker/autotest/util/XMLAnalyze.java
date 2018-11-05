package com.junker.autotest.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 解析xml文件封装
 * @author zhangjunke
 *
 */
public class XMLAnalyze {
	 public static NodeList selectNodes(String express, Object source) {//查找节点，返回符合条件的节点集。
	        NodeList result=null;
	        XPathFactory xpathFactory=XPathFactory.newInstance();
	        XPath xpath=xpathFactory.newXPath();
	        try {
	            result=(NodeList) xpath.evaluate(express, source, XPathConstants.NODESET);
	        } catch (XPathExpressionException e) {
	            e.printStackTrace();
	        }
	        
	        return result;
	    }
	 
	 public static int groupsCount(String xmlPath,String express) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);
        DocumentBuilder db=dbf.newDocumentBuilder();
        Document xmldoc=db.parse(new File(xmlPath)); 
        Element root=null;       
        root=xmldoc.getDocumentElement();
        NodeList groups=selectNodes(express, root);
		int groupsCount=groups.getLength();
		return groupsCount;
	 }	 
}
