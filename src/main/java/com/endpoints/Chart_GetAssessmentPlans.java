package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetAssessmentPlans {



	public static Boolean Chart_GetAssessmentPlans_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "Select top 25 "
						+ "seq_no as id,person_id as personId,enc_id as encounterId,practice_id as practiceId,enterprise_id as enterpriseId, "
						+ "txt_aip as aip,txt_aip_priority_display as aipPriority,txt_description as description,txt_diagnosis_code_id as diagnosisCode, "
						+ "txt_diagnosis_uniq_id as diagnosisId,txt_enc_dx_priority as encounterDiagnosisPriority,txt_icd9cm_code_id as icd9cmCodeId," 
						+ "txt_report_display as reportDisplay,axis as axis,chief_complaint as chiefComplaint,detail_type as detailType, "
						+ "detail_type_priority as detailTypePriority,differential_diagnosis as differentialDiagnosis,episode as episode, "
						+ "side as side,site as site,"
						+ "case when txt_hx_record_ind = 'N' then 'false' else 'true' end as hasHistoryRecord,"
						+ "txt_Specifiers as specifiers,"
						+ "case when txt_supplemental_ind = 'Y' then 'true' when txt_supplemental_ind = 'N' then 'false' else null  end as isSupplemental "
						+ "From assessment_impression_plan_ "
						+ "Where person_id = '"+parameters.get("personId")+"'"
						+ "Order by "
						+ "rtrim(ltrim(seq_no)) asc";
						
				
				String[] jsonMetaData = {"id","personId","encounterId","practiceId","enterpriseId","aip","aipPriority","description",
						"diagnosisCode","diagnosisId","encounterDiagnosisPriority","icd9cmCodeId","reportDisplay","axis","chiefComplaint",
						"detailType","detailTypePriority","differentialDiagnosis","episode","side","site","hasHistoryRecord","specifiers",
						"isSupplemental"};
				
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
