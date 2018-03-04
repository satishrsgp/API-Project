package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

/*
 * ********************************************************************
 * Class Name						: Users_GetLoginDefaults
 * Created By						: Seema Jaffary
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: 
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							:
 * ******************************************************************
 */

public class Users_GetLoginDefaults {


	public static Boolean users_GetLoginDefaults_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "select item_value from user_pref where item_name like '%Api Defaults%' and user_id='"+parameters.get("userId") +"'";
						
				//String[] jsonMetaData = {"providerId","lastName","firstName","middleName","isDeleted","degree","practiceId","enterpriseId", "userId"};
				
				try
				{	
					 ArrayList<Object> jsonList = new ArrayList<>();
                     jsonList.add(JsonPath.read(strResponse,"$.enterpriseId"));
                     jsonList.add(JsonPath.read(strResponse,"$.practiceId"));
                     jsonList.add(JsonPath.read(strResponse, "$.locationId").toString().toUpperCase());
                     jsonList.add(JsonPath.read(strResponse, "$.providerId").toString().toUpperCase());
                     jsonList.add(JsonPath.read(strResponse,"$.timeZone"));
                
                     
                     System.out.println("The JSON Response in a List: \n" + jsonList + "\n");

                     Object obj1 = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strQuery, "item_value");
                     ArrayList<Object> dbList = new ArrayList<>();
                     if(!Objects.equals(obj1, "0") && !Objects.equals(obj1, null))
                     {
                                    String[] strValues = obj1.toString().replace("{","").replace("}","").split(",");
                                    for(String str1 : strValues)
                                    {
                                                   if(str1.toString().startsWith("\"enterprise_id\""))
                                                   {
                                                                  if(Objects.equals(str1.toString().substring(str1.toString().indexOf(":")+1), ""))
                                                                  {
                                                                                 dbList.add("");
                                                                  }
                                                                  else
                                                                  {
                                                                                 dbList.add(str1.toString().substring(str1.toString().indexOf(":")+1).replace("\"", ""));
                                                                  }
                                                   }
                                                   else if(str1.toString().startsWith("\"practice_id\""))
                                                   {
                                                                  if(Objects.equals(str1.toString().substring(str1.toString().indexOf(":")+1), ""))
                                                                  {
                                                                                 dbList.add("");
                                                                  }
                                                                  else
                                                                  {
                                                                                 dbList.add(str1.toString().substring(str1.toString().indexOf(":")+1).replace("\"", ""));
                                                                  }
                                                   }
                                                   else if(str1.toString().startsWith("\"location_id\""))
                                                   {
                                                                  if(Objects.equals(str1.toString().substring(str1.toString().indexOf(":")+1), ""))
                                                                  {
                                                                                 dbList.add("");
                                                                  }
                                                                  else
                                                                  {
                                                                                 dbList.add(str1.toString().substring(str1.toString().indexOf(":")+1).replace("\"", "").toUpperCase());
                                                                  }
                                                   }
                                                   else if(str1.toString().startsWith("\"provider_id\""))
                                                   {
                                                                  if(Objects.equals(str1.toString().substring(str1.toString().indexOf(":")+1), ""))
                                                                  {
                                                                                 dbList.add("");
                                                                  }
                                                                  else
                                                                  {
                                                                                 dbList.add(str1.toString().substring(str1.toString().indexOf(":")+1).replace("\"", "").toUpperCase());
                                                                  }
                                                   }
                                                   else if(str1.toString().startsWith("\"timezone\""))
                                                   {
                                                                  if(Objects.equals(str1.toString().substring(str1.toString().indexOf(":")+1), ""))
                                                                  {
                                                                                 dbList.add("");
                                                                  }
                                                                  else
                                                                  {
                                                                                 dbList.add(str1.toString().substring(str1.toString().indexOf(":")+1).replace("\"", ""));
                                                                  }
                                                   }
                                    }
                     }
                     else
                     {
                                    dbList.add(null);
                                    dbList.add(null);
                                    dbList.add("00000000-0000-0000-0000-000000000000");
                                    dbList.add("00000000-0000-0000-0000-000000000000");
                                    dbList.add(null);
                     }
                     
                     status =  ValidateResults.resultValidation(jsonList, dbList);

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
