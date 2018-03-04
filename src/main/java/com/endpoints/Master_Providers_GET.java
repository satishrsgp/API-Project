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


//have to check after the defect is fixed.

public class Master_Providers_GET extends BaseTest_obsl
{

	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void master_Providers_GET()
	{
		String strClassName = getClass().getSimpleName();
		System.out.println("Running test case " + getClass().getSimpleName()+ " with Thread Id:- " + Thread.currentThread().getId());
		Log.info("Running test case " + getClass().getSimpleName()+ " with Thread Id:- " + Thread.currentThread().getId());
		boolean boolTestcaseStatus = true;
		//ResultSet rs = null;
		HashMap<String, Object[]> hashMapResult = new HashMap<>();
		hashMapResult.put("0", new Object[] {"Parameters","RequestURI", "RequestHeaders","ResponseCode","ResponseHeaders", "Response", "Status"});
		//Finding out the Row number of the Route to be executed for this particular Test Case
		String strURI = BaseTest_obsl.mapURLRepo.get(strClassName);
		String strQueryString = strURI.substring(strURI.indexOf("?"), strURI.length());
		Map<String, Object> mapRequestParrameters = new HashMap<String, Object>();
		mapRequestParrameters.put("strRequestType", "GET");
		mapRequestParrameters.put("strPayLoad", "");
		mapRequestParrameters.put("strQueryString", strQueryString);
		mapRequestParrameters.put("strURI",strURI);
		
		RequestResponseHandler objHandler = new RequestResponseHandler();
		
		try
		{
			objHandler = API_Request_RetObj.sendRequest(httpClient,mapRequestParrameters);

			if (objHandler != null)
			{
				String strResponse = objHandler.getStrResponse();
				ValidateResults.validateStatusCode(objHandler,200);

				if (!strResponse.isEmpty())
				{
					if (!strResponse.contains("The request is invalid"))
					{
						String strSQLQuery="select DISTINCT (pm.provider_id),pm.last_name,pm.first_name,pm.middle_name,case pm.delete_ind when 'n' then 'false' end as delete_ind,pm.degree,ppm.practice_id,ppm.enterprise_id from " 
											+ "provider_mstr pm "
											+ "inner join provider_practice_mstr ppm on ppm.provider_id = pm.provider_id "
											+ "left join provider_location_xref l on  pm.provider_id = l.provider_id "
											+ "where (pm.delete_ind != 'Y' and ppm.delete_ind != 'Y') "
											+ "order by pm.last_name,pm.first_name, pm.middle_name";
											

						Object objItems = JsonPath.read(strResponse, "$.items");
						System.out.println(objItems);
						System.out.println(((JSONArray)objItems).size());
						ArrayList<Object> jsonListProviders = new ArrayList<Object>();

						if(objItems instanceof JSONArray)
						{
							for(int m=0; m< ((JSONArray)objItems).size(); m++)
							{
								jsonListProviders.add(JsonPath.read(strResponse, "$.id["+m+"]").toString());
								jsonListProviders.add(JsonPath.read(strResponse, "$.lastName["+m+"]"));
								jsonListProviders.add(JsonPath.read(strResponse, "$.firstName["+m+"]"));
								jsonListProviders.add(JsonPath.read(strResponse, "$.middleName["+m+"]"));
								jsonListProviders.add(JsonPath.read(strResponse, "$.isDeleted["+m+"]").toString());
								jsonListProviders.add(JsonPath.read(strResponse, "$.degree["+m+"]"));
								jsonListProviders.add(JsonPath.read(strResponse, "$.practiceId["+m+"]."));
								jsonListProviders.add(JsonPath.read(strResponse, "$.enterpriseId["+m+"]"));
								
							}

								ResultSet rs = DatabaseConnection.statementExecution(connNGA, strSQLQuery);
								ValidateResults.resultsvalidation(jsonListProviders, rs, objHandler);
								hashMapResult.put("1", new Object[]{"",objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});

						}
						else
						{
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							hashMapResult.put("1", new Object[]{" " ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}
					else
					{
						Log.info("It's a Bad Request");
						Log.info(objHandler.getStrResponse());
						hashMapResult.put("1", new Object[]{" " ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
					}
				}

				else if(! (objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND))
				{
					//System.out.println("The API Call executed is : \n" + mapRequestParrameters.get("strURI") + "\n");
					Log.info("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + HttpStatus.SC_OK);
					softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + HttpStatus.SC_OK + " ");

					//Log.info("It's a Bad Request");
					Log.info(objHandler.getStrResponse());
					hashMapResult.put("1", new Object[]{"", objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
					objHandler.setStrRequestStatus("Pass");
				}
				else
				{
					softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server ");
					hashMapResult.put("1", new Object[]{"", objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
					objHandler.setStrRequestStatus("Fail");
				}
			}
			else
			{
				boolTestcaseStatus = false;
				log.info("The Request could not be sent as the Request Type is not a valid Http Method");
				softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
				hashMapResult.put("1", new Object[]{"" ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
			}
			if (objHandler.getStrRequestStatus().contains("Fail"))
			{
				boolTestcaseStatus = false;
				softAssert.fail("Test case fails ");
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
