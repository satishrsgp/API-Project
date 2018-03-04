package com.endpoints;

/*
 * ********************************************************************
 * Class Name						: Tasks_GET_ForLoggedInUser
 * Created By						: Guruprasad
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: 
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the details of the tasks 
 * 									  created, assigned to user individually and assigned to user's groups.
 * ******************************************************************
 */

//need to check for userid 0 we get tasks from different practice apart from default practice
public class Tasks_GET_ForLoggedInUser
{
	
}

/*public class Tasks_GET_ForLoggedInUser extends BaseTest
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void tasks_GET_ForLoggedInUser()
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
								strQuery = "select top 25 UPPER(CAST(categ_id AS CHAR(36))) as id,categ_name as name,case when delete_ind='Y' then 'true' else 'false' end as isDeleted from todo_categ_list where delete_ind not like 'Y' order by categ_name";
							}
							else
							{
								strQuery = "select UPPER(CAST(categ_id AS CHAR(36))) as id,categ_name as name,case when delete_ind='Y' then 'true' else 'false' end as isDeleted from todo_categ_list where delete_ind not like 'Y' order by categ_name";
							}
							try
							{
								Object obj = JsonPath.read(strResponse, "$..id");
								ArrayList<Object> jsonList = new ArrayList<>();       
								if (obj instanceof JSONArray)
								{
									for(int j = 0; j< ((JSONArray) obj).size(); j++)
									{
										jsonList.add(JsonPath.read(strResponse,"$..id["+j+"]").toString().toUpperCase());
										jsonList.add(JsonPath.read(strResponse,"$..name["+j+"]"));
										jsonList.add(JsonPath.read(strResponse,"$..isDeleted["+j+"]").toString());
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
