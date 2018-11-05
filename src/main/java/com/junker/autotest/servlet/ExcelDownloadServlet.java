package com.junker.autotest.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ExcelDownloadServlet
 */
@WebServlet("/ExcelDownloadServlet")
public class ExcelDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String rootpath1=this.getClass().getClassLoader().getResource("/").getPath();   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExcelDownloadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String rootpath=rootpath1.replace("WEB-INF/classes/", "");
		String filepath = rootpath+"Properties/templates.xlsx";
		//得到要下载的文件
		File file = new File(filepath);
		//如果文件不存在
		if(!file.exists()){
			request.setAttribute("message", "您要下载的资源已被删除！！");
			return;
		}
		//设置响应头，控制浏览器下载该文件
		response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("templates.xlsx", "UTF-8"));
		//读取要下载的文件，保存到文件输入流
		FileInputStream in = new FileInputStream(filepath);
		//创建输出流
		OutputStream outS = response.getOutputStream();
		//创建缓冲区
		byte buffer[] = new byte[1024];
		int len = 0;
		//循环将输入流中的内容读取到缓冲区当中
		while((len=in.read(buffer))>0){
		//输出缓冲区的内容到浏览器，实现文件下载
		outS.write(buffer, 0, len);
		}
		//关闭文件输入流
		in.close();
		//关闭输出流
		outS.close();
    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
	}

}
