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
 * Class Name						: Person_GET_ResultsForAnOrder
 * Created By						: Guruprasad
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: personId,orderId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the results for an order based on the personId & orderId
 * ******************************************************************
 */

public class Person_GET_ResultsForAnOrder extends BaseTest_obsl
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void person_GET_ResultsForAnOrder()
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

				String orderId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 1);
				if (orderId.equalsIgnoreCase("null"))
					orderId="";

				mapRequestParameters.put("strURI", BaseTest_obsl.mapURLRepo.get(strClassName).replace("personId", personId).replace("orderId", orderId));
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParameters);

				if (objHandler != null)
				{
					String strResponse = objHandler.getStrResponse();
					ValidateResults.validateStatusCode(objHandler, ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 3));

					if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
					{
						if (!strResponse.isEmpty())
						{
							if (!strResponse.contains("is not valid for Guid"))
							{
								if(strResponse.contains("/results?$skip="))
								{
									strQuery = "select top 25"
											+" lro.obx_seq_num AS obxSeqNumber,ln.create_timestamp AS orderDate,ln.order_num AS nextgenOrderNumber,lrop.unique_obr_num AS uniqueObrNumber,lrop.obs_batt_id AS observationBatteryId,lrop.test_desc AS testDescription,"
											+" lro.obs_id AS observationId,lro.units AS units,lro.ref_range AS referenceRange"
											+" FROM   dbo.lab_nor AS ln"
											+" INNER JOIN dbo.lab_results_obr_p AS lrop ON ln.order_num = lrop.ngn_order_num"
											+" LEFT OUTER JOIN dbo.lab_results_obx AS lro ON lrop.unique_obr_num = lro.unique_obr_num"
											+" where lro.person_id = '"+personId+"' and ln.order_num = '"+orderId+"' and" 
											+" ln.order_num = lrop.ngn_order_num and lrop.unique_obr_num = lro.unique_obr_num"
											+" order by lro.obs_id,lro.obx_seq_num,ln.create_timestamp";
								}
								else
								{
									strQuery = "select "
											+" lro.obx_seq_num AS obxSeqNumber,ln.create_timestamp AS orderDate,ln.order_num AS nextgenOrderNumber,lrop.unique_obr_num AS uniqueObrNumber,lrop.obs_batt_id AS observationBatteryId,lrop.test_desc AS testDescription,"
											+" lro.obs_id AS observationId,lro.units AS units,lro.ref_range AS referenceRange"
											+" FROM   dbo.lab_nor AS ln"
											+" INNER JOIN dbo.lab_results_obr_p AS lrop ON ln.order_num = lrop.ngn_order_num"
											+" LEFT OUTER JOIN dbo.lab_results_obx AS lro ON lrop.unique_obr_num = lro.unique_obr_num"
											+" where lro.person_id = '"+personId+"' and ln.order_num = '"+orderId+"' and" 
											+" ln.order_num = lrop.ngn_order_num and lrop.unique_obr_num = lro.unique_obr_num";
								}

								try
								{

									String[] jsonMetaData = {"obxSeqNumber","orderDate","nextgenOrderNumber","uniqueObrNumber","observationBatteryId","testDescription","observationId","units","referenceRange"};

									objHandler = FinalAssertions.assertResponseCount(jsonMetaData, objHandler, DatabaseConnection.statementExecution(connNGA, strQuery), "obxSeqNumber");
								} 
								catch (Exception e)
								{
									e.printStackTrace();
									log.info("Error in Test Class and the detail are : \n" + e.getMessage());
									softAssert.fail("Error in Test Script please look at logs");
									objHandler.setStrRequestStatus("Fail");
								}
								hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId +" and orderId="+ orderId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
							}
							else
							{
								//System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
								Log.info("It's a Bad Request");
								Log.info(objHandler.getStrResponse());
								//System.out.println("It's a Bad Request \n");
								hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId +" and orderId="+ orderId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
							}
						}
						else
						{
							System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							System.out.println("It's a Bad Request \n");
							hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId +" and orderId="+ orderId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}
					else if(! (objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND))
					{
						//System.out.println("The API Call executed is : \n" + mapRequestParrameters.get("strURI") + "\n");
						Log.info("We have recieved the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
						softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2) + " ");

						//Log.info("It's a Bad Request");
						Log.info(objHandler.getStrResponse());
						//System.out.println("We have received the Status Code " + objHandler.getIntResponseCode() + "where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(Configs.TestDataWorkBook, strClassName, k, 2) + "\n");
						hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId +" and orderId="+ orderId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Pass");
					}
					else
					{
						//Log.info("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server");
						softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server ");
						hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId +" and orderId="+ orderId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Fail");
					}
				}
				else
				{

					boolTestcaseStatus = false;
					//System.out.println("The Request could not be sent as the Request Type is not a valid Http Method");
					log.info("The Request could not be sent as the Request Type is not a valid Http Method");
					softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
					hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId +" and orderId="+ orderId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
				}

				if (objHandler.getStrRequestStatus().contains("Fail"))
				{
					boolTestcaseStatus = false;
					softAssert.fail("Test case fails with data: "+"personId=" + personId +" and orderId="+ orderId);
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