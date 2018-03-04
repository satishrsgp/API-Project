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
import com.utils.GeneralUtils;
import com.utils.Json_Parser;
import com.utils.Log;
import com.utils.RequestResponseHandler;
import com.utils.ValidateResults;

import net.minidev.json.JSONArray;


/*
 * ********************************************************************
 * Class Name						: Person_Lookup
 * Created By						: Anil Kumar Veluru
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: LastName, FirstName, ssn, and more
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch person details matching the Search Criteria
 * ******************************************************************
 */

public class Person_Lookup extends BaseTest_obsl
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void person_Lookup()
	{
		String strClassName = getClass().getSimpleName();
		System.out.println("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		Log.info("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		boolean boolTestcaseStatus = true;
		HashMap<String, Object[]> hashMapResult = new HashMap<>();
		hashMapResult.put("0", new Object[] {"Parameters","RequestURI", "RequestHeaders","ResponseCode","ResponseHeaders", "Response", "Status"});

		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParameters = new HashMap<String, Object>();
		mapRequestParameters.put("strRequestType", "GET");
		mapRequestParameters.put("strPayLoad", "");
		mapRequestParameters.put("strURI", BaseTest_obsl.mapURLRepo.get(strClassName));

		RequestResponseHandler objHandler = new RequestResponseHandler();
		try
		{
			for(int k=2; k<=ExcelReaderWriter.getRowCount(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{
				//Generate the Query String Parameter for the Request
				Map<String, String> mapQuery = new HashMap<String, String>();
				mapQuery = GeneralUtils.generateQueryString(strClassName, k);

				//Add the Query String Parameter to the Map
				mapRequestParameters.put("strQueryString", mapQuery.get("QueryString"));

				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParameters);

				if (objHandler != null)
				{
					String strResponse = objHandler.getStrResponse();
					ValidateResults.validateStatusCode(objHandler, ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 11));
					if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
					{
						if (!strResponse.isEmpty())
						{
							if (!(strResponse.contains("is not valid for Guid")||strResponse.contains("Date parameter is in an incorrect format.")))
							{
								String strQuery = "select pr.person_id as id, pr.first_name as firstName, pr.last_name as lastName, pr.middle_name as middleName, "
										+ "pr.prior_last_name as nickname, pr.address_line_1 as addressLine1, pr.address_line_2 as addressLine2, pr.city, pr.state, pr.zip, pr.country, "
										+ "pr.home_phone as homePhone, pr.date_of_birth as dateOfBirth, pr.sex, pr.ssn as socialSecurityNumber, pr.person_nbr as personNumber,"
										+ "pt.med_rec_nbr as medicalRecordNumber, pr.other_id_number as otherIdNumber, "
										+ "CASE WHEN pec.enterprise_chart_ind = 'Y'  THEN 'true' ELSE 'false' END as isEnterpriseChart, "
										+ "CASE WHEN pt.person_id = null THEN 'false' ELSE 'true'  END as isPatient "
										+ "from person pr INNER JOIN patient pt ON pr.person_id = pt.person_id "
										+ "INNER JOIN patient_enterprise_chart pec ON pec.enterprise_id = pt.enterprise_id AND pec.person_id = pt.person_id "
										+ "where " + mapQuery.get("Query") 
										+ " order by pr.create_timestamp desc";
								
								try
								{
									Object obj = JsonPath.read(strResponse, "$..id");
									/*ArrayList<String> jsonTokenNameList = new ArrayList<>();
									jsonTokenNameList.ad*/

									ArrayList<Object> jsonList = new ArrayList<>();       
									if (obj instanceof JSONArray)
									{
										for(int j = 0; j< ((JSONArray) obj).size(); j++)
										{
											jsonList.add((Object) JsonPath.read(strResponse,"$..id["+j+"]").toString().toUpperCase());
											jsonList =  Json_Parser.jsonTokenValue("firstName", strResponse, j, jsonList);
											jsonList =  Json_Parser.jsonTokenValue("lastName", strResponse, j, jsonList);
											jsonList =  Json_Parser.jsonTokenValue("middleName", strResponse, j, jsonList);
											jsonList =  Json_Parser.jsonTokenValue("nickname", strResponse, j, jsonList);
											jsonList =  Json_Parser.jsonTokenValue("addressLine1", strResponse, j, jsonList);
											jsonList =  Json_Parser.jsonTokenValue("addressLine2", strResponse, j, jsonList);
											jsonList =  Json_Parser.jsonTokenValue("city", strResponse, j, jsonList);
											jsonList =  Json_Parser.jsonTokenValue("state", strResponse, j, jsonList);
											jsonList =  Json_Parser.jsonTokenValue("zip", strResponse, j, jsonList);
											jsonList =  Json_Parser.jsonTokenValue("country", strResponse, j, jsonList);
											jsonList =  Json_Parser.jsonTokenValue("homePhone", strResponse, j, jsonList);
											jsonList =  Json_Parser.jsonTokenValue("dateOfBirth", strResponse, j, jsonList);
											
											String strDateOfBirth = jsonList.get(jsonList.size()-1).toString().substring(0, 10).replace("-", "");
											jsonList.set(jsonList.size()-1, strDateOfBirth);

											jsonList =  Json_Parser.jsonTokenValue("sex", strResponse, j, jsonList);
											jsonList =  Json_Parser.jsonTokenValue("socialSecurityNumber", strResponse, j, jsonList);
											jsonList =  Json_Parser.jsonTokenValue("personNumber", strResponse, j, jsonList);
											jsonList =  Json_Parser.jsonTokenValue("medicalRecordNumber", strResponse, j, jsonList);
											jsonList =  Json_Parser.jsonTokenValue("otherIdNumber", strResponse, j, jsonList);
											jsonList =  Json_Parser.jsonTokenValue("isEnterpriseChart", strResponse, j, jsonList);
											
											String isEnterpriseChart = jsonList.get(jsonList.size()-1).toString();
											jsonList.set(jsonList.size()-1, isEnterpriseChart);

											jsonList =  Json_Parser.jsonTokenValue("isPatient", strResponse, j, jsonList);
											String isPatient = jsonList.get(jsonList.size()-1).toString();
											jsonList.set(jsonList.size()-1, isPatient);
										}
									}
									System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
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
								hashMapResult.put(String.valueOf(k), new Object[]{mapQuery.get("QueryString") ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
							}
							else
							{
								Log.info("It's a Bad Request");
								Log.info(objHandler.getStrResponse());
								hashMapResult.put(String.valueOf(k), new Object[]{mapQuery.get("QueryString") ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
							}
						}
						else
						{
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							hashMapResult.put(String.valueOf(k), new Object[]{mapQuery.get("QueryString") ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}

					else if(! (objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND))
					{
						Log.info("We have recieved the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 11));
						softAssert.fail("We have recieved the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 11) + " ");
						Log.info(objHandler.getStrResponse());
						hashMapResult.put(String.valueOf(k), new Object[]{mapQuery.get("QueryString") ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Pass");
					}
					else
					{
						softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server ");
						hashMapResult.put(String.valueOf(k), new Object[]{mapQuery.get("QueryString") ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Fail");
					}
				}
				else
				{
					boolTestcaseStatus = false;
					log.info("The Request could not be sent as the Request Type is not a valid Http Method");
					softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
					hashMapResult.put(String.valueOf(k), new Object[]{mapQuery.get("QueryString") ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
				}
				if (objHandler.getStrRequestStatus().contains("Fail"))
				{
					boolTestcaseStatus = false;
					softAssert.fail("Test case fails with data: "+ mapQuery.get("QueryString"));
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
