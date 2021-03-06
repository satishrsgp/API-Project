package com.testscripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


import java.util.*;

import com.endpoints.Chart_AddPatCareplanOutDetails;
import com.utils.API_Request_RetObj;
import com.utils.ComputePayLoadDetails;
import com.utils.Compute_Paramters;
import com.utils.ExcelReaderWriter;
import com.utils.GeneralUtils;
import com.utils.Log;
import com.utils.RequestParameters;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.TestsEvaluation;

public class Chart_AddPatCareplanOutDetails_TC1 extends BaseTest
{
	SoftAssert softAssert = new SoftAssert();

	@Test
	public void chart_AddPatCareplanOutDetails_TC1()
	{
		boolean boolTestcaseStatus = true, boolTestDataStatus = true;

		String strClassName = getClass().getSimpleName();
		String strSimpleClassName = Log.intializeLoggin(strClassName);
		String strTestDataSheetName = GeneralUtils.sheetName(strSimpleClassName);

		//Creating a new HashMap to store all the Request and Response Details
		HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();

		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParrameters = RequestParameters.generateRequestParamsHashMap("POST", "", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			//Iterating through the Test DataSheet to run the testscript
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strTestDataSheetName); k++)
			{

				HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strTestDataSheetName);
				mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strSimpleClassName, parameters));

				String strCountQuery = "select "
                        + "concat(outcome_num,intervention_num,health_concern_num,goal_num) as primKey, "
                        + "person_id as personId,enc_id as encounterId,enterprise_id as enterpriseId,practice_id as practiceId, "
                        + "Case When del_ind = 'Y' then 'true' else 'false' end as isDeleted, "
                        + "case when goal_achieved = 'Yes' then 'true' else 'false' end as goalAchieved, "
                        + "goal_achieved_details as goalAchievedDetails,goal_num as goalId,health_concern_num as healthConcernId, "
                        + "intervention_num as interventionId,outcome as description,outcome_date as outcomeDate,outcome_num as outcomeId, "
                        + "created_by as createdBy,create_timestamp as createTimestamp,create_timestamp_tz as createTimestampTimeZone, "
                        + "modified_by as modifiedBy,modify_timestamp as modifyTimestamp,modify_timestamp_tz as modifyTimestampTimeZone " 
                        + "From care_plan_outcomes_ext_ " 
                        + "Where person_id = '"+parameters.get("personId")+"' and enc_id ='"+parameters.get("encounterId")+"' and health_concern_num = '"+parameters.get("healthConcernId")+"' and goal_num = '"+parameters.get("goalId")+"' and  intervention_num = '"+parameters.get("interventionId")+"'";
				
				
				HashMap<String, Object> mapPostDetails = ComputePayLoadDetails.computePayLoadMap(parameters, strCountQuery, strSimpleClassName);
				mapRequestParrameters.put("strPayLoad", mapPostDetails.get("PayLoad"));

				/*HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strTestDataSheetName);
				String strPayload = "{\"Note\":\"" + parameters.get("noteBody") + "\"}";
				mapRequestParrameters.put("strPayLoad", strPayload);
				mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strSimpleClassName, parameters));*/

				/*String strSQLQuery = "select count(pnx.note_id) as counts from patient_problems pp inner join problem_note_xref pnx on pp.uniq_id=pnx.problem_id where pp.person_id='"+parameters.get("personId")+"' and pp.uniq_id='"+parameters.get("problemId")+"' and pnx.delete_ind ='N'";
				int beforeCounts = (int) DatabaseConnection.fetchColumnAsObject(connNGA,strSQLQuery,"counts");*/

				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);

				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName, strTestDataSheetName, softAssert, parameters);

				Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));
				if(Objects.equals(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass") && objHandler.getIntResponseCode() == 200)
				{
					String strResponse = objHandler.getStrResponse();
					boolTestDataStatus = Chart_AddPatCareplanOutDetails.Chart_AddPatientCareplanOutcomeDetails_Test(parameters, strResponse,mapPostDetails, softAssert, strCountQuery);
					//boolTestDataStatus = Chart_AddPatientCareplanOutcomeDetails.Chart_AddPatientCareplanOutcomeDetails_Test(parameters, mapPostDetails, softAssert, strCountQuery);
				}
				else if(objHandler.getIntResponseCode() == 200)
					boolTestDataStatus = false;

				/*Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));
				if(Objects.equal(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass"))
				{
					int afterCounts = (int) DatabaseConnection.fetchColumnAsObject(connNGA,strSQLQuery,"counts");
					if(afterCounts == (beforeCounts+1))
					{
						boolTestDataStatus = Chart_AddNote.Chart_AddProblemNote_Test(parameters, softAssert);
					}
					else
					{
						boolTestDataStatus = false;
					}
				}
				else
					boolTestDataStatus = false;*/

				boolTestcaseStatus = boolTestcaseStatus && boolTestDataStatus;

				//Update the pass/fail and update the hashMapResult
				hashMapResult = TestsEvaluation.updateHashMap(hashMapResult, k, boolTestDataStatus);
				objHandler = TestsEvaluation.updateStatus(objHandler, softAssert, boolTestDataStatus, parameters);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		//Preparing the TestResults Excel Sheet and updating with all the Request and Response Details.
		TestResults.writeResults(strClassName, hashMapResult, boolTestcaseStatus);

		softAssert.assertAll();
	}
}
