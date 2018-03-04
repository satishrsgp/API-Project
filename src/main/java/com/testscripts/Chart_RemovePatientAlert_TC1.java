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




public class Chart_RemovePatientAlert_TC1 extends BaseTest
{
	SoftAssert softAssert = new SoftAssert();

	@Test
	public void chart_RemovePatientAlert_TC1()
	{
		boolean boolTestcaseStatus = true;
		String strClassName = getClass().getSimpleName();
		String strSimpleClassName = Log.intializeLoggin(strClassName);
		@SuppressWarnings("unused")
		String strTestDataSheetName = GeneralUtils.sheetName(strSimpleClassName);

		
		//Creating a new HashMap to store all the Request and Response Details
		HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();

		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParrameters = RequestParameters.generateRequestParamsHashMap("DELETE", "", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			//Iterating through the Test DataSheet to run the testscript
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{
				HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strClassName);
				mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strClassName, parameters));
				mapRequestParrameters.put("strPayLoad", "");
				String delete_indicator_query= "select delete_ind from patient_alerts_ehr where person_id = '"+parameters.get("personId")+"' and alert_id = '"+parameters.get("alertId")+"'";
				
				String delete_indicator_pre ="" ;
				String delete_indicator_post = "";
				if(ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2).contains("200")){
					delete_indicator_pre = DatabaseConnection.fetchColumnAsObject(connNGA, delete_indicator_query, "delete_ind").toString();
				}
				
				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);
				
				
				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strClassName, strClassName, softAssert, parameters);
				
				Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));

				
				if(Objects.equal(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass"))
				{
					if(objHandler.getIntResponseCode()==200)
					{	
					delete_indicator_post = DatabaseConnection.fetchColumnAsObject(connNGA, delete_indicator_query, "delete_ind").toString();

					if(delete_indicator_pre.equals("N") && delete_indicator_post.equals("Y"))
					{
						boolTestcaseStatus =true;
					}
					else 
					{
						boolTestcaseStatus =false;
					}
					}
				}
				else 
				{
					boolTestcaseStatus =false;
				}
				log.info("\n-------person_id  : " + parameters.get("personId") + " ---- Alert_id : "+ parameters.get("alertId"));
			
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
