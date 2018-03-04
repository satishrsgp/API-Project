package com.testscripts;


import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.endpoints.Persons_UpdatePerson;
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

public class Persons_UpdatePerson_TC1 extends BaseTest
{
	SoftAssert softAssert = new SoftAssert();
	
	 //for generating ssn,dob and last_name randomly
	 Random random = new Random();

	@Test
	public void persons_UpdatePerson_TC1()
	{
		boolean boolTestcaseStatus = true, boolTestDataStatus = true;

		String strClassName = getClass().getSimpleName();
		String strSimpleClassName = Log.intializeLoggin(strClassName);
		String strTestDataSheetName = GeneralUtils.sheetName(strSimpleClassName);

		//Creating a new HashMap to store all the Request and Response Details
		HashMap<String, Object[]> hashMapResult = TestResults.generateHashMapResults();

		//Preparing the Map with all the Request details
		Map<String, Object> mapRequestParrameters = RequestParameters.generateRequestParamsHashMap("PUT", "", "");

		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			//Iterating through the Test DataSheet to run the testscript
			for(int k=1; k<=ExcelReaderWriter.getRowCount( BaseTest.mapExecutionConfigs.get("TestDataWorkBook"), strTestDataSheetName); k++)
			{

				HashMap<String, String> parameters = Compute_Paramters.mapperCompute_Parameters(k, strTestDataSheetName);
				
				if(k==1)
				{
					ResultSet rs;
					int randSSN;
					String randLastName;
					String randDob;
					do
					{
									 randLastName=parameters.get("LastName")+random.nextInt(100);
									 randDob=(1900 + random.nextInt(100))+"-"+(String.format("%02d",(1+random.nextInt(12))))+"-"+(String.format("%02d",(1+random.nextInt(30))));

									
					 
					String unique=randLastName+randDob;
					
					
					 rs=DatabaseConnection.statementExecution(BaseTest.connNGA, "Select Concat(last_name,FORMAT (CAST(date_of_birth AS datetime),'yyyy-MM-dd')) from person where Concat(last_name,FORMAT (CAST(date_of_birth AS datetime),'yyyy-MM-dd'))='"+unique+"'");
					
					
					}while(rs.next());
					
					do
					{
					
					 randSSN=(100000000 + random.nextInt(900000000));
					
					
					 rs=DatabaseConnection.statementExecution(BaseTest.connNGA, "Select ssn from person where ssn='"+randSSN+"'");
					
					
					}while(rs.next());
					
					
					
					
					parameters.put("LastName", randLastName);
					parameters.put("DateOfBirth", randDob);
					parameters.put("SocialSecurityNumber", randSSN+"");
				}
				
				mapRequestParrameters.put("strURI", Compute_Paramters.formulateFinalURI(strSimpleClassName, parameters));

				HashMap<String, Object> mapPutDetails = ComputePayLoadDetails.computePUTPayloadMap(parameters, strSimpleClassName);
				mapRequestParrameters.put("strPayLoad", mapPutDetails.get("PayLoad"));
					
				
	
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParrameters);

				hashMapResult = TestsEvaluation.mapperExecuteTests(hashMapResult, objHandler, strSimpleClassName, strTestDataSheetName, softAssert, parameters);

				Object[] testexecutionResult = hashMapResult.get(String.valueOf(k));
				if(Objects.equals(testexecutionResult[testexecutionResult.length-1].toString().toLowerCase(), "pass"))
				{
					if(objHandler.getIntResponseCode() == 200)
					{
						boolTestDataStatus = Persons_UpdatePerson.Persons_UpdatePerson_Test(parameters, softAssert);
					}
					else
					{
						boolTestDataStatus = true;
					}
				}
				else
					boolTestDataStatus = false;

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
