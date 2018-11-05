package com.junker.autotest.util;

import com.junker.autotest.core.CreatHtmlReport;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class SendEmail {
	String to = "";// 收件人  
    String from = "";// 发件人  
    String host = "";// smtp主机  
    String username = "";  
    String password = "";  
    String filename = "";// 附件文件名  
    String subject = "";// 邮件主题  
    String content = "";// 邮件正文  
    Vector file = new Vector();// 附件文件集合 
    public SendEmail() {  
    }  
  
    /** 
     * <br> 
     * 方法说明：构造器，提供直接的参数传入 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public SendEmail(String to, String from, String smtpServer,  
            String username, String password, String subject, String content) {  
        this.to = to;  
        this.from = from;  
        this.host = smtpServer;  
        this.username = username;  
        this.password = password;  
        this.subject = subject;  
        this.content = content;  
    }  
  
    /** 
     * <br> 
     * 方法说明：设置邮件服务器地址 <br> 
     * 输入参数：String host 邮件服务器地址名称 <br> 
     * 返回类型： 
     */  
    public void setHost(String host) {  
        this.host = host;  
    }  
  
    /** 
     * <br> 
     * 方法说明：设置登录服务器校验密码 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public void setPassWord(String pwd) {  
        this.password = pwd;  
    }  
  
    /** 
     * <br> 
     * 方法说明：设置登录服务器校验用户 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public void setUserName(String usn) {  
        this.username = usn;  
    }  
  
    /** 
     * <br> 
     * 方法说明：设置邮件发送目的邮箱 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public void setTo(String to) {  
        this.to = to;  
    }  
  
    /** 
     * <br> 
     * 方法说明：设置邮件发送源邮箱 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public void setFrom(String from) {  
        this.from = from;  
    }  
  
    /** 
     * <br> 
     * 方法说明：设置邮件主题 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public void setSubject(String subject) {  
        this.subject = subject;  
    }  
  
    /** 
     * <br> 
     * 方法说明：设置邮件内容 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public void setContent(String content) {  
        this.content = content;  
    }  
  
    /** 
     * <br> 
     * 方法说明：把主题转换为中文 <br> 
     * 输入参数：String strText <br> 
     * 返回类型： 
     */  
    public String transferChinese(String strText) {  
        try {  
            strText = MimeUtility.encodeText(new String(strText.getBytes(),  
                    "GB2312"), "GB2312", "B");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return strText;  
    }  
  
    /** 
     * <br> 
     * 方法说明：往附件组合中添加附件 <br> 
     * 输入参数： <br> 
     * 返回类型： 
     */  
    public void attachfile(String fname) {  
        file.addElement(fname);  
    }  
    
    private String getMailList(String[] mailArray) { 

    	StringBuffer toList = new StringBuffer(); 
    	int length = mailArray.length; 
    	if (mailArray != null && length < 2) { 
    	toList.append(mailArray[0]); 
    	} else { 
    	for (int i = 0; i < length; i++) { 
    	toList.append(mailArray[i]); 
    	if (i != (length - 1)) { 
    	toList.append(","); 
    	} 

    	} 
    	} 
    	return toList.toString(); 

    	} 

    /** 
     * <br> 
     * 方法说明：发送邮件 <br> 
     * 输入参数： <br> 
     * 返回类型：boolean 成功为true，反之为false 
     */  
    public boolean mailUtility() {  
  
        // 构造mail session  
        Properties props = new Properties() ;  
        props.put("mail.smtp.host", host);  
        props.put("mail.smtp.auth", "true");  
        Session session = Session.getDefaultInstance(props,  
                new Authenticator() {  
                    public PasswordAuthentication getPasswordAuthentication() {  
                        return new PasswordAuthentication(username, password);  
                    }  
                });   
        try {  
            // 构造MimeMessage 并设定基本的值  
            MimeMessage msg = new MimeMessage(session);  
            //MimeMessage msg = new MimeMessage();  
            msg.setFrom(new InternetAddress(from));    
            
            //支持多个收件人，以“,”分割
            String toList=null;
            String[] recipientsList=to.split(",");
            if(null!=recipientsList){
            	toList = getMailList(recipientsList); 
            	InternetAddress[] iaToList = new InternetAddress().parse(toList); 
            	msg.setRecipients(Message.RecipientType.TO, iaToList); // 收件人 
            }
            msg.setSubject(subject);  
  
            // 构造Multipart  
            Multipart mp = new MimeMultipart();    
            // 向Multipart添加正文  
            MimeBodyPart mbpContent = new MimeBodyPart();  
            mbpContent.setContent(content, "text/html;charset=gb2312");                
            // 向MimeMessage添加（Multipart代表正文）  
            mp.addBodyPart(mbpContent);                    		
            // 向Multipart添加附件  
            Enumeration efile = file.elements();  
            while (efile.hasMoreElements()) {   
                MimeBodyPart mbpFile = new MimeBodyPart();  
                filename = efile.nextElement().toString();  
                FileDataSource fds = new FileDataSource(filename);  
                mbpFile.setDataHandler(new DataHandler(fds));     
                String filename= new String(fds.getName().getBytes(), StandardCharsets.ISO_8859_1);
                mbpFile.setFileName(filename);  
                // 向MimeMessage添加（Multipart代表附件）  
                mp.addBodyPart(mbpFile);  
            }  
  
            file.removeAllElements();  
            // 向Multipart添加MimeMessage  
            msg.setContent(mp);  
            msg.setSentDate(new Date());  
            msg.saveChanges() ;  
            // 发送邮件               
            Transport transport = session.getTransport("smtp");  
            transport.connect(host, username, password);  
            transport.sendMessage(msg, msg.getAllRecipients());  
            transport.close();  
        } catch (Exception mex) {  
            mex.printStackTrace();  
            return false;  
        }  
        return true;  
    }  

    public static boolean sendmail(String to,String caseFilePath,String testPlst,String testEnvironmentUrl,Exception e) {  
    	if(null==to||null==testPlst||null==testEnvironmentUrl||to.length()==0||testPlst.length()==0||testEnvironmentUrl.length()==0){
    		return false;
    	}
    	SendEmail sendmail = new SendEmail();           
        sendmail.setHost("");
        sendmail.setUserName("zhangjk@***.cn");
        sendmail.setPassWord("");
        sendmail.setTo(to+",zhangjk@***.cn");
        sendmail.setFrom("Autotest@QA-***.com");
        sendmail.setSubject("接口自动测试报告_"+testPlst+"_"+testEnvironmentUrl);  
    	if(null!=caseFilePath){
    		if(caseFilePath.length()!=0){
    			sendmail.attachfile(caseFilePath);	
    		}   		
    	} 
        sendmail.setContent(CreatHtmlReport.reportContent);
        return sendmail.mailUtility();
    }  
}
