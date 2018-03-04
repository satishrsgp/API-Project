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

public class Master_SearchDiagnoses {


	
	public static Boolean master_SearchDiagnoses_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strStmt = "{call ng_search_diagnosis (?,?,?,?,?,?,?,?,?)}";
				LinkedHashMap <String, Object> strSPParam = new LinkedHashMap<String, Object>();
				strSPParam.put("pi_search",parameters.get("searchText") );
				strSPParam.put("pi_encounter_date",null );
				strSPParam.put("pi_billing_search","N" );
				strSPParam.put("pi_patient_id",null );
				strSPParam.put("pi_user",null );
				strSPParam.put("pi_diag_lib_id",parameters.get("DiagLibID") );
				strSPParam.put("pi_practice_id",null);
				strSPParam.put("pi_kbm_search","Y");
				strSPParam.put("pi_enterprise_id","0001" );
								
				ArrayList<String> strColumns = new ArrayList<>();
				strColumns.add("comm_coefficient");
				strColumns.add("HCC_inst_coefficient");
				strColumns.add("HCC_Description");
				strColumns.add("HCC_ID");
				strColumns.add("NGHCC_ID");
				strColumns.add("NGRxHCC_ID");
				strColumns.add("Over65");
				strColumns.add("Under65");
				strColumns.add("LowIncome_Over65");
				strColumns.add("LowIncome_Under65");
				strColumns.add("RxHCC_inst_coefficient");
				strColumns.add("RxHCC_Description");
				strColumns.add("RxHCC_ID");
				strColumns.add("ICD_description");
				strColumns.add("Rank");				
				strColumns.add("ICD");				
				strColumns.add("term");	
								
				Object obj = JsonPath.read(strResponse, "$.."+strUniqueJsonToken);
				ArrayList<Object> jsonList = new ArrayList<>();
					
				if(obj instanceof JSONArray)
				{
					for(int i = 0; i< ((JSONArray)obj).size(); i++)
					{
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].hccCommunityCoefficient"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].hccCommunityCoefficient"));					
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].hccInstitutionalCoefficient"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].hccInstitutionalCoefficient"));						
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].hccDescription"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].hccDescription"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].hccId"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].hccId"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].ngHccId"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].ngHccId"));						
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].ngRxHccId"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].ngRxHccId"));						
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].rxHccCoefficientNonLowIncomeOver65"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].rxHccCoefficientNonLowIncomeOver65"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].rxHccCoefficientNonLowIncomeUnder65"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].rxHccCoefficientNonLowIncomeUnder65"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].rxHccCoefficientLowIncomeOver65"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].rxHccCoefficientLowIncomeOver65"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].rxHccCoefficientLowIncomeUnder65"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].rxHccCoefficientLowIncomeUnder65"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].rxHccInstitutionalCoefficient"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].rxHccInstitutionalCoefficient"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].rxHccDescription"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].rxHccDescription"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].rxHccId"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].rxHccId"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].billingDescription"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].billingDescription"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].rank"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].rank"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].icdCode"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].icdCode"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].clinicalDescription"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].clinicalDescription"));
						
					}
				}
				Log.info("JSON LIST is :\n" + jsonList);
				Map<String , ArrayList<Object>> mList = DatabaseConnection.addSPResultsToArrayList(BaseTest.connNGA, strStmt, strSPParam, strColumns, 0, 101, jsonList);
				Log.info("DB LIST is :\n" + mList.get("dbList"));
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
