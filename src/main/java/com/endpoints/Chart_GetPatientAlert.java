package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatientAlert 
{

	public static Boolean chart_GetPatientAlert_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT pae.alert_id AS id, pae.person_id AS personId, pae.message_id AS messageId, pae.source_type AS alertTypeId, "
						+ "atm.alerttype_description alertTypeDescription, am.description AS description, CASE WHEN am.delete_ind = 'N' THEN 'false' ELSE 'true' END AS isDeleted, "
						+ "CASE WHEN pae.flag_ind = 'N' THEN 'false' ELSE 'true' END AS isFlagged, case when pae.acknowledge_by = 'P' then 'Provider' "
						+ "else pae.acknowledge_by end AS shouldBeAcknowledgedBy , pae.source_name AS sourceName, pae.source_desc AS sourceDescription "
						+ "FROM patient_alerts_ehr pae INNER JOIN ehr_alerts_mstr am ON pae.message_id = am.message_id INNER JOIN ehr_alerttypes_mstr atm "
						+ "ON am.alerttype_id = atm.alerttype_id LEFT JOIN patient_alerts_ehr_snooze snz on snz.person_id =pae.person_id "
						+ "LEFT JOIN patient_alerts_ehr_audit ea on ea.alert_id = pae.alert_id where pae.alert_id = '"
						+parameters.get("alertId")+"'";
				
				String[] jsonMetaData = {"id","personId","messageId","alertTypeId","alertTypeDescription","description",
				"isDeleted","isFlagged","shouldBeAcknowledgedBy","sourceName",	"sourceDescription"};
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
