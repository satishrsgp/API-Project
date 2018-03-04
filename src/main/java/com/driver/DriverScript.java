/**
 * 
 */
package com.driver;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;

import com.utils.XMLGenerator;


public class DriverScript 
{
	//we create a logger object with the class name
	private static Logger Log = LoggerFactory.getLogger(DriverScript.class.getName());
	
	public static void main(String[] args)
	{
		//Mapped Diagnostic Context
		MDC.put("testname",  "DriverScript");
		DriverScript driverScript = new DriverScript();
		driverScript.executetestFromController();
		MDC.remove("testname");
	}

	public void executetestFromController()
	{
		//Since single sheet we are reading it as parameter, if multiple we have to loop it
		List <XmlSuite> suites = XMLGenerator.generateTestNGXML("./ExecutionController/ExecutionController.xlsx", "Projects");
		
		//To Print on Console
		System.out.println("The TestNG suite to be run is as follows:\n");
		
		//To log it in the log file
		Log.info("The TestNG suite to be run is as follows:\n");
		for(XmlSuite suite: suites)
		{
			System.out.println(suite.toXml());
			Log.info(suite.toXml());
		}

		TestNG tng = new TestNG();
		List<Class<? extends ITestNGListener>> myListenerClasses = new ArrayList<Class<? extends ITestNGListener>>();
		myListenerClasses.add(com.utils.ExtentReporterNG.class);
		tng.setListenerClasses(myListenerClasses);
		tng.setXmlSuites(suites);
		tng.run();
	}
}
