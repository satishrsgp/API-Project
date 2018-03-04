package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Favorites_GetCustomDosageOrders 
{
	public static Boolean favorites_GetCustomDosageOrders_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT fdb.uniq_id AS id, fdb.location_id AS locationId, fdb.provider_id AS providerId, "
						+ "fdb.created_by AS createdBy, fdb.create_timestamp AS createTimestamp, fdb.modified_by AS modifiedBy, "
						+ "fdb.modify_timestamp AS modifyTimestamp, fdb.gcn_seqno AS gcnSequenceNumber, fdb.additional_text AS additionalText, "
						+ "fdb.special_instruction AS specialInstruction, CASE WHEN fdb.delete_ind = 'N' THEN 'false' ELSE 'true' END AS isDeleted, "
						+ "fdb.admin_direction AS adminDirection, fdb.admin_direction2 AS adminDirection2, "
						+ "fdb.route_description_code AS routeDescriptionCode, fdb.route AS route, "
						+ "fdb.dosage_form_amount_unit_low AS dosageFormAmountUnitLow, fdb.dosage_form_amount_unit_high AS dosageFormAmountUnitHigh, "
						+ " fdb.dosage_form_amount_low AS dosageFormAmountLow, fdb.dosage_form_amount_high AS dosageFormAmountHigh, "
						+ " fdb.dosage_unit_low AS dosageUnitLow, fdb.dosage_unit_high AS dosageUnitHigh, fdb.dosage_low AS dosageLow, "
						+ " fdb.dosage_high AS dosageHigh, fdb.frequency_low AS frequencyLow, fdb.frequency_high AS frequencyHigh, "
						+ " fdb.frequency_low2 AS frequencyLow2, fdb.frequency_high2 AS frequencyHigh2, fdb.interval_unit_low AS intervalUnitLow, "
						+ " fdb.interval_unit_high AS intervalUnitHigh, fdb.interval_unit_low2 AS intervalUnitLow2, "
						+ " fdb.interval_unit_high2 AS intervalUnitHigh2, fdb.interval_low AS intervalLow, fdb.interval_high AS intervalHigh, "
						+ " fdb.interval_low2 AS intervalLow2, fdb.interval_high2 AS intervalHigh2, fdb.duration_unit_low AS durationUnitLow, "
						+ " fdb.duration_unit_high AS durationUnitHigh, fdb.duration_low AS durationLow, fdb.duration_high AS durationHigh "
						+ " FROM fdb_med_dosage_order_custom fdb where fdb.gcn_seqno = '"+parameters.get("gcnSeqNo")+"'" ;
			
				
				String[] jsonMetaData = {"id","locationId","providerId","createdBy","createTimestamp;Date_YYYYMMDD HH:MM:SS","modifiedBy",
						"modifyTimestamp;Date_YYYYMMDD HH:MM:SS","gcnSequenceNumber","additionalText","specialInstruction","isDeleted","adminDirection",
						"adminDirection2","routeDescriptionCode","route","dosageFormAmountUnitLow","dosageFormAmountUnitHigh","dosageFormAmountLow"
				,"dosageFormAmountHigh","dosageUnitLow","dosageUnitHigh","dosageLow","dosageHigh","frequencyLow","frequencyHigh","frequencyLow2","frequencyHigh2",
				"intervalUnitLow","intervalUnitHigh","intervalUnitLow2","intervalUnitHigh2","intervalLow","intervalHigh","intervalLow2","intervalHigh2","durationUnitLow"
				,"durationUnitHigh","durationLow","durationHigh"
				};

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
