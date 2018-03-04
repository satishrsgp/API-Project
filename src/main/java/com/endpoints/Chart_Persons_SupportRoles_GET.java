package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import org.testng.asserts.SoftAssert;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

/*
 * ********************************************************************
 * Class Name						: Chart_Persons_SupportRoles_GET
 * Created By						: Guruprasad
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: personId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the details of the support roles associated to a person based on the personId
 * ******************************************************************
 */

//While creating testdata, please make sure that the lastname for each of the support role that you are adding are different
public class Chart_Persons_SupportRoles_GET extends BaseTest_obsl
{
	public static Boolean Chart_Persons_SupportRoles_GET_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = " select psr.last_name as lastName,psr.first_name as firstName,psr.middle_name as middleName," 
						+" psr.person_id as personId,psr.practice_id as practiceId,psr.address_line_1 as addressLine1,psr.address_line_2 as addressLine2,"
						+" psr.city as city,psr.state as state,psr.zip as zip,psr.home_phone as phone,psr.cell_phone as alternatePhone,"
						+" ctr.description as relation,ctt.description as type "
						+" from person_support_role psr inner join person_supp_role_xref psrx on psr.support_role_id=psrx.support_role_id" 
						+" left outer join code_tables ctr on (ctr.code=psr.relation_code and ctr.code_type='relation')"
						+" left join code_tables ctt on (ctt.code=psrx.support_role_code and ctt.code_type='support_role')"
						+" where psr.person_id='"+parameters.get("personId")+"' and psr.delete_ind='N'";

				String[] jsonMetaData = {"lastName","firstName","middleName","personId","practiceId","addressLine1","addressLine2","city","state","zip","phone","alternatePhone","relation","type"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..personId", jsonList);
					//objHandler = FinalAssertions.assertResponseCount(jsonMetaData, objHandler, DatabaseConnection.statementExecution(connNGA, strQuery), "id");
				} 
				catch (Exception e)
				{
					e.printStackTrace();
					Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
					softAssert.fail("Error in Test Script please look at logs");
					//objHandler.setStrRequestStatus("Fail");
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			status = true;
		}

		return status;
	}
}
/*public class Chart_Persons_SupportRoles_GET extends BaseTest_obsl
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void chart_Persons_SupportRoles_GET()
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

		RequestResponseHandler objHandler = new RequestResponseHandler();
		try
		{
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
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
							if (!(strResponse.contains("is not valid for Guid")||strResponse.contains("Date parameter is in an incorrect format.")))
							{
								String strQuery = " select psr.first_name+' '+psr.last_name+' '+psr.middle_name+' '+psr.home_phone," 
										+" psr.practice_id as practiceId,psr.first_name as firstName,psr.last_name as lastName,psr.middle_name as middleName,psr.address_line_1 as addressLine1,psr.address_line_2 as addressLine2,"
										+" psr.city as city,psr.state as state,psr.zip as zip,psr.home_phone as phone,psr.cell_phone as alternatePhone,ctr.description as relation,ctt.description as type"
										+" from person_support_role psr inner join person_supp_role_xref psrx on psr.support_role_id=psrx.support_role_id" 
										+" left outer join code_tables ctr on (ctr.code=psr.relation_code and ctr.code_type='relation')"
										+" left join code_tables ctt on (ctt.code=psrx.support_role_code and ctt.code_type='support_role')"
										+" where psr.person_id='"+personId+"' and psr.delete_ind='N'";
								try
								{
									Object obj = JsonPath.read(strResponse, "$..practiceId");
									ArrayList<Object> jsonList = new ArrayList<>();       
									if (obj instanceof JSONArray)
									{
										for(int j = 0; j< ((JSONArray) obj).size(); j++)
										{
											jsonList.add(JsonPath.read(strResponse,"$..firstName["+j+"]").toString() +" "+ JsonPath.read(strResponse,"$..lastName["+j+"]").toString() +" "+JsonPath.read(strResponse,"$..middleName["+j+"]").toString()+" "+JsonPath.read(strResponse,"$..phone["+j+"]").toString());
											jsonList.add(JsonPath.read(strResponse,"$..practiceId["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..firstName["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..lastName["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..middleName["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..addressLine1["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..addressLine2["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..city["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..state["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..zip["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..phone["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..alternatePhone["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..relation["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..type["+j+"]"));
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
								hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
							}
							//if the request is treated as invalid
							else
							{
								System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
								Log.info("It's a Bad Request");
								Log.info(objHandler.getStrResponse());
								System.out.println("It's a Bad Request \n");
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
						Log.info("We have recieved the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), getClass().getSimpleName(), k, 2));
						softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), getClass().getSimpleName(), k, 2) + " ");
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
				//if objHandler is null meaning bad type of request method
				else
				{
					boolTestcaseStatus = false;
					log.info("The Request could not be sent as the Request Type is not a valid Http Method");
					softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
					hashMapResult.put("1", new Object[]{"" ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
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
}*/