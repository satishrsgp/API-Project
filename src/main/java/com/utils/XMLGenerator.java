package com.utils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.ParallelMode;
import org.testng.xml.XmlTest;

public class XMLGenerator {
	
	ArrayList<String> myTestSuites = new ArrayList<String>();
	
	public static List<XmlSuite> generateTestNGXML(String  strExcelPath, String strSheetName)
	{
		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		try
		{
			FileInputStream fis = new FileInputStream(strExcelPath);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet ws = wb.getSheet(strSheetName);
			//get the Project Count
			int rowcount = ws.getLastRowNum();
			
			for (int i=1; i<= rowcount; i++)
			{
				if((ws.getRow(i).getCell(1).toString()).equalsIgnoreCase("Yes"))
				{
					System.setProperty("ProjectName", ws.getRow(i).getCell(0).toString());
					
					XmlSuite suite = new XmlSuite();	//Create an object of the XmlSuite class which describes the tag <suite> in testng.xml. 
					suite.setName(System.getProperty("ProjectName") + "_Suite");			//Give a name to the TestSuite we are executing
					
					//to set testng to run tests parallel
					suite.setParallel(ParallelMode.CLASSES);
					suite.setThreadCount(6);						//To set maximum of 5 threads for the execution parallel
					
					XmlTest test = new XmlTest(suite);	//Create an object of the XmlTest class which describes the tag <test> in testng.xml.
					test.setName(System.getProperty("ProjectName") + "_Test");			//Give a name to the Test we are executing
					
					ArrayList<String> myTestClasses = new ArrayList<String>();
					
					//myTestClasses = null;
					Sheet projectWorkSheet = wb.getSheet((ws.getRow(i).getCell(0).toString()));
					
					int testsCount = projectWorkSheet.getLastRowNum();
					
					for (int j=1; j<= testsCount; j++)
					{
						if((projectWorkSheet.getRow(j).getCell(3).toString()).equalsIgnoreCase("Yes"))
						{
							myTestClasses.add("com.testscripts." + projectWorkSheet.getRow(j).getCell(1).toString());
						}
					}
					List<XmlClass> classes = new ArrayList<XmlClass>();
					
					//We are reading the classes that needs to be executed from the execution controller excel
					Iterator<String> ite = myTestClasses.iterator();
					
					while(ite.hasNext())
					{
						String num=ite.next();
						classes.add(new XmlClass(num));	// adding all the class names to the List classes
					}
					
					test.setXmlClasses(classes) ;
					suites.add(suite);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return suites;
	}
	
	/*public static void main(String [] agrs)
	{
		List <XmlSuite> suites = Test.generateTestNGXML(Configs.strExecutionControllerPath, Configs.strExecutionControllerSheet);
		System.out.println(suites);
	}*/
}
