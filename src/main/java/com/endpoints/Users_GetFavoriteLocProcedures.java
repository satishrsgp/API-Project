package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Users_GetFavoriteLocProcedures
{



	public static Boolean users_GetFavoriteLocProcedures_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		//System.out.println(strResponse);
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{ 
				String strQuery = "SELECT TOP (26) [Project2].[cpt4_code_id] AS code, [Project2].[description] AS description, "
						+ "[Project2].[service_item_id] AS serviceItemId, CASE WHEN [Project2].[delete_ind] = 'N' THEN 'false' ELSE 'true' "
						+ "END AS [delete_ind] FROM (SELECT [Distinct1].[location_id] AS [location_id], [Distinct1].[created_by] AS [created_by], "
						+ "[Distinct1].[service_item_lib_id] AS [service_item_lib_id], [Distinct1].[service_item_id] AS [service_item_id], "
						+ "[Distinct1].[cpt4_code_id] AS [cpt4_code_id], [Distinct1].[description] AS [description], "
						+ "[Distinct1].[delete_ind] AS [delete_ind] FROM (SELECT DISTINCT [Extent1].[location_id] AS [location_id], "
						+ "[Extent1].[created_by] AS [created_by], [Extent3].[service_item_lib_id] AS [service_item_lib_id], "
						+ "[Extent3].[service_item_id] AS [service_item_id], [Extent3].[cpt4_code_id] AS [cpt4_code_id], "
						+ "[Extent3].[description] AS [description], [Extent3].[delete_ind] AS [delete_ind] "
						+ "FROM [dbo].[patient_procedure] AS [Extent1] INNER JOIN [dbo].[patient_encounter] AS [Extent2] "
						+ "ON [Extent1].[enc_id] = [Extent2].[enc_id] INNER JOIN [dbo].[service_item_mstr] AS [Extent3] "
						+ "ON ([Extent1].[service_item_id] = [Extent3].[service_item_id]) AND ([Extent1].[service_item_lib_id] = "
						+ "[Extent3].[service_item_lib_id])) AS [Distinct1] WHERE ([Distinct1].[created_by] = '"+parameters.get("userId")+"') AND "
						+ "(([Distinct1].[location_id] = '"+parameters.get("locationId")+"') OR (([Distinct1].[location_id] IS NULL) "
						+ "AND ('"+parameters.get("locationId")+"' IS NULL)))) AS [Project2]";
						
				String[] jsonMetaData = {"code","description","serviceItemId","isDeleted"};

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
