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

/*
 * ********************************************************************
 * Class Name						: Prvdr_GtPrvdrDiagnsesByCatgry
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

public class Prvdr_GtPrvdrDiagnsesByCatgry 
{
	public static Boolean prvdr_GtPrvdrDiagnsesByCatgry_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
// Rank is a calculated field 	(exec ng_search_diagnosis @pi_search = 'v44.0', @pi_diag_lib_id = '7193C8FC-6D05-4BF0-933D-083B09680C7B')
				String strStmt = "{call ng_search_categories (?,?,?,?,?,?,?,?,?)}";
				LinkedHashMap <String, Object> strSPParam = new LinkedHashMap<String, Object>();
				strSPParam.put("pi_search", null );
				strSPParam.put("pi_category_id",parameters.get("categoryId") );
				strSPParam.put("pi_provider_id",parameters.get("providerId") );
				strSPParam.put("pi_encounter_date",null );
				strSPParam.put("pi_billing_search","Y" );
				strSPParam.put("pi_patient_id",null );
				strSPParam.put("pi_user",null );
				//strSPParam.put("pi_user",System.getProperty("LoggedInUserId") );
				strSPParam.put("pi_diag_lib_id",parameters.get("DiagLibId") );
				//strSPParam.put("pi_diag_lib_id",null );
				strSPParam.put("pi_practice_id",null);
						
				ArrayList<String> strColumns = new ArrayList<>();
				strColumns.add("RANK");
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
				strColumns.add("ICD");
				strColumns.add("Term");
	
				
				Object obj = JsonPath.read(strResponse, "$.."+strUniqueJsonToken);
				ArrayList<Object> jsonList = new ArrayList<>();
					
				if(obj instanceof JSONArray)
				{
					for(int i = 0; i< ((JSONArray)obj).size(); i++)
					{
						
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].rank"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].rank"));					
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
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].icdCode"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].icdCode"));
						jsonList.add(Objects.equals(JsonPath.read(strResponse, "$..["+i+"].clinicalDescription"), null) ? null: JsonPath.read(strResponse, "$..["+i+"].clinicalDescription"));
					}
				}
				Log.info("JSON LIST is :\n" + jsonList);
				Map<String , ArrayList<Object>> mList = DatabaseConnection.addSPResultsToArrayList(BaseTest.connNGA, strStmt, strSPParam, strColumns, 0, 38, jsonList);
				Log.info("DB LIST is :\n" + mList.get("dbList"));
				status = ValidateResults.resultValidation(mList.get("jsonList"),mList.get("dbList") );
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
