
package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import org.testng.asserts.SoftAssert;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_UpdateLabOrder
{
	public static Boolean chart_UpdateLabOrder(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
									+ "from lab_nor " 
									+ "Where person_id = '"+parameters.get("personId")+"' and order_num ='"+parameters.get("orderId")+"' and enterprise_id = "+System.getProperty("LoggedInEnterpriseId")+" and practice_id = "+System.getProperty("LoggedInPracticeId")+"";
									
			
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "Select LN.order_num as id,LN.test_desc as testDescription,LN.enc_id as encounterId,LN.person_id as personId,LN.ufo_num as ufoNumber,LN.generated_by as generatedBy,"
							+ "LN.order_type as orderType,LN.test_location as testLocation,LN.ordering_provider as orderingProvider,LN.enc_timestamp as encounterTimestamp,LN.create_timestamp as orderDate,"
							+ "LN.create_timestamp_tz as orderDateTimezone,LN.sign_off_date as signOffDate,LN.sign_off_person as signOffPerson,LN.test_status as testStatus,LN.ngn_status as nextgenStatus,"
							+ "LN.lab_id as labId,case when LN.signoff_comments_ind = 'N' then 'false' else 'true' end as signoffCommentsIndicator,case when LN.documents_ind = 'N' then 'false' else 'true' "
							+ "end as documentsIndicator, case LN.order_control when 'NW' then 'New' when 'CA' then 'Cancel' when 'RP' then 'Replace' when 'XO' then 'Change'when 'HD' then 'Hold' "
							+ "end as orderControl,case LN.order_priority when 'R' then 'Routine' when 'A' then 'ASAP' when 'S' then 'STAT' when 'C' then 'Callback' when 'T' then 'TimingCritial' "
							+ "end as orderPriority,LN.time_entered as timeEntered,LN.spec_action_code as specimenActionCode,LN.billing_type as billingType,LN.clinical_info as clinicalInformation,"
							+ "LN.cancel_reason as cancelReason,LN.intrf_msg as intrfMessage,LN.practice_id as practiceId,LN.enterprise_id as enterpriseId,Case when LN.delete_ind = 'N' "
							+ "then 'false' else 'true' end as isDeleted,PM.description as providerDisplayName,LM.location_name as locationName,LM.address_line_1 as addressLine1,LM.address_line_2 as addressLine2,"
							+ "LM.city as city,LM.state as state,LM.zip as zip,LM.phone as phone,LM.fax as fax,UM.first_name as firstName,UM.last_name as lastName,UM.mi as middleInitial,OS.future_ind as isFutureOrder,OS.start_date as startDate,"
							+ "OS.nextdue_date as nextDueDate, expected_rslt_date, general_comment,order_comment,patient_comment,case when ordered_elsewhere_ind='N' then 'false' else 'true' end as isOrderedElseWhere , "
							+ "case when completed_ind='N' then 'false' else 'true' end as isCompleted  From lab_nor LN "
							+ "Left outer join provider_mstr PM on LN.ordering_provider = PM.provider_id Left outer join location_mstr LM on LN.test_location = location_id Left outer join user_mstr UM on LN.sign_off_person = UM.user_id "
							+ "Left outer join order_schedule OS on LN.order_num = OS.order_id where LN.delete_ind<>'Y' AND LN.person_id ='"+parameters.get("personId")+"' AND LN.order_num = '"+parameters.get("orderId")+"' "
							+ "AND LN.enterprise_id = "+System.getProperty("LoggedInEnterpriseId")+" AND LN.practice_id = "+System.getProperty("LoggedInPracticeId")+"";
					
					//ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
					ArrayList<Object> jsonList  = new ArrayList<>();
					 	jsonList.add(parameters.get("id").toString().toUpperCase());
		                jsonList.add(parameters.get("testDescription").toString());
		                jsonList.add(parameters.get("encounterId").toString().toUpperCase());
		                jsonList.add(parameters.get("personId").toString().toUpperCase());
		                
		                jsonList.add(parameters.get("ufoNumber").toString());
		                jsonList.add(parameters.get("generatedBy").toString());
		                jsonList.add(parameters.get("orderType").toString());
		                jsonList.add(parameters.get("testLocation").toString().toUpperCase());
		                
		                jsonList.add(parameters.get("orderingProvider").toString().toUpperCase());
		                jsonList.add(parameters.get("encounterTimestamp").toString().replace("T", " "));
		                jsonList.add(parameters.get("orderDate").toString().replace("T", " "));
		                jsonList.add(parameters.get("orderDateTimezone").toString());
		                
		                jsonList.add(parameters.get("signOffDate").toString());
		                jsonList.add(parameters.get("signOffPerson").toString());
		                jsonList.add(parameters.get("testStatus").toString());
		                jsonList.add(parameters.get("nextgenStatus").toString());
		                
		                jsonList.add(parameters.get("labId").toString());
		                jsonList.add(parameters.get("signoffCommentsIndicator").toString());
		                jsonList.add(parameters.get("documentsIndicator").toString());
		                jsonList.add(parameters.get("orderControl").toString());
		                
		                jsonList.add(parameters.get("orderPriority").toString());
		                jsonList.add(parameters.get("timeEntered").toString().replace("T", " "));
		                jsonList.add(parameters.get("specimenActionCode").toString());
		                jsonList.add(parameters.get("billingType").toString());
		                
		                jsonList.add(Objects.equals(parameters.get("clinicalInformation"), "null") ? null: parameters.get("clinicalInformation"));
		                jsonList.add(Objects.equals(parameters.get("cancelReason"), "null") ? null: parameters.get("cancelReason"));
		                jsonList.add(Objects.equals(parameters.get("intrfMessage"), "null") ? null: parameters.get("intrfMessage"));
		                jsonList.add(parameters.get("practiceId").toString());
		                
		                jsonList.add(parameters.get("enterpriseId").toString());
		                jsonList.add(parameters.get("isDeleted").toString());
		                jsonList.add(parameters.get("providerDisplayName").toString());
		                jsonList.add(parameters.get("locationName").toString());
		                
		                jsonList.add(parameters.get("addressLine1").toString());
		                jsonList.add(parameters.get("addressLine2").toString());
		                jsonList.add(parameters.get("city").toString());
		                jsonList.add(parameters.get("state").toString());
		                
		                jsonList.add(parameters.get("zip").toString());
		                jsonList.add(parameters.get("phone").toString());
		                jsonList.add(parameters.get("fax").toString());
		                jsonList.add(parameters.get("firstName").toString());
		                
		                jsonList.add(parameters.get("lastName").toString());
		                jsonList.add(Objects.equals(parameters.get("middleInitial"), "null") ? null: parameters.get("middleInitial"));
		                jsonList.add(Objects.equals(parameters.get("isFutureOrder"), "null") ? null: parameters.get("isFutureOrder"));
		                jsonList.add(Objects.equals(parameters.get("startDate"), "null") ? null: parameters.get("startDate"));
		                
		                jsonList.add(Objects.equals(parameters.get("nextDueDate"), "null") ? null: parameters.get("nextDueDate"));
		                jsonList.add(parameters.get("expectedResultDate").toString().replace("T", " "));
		                jsonList.add(parameters.get("generalComment").toString());
		                jsonList.add(parameters.get("orderComment").toString());
		                
		                jsonList.add(parameters.get("patientComment").toString());
		                jsonList.add(parameters.get("isOrderedElseWhere").toString());
		                jsonList.add(parameters.get("isCompleted").toString());
		                
		                
					Log.info("Parameters:"+parameters);
					Log.info("TestData:\n"+jsonList);
					//Log.info("DBData:\n"+listDatabase);
					
					status = ValidateResults.resultsvalidation(jsonList,DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery1));
				}
				else
				{
					Log.error("LabOrder with person_id = '"+parameters.get("personId")+"' and order_num ='"+parameters.get("orderId")+"', was not updated correctly-modified_by is not updated with logged in user's credentials.");
			//		Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("LabOrder with person_id = '"+parameters.get("personId")+"' and order_num ='"+parameters.get("orderId")+"', was not updated correctly-modified_by is not updated with logged in user's credentials.");
				}
			}
			else
			{
				Log.error("LabOrder with person_id = '"+parameters.get("personId")+"' and order_num ='"+parameters.get("orderId")+"', was not updated correctly-modified_by is not updated with logged in user's credentials.");
				softAssert.fail("LabOrder with person_id = '"+parameters.get("personId")+"' and order_num ='"+parameters.get("orderId")+"', was not updated correctly-modified_by is not updated with logged in user's credentials.");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}


}

