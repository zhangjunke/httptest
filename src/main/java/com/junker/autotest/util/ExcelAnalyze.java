package com.junker.autotest.util;
import java.io.File;  
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;  
import org.apache.poi.ss.usermodel.Sheet;  
import org.apache.poi.ss.usermodel.Workbook;  
import org.apache.poi.ss.usermodel.WorkbookFactory;  
import org.apache.poi.ss.util.CellRangeAddress;  
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
  
public class ExcelAnalyze { 
	public static LinkedHashMap<String,Object> featureMap=new LinkedHashMap<String,Object>();
	public static void main(String[] args){
		String path="/";
		System.out.println("path:"+path);
		ExcelAnalyze tr=new ExcelAnalyze();
		Workbook wb = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
			if(path.toLowerCase().endsWith("xlsx")){  
				wb = new XSSFWorkbook(fis);  
	        }else if(path.toLowerCase().endsWith("xls")){  
	        	wb = new HSSFWorkbook(fis);  
	        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		Sheet sheet = wb.getSheetAt(0); 
		for(int i=1;i<=sheet.getLastRowNum();i++){			
			boolean isM=false;
			String Mergedvalue="";
			String commonvalue="";			
			Row row =sheet.getRow(i); 
			for(int j=0;j<10;j++){
				isM=tr.isMergedRegion(sheet, i, j);
				System.out.println("第"+i+"行"+"第"+j+"列是合并单元格："+isM);
				Mergedvalue=tr.getMergedRegionValue(sheet, i, j);
				Cell cell=row.getCell(j);
				commonvalue= getCellValue(cell, i, j);
				System.out.println("Mergedvalue:"+Mergedvalue);
				System.out.println("commonvalue:"+commonvalue);
			}						
		}
		
		
	}
	
	public boolean writeTestResult(String path,List<Integer> rownum,List<Integer> coloumnum,List<String> value) throws EncryptedDocumentException, IOException{
		Workbook wb = null;
		FileInputStream fis = new FileInputStream(path); 
		if(path.toLowerCase().endsWith("xlsx")){  
			wb = new XSSFWorkbook(fis);  
        }else if(path.toLowerCase().endsWith("xls")){  
        	wb = new HSSFWorkbook(fis);  
        }  
		Sheet sheet = wb.getSheetAt(0); 
		
		for(int i=1;i<=rownum.size();i++){
			Row row =sheet.getRow(rownum.get(i-1)); 
			Cell cell=row.getCell(coloumnum.get(i-1));
			String cellValue=value.get(i-1);
			if(cellValue.length()>=32500){ //单元格最长可存入32767个字符
				cellValue=cellValue.substring(0,32500)+"...太多显示不下了...";
			}
			cell.setCellValue(cellValue);
		}
		FileOutputStream fos = new FileOutputStream(path);  
	    wb.write(fos); 
	    sheet.setDefaultRowHeight((short)100); 
	    fos.close();  
		fis.close();
		wb.close();
		return true;
	}

    /**  
    * 读取excel数据  
    * @param path  
    */  
    public void readExcelToObj(String path) {       
        Workbook wb = null; 
        try {  
            wb = WorkbookFactory.create(new File(path));  
            System.out.println(readExcel(wb, 0, 0, 0));  
        } catch (InvalidFormatException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
      
    /**  
    * 读取excel文件  
    * @param wb   
    * @param sheetIndex sheet页下标：从0开始  
    * @param startReadLine 开始读取的行:从0开始  
    * @param tailLine 去除最后读取的行  
    */   
    private String readExcel(Workbook wb,int sheetIndex, int startReadLine, int tailLine) { 
    	ExcelAnalyze tr=new ExcelAnalyze();
        Sheet sheet = wb.getSheetAt(sheetIndex); 
        int rowCount=sheet.getLastRowNum();
        int coloumNum=sheet.getRow(0).getPhysicalNumberOfCells();

        if(coloumNum!=10){
        	return "文件格式不正确！！！";
        }
        if(rowCount<1){
        	return "文件内容为空！！！";
        }        
        LinkedHashMap<Integer,String> featureMap2=new LinkedHashMap<Integer,String>();
		List<Integer> startline=new ArrayList<Integer>();
		List<Integer> endline=new ArrayList<Integer>();
		List<String> FName=new ArrayList<String>();
		for(int i=1;i<=rowCount;i++){
			Row row =sheet.getRow(i); 
			Cell c0=row.getCell(0);
			c0.setCellType(Cell.CELL_TYPE_STRING);
			String featureName="";
			if(tr.isMergedRegion(sheet, i, 0)){
				featureName=c0.getRichStringCellValue().toString();
			}else{
				featureName= getCellValue(c0, i, 0);
			}
			if(!featureName.equals("")&&!FName.contains(featureName)){
				startline.add(i);
				FName.add(featureName);
			}
			
			Cell c1=row.getCell(1);
			c1.setCellType(Cell.CELL_TYPE_STRING);
			String featureUrl="";
			if(tr.isMergedRegion(sheet, i, 1)){
				featureUrl=getMergedRegionValue(sheet, row.getRowNum(), c1.getColumnIndex());
			}else{
				featureUrl= getCellValue(c1, i, 1);
			}

			
			Cell c2=row.getCell(2);
			c2.setCellType(Cell.CELL_TYPE_STRING);
			String featureType="";
			if(tr.isMergedRegion(sheet, i, 2)){
				featureType=getMergedRegionValue(sheet, row.getRowNum(), c2.getColumnIndex());
			}else{
				featureType= getCellValue(c2, i, 2);
			}


			if(null!=featureName&&null!=featureUrl&&null!=featureUrl){
				if(featureName.length()>0&&featureUrl.length()>0&&featureType.length()>0){
					featureMap.put(featureName+"_"+"featureName",featureName);
					featureMap2.put(i,featureName);
					featureMap.put(featureName+"_"+"featureUrl",featureUrl);    
					featureMap.put(featureName+"_"+"featureType",featureType); 
				}					   
			}	
		}
			for(int k=1;k<startline.size();k++){
				endline.add(startline.get(k)-1);
			}
			endline.add(rowCount);
			for(int j=0;j<startline.size();j++){
				LinkedHashMap<String,String> caseMap=new LinkedHashMap<String,String>();
				int start=startline.get(j);
				int end=endline.get(j);
				for(int k=start;k<=end;k++){
					Row row =sheet.getRow(k); 				
					Cell c3=row.getCell(3);
					Cell c4=row.getCell(4);
					Cell c5=row.getCell(5);
					Cell c6=row.getCell(6);
					c3.setCellType(Cell.CELL_TYPE_STRING);
					c4.setCellType(Cell.CELL_TYPE_STRING);
					c5.setCellType(Cell.CELL_TYPE_STRING);
					c6.setCellType(Cell.CELL_TYPE_STRING);
					String casename=c3.getRichStringCellValue().getString();
					String APIparameter=c4.getRichStringCellValue().getString();
					String assertstatusCode=c5.getRichStringCellValue().getString();
					String assertmessage=c6.getRichStringCellValue().getString();
					caseMap.put(casename+"_casename",casename);	
					caseMap.put(casename+"_APIparameter",APIparameter);	
					caseMap.put(casename+"_assertstatusCode",assertstatusCode);						
					caseMap.put(casename+"_assertmessage",assertmessage);	
					featureMap.put(featureMap2.get(start)+"_"+"caseMap",caseMap);					
				}
			}
			System.out.println("startline:"+startline);
			System.out.println("endline:"+endline);

        return "OK";
    }  
  
    /**   
    * 获取合并单元格的值   
    * @param sheet   
    * @param row   
    * @param column   
    * @return   
    */    
    public String getMergedRegionValue(Sheet sheet ,int row , int column){    
        
        int sheetMergeCount = sheet.getNumMergedRegions();    
            
        for(int i = 0 ; i < sheetMergeCount ; i++){    
            CellRangeAddress ca = sheet.getMergedRegion(i);    
            int firstColumn = ca.getFirstColumn();    
            int lastColumn = ca.getLastColumn();    
            int firstRow = ca.getFirstRow();    
            int lastRow = ca.getLastRow();    
                
            if(row >= firstRow && row <= lastRow){    
                    
                if(column >= firstColumn && column <= lastColumn){    
                    Row fRow = sheet.getRow(firstRow);    
                    Cell fCell = fRow.getCell(firstColumn);    
                    return getCellValue(fCell,row , column) ;    
                }    
            }    
        }    
            
        return null ;    
    }    
      
    /**  
    * 判断合并了行  
    * @param sheet  
    * @param row  
    * @param column  
    * @return  
    */  
    private boolean isMergedRow(Sheet sheet,int row ,int column) {  
      
      int sheetMergeCount = sheet.getNumMergedRegions();  
      for (int i = 0; i < sheetMergeCount; i++) {  
        CellRangeAddress range = sheet.getMergedRegion(i);  
        int firstColumn = range.getFirstColumn();  
        int lastColumn = range.getLastColumn();  
        int firstRow = range.getFirstRow();  
        int lastRow = range.getLastRow();  
        if(row == firstRow && row == lastRow){  
            if(column >= firstColumn && column <= lastColumn){  
                return true;  
            }  
        }  
      }  
      return false;  
    }  
      
    /**  
    * 判断指定的单元格是否是合并单元格  
    * @param sheet   
    * @param row 行下标  
    * @param column 列下标  
    * @return  
    */  
    private boolean isMergedRegion(Sheet sheet,int row ,int column) {  
      
      int sheetMergeCount = sheet.getNumMergedRegions();  
      for (int i = 0; i < sheetMergeCount; i++) {  
        
        CellRangeAddress range = sheet.getMergedRegion(i);  
        int firstColumn = range.getFirstColumn();  
        int lastColumn = range.getLastColumn();  
        int firstRow = range.getFirstRow();  
        int lastRow = range.getLastRow();  
        if(row >= firstRow && row <= lastRow){  
            if(column >= firstColumn && column <= lastColumn){  
                return true;  
            }  
        }  
      }  
      return false;  
    }  
      
    /**  
    * 判断sheet页中是否含有合并单元格   
    * @param sheet   
    * @return  
    */  
    private boolean hasMerged(Sheet sheet) {  
            return sheet.getNumMergedRegions() > 0;
    }  
      
    /**  
    * 合并单元格  
    * @param sheet   
    * @param firstRow 开始行  
    * @param lastRow 结束行  
    * @param firstCol 开始列  
    * @param lastCol 结束列  
    */  
    private void mergeRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {  
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));  
    }  
      
    /**   
    * 获取单元格的值   
    * @param cell   
    * @return   
    */    
    public static String  getCellValue(Cell cell, int rowIndex, int cellnum){  
        if(cell == null){  
            return "";  
        }else {
			cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        return cell.getRichStringCellValue().getString();  
    }  
	public static String xlsx2String(File file){  
	    String result = "";  
	    try {  
	        FileInputStream fis = new FileInputStream(file);  
	        StringBuilder sb = new StringBuilder();  
	        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fis);  
	          
	        //read the sheet  
	        for(int numSheet=0;numSheet<xssfWorkbook.getNumberOfSheets();numSheet++){  
	            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);  
	            if(xssfSheet ==null){  
	                continue;  
	            }  
	            //read the row  
	            for(int rowNum=0; rowNum<=xssfSheet.getLastRowNum();rowNum++){  
	                XSSFRow xssfRow = xssfSheet.getRow(rowNum);  
	                xssfRow.getCell(rowNum).setCellType(Cell.CELL_TYPE_STRING);
	                for(int cosNum=0;cosNum<xssfRow.getLastCellNum();cosNum++){  
	                    XSSFCell cell = xssfRow.getCell(cosNum);  
	                    cell.setCellType(Cell.CELL_TYPE_STRING);
	                    sb.append(cell.getStringCellValue());  
	                }  
	            }  
	        }  
	        fis.close();  
	        result +=sb.toString();  
	    } catch (IOException e) {  
	        // TODO Auto-generated catch block  
	        e.printStackTrace();  
	    }  
	    return result;  
	}      
} 