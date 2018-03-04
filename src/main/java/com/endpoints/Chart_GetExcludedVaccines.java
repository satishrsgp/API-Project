package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetExcludedVaccines 
{
	public static Boolean Chart_GetExcludedVaccines_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select IMVE.exclusion_id AS id, IMVE.person_id AS personId, IMVE.enterprise_id AS enterpriseId, IMVE.practice_id AS practiceId, "
						+ "IMVE.cvx AS cvxCode, IMVE.vaccine_name AS name, IMVE.reason AS reasons, IMVE.start_date AS startDate, IMVE.end_date AS endDate, IMVE.comment, "
						+ "IMVE.created_by AS createdBy, IMVE.create_timestamp AS createTimestamp, IMVE.modified_by AS modifiedBy,IMVE. modify_timestamp AS modifyTimestamp, "
						+ "p.practice_name AS practiceNme,(UM.last_name + ', ' + UM.first_name + ' '+ ISNULL(UM.mi, ' ')) AS excludedBy, CASE WHEN IMVE.delete_ind = 'N' THEN 'false' ELSE 'true' END AS isDeleted  "
						+ "from imm_vaccine_exclusion AS IMVE INNER JOIN user_mstr AS UM ON IMVE.modified_by = UM.user_id "
						+ "INNER JOIN practice p ON IMVE.practice_id = p.practice_id where person_id = '"
						+ parameters.get("personId") + "'";
				
				String[] jsonMetaData = {"id", "personId", "enterpriseId", "practiceId", "cvxCode", "name", "reasons", "startDate;Date_YYYYMMDD HH:MM:SS", "endDate;Date_YYYYMMDD HH:MM:SS", "comment", "createdBy", "createTimestamp;Date_YYYYMMDD HH:MM:SS", 
						"modifiedBy", "modifyTimestamp;Date_YYYYMMDD HH:MM:SS", "practiceName", "excludedBy", "isDeleted"};
				/*String strQuery = "select IMVE.exclusion_id AS id, IMVE.reason AS reasons "
						+ "from imm_vaccine_exclusion AS IMVE INNER JOIN user_mstr AS UM ON IMVE.modified_by = UM.user_id "
						+ "INNER JOIN practice p ON IMVE.practice_id = p.practice_id where person_id = '"
						+ parameters.get("personId") + "'";
				
				String[] jsonMetaData = {"id", "reasons"};*/
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
