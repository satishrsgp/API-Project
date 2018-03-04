package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetObservationPanelResult
{
	public static Boolean Chart_GetObservationPanelResult_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		System.out.println("Chart_GetObservationPanelResult_Test Parameters for the run is:"+parameters);
		Boolean status=false;
		try
		{
			if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
			{
				String strQuery = "SELECT lro.obx_seq_num AS observationSequenceNumber,UPPER(CAST(lro.unique_obr_num AS CHAR(36))) AS panelId,UPPER(CAST(p.person_id AS CHAR(36))) AS personId,UPPER(CAST(ln.order_num AS CHAR(36))) AS orderId,lro.enterprise_id AS enterpriseId, "
						+"lro.practice_id AS practiceId,lro.obs_id AS obsId,lro.result_desc AS resultDescription,case when substring(lro.observ_value,9,1)='-' and substring(lro.observ_value,14,1)='-' and "
						+"substring(lro.observ_value,19,1)='-' and substring(lro.observ_value,24,1)='-' and len(lro.observ_value) = 36 then UPPER(CAST(lro.observ_value AS CHAR(36))) else lro.observ_value end AS observationValue,lro.abnorm_flags AS abnormalityCode, "
						+"case when lro.abnorm_flags='A' then 'Abnormal'  when lro.abnorm_flags='AA' then 'VeryAbnormal' when lro.abnorm_flags='B' then 'Better' when lro.abnorm_flags='W' then 'Worse' " 
						+"when lro.abnorm_flags='D' then 'SignificantChangeDown' when lro.abnorm_flags='U' then 'SignificantChangeUp' when lro.abnorm_flags='R' then 'Resistant' when lro.abnorm_flags='I' then 'Intermediate' " 
						+"when lro.abnorm_flags='S' then 'Susceptible' when lro.abnorm_flags='MS' then 'ModeratelySusceptible' when lro.abnorm_flags='VS' then 'VerySusceptible' when lro.abnorm_flags='H' then 'High' " 
						+"when lro.abnorm_flags='HH' then 'VeryHigh' when lro.abnorm_flags='>' then 'GreaterThan' when lro.abnorm_flags='L' then 'Low' when lro.abnorm_flags='LL' then 'VeryLow' when lro.abnorm_flags='<' then 'LessThan' else 'Normal' end as abnormality, "
						+"lro.units AS units,lro.ref_range AS referenceRange,lro.observ_result_stat AS observResultStatus,lro.clinical_name AS clinicalName,lro.obs_date_time AS observationDateTime, "
						+"lro.obs_date_time_tz AS observationDateTimeTimezone,lro.loinc_code AS loincCode,lro.snomed_code AS snomedCode,lro.result_seq_num AS resultSequenceNumber, "
						+"case when lro.comment_ind='N' then 'false' else 'true' end AS hasComment,case when lro.delete_ind='N' then 'false' else 'true' end AS isDeleted "
						+"FROM lab_results_obx AS lro INNER JOIN lab_results_obr_p AS lrop ON (lro.unique_obr_num = lrop.unique_obr_num) "
						+"INNER JOIN person AS p ON lrop.person_id = p.person_id "
						+"INNER JOIN lab_nor AS ln ON (lrop.ngn_order_num = ln.order_num) AND (p.person_id = ln.person_id) "
						+"WHERE lro.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"' AND p.person_id = '"+parameters.get("personId")+"' AND lro.unique_obr_num = '"+parameters.get("panelId")+"'";

				String[] jsonMetaData = {"observationSequenceNumber","panelId","personId","orderId","enterpriseId","practiceId","observationId","resultDescription","observationValue","abnormalityCode","abnormality","units","referenceRange","observResultStatus","clinicalName","observationDateTime","observationDateTimeTimezone","loincCode","snomedCode","resultSequenceNumber","hasComment","isDeleted"};
				ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
				Log.info("JSON LIST is :\n" + jsonList);
				status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..observationSequenceNumber", jsonList);
			}
			else
			{
				status = true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
			softAssert.fail("Error in Test Script please look at logs");
		}
		return status;
	}
}