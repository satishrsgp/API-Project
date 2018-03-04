/**
 * API 1.x route_Abhishek- Modified at script level to match with the XML
 */

package com.endpoints;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.jayway.jsonpath.JsonPath;
import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

import net.minidev.json.JSONArray;

public class Users_GetUserOptions {

	public static Boolean users_GetUserOptions(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery="select config_value from surescripts_config where config_id='NG_SEND_ERX_CREATE_DATE_LIMIT' UNION select CASE pl.preference_value WHEN 0 then 'false' else 'true' end as preference_value from preferences p "
						+ "inner join preference_list pl on p.preference_id=pl.preference_id where pl.preference_id = 1421 UNION select Convert(VARCHAR(MAX), Convert(varbinary(max), [data])) as binary from user_component_preference where name like '%Med Search Options-fav%' "
						+ "AND user_id="+parameters.get("userId")+" AND enterprise_id="+System.getProperty("LoggedInEnterpriseId")+" and practice_id=" +System.getProperty("LoggedInPracticeId")+" UNION select preference_value from preference_list "
						+ "where preference_value='vital_signs_.wt_kg_graph' or preference_value='wt_lb_graph'or preference_value='ht_in_graph' or preference_value='vital_signs_.ht_cm_graph' or preference_value='head_circ_in' or preference_value='vital_signs_.head_circ_cm' "
						+ "or preference_value='vital_signs_.temp_deg_F' or preference_value='vital_signs_.bp_systolic' or preference_value='vital_signs_.bp_diastolic' or preference_value='vital_signs_.BMI_calc' or preference_value='vital_signs_.pulse_rate' or preference_value='vital_signs_.respiration_rate' "
						+ "or preference_value='vitalSignsDate' or preference_value='vital_signs_.sp_o2_dtl' or preference_value='vital_signs_.txt_pulse_ox_amb' or preference_value='vital_signs_.waist_circ_in' or preference_value='vital_signs_.hip_circ_in' or preference_value='vital_signs_.FiO2' or preference_value='vital_signs_.peakFlow'";

				/*String[] jsonMetaData = {"providerFavoriteMedicationsXml","sendErxCreatedDayLimit","displayRenewals","weightColumn","heightColumn","headCircumferenceColumn","bmiColumn","measurementDateColumn",
						"temperatureColumn","systolicBloodPressureColumn","diastolicBloodPressureColumn","heartRateColumn","respirationRateColumn","pulseOximetryRestColumn","pulseOximetryAmbColumn",
						"waistCircumferenceColumn","hipCircumferenceColumn","fio2PercentageColumn","peakFlowColumn","metricHeightColumn","metricWeightColumn","metricHeadCircumferenceColumn"};*/

				try
				{	
					Object obj = JsonPath.read(strResponse, "$..displayRenewals");
					ArrayList<Object> jsonList = new ArrayList<>();      
					if (obj instanceof JSONArray)
					{
						
							if(JsonPath.read(strResponse, "$.providerFavoriteMedicationsXml")!=null){
								jsonList.add((Object)JsonPath.read(strResponse, "$.providerFavoriteMedicationsXml").toString().substring(1));
							}
							else
							{
								jsonList.add((Object)JsonPath.read(strResponse, "$.providerFavoriteMedicationsXml"));
							}

							jsonList.add(JsonPath.read(strResponse, "$.displayRenewals").toString());
							jsonList.add(JsonPath.read(strResponse, "$.sendErxCreatedDayLimit").toString());
							jsonList.add(JsonPath.read(strResponse, "$.weightColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.metricWeightColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.heightColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.metricHeightColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.headCircumferenceColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.metricHeadCircumferenceColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.temperatureColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.systolicBloodPressureColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.diastolicBloodPressureColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.bmiColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.heartRateColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.respirationRateColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.measurementDateColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.pulseOximetryAmbColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.pulseOximetryRestColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.waistCircumferenceColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.hipCircumferenceColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.fio2PercentageColumn").toString());
							jsonList.add(JsonPath.read(strResponse, "$.peakFlowColumn").toString());
							
						ResultSet rs = DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery);
						status = ValidateResults.resultsvalidation(jsonList, rs);
					
					
					//ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					//Log.info("JSON LIST is :\n" + jsonList);
					//status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..displayRenewals", jsonList);
					//objHandler = FinalAssertions.assertResponseCount(jsonMetaData, objHandler, DatabaseConnection.statementExecution(connNGA, strQuery), "id");
				} 
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
