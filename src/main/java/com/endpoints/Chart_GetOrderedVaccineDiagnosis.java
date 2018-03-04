package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetOrderedVaccineDiagnosis
{

	/*
	 * ********************************************************************************
	 * Class Name						:Chart_GetOrderedVaccineDiagnosis
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

	public static Boolean Chart_GetOrderedVaccineDiagnosis_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		//System.out.println(strResponse);
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{ 
				String strQuery = "select  lod.unique_diag_num AS id, "
						+ "ino.person_id  as personId,  "
						+ "ino.order_num as orderNumber, "
						+"iov.order_vaccine_id as orderVaccineId, "
						+"lod.diagnosis_code_lib_id as diagnosisCodeLibraryId, "
						+"lod.diagnosis_code_id as diagnosisCodeId, "
						+"dcm.[description] as [description], "   
						+"dcm.icd9cm_code_id as icd9CmCodeId,  "
						+ " dcm.[user_description] as userDescription "
						+ "FROM  imm_nor AS ino INNER JOIN imm_order_vaccines AS iov ON (ino.order_num = iov.order_num) "
						+ "  INNER JOIN lab_order_diag AS lod ON (iov.order_vaccine_id = lod.order_test_id) AND (ino.order_num = lod.order_num) "
						+ "INNER JOIN diagnosis_code_mstr AS dcm ON (lod.diagnosis_code_id = dcm.diagnosis_code_id) AND (lod.diagnosis_code_lib_id = dcm.diagnosis_code_lib_id) "
						+ " WHERE (ino.person_id = '6CEC0FF9-76BB-4DC0-B331-F7A3BA45EA4F') and (iov.order_vaccine_id = '2714742A-0962-4800-B45A-8B963C5397EA') and (ino.order_num = '09982C07-CD2E-4A8A-9B5D-79F9996427F1') ";
					
				
						
		
				//
				String[] jsonMetaData = {"id","personId","orderNumber","orderVaccineId","diagnosisCodeLibraryId","diagnosisCodeId","description","icd9CmCodeId","userDescription" };

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
