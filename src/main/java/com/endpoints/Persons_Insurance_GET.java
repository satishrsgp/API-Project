package com.endpoints;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
 * Class Name						: Persons_Insurance_GET
 * Created By						: Anil Kumar Veluru
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: PersonId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch details of Insurance for a Particular Person
 * ******************************************************************
 */

public class Persons_Insurance_GET extends BaseTest_obsl
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void persons_Insurance_GET()
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
			for(int k=1; k<=ExcelReaderWriter.getRowCount(  BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{
				String personId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 0);
				if (personId.equalsIgnoreCase("null"))
					personId="";

				mapRequestParrameters.put("strURI", BaseTest_obsl.mapURLRepo.get(strClassName) .replace("personId", personId));
				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);

				if (objHandler != null)
				{
					String strResponse = objHandler.getStrResponse();
					ValidateResults.validateStatusCode(objHandler, ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 1));
					if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
					{
						if (!strResponse.isEmpty())
						{
							if (!(strResponse.contains("is not valid for Guid")||strResponse.contains("Date parameter is in an incorrect format.")))
							{
								String strQuery = "select payer_id as payerId, payer_name as payerName, def_cob as defaultCob, policy_eff_date as policyEffectiveDate, "
										+ "policy_exp_date as policyExpirationDate, CASE WHEN active_ind = 'Y'  THEN 'true' ELSE 'false' END as isActive from person_payer"
										+" where person_id='"+personId+"'" + "and (policy_exp_date > getdate() or policy_exp_date = '') and ins_type != 'MB'";
								try
								{
									Object obj = JsonPath.read(strResponse, "$..payerId");
									ArrayList<Object> jsonList = new ArrayList<>();       
									if (obj instanceof JSONArray)
									{
										for(int j = 0; j< ((JSONArray) obj).size(); j++)
										{
											jsonList.add((Object) JsonPath.read(strResponse,"$..payerId["+j+"]").toString().toUpperCase());
											jsonList.add(JsonPath.read(strResponse,"$..payerName["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..defaultCob["+j+"]"));
											if(Objects.equals(JsonPath.read(strResponse,"$..policyEffectiveDate["+j+"]"), null))
											{
												jsonList.add(null);
											}
											else
											{
												jsonList.add(JsonPath.read(strResponse,"$..policyEffectiveDate["+j+"]").toString().substring(0, 10).replace("-", ""));
											}
											if(Objects.equals(JsonPath.read(strResponse,"$..policyExpirationDate["+j+"]"), null))
											{
												jsonList.add(null);
											}
											else
											{
												jsonList.add(JsonPath.read(strResponse,"$..policyExpirationDate["+j+"]").toString().substring(0, 10).replace("-", ""));
											}
											
											jsonList.add(JsonPath.read(strResponse,"$..isActive["+j+"]").toString());
										}
									}
									System.out.println("The API Call executed is : \n" + mapRequestParrameters.get("strURI") + "\n");
									System.out.println("The JSON Response in a List: \n" + jsonList + "\n");

									ResultSet rs = DatabaseConnection.statementExecution(connNGA, strQuery);
									int intNumberOfRows = DatabaseConnection.returnNumberOfRows(rs);
									
									//Check the number of rows and if it is same as the number of ID's in jsonList then only proceed to validation
									if( intNumberOfRows == ((JSONArray) obj).size())
									{
										ValidateResults.resultsvalidation(jsonList, rs, objHandler);
									}
									else
									{
										System.out.println("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
										System.out.println("The JSON List size is :" + ((JSONArray) obj).size() + " and the SQL Result Set Row Count is : " + intNumberOfRows);
										Log.info("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
										objHandler.setStrRequestStatus("Fail");
									}
									
									//ValidateResults.resultsvalidation(jsonList, rs, objHandler);
								} 
								catch (Exception e)
								{
									e.printStackTrace();
								}
								hashMapResult.put(String.valueOf(k), new Object[]{"?personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
							}
							else
							{
								Log.info("It's a Bad Request");
								Log.info(objHandler.getStrResponse());
								hashMapResult.put(String.valueOf(k), new Object[]{"?personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
							}
						}
						else
						{
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							hashMapResult.put(String.valueOf(k), new Object[]{"?personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}

					else if(! (objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND))
					{
						Log.info("We have recieved the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
						softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2) + " ");
						Log.info(objHandler.getStrResponse());
						hashMapResult.put(String.valueOf(k), new Object[]{"?personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Pass");
					}
					else
					{
						softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server ");
						 hashMapResult.put(String.valueOf(k), new Object[]{"?personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Fail");
					}
				}
				else
				{
					boolTestcaseStatus = false;
					log.info("The Request could not be sent as the Request Type is not a valid Http Method");
					softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
					hashMapResult.put(String.valueOf(k), new Object[]{"?personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
				}
				if (objHandler.getStrRequestStatus().contains("Fail"))
				{
					boolTestcaseStatus = false;
					softAssert.fail("Test case fails with data: "+"?personId=" + personId);
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