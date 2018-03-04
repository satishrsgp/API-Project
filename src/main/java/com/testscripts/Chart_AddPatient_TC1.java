package com.testscripts;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.endpoints.Chart_AddPatient;
import com.utils.API_Request_RetObj;
import com.utils.ComputePayLoadDetails;
import com.utils.Compute_Paramters;
import com.utils.DatabaseConnection;
import com.utils.ExcelReaderWriter;
import com.utils.GeneralUtils;
import com.utils.Log;
import com.utils.RequestParameters;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.TestsEvaluation;

public class Chart_AddPatient_TC1 extends BaseTest
{

	SoftAssert softAssert = new SoftAssert();

	@Test(dependsOnMethods={"com.testscripts.Persons_AddPerson_TC1.persons_AddPerson_TC1"})
	public void chart_AddPatient_TC1()
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

			new Persons_AddPerson_TC1(httpClient).persons_AddPerson_TC1();
			
			ResultSet rs=DatabaseConnection.statementExecution(BaseTest.connNGA, "Select top 1 person_id From person order by create_timestamp desc");
			rs.next();
			
			
			//Iterating through the Test DataSheet to run the testscript
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strTestDataSheetName); k++)
			{

				HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strTestDataSheetName);
				parameters.put("personId", rs.getString("person_id"));
		
				mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strSimpleClassName, parameters));

				
				String strCountQuery = "Select * from patient";
					
				
				HashMap<String, Object> mapPostDetails = ComputePayLoadDetails.computePayLoadMap(parameters, strCountQuery, strSimpleClassName);
				mapRequestParrameters.put("strPayLoad", mapPostDetails.get("PayLoad"));

			

				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);

				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName, strTestDataSheetName, softAssert, parameters);

				Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));
				if(Objects.equals(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass") && objHandler.getIntResponseCode() == 201)
				{
					String strResponse = objHandler.getStrResponse();
					boolTestDataStatus = Chart_AddPatient.Chart_AddPatient_Test(parameters,strResponse, mapPostDetails, softAssert, strCountQuery);
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
