package com.endpoints;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
 * Class Name						: Person_GET_OrdrDetailedResults
 * Created By						: Guruprasad
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: personId,orderId,resultId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the details of result assocaited to an order 
 * 									  based on the personId,orderId and resultId
 * ******************************************************************
 */

public class Person_GET_OrdrDetailedResults extends BaseTest_obsl
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void person_GET_OrdrDetailedResults()
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
				
				String resultId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2);
				if (resultId.equalsIgnoreCase("null"))
					resultId="";

				mapRequestParameters.put("strURI", BaseTest_obsl.mapURLRepo.get(strClassName).replace("personId", personId).replace("orderId", orderId).replace("resultId", resultId));
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParameters);

				if (objHandler != null)
				{
					String strResponse = objHandler.getStrResponse();
					ValidateResults.validateStatusCode(objHandler, ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 4));
					
					if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
					{
						if (!strResponse.isEmpty())
						{
							if (!strResponse.contains("is not valid for Guid"))
							{
								if(strResponse.contains("?$skip="))
								{
									strQuery = "select top 25"
											+" lro.obx_seq_num AS obxSeqeunceNumber,"
											+" 	case when ln.delete_ind='Y' then 'true' else 'false' end AS isDeleted,ln.enterprise_id AS enterpriseId,ln.practice_id AS practiceId,ln.person_id AS personId,"
											+" 	ln.ordering_provider AS orderingProvider,ln.enc_id AS encounterId,lro.create_timestamp AS orderDate,ln.test_location AS testLocation,ln.test_status AS testStatus,ln.ngn_status AS nextgenStatus,"
											+" 	ln.order_control AS orderControl,ln.order_priority AS orderPriority,lrop.obs_batt_id AS observationBatteryId,lrop.test_desc AS testDescription,lrop.coll_date_time AS collectionDateTime,"
											+" 	lrop.unique_obr_num AS uniqueObrNumber,ln.order_num AS nextgenOrderNumber,lrop.obr_comment AS observationResultComment,lrop.order_result_stat AS orderResultStatus,"
											+" 	lrop.ng_test_desc AS nextgenTestDescription,lrop.spec_src_code1 AS specimenSourceCode1,lrop.spec_src_code1_txt AS specimenSourceCode1Text,lrop.date_time_reported AS dateTimeReported,"
											+" 	lro.obs_id AS observationId,lro.result_desc AS resultDescription,lro.observ_value AS observeValue,lro.abnorm_flags AS abnormalFlags,lro.units AS units,lro.ref_range AS referanceRange,"
											+" 	lro.obs_date_time AS observationDateTime,lro.result_seq_num AS resultSequenceNumber,lro.comment_ind AS commentIndicator,lro.result_comment AS resultComment,"
											+" 	lro.observ_result_stat AS observationResultStatus,lro.clinical_name AS clinicalName,lro.prod_id_code1 AS productIdCode1,lro.prod_id_code1_txt AS productIdCode1Text,"
											+" 	lro.prod_id_code1_typ AS productIdCode1Type,lro.loinc_code AS loincCode"
											+" 	FROM   dbo.lab_nor AS ln"
											+" 	INNER JOIN dbo.lab_results_obr_p AS lrop ON ln.order_num = lrop.ngn_order_num"
											+" 	LEFT OUTER JOIN dbo.lab_results_obx AS lro ON lrop.unique_obr_num = lro.unique_obr_num"
											+" 	where lro.person_id = '"+personId+"' and ln.order_num = '"+orderId+"' and lrop.unique_obr_num = '"+resultId+"' and" 
											+" 	ln.order_num = lrop.ngn_order_num and lrop.unique_obr_num = lro.unique_obr_num"
											+" 	order by lro.obs_id,lro.obx_seq_num,ln.create_timestamp";
								}
								else
								{
									strQuery = "select "
											+" lro.obx_seq_num AS obxSeqeunceNumber,"
											+" 	case when ln.delete_ind='Y' then 'true' else 'false' end AS isDeleted,ln.enterprise_id AS enterpriseId,ln.practice_id AS practiceId,ln.person_id AS personId,"
											+" 	ln.ordering_provider AS orderingProvider,ln.enc_id AS encounterId,lro.create_timestamp AS orderDate,ln.test_location AS testLocation,ln.test_status AS testStatus,ln.ngn_status AS nextgenStatus,"
											+" 	ln.order_control AS orderControl,ln.order_priority AS orderPriority,lrop.obs_batt_id AS observationBatteryId,lrop.test_desc AS testDescription,lrop.coll_date_time AS collectionDateTime,"
											+" 	lrop.unique_obr_num AS uniqueObrNumber,ln.order_num AS nextgenOrderNumber,lrop.obr_comment AS observationResultComment,lrop.order_result_stat AS orderResultStatus,"
											+" 	lrop.ng_test_desc AS nextgenTestDescription,lrop.spec_src_code1 AS specimenSourceCode1,lrop.spec_src_code1_txt AS specimenSourceCode1Text,lrop.date_time_reported AS dateTimeReported,"
											+" 	lro.obs_id AS observationId,lro.result_desc AS resultDescription,lro.observ_value AS observeValue,lro.abnorm_flags AS abnormalFlags,lro.units AS units,lro.ref_range AS referanceRange,"
											+" 	lro.obs_date_time AS observationDateTime,lro.result_seq_num AS resultSequenceNumber,lro.comment_ind AS commentIndicator,lro.result_comment AS resultComment,"
											+" 	lro.observ_result_stat AS observationResultStatus,lro.clinical_name AS clinicalName,lro.prod_id_code1 AS productIdCode1,lro.prod_id_code1_txt AS productIdCode1Text,"
											+" 	lro.prod_id_code1_typ AS productIdCode1Type,lro.loinc_code AS loincCode"
											+" 	FROM   dbo.lab_nor AS ln"
											+" 	INNER JOIN dbo.lab_results_obr_p AS lrop ON ln.order_num = lrop.ngn_order_num"
											+" 	LEFT OUTER JOIN dbo.lab_results_obx AS lro ON lrop.unique_obr_num = lro.unique_obr_num"
											+" 	where lro.person_id = '"+personId+"' and ln.order_num = '"+orderId+"' and lrop.unique_obr_num = '"+resultId+"' and" 
											+" 	ln.order_num = lrop.ngn_order_num and lrop.unique_obr_num = lro.unique_obr_num"
											+" 	order by lro.obs_id,lro.obx_seq_num,ln.create_timestamp";
								}
								
								try
								{
									Object obj = JsonPath.read(strResponse, "$..obxSeqeunceNumber");
									ArrayList<Object> jsonList = new ArrayList<>();       
									if (obj instanceof JSONArray)
									{
										for(int j = 0; j< ((JSONArray) obj).size(); j++)
										{
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].obxSeqeunceNumber"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].isDeleted").toString());
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].enterpriseId"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].practiceId"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].personId").toString().toUpperCase());
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].orderingProvider").toString().toUpperCase());
											jsonList.add((JsonPath.read(strResponse, "$.["+j+"].encounterId") == null) ? null : JsonPath.read(strResponse, "$.["+j+"].encounterId").toString().toUpperCase());
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].orderDate").toString().replace("T", " "));
											jsonList.add((JsonPath.read(strResponse, "$.["+j+"].testLocation") == null) ? null : JsonPath.read(strResponse, "$.["+j+"].testLocation").toString().toUpperCase());
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].testStatus"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].nextgenStatus"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].orderControl"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].orderPriority"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].observationBatteryId"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].testDescription"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].collectionDateTime").toString().replace("T", " "));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].uniqueObrNumber").toString().toUpperCase());
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].nextgenOrderNumber").toString().toUpperCase());
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].observationResultComment"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].orderResultStatus"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].nextgenTestDescription"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].specimenSourceCode1"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].specimenSourceCode1Text"));
											jsonList.add((JsonPath.read(strResponse, "$.["+j+"].dateTimeReported") == null) ? null : JsonPath.read(strResponse, "$.["+j+"].dateTimeReported").toString().replace("T", " "));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].observationId"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].resultDescription"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].observeValue"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].abnormalFlags"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].units"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].referanceRange"));
											jsonList.add((JsonPath.read(strResponse, "$.["+j+"].observationDateTime") == null) ? null : JsonPath.read(strResponse, "$.["+j+"].observationDateTime").toString().replace("T", " "));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].resultSequenceNumber"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].commentIndicator"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].resultComment"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].observationResultStatus"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].clinicalName"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].productIdCode1"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].productIdCode1Text"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].productIdCode1Type"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].loincCode"));
										}
									}
									System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
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
								hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId +" and orderId="+ orderId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
							}
							//if the request is treated as invalid
							else
							{
								Log.info("It's a Bad Request");
								Log.info(objHandler.getStrResponse());
								hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId +" and orderId="+ orderId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
							}
						}
						//if response is empty but expected and actual response codes are same
						else
						{
							System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							System.out.println("It's a Bad Request \n");
							hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId +" and orderId="+ orderId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}
					//if actual status code is not equal to expected status code
					else if(! (objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND))
					{
						Log.info("We have recieved the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
						softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2) + " ");
						Log.info(objHandler.getStrResponse());
						hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId +" and orderId="+ orderId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Pass");
					}
					else
					{
						softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server ");
						hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId +" and orderId="+ orderId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Fail");
					}
				}
				//if objHandler is null meaning bad type of request method
				else
				{

					boolTestcaseStatus = false;
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