package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetFdbDosageOrders
{

	public static Boolean master_GetFdbDosageOrders_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "Select DISTINCT  top 25 "
						+ "FMD.poeostrid as id,FMD.admin_direction as adminDirection,FMD.admin_direction2 as adminDirection2,FMD.route_description_code as routeDescriptionCode, "
						+ "FMD.route as route,FMD.dosage_form_amount_unit_low as dosageFormAmountUnitLow,FMD.dosage_form_amount_unit_high as dosageFormAmountUnitHigh, "
						+ "FMD.dosage_form_amount_low as dosageFormAmountLow,FMD.dosage_form_amount_high as dosageFormAmountHigh,FMD.dosage_unit_low as dosageUnitLow, " 
						+ "FMD.dosage_unit_high as dosageUnitHigh,FMD.dosage_low as dosageLow,FMD.dosage_high as dosageHigh,FMD.frequency_low as frequencyLow, "
						+ "FMD.frequency_high as frequencyHigh,FMD.frequency_low2 as frequencyLow2,FMD.frequency_high2 as frequencyHigh2,FMD.interval_unit_low as intervalUnitLow, "
						+ "FMD.interval_unit_high as intervalUnitHigh,FMD.interval_unit_low2 as intervalUnitLow2,FMD.interval_unit_high2 as intervalUnitHigh2, "
						+ "FMD.interval_low as intervalLow,FMD.interval_high as intervalHigh,FMD.interval_low2 as intervalLow2,FMD.interval_high2 as intervalHigh2, "
						+ "FMD.duration_unit_low as durationUnitLow,FMD.duration_unit_high as durationUnitHigh,FMD.duration_low as durationLow,FMD.duration_high as durationHigh, "
						+ "FMD.text_code as textCode,FMD.dose_calculation_unit as doseCalculationUnit, "
						+ "case when FMD.calc_required = '0' then 'false' else 'true' end as calculationRequired, "
						+ "FMD.per_day_calc as perDayCalculation,FMD.duration_additional_days as durationAdditionalDays,FMDT.text as additionalText,FMDG.gcn_seqno as gcnSequenceNumber, " 
						+ "CASE WHEN ( EXISTS (SELECT 'True' AS [C1] FROM [dbo].[fdb_med_dosage_default_xref] AS [Extent3]WHERE ([Extent3].[POEOSETID] = FMD.[poeostrid]) AND ([Extent3].[gcn_seqno] = FMDG.[gcn_seqno])))  " 
						+ "THEN cast('true' as varchar) ELSE cast('false' as varchar) END AS isDefaultOrder "
						+ "From fdb_med_dosage_order_def as FMD  "
						+ "INNER JOIN fdb_med_dxid_gcn_xref FMDG ON FMD.[poeostrid] = FMDG.[poeosetid] "
						+ "Left outer Join fdb_med_dosage_default_text FMDT on FMD.text_code = FMDT.text_code "
						+ "WHERE FMDG.gcn_seqno = '"+parameters.get("gcnSeqNo")+"' "
						+ "ORDER BY FMD.[poeostrid] ASC";
						
				String[] jsonMetaData = {"id","adminDirection","adminDirection2","routeDescriptionCode","route","dosageFormAmountUnitLow","dosageFormAmountUnitHigh","dosageFormAmountLow","dosageFormAmountHigh",
						"dosageUnitLow","dosageUnitHigh","dosageLow","dosageHigh","frequencyLow","frequencyHigh","frequencyLow2","frequencyHigh2","intervalUnitLow","intervalUnitHigh",
						"intervalUnitLow2","intervalUnitHigh2","intervalLow","intervalHigh","intervalLow2","intervalHigh2","durationUnitLow","durationUnitHigh","durationLow",
						"durationHigh","textCode","doseCalculationUnit","calculationRequired","perDayCalculation","durationAdditionalDays","additionalText","gcnSequenceNumber",
						"isDefaultOrder"};
				
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
