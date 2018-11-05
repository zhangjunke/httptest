package com.junker.autotest.global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.junker.autotest.global.HttpController;
import com.junker.autotest.servlet.Log4jInitServlet;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;


/**
 * mysql的备份、恢复、执行语句封装
 * @author zhangjunke
 *
 */
public class SQLController {
	private static Logger logger =Logger.getLogger(Log4jInitServlet.class);
	public static void insertSQL(String sql) throws ConfigurationException{
		Configuration config = new XMLConfiguration("initsql.xml"); 
		String mysqlUrl="jdbc:mysql://"+config.getString("content.finalparameter.sqlhost")+":"+config.getString("content.finalparameter.sqlport")+"/"+config.getString("content.finalparameter.tablename");
		String username=config.getString("content.finalparameter.sqlusername");
		String password=config.getString("content.finalparameter.sqlpassword");
		String allowMultiQueries=config.getString("content.finalparameter.sqlallowMultiQueries");
		
		Connection conn = null; 
		Statement stmt = null; 
		
		try {
			Class.forName("com.mysql.jdbc.Driver");	
			conn=DriverManager.getConnection(mysqlUrl+"?"+"user="+username+"&password="+password+"&allowMultiQueries="+allowMultiQueries);
			stmt=conn.createStatement();
			conn.setAutoCommit(false);
			stmt.addBatch(sql);
			stmt.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String backupSQL() throws SQLException, ConfigurationException {  
		Configuration config = new XMLConfiguration("initsql.xml"); 
		String tablename=config.getString("content.finalparameter.tablename");
		String mysqlUrl="jdbc:mysql://"+config.getString("content.finalparameter.sqlhost")+":"+config.getString("content.finalparameter.sqlport")+"/"+config.getString("content.finalparameter.tablename");
		String username=config.getString("content.finalparameter.sqlusername");
		String password=config.getString("content.finalparameter.sqlpassword");
		String allowMultiQueries=config.getString("content.finalparameter.sqlallowMultiQueries");
		Connection conn = DriverManager.getConnection(mysqlUrl+"?"+"user="+username+"&password="+password+"&allowMultiQueries="+allowMultiQueries);
		String bakupFilePath=System.getProperty("user.dir")+"\\"+System.currentTimeMillis()+ ".sql";
		logger.debug("bakupFilePath:"+bakupFilePath);
        try {  
            String bakSQL = "mysqldump "+"-h"+config.getString("content.finalparameter.sqlhost")+" -u"+username+" -p"+password+" --skip-lock-tables "+tablename;
            logger.debug("bakSQL:"+bakSQL);
            Runtime rt=Runtime.getRuntime();
            Process process = rt.exec(bakSQL); 
            InputStream inputStream = process.getInputStream();//得到输入流，写成.sql文件
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(reader);
            String s = null;
            StringBuffer sb = new StringBuffer();
            while((s = br.readLine()) != null){
                sb.append(s+"\\r\\n");
            }
            s = sb.toString();
            FileOutputStream fout = new FileOutputStream(bakupFilePath);
            OutputStreamWriter writer = new OutputStreamWriter(fout, StandardCharsets.UTF_8);
            writer.write(s);
            writer.flush();
            inputStream.close();
            reader.close();
            br.close();
            writer.close();
            fout.close();
            File file = new File(bakupFilePath);
            file.getParentFile().mkdirs();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(s.getBytes());
            fileOutputStream.close();
            br.close();
            reader.close();
            inputStream.close();          
            conn.close();
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return bakupFilePath;
    } 
	
	 public static void recoverySQL(String bakupFilePath) throws SQLException, ConfigurationException {  
			Configuration config = new XMLConfiguration("initsql.xml"); 
			String tablename=config.getString("content.finalparameter.tablename");
			String mysqlUrl="jdbc:mysql://"+config.getString("content.finalparameter.sqlhost")+":"+config.getString("content.finalparameter.sqlport")+"/"+config.getString("content.finalparameter.tablename");
			String username=config.getString("content.finalparameter.sqlusername");
			String password=config.getString("content.finalparameter.sqlpassword");
			String allowMultiQueries=config.getString("content.finalparameter.sqlallowMultiQueries");
			Connection conn = DriverManager.getConnection(mysqlUrl+"?"+"user="+username+"&password="+password+"&allowMultiQueries="+allowMultiQueries);
	        try {  
	            String recSQL="mysql "+"-h"+config.getString("content.finalparameter.sqlhost")+" -u"+username+" -p"+password+" "+tablename;
	            logger.debug("recSQL:"+recSQL);
	            Runtime rt=Runtime.getRuntime(); 
	            Process process = rt.exec("cmd /c"+recSQL); 
	            OutputStream outputStream = process.getOutputStream();
	            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(bakupFilePath), StandardCharsets.UTF_8));
	            String str = null;
	            StringBuffer sb = new StringBuffer();
	            while((str = br.readLine()) != null){
	                sb.append(str+"\\r\\n");
	            }
	            str = sb.toString();
	            OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
	            writer.write(str);
	            writer.flush();
	            outputStream.close();
	            br.close();
	            writer.close();	            
	            conn.close();
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  
}
