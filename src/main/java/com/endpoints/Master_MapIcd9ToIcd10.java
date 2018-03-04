package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

import net.minidev.json.JSONArray;

public class Master_MapIcd9ToIcd10 
{
	public static Boolean master_MapIcd9ToIcd10_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
// Rank is a calculated field 	(exec ng_search_diagnosis @pi_search = 'v44.0', @pi_diag_lib_id = '7193C8FC-6D05-4BF0-933D-083B09680C7B')
				String strStmt = "{call ng_search_legacy (?,?,?)}";
				LinkedHashMap <String, Object> strSPParam = new LinkedHashMap<String, Object>();
				strSPParam.put("pi_search",parameters.get("ICD9code") );
				strSPParam.put("pi_encounter_date",null );
				strSPParam.put("pi_diag_lib_id",parameters.get("DiagLibID") );
								
				ArrayList<String> strColumns = new ArrayList<>();
				strColumns.add("ICD");
				strColumns.add("ICD_description");
				strColumns.add("TERM");
				strColumns.add("HCC_ID");
				strColumns.add("provider_id");
				strColumns.add("HCC_Description");
				strColumns.add("comm_coefficient");
				strColumns.add("HCC_inst_coefficient");
				strColumns.add("RxHCC_ID");
				strColumns.add("RxHCC_Description");
				strColumns.add("RxHCC_inst_coefficient");
				strColumns.add("LowIncome_Under65");
				strColumns.add("LowIncome_Over65");
				strColumns.add("Under65");
				strColumns.add("Over65");
				strColumns.add("Rank");
								
				Object obj = JsonPath.read(strResponse, "$.."+strUniqueJsonToken);
				ArrayList<Object> jsonList = new ArrayList<>();
					
				if(obj instanceof JSONArray)
				{
					for(int i = 0; i< ((JSONArray)obj).size(); i++)
					{
						jsonList.add(JsonPath.read(strResponse, "$..["+i+"].clinicalDescription"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].billingDescription"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].billingDescription"));						
						jsonList.add(JsonPath.read(strResponse, "$..["+i+"].icdCode"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].hccCommunityCoefficient"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].hccCommunityCoefficient"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].hccInstitutionalCoefficient"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].hccInstitutionalCoefficient"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].hccDescription"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].hccDescription"));						
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].hccId"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].hccId"));						
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].rxHccCoefficientNonLowIncomeOver65"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].rxHccCoefficientNonLowIncomeOver65"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].rxHccCoefficientNonLowIncomeUnder65"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].rxHccCoefficientNonLowIncomeUnder65"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].rxHccCoefficientLowIncomeOver65"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].rxHccCoefficientLowIncomeOver65"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].rxHccCoefficientLowIncomeUnder65"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].rxHccCoefficientLowIncomeUnder65"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].rxHccInstitutionalCoefficient"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].rxHccInstitutionalCoefficient"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].rxHccDescription"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].rxHccDescription"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].rxHccId"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].rxHccId"));
						
					}
				}
				Log.info("JSON LIST is :\n" + jsonList);
				Map<String , ArrayList<Object>> mList = DatabaseConnection.addSPResultsToArrayList(BaseTest.connNGA, strStmt, strSPParam, strColumns, 0, 25, jsonList);
				Log.info("JSON LIST is :\n" + mList.get("dbList"));
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
