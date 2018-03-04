package com.endpoints;

/*
 * ********************************************************************
 * Class Name						: TaskSet_GET_ForLoggedInUser
 * Created By						: Guruprasad
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: 
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the details of the task sets created in EHR by the logged in user
 * ******************************************************************
 */

public class TaskSet_GET_ForLoggedInUser
{
	
}

/*public class TaskSet_GET_ForLoggedInUser extends BaseTest
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void taskSet_GET_ForLoggedInUser()
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
		String strSQLUniqueId ="select tab_id as id,user_id as userId,tab_name as name,tab_order as ordr from todo_tabs where user_id='"+System.getProperty("LoggedInUserId")+"' order by tab_order";
		String strSQLQuery = "SELECT x.categ_id as assignedCategoryIds" 
							+" FROM  todo_tabs t LEFT OUTER JOIN todo_tab_xref x ON t.tab_id=x.tab_id" 
							+" LEFT OUTER JOIN todo_categ_list c ON x.categ_id=c.categ_id" 
							+" WHERE t.user_id='"+System.getProperty("LoggedInUserId")+"' and x.categ_id is not null ORDER BY t.tab_order,c.categ_name";

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
								Object objTabs = JsonPath.read(strResponse, "$..id");
								ArrayList<Object> jsonListTabs = new ArrayList<Object>();
								ArrayList<Object> jsonListUniqueId = new ArrayList<Object>();
								if(objTabs instanceof JSONArray)
								{
									for(int noOfTabs=0; noOfTabs< ((JSONArray)objTabs).size(); noOfTabs++)
									{
										jsonListUniqueId.add(JsonPath.read(strResponse, "$["+noOfTabs+"]['id']").toString().toUpperCase());
										jsonListUniqueId.add(JsonPath.read(strResponse, "$["+noOfTabs+"]['userId']"));
										jsonListUniqueId.add(JsonPath.read(strResponse, "$["+noOfTabs+"]['name']"));
										jsonListUniqueId.add(JsonPath.read(strResponse, "$["+noOfTabs+"]['order']"));
										Object objCategories = JsonPath.read(strResponse, "$["+noOfTabs+"]['assignedCategoryIds']");
										if((objCategories instanceof JSONArray) && (((JSONArray)objCategories).size() >0))
										{
											for(int noOfCategories=0; noOfCategories< ((JSONArray)objCategories).size(); noOfCategories++)
											{
												jsonListTabs.add(((JSONArray)objCategories).get(noOfCategories).toString().toUpperCase());
											}
										}
									}
								}
								System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI").toString() + "\n");
								System.out.println("The JSON Response in a List: \n" + jsonListUniqueId + "\n");
								System.out.println("The JSON Response in a List: \n" + jsonListTabs + "\n");

								ResultSet rs = DatabaseConnection.statementExecution(connNGA, strSQLUniqueId);
								int intNumberOfRows = DatabaseConnection.returnNumberOfRows(rs);
								//Check the number of rows and if it is same as the number of ID's in jsonList then only proceed to validation
								if(Objects.equals(((JSONArray)objTabs).size(), intNumberOfRows))
								{
									ValidateResults.resultsvalidation(jsonListUniqueId, rs, objHandler);
									if(objHandler.getStrRequestStatus().contains("Pass"))
									{
										ResultSet rs1 = DatabaseConnection.statementExecution(connNGA, strSQLQuery);
										ValidateResults.resultsvalidation(jsonListTabs, rs1, objHandler);
										rs = DatabaseConnection.statementExecution(connNGA, strSQLQuery);
										int intNumOfRows = DatabaseConnection.returnNumberOfRows(rs);
										if(Objects.equals(((JSONArray)jsonListTabs).size(), intNumOfRows))
										{
											ValidateResults.resultsvalidation(jsonListTabs, rs, objHandler);
										}
										else
										{
											System.out.println("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
											System.out.println("The JSON List size is :1 and the SQL Result Set Row Count is : " + intNumOfRows);
											Log.info("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
											Log.info("The JSON List size is "+((JSONArray)jsonListTabs).size()+" and the SQL Result Set Row Count is : " + intNumOfRows);
											objHandler.setStrRequestStatus("Fail");
										}
									}
									
								}
								else
								{
									System.out.println("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
									System.out.println("The JSON List size is :1 and the SQL Result Set Row Count is : " + intNumberOfRows);
									Log.info("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
									Log.info("The JSON List size is "+((JSONArray)jsonListUniqueId).size()+" and the SQL Result Set Row Count is : " + intNumberOfRows);
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