package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetAllergies 
{
	public static Boolean master_GetAllergies_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String  strQuery = "SELECT top 25 Rtrim(Ltrim([Project4].[C4] )) AS [description],[Project4].[C2] AS [allergyId], [Project4].[C3] AS [allergyType] "
						+ "FROM (SELECT [Distinct1].[C1] AS [C1], [Distinct1].[C2] AS [C2], [Distinct1].[C3] AS [C3], [Distinct1].[C4] AS [C4],  "
						+ "[Distinct1].[C5] AS [C5] FROM (SELECT DISTINCT [UnionAll1].[C1] AS [C1], [UnionAll1].[C2] AS [C2],  "
						+ "[UnionAll1].[description] AS [C3], [UnionAll1].[dam_concept_id_desc] AS [C4], [UnionAll1].[allergy_type_id] AS [C5]  "
						+ "FROM (SELECT 1 AS [C1], CAST([Extent1].[dam_concept_id] AS NVARCHAR(MAX)) AS [C2], [Extent2].[description] AS [description],  "
						+ "[Extent1].[dam_concept_id_desc] AS [dam_concept_id_desc], [Extent2].[allergy_type_id] AS [allergy_type_id] FROM [dbo].[rdamapm0] "
						+ " AS [Extent1] INNER JOIN [dbo].[fdb_allergy_type_mstr] AS [Extent2] ON [Extent1].[dam_concept_id_typ] = CAST ([Extent2].[allergy_type_id]  "
						+ "AS DECIMAL (19,0)) UNION ALL SELECT 1 AS [C1], [Extent3].[allergy_id] AS [allergy_id], [Extent4].[description] AS [description],  "
						+ "[Extent3].[description] AS [description1], [Extent3].[allergy_type_id] AS [allergy_type_id] FROM [dbo].[allergy_mstr] AS [Extent3]  "
						+ "INNER JOIN [dbo].[fdb_allergy_type_mstr] AS [Extent4] ON [Extent3].[allergy_type_id] = [Extent4].[allergy_type_id]  "
						+ "WHERE [Extent3].[allergy_type_id] = 5) AS [UnionAll1]) AS [Distinct1]) AS [Project4] ORDER BY [Project4].[C2] ASC  ";
				
				String[] jsonMetaData = {"description","allergyId","allergyType"};
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..allergyId", jsonList);
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
