package com.endpoints;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.utils.API_Request_RetObj;
import com.utils.DatabaseConnection;
import com.utils.ExcelReaderWriter;
import com.utils.FinalAssertions;
import com.utils.Log;
import com.utils.RequestParameters;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.ValidateResults;

/*
 * ********************************************************************
 * Class Name						: Person_GET_All_Orders
 * Created By						: Guruprasad
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: personId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the details of orders associated with a person based on the personId
 * ******************************************************************
 */

public class Person_GET_All_Orders extends BaseTest_obsl
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void person_GET_All_Orders()
	{
		String strClassName = getClass().getSimpleName();
		System.out.println("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		Log.info("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		boolean boolTestcaseStatus = true;
		
		//Creating a new HashMap to store all the Request and Response Details
				HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();

				//Preparing the Map with all the Request details
				Map<String, Object> mapRequestParameters = RequestParameters.generateRequestParamsHashMap("GET", "", "");
		
		String strQuery=null;

		RequestResponseHandler objHandler = new RequestResponseHandler();
		try
		{
			for(int k=1; k<=ExcelReaderWriter.getRowCount(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{
				String personId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 0);
				if (personId.equalsIgnoreCase("null"))
					personId="";

				mapRequestParameters.put("strURI", BaseTest_obsl.mapURLRepo.get(strClassName).replace("personId", personId));
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParameters);

				if (objHandler != null)
				{
					String strResponse = objHandler.getStrResponse();
					ValidateResults.validateStatusCode(objHandler, ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
					
					if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
					{
						if (!strResponse.isEmpty())
						{
							if (!strResponse.contains("is not valid for Guid"))
							{
								if(strResponse.contains("/chart/orders?$skip="))
								{
									strQuery = "select top 25"
											+" order_num as orderNumber,test_desc as testDescription,test_status as testStatus,ngn_status as nextgenStatus,lab_id as labId,case when ng_order_ind='Y' then 'true' else 'false' end as nextgenOrderIndicator,enc_id as encounterId,"
											+" enc_timestamp as encounterTimestamp,person_id as personId,ufo_num as ufoNumber,generated_by as generatedBy,order_type as orderType,test_location as testLocation,order_control as orderControl,"
											+" order_priority as orderPriority,ordering_provider as orderingProvider,sign_off_date as signOffDate,intrf_msg as intrfMessage,enterprise_id as enterpriseId,practice_id as practiceId,"
											+" case when delete_ind='Y' then 'true' else 'false' end as isDeleted,sign_off_person as signOffPerson"
											+" from lab_nor"
											+" where person_id='" + personId + "' and delete_ind='N' order by order_num";
								}
								else
								{
									strQuery = "select "
											+" order_num as orderNumber,test_desc as testDescription,test_status as testStatus,ngn_status as nextgenStatus,lab_id as labId,case when ng_order_ind='Y' then 'true' else 'false' end as nextgenOrderIndicator,enc_id as encounterId,"
											+" enc_timestamp as encounterTimestamp,person_id as personId,ufo_num as ufoNumber,generated_by as generatedBy,order_type as orderType,test_location as testLocation,order_control as orderControl,"
											+" order_priority as orderPriority,ordering_provider as orderingProvider,sign_off_date as signOffDate,intrf_msg as intrfMessage,enterprise_id as enterpriseId,practice_id as practiceId,"
											+" case when delete_ind='Y' then 'true' else 'false' end as isDeleted,sign_off_person as signOffPerson"
											+" from lab_nor"
											+" where person_id='" + personId + "' and delete_ind='N' order by order_num";
								}
								
								try
								{
									String[] jsonMetaData = {"id","testDescription","testStatus","nextgenStatus","labId","nextgenOrderIndicator","encounterId","encounterTimestamp","personId","ufoNumber","generatedBy","orderType",
											"testLocation","orderControl","orderPriority","orderingProvider","signOffDate","intrfMessage","enterpriseId","practiceId","isDeleted","signOffPerson"};

									objHandler = FinalAssertions.assertResponseCount(jsonMetaData, objHandler, DatabaseConnection.statementExecution(connNGA, strQuery), "id");
									
								} 
								catch (Exception e)
								{
									e.printStackTrace();
                                    log.info("Error in Test Class and the detail are : \n" + e.getMessage());
                                    softAssert.fail("Error in Test Script please look at logs");
                                    objHandler.setStrRequestStatus("Fail");
								}
								hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
							}
							//if the request is treated as invalid
							else
							{
								Log.info("It's a Bad Request");
								Log.info(objHandler.getStrResponse());
								hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
							}
						}
						//if response is empty but expected and actual response codes are same
						else
						{
							System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							System.out.println("It's a Bad Request \n");
							hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}
					//if actual status code is not equal to expected status code
					else if(! (objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND))
					{
						Log.info("We have recieved the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
						softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2) + " ");
						Log.info(objHandler.getStrResponse());
						hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Pass");
					}
					else
					{
						softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server ");
						hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Fail");
					}
				}
				//if objHandler is null meaning bad type of request method
				else
				{

					boolTestcaseStatus = false;
					log.info("The Request could not be sent as the Request Type is not a valid Http Method");
					softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
					hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
				}
				
				if (objHandler.getStrRequestStatus().contains("Fail"))
				{
					boolTestcaseStatus = false;
					softAssert.fail("Test case fails with data: "+"personId=" + personId);
				}
				
				objHandler = null;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		TestResults.writeResults(strClassName, hashMapResult, boolTestcaseStatus);
		softAssert.assertAll();
	}
}