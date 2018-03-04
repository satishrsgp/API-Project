package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPatientMedicationSummaries 
{

	public static Boolean chart_GetPatientMedicationSummaries_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = " select pm.uniq_id as id, pm.person_id as personId, pm.provider_id as providerId, fm.brand_name as fdbName, "
						+ " pm.medication_name as medicationName, fm.generic_name as genericName, fm.brand_name as brandName, "
						+ " case when pm.selected_generic_ind = 'N' then 'false' else 'true' end as isGenericSelected, "
						+ " fm.dose as dose, fm.route_desc as route, fm.dose_form_desc as doseForm, pm.start_date as originalStartDate, "
						+ " pm.start_date as startDate, pm.date_stopped as stopDate, pm.sig_desc as sigDescription, "
						+ " pm.enterprise_id as enterpriseId, pm.practice_id as practiceId, pm.enc_id as encounterId, "
						+ " pm.source_product_id as sourceProductId, pm.last_audit_type as lastAuditType, "
						+ " CASE when pm.reprndc_ind = 'N' then 'false' else 'true' end as representativeNdcIndicator, case when pm.privacy_ind = 'N' then 'false' else 'true' end as privacyIndicator, "
						+ " CASE WHEN mh.hidden_flag = 'N' THEN 'true' ELSE 'false' END AS isHidden, pm.medid as medicationId, "
						+ " pm.ndc_id as ndcId, pm.create_timestamp as createTimestamp, pm.create_timestamp_tz as createTimestampTimezone "
						+ " from patient_medication pm inner join fdb_medication fm on pm.ndc_id = fm.ndc_id "
						+ " left join medication_hidden mh on mh.medid =pm.medid where pm.person_id = '"+parameters.get("personId")+"'"
						+ " and pm.enc_id ='"+parameters.get("encounterId")+"' order by pm.uniq_id" ;
					
				

				
				String[] jsonMetaData = {"id","personId","providerId","fdbName","medicationName","genericName","brandName",
						"isGenericSelected","dose","route","doseForm","originalStartDate","startDate","stopDate","sigDescription","enterpriseId",
						"practiceId","encounterId","sourceProductId","lastAuditType","representativeNdcIndicator","privacyIndicator","isHidden","medicationId","ndcId","createTimestamp;Date_YYYYMMDD HH:MM:SS",
						"createTimestampTimezone"};
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
