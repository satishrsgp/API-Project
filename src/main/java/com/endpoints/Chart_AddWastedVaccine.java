package com.endpoints;



import java.util.ArrayList;
import java.util.HashMap;

import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_AddWastedVaccine {
	public static Boolean chart_AddWastedVaccine_Test(HashMap<String, String> parameters, String strResponse,HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;


		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(Math.abs(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")))  == 1)
			{
				try
				{
					String strSQLQuery = "select top 1 iov.cvx_code as CVxCode, "
							+"iov.vaccine_desc as VaccineDescription, "
							+"iov.lot_num as LotNumber, "
							+"iov.cpt4_code as Cpt4Code, "
							+"iov.manufacturer_name as ManufacturerName, "   
							+"iov.manufacturer_nbr as ManufacturerNumber,  "
							+ "iov.strength as Strength, "
							+ "iov.units as Units, "
							+ "iov.units_code as UnitsCode, "
							+ "iov.dose as Dose, "
							+ "iov.route as Route, "
							+ "iov.route_code as RouteCode, "
							+ "iov.site as Site, "
							+ "iov.site_code as SiteCode, "
							+ "iov.brand_name as BrandName, "
							+ "ivw.wasted_reason as WastedReason, "
							+  "ivw.wasted_date as WastedDate, "
					//		+ "iov.units_code as BillingUnits, "
							+ "iov.record_source as RecordSource "
							+ "from imm_nor imn Inner join imm_order_vaccines iov on iov.order_num = imn.order_num inner join imm_vaccine_waste ivw on ivw.order_vaccine_id = iov.order_vaccine_id "
							+ " WHERE imn.person_id = '"+parameters.get("personId")+"' and imn.enc_id = '"+parameters.get("encounterId")+"' and imn.order_num = '"+parameters.get("orderId")+"' and ivw.order_vaccine_id = '"+parameters.get("vaccineId")+"' order by ivw.create_timestamp desc";
					
				//	ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));

					ArrayList<Object> jsonList = new ArrayList<Object>();
					jsonList.add(parameters.get("CvxCode"));
					jsonList.add(parameters.get("VaccineDescription").toString());
					//jsonList.add(Objects.equals(parameters.get("CVxCode"), "") ? null : (parameters.get("CVxCode").toString()));		
					//jsonList.add(Objects.equals(parameters.get("VaccineDescription"), "") ? null : (parameters.get("VaccineDescription").toString()));	
					jsonList.add(Objects.equals(parameters.get("LotNumber"), "") ? null : (parameters.get("LotNumber").toString()));	
					jsonList.add(Objects.equals(parameters.get("Cpt4Code"), "") ? null : (parameters.get("Cpt4Code").toString()));	
					jsonList.add(Objects.equals(parameters.get("ManufacturerName"), "") ? null : (parameters.get("ManufacturerName").toString()));		
					jsonList.add(Objects.equals(parameters.get("ManufacturerNumber"), "") ? null : (parameters.get("ManufacturerNumber").toString()));	
					//jsonList.add(get(parameters.get("Strength").toString());	
					jsonList.add(parameters.get("Strength").toString());
					jsonList.add(Objects.equals(parameters.get("Units"), "") ? null : (parameters.get("Units").toString()));	
				
					jsonList.add(Objects.equals(parameters.get("UnitsCode"), "") ? null : (parameters.get("UnitsCode").toString()));	
					jsonList.add(Objects.equals(parameters.get("Dose"), "") ? null : (parameters.get("Dose").toString().concat("0")));	
					jsonList.add(Objects.equals(parameters.get("Route"), "") ? null : (parameters.get("Route").toString()));	
					jsonList.add(Objects.equals(parameters.get("RouteCode"), "") ? null : (parameters.get("RouteCode").toString().toUpperCase()));	
					jsonList.add(Objects.equals(parameters.get("Site"), "") ? null : (parameters.get("Site").toString()));	
					jsonList.add(Objects.equals(parameters.get("SiteCode"), "") ? null : (parameters.get("SiteCode").toString().toUpperCase()));	
					jsonList.add(Objects.equals(parameters.get("BrandName"), "") ? null : (parameters.get("BrandName").toString().toUpperCase()));	
				
					jsonList.add(Objects.equals(parameters.get("WastedReason"), null) ? null : (parameters.get("WastedReason").toString().split("T")[0]).replace("-", ""));
					jsonList.add(Objects.equals(parameters.get("WastedDate"), null) ? null : (parameters.get("WastedDate").toString().split("T")[0]));
					//jsonList.add(parameters.get("VaccineInventoryId").toString());
					//jsonList.add(Objects.equals(parameters.get("VaccineInventoryId"), "") ? null : (parameters.get("VaccineInventoryId").toString()));	
				//	jsonList.add(Objects.equals(parameters.get("BillingUnits"), "") ? null : (parameters.get("BillingUnits").toString()));	
					
					jsonList.add(Objects.equals(parameters.get("RecordSource"), "") ? null : (parameters.get("RecordSource").toString()));	
					//jsonList.add(parameters.get("supportRole").toString());
					//jsonList.add(Objects.equals(parameters.get("fax"), "") ? null : (parameters.get("fax").toString().toLowerCase()));	
					
					//jsonList.add(parameters.get("zip").toString());
					//jsonList.add(Objects.equals(parameters.get("VerbalOrderIndicator"), "false") ? "N" : "Y");
					//jsonList.add(parameters.get("AllergiesReviewed").toString());
					//if (get.(AllergiesReviewed) =="egg")
				


					status = ValidateResults.resultsvalidation(jsonList, DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
					//status = ValidateResults.resultValidation(jsonList, listDatabase);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
					softAssert.fail("Error in Test Script please look at logs");
				}
				/*String strQuery1 = "select "
                                                                                    + "enterprise_id as enterpriseId,practice_id as practiceId,person_id as personId,case_id as caseId,casemgt_case_id as caseManagementCaseId, "
                                                                                    + "parent_enc_id as parentEncounterId,group_enc_id as groupEncounterId,enc_timestamp as timestamp,Lock_ind as isLocked, "
                                                                                    + "enc_timestamp_tz as timestampTimezone,rendering_provider_id as renderingProviderId,locked_by as lockedBy,lock_timestamp as lockTimestamp, "
                                                                                    + "lock_timestamp_tz as lockTimestampTimezone,billable_timestamp as billableTimestamp,billable_timestamp_tz as billableTimestampTimezone, "
                                                                                    + "clinical_ind as isClinical,billable_ind as isBillable,optical_ind as isOptical,location_id as locationId,remarks as remarks, "
                                                                                    + "enc_status as status,source_product_id as sourceProductId,site_id as siteId,guar_id as guarantorId,guar_type as guarantorType, "
                                                                                    + "cob1_payer_id as cob1PayerId,cob1_insured_person_id as cob1InsuredPersonId,cob1_person_payer_id as cob1PersonPayerId,onset_date as onsetDate, "
                                                                                    + "onset_time as onsetTime,admit_date as admitDate,discharge_date as dischargeDate,refer_provider_id as referringProviderId,refer_provider_name as referringProviderName "
                                                                                    + "From Patient_encounter "
                                                                                    + "where person_id='"+parameters.get("personId")+"' order by create_timestamp desc";
                                                    ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
                                                    ArrayList<Object> listTestData  = new ArrayList<>();
                                                    for(Map.Entry<String, Object> entry1 : mapPostDetails.entrySet())
                                                    {
                                                                    for(Map.Entry<String, String> entry2 : parameters.entrySet())
                                                                    {
                                                                                    if(Objects.equals(entry2.getKey(), entry1.getKey()))
                                                                                                    listTestData.add(entry1.getValue());
                                                                    }              
                                                    }
                                                    status = ValidateResults.resultValidation(listTestData, listDatabase);
				 */          

			}
			else
			{
				Log.info("The Difference between records count before and after post is more than one, please reexcute once more");
				softAssert.fail("The Difference between records count before and after post is more than one. Please reexecute once more");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}

}
