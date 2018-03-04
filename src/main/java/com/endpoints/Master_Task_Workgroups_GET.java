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
 * Class Name						: Task_Workgroups_GET
 * Created By						: Guruprasad
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: 
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the details of the task categories that have been configured
 * ******************************************************************
 */

public class Master_Task_Workgroups_GET
{
	public static Boolean master_Task_Workgroups_GET_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strQuery = null;
			if(strResponse.contains("skip=25"))
			{
				strQuery = "select top 25"
						+" workgroup_name as name,workgroup_id as id,enterprise_id as enterpriseId,practice_id as practiceId,workgroup_desc as description,note as note,"
						+" case when delete_ind='Y' then 'true' when delete_ind='N' then 'false' end as isDeleted,create_timestamp as createTimestamp,created_by as createdBy,modify_timestamp as modifyTimestamp,"
						+" modified_by as modifiedBy,former_group_id as formerGroupId,workgroup_type as type"
						+" from task_workgroup_mstr where enterprise_id='"+System.getProperty("LoggedInEnterpriseId")+"' and practice_id='"+System.getProperty("LoggedInPracticeId")+"' and(workgroup_type='1' or  workgroup_type is null)"
						+" and delete_ind='N' order by workgroup_name";
			}
			else
			{
				strQuery = "select"
						+" workgroup_name as name,workgroup_id as id,enterprise_id as enterpriseId,practice_id as practiceId,workgroup_desc as description,note as note,"
						+" case when delete_ind='Y' then 'true' when delete_ind='N' then 'false' end as isDeleted,create_timestamp as createTimestamp,created_by as createdBy,modify_timestamp as modifyTimestamp,"
						+" modified_by as modifiedBy,former_group_id as formerGroupId,workgroup_type as type"
						+" from task_workgroup_mstr where enterprise_id='"+System.getProperty("LoggedInEnterpriseId")+"' and practice_id='"+System.getProperty("LoggedInPracticeId")+"' and(workgroup_type='1' or  workgroup_type is null)"
						+" and delete_ind='N' order by workgroup_name";
			}

			String[] jsonMetaData = {"name","id","enterpriseId","practiceId","description","note","isDeleted","createTimestamp","createdBy","modifyTimestamp","modifiedBy","formerGroupId","type"};

			try
			{	
				ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
				Log.info("JSON LIST is :\n" + jsonList);
				status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..name", jsonList);
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

/*public class Task_Workgroups_GET extends BaseTest
{

	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void task_Workgroups_GET()
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
		mapRequestParameters.put("strURI", BaseTest.mapURLRepo.get(strClassName));

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
							String strQuery = null;

							if(strResponse.contains("skip=25"))
							{
								strQuery = "select top 25"
											+" concat(workgroup_id,enterprise_id,practice_id) as primary_Key,workgroup_id as id,enterprise_id as enterpriseId,practice_id as practiceId,workgroup_name as name,workgroup_desc as description,note as note,"
											+" case when delete_ind='Y' then 'true' when delete_ind='N' then 'false' end as isDeleted,create_timestamp as createTimestamp,created_by as createdBy,modify_timestamp as modifyTimestamp,"
											+" modified_by as modifiedBy,former_group_id as formerGroupId,workgroup_type as type"
											+" from task_workgroup_mstr where enterprise_id='"+System.getProperty("LoggedInEnterpriseId")+"' and practice_id='"+System.getProperty("LoggedInPracticeId")+"' and(workgroup_type='1' or  workgroup_type is null)"
											+" and delete_ind='N' order by workgroup_name";
							}
							else
							{
								strQuery = "select"
										+" concat(workgroup_id,enterprise_id,practice_id) as primary_Key,workgroup_id as id,enterprise_id as enterpriseId,practice_id as practiceId,workgroup_name as name,workgroup_desc as description,note as note,"
										+" case when delete_ind='Y' then 'true' when delete_ind='N' then 'false' end as isDeleted,create_timestamp as createTimestamp,created_by as createdBy,modify_timestamp as modifyTimestamp,"
										+" modified_by as modifiedBy,former_group_id as formerGroupId,workgroup_type as type"
										+" from task_workgroup_mstr where enterprise_id='"+System.getProperty("LoggedInEnterpriseId")+"' and practice_id='"+System.getProperty("LoggedInPracticeId")+"' and(workgroup_type='1' or  workgroup_type is null)"
										+" and delete_ind='N' order by workgroup_name";
							}
							try
							{
								Object obj = JsonPath.read(strResponse, "$..id");
								ArrayList<Object> jsonList = new ArrayList<>();       
								if (obj instanceof JSONArray)
								{
									for(int j = 0; j< ((JSONArray) obj).size(); j++)
									{
										jsonList.add(JsonPath.read(strResponse,"$..id["+j+"]").toString() + JsonPath.read(strResponse,"$..enterpriseId["+j+"]").toString()+JsonPath.read(strResponse,"$..practiceId["+j+"]").toString());
										jsonList.add(JsonPath.read(strResponse,"$..id["+j+"]"));
										jsonList.add(JsonPath.read(strResponse,"$..enterpriseId["+j+"]"));
										jsonList.add(JsonPath.read(strResponse,"$..practiceId["+j+"]"));
										jsonList.add(JsonPath.read(strResponse,"$..name["+j+"]"));
										jsonList.add(JsonPath.read(strResponse,"$..description["+j+"]"));
										jsonList.add(JsonPath.read(strResponse,"$..note["+j+"]"));
										jsonList.add(JsonPath.read(strResponse,"$..isDeleted["+j+"]").toString());
										jsonList.add(JsonPath.read(strResponse,"$..createTimestamp["+j+"]").toString().replace("T", " "));
										jsonList.add(JsonPath.read(strResponse,"$..createdBy["+j+"]"));
										jsonList.add(JsonPath.read(strResponse,"$..modifyTimestamp["+j+"]").toString().replace("T", " "));
										jsonList.add(JsonPath.read(strResponse,"$..modifiedBy["+j+"]"));
										jsonList.add(JsonPath.read(strResponse,"$..formerGroupId["+j+"]"));
										jsonList.add(JsonPath.read(strResponse,"$..type["+j+"]"));
									}
								}
								System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI").toString() + "\n");
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
									System.out.println("The JSON List size is :1 and the SQL Result Set Row Count is : " + intNumberOfRows);
									Log.info("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
									Log.info("The JSON List size is "+((JSONArray)obj).size()+" and the SQL Result Set Row Count is : " + intNumberOfRows);
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
		//Preparing the TestResults Excel Sheet and updating with all the Request and Response Details.
		TestResults.writeResults(strClassName, hashMapResult, boolTestcaseStatus);
		softAssert.assertAll();
	}

}*/
