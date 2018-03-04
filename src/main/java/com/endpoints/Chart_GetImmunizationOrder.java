package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetImmunizationOrder 
{
	public static Boolean Chart_GetImmunizationOrder_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select im.order_num as id, im.enterprise_id as enterpriseId, im.practice_id as practiceId, im.enc_id as encounterId, "
						+ "im.person_id as personId, p.last_name + ', ' + p.first_name as personName, "
						//+ "--https://www.codeproject.com/Questions/310086/How-to-concatenate-two-columns-in-SQL-server"
						//+ "Convert(nvarchar(50),p.last_name)+', '+Convert(nvarchar(50),p.first_name) as personName, "
						+ "im.ufo_num as userFriendlyOrderNumber,im.generated_by as generatedBy,im.location_id as locationId,lm.location_name as locationName, im.ordering_provider as orderingProviderId, "
						+ "pm.description as orderingProviderName,im.supervisor_provider as supervisorProviderId, im.ngn_status as status, im.vaccine_desc as vaccineDescription, "
						+ "im.create_timestamp as createTimestamp,im.create_timestamp_tz as createTimestampTimezone, im.modify_timestamp as modifyTimestamp, "
						+ "im.modify_timestamp_tz as modifyTimestampTimezone, im.registry_id as registryId, im.intrf_msg as interfaceMessage, case when im.documents_ind = 'N' then 'false' else 'true' end as hasDocuments,"
						+ "case when im.tracking_comments_ind = 'N' then 'false' else 'true' end as hasTrackingComments,im.immunizations_desc as immunizationsDescription, im.order_comment as orderComment, "
						+ "case when im.verbal_order_ind = 'N' then 'false' else 'true' end as isVerbalOrder, im.allergy_review as allergiesReviewed,im.cancel_reason as cancelReason, im.sign_off_date as signOffDate, "
						+ "im.sign_off_date_tz as signOffDateTimezone, im.sign_off_person as signOffUserId, "
						+ "Convert(nvarchar(50),um.last_name)+','+Convert(nvarchar(50),um.first_name) as signOffUserName, um.privacy_level, "
						+ "pe.enc_timestamp as encounterTimestamp, pe.enc_timestamp_tz as encounterTimestampTimezone, "
						+ "os.schedule_id as scheduleId,os.future_ind as scheduleFutureIndicator,os.start_date as scheduleStartDate, "
						+ "os.max_occurrence as scheduleMaximumOccurrence, os.end_date as scheduleEndDate, os.interval as scheduleInterval, os.timespan as scheduleTimespan, "
						+ "os.stop_date as scheduleStopDate, os.stop_reason as scheduleStopReason, os.nextdue_date as scheduleNextDueDate,  "
						+ "CASE WHEN im.delete_ind = 'Y' THEN 'true' ELSE 'false' END AS isDeleted  "
						+ "FROM imm_nor im  left JOIN provider_mstr pm ON im.ordering_provider =   pm.provider_id  "	
						+ "left JOIN patient_encounter pe ON im.enc_id = pe.enc_id  "
						+ "left JOIN person p ON im.person_id = p.person_id  "
						+ "left JOIN location_mstr lm ON im.location_id = lm.location_id  "
						+ "left JOIN order_schedule os ON im.order_num = os.order_id  "
						+ "left JOIN user_mstr um ON im.sign_off_person  = um.user_id  "
						+ "where im.enterprise_id = '" + System.getProperty("LoggedInEnterpriseId")
						+ "' AND im.practice_id = '" + System.getProperty("LoggedInPracticeId")
						+ "' AND im.person_id = '" + parameters.get("personId") 
						+ "' AND im.order_num='" + parameters.get("orderId") + "'";

				String[] jsonMetaData = {"id","enterpriseId","practiceId","encounterId", "personId","personName","userFriendlyOrderNumber","generatedBy","locationId","locationName",
						"orderingProviderId","orderingProviderName","supervisorProviderId","status","vaccineDescription","createTimestamp;Date_YYYYMMDD HH:MM:SS","createTimestampTimezone",
						"modifyTimestamp;Date_YYYYMMDD HH:MM:SS",
						"modifyTimestampTimezone","registryId","interfaceMessage","hasDocuments", "hasTrackingComments", "immunizationsDescription","orderComment","isVerbalOrder",
						"allergiesReviewed","cancelReason","signOffDate","signOffDateTimezone","signOffUserId","signOffUserName","privacyLevel","encounterTimestamp;Date_YYYYMMDD HH:MM:SS",
						"encounterTimestampTimezone","scheduleId","scheduleFutureIndicator","scheduleStartDate","scheduleMaximumOccurrence","scheduleEndDate","scheduleInterval",
						"scheduleTimespan","scheduleStopDate","scheduleStopReason","scheduleNextDueDate", "isDeleted"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
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
