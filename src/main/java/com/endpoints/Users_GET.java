package com.endpoints;

import java.sql.ResultSet;
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
import net.minidev.json.JSONArray;

/*
 * ********************************************************************
 * Class Name						: Users_GET
 * Created By						: Guruprasad
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: 
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the details of all the users configured in EHR
 * ******************************************************************
 */

public class Users_GET extends BaseTest_obsl
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void users_GET()
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

		String strQuery;
		
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
							if(strResponse.contains("/api/users?$skip=25"))
							{
								strQuery =  "select top 25" 
										+" user_id as id,first_name as firstName, last_name as lastName, mi as mi,employee_num as employeeNum,enterprise_id as enterpriseId,practice_id as practiceId,login_id as loginId,"
										+" case when delete_ind='N' then 'false' else 'true' end as isDeleted"
										+" from user_mstr where delete_ind='N' order by first_name";
							}
							else
							{
								strQuery = "select" 
										+" user_id as id,first_name as firstName, last_name as lastName, mi as mi,employee_num as employeeNum,enterprise_id as enterpriseId,practice_id as practiceId,login_id as loginId,"
										+" case when delete_ind='N' then 'false' else 'true' end as isDeleted"
										+" from user_mstr where delete_ind='N'";
							}
							
							try
							{
								Object obj = JsonPath.read(strResponse, "$.items");
								ArrayList<Object> jsonList = new ArrayList<>();
								if (obj instanceof JSONArray)
								{
									for(int j = 0; j< ((JSONArray) obj).size(); j++)
									{
										jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].id"));
										jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].firstName"));
										jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].lastName"));
										jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].mi"));
										jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].employeeNum"));
										jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].enterpriseId"));
										jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].practiceId"));
										jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].loginId"));
										jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].isDeleted").toString());
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
				log.info("The Request could not be sent as the Request Type is not a valid Http Method");
				softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
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
}