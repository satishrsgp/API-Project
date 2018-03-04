package com.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class TestLogHelper
{
	private static final Logger log = LoggerFactory.getLogger(TestLogHelper.class);
	public static final String TEST_NAME = "testname";
	
	public static void startTestCaseLogging(String name)
	{
		try
		{
			MDC.put(TEST_NAME, name);
			log.info("Started logging for the testcase:"+name);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static String stopTestLogging()
	{
		String name = MDC.get(TEST_NAME);
		//System.out.println("Stop logging request has come for class:"+name);
		MDC.remove(TEST_NAME);
		log.info("Stopped logging of info from testcase:"+name);
		return name;
	}
}
