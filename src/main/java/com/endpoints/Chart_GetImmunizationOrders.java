package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetImmunizationOrders 
{
	public static Boolean Chart_GetImmunizationOrders_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = " select top 25 im.order_num as id, im.enterprise_id as enterpriseId, im.practice_id as practiceId, im.enc_id as encounterId, im.person_id as personId, "
						+ "im.ufo_num as userFriendlyOrderNumber,im.ordering_provider as orderingProviderId, pm.description as orderingProviderName, "
						+ "im.supervisor_provider as supervisorProviderId, im.ngn_status as status, im.vaccine_desc as vaccineDescription, im.create_timestamp as createTimestamp, "
						+ "im.create_timestamp_tz as createTimestampTimezone, im.modify_timestamp as modifyTimestamp, im.modify_timestamp_tz as modifyTimestampTimezone, "
						+ "case when im.delete_ind='N' then 'false' else 'true' end as isDeleted, case when im.documents_ind = 'N' then 'false' else 'true' end as hasDocuments, "
						+ "case when im.tracking_comments_ind = 'N' then 'false' else 'true' end as hasTrackingComments, im.immunizations_desc as immunizationsDescription, "
						+ "case when im.verbal_order_ind = 'N' then 'false' else 'true' end as isVerbalOrder,  pe.enc_timestamp as encounterTimestamp, pe.enc_timestamp_tz as encounterTimestampTimezone "
						+ " FROM imm_nor im  left JOIN provider_mstr pm ON im.ordering_provider =   pm.provider_id "
						+ " left JOIN patient_encounter pe ON im.enc_id = pe.enc_id "
						+ " where im.enterprise_id = '" + System.getProperty("LoggedInEnterpriseId") + "' "
						+ " AND im.practice_id = '" + System.getProperty("LoggedInPracticeId")
						+ "' AND im.person_id = '" + parameters.get("personId") + "' "
								+ "AND im.delete_ind = 'N' ORDER BY userFriendlyOrderNumber desc";

				String[] jsonMetaData = {"id","enterpriseId","practiceId","encounterId", "personId","userFriendlyOrderNumber","orderingProviderId","orderingProviderName",
						"supervisorProviderId","status","vaccineDescription","createTimestamp;Date_YYYYMMDD HH:MM:SS","createTimestampTimezone","modifyTimestamp;Date_YYYYMMDD HH:MM:SS","modifyTimestampTimezone","isDeleted",
						"hasDocuments", "hasTrackingComments", "immunizationsDescription", "isVerbalOrder", "encounterTimestamp;Date_YYYYMMDD HH:MM:SS", "encounterTimestampTimezone"};

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
