package com.testscripts;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


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

public class Chart_DeleteSuspectedDiagnosis_TC1 extends BaseTest
{
	SoftAssert softAssert = new SoftAssert();

	@Test
	public void chart_DeleteSuspectedDiagnosis_TC1()
	{
		boolean boolTestcaseStatus = true, boolTestDataStatus = true;

		String strClassName = getClass().getSimpleName();
		String strSimpleClassName = Log.intializeLoggin(strClassName);
		String strTestDataSheetName = GeneralUtils.sheetName(strSimpleClassName);

		//Creating a new HashMap to store all the Request and Response Details
		HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();

		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParrameters = RequestParameters.generateRequestParamsHashMap("DELETE", "", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			//Iterating through the Test DataSheet to run the testscript
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strTestDataSheetName); k++)
			{
				HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strTestDataSheetName);
				mapRequestParrameters.put("strPayLoad", "");
				mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strSimpleClassName, parameters));

				//Sending the Request to get the Response
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);

				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName, strTestDataSheetName, softAssert, parameters);

				Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));
				//Changed this line if(Objects.equals(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass") && objHandler.getIntResponseCode() == 200)
				//to if(Objects.equals(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass"))
				//After this internally we will do the checking for the cases of 400 and 403 response codes and is its 200 then only proceed with validations
				if(Objects.equals(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass"))
				{
					if(objHandler.getIntResponseCode() == 200)
					{
						String strSQLQuery = "select UPPER(CAST(lod.unique_diag_num AS CHAR(36))) AS id,UPPER(CAST(lod.order_num AS CHAR(36))) AS orderNumber,UPPER(CAST(ln.person_id AS CHAR(36))) AS personId,ln.enterprise_id as enterpriseId, "
								+"ln.practice_id as practiceId,UPPER(CAST(lod.order_test_id AS CHAR(36))) AS orderTestId,UPPER(CAST(lod.diagnosis_code_lib_id AS CHAR(36))) AS diagnosisCodeLibraryId, "
								+"lod.diagnosis_code_id as diagnosisCodeId,dcm.description as description,dcm.icd9cm_code_id as icd9cmCodeId,dcm.user_description as userDescription "
								+"from lab_order_diag lod inner join lab_nor ln on lod.order_num=ln.order_num "
								+"inner join diagnosis_code_mstr dcm on (lod.diagnosis_code_lib_id=dcm.diagnosis_code_lib_id and lod.diagnosis_code_id=dcm.diagnosis_code_id) "
								+"where ln.person_id='"+parameters.get("personId")+"' and ln.order_num='"+parameters.get("orderId")+"' and lod.order_test_id='"+parameters.get("testId")+"' and lod.unique_diag_num='"+parameters.get("diagnosisId")+"'";
						
						int countAfterDelete = DatabaseConnection.returnNumberOfRows(DatabaseConnection.statementExecution(BaseTest.connNGA, strSQLQuery));
						if(Objects.equals(countAfterDelete, 0))
						{
							boolTestDataStatus = true;
							Log.info("Record sucessfuly deleted");
						}
						else
						{
							boolTestDataStatus = false;
							Log.info("The Difference between records count before and after post is more than one, please reexcute once more");
							softAssert.fail("The Difference between records count before and after post is more than one. Please reexecute once");
							
						}
					}
					else
					{
						boolTestDataStatus = true;
					}
				}
				else
				{
					boolTestDataStatus = false;
				}
				/*else if(objHandler.getIntResponseCode() == 200)
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