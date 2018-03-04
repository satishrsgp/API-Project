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

public class Master_ProviderById_GET extends BaseTest_obsl
{


	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void master_ProviderById_GET()
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
				
			
				String strFinalURI = BaseTest_obsl.mapURLRepo.get(strClassName) +"/"+strProviderId;
				mapRequestParrameters.put("strURI", strFinalURI);
				
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
								String strSQLQuery="select pm.provider_id,ppm.enterprise_id,ppm.practice_id,pm.first_name,pm.last_name,pm.middle_name,pm.address_line_1,pm.address_line_2,pm.city,pm.state,pm.zip,pm.phone,pm.phone_ext,pm.fax,pm.degree, "
													+ "ppm.dea_nbr,pm.email_address,pm.national_provider_id,pm.other_lic_id,pm.other_lic_desc,ppm.lic_nbr,pm.description, "
													+ "case pm.delete_ind when 'n' then 'false' end as delete_ind, "
													+ "pm.note,pm.ssn,pm.salutory_name,pm.hosp_affiliations,pm.mobile_phone,pm.home_addr_line_1,pm.home_addr_line_2, "
													+ "pm.home_city,pm.home_state,pm.home_zip,pm.home_phone,pm.home_fax,pm.fax_extras,pm.default_tax_id "
													+ "from provider_mstr pm inner join provider_practice_mstr ppm on ppm.provider_id=pm.provider_id where ppm.provider_id='"+strProviderId+"'";
								
								@SuppressWarnings("unused")
								Object objProviderId = JsonPath.read(strResponse, "$.id");
									ArrayList<Object> jsonListProviderDetails = new ArrayList<Object>();

									
									if(JsonPath.read(strResponse, "$.id")!=null){
										jsonListProviderDetails.add((Object)JsonPath.read(strResponse, "$.id").toString().toUpperCase());
									}
									else
									{
										jsonListProviderDetails.add((Object)JsonPath.read(strResponse, "$.id"));
									}

									
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.enterpriseId"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.practiceId"));
										
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.firstName"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.lastName"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.middleName"));
										
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.providerAddressLine1"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.providerAddressLine2"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.providerCity"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.providerState"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.providerZip"));
								
										
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.providerPhone"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.providerPhoneExtension"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.providerFax"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.degree"));
										
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.deaNumber"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.emailAddress"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.nationalProviderId"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.otherLicenseId"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.otherLicenseDescription"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.stateLicenseNumber"));
										
										
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.description"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.isDeleted").toString());
									
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.note"));
									
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.ssn"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.salutoryName"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.hospitalAffiliations"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.mobilePhone"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.homeAddressLine1"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.homeAddressLine2"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.homeCity"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.homeState"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.homeZip"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.homePhone"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.homeFax"));
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.faxExtras"));
									
										jsonListProviderDetails.add(JsonPath.read(strResponse, "$.defaultTaxId"));
										
										


									ResultSet rs = DatabaseConnection.statementExecution(connNGA, strSQLQuery);
									ValidateResults.resultsvalidation(jsonListProviderDetails, rs, objHandler);
									hashMapResult.put(String.valueOf(k), new Object[]{"?ProviderId=" + strProviderId,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});

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
							hashMapResult.put(String.valueOf(k), new Object[]{"?strProviderId=" + strProviderId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
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
					softAssert.fail("Test case fails with data: "+"?strProviderId=" + strProviderId);
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
