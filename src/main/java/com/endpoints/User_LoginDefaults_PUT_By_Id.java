package com.endpoints;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.utils.API_Request_RetObj;
import com.utils.DatabaseConnection;
import com.utils.ExcelReaderWriter;
import com.utils.Log;
import com.utils.RequestResponseHandler;
import com.utils.ValidateResults;

/*
 * ********************************************************************
 * Class Name						: User_LoginDefaults_PUT_By_Id
 * Created By						: Guruprasad
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: userId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will set the login defaults for a user based on the userId
 * ******************************************************************
 */

public class User_LoginDefaults_PUT_By_Id extends BaseTest_obsl
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void user_LoginDefaults_PUT_By_Id()
	{
		String strClassName = getClass().getSimpleName();
		System.out.println("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		Log.info("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		boolean boolTestcaseStatus = true;
		HashMap<String, Object[]> hashMapResult = new HashMap<>();
		Map<String, Object> mapRequestParameters = new HashMap<String, Object>();
		mapRequestParameters.put("strRequestType", "PUT");
		mapRequestParameters.put("strQueryString", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();
		try
		{
			String strPayload = new String(Files.readAllBytes(Paths.get("./TestData/User_LoginDefaults_PUT_By_Id.json")));
			for(int k=1; k<=ExcelReaderWriter.getRowCount(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{
				String userId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 0);
				if (userId.equalsIgnoreCase("null"))
					userId="";
				
				String enterpriseId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 1);
				if (enterpriseId.equalsIgnoreCase("null"))
					enterpriseId="";
				
				String practiceId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2);
				if (practiceId.equalsIgnoreCase("null"))
					practiceId="";
				
				String locationId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 3);
				if (locationId.equalsIgnoreCase("null"))
					locationId="00000000-0000-0000-0000-000000000000";
				
				String providerId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 4);
				if (providerId.equalsIgnoreCase("null"))
					providerId="00000000-0000-0000-0000-000000000000";
				
				String timeZone = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 5);
				if (timeZone.equalsIgnoreCase("null"))
					timeZone="";
				
				mapRequestParameters.put("strPayLoad", strPayload
						.replace("#id#", userId)
						.replace("#enterpriseId#", enterpriseId)
						.replace("#practiceId#", practiceId)
						.replace("#locationId#", locationId)
						.replace("#providerId#", providerId)
						.replace("#timeZone#", timeZone)); 

				mapRequestParameters.put("strURI", BaseTest_obsl.mapURLRepo.get(strClassName).replace("userId", userId));
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParameters);

				if (objHandler != null)
				{
					String strResponse = objHandler.getStrResponse();
					ValidateResults.validateStatusCode(objHandler, ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 7));
					if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
					{
						if (strResponse.isEmpty())
						{
							Object objSQL = DatabaseConnection.fetchColumnAsObject(connNGA, "select item_value from user_pref where item_name like '%Api Defaults%' and user_id='"+userId+"'", "item_value");
							ArrayList<Object> dbList = new ArrayList<Object>();
							dbList.add(JsonPath.read(objSQL.toString(), "$.enterprise_id"));
							dbList.add(JsonPath.read(objSQL.toString(), "$.practice_id"));
							dbList.add(JsonPath.read(objSQL.toString(), "$.location_id").toString().toUpperCase());
							dbList.add(JsonPath.read(objSQL.toString(), "$.provider_id").toString().toUpperCase());
							dbList.add(JsonPath.read(objSQL.toString(), "$.timezone"));
							
							ArrayList<Object> jsonList = new ArrayList<Object>();
							jsonList.add(enterpriseId);
							jsonList.add(practiceId);
							jsonList.add(locationId.toUpperCase());
							jsonList.add(providerId.toUpperCase());
							jsonList.add(timeZone);
							
							//Check the number of rows and if it is same as the number of ID's in jsonList then only proceed to validation
							Map<String, ArrayList<Object>> mList = new HashMap<String, ArrayList<Object>>();
							mList.put("jsonList", jsonList);
							mList.put("dbList", dbList);
							
							ValidateResults.resultsvalidation(mList, objHandler);
							hashMapResult.put(String.valueOf(k), new Object[]{"userId=" + userId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), "There is no response", objHandler.getStrRequestStatus()});
						}
						//if response is not empty. Here we expect an empty response
						else
						{
							System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							System.out.println("It's a Bad Request \n");
							hashMapResult.put(String.valueOf(k), new Object[]{"userId=" + userId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}
					//if actual status code is not equal to expected status code
					else
					{
						System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
						Log.info("It's a Bad Request");
						Log.info(objHandler.getStrResponse());
						System.out.println("It's a Bad Request \n");
						hashMapResult.put(String.valueOf(k), new Object[]{"userId=" + userId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
					}
				}
				//if objHandler is null meaning bad type of request method
				else
				{
					System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
					Log.info("It's a Bad Request");
					Log.info(objHandler.getStrResponse());
					System.out.println("It's a Bad Request \n");
					hashMapResult.put(String.valueOf(k), new Object[]{"userId=" + userId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
				}
				if (objHandler.getStrRequestStatus().contains("Fail"))
				{
					boolTestcaseStatus = false;
					softAssert.fail("Test case fails with data: "+"userId=" + userId);
				}
				objHandler = null;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
            log.info("Error in Test Class and the detail are : \n" + e.getMessage());
            softAssert.fail("Error in Test Script please look at logs");
            objHandler.setStrRequestStatus("Fail");
		}

		ExcelReaderWriter.writeDetailedTestLogs(BaseTest_obsl.strDetailedTestLogs, strClassName, hashMapResult);

		if (boolTestcaseStatus)
		{
			ExcelReaderWriter.setCellValue(BaseTest_obsl.mapExecutionConfigs.get("ExecutionControllerPath"), BaseTest_obsl.mapExecutionConfigs.get("RouteRepositorySheet"), ExcelReaderWriter.findRow(BaseTest_obsl.mapExecutionConfigs.get("ExecutionControllerPath"), BaseTest_obsl.mapExecutionConfigs.get("RouteRepositorySheet"), strClassName), 4, "Pass");
		}
		else
		{
			ExcelReaderWriter.setCellValue(BaseTest_obsl.mapExecutionConfigs.get("ExecutionControllerPath"), BaseTest_obsl.mapExecutionConfigs.get("RouteRepositorySheet"), ExcelReaderWriter.findRow(BaseTest_obsl.mapExecutionConfigs.get("ExecutionControllerPath"), BaseTest_obsl.mapExecutionConfigs.get("RouteRepositorySheet"), strClassName), 4, "Fail");
		}
		softAssert.assertAll();
	}
}