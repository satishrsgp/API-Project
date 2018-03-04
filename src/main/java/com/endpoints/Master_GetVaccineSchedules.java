package com.endpoints;

import java.util.ArrayList;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.Log;

import net.minidev.json.JSONArray;

public class Master_GetVaccineSchedules
{
	public static Boolean master_GetVaccineSchedules_Test(String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strQuery =null;
			if(strResponse.contains("$skip=25"))
			{
				strQuery= "select top 25 CAST(practice_id AS varchar)+','+CAST(vaccine_group AS varchar)+','+CAST(start_age AS varchar)+','+CAST(end_age AS varchar)+','+case when delete_ind='N' then 'false' else 'true' end as primaryKey, "
						+"practice_id as practiceId,vaccine_group as vaccineGroup,start_age as startAge,end_age as endAge,sex, note,case when delete_ind='N' then 'false' else 'true' end as isDeleted from imm_cdc_schedule "
						+"where delete_ind = 'N' order by practiceId,vaccineGroup,sex,start_age,end_age,delete_ind,note";
			}
			else
			{
				strQuery= "select CAST(practice_id AS varchar)+','+CAST(vaccine_group AS varchar)+','+CAST(start_age AS varchar)+','+CAST(end_age AS varchar)+','+case when delete_ind='N' then 'false' else 'true' end primaryKey, "
						+"practice_id as practiceId,vaccine_group as vaccineGroup,start_age as startAge,end_age as endAge,sex, note,case when delete_ind='N' then 'false' else 'true' end as isDeleted from imm_cdc_schedule "
						+"where delete_ind = 'N' order by practiceId,vaccineGroup,sex,start_age,end_age,delete_ind,note";
			}

			try
			{	
				Object obj = JsonPath.read(strResponse, "$..startAge");
				ArrayList<Object> jsonList = new ArrayList<>();
				if (obj instanceof JSONArray)
				{
					for(int j = 0; j< ((JSONArray) obj).size(); j++)
					{
						jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].practiceId").toString()+","+JsonPath.read(strResponse,"$.items["+j+"].vaccineGroup").toString()+","+JsonPath.read(strResponse,"$.items["+j+"].startAge").toString()+","+JsonPath.read(strResponse,"$.items["+j+"].endAge").toString()+","+JsonPath.read(strResponse,"$.items["+j+"].isDeleted").toString());
						jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].practiceId"));
						jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].vaccineGroup"));
						jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].startAge"));
						jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].endAge"));
						jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].sex"));
						jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].note"));
						jsonList.add(JsonPath.read(strResponse,"$.items["+j+"].isDeleted").toString());
					}
				}
				Log.info("JSON LIST is :\n" + jsonList);
				status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..startAge", jsonList);
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
		return status;
	}
}