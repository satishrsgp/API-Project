package com.endpoints;

/*
 * ********************************************************************
 * Class Name						: Task_GET_FavAssigneeForLoggedInUser
 * Created By						: Guruprasad
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: 
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the details of the task favorites created in EHR by the logged in user
 * ******************************************************************
 */

public class Task_GET_FavAssigneeForLoggedInUser
{
	
}

/*public class Task_GET_FavAssigneeForLoggedInUser extends BaseTest
{

	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void task_GET_FavAssigneeForLoggedInUser()
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
		mapRequestParameters.put("strURI", BaseTest.mapURLRepo.get(strClassName).replace("userId", System.getProperty("LoggedInUserId")));
		String strSQLFavsQuery = "select "
								+" concat(user_id,'false') as primaryKey,enterprise_id as enterpriseId,practice_id as practiceId,user_id as id,last_name+', '+first_name as name,'false' as isGroup" 
								+" from user_mstr where user_id in (USR_IDS)" 
								+" union  select" 
								+" concat(workgroup_id,'true') as primaryKey,enterprise_id as enterpriseId,practice_id as practiceId,workgroup_id as id,workgroup_name as name,'true' as isGroup" 
								+" from task_workgroup_mstr WGM where WGM.workgroup_id in (GRP_IDS)";	

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
							try
							{
								Object obj= JsonPath.read(strResponse, "$..id");
								ArrayList<Object> jsonList = new ArrayList<>();
								if (obj instanceof JSONArray)
								{
									for(int j = 0; j< ((JSONArray) obj).size(); j++)
									{
										jsonList.add(JsonPath.read(strResponse, "$..id["+j+"]").toString()+JsonPath.read(strResponse, "$..isGroup["+j+"]").toString());
										jsonList.add(JsonPath.read(strResponse, "$..enterpriseId["+j+"]"));
										jsonList.add(JsonPath.read(strResponse, "$..practiceId["+j+"]"));
										jsonList.add(JsonPath.read(strResponse, "$..id["+j+"]"));
										jsonList.add(JsonPath.read(strResponse, "$..name["+j+"]"));
										jsonList.add(JsonPath.read(strResponse, "$..isGroup["+j+"]").toString());
									}
								}
								System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI").toString() + "\n");
								System.out.println("The JSON Response in a List: \n" + jsonList + "\n");
								
								String strSQLQuery = "SELECT item_value from user_pref where user_id="+System.getProperty("LoggedInUserId")+" and item_name like 'TaskRecipFav%%' and enterprise_id='"+System.getProperty("LoggedInEnterpriseId")+"' and practice_id='"+System.getProperty("LoggedInPracticeId")+"'";
								Object obj1 = DatabaseConnection.fetchColumnAsObject(connNGA, strSQLQuery, "item_value");
								if(!Objects.equals(obj1, null))
								{
									String strGroup="";
									String strUser = "";
									String[] strValues = obj1.toString().replace(" ", ",").split(",");
									for(String str1: strValues)
									{
										if(str1.startsWith("G"))
										{
											strGroup = strGroup+ str1+",";
										}
										else if(str1.startsWith("U"))
										{
											strUser = strUser + str1+",";
										}
									}
									if(strGroup.isEmpty())
									{
										strGroup = "null";
									}
									else if(strGroup.endsWith(","))
									{
										strGroup = strGroup.substring(0, strGroup.length()-1).replace("G", "");
									}
									
									if(strUser.isEmpty())
									{
										strUser = "null";
									}
									else if(strUser.endsWith(","))
									{
										strUser = strUser.substring(0, strUser.length()-1).replace("U", "");
									}
									strSQLFavsQuery = strSQLFavsQuery.replace("USR_IDS", strUser).replace("GRP_IDS", strGroup);
									
									ResultSet rs = DatabaseConnection.statementExecution(connNGA, strSQLFavsQuery);
									int intNumberOfRows = DatabaseConnection.returnNumberOfRows(rs);
									//Check the number of rows and if it is same as the number of ID's in jsonList then only proceed to validation
									if(Objects.equals(((JSONArray)obj).size(), intNumberOfRows))
									{
										ValidateResults.resultsvalidation(jsonList, DatabaseConnection.statementExecution(connNGA, strSQLFavsQuery), objHandler);
									}
									else
									{
										System.out.println("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
										System.out.println("The JSON List size is :" + ((JSONArray) obj).size() + " and the SQL Result Set Row Count is : " + intNumberOfRows);
										Log.info("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
										Log.info("The JSON List size is "+((JSONArray)obj).size()+" and the SQL Result Set Row Count is : " + intNumberOfRows);
										objHandler.setStrRequestStatus("Fail");
									}
								}
								else
								{
									System.out.println("No favorites have been set in EHR for the logged in user with user id:"+System.getProperty("LoggedInUserId"));
									Log.info("No favorites have been set in EHR for the logged in user with user id:"+System.getProperty("LoggedInUserId"));
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