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
 * Class Name						: Provider_Favorites_GET
 * Created By						: Guruprasad
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: providerId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the favorites associated to a provider based on the providerId
 * ******************************************************************
 */

public class Provider_Favorites_GET extends BaseTest_obsl
{
	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void provider_Favorites_GET()
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
		String strQuery="";
		boolean boolTallman = false;

		RequestResponseHandler objHandler = new RequestResponseHandler();
		String strSQLTallManTrue = " o.order_id as id,pg.provider_id as providerId,o.order_group_id as groupId,og.group_name as groupName,o.medid as medicationId,o.ndc_id as ndcId,fmn.med_name_id as medicationNameId,"
				+" ISNULL(t.tm_alt_med_name_desc,m.med_name)as medicationName,"
				//+" ISNULL(t.tm_alt_medid_desc,m.medid_desc) as medicationDescription,"
				+" m.med_strength as medicationStrength,"
				+" m.med_strength_uom as medicationStrengthUnit,m.gcn_seqno as gcnSequenceNumber,CASE m.generic_medid WHEN 0 THEN 'true'ELSE 'false' END AS isGeneric,m.generic_medid as genericMedicationId,"
				+" g.med_name AS genericName,dfm.med_dosage_form_id as medicationDosageFormId,m.med_dosage_form_desc as medicationDosageFormDescription,mr.med_route_id as medicationRouteId,"
				+" mr.med_route_desc as medicationRouteDescription,o.sig_id as sigId,o.sig_desc as sigDescription,ISNULL(m.med_ref_dea_class_code, 0) AS deaId,f.hicl_seqno as hiclSequenceNumber,"
				+" case when o.custom_sig_ind='Y' then 'true' else 'false' end as isCustomSig,o.refills as refills,o.quantity as quantity,o.duration as duration,o.rx_units as rxUnits,o.rx_comment as rxComment,"
				+" case when o.generic_ok_ind='Y' then 'true' else 'false' end as isGenericOk,case when o.prn_ind='Y' then 'true' else 'false' end as isPrn,"
				+" case when o.limit_renewal='Y' then 'true' else 'false' end as limitRenewal,case when o.pediatric_sig_ind='Y' then 'true' else 'false' end as isPediatricSig" 
				+" FROM provider_order_mstr o INNER JOIN fdb_med_name_search_mstr m ON o.medid = m.medid "
				+" INNER JOIN gcnseqnotbl f  ON m.gcn_seqno = f.gcn_seqno "
				+" INNER JOIN provider_order_group_xref og ON o.order_group_id = og.order_group_id" 
				+" INNER JOIN fdb_med_name_mstr fmn ON fmn.med_name = m.med_name "
				+" LEFT OUTER JOIN fdb_med_name_search_mstr g ON g.medid = m.generic_medid" 
				+" INNER JOIN provider_order_group_xref pg ON o.order_group_id = pg.order_group_id" 
				+" INNER JOIN fdb_med_route_mstr mr ON mr.med_route_id = m.med_route_id "
				+" INNER JOIN fdb_med_dosage_form_mstr dfm ON dfm.med_dosage_form_desc = m.med_dosage_form_desc" 
				+" LEFT JOIN  fdb_tall_man_name_mstr t on t.medid = m.medid "
				+" LEFT JOIN  pdr_fdb_prod_list p on CONVERT(VARCHAR, o.medid) = p.term_code and p.term_type = 'FDB_MEDID'" ;
		String strSQLTallManFalse = " o.order_id as id,pg.provider_id as providerId,o.order_group_id as groupId,og.group_name as groupName,o.medid as medicationId,o.ndc_id as ndcId,fmn.med_name_id as medicationNameId,"
				+" m.med_name as medicationName,"
				//+"m.medid_desc as medicationDescription,"
				+" m.med_strength as medicationStrength,"
				+" m.med_strength_uom as medicationStrengthUnit,m.gcn_seqno as gcnSequenceNumber,CASE m.generic_medid WHEN 0 THEN 'true'ELSE 'false' END AS isGeneric,m.generic_medid as genericMedicationId,"
				+" g.med_name AS genericName,dfm.med_dosage_form_id as medicationDosageFormId,m.med_dosage_form_desc as medicationDosageFormDescription,mr.med_route_id as medicationRouteId,"
				+" mr.med_route_desc as medicationRouteDescription,o.sig_id as sigId,o.sig_desc as sigDescription,ISNULL(m.med_ref_dea_class_code, 0) AS deaId,f.hicl_seqno as hiclSequenceNumber,"
				+" case when o.custom_sig_ind='Y' then 'true' else 'false' end as isCustomSig,o.refills as refills,o.quantity as quantity,o.duration as duration,o.rx_units as rxUnits,o.rx_comment as rxComment,"
				+" case when o.generic_ok_ind='Y' then 'true' else 'false' end as isGenericOk,case when o.prn_ind='Y' then 'true' else 'false' end as isPrn,"
				+" case when o.limit_renewal='Y' then 'true' else 'false' end as limitRenewal,case when o.pediatric_sig_ind='Y' then 'true' else 'false' end as isPediatricSig" 
				+" FROM provider_order_mstr o INNER JOIN fdb_med_name_search_mstr m ON o.medid = m.medid "
				+" INNER JOIN gcnseqnotbl f  ON m.gcn_seqno = f.gcn_seqno "
				+" INNER JOIN provider_order_group_xref og ON o.order_group_id = og.order_group_id" 
				+" INNER JOIN fdb_med_name_mstr fmn ON fmn.med_name = m.med_name "
				+" LEFT OUTER JOIN fdb_med_name_search_mstr g ON g.medid = m.generic_medid" 
				+" INNER JOIN provider_order_group_xref pg ON o.order_group_id = pg.order_group_id" 
				+" INNER JOIN fdb_med_route_mstr mr ON mr.med_route_id = m.med_route_id "
				+" INNER JOIN fdb_med_dosage_form_mstr dfm ON dfm.med_dosage_form_desc = m.med_dosage_form_desc" 
				+" LEFT JOIN  fdb_tall_man_name_mstr t on t.medid = m.medid "
				+" LEFT JOIN  pdr_fdb_prod_list p on CONVERT(VARCHAR, o.medid) = p.term_code and p.term_type = 'FDB_MEDID'" ;
		try
		{
			for(int k=1; k<=ExcelReaderWriter.getRowCount(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{
				String providerId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 0);
				if (providerId.equalsIgnoreCase("null"))
					providerId="";

				mapRequestParameters.put("strURI", BaseTest_obsl.mapURLRepo.get(strClassName).replace("providerId", providerId));
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParameters);

				if (objHandler != null)
				{
					String strResponse = objHandler.getStrResponse();
					ValidateResults.validateStatusCode(objHandler, ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
					if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
					{
						if (!strResponse.isEmpty())
						{
							if (!(strResponse.contains("is not valid for Guid.")))
							{
								if(mapUserSettings.get("item_value")!= null)
								{
									if(mapUserSettings.get("item_value").toString().equalsIgnoreCase("True") || mapUserSettings.get("item_value").toString().equalsIgnoreCase("Y"))
										boolTallman = true;
								}
								else if(mapPracticeSettings.get("display_tallman") != null)
								{
									if(mapPracticeSettings.get("display_tallman").toString().equalsIgnoreCase("Y"))
										boolTallman = true;
								}

								if(strResponse.contains("skip=25"))
								{
									if(boolTallman)
									{
										strQuery = "Select distinct top 25 "+ strSQLTallManTrue
												+" WHERE pg.provider_id='"+providerId+"' and o.pediatric_sig_ind = 'N' "
												+" ORDER BY  8,28,19";
									}
									else
									{
										strQuery = "Select distinct top 25 "+ strSQLTallManFalse
												+" WHERE pg.provider_id='"+providerId+"' and o.pediatric_sig_ind = 'N' "
												+" ORDER BY  8,28,19";
									}
								}
								else
								{
									if(boolTallman)
									{
										strQuery = "Select distinct "+ strSQLTallManTrue
												+" WHERE pg.provider_id='"+providerId+"' and o.pediatric_sig_ind = 'N' "
												+" ORDER BY  8,28,19";
									}
									else
									{
										strQuery = "Select distinct "+ strSQLTallManFalse
												+" WHERE pg.provider_id='"+providerId+"' and o.pediatric_sig_ind = 'N' "
												+" ORDER BY  8,28,19";
									}
								}

								try
								{
									Object obj = JsonPath.read(strResponse, "$..id");
									ArrayList<Object> jsonList = new ArrayList<>();
									if(obj instanceof JSONArray)
									{
										for(int j=0; j< ((JSONArray)obj).size(); j++)
										{
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].id").toString().toUpperCase());
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].providerId").toString().toUpperCase());
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].groupId").toString().toUpperCase());
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].groupName"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].medicationId"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].ndcId"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].medicationNameId"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].medicationName"));
											//jsonList.add(JsonPath.read(strResponse, "$.["+j+"].medicationDescription"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].medicationStrength"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].medicationStrengthUnit"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].gcnSequenceNumber"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].isGeneric").toString());
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].genericMedicationId"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].genericName"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].medicationDosageFormId"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].medicationDosageFormDescription"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].medicationRouteId"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].medicationRouteDescription"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].sigId"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].sigDescription"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].deaId"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].hiclSequenceNumber"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].isCustomSig").toString());
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].refills"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].quantity"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].duration"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].rxUnits"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].rxComment"));
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].isGenericOk").toString());
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].isPrn").toString());
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].limitRenewal").toString());
											jsonList.add(JsonPath.read(strResponse, "$.["+j+"].isPediatricSig").toString());
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
								hashMapResult.put(String.valueOf(k), new Object[]{"providerId=" + providerId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
							}
							//if the request is treated as invalid
							else
							{
								System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
								Log.info("It's a Bad Request");
								Log.info(objHandler.getStrResponse());
								System.out.println("It's a Bad Request \n");
								hashMapResult.put(String.valueOf(k), new Object[]{"providerId=" + providerId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
							}
						}
						//if response is empty but expected and actual response codes are same
						else
						{
							System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							System.out.println("It's a Bad Request \n");
							hashMapResult.put(String.valueOf(k), new Object[]{"providerId=" + providerId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}
					//if actual status code is not equal to expected status code
					else if(! (objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND))
					{
						Log.info("We have recieved the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
						softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2) + " ");
						Log.info(objHandler.getStrResponse());
						hashMapResult.put(String.valueOf(k), new Object[]{"providerId=" + providerId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Pass");
					}
					else
					{
						softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server ");
						hashMapResult.put(String.valueOf(k), new Object[]{"providerId=" + providerId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Fail");
					}
				}
				//if objHandler is null meaning bad type of request method
				else
				{
					System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
					Log.info("It's a Bad Request");
					Log.info(objHandler.getStrResponse());
					System.out.println("It's a Bad Request \n");
					hashMapResult.put(String.valueOf(k), new Object[]{"providerId=" + providerId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
				}

				if (objHandler.getStrRequestStatus().contains("Fail"))
				{
					boolTestcaseStatus = false;
					softAssert.fail("Test case fails with data: "+"providerId=" + providerId);
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