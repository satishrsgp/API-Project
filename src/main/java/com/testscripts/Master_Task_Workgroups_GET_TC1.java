package com.testscripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.endpoints.Master_Task_Workgroups_GET;
import com.google.common.base.Objects;
import com.utils.API_Request_RetObj;
import com.utils.Compute_Paramters;
import com.utils.GeneralUtils;
import com.utils.Log;
import com.utils.RequestParameters;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.TestsEvaluation;

public class Master_Task_Workgroups_GET_TC1 extends BaseTest
{
	SoftAssert softAssert = new SoftAssert();

	@Test
	public void master_Task_Workgroups_GET_TC1()
	{
		String strClassName = getClass().getSimpleName();
		String strSimpleClassName = Log.intializeLoggin(strClassName);
		String strTestDataSheetName = GeneralUtils.sheetName(strSimpleClassName);
		boolean boolTestcaseStatus = true;
		boolean boolTestDataStatus = true;

		//Creating a new HashMap to store all the Request and Response Details
		HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();

		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParrameters = RequestParameters.generateRequestParamsHashMap("GET", "", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(1, strTestDataSheetName);
			mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strSimpleClassName, parameters));
			mapRequestParrameters.put("strQueryString", "");

			//Sending the Request to get the Response
			objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);

			hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName,strTestDataSheetName, softAssert, parameters);

			Object[] testexecutionResult = hashMapResult.get(String.valueOf(1));
			if(!objHandler.getStrResponse().isEmpty())
			{
				if(Objects.equal(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass"))
				{
					//we are evaluating the chart person details here
					String strResponse = objHandler.getStrResponse();
					boolTestDataStatus = Master_Task_Workgroups_GET.master_Task_Workgroups_GET_Test(parameters, strResponse, "id", softAssert);
				}
				else
				{
					boolTestDataStatus = false;
				}
			}
			boolTestcaseStatus = boolTestcaseStatus && boolTestDataStatus;
			//Update the pass/fail and update the hashMapResult
			hashMapResult = TestsEvaluation.updateHashMap(hashMapResult, 1, boolTestDataStatus);
			objHandler = TestsEvaluation.updateStatus(objHandler, softAssert, boolTestDataStatus, parameters);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		//Preparing the TestResults Excel Sheet and updating with all the Request and Response Details.
		TestResults.writeResults(strClassName, hashMapResult, boolTestcaseStatus);

		softAssert.assertAll();
	}
}