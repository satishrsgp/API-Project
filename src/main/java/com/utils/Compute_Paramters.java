package com.utils;

import java.util.HashMap;
import java.util.Map;

import com.testscripts.BaseTest;

public class Compute_Paramters 
{
	public static HashMap<String, String> mapperCompute_Parameters(int rowNumber, String strSimpleClassName)
	{
		HashMap<String, String> parameters = new HashMap<String,String>();
		
		for(int i=0; i< ExcelReaderWriter.getColumnCount(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strSimpleClassName)-2; i++)
		{
			parameters.put(ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strSimpleClassName, 0, i), ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strSimpleClassName, rowNumber, i));
			if(parameters.get(ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strSimpleClassName, 0, i)).equalsIgnoreCase("null"))
			{
				parameters.put(ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strSimpleClassName, 0, i), "");
			}

			else if (parameters.get(ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strSimpleClassName, 0, i)).equalsIgnoreCase("$$"))
			{
				parameters.put(ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strSimpleClassName, 0, i), "null");
			}			

			// 09/18/2017 -- added for the script Chart_UpdateOrderedVaccine_TC1 - Satish added
			else if (parameters.get(ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strSimpleClassName, 0, i)).equalsIgnoreCase("$$"))
			{
				parameters.put(ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strSimpleClassName, 0, i), "null");
			}

		}
		return parameters;
	}
	
	
	public static String formulateFinalURI(String strSimpleClassName, HashMap<String, String> parameters)
	{
		String strFinalURI = BaseTest.mapURLRepo.get(strSimpleClassName);
		
		for(Map.Entry<String, String> entry : parameters.entrySet())
		{
			strFinalURI = strFinalURI.replace(entry.getKey(), entry.getValue());
		}
		return strFinalURI;
	}
}
