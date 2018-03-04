package com.testscripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.endpoints.Allergy_Get_Chart;
import com.endpoints.Chart_Persons_GET;
import com.google.common.base.Objects;
import com.jayway.jsonpath.JsonPath;
import com.utils.API_Request_RetObj;
import com.utils.Compute_Paramters;
import com.utils.ExcelReaderWriter;
import com.utils.GeneralUtils;
import com.utils.Log;
import com.utils.RequestParameters;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.TestsEvaluation;

public class Chart_GetChart_TC3 extends BaseTest
{
	/*Odata not validated so not tested in V2.0 execution */
	SoftAssert softAssert = new SoftAssert();

	@Test
	public void chart_GetChart_TC3()
	{
		String strClassName = getClass().getSimpleName();
		String strSimpleClassName = Log.intializeLoggin(strClassName);
		String strTestDataSheetName = GeneralUtils.sheetName(strSimpleClassName);
		
		boolean boolTestcaseStatus = true, boolTestDataStatus = true;
		
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
				mapRequestParrameters.put("strQueryString", "?$expand=Allergies");

				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);

				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName, strTestDataSheetName, softAssert, parameters);

				Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));
				if(!objHandler.getStrResponse().isEmpty())
				{
					if(Objects.equal(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass"))
					{
						//we are evaluating the chart person details here
						String strResponse = objHandler.getStrResponse();
						boolTestcaseStatus = Chart_Persons_GET.chart_Person_GET_Test(parameters, strResponse, "medicalRecordNumber", softAssert);

						//we are evaluating the allergy property details here
						if(boolTestcaseStatus)
						{
							String strResponseAllergyPart = JsonPath.read(strResponse, "$..allergies").toString();
							if(strResponseAllergyPart.startsWith("[") && strResponseAllergyPart.endsWith("]"))
								strResponseAllergyPart = strResponseAllergyPart.substring(1, strResponseAllergyPart.length()-1);
							boolTestDataStatus = Allergy_Get_Chart.Allergy_Get_Chart_Test(parameters, strResponseAllergyPart, "id", softAssert);
						}
					}
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

		//Preparing the TestResults Excel Sheet and updating with all the Request and Response Details.
		TestResults.writeResults(strClassName, hashMapResult, boolTestcaseStatus);

		softAssert.assertAll();
	}
}
