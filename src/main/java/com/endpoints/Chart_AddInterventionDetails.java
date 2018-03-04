package com.endpoints;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Map.Entry;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.ValidateResults;

public class Chart_AddInterventionDetails
{


	
	public static Boolean Chart_AddInterventionDetails_Test(HashMap<String, String> parameters, String strResponse,HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;
		
		
		try
		{
			int countAfterPOST = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery));
			if(Math.abs(countAfterPOST - ((int)mapPostDetails.get("countBeforePOST")))  == 1)
			{
				try
				{
					
					String strSQLQuery = "Select top 1"
							+ "intervention as name,"
							+ "case when intervention_category = 'Planned Procedure' then 'PlannedProcedure' when intervention_category = 'Medication Activity' then 'MedicationActivity' when intervention_category = 'Immunization Activity' then 'ImmunizationActivity' when intervention_category = 'Nutrition Recommendation' then 'NutritionRecommendation' else intervention_category end  as category,"
							+ "case when pointer_value is null then null else upper(pointer_value) end as categoryId,"
							+ "intervention_status as status,"
							+ "intervention_start_date as startDate,"
							+ "intervention_frequency as frequency,"
							+ "intervention_review_date as nextReviewDate,"
							+ "intervention_progress as interventionProgress,"
							+ "code_system as codeSystem,"
							+ "code_value as codeValue,"
							+ "code_description as codeDescription "
							+ "from care_plan_intervention_ext_ "
							+ "where person_id = '"+parameters.get("personId")+"' "
							+ "And goal_num = '"+parameters.get("goalId")+"'  "
							+ "And encounterID ='"+parameters.get("encounterId")+"' "
							+ "And health_concern_num = '"+parameters.get("healthConcernId")+"'  "
							+ "order by create_timestamp desc";
					
					ArrayList<Object> listDatabase = DatabaseConnection.addResultSettoArrayList(DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
					
					ArrayList<Object> jsonList = new ArrayList<Object>();
					
					jsonList.add(Objects.equals(parameters.get("name"), "") ? null : (parameters.get("name").toString()));
					jsonList.add(Objects.equals(parameters.get("category"), "") ? null : (parameters.get("category").toString()));
					jsonList.add(Objects.equals(parameters.get("categoryId"), "") ? null : (parameters.get("categoryId").toString().toUpperCase()));
					jsonList.add(Objects.equals(parameters.get("status"), "") ? null : (parameters.get("status")));
					jsonList.add(Objects.equals(parameters.get("startDate"), "") ? null : (parameters.get("startDate").toString().split("T")[0]).replace("-", ""));
					jsonList.add(Objects.equals(parameters.get("frequency"), "") ? null : (parameters.get("frequency")));
					jsonList.add(Objects.equals(parameters.get("nextReviewDate"), "") ? null : (parameters.get("nextReviewDate").toString().split("T")[0]).replace("-", ""));
					jsonList.add(Objects.equals(parameters.get("interventionProgress"), "") ? null : (parameters.get("interventionProgress")));
					String Prmvalue = parameters.get("category");
					if (Objects.equals(Prmvalue, "NutritionRecommendation")) 
					
					{
						String strSQLQuery1 = "Select top 1 "
								+ "encounterDate "								
								+ "from care_plan_intervention_ext_ "
								+ "where person_id = '"+parameters.get("personId")+"' "
								+ "And goal_num = '"+parameters.get("goalId")+"'  "
								+ "And encounterID ='"+parameters.get("encounterId")+"' "
								+ "And health_concern_num = '"+parameters.get("healthConcernId")+"'  "
								+ "order by create_timestamp desc";
						
						Object objIntencounterDate = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery1, "encounterDate");
						
						String strStmt = "{call ngkbm_dbp_careplan_interv_proc (?,?,?,?,?,?)}";
						LinkedHashMap <String, Object> strSPParam = new LinkedHashMap<String, Object>();
						strSPParam.put("Design",0);
						strSPParam.put("enterprise_id",System.getProperty("LoggedInEnterpriseId") );
						strSPParam.put("practice_id",System.getProperty("LoggedInPracticeId") );
						strSPParam.put("person_id",parameters.get("personId") );
						strSPParam.put("category","Nutrition Recommendation");
						strSPParam.put("enc_date",objIntencounterDate );
						
						ArrayList<String> strColumns = new ArrayList<>();
						strColumns.add("description4");
						strColumns.add("description5");
						
						ResultSet rs = null;
						String Codevalue = null,CodeSystem = null;
						CallableStatement callableStmt = BaseTest.connNGA.prepareCall(strStmt);
						for(Entry<String, Object> entry : strSPParam.entrySet())
						{
							callableStmt.setObject(entry.getKey(), entry.getValue());
							//System.out.println(entry.getKey() + " " +entry.getValue());
						}
						boolean results = callableStmt.execute();
						if (results)
						{
							rs = callableStmt.getResultSet();
							while(rs.next())
							{
																
								Codevalue = rs.getString(strColumns.get(0));
								CodeSystem = rs.getString(strColumns.get(1));
								
							}
							
							jsonList.add(CodeSystem);
							jsonList.add(Codevalue);					
							jsonList.add(Objects.equals(parameters.get("codeDescription"), "") ? null : (parameters.get("codeDescription").toString()));
						}
						else
						{
							jsonList.add(Objects.equals(parameters.get("codeSystem"), "") ? null : (parameters.get("codeSystem").toString()));
							jsonList.add(Objects.equals(parameters.get("codeValue"), "") ? null : (parameters.get("codeValue").toString()));					
							jsonList.add(Objects.equals(parameters.get("codeDescription"), "") ? null : (parameters.get("codeDescription").toString()));
						}
						
						
					}
					else
					{
						jsonList.add(Objects.equals(parameters.get("codeSystem"), "") ? null : (parameters.get("codeSystem").toString()));
						jsonList.add(Objects.equals(parameters.get("codeValue"), "") ? null : (parameters.get("codeValue").toString()));					
						jsonList.add(Objects.equals(parameters.get("codeDescription"), "") ? null : (parameters.get("codeDescription").toString()));
					}
					
					
									
					status = ValidateResults.resultValidation(jsonList, listDatabase);
					//status = ValidateResults.resultsvalidation(jsonList, DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
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
				Log.info("The Difference between records count before and after post is more than one, please reexcute once more");
				softAssert.fail("The Difference between records count before and after post is more than one. Please reexecute once more");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}






}
