package com.testscripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.google.common.base.Objects;
import com.utils.API_Request_RetObj;
import com.utils.ComputePayLoadDetails;
import com.utils.Compute_Paramters;
import com.utils.DatabaseConnection;
import com.utils.ExcelReaderWriter;
import com.utils.Log;
import com.utils.RequestParameters;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.TestsEvaluation;

public class Tasking_Respond_TC1 extends BaseTest {

	SoftAssert softAssert = new SoftAssert();

	@Test
	public void tasking_Respond_TC1()
	{
		String strClassName = getClass().getSimpleName();
		String strSimpleClassName = Log.intializeLoggin(strClassName);
		boolean boolTestcaseStatus = true;
		boolean boolTestDataStatus = true;

		//Creating a new HashMap to store all the Request and Response Details
		HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();

		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParrameters = RequestParameters.generateRequestParamsHashMap("PUT", "", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			//Iterating through the Test DataSheet to run the testscript
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{

				HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strClassName);
				mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strClassName, parameters));

				HashMap<String, Object> mapPutDetails = ComputePayLoadDetails.computePUTPayloadMap(parameters, strSimpleClassName);
				
				//Assignee token reading before sending request to make sure that it is in Y status  ....
				
				String query_Task_Assign="select task_assgn  from user_todo_list where task_id = '"+parameters.get("taskId")+"'";  
				mapRequestParrameters.put("strPayLoad", mapPutDetails.get("PayLoad"));

				
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);
			
				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strClassName,strClassName, softAssert, parameters);

				Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));
				
				if(Objects.equal(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass"))
				{
					if(objHandler.getIntResponseCode()==200)
				{	

				boolTestcaseStatus = DatabaseConnection.fetchColumnAsObject(connNGA, query_Task_Assign, "task_assgn").toString().contains(parameters.get("response"))    ;
				}
				}
				else
				{
					boolTestcaseStatus=false;
				}
				
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
