package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Resources_GetResourceAppStatHis
{

	/*
	 * ********************************************************************************
	 * Class Name						:Resources_GetResourceAppStatHis
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

	public static Boolean resources_GetResourceAppStatHis_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		//System.out.println(strResponse);
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{ 
				String strQuery = "	select r.resource_id as resourceId,"
						+" 	a.appt_id as appointmentId, pash.seq_no as sequenceNumber, pash.enc_id as encounterId, pash.person_id as personId,pash.txt_status as status,pash.txt_room as room"
						
						+"  FROM  pat_apt_status_hx_ AS pash"
						+"     INNER JOIN appointments AS a ON pash.enc_id = a.enc_id"
						+"  INNER JOIN appointment_members AS am ON a.appt_id = am.appt_id"
						+"    INNER JOIN resources AS r ON am.resource_id = r.resource_id"
						+"  WHERE (a.appt_id ='" +parameters.get("appointmentId")+"') AND (r.resource_id= '" +parameters.get("resourceId")+"')";
					//	+" where am.resource_id='" +parameters.get("resourceId")+"' order by a.appt_date desc,a.begintime asc,a.appt_id asc";
				// "and resource_id='"+parameters.get("id")+"'";
						
		
				//
				String[] jsonMetaData = {"resourceId","appointmentId","sequenceNumber","encounterId","personId","status","room","documentedBy","createTimestamp","createTimestampZone","modifyTimestamp","modifyTimestampZone"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..resourceId", jsonList);
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
