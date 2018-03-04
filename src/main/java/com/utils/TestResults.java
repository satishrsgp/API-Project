package com.utils;

import java.util.HashMap;

import com.testscripts.BaseTest;

public class TestResults 
{
	public static void writeResults(String strClassName, HashMap<String, Object[]> hashMapResult,boolean boolTestcaseStatus)
	{
		try
		{
			ExcelReaderWriter.writeDetailedTestLogs(BaseTest.strDetailedTestLogs, strClassName, hashMapResult);

			if (boolTestcaseStatus)
			{
				ExcelReaderWriter.setCellValue(BaseTest.mapExecutionConfigs.get("ExecutionControllerPath"), BaseTest.mapExecutionConfigs.get("ExcutionControllerSheet"), ExcelReaderWriter.findRow(BaseTest.mapExecutionConfigs.get("ExecutionControllerPath"), BaseTest.mapExecutionConfigs.get("ExcutionControllerSheet"), strClassName), 4, "Pass");
			}
			else
			{
				ExcelReaderWriter.setCellValue(BaseTest.mapExecutionConfigs.get("ExecutionControllerPath"), BaseTest.mapExecutionConfigs.get("ExcutionControllerSheet"), ExcelReaderWriter.findRow(BaseTest.mapExecutionConfigs.get("ExecutionControllerPath"), BaseTest.mapExecutionConfigs.get("ExcutionControllerSheet"), strClassName), 4, "Fail");
			}
			Log.endTestCase(strClassName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static HashMap<String, Object[]> generateHashMapResults()
	{
		HashMap<String, Object[]> hashMapResult = new HashMap<>();
		hashMapResult.put("0", new Object[] {"Parameters","RequestURI", "RequestHeaders","ResponseCode","ResponseHeaders", "Response", "Status"});
		return hashMapResult;
	}
}
