package com.endpoints;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.utils.API_Request_RetObj;
import com.utils.ExcelReaderWriter;
import com.utils.Log;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.ValidateResults;


/*
 * ********************************************************************
 * Class Name						: Document_Post
 * Created By						: Anil Kumar Veluru
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: 
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will post a document to person chart
 * ******************************************************************
 */

public class Document_Post extends BaseTest_obsl
{
	public static String testStatus = null;
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void tasking_Post()
	{
		String strClassName = getClass().getSimpleName();
		System.out.println("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		Log.info("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		boolean boolTestcaseStatus = true;
		HashMap<String, Object[]> hashMapResult = new HashMap<>();
		hashMapResult.put("0", new Object[] {"Parameters","RequestURI", "RequestHeaders","ResponseCode","ResponseHeaders", "Response", "Status"});
		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParameters = new HashMap<String, Object>();
		mapRequestParameters.put("strRequestType", "POST");
		mapRequestParameters.put("strQueryString", "");
		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{
				String personId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 0);
				if (personId.equalsIgnoreCase("null"))
					personId="";
				
				String EncounterId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 1) ;
				String FileDescription = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2);
				String AppCreatedBy = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 3);
				//if we need send multiple categories send them by separating each with a comma(,) 
				String Categories = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 4);
				String DocumentType = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 5);

				String payload = 
						"{\"encounterId\": \""+EncounterId+"\","
								+ "\"fileDescription\": \""+FileDescription+"\","
								+ "\"appCreatedBy\": \""+AppCreatedBy+"\","
								+ "\"categories\": \""+Categories +"\","
								+ "\"documentType\": \""+DocumentType +"\"}";


				mapRequestParameters.put("strPayLoad", payload);

				mapRequestParameters.put("isMultiPart", "YES");
				mapRequestParameters.put("strURI", BaseTest_obsl.mapURLRepo.get(strClassName).replace("personId", personId));
				mapRequestParameters.put("strFilePath", "/Users/aveluru/Documents/Maven_Workspace/APIV2.0_Milestone1/TestData/kong.jpg");
				//Sending the Request to get the Response
				String task_id = null;

				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParameters);
				if (objHandler!= null)
				{
					String strResponse = objHandler.getStrResponse();
					task_id = strResponse.replace("\"", "");

					@SuppressWarnings("unused")
					String strQuery = "select * from user_todo_list where task_id = '"+strResponse.replace("\"", "")+"'";
					@SuppressWarnings("unused")
					String getCatId ="select * from todo_categ_xref where task_id = '"+task_id+"'";
					Log.info("\n Task_id generated post request : "+ strResponse + "\n");

					ValidateResults.validateStatusCode(objHandler, ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 12));
					if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
					{
						if (!(strResponse.contains("is not valid for Guid")||strResponse.contains("Date parameter is in an incorrect format.")))
						{


							/*try
							{
								ResultSet rs = DatabaseConnection.statementExecution(connNGA, strQuery);


								int intNumberOfRows = DatabaseConnection.returnNumberOfRows(rs);


								boolean task_priority=DatabaseConnection.fetchColumnAsObject(connNGA, strQuery, "task_priority").toString().contains(Priority);
								Log.info("\n Pass :::  Task_Priority : "+ task_priority);
								testStatus = task_priority ? "Pass " : "Fail : "+Priority;
								Log.info("\n Pass :::  task_subj : "+ task_priority);


								boolean task_subj=DatabaseConnection.fetchColumnAsObject(connNGA, strQuery, "task_subj").toString().toString().contains(Subject);


								boolean task_desc=DatabaseConnection.fetchColumnAsObject(connNGA, strQuery, "task_desc").toString().contains(Description);
								boolean pat_item_id=DatabaseConnection.fetchColumnAsObject(connNGA, strQuery, "pat_item_id").toString().contains(ItemId);
								boolean pat_item_desc=DatabaseConnection.fetchColumnAsObject(connNGA, strQuery, "pat_item_desc").toString().contains(ItemDescription);
								boolean pat_item_type=DatabaseConnection.fetchColumnAsObject(connNGA, strQuery, "pat_item_type").toString().contains(ItemType);
								boolean pat_enc_id=DatabaseConnection.fetchColumnAsObject(connNGA, strQuery, "pat_enc_id").toString().contains(EncounterId);
								boolean pat_acct_id=DatabaseConnection.fetchColumnAsObject(connNGA, strQuery, "pat_acct_id").toString().contains(PersonId.toUpperCase());
								boolean Assigned_cat_id=DatabaseConnection.fetchColumnAsObject(connNGA, getCatId, "categ_id").toString().contains(AssignedCategoryIds.toUpperCase());
								//   AssignedUserIds
								//  AssignedGroupIds



								if(task_priority && task_subj && task_desc && pat_item_id && pat_item_desc && pat_item_type && pat_enc_id && pat_acct_id && Assigned_cat_id){
									objHandler.setStrRequestStatus("Pass");

								}
								else{
									//  Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
									softAssert.fail("Check the Task_id please or create new tasks to delete");
									boolTestcaseStatus = false;
									objHandler.setStrRequestStatus("Fail");
								}

								//    Object obj = JsonPath.read(strResponse, "$..id");
								//  ArrayList<Object> jsonList = new ArrayList<>();       

								System.out.println("The API Call executed is : \n" + mapRequestParrameters.get("strURI") + "\n");
								//   System.out.println("The JSON Response in a List: \n" + jsonList + "\n");

								//Check the number of rows and if it is same as the number of ID's in jsonList then only proceed to validation

							} 
							catch (Exception e)
							{
								e.printStackTrace();
								Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
								softAssert.fail("Error in Test Script please look at logs");
								objHandler.setStrRequestStatus("Fail");
							}*/
							hashMapResult.put(String.valueOf(k), new Object[]{"task_id=" + task_id ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						}
						else
						{
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							hashMapResult.put(String.valueOf(k), new Object[]{"task_id=" + task_id ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
						// }
						/*   else
                        {
                         Log.info("Request got processed successfully; but response was empty");
                         Log.info("Response is:"+objHandler.getStrResponse());
                         softAssert.fail("Request got processed successfully; but response was empty");
                         objHandler.setStrRequestStatus("Fail");
                         hashMapResult.put(String.valueOf(k), new Object[]{"task_id=" + task_id ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
                        }*/
					}
					//if actual status code is not equal to expected status code
					else 
					{
						if(objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND)
						{
							Log.info("We have recieved the Status Code "+HttpStatus.SC_NOT_FOUND+" where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
							softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server;"+" ");
							Log.info(objHandler.getStrResponse());
							hashMapResult.put(String.valueOf(k), new Object[]{"task_id=" + task_id ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						}
						else
						{
							Log.info("We have recieved the Status Code " + objHandler.getIntResponseCode() +  " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
							softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() +  " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2) + "; ");
							Log.info(objHandler.getStrResponse());
							hashMapResult.put(String.valueOf(k), new Object[]{"task_id=" + task_id ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}
				}
				//if objHandler is null meaning bad type of request method
				else
				{
					boolTestcaseStatus = false;
					log.info("The Request could not be sent as the Request Type is not a valid Http Method");
					softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
					hashMapResult.put(String.valueOf(k), new Object[]{"task_id=" + task_id ,null, objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
				}
				if (objHandler.getStrRequestStatus().contains("Fail"))
				{
					boolTestcaseStatus = false;
					softAssert.fail("Test case fails with data: "+"task_id=" + task_id);
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


}
