/**
 * 
 */
package com.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.config.Configs;
import com.testscripts.BaseTest;

/**
 * @author aveluru
 * This class will wirte and read from an excel sheet
 */
public class ExcelReaderWriter {

	public static int getRowCount (String strExcelPath, String strSheetName)
	{
		int intRowCount = 0;
		try {
			FileInputStream fis = new FileInputStream(strExcelPath);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheet(strSheetName);
			intRowCount = s.getLastRowNum();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return intRowCount;
	}

	public static String getCellValue(String strExcelPath, String strSheetName, int intRowNum, int intColNum)
	{
		String strCellValue = null;
		try {
			FileInputStream fis = new FileInputStream(strExcelPath);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheet(strSheetName);
			strCellValue = s.getRow(intRowNum).getCell(intColNum).toString();
			//System.out.println(strCellValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strCellValue;
	}

	public static int getIntCellValue(String strExcelPath, String strSheetName, int intRowNum, int intColNum)
	{
		int intCellValue = 0;
		try {
			FileInputStream fis = new FileInputStream(strExcelPath);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheet(strSheetName);
			Row myRow = s.getRow(intRowNum);
			myRow.getCell(intColNum).setCellType(Cell.CELL_TYPE_STRING);
			intCellValue = Integer.parseInt(myRow.getCell(intColNum).toString());
			//System.out.println(strCellValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return intCellValue;
	}

	public synchronized static void  setCellValue(String strExcelPath, String strSheetName, int intRowNum, int intColNum, String strTestCaseStatus)
	{
		try
		{
			FileInputStream fis = new FileInputStream(strExcelPath);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheet(strSheetName);
			//s.getRow(intRowNum).getCell(intColNum).setCellValue(strTestCaseStatus);
			Row row = s.getRow(intRowNum);
			Cell cell = row.getCell(intColNum);
			cell = row.createCell(intColNum);

			CellStyle style = wb.createCellStyle();
			//style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
			style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			Font font = wb.createFont();

			if(strTestCaseStatus.equalsIgnoreCase("Pass"))
			{
				font.setColor(IndexedColors.GREEN.getIndex());
			}
			else
			{
				font.setColor(IndexedColors.RED.getIndex());	
			}

			style.setFont(font);
			cell.setCellValue(strTestCaseStatus);
			cell.setCellStyle(style);

			FileOutputStream fos = new FileOutputStream(strExcelPath);
			wb.write(fos);
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String createDetailedTestLogs(String strFolderPath)
	{
		String strDetailedTestLog = strFolderPath + "/" + Configs.strDetailedTestLog;
		Workbook wb = new XSSFWorkbook();
		FileOutputStream fileOut;
		try 
		{
			fileOut = new FileOutputStream(strDetailedTestLog);
			wb.write(fileOut);
			fileOut.close();
			wb.close(); // since we have opened a workbook, we are closing it after working on it.
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDetailedTestLog;
	}

	public static synchronized void writeDetailedTestLogs(String strExcelPath, String strSheetName, HashMap<String, Object[]> hashMapResult)
	{
		XSSFWorkbook workbook = null;
		try {
			FileInputStream fis = new FileInputStream(strExcelPath);
			workbook = new XSSFWorkbook(fis); 

			//Create a blank sheet
			XSSFSheet sheet = workbook.createSheet(strSheetName);

			//This data needs to be written (Object[])
			Map<String, Object[]> data = hashMapResult;   
			//Iterate over data and write to sheet
			Set<String> keyset = data.keySet();
			int rownum = 0;
			for (String key : keyset)
			{
				int intLongJSONResponse = 1;
				Row row = sheet.createRow(rownum++);
				Object [] objArr = data.get(key);
				int cellnum = 0;
				for (Object obj : objArr)
				{
					Cell cell = row.createCell(cellnum++);
					if(obj instanceof String)
					{
						if(((String)obj).length() < 32767)
						{
							if (((String) obj).contentEquals("Pass"))
							{
								CellStyle style = workbook.createCellStyle();
								style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
								style.setFillPattern(CellStyle.SOLID_FOREGROUND);
								Font font = workbook.createFont();
								font.setColor(IndexedColors.GREEN.getIndex());
								style.setFont(font);
								cell.setCellValue((String)obj);
								cell.setCellStyle(style);
							}
							else if (((String) obj).contentEquals("Fail"))
							{
								CellStyle style = workbook.createCellStyle();
								style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
								style.setFillPattern(CellStyle.SOLID_FOREGROUND);
								Font font = workbook.createFont();
								font.setColor(IndexedColors.RED.getIndex());
								style.setFont(font);
								cell.setCellValue((String)obj);
								cell.setCellStyle(style);
							}
							else
								cell.setCellValue((String)obj);
						}
						else
						{
							File jsonFile = new File(BaseTest.strFolderPath+"/"+strSheetName+"_"+BaseTest.strDetailedTestLogs.substring(39, (BaseTest.strDetailedTestLogs.length()-5))+"_"+String.valueOf(intLongJSONResponse)+".json");
							//File jsonFile = new File("TestResults/15-07-2016"+"/"+"abcd"+"_"+String.valueOf(1)+".json");
							FileOutputStream is = new FileOutputStream(jsonFile);
							OutputStreamWriter osw = new OutputStreamWriter(is);    
							Writer w = new BufferedWriter(osw);
							w.write(GeneralUtils.prettyprintJsonResponse((String)obj));
							w.close();
							cell.setCellValue("Json response is very long. Please see the response in the file \n"+strSheetName+"_"+BaseTest.strDetailedTestLogs.substring(39, (BaseTest.strDetailedTestLogs.length()-5))+"_"+String.valueOf(intLongJSONResponse)+".json"+", \npresent in "+BaseTest.strFolderPath+" folder");
						}
					}
					else if(obj instanceof Integer)
						cell.setCellValue((Integer)obj);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try
		{
			//Write the workbook in file system
			//FileOutputStream out = new FileOutputStream(new File("howtodoinjava_demo.xlsx"));
			FileOutputStream out = new FileOutputStream(strExcelPath);
			workbook.write(out);
			out.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public static int findRow(String strExcelPath, String strSheetName, String cellContent)
	{
		FileInputStream fis;
		try {
			fis = new FileInputStream(strExcelPath);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet sheet = wb.getSheet(strSheetName);
			for (Row row : sheet) 
			{
				for (Cell cell : row) 
				{
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) 
					{
						if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) 
						{
							return row.getRowNum();  
						}
					}
				}
			}               
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}


	public static int getColumnCount(String strExcelPath, String strSheetName)
	{
		int intColumnCount = 0;
		try {
			FileInputStream fis = new FileInputStream(strExcelPath);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheet(strSheetName);
			Row  r = s.getRow(0);
			intColumnCount = r.getLastCellNum();
			//System.out.println(intColumnCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return intColumnCount;

	}

	public static void main(String[] args)
	{

		//int i = ExcelReaderWriter.findRow(Configs.strExecutionControllerPath, "WeathermapAPI", "com.testscripts.WeatherByCityName");
		//System.out.println(i);
		//ExcelReaderWriter.setCellValue(Configs.strExecutionControllerPath, "WeathermapAPI", ExcelReaderWriter.findRow(Configs.strExecutionControllerPath, "WeathermapAPI", "com.testscripts.WeatherByCityName"), 3, "Pass");
	}
}
