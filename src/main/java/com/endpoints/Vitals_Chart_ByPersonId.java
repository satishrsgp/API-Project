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

public class Vitals_Chart_ByPersonId extends BaseTest_obsl
{


	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void vitals_Chart_ByPersonId()
	{
		String strClassName = getClass().getSimpleName(); 
		System.out.println("Running test case " + getClass().getSimpleName()+ " with Thread Id:- " + Thread.currentThread().getId());
		Log.info("Running test case " + getClass().getSimpleName()+ " with Thread Id:- " + Thread.currentThread().getId());
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
			
			
				String strPersonId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 0);
				if (strPersonId.equalsIgnoreCase("null"))
					strPersonId="";
			
				String strFinalURI = BaseTest_obsl.mapURLRepo.get(strClassName).replace("personId", strPersonId);
				mapRequestParrameters.put("strURI", strFinalURI);
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);

				if (objHandler != null)
				{
					String strResponse = objHandler.getStrResponse();
					ValidateResults.validateStatusCode(objHandler, ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"),strClassName, k, 2));

					if (!strResponse.isEmpty())
					{
						if (!strResponse.contains("is not valid for Guid."))
						{
							String strSQLQuery="select seq_no,person_id,practice_id,enterprise_id,enc_id,vitalSignsDate,vitalSignsTime "
									+ "from vital_signs_ where person_id='"+strPersonId+"'";

							Object objProviderId = JsonPath.read(strResponse, "$..id");
							//System.out.println(objProviderId);
							//	System.out.println(((JSONArray)objProviderId).size());
							ArrayList<Object> jsonList = new ArrayList<Object>();

							if (objProviderId instanceof JSONArray)
							{
								for(int j = 0; j< ((JSONArray) objProviderId).size(); j++)
								{
									//System.out.println(JsonPath.read(strResponse, "$..id").getClass());
									jsonList.add(JsonPath.read(strResponse, "$..id["+j+"]").toString().toUpperCase());
									jsonList.add(JsonPath.read(strResponse, "$..personId["+j+"]").toString().toUpperCase());
									jsonList.add(JsonPath.read(strResponse, "$..practiceId["+j+"]"));
									jsonList.add(JsonPath.read(strResponse, "$..enterpriseId["+j+"]"));
									jsonList.add(JsonPath.read(strResponse, "$..encounterId["+j+"]").toString().toUpperCase());
									jsonList.add(JsonPath.read(strResponse, "$..vitalSignsDate["+j+"]").toString().substring(0,10).replace("-", ""));
									jsonList.add(JsonPath.read(strResponse, "$..vitalSignsTime["+j+"]"));

								}

								ResultSet rs = DatabaseConnection.statementExecution(connNGA, strSQLQuery);
								ValidateResults.resultsvalidation(jsonList, rs, objHandler);
								hashMapResult.put(String.valueOf(k), new Object[]{"?PersonId=" + strPersonId,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});

							}
							else
							{
								System.out.println("The API Call executed is : \n" + strFinalURI + "\n");
								Log.info("It's a Bad Request");
								Log.info(objHandler.getStrResponse());
								System.out.println("It's a Bad Request \n");
								hashMapResult.put(String.valueOf(1), new Object[]{"",objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
							}
						}
						else
						{
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							hashMapResult.put(String.valueOf(k), new Object[]{"?strProviderId=" + strPersonId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}
					else if(! (objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND))
					{
						Log.info("We have recieved the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
						softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2) + " ");
						Log.info(objHandler.getStrResponse());
						hashMapResult.put(String.valueOf(k), new Object[]{"?strProviderId=" + strPersonId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Pass");
					}
					else
					{
						softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server ");
						 hashMapResult.put(String.valueOf(k), new Object[]{"?strProviderId=" + strPersonId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Fail");
					}

				}
				else
				{
					boolTestcaseStatus = false;
					log.info("The Request could not be sent as the Request Type is not a valid Http Method");
					softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
					hashMapResult.put(String.valueOf(k), new Object[]{"?strProviderId=" + strPersonId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
				}
				if (objHandler.getStrRequestStatus().contains("Fail"))
				{
					boolTestcaseStatus = false;
					softAssert.fail("Test case fails with data: "+"?strProviderId=" + strPersonId);
				}
				objHandler = null;
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		ExcelReaderWriter.writeDetailedTestLogs(BaseTest_obsl.strDetailedTestLogs, getClass().getSimpleName(), hashMapResult);

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
