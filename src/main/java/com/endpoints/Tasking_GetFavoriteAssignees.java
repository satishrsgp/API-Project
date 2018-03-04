package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.google.common.base.Objects;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Tasking_GetFavoriteAssignees
{

	/*
	 * ********************************************************************************
	 * Class Name						:Resources_GetResourceAppointmen
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

	public static Boolean tasking_GetFavoriteAssignees_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		//System.out.println(strResponse);
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{ 
				String strSQLFavsQuery = "select "
						+ "workgroup_id as id,"
					//	+" concat(user_id,'false') as primaryKey,"
					+ "enterprise_id as enterpriseId,practice_id as practiceId,workgroup_name as name,'true' as isGroup" 
					+" from task_workgroup_mstr WGM where WGM.workgroup_id in (GRP_IDS)"
		
						+" union  select " 
					//	+" concat(workgroup_id,'true') as primaryKey,"
						+ "user_id as id,"
					+ "enterprise_id as enterpriseId,practice_id as practiceId,last_name+', '+first_name as name,'false' as isGroup" 
					+" from user_mstr where user_id in (USR_IDS)" ;
		
					
				String strSQLQuery = "SELECT item_value from user_pref where user_id="+System.getProperty("LoggedInUserId")+" and item_name like 'TaskRecipFav%%' and enterprise_id='"+System.getProperty("LoggedInEnterpriseId")+"' and practice_id='"+System.getProperty("LoggedInPracticeId")+"'";
				Object obj1 = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "item_value");
				
				if(!Objects.equal(obj1, null))
				{
					String strGroup="";
					String strUser = "";
					String[] strValues = obj1.toString().replace(" ", ",").split(",");
					for(String str1: strValues)
					{
						if(str1.startsWith("G"))
						{
							strGroup = strGroup+ str1+",";
						}
						else if(str1.startsWith("U"))
						{
							strUser = strUser + str1+",";
						}
					}
					if(strGroup.isEmpty())
					{
						strGroup = "null";
					}
					else if(strGroup.endsWith(","))
					{
						strGroup = strGroup.substring(0, strGroup.length()-1).replace("G", "");
					}
					
					if(strUser.isEmpty())
					{
						strUser = "null";
					}
					else if(strUser.endsWith(","))
					{
						strUser = strUser.substring(0, strUser.length()-1).replace("U", "");
					}
					strSQLFavsQuery = strSQLFavsQuery.replace("USR_IDS", strUser).replace("GRP_IDS", strGroup);
				}
						
		
				//
				String[] jsonMetaData = {"id","enterpriseId","practiceId","name","isGroup"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLFavsQuery), strResponse, "$..id", jsonList);
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
