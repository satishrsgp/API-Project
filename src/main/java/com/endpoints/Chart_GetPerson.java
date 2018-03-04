package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetPerson {




	public static Boolean Chart_GetPerson_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = " select "
						+ "P.person_id as personId, "
						+ "P.enterprise_id as enterpriseId, "
						+ "P.practice_id as practiceId, "
						+ "P.med_rec_nbr as medicalRecordNumber, "
						+ "P.pharmacy_code_1_id as primaryPharmacyId, "
						+ "P.pharmacy_code_2_id as secondaryPharmacyId, "
						+ "P.pharmacy_code_mo_id as mailOrderPharmacyId, "
						+ "P_.blood_type as bloodType, "
						+ "case when P.no_active_devices_ind = 'N' then 'false' "
						+ "when P.no_active_devices_ind = 'Y' then 'true' "
						+ "else P.no_active_devices_ind end as hasNoActiveDevices,   "
						+ "Case when P.no_active_medications_ind = 'Y' then 'true' else 'false' end  as hasNoActiveMedications, "
						+ "case when P.no_active_problems = 'N' then 'false' else 'true' end as hasNoActiveProblems, "
						+ "Case when P.no_active_problems_snomed='Y' then 'true' else 'false' end as hasNoActiveProblemsSnomed, "
						+ "Case when P.no_known_imm_history_ind='Y' then 'true' else 'false' end as hasNoKnownImmunizationHistory, "
						+ "Case when P.no_known_procedures_ind='Y' then 'true' else 'false' end as hasNoKnownProcedures, "
						+ "Case when P.no_unresolved_allergies_ind='Y' then 'true' else 'false' end as hasNoUnresolvedAllergies "
						+ "from patient P  "
						+ "Left outer join Patient_ P_ on  P.person_id = P_.person_id "										
						+" where p.person_id='"+parameters.get("personId")+"'"
						+ "AND P.enterprise_id = '" + System.getProperty("LoggedInEnterpriseId") + "' "
						+ "AND P.practice_id = '" + System.getProperty("LoggedInPracticeId") + "' ";				
				
				String[] jsonMetaData = {"personId","enterpriseId","practiceId","medicalRecordNumber","primaryPharmacyId","secondaryPharmacyId","mailOrderPharmacyId","bloodType","hasNoActiveDevices","hasNoActiveMedications",
						"hasNoActiveProblems","hasNoActiveProblemsSnomed","hasNoKnownImmunizationHistory","hasNoKnownProcedures","hasNoUnresolvedAllergies"};
						
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..personId", jsonList);
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
