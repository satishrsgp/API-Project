package com.endpoints;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.http.HttpStatus;
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
 * Class Name						: Master_Practice
 * Created By						: Anil Kumar Veluru
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: None
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch details of all Practices
 * ******************************************************************
 */

public class Master_Practice extends BaseTest_obsl
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void master_Practices()
	{
		//Extracting the simple name of the class and assigning it to string.
		String strClassName = getClass().getSimpleName();

		//we print and log the Thread ID with which the Test gets executed
		System.out.println("Running test case " + strClassName + " with Thread Id:- " + Thread.currentThread().getId());
		Log.info("Running test case " + strClassName + " with Thread Id:- " + Thread.currentThread().getId());

		//we set the initial status of test case execution to false
		boolean boolTestcaseStatus = true;

		//Creating the Hash map in which we store the execution results, which can be used for updating detailed execution results
		HashMap<String, Object[]> hashMapResult = new HashMap<>();

		//adding the initial headers to the Hash Map
		hashMapResult.put("0", new Object[] {"Parameters","RequestURI", "RequestHeaders","ResponseCode","ResponseHeaders", "Response", "Status"});

		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParameters = new HashMap<String, Object>();
		mapRequestParameters.put("strRequestType", "GET");
		mapRequestParameters.put("strPayLoad", "");
		mapRequestParameters.put("strQueryString", "");
		mapRequestParameters.put("strURI", BaseTest_obsl.mapURLRepo.get(strClassName));

		//creating an instance or object of RequestResponseHandler
		RequestResponseHandler objHandler = new RequestResponseHandler();
		try
		{
			//Sending the Request to get the Response
			objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParameters);

			//check that object handler is not null to continue with validations
			if (objHandler != null)
			{
				//fetch the actual response from object handler and assigning to a string
				String strResponse = objHandler.getStrResponse();

				//validating the response code, for master routes the status code we expect is always 200
				ValidateResults.validateStatusCode(objHandler, 200);

				//validating whether the request got successfully passed then we will validate the response else we needn't
				if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
				{
					//validating that the response is not empty
					if (!strResponse.isEmpty())
					{
						/*if (!(strResponse.contains("is not valid for Guid")||strResponse.contains("Date parameter is in an incorrect format.")))
						{}
						else
						{
							//System.out.println("The API Call executed is : \n" + mapRequestParrameters.get("strURI") + "\n");
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							//System.out.println("It's a Bad Request \n");
							hashMapResult.put("1", new Object[]{" " ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}*/


						String strQuery = "Select top 25 practice_id, enterprise_id, practice_name, delete_ind from practice Order by 3";
						try
						{
							Object obj = JsonPath.read(strResponse, "$..id");
							ArrayList<Object> jsonList = new ArrayList<>();       
							if (obj instanceof JSONArray)
							{
								for(int j = 0; j< ((JSONArray) obj).size(); j++)
								{
									jsonList.add((Object) JsonPath.read(strResponse,"$..id["+j+"]"));
									jsonList.add((Object) JsonPath.read(strResponse,"$..enterpriseId["+j+"]"));
									if(Objects.equals(JsonPath.read(strResponse,"$..name["+j+"]"), null))
										jsonList.add(null);
									else
										jsonList.add(JsonPath.read(strResponse,"$..name["+j+"]").toString());
									if(Objects.equals(JsonPath.read(strResponse,"$..isDeleted["+j+"]").toString(), "false"))
										jsonList.add("N");
									else if(Objects.equals(JsonPath.read(strResponse,"$..isDeleted["+j+"]").toString(), "true"))
										jsonList.add("Y");
									else
										jsonList.add(null);
								}
							}
							System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
							System.out.println("The JSON Response in a List: \n" + jsonList + "\n");

							ResultSet rs = DatabaseConnection.statementExecution(connNGA, strQuery);
							int intNumberOfRows = DatabaseConnection.returnNumberOfRows(rs);
							
							//Check the number of rows and if it is same as the number of ID's in jsonList then only proceed to validation
							if( intNumberOfRows == ((JSONArray) obj).size())
							{
								ValidateResults.resultsvalidation(jsonList, rs, objHandler);
							}
							else
							{
								System.out.println("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
								System.out.println("The JSON List size is :" + ((JSONArray) obj).size() + " and the SQL Result Set Row Count is : " + intNumberOfRows);
								Log.info("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
								objHandler.setStrRequestStatus("Fail");
							}

							//ArrayList<Object> dbList = DatabaseConnection.addResultSettoArrayList(rs);
							//ValidateResults.resultValidation(jsonList, dbList, objHandler);
							//ValidateResults.resultsvalidation(jsonList, rs, objHandler);
						} 
						catch (Exception e)
						{
							e.printStackTrace();
							log.info("Error in Test Class and the detail are : \n" + e.getMessage());
							softAssert.fail("Error in Test Script please look at logs");
							objHandler.setStrRequestStatus("Fail");
						}
						hashMapResult.put("1", new Object[]{" ", objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});

					}
					else
					{
						//System.out.println("The API Call executed is : \n" + mapRequestParrameters.get("strURI") + "\n");
						Log.info("It's a Bad Request");
						Log.info(objHandler.getStrResponse());
						//System.out.println("It's a Bad Request \n");
						hashMapResult.put("1", new Object[]{" " ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
					}
				}

				else if(! (objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND))
				{
					//System.out.println("The API Call executed is : \n" + mapRequestParrameters.get("strURI") + "\n");
					Log.info("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + HttpStatus.SC_OK);
					softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + HttpStatus.SC_OK + " ");

					//Log.info("It's a Bad Request");
					Log.info(objHandler.getStrResponse());
					//System.out.println("We have received the Status Code " + objHandler.getIntResponseCode() + "where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(Configs.TestDataWorkBook, getClass().getSimpleName(), k, 2) + "\n");
					//hashMapResult.put("0", new Object[] {"Parameters","RequestURI", "RequestHeaders","ResponseCode","ResponseHeaders", "Response", "Status"});
					hashMapResult.put("1", new Object[]{"", objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
					objHandler.setStrRequestStatus("Pass");
				}
				else
				{
					//Log.info("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server");
					softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server ");
					hashMapResult.put("1", new Object[]{"", objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
					objHandler.setStrRequestStatus("Fail");
				}


				/*if (objHandler.getStrRequestStatus().contains("Fail"))
				{
					boolTestcaseStatus = false;
					softAssert.fail("Test case fails with data: "+"?personId=" + personId);
				}*/
			}
			else
			{
				boolTestcaseStatus = false;
				//System.out.println("The Request could not be sent as the Request Type is not a valid Http Method");
				log.info("The Request could not be sent as the Request Type is not a valid Http Method");
				softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
				//softAssert.fail("Test case fails with data: "+"?personId=" + personId);
				hashMapResult.put("1", new Object[]{"" ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
			}
			if (objHandler.getStrRequestStatus().contains("Fail"))
			{
				boolTestcaseStatus = false;
				softAssert.fail("Test case fails ");
			}
			objHandler = null;

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		ExcelReaderWriter.writeDetailedTestLogs(BaseTest_obsl.strDetailedTestLogs, getClass().getSimpleName(), hashMapResult);

		if (boolTestcaseStatus)
		{
			ExcelReaderWriter.setCellValue(BaseTest_obsl.mapExecutionConfigs.get("ExecutionControllerPath"), BaseTest_obsl.mapExecutionConfigs.get("RouteRepositorySheet"), ExcelReaderWriter.findRow(BaseTest_obsl.mapExecutionConfigs.get("ExecutionControllerPath"), BaseTest_obsl.mapExecutionConfigs.get("RouteRepositorySheet"), getClass().getSimpleName()), 4, "Pass");
		}
		else
		{
			ExcelReaderWriter.setCellValue(BaseTest_obsl.mapExecutionConfigs.get("ExecutionControllerPath"), BaseTest_obsl.mapExecutionConfigs.get("RouteRepositorySheet"), ExcelReaderWriter.findRow(BaseTest_obsl.mapExecutionConfigs.get("ExecutionControllerPath"), BaseTest_obsl.mapExecutionConfigs.get("RouteRepositorySheet"), getClass().getSimpleName()), 4, "Fail");
			//ExcelReaderWriter.setCellValue(Configs.strExecutionControllerPath, Configs.strRouteRepositorySheet, ExcelReaderWriter.findRow(Configs.strExecutionControllerPath, Configs.strRouteRepositorySheet, "com.testscripts."+getClass().getSimpleName()), 4, "Fail");
		}
		softAssert.assertAll();
	}
}
