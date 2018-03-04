package com.testscripts;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.utils.API_Request_RetObj;
import com.utils.Compute_Paramters;
import com.utils.DatabaseConnection;
import com.utils.ExcelReaderWriter;
import com.utils.Log;
import com.utils.RequestParameters;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.TestsEvaluation;


/*
 * ********************************************************************
 * Class Name						: Chart_AddNewPatientAlert_TC1
 * Created By						: 
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: personId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Adds a single alert message based on  alert_type_id 
 * ******************************************************************
 */

public class Chart_AddNewPatientAlert_TC1 extends BaseTest
{
	SoftAssert softAssert = new SoftAssert();

	@Test
	public void chart_AddNewPatientAlert_TC1()
	{
		boolean boolTestcaseStatus = true;
		String strClassName = getClass().getSimpleName();
		String strSimpleClassName = Log.intializeLoggin(strClassName);
		/*String strSimpleClassName = strClassName.substring(0, strClassName.length()-4);
		System.out.println("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		Log.info("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());*/

		//Creating a new HashMap to store all the Request and Response Details
		HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();

		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParrameters = RequestParameters.generateRequestParamsHashMap("POST", "", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			//Iterating through the Test DataSheet to run the testscript
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{
				HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strClassName);
				mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strClassName, parameters));

				String payLoad = "{\"MessageId\":\""+parameters.get("MessageId") +"\""
				+",\"AlertTypeId\":\""+parameters.get("Alerttypeid") +"\""
				+",\"Comment\":\""+parameters.get("Comment") +"\""
				+",\"IsFlagged\":\""+parameters.get("IsFlagged") +"\""
				+",\"IsCustomAlert\":\""+parameters.get("IsCustomAlert") +"\""
			    +",\"UserDescription\":\""+parameters.get("UserDescription") 
				+ "\"}";
				
				
				mapRequestParrameters.put("strPayLoad", payLoad);	
				String message="";
				String row_Count_Query = "select count(*) as count from patient_alerts_ehr where person_id = '"+parameters.get("personId")+"'";
				String before_rowcount = DatabaseConnection.fetchColumnAsObject(connNGA, row_Count_Query, "count").toString();
				
				
				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);
				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName, strClassName, softAssert, parameters);
				
				
			
				String strSQLQuery = "select *  from patient_alerts_ehr where person_id = '"+parameters.get("personId")+"'";
				String after_rowcount = DatabaseConnection.fetchColumnAsObject(connNGA, row_Count_Query, "count").toString();
				if(objHandler.getIntResponseCode()==201){
					 message = DatabaseConnection.fetchColumnAsObject(connNGA, strSQLQuery, "comment").toString();
						boolTestcaseStatus = message.contains(parameters.get("Comment")) && Integer.parseInt(after_rowcount)==(Integer.parseInt(before_rowcount)+1);
						log.info("\n------- Message added to the AlertTypeId " +  parameters.get("Alerttypeid")  +" ANd  Person_ID : "+parameters.get("personId") +":" +parameters.get("comment"));
				}
				
				else
				{
					boolTestcaseStatus=String.valueOf(objHandler.getIntResponseCode()).contains(ExcelReaderWriter.getCellValue(BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 7));
					log.info("\n------- No alerts added to the AlertTypeId " +  parameters.get("Alerttypeid")  +" ANd  Person_ID : "+parameters.get("personId") );
				}
				
			
                hashMapResult = TestsEvaluation.updateHashMap(hashMapResult, k, boolTestcaseStatus);
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
