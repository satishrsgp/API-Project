package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetProcedures
{

	/*
	 * ********************************************************************************
	 * Class Name						:Master_GetProcedures
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

	public static Boolean master_GetProcedures_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{ 
				String strQuery = "select top 25  "
						+ "cpt4_code_id as code, "
						+"description as description, "
						+"service_item_id as serviceItemId, "
					//+"delete_ind as isDeleted "
						 + "CASE  WHEN delete_ind = 'N' THEN 'false' ELSE 'true'  END AS isDeleted "
					        
						+"from service_item_mstr  "
						+ "where eff_date <= '20171113' and exp_date>='20171113' and service_item_lib_id ='3a04faad-48b8-48ed-90c0-fa45cfa2eecb'"
						+ "ORDER BY [description] ASC, [cpt4_code_id] ASC, [delete_ind] ASC, [service_item_id] ASC";
				
				//String[] jsonMetaData = {"code;GUID", "description;Normal","serviceItemId;Normal","isDeleted;Boolean"};
				String[] jsonMetaData = {"code", "description","serviceItemId","isDeleted"};
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$.."+strUniqueJsonToken, jsonList);
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
