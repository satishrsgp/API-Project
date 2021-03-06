package com.testscripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.endpoints.Chart_Persons_GET;
import com.endpoints.Chart_Persons_SupportRoles_GET;
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

public class Chart_GetChart_TC19 extends BaseTest
{
	/*Odata not validated so not tested in V2.0 execution */
	SoftAssert softAssert = new SoftAssert();

	@Test
	public void chart_GetChart_Tc19()
	{
		String strClassName = getClass().getSimpleName();
		String strSimpleClassName = Log.intializeLoggin(strClassName);
		String strTestDataSheetName = GeneralUtils.sheetName(strSimpleClassName);
		boolean boolTestcaseStatus = true;

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
				mapRequestParrameters.put("strQueryString", "?$expand=SupportRoles");

				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);

				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName,strTestDataSheetName, softAssert, parameters);

				Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));
				if(!objHandler.getStrResponse().isEmpty())
				{
					if(Objects.equal(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass"))
					{
						//we are evaluating the chart person details here
						String strResponse = objHandler.getStrResponse();
						boolTestcaseStatus = Chart_Persons_GET.chart_Person_GET_Test(parameters, strResponse, "medicalRecordNumber", softAssert);

						//we are evaluating the person support roles details here
						if(boolTestcaseStatus)
						{
							String strResponsePharmaciesPart = JsonPath.read(strResponse, "$..support-roles").toString();
							if(strResponsePharmaciesPart.startsWith("[") && strResponsePharmaciesPart.endsWith("]"))
								strResponsePharmaciesPart = strResponsePharmaciesPart.substring(1, strResponsePharmaciesPart.length()-1);
							boolTestcaseStatus = Chart_Persons_SupportRoles_GET.Chart_Persons_SupportRoles_GET_Test(parameters, strResponsePharmaciesPart, "personId", softAssert);
						}
					}
				}

				objHandler = TestsEvaluation.updateStatus(objHandler, softAssert, boolTestcaseStatus, parameters);
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
