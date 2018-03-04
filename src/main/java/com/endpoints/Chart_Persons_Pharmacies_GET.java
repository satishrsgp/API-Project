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
 * Class Name						: Chart_Persons_Pharmacies_GET
 * Created By						: Guruprasad
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: personId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the details of the pharmacies associated to a person based on the personId
 * ******************************************************************
 */

public class Chart_Persons_Pharmacies_GET extends BaseTest_obsl
{
	public static Boolean Chart_Persons_Pharmacies_GET_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			String strQuery = " SELECT" 
					+" UPPER(CAST(pm.pharmacy_id AS CHAR(36))) as id,pa.person_id as personId,pa.practice_id as practiceId,pm.name as name,pm.address_line_1 as addressLine1,pm.address_line_2 as addressLine2,pm.city as city,pm.state as state"
					+" ,pm.zip as zip,pm.phone as phone,pm.alternate_phone as alternatePhone,pm.alt_phone_desc as alternatePhoneDescription,pm.fax as fax,pm.chief_pharm_name as chiefPharmacistName,"
					+" case when pm.rx_by_fax_ind='Y' then 'true' else 'false' end as allowsRxByFax,case when pm.active_erx_ind='Y' then 'true' else 'false' end as hasActiveErx,epm.ncpdp_id as ncpdpId"
					+" ,epm.surescripts_enroll_date as surescriptsEnrollDate,epm.surescripts_unenroll_date as surescriptsUnenrollDate,epm.pharmacy_model as pharmacyModel,"
					+" case when pm.delete_ind='Y' then 'true' else 'false' end as isDeleted"
					+" FROM patient pa Left join pharmacy_mstr pm on (pm.pharmacy_id=pharmacy_code_1_id OR pm.pharmacy_id=pharmacy_code_2_id) LEFT JOIN erx_pharmacy_xref epx ON pm.pharmacy_id = epx.pharmacy_id"  
					+" LEFT JOIN erx_pharmacy_mstr epm  ON epm.ncpdp_id = epx.ncpdp_id"
					+" where pa.person_id='"+parameters.get("personId")+"'";

			String[] jsonMetaData = {"id","personId","practiceId","name","addressLine1","addressLine2","city","state","zip","phone","alternatePhone","alternatePhoneDescription","fax","chiefPharmacistName","allowsRxByFax","hasActiveErx","ncpdpId","surescriptsEnrollDate","surescriptsUnenrollDate","pharmacyModel","isDeleted"};

			try
			{
				ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
				Log.info("JSON LIST is :\n" +jsonList);
				status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
				softAssert.fail("Error in Test Script please look at logs");
			}
		}
		else
		{
			status = true;
		}

		return status;
	}
}

/*public class Chart_Persons_Pharmacies_GET extends BaseTest_obsl
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void chart_Persons_Pharmacies_GET()
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
							if (!(strResponse.contains("is not valid for Guid")||strResponse.contains("\"message\":") ||strResponse.contains("Date parameter is in an incorrect format.")))
							{
								String strQuery = " SELECT" 
										+" UPPER(CAST(pm.pharmacy_id AS CHAR(36))) as id,pa.practice_id as practiceId,pm.name as name,pm.address_line_1 as addressLine1,pm.address_line_2 as addressLine2,pm.city as city,pm.state as state"
										+" ,pm.zip as zip,pm.phone as phone,pm.alternate_phone as alternatePhone,pm.alt_phone_desc as alternatePhoneDescription,pm.fax as fax,pm.chief_pharm_name as chiefPharmacistName,"
										+" case when pm.rx_by_fax_ind='Y' then 'true' else 'false' end as allowsRxByFax,case when pm.active_erx_ind='Y' then 'true' else 'false' end as hasActiveErx,epm.ncpdp_id as ncpdpId"
										+" ,epm.surescripts_enroll_date as surescriptsEnrollDate,epm.surescripts_unenroll_date as surescriptsUnenrollDate,epm.pharmacy_model as pharmacyModel,"
										+" case when pm.delete_ind='Y' then 'true' else 'false' end as isDeleted"
										+" FROM patient pa Left join pharmacy_mstr pm on (pm.pharmacy_id=pharmacy_code_1_id OR pm.pharmacy_id=pharmacy_code_2_id) LEFT JOIN erx_pharmacy_xref epx ON pm.pharmacy_id = epx.pharmacy_id"  
										+" LEFT JOIN erx_pharmacy_mstr epm  ON epm.ncpdp_id = epx.ncpdp_id"
										+" where pa.person_id='"+personId+"'";
								try
								{
									Object obj = JsonPath.read(strResponse, "$..id");
									ArrayList<Object> jsonList = new ArrayList<>();       
									if (obj instanceof JSONArray)
									{
										for(int j = 0; j< ((JSONArray) obj).size(); j++)
										{
											jsonList.add((Object) JsonPath.read(strResponse,"$..id["+j+"]").toString().toUpperCase());
											jsonList.add(JsonPath.read(strResponse,"$..practiceId["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..name["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..addressLine1["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..addressLine2["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..city["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..state["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..zip["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..phone["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..alternatePhone["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..alternatePhoneDescription["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..fax["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..chiefPharmacistName["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..allowsRxByFax["+j+"]").toString());
											jsonList.add(JsonPath.read(strResponse,"$..hasActiveErx["+j+"]").toString());
											jsonList.add(JsonPath.read(strResponse,"$..ncpdpId["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..surescriptsEnrollDate["+j+"]").toString().replace("T", " "));
											jsonList.add(JsonPath.read(strResponse,"$..surescriptsUnenrollDate["+j+"]").toString().replace("T", " "));
											jsonList.add(JsonPath.read(strResponse,"$..pharmacyModel["+j+"]"));
											jsonList.add(JsonPath.read(strResponse,"$..isDeleted["+j+"]").toString());
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
						objHandler.setStrRequestStatus("Fail");
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