package com.endpoints;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_SetOrderSchedule {
	public static Boolean chart_SetOrderSchedule_Test(HashMap<String, String> parameters, String strResponse,HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;


		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(Math.abs(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")))  == 1)
			{
				try
				{
					String strSQLQuery = "select "
							+ "OS.future_ind as scheduleType, "
							+ "cast (cast(OS.start_date as date) as varchar ) as StartDate, "
							+ "cast (cast(OS.end_date as date) as varchar ) as EndDate, "
					//		+ "OS.start_date as StartDate,"
					//		+ "OS.end_date as EndDate,"
							+ "OS.interval as Interval, "
							+ "OS.timespan as IntervalMode, "   
							+ "OS.auto_release_ind as IsAutoReleasable "
							+  "from order_schedule OS inner join lab_nor LN on LN.order_num = OS.order_id "
							+  " WHERE LN.person_id = '"+parameters.get("personId")+"' and OS.order_id = '"+parameters.get("orderId")+"' ";				
			//		ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));

					ArrayList<Object> jsonList = new ArrayList<Object>();
					String IntervalMode = parameters.get("IntervalMode");
					//jsonList.add(parameters.get("ScheduleType").toString());
					jsonList.add(Objects.equals(parameters.get("ScheduleType"), "recurring") ? "R" : "F");
					jsonList.add(Objects.equals(parameters.get("StartDate"), null) ? null : parameters.get("StartDate").toString().substring(0,10));
				jsonList.add(Objects.equals(parameters.get("EndDate"), null) ? null : (parameters.get("EndDate").toString()).substring(0,10));
					//jsonList.add(parameters.get("Interval"), "") ? null : (parameters.get("IntervalMode").toString()));
					jsonList.add(Objects.equals(parameters.get("Interval"), "null") ? null : (parameters.get("Interval").toString()));	
					jsonList.add(IntervalMode.contains("Daily")? "D":IntervalMode.contains("Weekly")?"W":IntervalMode.contains("Monthly")?"M":IntervalMode.contains("Yearly")?"Y":IntervalMode.contains("Today")?"T":IntervalMode.contains("None")?"n":"");				
					//jsonList.add(Objects.equals(parameters.get("IntervalMode"), "") ? null : (parameters.get("IntervalMode").toString()));	
					jsonList.add(Objects.equals(parameters.get("IsAutoReleasable"), "false") ? "N" : "Y");
					
				
					//jsonList.add(parameters.get("state").toString());
					
					//jsonList.add(parameters.get("zip").toString());
					//jsonList.add(Objects.equals(parameters.get("VerbalOrderIndicator"), "false") ? "N" : "Y");
					//jsonList.add(parameters.get("AllergiesReviewed").toString());
					



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
