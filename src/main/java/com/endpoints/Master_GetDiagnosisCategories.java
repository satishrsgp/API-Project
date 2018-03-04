package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;


import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetDiagnosisCategories {


	public static Boolean master_GetDiagnosisCategories_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{

				/*String strQuery = "SELECT "
						+ "[Project2].[icd_category_id] AS [icd_category_id],"
						+ "[Project2].[description] AS [description], "
						+ "[Project2].[short_description] AS [short_description], "
						+ "case when [Project2].[delete_ind] = 'N' then 'false' else 'true' end  AS [delete_ind],"
						+ "case when [Project2].[kbm_ind] = 'N' then 'false' else 'true' end  AS [kbm_ind], "
						+ "case when [Project2].[show_in_epm_ind] = 'N' then 'false' else 'true' end  AS [show_in_epm_ind], "
						+ "case when [Project2].[show_in_ehr_ind] = 'N' then 'false' else 'true' end  AS [show_in_ehr_ind], "
						+ "[Project2].[practice_id] AS [practice_id]"
						+ " FROM ( SELECT  "
						+ "[Limit1].[icd_category_id] AS [icd_category_id],  "
						+ "[Limit1].[description] AS [description], "
						+ "[Limit1].[short_description] AS [short_description],  "
						+ "[Limit1].[delete_ind] AS [delete_ind], "
						+ "[Limit1].[kbm_ind] AS [kbm_ind], "
						+ "[Limit1].[show_in_epm_ind] AS [show_in_epm_ind], "
						+ "[Limit1].[show_in_ehr_ind] AS [show_in_ehr_ind], "
						+ "[Limit1].[C1] AS [C1], "
						+ "[Extent2].[practice_id] AS [practice_id], "
						+ "CASE WHEN ([Extent2].[practice_id] IS NULL) THEN CAST(NULL AS int) ELSE 1 END AS [C2] "
						+ "FROM   (SELECT TOP (25) [Project1].[icd_category_id] AS [icd_category_id], [Project1].[description] AS [description], [Project1].[short_description] AS [short_description], [Project1].[delete_ind] AS [delete_ind], [Project1].[kbm_ind] AS [kbm_ind], [Project1].[show_in_epm_ind] AS [show_in_epm_ind], [Project1].[show_in_ehr_ind] AS [show_in_ehr_ind], [Project1].[C1] AS [C1] "
						+ "FROM ( SELECT  "
						+ "[Extent1].[icd_category_id] AS [icd_category_id],"
						+ "[Extent1].[description] AS [description], "
						+ "[Extent1].[short_description] AS [short_description], "
						+ "[Extent1].[delete_ind] AS [delete_ind], "
						+ "[Extent1].[kbm_ind] AS [kbm_ind], "
						+ "[Extent1].[show_in_epm_ind] AS [show_in_epm_ind], "
						+ "[Extent1].[show_in_ehr_ind] AS [show_in_ehr_ind], "
						+ "1 AS [C1] "
						+ "FROM [dbo].[icd_category_mstr] AS [Extent1] "
						+ "WHERE (N'0' = (LTRIM(RTRIM([Extent1].[delete_ind])))) OR (N'n' = (LOWER(LTRIM(RTRIM([Extent1].[delete_ind]))))) "
						+ ")  AS [Project1] "
						+ "ORDER BY [Project1].[description] ASC, [Project1].[icd_category_id] ASC ) AS [Limit1] "
						+ "LEFT OUTER JOIN [dbo].[practice_mstr_files] AS [Extent2] ON [Extent2].[mstr_file_id] = [Limit1].[icd_category_id] "
						+ ")  AS [Project2] "
						+ "ORDER BY [Project2].[description] ASC, [Project2].[icd_category_id] ASC, [Project2].[C2] ASC ";*/
				
				String strQuery = "Select Top 25 "
						+ "icd_category_id as id,"
						+ "description as description,"
						+ "short_description as shortDescription,"
						+ "Case when delete_ind = 'N' then 'false' else 'true' end as isDeleted,"
						+ "Case when kbm_ind = 'N' then 'false' else 'true' end as fromKbm,"
						+ "case when show_in_epm_ind = 'N' then 'false' else 'true' end as showInEpm,"
						+ "case when show_in_ehr_ind = 'N' then 'false' else 'true' end as showInEhr "
						+ "From icd_category_mstr "
						+ "order by description asc,icd_category_id asc";
				
				String[] jsonMetaData = {"id","description","shortDescription","isDeleted","fromKbm","showInEpm","showInEhr"};
				
			/*	ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
						
				Object obj = JsonPath.read(strResponse, "$.."+strUniqueJsonToken);
				ArrayList<Object> jsonList = new ArrayList<>();

				if(obj instanceof JSONArray)
				{
					int index =0;
					for(int i = 0; i< ((JSONArray)obj).size(); i++)
					{
						
						
						
						if(JsonPath.read(strResponse, "$.items["+index+"].practiceIds").toString().contains("[") && JsonPath.read(strResponse, "$.items["+index+"].practiceIds").toString().contains("]") )
						{
							if(JsonPath.read(strResponse, "$.items["+index+"].practiceIds").toString().equals("[]"))
							{
								jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].id").toString().toUpperCase());
								jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].description"));
								jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].shortDescription"));
								jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].isDeleted").toString());
								jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].fromKbm").toString());
								jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].showInEpm").toString());
								jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].showInEhr").toString());
								jsonList.add("");
							}
							else
							{
								int tempIndex = index;
								if(JsonPath.read(strResponse, "$.items["+index+"].practiceIds").toString().contains(","))
								{
									String [] SubArray = (JsonPath.read(strResponse, "$.items["+index+"].practiceIds").toString().replace("[", "").replace("]", "").split(","));
									for (int j=0;j<SubArray.length;j++)
									{
										jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].id").toString().toUpperCase());
										jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].description"));
										jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].shortDescription"));
										jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].isDeleted").toString());
										jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].fromKbm").toString());
										jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].showInEpm").toString());
										jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].showInEhr").toString());
										jsonList.add(SubArray[j].replace("\"", ""));
									}
								}
								else
								{
									jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].id").toString().toUpperCase());
									jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].description"));
									jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].shortDescription"));
									jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].isDeleted").toString());
									jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].fromKbm").toString());
									jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].showInEpm").toString());
									jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].showInEhr").toString());
									jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].practiceIds").toString().replace("[\"", "").replace("\"]", ""));
									
								}
								
							}
						}
						else
						{
							jsonList.add(JsonPath.read(strResponse, "$.items["+index+"].practiceIds"));
						}
						index++;
					}
				}
				Log.info("JSON LIST is :\n" + jsonList);
				//status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$.."+strUniqueJsonToken, jsonList);
				status = ValidateResults.resultValidation(jsonList, listDatabase);*/
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
				Log.info("Error in Test Class and the detail are : \n" + e.getMessage());
				softAssert.fail("Error in Test Script please look at logs");
			}
		}
		else
		{
			status = true;
		}
		return status;
	}



}
