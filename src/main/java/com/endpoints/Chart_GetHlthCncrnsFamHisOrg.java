package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetHlthCncrnsFamHisOrg {

	public static Boolean chart_GetHlthCncrnsFamHisOrg(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{


				String strQuery="select seq_no,enterprise_id,practice_id,person_id,family_mbr_dtl,family_mbr_name,case when deceased like 'N' then 'false' when deceased like 'Y' then 'true' else '' end as deceased , "
						+ "age_dtl,dx_dtl,code_system,code,create_timestamp " 

						+ "from Family_Hx_Dtl_ where person_id='"+parameters.get("personId")+"'";

				String[] jsonMetaData = {"id","enterpriseId","practiceId","personId","relationship","familyMember","isDeceased","age","condition","codeSystem","code","createTimestamp"};

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
