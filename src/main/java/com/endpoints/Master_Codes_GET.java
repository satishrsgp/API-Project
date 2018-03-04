package com.endpoints;

import java.util.HashMap;
import org.testng.asserts.SoftAssert;

/*
 * ********************************************************************
 * Class Name						: Master_Codes_GET
 * Created By						: Guruprasad
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: 
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the master codes that have been setup in EHR
 * ******************************************************************
 */

public class Master_Codes_GET
{
	public static Boolean master_Codes_GET_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		return true;
	}
}

/*public class Master_Codes_GET extends BaseTest_obsl
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void master_Codes_GET()
	{
		String strClassName = getClass().getSimpleName();
		System.out.println("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		Log.info("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		boolean boolTestcaseStatus = true;
		HashMap<String, Object[]> hashMapResult = new HashMap<>();
		hashMapResult.put("0", new Object[] {"Parameters","RequestURI", "RequestHeaders","ResponseCode","ResponseHeaders", "Response", "Status"});
		Map<String, Object> mapRequestParameters = new HashMap<String, Object>();
		mapRequestParameters.put("strRequestType", "GET");
		mapRequestParameters.put("strPayLoad", "");
		mapRequestParameters.put("strQueryString", "");
		mapRequestParameters.put("strURI", BaseTest_obsl.mapURLRepo.get(strClassName));

		RequestResponseHandler objHandler = new RequestResponseHandler();
		try
		{
			objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParameters);

			if (objHandler != null)
			{
				String strResponse = objHandler.getStrResponse();
				ValidateResults.validateStatusCode(objHandler, 200);
				if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
				{
					if (!strResponse.isEmpty())
					{
						if (!(strResponse.contains("The request is invalid")))
						{
							String strQuery = "select distinct code_type from code_tables order by code_type";
							try
							{
								Object obj = JsonPath.read(strResponse, "$");
								ArrayList<Object> jsonList = new ArrayList<>();       
								if (obj instanceof JSONArray)
								{
									for(int j = 0; j< ((JSONArray) obj).size(); j++)
									{
										jsonList.add(JsonPath.read(strResponse,"$.["+j+"]"));
									}
								}
								System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI").toString() + "\n");
								System.out.println("The JSON Response in a List: \n" + jsonList + "\n");

								ResultSet rs = DatabaseConnection.statementExecution(connNGA, strQuery);
								int intNumberOfRows = DatabaseConnection.returnNumberOfRows(rs);
								//Check the number of rows and if it is same as the number of ID's in jsonList then only proceed to validation
								if( intNumberOfRows == ((JSONArray)obj).size())
								{
									ValidateResults.resultsvalidation(jsonList, rs, objHandler);
								}
								else
								{
									System.out.println("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
									System.out.println("The JSON List size is :1 and the SQL Result Set Row Count is : " + intNumberOfRows);
									Log.info("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
									objHandler.setStrRequestStatus("Fail");
								}
							} 
							catch (Exception e)
							{
								e.printStackTrace();
                                log.info("Error in Test Class and the detail are : \n" + e.getMessage());
                                softAssert.fail("Error in Test Script please look at logs");
                                objHandler.setStrRequestStatus("Fail");
							}
							hashMapResult.put("1", new Object[]{"" ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						}
						 //if the request is treated as invalid
						else
						{
							System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							System.out.println("It's a Bad Request \n");
							hashMapResult.put("1", new Object[]{"" ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}
					 //if response is empty but expected and actual response codes are same
					else
					{
						System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
						Log.info("It's a Bad Request");
						Log.info(objHandler.getStrResponse());
						System.out.println("It's a Bad Request \n");
						hashMapResult.put("1", new Object[]{"" ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
					}
				}
				 //if actual status code is not equal to expected status code
				else
				{
					System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
					Log.info("It's a Bad Request");
					Log.info(objHandler.getStrResponse());
					System.out.println("It's a Bad Request \n");
					hashMapResult.put("1", new Object[]{"" ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
				}
			}
			//if objHandler is null meaning bad type of request method
			else
			{
				System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
				Log.info("It's a Bad Request");
				Log.info(objHandler.getStrResponse());
				System.out.println("It's a Bad Request \n");
				hashMapResult.put("1", new Object[]{"" ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
			}

			if (objHandler.getStrRequestStatus().contains("Fail"))
			{
				boolTestcaseStatus = false;
				softAssert.fail("Test case fails");
			}
			objHandler = null;

		}
		catch(Exception e)
		{
			e.printStackTrace();
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
}*/