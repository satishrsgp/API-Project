package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPrescriptionNoteDetails 
{

	public static Boolean chart_GetPrescriptionNoteDetails_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT mrx.note_id AS id, CASE WHEN mrx.note_type = '1' THEN 'General' WHEN mrx.note_type = '2' THEN "
						+ "'Prescribe' WHEN mrx.note_type = '3' THEN 'Patient' WHEN mrx.note_type = '4' THEN 'Stop' ELSE 'None' END AS noteType, "
						+ "mrx.note AS note, p.person_id AS personId, mrx.medication_id AS medicationId, mrx.created_by AS createdBy, "
						+ "mrx.modified_by AS modifiedBy, mrx.create_timestamp AS createTimestamp, mrx.create_timestamp_tz AS createTimestampTimezone, "
						+ "mrx.modify_timestamp AS modifyTimestamp, mrx.modify_timestamp_tz AS modifyTimestampTimezone FROM person p "
						+ "INNER JOIN patient_medication pm ON pm.person_id = p.person_id INNER JOIN med_rx_notes mrx ON mrx.medication_id = pm.uniq_id "
						+ "WHERE mrx.medication_id = '"+parameters.get("medicationId")+"' AND p.person_id = '"+parameters.get("personId")+"' "
						+ "AND mrx.note_id = '"+parameters.get("noteId")+"' ORDER BY mrx.create_timestamp DESC"  ;
						
								
				
				
				String[] jsonMetaData = {"id;GUID","noteType;Normal","note;Normal","personId;GUID","medicationId;GUID","createdBy;Normal","modifiedBy;Normal","createTimestamp;Date_YYYYMMDD HH:MM:SS",
						"createTimestampTimezone;Normal","modifyTimestamp;Date_YYYYMMDD HH:MM:SS","modifyTimestampTimezone;Normal"};
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
