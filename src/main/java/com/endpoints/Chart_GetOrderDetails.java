package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetOrderDetails 
{

	public static Boolean Chart_GetOrderDetails_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "Select "
						+ "LN.order_num as id,"
						+ "LN.test_desc as testDescription,"
						+ "LN.enc_id as encounterId,"
						+ "LN.person_id as personId,"
						+ "LN.ufo_num as ufoNumber,"
						+ "LN.generated_by as generatedBy,"
						+ "LN.order_type as orderType,"
						+ "LN.test_location as testLocation,"
						+ "LN.ordering_provider as orderingProvider,"
						+ "LN.enc_timestamp as encounterTimestamp,"
						+ "LN.create_timestamp as orderDate,"
						+ "LN.create_timestamp_tz as orderDateTimezone,"
						+ "LN.sign_off_date as signOffDate,"
						+ "LN.sign_off_person as signOffPerson,"
						+ "LN.test_status as testStatus,"
						+ "LN.ngn_status as nextgenStatus,"
						+ "LN.lab_id as labId,"
						//+ "case when LN.ng_order_ind = 'N' then 'false' else 'true' end as nextgenOrderIndicator,"
						+ "case when LN.signoff_comments_ind = 'N' then 'false' else 'true' end  as signoffCommentsIndicator,"
						+ "case when LN.documents_ind = 'N' then 'false' else 'true' end  as documentsIndicator,"
						+ "case LN.order_control "
						+ "when 'NW' then 'New' "
						+ "when 'CA' then 'Cancel' "
						+ "when 'RP' then 'Replace' "
						+ "when  'XO' then 'Change'"
						+ "when 'HD' then 'Hold' end as orderControl,"
						+ "case LN.order_priority "
						+ "when 'R' then 'Routine' "
						+ "when 'A' then 'ASAP' "
						+ "when 'S' then 'STAT' "
						+ "when 'C' then 'Callback' "
						+ "when 'T' then 'TimingCritial' "
						+ "end as orderPriority,"
						+ "LN.time_entered as timeEntered,"
						+ "LN.spec_action_code as specimenActionCode,"
						+ "LN.billing_type as billingType,"
						+ "LN.clinical_info as clinicalInformation,"
						+ "LN.cancel_reason as cancelReason,"
						+ "LN.intrf_msg as intrfMessage,"
						+ "LN.practice_id as practiceId,"
						+ "LN.enterprise_id as enterpriseId,"
						+ "Case when LN.delete_ind = 'N' then 'false' else 'true' end as isDeleted,"
						+ "PM.description as providerDisplayName,"
						+ "LM.location_name as locationName,"
						+ "LM.address_line_1 as addressLine1,"
						+ "LM.address_line_2 as addressLine2,"
						+ "LM.city as city,"
						+ "LM.state as state,"
						+ "LM.zip as zip,"
						+ "LM.phone as phone,"
						+ "LM.fax as fax,"
						+ "UM.first_name as firstName,"
						+ "UM.last_name as lastName,"
						+ "UM.mi as middleInitial,"
						+ "OS.future_ind as isFutureOrder,"
						+ "OS.nextdue_date as nextDueDate "
						+ "From lab_nor LN "
						+ "Left outer join provider_mstr PM on LN.ordering_provider  = PM.provider_id "
						+ "Left outer join location_mstr LM on LN.test_location = location_id "
						+ "Left outer join user_mstr UM on LN.sign_off_person = UM.user_id "
						+ "Left outer join order_schedule OS on LN.order_num = OS.order_id "
						+ "where LN.delete_ind<>'Y' "
						+ "AND LN.person_id ='"+parameters.get("personId")+"'"
						+ "AND LN.order_num = '"+parameters.get("orderId")+"'"
						+ "AND LN.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"'"
						+ "AND LN.practice_id = '"+System.getProperty("LoggedInPracticeId")+"'";				
				
				String[] jsonMetaData = {"id","testDescription","encounterId","personId","ufoNumber","generatedBy","orderType"
						,"testLocation","orderingProvider","encounterTimestamp;Date_YYYYMMDD HH:MM:SS","orderDate;Date_YYYYMMDD HH:MM:SS","orderDateTimezone","signOffDate;Date_YYYYMMDD HH:MM:SS",
						"signOffPerson","testStatus","nextgenStatus","labId",
						//"nextgenOrderIndicator",
						"signoffCommentsIndicator",
						"documentsIndicator","orderControl","orderPriority","timeEntered;Date_YYYYMMDD HH:MM:SS","specimenActionCode","billingType",
						"clinicalInformation","cancelReason","intrfMessage","practiceId","enterpriseId","isDeleted","providerDisplayName",
						"locationName","addressLine1","addressLine2","city","state","zip","phone","fax","firstName","lastName",
						"middleInitial","isFutureOrder;Date_YYYYMMDD HH:MM:SS","nextDueDate;Date_YYYYMMDD HH:MM:SS"};
						
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
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
