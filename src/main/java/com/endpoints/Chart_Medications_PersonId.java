package com.endpoints;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
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

public class Chart_Medications_PersonId extends BaseTest_obsl {
/*
* ********************************************************************
* Class Name						: Alrgy_Get_PerEncUnqID
* Created By						: Pavan
* Created Date						: 
* Modified Date					:
* Modified By						:
* Parameters						: 
* Test Case ID						:
* Test Case Author					: 
* Test Case Modified By			:
* Test Case Reviewed By			:
* Purpose							: Route will fetch the Medications with perID
*********************************************************************
*/
	SoftAssert softAssert = new SoftAssert();
	//public CloseableHttpClient httpClient;
	public CloseableHttpResponse httpResponse;
	@SuppressWarnings("null")
	@Test
	public void chart_Medications_PersonId()
	{

		boolean boolTestcaseStatus = true;
		HashMap<String, Object[]> hashMapResult = new HashMap<>();
		hashMapResult.put("0", new Object[] {"Parameters","RequestURI", "RequestHeaders","ResponseCode", "ResponseHeaders", "Response", "Status"});
		String strClassName = getClass().getSimpleName();

		try
		{

			
			RequestResponseHandler objHandler = new RequestResponseHandler();
			for(int i=1; i<=ExcelReaderWriter.getRowCount(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); i++)
			{
				String PersonID = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, i, 0);

				System.out.println(PersonID);
				if (PersonID.equalsIgnoreCase("null"))
					PersonID="";

				//pass strQueryString,Request Type and strPayLoad
				//HashMap<String, Object>mapRequestParrameters =mapRequestParrameters(strURL,"GET","",strURL.replace("personId", PersonID));
				
				Map<String, Object> mapRequestParameters = new HashMap<String, Object>();
				mapRequestParameters.put("strRequestType", "GET");
				mapRequestParameters.put("strPayLoad", "");
				//mapRequestParameters.put("strURI", BaseTest.mapURLRepo.get(strClassName));
				mapRequestParameters.put("strURI", BaseTest_obsl.mapURLRepo.get(strClassName).replace("personId", PersonID));
				mapRequestParameters.put("strQueryString", "");
				
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParameters);
				String strSQLQuery = "select pm.uniq_id as id,pm.person_id as personId,fm.brand_name as fdbName,pm.medication_name AS medicationName,"
						+" case when pm.generic_ok_ind='Y' then 'false' else 'true' end as isGenericSelected,fm.dose as dose,fm.route_desc as route,fm.dose_form_desc as doseForm,pm.start_date as originalStartDate,"
						+ " pm.start_date as startDate,pm.date_stopped as stopDate,pm.sig_desc as sigDescription,pm.enterprise_id as enterpriseId,pm.practice_id as practiceId,pm.enc_id as encounterId,"
						+ " pm.source_product_id as sourceProductId,pm.last_audit_type as lastAuditType,case when pm.reprndc_ind='Y' then 'true' else 'false' end as representativeNdcIndicator,"
						+ " case when pm.medid > 0 then 'true' when pm.medid is null then null else 'false' end as isHidden,"
						+ " case when pm.privacy_ind='Y' then 'true' else 'false' end as privacyIndicator,pm.medid as medicationId,fm.ndc_id as ndcId,"
						+ " case when pm.date_stopped is null then 'ACTIVE' when pm.date_stopped='' then 'ACTIVE' when pm.date_stopped > getdate() then 'ACTIVE' else 'INACTIVE' end as status"
						+ " from patient_medication pm inner join fdb_medication fm on pm.ndc_id=fm.ndc_id"
						+ " left outer join fdb_tall_man_name_mstr ftmnm on fm.medid=ftmnm.medid"
						+ " left outer join fdb_med_name_search_mstr fmnsm on pm.medid=fmnsm.medid"
						+ " where pm.person_id='"+PersonID+"'";

						System.out.println(strSQLQuery);
						

				//ResultSet rs =  DatabaseConnection.statementExecution(connNGA, strSQLQuery);
				if (objHandler!= null)
				{
					String strResponse = objHandler.getStrResponse();
					ValidateResults.validateStatusCode(objHandler , ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), getClass().getSimpleName(), i, 1));
					if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
					{
						if (!strResponse.isEmpty())
						{
							if (!(strResponse.contains("The request is invalid")))
							{
								try
								{
									ArrayList<Object> jsonList = new ArrayList<>();
									Object obj = JsonPath.read(strResponse, "$..id");
									if (obj instanceof JSONArray)
									{
										for(int j = 0; j< ((JSONArray) obj).size(); j++)
										{
											jsonList.add((Object)JsonPath.read(strResponse, "$..id["+j+"]").toString().toUpperCase());
											jsonList.add((Object)JsonPath.read(strResponse, "$..personId["+j+"]").toString().toUpperCase()); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..fdbName["+j+"]").toString()); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..medicationName["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..isGenericSelected["+j+"]").toString()); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..dose["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..route["+j+"]").toString().toUpperCase()); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..doseForm["+j+"]").toString().toUpperCase()); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..originalStartDate["+j+"]").toString().substring(0, 10).replace("-", "")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..startDate["+j+"]").toString().substring(0, 10).replace("-", "")); 
											if(JsonPath.read(strResponse, "$..stopDate["+j+"]")!=null){
												jsonList.add((Object)JsonPath.read(strResponse, "$..stopDate["+j+"]").toString().substring(0, 10).replace("-", "")); 
											}
											else
											{
												jsonList.add((Object)JsonPath.read(strResponse, "$..stopDate["+j+"]")); 
											}
											jsonList.add((Object)JsonPath.read(strResponse, "$..sigDescription["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..enterpriseId["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..practiceId["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..encounterId["+j+"]").toString().toUpperCase()); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..sourceProductId["+j+"]").toString().toUpperCase()); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..lastAuditType["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..representativeNdcIndicator["+j+"]").toString()); 
											jsonList.add(JsonPath.read(strResponse, "$..isHidden["+j+"]").toString()); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..privacyIndicator["+j+"]").toString()); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..medicationId["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..ndcId["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..status["+j+"]").toString().toUpperCase()); 
										}	
									}
									System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
									System.out.println("The JSON Response in a List: \n" + jsonList + "\n");
									ResultSet rs = DatabaseConnection.statementExecution(connNGA, strSQLQuery);
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
									}								} catch (Exception e) {
									e.printStackTrace();
								}
								hashMapResult.put(String.valueOf(i), new Object[]{"personId=" + PersonID,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
							}
							else
							{
								Log.info("It's a Bad Request");
								Log.info(objHandler.getStrResponse());
								hashMapResult.put(String.valueOf(i), new Object[]{"personId=" + PersonID,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
							}
						}
						else
						{
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							hashMapResult.put(String.valueOf(i), new Object[]{"personId=" + PersonID ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}
					
					else if(! (objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND))
					{
						Log.info("We have recieved the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, i, 2));
						softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, i, 2) + " ");
						Log.info(objHandler.getStrResponse());
						hashMapResult.put(String.valueOf(i), new Object[]{"personId=" + PersonID ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Pass");
					}
					else
					{
						softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server ");
						hashMapResult.put(String.valueOf(i), new Object[]{"personId=" + PersonID ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Fail");
					}
				}
				else
				{
					boolTestcaseStatus = false;
					log.info("The Request could not be sent as the Request Type is not a valid Http Method");
					softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
					hashMapResult.put(String.valueOf(i), new Object[]{"personId=" + PersonID ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
				}


				if (objHandler.getStrRequestStatus().contains("Fail"))
				{
					boolTestcaseStatus = false;
					softAssert.fail("The test case fails with the data : " + "PersonId=" + PersonID);	
				}
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
