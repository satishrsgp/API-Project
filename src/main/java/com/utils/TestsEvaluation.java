package com.utils;

import java.util.HashMap;

import org.apache.http.HttpStatus;
import org.testng.asserts.SoftAssert;

import com.google.common.base.Objects;
import com.testscripts.BaseTest;

public class TestsEvaluation 
{
	@SuppressWarnings("null")
	public static HashMap<String, Object[]> mapperExecuteTests(HashMap<String, Object[]> hashMapResult, RequestResponseHandler objHandler, String strSimpleClassName, String strTestDataSheetName, SoftAssert softAssert, HashMap<String, String> parameters)
	{		
		try
		{
			if (objHandler != null)
			{
				String strResponse = objHandler.getStrResponse();

				//Have added this code specially handle the Patient Search Test Data sheet Objects.equal(System.getProperty("strQueryString"), null) || System.getProperty("strQueryString").isEmpty()
				if(!strSimpleClassName.equals("Persons_GetPersonLookup"))
				{
					ValidateResults.validateStatusCode(objHandler, ExcelReaderWriter.getIntCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strTestDataSheetName, hashMapResult.size(), parameters.size()));
				}
				else
				{
					ValidateResults.validateStatusCode(objHandler, ExcelReaderWriter.getIntCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strTestDataSheetName, hashMapResult.size()+1, parameters.size()));
				}
				//ValidateResults.validateStatusCode(objHandler, ExcelReaderWriter.getIntCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strTestDataSheetName, hashMapResult.size(), parameters.size()));
				if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
				{
					//Handle Schema Validations
					//added the and condition to handle the post request
					if(objHandler.getIntResponseCode() == 200 && Objects.equal(objHandler.getStrRequestType(), "GET") && (!strSimpleClassName.equals("Master_GetCodeTypes")))
						ValidateSchema.validateSchema(objHandler.getStrResponse(), "./src/main/java/Resources/APIv2.0_JSONSchema/" + BaseTest.mapJSONSchema.get(strSimpleClassName));
					if (!strResponse.isEmpty() || !Objects.equal(objHandler.getStrRequestType(), "GET"))
					{
						if (!(strResponse.contains("is not valid for Guid")||strResponse.contains("Date parameter is in an incorrect format.")))
						{
							hashMapResult.put(String.valueOf(hashMapResult.size()), new Object[]{parameters.toString() ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						}
						else
						{
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							hashMapResult.put(String.valueOf(hashMapResult.size()), new Object[]{parameters.toString() ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}
					else
					{
						Log.info("It's a Bad Request");
						Log.info(objHandler.getStrResponse());
						hashMapResult.put(String.valueOf(hashMapResult.size()), new Object[]{parameters.toString() ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
					}
				}

				else if(! (objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND))
				{
					Log.info("We have recieved the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strSimpleClassName, hashMapResult.size(), 2));
					softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strSimpleClassName, hashMapResult.size(), 2) + " ");
					Log.info(objHandler.getStrResponse());
					hashMapResult.put(String.valueOf(hashMapResult.size()), new Object[]{parameters.toString() ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
					objHandler.setStrRequestStatus("Pass");
				}
				else
				{
					softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server ");
					hashMapResult.put(String.valueOf(hashMapResult.size()), new Object[]{parameters.toString() ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
					objHandler.setStrRequestStatus("Fail");
				}
			}
			else
			{
				//boolTestcaseStatus = false;
				objHandler.setStrRequestStatus("Fail");
				Log.info("The Request could not be sent as the Request Type is not a valid Http Method");
				softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
				hashMapResult.put(String.valueOf(hashMapResult.size()), new Object[]{parameters.toString() ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return hashMapResult;
		
	}
	
	public static RequestResponseHandler updateStatus(RequestResponseHandler objHandler,SoftAssert softAssert, Boolean boolTestcaseStatus, HashMap<String, String> parameters)
	{
		objHandler.setStrRequestStatus(boolTestcaseStatus ? "Pass" : "Fail");
		
		
		if (objHandler.getStrRequestStatus().contains("Fail"))
		{
			boolTestcaseStatus = false;
			softAssert.fail("Test case fails with data: "+ parameters);
		}
		return objHandler;
	}
	
	
	public static HashMap<String, Object[]> updateHashMap(HashMap<String, Object[]> hashMapResult,Integer k,boolean boolTestcaseStatus)
    {
        Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));
        if(!boolTestcaseStatus)
        {
            testexecutionResult[testexecutionResult.length-1] = "Fail";
            hashMapResult.put(String.valueOf(k), testexecutionResult);
        }
        return hashMapResult;
    }
}
