package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

import net.minidev.json.JSONArray;

public class Master_SearchPatientProblems {

	public static Boolean master_SearchPatientProblems_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{

				String strStmt = "{call ng_search_snomed (?)}";
				LinkedHashMap <String, Object> strSPParam = new LinkedHashMap<String, Object>();
				strSPParam.put("pi_search",parameters.get("ProblemSearch") );
				
				ArrayList<String> strColumns = new ArrayList<>();
				strColumns.add("snomed");
				strColumns.add("CONCEPT_ID");
				strColumns.add("FSN");
				
				Object obj = JsonPath.read(strResponse, "$.."+strUniqueJsonToken);
				ArrayList<Object> jsonList = new ArrayList<>();
				int jsonSize = ((JSONArray)obj).size()+1;
					
				if(obj instanceof JSONArray)
				{
					for(int i = 0; i< ((JSONArray)obj).size(); i++)
					{
				
						jsonList.add(JsonPath.read(strResponse, "$.["+i+"].description"));
						if (JsonPath.read(strResponse, "$.["+i+"].conceptId").toString().contains("E"))
						{
							String[] str_Arr = JsonPath.read(strResponse, "$.["+i+"].conceptId").toString().split("E");
							if(str_Arr[0].substring(str_Arr[0].toString().lastIndexOf(".")+1).length() < Integer.valueOf(str_Arr[1]))
							{
								String strA = str_Arr[0].substring(str_Arr[0].toString().lastIndexOf(".")+1);
								strA = StringUtils.rightPad(strA, Integer.valueOf(str_Arr[1]), "0");
								String FinalStr = str_Arr[0].substring(0, str_Arr[0].toString().lastIndexOf(".")).concat(strA);
								jsonList.add(FinalStr);
							}
							else
							{
								jsonList.add(str_Arr[0].substring(0, str_Arr[0].toString().lastIndexOf(".")) + str_Arr[0].substring(str_Arr[0].toString().lastIndexOf(".")+1));
							}
							
						}
						else
						{
							String str_A = JsonPath.read(strResponse, "$.["+i+"].conceptId").toString();
							if(str_A.contains("."))
							{
								jsonList.add(str_A.substring(0, str_A.toString().lastIndexOf(".")));
														
							}
							else
							{
								jsonList.add(JsonPath.read(strResponse, "$.["+i+"].conceptId"));
							}
							
						}
						jsonList.add(JsonPath.read(strResponse, "$.["+i+"].fullySpecifiedName"));
					}
				}
				Log.info("JSON LIST is :\n" + jsonList);
				Map<String , ArrayList<Object>> mList = DatabaseConnection.addSPResultsToArrayList(BaseTest.connNGA, strStmt, strSPParam, strColumns, 0, jsonSize, jsonList);
				status = ValidateResults.resultValidation(mList.get("jsonList"),mList.get("dbList") );
				
				//status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$.."+strUniqueJsonToken, jsonList);
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
