package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_Person_Races
{

	/*
	 * ********************************************************************************
	 * Class Name						:Master_Person_Races
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

	public static Boolean master_Person_Races_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		//System.out.println(strResponse);
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{ 
				String strQuery = "SELECT prx.race_item_id AS id,  "
						+ "prx.person_id AS person_id, "
						+"ml.mstr_list_item_desc AS [description], "
						+"prx.race_order AS [order], "
						+"rcm.code AS categoryId, "
						+"ct.description AS categoryName, "
						+"iml.external_rec_id AS cdcRaceCode "   
						+"FROM  [dbo].[person_race_xref] AS prx  "
						+ " INNER JOIN [dbo].[mstr_lists] AS ml ON prx.race_item_id = ml.mstr_list_item_id "
						+ "LEFT OUTER JOIN [dbo].[race_category_mapping] AS rcm ON ml.[mstr_list_item_id] = rcm.[mstr_list_item_id] "
						+ "  LEFT OUTER JOIN [dbo].[code_tables] AS ct ON (rcm.[code] = ct.[code]) AND (N'race_category' = ct.[code_type]) "
						+ "LEFT OUTER JOIN [dbo].[intrf_mstr_lists] AS iml ON (ml.[mstr_list_item_id] = iml.[internal_rec_id]) AND (80080 = iml.[external_system_id]) "
						+ " WHERE (N'race' = ml.[mstr_list_type]) AND (prx.[person_id] = '"+parameters.get("personId")+"') "
						+ "ORDER BY prx.race_order ASC";
				
						
		
				//
				String[] jsonMetaData = {"id;GUID","personId;GUID","description;Normal","order","categoryId;Normal","categoryName;Normal","cdcRaceCode;Normal"};

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
