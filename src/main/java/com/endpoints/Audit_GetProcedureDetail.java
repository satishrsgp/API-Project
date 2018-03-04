package com.endpoints;

import java.util.ArrayList;

import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Audit_GetProcedureDetail
{
	public static Boolean audit_GetProcedureDetail(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				Object objId = JsonPath.read(strResponse, "$.id");

				String strQuery="select pp.uniq_id,pp.service_item_desc,pp.service_date,lm.location_name,pm.first_name,pm.last_name,pp.modifier_id_1,pp.modifier_id_2,pp."
						+ "modifier_id_3,pp.modifier_id_4, pp.diagnosis_code_id_1,pp.diagnosis_code_id_2,pp.diagnosis_code_id_3,pp.diagnosis_code_id_4,pp.diagnosis_code_id_5,"
						+ "pp.diagnosis_code_id_6,pp.diagnosis_code_id_7,pp.diagnosis_code_id_8, pp.place_of_service,pp.units,pp.amount,pp.note,pp.modify_timestamp "
						+ "from patient_procedure pp inner join provider_mstr pm on pp.provider_id=pm.provider_id inner join location_mstr lm on lm.location_id=pp.location_id "
						+ "where pp.uniq_id='"+objId.toString()+"'";

				String[] jsonMetaData = {"id", "serviceItemDesc", "serviceDate","locationName","providerFirstName","providerLastName","modifierId1",
						"modifierId2","modifierId3" ,"modifierId4","diagnosisCodeId1","diagnosisCodeId2","diagnosisCodeId3","diagnosisCodeId4","diagnosisCodeId5",
						"diagnosisCodeId6","diagnosisCodeId7","diagnosisCodeId8","placeOfService","units","amount","notes","timestamp;Date_YYYYMMDD HH:MM:SS"};

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