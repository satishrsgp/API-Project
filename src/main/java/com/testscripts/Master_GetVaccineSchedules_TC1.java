package com.testscripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.endpoints.Master_GetVaccineSchedules;
import com.google.common.base.Objects;
import com.utils.API_Request_RetObj;
import com.utils.Compute_Paramters;
import com.utils.GeneralUtils;
import com.utils.Log;
import com.utils.RequestParameters;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.TestsEvaluation;

public class Master_GetVaccineSchedules_TC1 extends BaseTest
{
	SoftAssert softAssert = new SoftAssert();

	@Test
	public void master_GetVaccineSchedules_TC1()
	{
		boolean boolTestcaseStatus = true;
		boolean boolTestDataStatus = true;

		String strClassName = getClass().getSimpleName();
		String strSimpleClassName = Log.intializeLoggin(strClassName);
		String strTestDataSheetName = GeneralUtils.sheetName(strSimpleClassName);

		//Creating a new HashMap to store all the Request and Response Details
		HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();

		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParameters = RequestParameters.generateRequestParamsHashMap("GET", "", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(1,strTestDataSheetName);
			mapRequestParameters.put("strURI", Compute_Paramters.formulateFinalURI(strSimpleClassName, parameters));

			//Sending the Request to get the Response
			objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParameters);

			hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName,strTestDataSheetName, softAssert, parameters);

			Object[] testexecutionResult = hashMapResult.get(String.valueOf(1));
			if(Objects.equal(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass"))
			{
				String strResponse = objHandler.getStrResponse();
				boolTestDataStatus = Master_GetVaccineSchedules.master_GetVaccineSchedules_Test(strResponse, "startAge", softAssert);
			}
			else
			{
				boolTestDataStatus = false;
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