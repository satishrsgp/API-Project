package com.testscripts;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.utils.API_Request_RetObj;
import com.utils.ComputePayLoadDetails;
import com.utils.Compute_Paramters;
import com.utils.DatabaseConnection;
import com.utils.ExcelReaderWriter;
import com.utils.Log;
import com.utils.RequestParameters;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.TestsEvaluation;

public class Chart_UpdateVisHistory_TC1 extends BaseTest
{

	SoftAssert softAssert = new SoftAssert();

	@Test
	public void chart_UpdateVisHistory_TC1()
	{
		boolean boolTestcaseStatus = true, boolTestDataStatus = true;

		String strClassName = getClass().getSimpleName();
		String strSimpleClassName = Log.intializeLoggin(strClassName);
		//String strTestDataSheetName = GeneralUtils.sheetName(strSimpleClassName);

		//Creating a new HashMap to store all the Request and Response Details
		HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();

		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParrameters = RequestParameters.generateRequestParamsHashMap("PUT", "", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			//Iterating through the Test DataSheet to run the testscript
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{

				HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strClassName);
				mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strClassName, parameters));

				
				String publish_date  = "select * from imm_vaccine_vis_hist where vaccine_vis_id = '"+parameters.get("visId")+"' and  order_vaccine_id = '"+parameters.get("vaccineId") +"'";


				HashMap<String, Object> mapPutDetails = ComputePayLoadDetails.computePUTPayloadMap(parameters, strClassName);
				mapRequestParrameters.put("strPayLoad", mapPutDetails.get("PayLoad"));

				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);

				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName, strClassName, softAssert, parameters);

				Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));
				if(Objects.equals(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass"))
				{
					if(objHandler.getIntResponseCode() == 200)
				{		
				if(DatabaseConnection.fetchColumnAsObject(connNGA, publish_date, "vis_publish_date").toString().contains(parameters.get("visPublishDate").replace("T", "").replace("Z", "")))
				{
						boolTestDataStatus = true;
						log.info("vis_publish_date is updated for the : " + "Vaccine_vis_id = '"+parameters.get("visId"));
				}
				else
				{
					boolTestDataStatus = false;
					log.info("vis_publish_date is not updated for the : " + "Vaccine_vis_id = '"+parameters.get("visId"));

				}
				}
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

		//Preparing the TestResults Excel Sheet and updating with all the Request and Response Details.
		TestResults.writeResults(strClassName, hashMapResult, boolTestcaseStatus);

		softAssert.assertAll();
	}
}
