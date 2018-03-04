package com.testscripts;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.google.common.base.Objects;
import com.utils.API_Request_RetObj;
import com.utils.Compute_Paramters;
import com.utils.DatabaseConnection;
import com.utils.ExcelReaderWriter;
import com.utils.GeneralUtils;
import com.utils.Log;
import com.utils.RequestParameters;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.TestsEvaluation;

public class Persons_CreateEligibilityRequest_TC1 extends BaseTest
{

	SoftAssert softAssert = new SoftAssert();

	@Test
	public void persons_CreateEligibilityRequest_TC1()
	{
		boolean boolTestcaseStatus = true;

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
				int request_Timestamp_after = 0;
				HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strTestDataSheetName);
				mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strClassName, parameters));
				String query ="select count(*) request_Timestamp from patient_eligibility where person_id = '"+ parameters.get("personId")+"'";
				int request_Timestamp_Before=0;
				if(Objects.equal(DatabaseConnection.fetchColumnAsObject(connNGA, query, "request_Timestamp"), 0))
				request_Timestamp_Before =	Integer.parseInt(DatabaseConnection.fetchColumnAsObject(connNGA, query, "request_Timestamp").toString());
				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);

				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName,strTestDataSheetName, softAssert, parameters);
				
				if(objHandler.getIntResponseCode()==200){
					 request_Timestamp_after = Integer.parseInt(DatabaseConnection.fetchColumnAsObject(connNGA, query, "request_Timestamp").toString());
				}
						
				if(request_Timestamp_after==request_Timestamp_Before+1  &&  objHandler.getIntResponseCode()==200){
					log.info("Creates an eligibility request for the given person id : "+parameters.get("personId"));
					boolTestcaseStatus=true;
				}
				else if(objHandler.getIntResponseCode()==200)
					{
					
					boolTestcaseStatus=false;
					
					}
				//Update the pass/fail and update the hashMapResult
				//hashMapResult = TestsEvaluation.updateHashMap(hashMapResult, k, boolTestcaseStatus);
				objHandler = TestsEvaluation.updateStatus(objHandler, softAssert, boolTestcaseStatus, parameters);
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