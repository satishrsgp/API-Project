package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Resources_GetResourceAppointmen
{

	/*
	 * ********************************************************************************
	 * Class Name						:Resources_GetResourceAppointmen
	 * Created By						: 
	 * Created Date						: 
	 * Modified Date					:
	 * Modified By						:
	 * Parameters						: 
	 * Test Case ID						:
	 * Test Case Author					: 
	 * Test Case Modified By			:
	 * Test Case Reviewed By			:
	 * Purpose							: Route will fetch the allergy chart details
	 ***********************************************************************************
	 */

	public static Boolean resources_GetResourceAppointmen_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		//System.out.println(strResponse);
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{ 
				String strQuery = "select top 25"
						+" UPPER(CAST(a.appt_id AS CHAR(36))) as id,a.appt_date as appointmentDate,a.begintime as beginTime,case when a.cancel_ind='Y' then 'true' else 'false' end as isCancelled,a.duration as duration,"
						+" UPPER(CAST(a.enc_id AS CHAR(36))) as encounterId,a.endtime as endTime,e.event as eventName,UPPER(CAST(a.location_id AS CHAR(36))) as locationId,a.practice_id as practiceId,"
						+" UPPER(CAST(a.rendering_provider_id AS CHAR(36))) as renderingProviderId,UPPER(CAST(am.resource_id AS CHAR(36))) as resourceId,a.workflow_room as workflowRoom,a.workflow_status as workflowStatus,"
						+" UPPER(CAST(a.person_id AS CHAR(36))) as personId,a.first_name as firstName,a.middle_name as middleName,a.last_name as lastName,"
						+" p.home_phone as homePhone,a.address_line_1 as addressLine1,a.address_line_2 as addressLine2,a.city as city,a.state as state,a.zip as zip"
						+" from"
						+" appointments a inner join appointment_members am on (a.appt_id=am.appt_id and am.delete_ind='N')"
						+" left outer join person p on (a.person_id=p.person_id) left outer join events e on (e.event_id=a.event_id)"
						+" where am.resource_id='" +parameters.get("resourceId")+"' order by a.appt_date desc,a.begintime asc,a.appt_id asc";
				
				String[] jsonMetaData = {"id","appointmentDate;Date_YYYYMMDD HH:MM:SS","beginTime","isCancelled","duration","encounterId","endTime","eventName","locationId","practiceId","renderingProviderId","resourceId","workflowRoom","workflowStatus","personId","firstName","middleName","lastName","homePhone","addressLine1","addressLine2","city","state","zip","details"};

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
