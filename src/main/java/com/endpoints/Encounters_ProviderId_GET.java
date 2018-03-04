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

public class Encounters_ProviderId_GET extends BaseTest_obsl
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void encounters_ProviderId_GET()
	{
		String strClassName = getClass().getSimpleName(); 
		System.out.println("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		Log.info("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		boolean boolTestcaseStatus = true;
		HashMap<String, Object[]> hashMapResult = new HashMap<>();
		hashMapResult.put("0", new Object[] {"Parameters","RequestURI", "RequestHeaders","ResponseCode","ResponseHeaders", "Response", "Status"});
		Map<String, Object> mapRequestParrameters = new HashMap<String, Object>();
		mapRequestParrameters.put("strRequestType", "GET");
		mapRequestParrameters.put("strPayLoad", "");
		mapRequestParrameters.put("strQueryString", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();
		try
		{
			for(int k=1; k<=ExcelReaderWriter.getRowCount(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{

				String strProviderId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 0);
				if (strProviderId.equalsIgnoreCase("null"))
					strProviderId="";

				mapRequestParrameters.put("strURI", BaseTest_obsl.mapURLRepo.get(strClassName).replace("providerId", strProviderId));
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);

				if (objHandler != null)
				{
					String strResponse = objHandler.getStrResponse();
					ValidateResults.validateStatusCode(objHandler, ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
					if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
					{
						if (!strResponse.isEmpty())
						{
							if (!strResponse.contains("is not valid for Guid."))
							{
								String strSQLQuery="SELECT pe.enc_id, p.practice_id,pe.enc_timestamp,p.person_id, p.last_name, p.first_name, p.middle_name, p.date_of_birth,p.sex,pe.rendering_provider_id "
										+ "FROM patient_encounter AS pe "
										+ "INNER JOIN person AS p ON pe.person_id=p.person_id "
										+ "WHERE(pe.rendering_provider_id='"+strProviderId+"')  AND enc_timestamp>='2014-01-01 00:00:00'";

								Object objProviderId = JsonPath.read(strResponse, "$.items");

								ArrayList<Object> jsonList = new ArrayList<Object>();

								if (objProviderId instanceof JSONArray)
								{
									for(int j = 0; j< ((JSONArray) objProviderId).size(); j++)
									{
										jsonList.add(JsonPath.read(strResponse, "$..id["+j+"]").toString().toUpperCase());
										jsonList.add(JsonPath.read(strResponse, "$..practiceId["+j+"]"));
										jsonList.add(JsonPath.read(strResponse, "$..timestamp["+j+"]").toString().replace("T", " "));
										jsonList.add(JsonPath.read(strResponse, "$..personId["+j+"]").toString().toUpperCase());
										jsonList.add(JsonPath.read(strResponse, "$..patientLastName["+j+"]"));
										jsonList.add(JsonPath.read(strResponse, "$..patientFirstName["+j+"]"));
										jsonList.add(JsonPath.read(strResponse, "$..patientMiddleName["+j+"]"));
										jsonList.add(JsonPath.read(strResponse, "$..patientDateOfBirth["+j+"]").toString().substring(0,10).replace("-", ""));
										jsonList.add(JsonPath.read(strResponse, "$..patientSex["+j+"]"));
										jsonList.add(JsonPath.read(strResponse, "$..providerId["+j+"]").toString().toUpperCase());
									}

									ResultSet rs = DatabaseConnection.statementExecution(connNGA, strSQLQuery);
									ValidateResults.resultsvalidation(jsonList, rs, objHandler);
									hashMapResult.put(String.valueOf(k), new Object[]{"?ProviderId=" + strProviderId,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
								}		
							}
							else
							{
								System.out.println("The API Call executed is : \n" + BaseTest_obsl.mapURLRepo.get(strClassName) + "\n");
								Log.info("It's a Bad Request");
								Log.info(objHandler.getStrResponse());
								System.out.println("It's a Bad Request \n");
								hashMapResult.put(String.valueOf(1), new Object[]{"?ProviderId=" + strProviderId,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
							}
						}
						else
						{
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							hashMapResult.put(String.valueOf(k), new Object[]{"?ProviderId=" + strProviderId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}	
					}
					else if(! (objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND))
					{
						Log.info("We have recieved the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
						softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2) + " ");
						Log.info(objHandler.getStrResponse());
						hashMapResult.put(String.valueOf(k), new Object[]{"?strProviderId=" + strProviderId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Pass");
					}
					else
					{
						softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server ");
						hashMapResult.put(String.valueOf(k), new Object[]{"?strProviderId=" + strProviderId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Fail");
					}
				}
				else
				{
					boolTestcaseStatus = false;
					log.info("The Request could not be sent as the Request Type is not a valid Http Method");
					softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
					hashMapResult.put(String.valueOf(k), new Object[]{"?strProviderId=" + strProviderId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
				}
				if (objHandler.getStrRequestStatus().contains("Fail"))
				{
					boolTestcaseStatus = false;
					softAssert.fail("Test case fails with data: "+""+"bad data");
				}
			}
			objHandler = null;
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
