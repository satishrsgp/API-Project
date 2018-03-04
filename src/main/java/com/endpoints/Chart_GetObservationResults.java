package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.Log;

import net.minidev.json.JSONArray;

public class Chart_GetObservationResults
{
	public static Boolean Chart_GetObservationResults_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		System.out.println("Chart_GetObservationResults_Test Parameters for this run is:"+parameters);
		Boolean status=false;
		try
		{
			if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
			{
				String strQuery = "SELECT RTRIM(UPPER(CAST(ln.order_num AS CHAR(36))) + UPPER(CAST(lro.obx_seq_num AS CHAR(36)))) AS primaryKey,UPPER(CAST(lro.unique_obr_num AS CHAR(36))) AS panelId,UPPER(CAST(p.person_id AS CHAR(36))) AS personId,UPPER(CAST(ln.order_num AS CHAR(36))) AS orderId,lro.obx_seq_num AS observationSequenceNumber,lro.enterprise_id AS enterpriseId, "
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
						+"WHERE lro.enterprise_id = '"+System.getProperty("LoggedInEnterpriseId")+"' AND p.person_id = '"+parameters.get("personId")+"' order by lro.unique_obr_num,observationSequenceNumber";

				//String[] jsonMetaData = {"panelId","personId","orderId","observationSequenceNumber","enterpriseId","practiceId","obsId","resultDescription","observationValue","abnormalityCode","abnormality","units","referenceRange","observResultStatus","clinicalName","observationDateTime","observationDateTimeTimezone","loincCode","snomedCode","resultSequenceNumber","hasComment","isDeleted"};
				ArrayList<Object> jsonList = new ArrayList<Object>();
				Object obj = JsonPath.read(strResponse, "$.." + strUniqueJsonToken);
				if (obj instanceof JSONArray)
				{
					for(int j = 0; j< ((JSONArray) obj).size(); j++)
					{
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].orderId").toString().toUpperCase() + JsonPath.read(strResponse, "$.items["+j+"].observationSequenceNumber").toString());
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].panelId").toString().toUpperCase());
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].personId").toString().toUpperCase());
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].orderId").toString().toUpperCase());
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].observationSequenceNumber"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].enterpriseId"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].practiceId"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].observationId"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].resultDescription"));
						if(!Objects.equals(JsonPath.read(strResponse, "$.items["+j+"].observationValue"), null) && (JsonPath.read(strResponse, "$.items["+j+"].observationValue").toString().length() == 36) && (JsonPath.read(strResponse, "$.items["+j+"].observationValue").toString().substring(8, 9).equals("-") && JsonPath.read(strResponse, "$.items["+j+"].observationValue").toString().substring(13, 14).equals("-") && JsonPath.read(strResponse, "$.items["+j+"].observationValue").toString().substring(18, 19).equals("-") && JsonPath.read(strResponse, "$.items["+j+"].observationValue").toString().substring(23,24).equals("-")))
						{
							jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].observationValue").toString().toUpperCase());
						}
						else
						{
							jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].observationValue"));
						}
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].abnormalityCode"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].abnormality"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].units"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].referenceRange"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].observResultStatus"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].clinicalName"));
						jsonList.add((JsonPath.read(strResponse, "$.items["+j+"].observationDateTime") == null) ? null : JsonPath.read(strResponse, "$.items["+j+"].observationDateTime").toString().replace("T", " "));
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].observationDateTimeTimezone"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].loincCode"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].snomedCode"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].resultSequenceNumber"));
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].hasComment").toString());
						jsonList.add(JsonPath.read(strResponse, "$.items["+j+"].isDeleted").toString());
					}
				}
				Log.info("JSON LIST is :\n" + jsonList);
				status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..panelId", jsonList);
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