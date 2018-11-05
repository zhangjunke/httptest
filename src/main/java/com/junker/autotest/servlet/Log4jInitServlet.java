package com.junker.autotest.servlet;

import org.apache.log4j.Logger;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.log4j.PropertyConfigurator;

@WebServlet(name = "Log4jInitServlet")
public class Log4jInitServlet extends HttpServlet {
    static Logger logger = Logger.getLogger(Log4jInitServlet.class);
    public Log4jInitServlet() {
    }

    public void init(ServletConfig config) {
        String prefix = config.getServletContext().getRealPath("/");
        String file = config.getInitParameter("log4j");
        String filePath = prefix + file;
        Properties props = new Properties();
        try {
            FileInputStream istream = new FileInputStream(filePath);
            props.load(istream);
            istream.close();
            //toPrint(props.getProperty("log4j.appender.file.File"));
            String DlogFile = prefix + props.getProperty("log4j.appender.D.File");//设置路径
            String ElogFile = prefix + props.getProperty("log4j.appender.E.File");//设置路径
            props.setProperty("log4j.appender.D.File",DlogFile);
            props.setProperty("log4j.appender.E.File",ElogFile);
            PropertyConfigurator.configure(props);//装入log4j配置信息
        } catch (IOException e) {
            toPrint("Could not read configuration file [" + filePath + "].");
            toPrint("Ignoring configuration file [" + filePath + "].");
            return;
        }
    }

    public static void toPrint(String content) {
        System.out.println(content);
    }
}
