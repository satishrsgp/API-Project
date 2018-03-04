package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

/*
 * ********************************************************************
 * Class Name						: Chart_Persons_GET
 * Created By						: 
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: personId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch details for a person based on the personId
 * ******************************************************************
 */

public class Chart_Persons_GET
{
	public static Boolean chart_Person_GET_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = " select "
						+" UPPER(CAST(pa.person_id AS CHAR(36))) as personId,pa.enterprise_id as enterpriseId, pa.practice_id as practiceId,med_rec_nbr as medicalRecordNumber,UPPER(CAST(pa.pharmacy_code_1_id AS CHAR(36))) as primaryPharmacyId"
						+" ,UPPER(CAST(pa.pharmacy_code_2_id AS CHAR(36))) as secondaryPharmacyId,UPPER(CAST(pa.pharmacy_code_mo_id AS CHAR(36))) as mailOrderPharmacyId,blood_type as bloodType"
						+" from patient pa left outer join patient_ pa_ on pa.person_id = pa_.person_id"
						+" where pa.person_id='"+parameters.get("personId")+"'";
				
				String[] jsonMetaData = {"personId;GUID","enterpriseId;Normal","practiceId;Normal","medicalRecordNumber;Normal","primaryPharmacyId;GUID","secondaryPharmacyId;GUID","mailOrderPharmacyId;GUID","bloodType;Normal"};
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..medicalRecordNumber", jsonList);
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