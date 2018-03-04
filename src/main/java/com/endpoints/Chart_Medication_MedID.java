package com.endpoints;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

public class Chart_Medication_MedID extends BaseTest_obsl {
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
	 * Purpose							: Route will fetch the Medications with MEDID 
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


			Map<String, Object> mapRequestParrameters = new HashMap<String, Object>();
			mapRequestParrameters.put("strRequestType", "GET");
			mapRequestParrameters.put("strPayLoad", "");
			mapRequestParrameters.put("strQueryString", "");
			//mapRequestParrameters.put("strURI", strURI);

			RequestResponseHandler objHandler = new RequestResponseHandler();
			for(int i=1; i<=ExcelReaderWriter.getRowCount(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName ); i++)
			{
				String PersonID = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, i, 0);
				String MedID = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, i, 1);

				System.out.println(PersonID);
				if (Objects.equals(PersonID, null))//PersonID.equalsIgnoreCase("null"))
					PersonID="";
				//pa.audit_id,
				mapRequestParrameters.put("strURI", BaseTest_obsl.mapURLRepo.get(strClassName).replace("personId", PersonID).replace("med_id", MedID));
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);
				String strSQLQuery = "select top 1 pm.uniq_id as id,fm.brand_name as fdbName,pm.medication_name as medicationName, "
						+ "fm.dose as dose,fm.dose_form_desc as doseForm, fm.ndc_id as ndcId,fm.gcn,fm.route_desc as route,fm.hicl_seqno as hiclSequenceNumber "
						+ ",fm.gcn_seqno as gcnSequenceNumber,fm.dea_id as deaId,pm.medid as medicationId,pm.created_by as createdBy,pm.create_timestamp as createTimestamp, "
						+ "pm.create_timestamp_tz as createTimestampTimezone,fm.modified_by as modifiedBy,pm.modify_timestamp as modifyTimestamp, "
						+ "pm.modify_timestamp_tz as modifyTimestampTimezone,pm.person_id as personId,pm.enc_id as encounterId,pe.rendering_provider_id as encounterProviderId, "
						+ "pe.enc_timestamp as encounterTimestamp,pe.enc_timestamp_tz as encounterTimestampTimezone,pm.provider_id as providerId,pm.location_id, "
						+ "pm.practice_id as practiceId,p.practice_name as practiceName,pm.enterprise_id as enterpriseId,e.enterprise_name as enterpriseName, "
						+ "pm.start_date as startDate,pm.start_date as originalStartDate,pm.date_stopped as stopDate,pm.sig_codes as sigCodes, "
						+ "pm.rx_quanity as rxQuantity,pm.rx_refills as rxRefills,case when pm.generic_ok_ind = 'Y' then 'true' else 'false' end as isGenericAllowed, "
						+ "case when pm.samples_ind = 'Y' then 'true' else 'false' end as isSample,case when pm.dispense_as_writtn_ind = 'Y' then 'true' else 'false' end as dispenseAsWritten, "
						+ "case when pm.org_refills ='  '  then '0' else '1' end AS orgRefills,pm.date_last_refilled as lastRefillDate,pm.sig_desc as sigDescription, "
						+ "case when pm.prescribed_else_ind = 'N' then 'false' else 'true' end as isPrescribedElsewhere,pm.rx_units as rxUnits,pm.rx_comment as rxComment,pm.rx_special_instruction as rxSpecialInstruction,"
						+ "case when pm.print_spanish_ind ='1' then 'true' else 'false' end as printSpanishIndicator , "
						+ "case when pm.selected_generic_ind = '1' then 'true' else 'false'  end as isGenericSelected , "
						+ "pm.diagnosis_code_id as diagnosisCode,pm.diagnosis_code_id as diagnosisCode2,pm.diagnosis_code_id as diagnosisCode3, "
						+ "case when pm.reprndc_ind = 'N' then 'false' else 'true' end as representativeNdcIndicator,pm.prn_reason as prnReason, "
						+ "pm.formulary_id as formularyId,pm.Formu_Override_code as formulaOverrideCode,pm.Formu_Override_text asformulaOverrideText, "
						+ "pm.original_rx_ndc as originalRxNdc,pm.formu_status as formulaStatus,pm.dur_audit_mask as auditMask,pm.dur_audit_id as auditId, "
						+ "pm.send_audit_id as sendAuditId,pm.last_audit_type  as lastAuditType "
						+ ",pm.lot_number as lotNumber, "
						+ "case when pm.expiration_date =' ' then null else pm.expiration_date end as expirationDate, "
						+ "case when pm.prn_ind ='N' then 'false' else 'true' end as isPrn,pm.renew_parent_id as renewParentId,pm.ng_med_rx_unit_code as rxUnitCode, "
						+ "pm.old_uniq_id as oldId,pm.rx_renew_note as rxRenewNote,pm.refill_limit as refillLimit, "//pm.poeostrid as fdbDosageId, before pm.refill_limit as refillLimit
						+ "pm.refill_limit_date as refillLimitDate,pm.prescribed_else_location as prescribedElsewhereLocation,case when pm.prior_auth_ind = 'N' then 'false' else 'true' end as  priorAuthorizationIndicator, "
						+ "pm.prior_auth_id as  priorAuthorizationId,pm.prior_auth_date as priorAuthorizationDate, "
						+ "case when pm.privacy_ind='Y' then 'true' else 'false' end as privacyIndicator,pm.refills_left as refillsRemaining, "
						+ "pm.custom_dosage_order_id  as customDosageId,case when pm.auto_calc_ind ='Y' then 'true' else 'false' end as isAutoCalculated,"
						+ "pm.pbm_id as pbmId,pm.formu_data as formularyDetailData,pm.formu_summ_data as formularySummaryData,pm.source_product_id as sourceProductId,"
						+ "pm.supervising_provider_id as supervisingProviderId,case when pm.ineffective_med_ind = 'N' then 'false' else 'true' end as ineffectiveMedicationIndicator,pm.ped_order_id as pedOrderId,"
						+ "pm.e_coupon_notes as eCouponNote,pm.e_coupon_url as eCouponUrl, "         //--auditTimestamp,isErxDisabledByState,isFaxDisabledByState, "
						+ "case when pm.medid > 0 then 'true' when pm.medid is null then null else 'false' end as isHidden, " //--isSupply, 
						+ "case when pm.date_stopped is null then 'ACTIVE' when pm.date_stopped='' then 'ACTIVE' when pm.date_stopped > getdate() then 'ACTIVE' else 'INACTIVE' end as status "
						+ "from patient_medication pm inner join fdb_medication fm on pm.ndc_id=fm.ndc_id left outer join fdb_tall_man_name_mstr ftmnm on fm.medid=ftmnm.medid "
						+ "left outer join fdb_med_name_search_mstr fmnsm on pm.medid=fmnsm.medid left outer join patient_encounter pe on pm.person_id = pe.person_id "
						+ "left outer join enterprise e  on pm.enterprise_id = e.enterprise_id left outer join practice p on p.practice_id =  pm.practice_id "
						+ " where pm.person_id='"+PersonID+"'  AND pm.uniq_id = '"+MedID+"' ORDER BY pe.enc_timestamp";
						

				System.out.println(strSQLQuery);

				//ResultSet rs =  DatabaseConnection.statementExecution(connNGA, strSQLQuery);
				if (objHandler!= null)
				{
					String strResponse = objHandler.getStrResponse();
					ValidateResults.validateStatusCode(objHandler , ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), getClass().getSimpleName(), i, 2));
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
											jsonList.add((Object)JsonPath.read(strResponse, "$..fdbName["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..medicationName["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..dose["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..doseForm["+j+"]").toString().toUpperCase()); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..ndcId["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..gcn["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..route["+j+"]").toString().toUpperCase()); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..hiclSequenceNumber["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..gcnSequenceNumber["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..deaId["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..medicationId["+j+"]")); 
										//	jsonList.add((Object)JsonPath.read(strResponse, "$..isAvailable["+j+"]").toString()); 

											jsonList.add((Object)JsonPath.read(strResponse, "$..createdBy["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..createTimestamp["+j+"]").toString().replace("T", " ")); 

											jsonList.add((Object)JsonPath.read(strResponse, "$..createTimestampTimezone["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..modifiedBy["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..modifyTimestamp["+j+"]").toString().replace("T", " ")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..modifyTimestampTimezone["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..personId["+j+"]").toString().toUpperCase());
											jsonList.add((Object)JsonPath.read(strResponse, "$..encounterId["+j+"]").toString().toUpperCase());
											jsonList.add((Object)JsonPath.read(strResponse, "$..encounterProviderId["+j+"]").toString().toUpperCase());
											jsonList.add((Object)JsonPath.read(strResponse, "$..encounterTimestamp["+j+"]").toString().replace("T", " "));
											jsonList.add((Object)JsonPath.read(strResponse, "$..encounterTimestampTimezone["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..providerId["+j+"]").toString().toUpperCase());
											jsonList.add((Object)JsonPath.read(strResponse, "$..locationId["+j+"]").toString().toUpperCase());
											jsonList.add((Object)JsonPath.read(strResponse, "$..practiceId["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..practiceName["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..enterpriseId["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..enterpriseName["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..startDate["+j+"]").toString().substring(0, 10).replace("T", "").replace("-", ""));
											jsonList.add((Object)JsonPath.read(strResponse, "$..originalStartDate["+j+"]").toString().substring(0, 10).replace("T", "").replace("-", ""));
											if(JsonPath.read(strResponse, "$..stopDate["+j+"]")!=null){
												jsonList.add((Object)JsonPath.read(strResponse, "$..stopDate["+j+"]").toString().substring(0, 10).replace("T", "").replace("-", "")); 
											}
											else
											{
												jsonList.add((Object)JsonPath.read(strResponse, "$..stopDate["+j+"]")); 
											}
											jsonList.add((Object)JsonPath.read(strResponse, "$..sigCodes["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..rxQuantity["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..rxRefills["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..isGenericAllowed["+j+"]").toString());
											jsonList.add((Object)JsonPath.read(strResponse, "$..isSample["+j+"]").toString());
											jsonList.add((Object)JsonPath.read(strResponse, "$..dispenseAsWritten["+j+"]").toString());
											jsonList.add((Object)JsonPath.read(strResponse, "$..orgRefills["+j+"]"));
											if(JsonPath.read(strResponse, "$..lastRefillDate["+j+"]")!=null){
												jsonList.add((Object)JsonPath.read(strResponse, "$..lastRefillDate["+j+"]").toString().substring(0, 10).replace("-", "")); 
											}
											else
											{
												jsonList.add((Object)JsonPath.read(strResponse, "$..lastRefillDate["+j+"]")); 
											}
											jsonList.add((Object)JsonPath.read(strResponse, "$..sigDescription["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..isPrescribedElsewhere["+j+"]").toString());
											jsonList.add((Object)JsonPath.read(strResponse, "$..rxUnits["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..rxComment["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..rxSpecialInstruction["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..printSpanishIndicator["+j+"]").toString());
											jsonList.add((Object)JsonPath.read(strResponse, "$..isGenericSelected["+j+"]").toString());
											jsonList.add((Object)JsonPath.read(strResponse, "$..diagnosisCode["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..secondaryDiagnosisCode["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..tertiaryDiagnosisCode["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..representativeNdcIndicator["+j+"]").toString());
											jsonList.add((Object)JsonPath.read(strResponse, "$..prnReason["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..formularyId["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..formulaOverrideCode["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..formulaOverrideText["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..originalRxNdc["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..formulaStatus["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..auditMask["+j+"]"));
											jsonList.add((Object)JsonPath.read(strResponse, "$..auditId["+j+"]").toString().toUpperCase());
											////////////////////////
											if(JsonPath.read(strResponse, "$..sendAuditId["+j+"]")!=null){
												jsonList.add((Object)JsonPath.read(strResponse, "$..sendAuditId["+j+"]").toString().substring(0, 10).replace("-", "")); 
											}
											else
											{
												jsonList.add((Object)JsonPath.read(strResponse, "$..sendAuditId["+j+"]")); 
											}
											
											if(JsonPath.read(strResponse, "$..lastAuditType["+j+"]")!=null){
												jsonList.add((Object)JsonPath.read(strResponse, "$..lastAuditType["+j+"]").toString()); 
											}
											else
											{
												jsonList.add((Object)JsonPath.read(strResponse, "$..lastAuditType["+j+"]")); 
											}
											jsonList.add((Object)JsonPath.read(strResponse, "$..lotNumber["+j+"]").toString()); 
											if(JsonPath.read(strResponse, "$..expirationDate["+j+"]")!=null){
												jsonList.add((Object)JsonPath.read(strResponse, "$..expirationDate["+j+"]").toString()); 
											}
											else
											{
												jsonList.add((Object)JsonPath.read(strResponse, "$..expirationDate["+j+"]")); 
											}

											jsonList.add((Object)JsonPath.read(strResponse, "$..isPrn["+j+"]").toString()); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..renewParentId["+j+"]").toString().toUpperCase()); 
											System.out.println(jsonList.size());

											jsonList.add((Object)JsonPath.read(strResponse, "$..rxUnitCode["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..oldId["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..rxRenewNote["+j+"]")); 


										/*	if(JsonPath.read(strResponse, "$..fdbDosageId["+j+"]")!=null){
												jsonList.add((Object)JsonPath.read(strResponse, "$..fdbDosageId["+j+"]").toString().replace("-", "")); 
											}
											else
											{
												jsonList.add((Object)JsonPath.read(strResponse, "$..fdbDosageId["+j+"]")); 
											}*/
											
											
											jsonList.add((Object)JsonPath.read(strResponse, "$..refillLimit["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..refillLimitDate["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..prescribedElsewhereLocation["+j+"]")); 
											
											jsonList.add((Object)JsonPath.read(strResponse, "$..priorAuthorizationIndicator["+j+"]").toString()); 

											jsonList.add((Object)JsonPath.read(strResponse, "$..priorAuthorizationId["+j+"]")); 

											if(JsonPath.read(strResponse, "$..priorAuthorizationDate["+j+"]")!=null){
												jsonList.add((Object)JsonPath.read(strResponse, "$..priorAuthorizationDate["+j+"]").toString()); 
											}
											else
											{
												jsonList.add((Object)JsonPath.read(strResponse, "$..priorAuthorizationDate["+j+"]")); 
											}
											jsonList.add((Object)JsonPath.read(strResponse, "$..privacyIndicator["+j+"]").toString()); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..refillsRemaining["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..customDosageId["+j+"]").toString().toUpperCase()); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..isAutoCalculated["+j+"]").toString()); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..pbmId["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..formularyDetailData["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..formularySummaryData["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..sourceProductId["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..supervisingProviderId["+j+"]")); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..ineffectiveMedicationIndicator["+j+"]").toString()); 
											if(JsonPath.read(strResponse, "$..pedOrderId["+j+"]")!=null){
												jsonList.add((Object)JsonPath.read(strResponse, "$..pedOrderId["+j+"]").toString().replace("-", "")); 
											}
											else
											{
												jsonList.add((Object)JsonPath.read(strResponse, "$..pedOrderId["+j+"]")); 
											}	if(JsonPath.read(strResponse, "$..eCouponNote["+j+"]")!=null){
												jsonList.add((Object)JsonPath.read(strResponse, "$..eCouponNote["+j+"]").toString()); 
											}
											else
											{
												jsonList.add((Object)JsonPath.read(strResponse, "$..eCouponNote["+j+"]")); 
											}	if(JsonPath.read(strResponse, "$..eCouponUrl["+j+"]")!=null){
												jsonList.add((Object)JsonPath.read(strResponse, "$..eCouponUrl["+j+"]").toString()); 
											}
											else
											{
												jsonList.add((Object)JsonPath.read(strResponse, "$..eCouponUrl["+j+"]")); 
											}
											jsonList.add((Object)JsonPath.read(strResponse, "$..isHidden["+j+"]").toString()); 
											jsonList.add((Object)JsonPath.read(strResponse, "$..status["+j+"]").toString().toUpperCase()); 
										}	
									}
									System.out.println("The API Call executed is : \n" + mapRequestParrameters.get("strURI") + "\n");
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
								hashMapResult.put(String.valueOf(i), new Object[]{"$orderby=Description+asc&personId=" + PersonID,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
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
