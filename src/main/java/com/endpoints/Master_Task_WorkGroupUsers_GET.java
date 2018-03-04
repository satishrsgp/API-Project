package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

/*
 * ********************************************************************
 * Class Name						: Task_WorkGroupUsers_GET
 * Created By						: Guruprasad
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: 
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the details of all the users who are part of a task workgroup
 * ******************************************************************
 */

public class Master_Task_WorkGroupUsers_GET
{
	public static Boolean master_Task_WorkGroupUsers_GET_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strQuery = null;
			if(strResponse.contains("?$skip=25"))
			{
				strQuery = "select top 25"
						+" P.user_id as userId,G.workgroup_id as workgroupId,X.created_by as createdBy,X.create_timestamp as createTimestamp"
						+" FROM "
						+" task_workgroup_mstr G LEFT OUTER JOIN task_workgroup_user_xref X ON G.workgroup_id = X.workgroup_id" 
						+" LEFT OUTER JOIN user_mstr P ON X.user_id = P.user_id AND P.delete_ind = 'N'" 
						+" WHERE "
						+" G.delete_ind = 'N' AND (G.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"' AND G.practice_id = '"+System.getProperty("LoggedInPracticeId")+"') AND G.workgroup_id = "+parameters.get("workgroupId")+" AND (G.workgroup_type IS NULL OR G.workgroup_type = 1) AND P.user_id is not null" 
						+" ORDER BY X.create_timestamp asc,P.user_id asc";
			}
			else
			{
				strQuery = "select"
						+" P.user_id as userId,G.workgroup_id as workgroupId,X.created_by as createdBy,X.create_timestamp as createTimestamp"
						+" FROM "
						+" task_workgroup_mstr G LEFT OUTER JOIN task_workgroup_user_xref X ON G.workgroup_id = X.workgroup_id" 
						+" LEFT OUTER JOIN user_mstr P ON X.user_id = P.user_id AND P.delete_ind = 'N'" 
						+" WHERE "
						+" G.delete_ind = 'N' AND (G.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"' AND G.practice_id = '"+System.getProperty("LoggedInPracticeId")+"') AND G.workgroup_id = "+parameters.get("workgroupId")+" AND (G.workgroup_type IS NULL OR G.workgroup_type = 1) AND P.user_id is not null" 
						+" ORDER BY X.create_timestamp asc,P.user_id asc";
			}

			String[] jsonMetaData = {"userId","workgroupId","createdBy","createTimestamp"};

			try
			{	
				ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
				Log.info("JSON LIST is :\n" + jsonList);
				status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..userId", jsonList);
			} 
			catch (Exception e)
			{
				e.printStackTrace();
				Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
				softAssert.fail("Error in Test Script please look at logs");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return status;
	}

}

/*public class Task_WorkGroupUsers_GET extends BaseTest
{

	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void task_WorkGroupUsers_GET()
	{
		String strClassName = getClass().getSimpleName();
		System.out.println("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		Log.info("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		boolean boolTestcaseStatus = true;
		HashMap<String, Object[]> hashMapResult = new HashMap<>();
		hashMapResult.put("0", new Object[] {"Parameters","RequestURI", "RequestHeaders","ResponseCode","ResponseHeaders", "Response", "Status"});
		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParrameters = new HashMap<String, Object>();
		mapRequestParrameters.put("strRequestType", "GET");
		mapRequestParrameters.put("strPayLoad", "");
		mapRequestParrameters.put("strQueryString", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();
		try
		{
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{
				String workGroupId = ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 0);
				if (workGroupId.equalsIgnoreCase("null"))
					workGroupId="";

				mapRequestParrameters.put("strURI", BaseTest.mapURLRepo.get(strClassName) .replace("workgroupId", workGroupId));
				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);

				if (objHandler != null)
				{
					String strResponse = objHandler.getStrResponse();
					ValidateResults.validateStatusCode(objHandler, ExcelReaderWriter.getIntCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
					if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
					{
						if (!strResponse.isEmpty())
						{
							if (!(strResponse.contains("is not valid for Guid")||strResponse.contains("Date parameter is in an incorrect format.")))
							{
								String strQuery ="";
								if(strResponse.contains("skip=25"))
								{
									strQuery = "select top 25"
											+" P.user_id as userId,G.workgroup_id as workgroupId,X.created_by as createdBy,X.create_timestamp as createTimestamp"
											+" FROM "
											+" task_workgroup_mstr G LEFT OUTER JOIN task_workgroup_user_xref X ON G.workgroup_id = X.workgroup_id" 
											+" LEFT OUTER JOIN user_mstr P ON X.user_id = P.user_id AND P.delete_ind = 'N'" 
											+" WHERE "
											+" G.delete_ind = 'N' AND (G.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"' AND G.practice_id = '"+System.getProperty("LoggedInPracticeId")+"') AND G.workgroup_id = "+workGroupId+" AND (G.workgroup_type IS NULL OR G.workgroup_type = 1) AND P.user_id is not null" 
											+" ORDER BY X.create_timestamp asc,P.user_id asc";
								}
								else
								{
									strQuery = "select "
											+" P.user_id as userId,G.workgroup_id as workgroupId,X.created_by as createdBy,X.create_timestamp as createTimestamp"
											+" FROM "
											+" task_workgroup_mstr G LEFT OUTER JOIN task_workgroup_user_xref X ON G.workgroup_id = X.workgroup_id" 
											+" LEFT OUTER JOIN user_mstr P ON X.user_id = P.user_id AND P.delete_ind = 'N'" 
											+" WHERE "
											+" G.delete_ind = 'N' AND (G.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"' AND G.practice_id = '"+System.getProperty("LoggedInPracticeId")+"') AND G.workgroup_id = "+workGroupId+" AND (G.workgroup_type IS NULL OR G.workgroup_type = 1) AND P.user_id is not null" 
											+" ORDER BY X.create_timestamp asc,P.user_id asc";
								}
								try
								{
									Object obj = JsonPath.read(strResponse, "$..userId");
									ArrayList<Object> jsonList = new ArrayList<>();       
									if (obj instanceof JSONArray)
									{
										for(int j = 0; j< ((JSONArray) obj).size(); j++)
										{
											jsonList.add(JsonPath.read(strResponse,"$..userId["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..workgroupId["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..createdBy["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..createTimestamp["+j+"]").toString().replace("T", " "));
										}
									}
									System.out.println("The API Call executed is : \n" + mapRequestParrameters.get("strURI") + "\n");
									System.out.println("The JSON Response in a List: \n" + jsonList + "\n");

									ResultSet rs = DatabaseConnection.statementExecution(connNGA, strQuery);
									int intNumberOfRows = DatabaseConnection.returnNumberOfRows(rs);
									//Check the number of rows and if it is same as the number of ID's in jsonList then only proceed to validation
									if(Objects.equals(((JSONArray)obj).size(), intNumberOfRows))
									{
										ValidateResults.resultsvalidation(jsonList, rs, objHandler);
									}
									else
									{
										System.out.println("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
										System.out.println("The JSON List size is :" + ((JSONArray) obj).size() + " and the SQL Result Set Row Count is : " + intNumberOfRows);
										Log.info("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
										Log.info("The JSON List size is "+((JSONArray)obj).size()+" and the SQL Result Set Row Count is : " + intNumberOfRows);
										softAssert.fail("The JSON List size is "+((JSONArray)obj).size()+" and the SQL Result Set Row Count is : " + intNumberOfRows);
										objHandler.setStrRequestStatus("Fail");
									}
								} 
								catch (Exception e)
								{
									e.printStackTrace();
                                    Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
                                    softAssert.fail("Error in Test Script please look at logs");
                                    objHandler.setStrRequestStatus("Fail");
								}
								hashMapResult.put(String.valueOf(k), new Object[]{"workGroupId=" + workGroupId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
							}
							else
							{
								Log.info("It's a Bad Request");
								Log.info(objHandler.getStrResponse());
								hashMapResult.put(String.valueOf(k), new Object[]{"workGroupId=" + workGroupId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
							}
						}
						//if response is empty but expected and actual response codes are same
						else
						{
							Log.info("Request got processed successfully; but response was empty");
							Log.info("Response is:"+objHandler.getStrResponse());
							softAssert.fail("Request got processed successfully; but response was empty");
							objHandler.setStrRequestStatus("Fail");
							hashMapResult.put(String.valueOf(k), new Object[]{"workGroupId=" + workGroupId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}
					//if actual status code is not equal to expected status code
					else 
					{
						if(objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND)
						{
							Log.info("We have recieved the Status Code "+HttpStatus.SC_NOT_FOUND+" where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
							softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server;"+" ");
							Log.info(objHandler.getStrResponse());
							hashMapResult.put(String.valueOf(k), new Object[]{"workGroupId=" + workGroupId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						}
						else
						{
							Log.info("We have recieved the Status Code " + objHandler.getIntResponseCode() +  " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
							softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() +  " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2) + "; ");
							Log.info(objHandler.getStrResponse());
							hashMapResult.put(String.valueOf(k), new Object[]{"workGroupId=" + workGroupId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}
				}
				//if objHandler is null meaning bad type of request method
				else
				{
					boolTestcaseStatus = false;
					log.info("The Request could not be sent as the Request Type is not a valid Http Method");
					softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
					hashMapResult.put(String.valueOf(k), new Object[]{"workGroupId=" + workGroupId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
				}
				if (objHandler.getStrRequestStatus().contains("Fail"))
				{
					boolTestcaseStatus = false;
					softAssert.fail("Test case fails with data: "+"workGroupId=" + workGroupId);
				}
				objHandler = null;
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

}*/
