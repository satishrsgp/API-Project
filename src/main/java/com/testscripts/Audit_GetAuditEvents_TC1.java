package com.testscripts;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.endpoints.Audit_GetAuditEvents;
import com.google.common.base.Objects;
import com.utils.API_Request_RetObj;
import com.utils.Compute_Paramters;
import com.utils.ExcelReaderWriter;
import com.utils.GeneralUtils;
import com.utils.Log;
import com.utils.RequestParameters;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.TestsEvaluation;


public class Audit_GetAuditEvents_TC1 extends BaseTest
{

	SoftAssert softAssert = new SoftAssert();
	
	@Test
	public void audit_GetAuditEvents_TC1()
	{
		boolean boolTestcaseStatus = true, boolTestDataStatus = true;
		
		String strClassName = getClass().getSimpleName();
		String strSimpleClassName = Log.intializeLoggin(strClassName);
		String strTestDataSheetName = GeneralUtils.sheetName(strSimpleClassName);

		//Creating a new HashMap to store all the Request and Response Details
		HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();

		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParrameters = RequestParameters.generateRequestParamsHashMap("GET", "", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			//Iterating through the Test DataSheet to run the testscript
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strTestDataSheetName); k++)
			{	
				
				HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strTestDataSheetName);
				mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strSimpleClassName, parameters));

				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);
				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName, strTestDataSheetName, softAssert, parameters);
				
				
				Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));
				if(Objects.equal(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass"))
				{
					String strResponse = objHandler.getStrResponse();
					boolTestDataStatus = Audit_GetAuditEvents.audit_GetAuditEvents_Test(parameters, strResponse, "id", softAssert);
				}
				else
				{
					boolTestDataStatus = false;
				}
					boolTestcaseStatus = boolTestcaseStatus && boolTestDataStatus;

                //Update the pass/fail and update the hashMapResult
                hashMapResult = TestsEvaluation.updateHashMap(hashMapResult, k, boolTestDataStatus);
                objHandler = TestsEvaluation.updateStatus(objHandler, softAssert, boolTestDataStatus, parameters);
                }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		//Preparing the TestResults Excel Sheet and updating with all the Request and Response Details and also upadate pass fail status in exc controller.
		TestResults.writeResults(strClassName, hashMapResult, boolTestcaseStatus);

		softAssert.assertAll();
	}
}
