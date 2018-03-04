/**
 * API 1.x route_Abhishek
 */

package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Users_GetUserPractices {

	public static Boolean users_GetUserPractices(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery="select distinct(p.practice_id),p.enterprise_id,p.practice_name,case p.delete_ind when 'n' "
												+ "then 'false' end as delete_ind,pd.def_lab_id,pd.def_rad_id,pd.def_registry_id "
												+ "from user_mstr um inner join user_group_xref ugx on (um.user_id=ugx.user_id and um.delete_ind='N') "
												+ "inner join security_groups sg on sg.group_id=ugx.group_id "
												+ "inner join practice p on (sg.practice_id=p.practice_id and p.delete_ind='N') "
												+ "inner join practice_defaults pd on (pd.practice_id=p.practice_id and p.delete_ind='N') "
												+ "inner join enterprise e on (sg.enterprise_id=e.enterprise_id and e.delete_ind='N') where um.user_id='"+parameters.get("userId")+"'";

				String[] jsonMetaData = {"id","enterpriseId","name","isDeleted","defaultLabId","defaultRadiologyId","defaultRegistryId"};

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
