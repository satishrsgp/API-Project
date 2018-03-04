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
 * Class Name						: Person_GET_An_Order
 * Created By						: Guruprasad
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: personId,orderId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the details of an order based on the personId & orderId
 * ******************************************************************
 */

public class Person_GET_An_Order extends BaseTest_obsl
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void person_GET_An_Order()
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
								
									strQuery = "select" 
											+" ln.order_num as orderNumber,ln.test_desc as testDescription,ln.enc_id as encounterId,ln.person_id as personId,ln.ufo_num as ufoNumber,ln.generated_by as generatedBy,ln.order_type as orderType,"
											+" ln.test_location as testLocation,ln.ordering_provider as orderingProvider,ln.enc_timestamp as encounterTimestamp,ln.sign_off_date as signOffDate,ln.sign_off_person as signOffPerson,"
											+" ln.test_status as testStatus,ln.ngn_status as nextgenStatus,ln.lab_id as labId,case when ln.ng_order_ind='Y' then 'true' else 'false' end as nextgenOrderIndicator,case when ln.signoff_comments_ind='Y' then 'true' else 'false' end as signoffCommentsIndicator,"
											+" case when ln.documents_ind='Y' then 'true' else 'false' end as documentsIndicator,ln.order_control as orderControl,ln.order_priority as orderPriority,ln.time_entered as timeEntered,ln.spec_action_code as specimenActionCode,"
											+" ln.billing_type as billingType,ln.clinical_info as clinicalInformation,ln.cancel_reason as cancelReason,ln.intrf_msg as intrfMessage,ln.old_order_num as oldOrderNumber,ln.practice_id as practiceId,"
											+" ln.enterprise_id as enterpriseId,case when ln.delete_ind='Y' then 'true' else 'false' end as isDeleted,pm.description as description,lm.location_name as locationName,lm.address_line_1 as addressLine1,"
											+" lm.address_line_2 as addressLine2,lm.city as city,lm.state as state,lm.zip as zip,lm.phone as phone,lm.fax as fax,um.first_name as firstName,um.last_name as lastName,um.mi as middleInitial"
											+" from lab_nor ln left outer join provider_mstr pm on ln.ordering_provider=pm.provider_id"
											+" left join location_mstr lm on ln.test_location=lm.location_id"
											+" left join user_mstr um on um.user_id=ln.sign_off_person"
											+" where ln.person_id = '"+personId+"' and ln.order_num = '"+orderId+"'";
								
								try
								{
									Object obj = JsonPath.read(strResponse, "$..id");
									ArrayList<Object> jsonList = new ArrayList<>();   
									jsonList.add(JsonPath.read(strResponse, "$.id").toString().toUpperCase());
									jsonList.add(JsonPath.read(strResponse, "$.testDescription"));
									jsonList.add((JsonPath.read(strResponse, "$.encounterId") == null ? null : JsonPath.read(strResponse, "$.encounterId").toString().toUpperCase()));
									jsonList.add(JsonPath.read(strResponse, "$.personId").toString().toUpperCase());
									jsonList.add(JsonPath.read(strResponse, "$.ufoNumber"));
									jsonList.add(JsonPath.read(strResponse, "$.generatedBy"));
									jsonList.add(JsonPath.read(strResponse, "$.orderType"));
									jsonList.add((JsonPath.read(strResponse, "$.testLocation") == null)? null : JsonPath.read(strResponse, "$.testLocation").toString().toUpperCase());
									jsonList.add(JsonPath.read(strResponse, "$.orderingProvider").toString().toUpperCase());
									jsonList.add(JsonPath.read(strResponse, "$.encounterTimestamp").toString().replace("T", " "));
									jsonList.add( (JsonPath.read(strResponse, "$.signOffDate") == null) ? null : JsonPath.read(strResponse, "$.signOffDate").toString().replace("T", " "));
									jsonList.add(JsonPath.read(strResponse, "$.signOffPerson"));
									jsonList.add(JsonPath.read(strResponse, "$.testStatus"));
									jsonList.add(JsonPath.read(strResponse, "$.nextgenStatus"));
									jsonList.add(JsonPath.read(strResponse, "$.labId"));
									jsonList.add(JsonPath.read(strResponse, "$.nextgenOrderIndicator").toString());
									jsonList.add(JsonPath.read(strResponse, "$.signoffCommentsIndicator").toString());
									jsonList.add(JsonPath.read(strResponse, "$.documentsIndicator").toString());
									jsonList.add(JsonPath.read(strResponse, "$.orderControl"));
									jsonList.add(JsonPath.read(strResponse, "$.orderPriority"));
									jsonList.add(JsonPath.read(strResponse, "$.timeEntered") == null ?  null: JsonPath.read(strResponse, "$.timeEntered").toString().replace("T", " "));
									jsonList.add(JsonPath.read(strResponse, "$.specimenActionCode"));
									jsonList.add(JsonPath.read(strResponse, "$.billingType"));
									jsonList.add(JsonPath.read(strResponse, "$.clinicalInformation"));
									jsonList.add(JsonPath.read(strResponse, "$.cancelReason"));
									jsonList.add(JsonPath.read(strResponse, "$.intrfMessage"));
									jsonList.add(JsonPath.read(strResponse, "$.oldOrderNumber"));
									jsonList.add(JsonPath.read(strResponse, "$.practiceId"));
									jsonList.add(JsonPath.read(strResponse, "$.enterpriseId"));
									jsonList.add(JsonPath.read(strResponse, "$.isDeleted").toString());
									jsonList.add(JsonPath.read(strResponse, "$.description"));
									jsonList.add(JsonPath.read(strResponse, "$.locationName"));
									jsonList.add(JsonPath.read(strResponse, "$.addressLine1"));
									jsonList.add(JsonPath.read(strResponse, "$.addressLine2"));
									jsonList.add(JsonPath.read(strResponse, "$.city"));
									jsonList.add(JsonPath.read(strResponse, "$.state"));
									jsonList.add(JsonPath.read(strResponse, "$.zip"));
									jsonList.add(JsonPath.read(strResponse, "$.phone"));
									jsonList.add(JsonPath.read(strResponse, "$.fax"));
									jsonList.add(JsonPath.read(strResponse, "$.firstName"));
									jsonList.add(JsonPath.read(strResponse, "$.lastName"));
									jsonList.add(JsonPath.read(strResponse, "$.middleInitial"));
									
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
								//System.out.println("It's a Bad Request \n");
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