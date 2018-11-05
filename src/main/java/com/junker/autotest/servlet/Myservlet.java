package com.junker.autotest.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.junker.autotest.core.CaseRun;
import com.junker.autotest.core.CreatHtmlReport;
import com.junker.autotest.core.ResultAssert;
import com.junker.autotest.util.DeleteHistoryLogs;
import com.junker.autotest.util.ExcelAnalyze;
import com.junker.autotest.util.FileOperation;
import com.junker.autotest.util.SendEmail;
import org.apache.log4j.Logger;

import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
@WebServlet("/Myservlet")
public class Myservlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static Logger logger =Logger.getLogger(Log4jInitServlet.class);
    String rootpath1=this.getClass().getClassLoader().getResource("/").getPath();

    public Myservlet() {
        super();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {response.setContentType("text/html");
        //System.out.println("rootpath1:"+rootpath1);

        response.reset();
        response.setCharacterEncoding("utf8");
        request.setCharacterEncoding("utf8");
        response.setContentType("text/html;charset=utf8");
        PrintWriter out = response.getWriter();
        request.getRequestDispatcher("html/indexPage.html").forward(request, response);//转发
        System.out.println("hello");
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.reset();
        request.setCharacterEncoding("utf8");
        response.setCharacterEncoding("utf8");
        response.setContentType("text/html;charset=utf8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("  <HEAD><TITLE>HTTPTEST</TITLE>");
        out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf8\">");
        out.println("  <link rel='stylesheet' type='text/css'>");
        out.println("  </HEAD>");
        out.println("  <BODY>");

        out.println("<div align=center><br/>");
        out.println("<fieldset style='width:90%'><legend>接口自动测试</legend><br/>");
        out.println("       <div class='line'>");
        out.println("           <div align='left' class='leftDiv'>>>>开始测试<<<<br/></div>");
        out.println("           <div align='left' class='rightDiv'>");
        Exception e=new Exception();
        String rootpath=rootpath1.replace("WEB-INF/classes/", "");
        String testPlat=null;
        String testEnvironmentUrl=null;
        String recipient=null;
        String caseFile=null;
        try{
            //使用 FileItemFactory 对象解析 request
            DiskFileItemFactory  factory = new DiskFileItemFactory();
            //设置文件的缓存路径
            String tmp=rootpath+"Upload/tmp/";
            factory.setRepository(new File(tmp));
            ServletFileUpload upload = new ServletFileUpload(factory);
            //设置整个表单的最大字节数为1G
            upload.setSizeMax(1024*1024*1024);
            //设置上传文件大小的上限-1表示无上限
            upload.setFileSizeMax(1024*1024*100);
            List items = new ArrayList();
            try {
                //上传文件并解析出所有的表单字段包括普通字段和文件字段
                items = upload.parseRequest(request);
            } catch (FileUploadException e1) {
                out.println("上传文件遇到问题：" + e1.getMessage()+"<br/>");
            }

            //下面对每个字段进行处理分表单字段和文件字段
            Iterator it = items.iterator();
            int flag=0;//标识是否上传了文件，0=是、1=否
            while(it.hasNext()){
                FileItem fileItem = (FileItem) it.next();
                //如果是表单字段
                if(fileItem.isFormField()){
                    out.println(fileItem.getFieldName() + ":  " + fileItem.getString("utf8") + "<br/>");
                    if(fileItem.getFieldName().equals("测试环境地址")){
                        testEnvironmentUrl=fileItem.getString("utf8");
                    }
                    else if(fileItem.getFieldName().equals("测试报告接收邮箱")){
                        recipient=fileItem.getString("utf8");
                    }
                    else if(fileItem.getFieldName().equals("测试平台")){
                        testPlat=fileItem.getString("utf8");
                    }
                }

                //如果是非表单字段
                if(!fileItem.isFormField()){
                    if(fileItem.getName().equals("")){
                        out.println(fileItem.getFieldName() + ":  " + "未上传文件"+"<br/>");
                        flag=1;
                    }else {
                        //保存文件其实就是把缓存里的数据写到目标路径下
                        out.println(fileItem.getFieldName() + ":  " + fileItem.getName() + "<br/>");
                        File newFile = new File("");
                        File fullFile = new File(fileItem.getName());
                        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date date = new Date();
                        caseFile=rootpath+"Upload/"+fullFile.getName().replace(".xlsx", "")+"_"+bartDateFormat.format(date)+".xlsx";
                        newFile = new File(caseFile);
                        fileItem.write(newFile);
                    }
                }
            }

            out.println("<br/>");

            String initFile=rootpath+"Properties/"+testPlat+".xlsx";
            File initF=new File(initFile);
            if(flag==1){
                if(!initF.exists()){
                    logger.error(initFile+" 文件不存在！！！");
                    out.println(initFile+" 文件不存在！！！<br/>");
                    return;
                }else{
                    SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date = new Date();
                    caseFile=rootpath+"Upload/" + testPlat+"_"+bartDateFormat.format(date)+".xlsx";
                    FileOperation.copy(initFile, caseFile);
                }
            }
            out.println("开始解析excel..."+"<br/>");
            ExcelAnalyze tr=new ExcelAnalyze();
            tr.readExcelToObj(caseFile);
            if(testPlat.length()==0||testEnvironmentUrl.length()==0){
                out.println("参数错误！！！"+"<br/>");
                return;
            }else{
                String platconfigurl=rootpath+"Properties/PlatConfig.xml";
                //String apiconfigurl=rootpath+"Properties/APIConfig_"+testPlat+".xml";
                File platFile=new File(platconfigurl);
                //File conFile=new File(apiconfigurl);
                if(!platFile.exists()){
                    out.println("PlatConfig.xml未配置！！！"+"<br/>");
                    logger.error("PlatConfig.xml未配置！！！");
                }
                else{
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                    Date now= new Date();
                    String today=df.format(now);
                    try {
                        DeleteHistoryLogs.deleteFile(today,rootpath);
                        out.println("开始执行测试用例..."+"<br/>");
                        CaseRun.runCase(testEnvironmentUrl,testPlat,platconfigurl,caseFile,rootpath);
                        if(ResultAssert.reportResult!=0){
                            out.println("测试结论：不通过。有一个或多个测试用例测试不通过。<br/>");
                        }else{
                            out.println("测试结论：通过。<br/>");
                        }
                    } catch (Exception e1) {
                        e=e1;
                        logger.error(e);
                        out.println("运行失败。请联系zhangjk@***.cn.<br/>>>>"+e+"<<<<br/>");
                    }
                    //重置测试结果
                    ResultAssert.resultMapGroup.clear();
                    ResultAssert.reportResult=0;
                    ExcelAnalyze.featureMap.clear();

                }
            }
        }catch(Exception e1){
            e=e1;
            out.println("<br/>>>>"+e+"<<<<br/>");
        }

        //发送测试报告
        SendEmail se=new SendEmail();
        if(null!=recipient){
            out.println("开始发送测试报告..."+"<br/>");
            boolean result = SendEmail.sendmail(recipient, caseFile,testPlat,testEnvironmentUrl,e);
            if(!result){
                out.println("\n测试报告发送失败，请检查邮箱是否输入有误。<br/>");
            }else{
                out.println("\n测试报告发送成功。<br/>");
            }
        }

        CreatHtmlReport.reportContent="";
        CreatHtmlReport.sb=new StringBuilder();
        out.println(">>>结束测试<<<<br/>");
        out.println("           </div>");
        out.println("       </div>");
        out.println("</fieldset></div>");
        out.println("  </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }
}