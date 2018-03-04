package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

import net.minidev.json.JSONArray;

public class Master_GetProcedureGroups 
{

	public static Boolean master_GetProcedureGroups_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "select "
						+ "SGM.service_item_id as serviceItemIds, "
						+ "SG.service_item_group_name as name, "
						+ "SG.description as description  "
						+ "from sim_groups SG , sim_group_members SGM "
						+ "where SG.service_item_lib_id = SGM.service_item_lib_id  "
						+ "and SG.service_item_group_name = SGM.service_item_group_name "
						+ "order by SG.service_item_group_name asc";
				
				ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
				try
				{
					
					Object obj = JsonPath.read(strResponse, "$.."+strUniqueJsonToken);
					ArrayList<Object> jsonList = new ArrayList<>();
					if(obj instanceof JSONArray)
					{
						for(int i = 0; i< ((JSONArray)obj).size(); i++)
						{
							if (JsonPath.read(strResponse, "$.items["+i+"].serviceItemIds").toString().contains(","))
							{
								String[] Arr_serviceItemIds = JsonPath.read(strResponse, "$.items["+i+"].serviceItemIds").toString().split(",");
								for (String S : Arr_serviceItemIds)
								{
									jsonList.add(S.toString().replace("[", "").replace('\"', ' ').replace("]", "").trim());
									jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].name"));
									jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].description"));
								}
								
							}
							else
							{
								String[] Arr_serviceItemIds = {JsonPath.read(strResponse, "$.items["+i+"].serviceItemIds").toString()};
								for (String S : Arr_serviceItemIds)
								{
									jsonList.add(S);
									jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].name"));
									jsonList.add(JsonPath.read(strResponse, "$.items["+i+"].description"));
								}
								
							}
							
						}
					}
					Log.info("JSON LIST is :\n" + jsonList);
					//status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$.."+strUniqueJsonToken, jsonList);
					status = ValidateResults.resultValidation(jsonList, listDatabase);
					
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
