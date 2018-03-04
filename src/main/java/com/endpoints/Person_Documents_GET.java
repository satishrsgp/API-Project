package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

/*
 * ********************************************************************
 * Class Name						: Person_Documents_GET
 * Created By						: Guruprasad
 * Created Date						: 
 * Modified Date					:
 * Modified By						:
 * Parameters						: personId
 * Test Case ID						:
 * Test Case Author					: 
 * Test Case Modified By			:
 * Test Case Reviewed By			:
 * Purpose							: Route will fetch the details of documents associated to a person based on the personId
 * ******************************************************************
 */

public class Person_Documents_GET extends BaseTest_obsl
{
	public static Boolean Person_Documents_GET_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = null;
				if(strResponse.contains("$skip=25"))
				{
					strQuery = "select top 25 * from ("
							+" select "
							+" document_id as id,person_id as personId,document_desc as description,1 as itemType,'EHRDocument' as itemTypeDescription,create_timestamp as createTimestamp,create_timestamp_tz as createTimestampTimezone,"
							+" modify_timestamp as modifyTimestamp,modify_timestamp_tz as modifyTimestampTimezone,enc_id as encounterId"
							+" from patient_documents where person_id='"+parameters.get("personId")+"'"
							+" union all "
							+" select" 
							+" image_id as id,person_id as personId,image_desc as description,2 as itemType,'EHRImage' as itemTypeDescription,create_timestamp as createTimestamp,create_timestamp_tz as createTimestampTimezone,"
							+" modify_timestamp as modifyTimestamp,modify_timestamp_tz as modifyTimestampTimezone,enc_id as encounterId"
							+" from patient_images where person_id='"+parameters.get("personId")+"'"
							+" union all "
							+" select"
							+" pii.document_id as id,pii.person_id as personId,dtm.description as description,0 as itemType,'ICS' as itemTypeDescription,pii.create_timestamp as createTimestamp,pii.create_timestamp_tz as createTimestampTimezone,"
							+" pii.modify_timestamp as modifyTimestamp,pii.modify_timestamp_tz as modifyTimestampTimezone,pii.enc_id as encounterId"
							+" from"
							+" patient_ics_images pii inner join document d on pii.document_id=d.document_id"
							+" inner join doc_type_mstr dtm on d.doc_type_id=dtm.doc_type_id"
							+" where pii.person_id='"+parameters.get("personId")+"') tbl"
							+" order by 3";
				}
				else
				{
					strQuery = "select * from ("
							+" select "
							+" document_id as id,person_id as personId,document_desc as description,1 as itemType,'EHRDocument' as itemTypeDescription,create_timestamp as createTimestamp,create_timestamp_tz as createTimestampTimezone,"
							+" modify_timestamp as modifyTimestamp,modify_timestamp_tz as modifyTimestampTimezone,enc_id as encounterId"
							+" from patient_documents where person_id='"+parameters.get("personId")+"'"
							+" union all "
							+" select " 
							+" image_id as id,person_id as personId,image_desc as description,2 as itemType,'EHRImage' as itemTypeDescription,create_timestamp as createTimestamp,create_timestamp_tz as createTimestampTimezone,"
							+" modify_timestamp as modifyTimestamp,modify_timestamp_tz as modifyTimestampTimezone,enc_id as encounterId"
							+" from patient_images where person_id='"+parameters.get("personId")+"'"
							+" union all "
							+" select"
							+" pii.document_id as id,pii.person_id as personId,dtm.description as description,0 as itemType,'ICS' as itemTypeDescription,pii.create_timestamp as createTimestamp,pii.create_timestamp_tz as createTimestampTimezone,"
							+" pii.modify_timestamp as modifyTimestamp,pii.modify_timestamp_tz as modifyTimestampTimezone,pii.enc_id as encounterId"
							+" from"
							+" patient_ics_images pii inner join document d on pii.document_id=d.document_id"
							+" inner join doc_type_mstr dtm on d.doc_type_id=dtm.doc_type_id"
							+" where pii.person_id='"+parameters.get("personId")+"') tbl"
							+" order by 3";
				}

				String[] jsonMetaData = {"id","personId","description","itemType","itemTypeDescription","createTimestamp","createTimestampTimezone","modifyTimestamp","modifyTimestampTimezone","encounterId"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..id", jsonList);
					//objHandler = FinalAssertions.assertResponseCount(jsonMetaData, objHandler, DatabaseConnection.statementExecution(connNGA, strQuery), "id");
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

/*public class Person_Documents_GET extends BaseTest_obsl
{

	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void person_Documents_GET()
	{
		String strClassName = getClass().getSimpleName();
		System.out.println("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		Log.info("Running test case " + strClassName+ " with Thread Id:- " + Thread.currentThread().getId());
		boolean boolTestcaseStatus = true;
		HashMap<String, Object[]> hashMapResult = new HashMap<>();
		hashMapResult.put("0", new Object[] {"Parameters","RequestURI", "RequestHeaders","ResponseCode","ResponseHeaders", "Response", "Status"});
		Map<String, Object> mapRequestParameters = new HashMap<String, Object>();
		mapRequestParameters.put("strRequestType", "GET");
		mapRequestParameters.put("strPayLoad", "");
		mapRequestParameters.put("strQueryString", "");
		String strQuery=null;

		RequestResponseHandler objHandler = new RequestResponseHandler();
		try
		{
			for(int k=1; k<=ExcelReaderWriter.getRowCount(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName); k++)
			{
				String personId = ExcelReaderWriter.getCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 0);
				if (personId.equalsIgnoreCase("null"))
					personId="";

				mapRequestParameters.put("strURI", BaseTest_obsl.mapURLRepo.get(strClassName).replace("personId", personId));
				objHandler = API_Request_RetObj.sendRequest(httpClient, mapRequestParameters);

				if (objHandler != null)
				{
					String strResponse = objHandler.getStrResponse();
					ValidateResults.validateStatusCode(objHandler, ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));

					if(objHandler.getStrRequestStatus().equalsIgnoreCase("Pass"))
					{
						if(objHandler.getIntResponseCode() == 200)
							ValidateSchema.validateSchema(objHandler.getStrResponse(), "./src/main/java/Resources/APIv2.0_JSONSchema/DocumentSummary.json");
						if (!strResponse.isEmpty())
						{
							if (!strResponse.contains("is not valid for Guid"))
							{
								if(strResponse.contains("$skip=25"))
								{
									strQuery = "select top 25 * from ("
											+" select "
											+" document_id as id,document_desc as description,1 as itemType,'EHRDocument' as itemTypeDescription,create_timestamp as createTimestamp,create_timestamp_tz as createTimestampTimezone,"
											+" modify_timestamp as modifyTimestamp,modify_timestamp_tz as modifyTimestampTimezone,enc_id as encounterId"
											+" from patient_documents where person_id='"+personId+"'"
											+" union all "
											+" select" 
											+" image_id as id,image_desc as description,2 as itemType,'EHRImage' as itemTypeDescription,create_timestamp as createTimestamp,create_timestamp_tz as createTimestampTimezone,"
											+" modify_timestamp as modifyTimestamp,modify_timestamp_tz as modifyTimestampTimezone,enc_id as encounterId"
											+" from patient_images where person_id='"+personId+"'"
											+" union all "
											+" select"
											+" pii.document_id as id,dtm.description as description,0 as itemType,'ICS' as itemTypeDescription,pii.create_timestamp as createTimestamp,pii.create_timestamp_tz as createTimestampTimezone,"
											+" pii.modify_timestamp as modifyTimestamp,pii.modify_timestamp_tz as modifyTimestampTimezone,pii.enc_id as encounterId"
											+" from"
											+" patient_ics_images pii inner join document d on pii.document_id=d.document_id"
											+" inner join doc_type_mstr dtm on d.doc_type_id=dtm.doc_type_id"
											+" where pii.person_id='"+personId+"') tbl"
											+" order by 3";
								}
								else
								{
									strQuery = "select * from ("
											+" select "
											+" document_id as id,document_desc as description,1 as itemType,'EHRDocument' as itemTypeDescription,create_timestamp as createTimestamp,create_timestamp_tz as createTimestampTimezone,"
											+" modify_timestamp as modifyTimestamp,modify_timestamp_tz as modifyTimestampTimezone,enc_id as encounterId"
											+" from patient_documents where person_id='"+personId+"'"
											+" union all "
											+" select " 
											+" image_id as id,image_desc as description,2 as itemType,'EHRImage' as itemTypeDescription,create_timestamp as createTimestamp,create_timestamp_tz as createTimestampTimezone,"
											+" modify_timestamp as modifyTimestamp,modify_timestamp_tz as modifyTimestampTimezone,enc_id as encounterId"
											+" from patient_images where person_id='"+personId+"'"
											+" union all "
											+" select"
											+" pii.document_id as id,dtm.description as description,0 as itemType,'ICS' as itemTypeDescription,pii.create_timestamp as createTimestamp,pii.create_timestamp_tz as createTimestampTimezone,"
											+" pii.modify_timestamp as modifyTimestamp,pii.modify_timestamp_tz as modifyTimestampTimezone,pii.enc_id as encounterId"
											+" from"
											+" patient_ics_images pii inner join document d on pii.document_id=d.document_id"
											+" inner join doc_type_mstr dtm on d.doc_type_id=dtm.doc_type_id"
											+" where pii.person_id='"+personId+"') tbl"
											+" order by 3";
								}

								try
								{
									Object obj = JsonPath.read(strResponse, "$..id");
									ArrayList<Object> jsonList = new ArrayList<>();       
									if (obj instanceof JSONArray)
									{
										for(int j = 0; j< ((JSONArray) obj).size(); j++)
										{
											jsonList.add(JsonPath.read(strResponse,"$.["+j+"].id").toString().toUpperCase());
											jsonList.add(JsonPath.read(strResponse,"$.["+j+"].description"));
											jsonList.add(JsonPath.read(strResponse,"$.["+j+"].itemType"));
											jsonList.add(JsonPath.read(strResponse,"$.["+j+"].itemTypeDescription"));
											jsonList.add(JsonPath.read(strResponse,"$.["+j+"].createTimestamp").toString().replace("T", " "));
											jsonList.add(JsonPath.read(strResponse,"$.["+j+"].createTimestampTimezone"));
											jsonList.add(JsonPath.read(strResponse,"$.["+j+"].modifyTimestamp").toString().replace("T"," "));
											jsonList.add(JsonPath.read(strResponse,"$.["+j+"].modifyTimestampTimezone"));
											jsonList.add((JsonPath.read(strResponse,"$.["+j+"].encounterId") == null) ? null : JsonPath.read(strResponse,"$.["+j+"].encounterId").toString().toUpperCase());
										}
									}
									System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
									System.out.println("The JSON Response in a List: \n" + jsonList + "\n");

									ResultSet rs = DatabaseConnection.statementExecution(connNGA, strQuery);
									int intNumberOfRows = DatabaseConnection.returnNumberOfRows(rs);
									//Check the number of rows and if it is same as the number of ID's in jsonList then only proceed to validation
									if( intNumberOfRows == ((JSONArray)obj).size())
									{
										ValidateResults.resultsvalidation(jsonList, rs, objHandler);
									}
									else
									{
										System.out.println("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
										System.out.println("The JSON List size is :1 and the SQL Result Set Row Count is : " + intNumberOfRows);
										Log.info("The JSON List returned does not match with the SQL Result Set. Please Review the API Call");
										objHandler.setStrRequestStatus("Fail");
									}
								} 
								catch (Exception e)
								{
									e.printStackTrace();
									log.info("Error in Test Class and the detail are : \n" + e.getMessage());
									softAssert.fail("Error in Test Script please look at logs");
									objHandler.setStrRequestStatus("Fail");
								}
								hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
							}
							//if the request is treated as invalid
							else
							{
								Log.info("It's a Bad Request");
								Log.info(objHandler.getStrResponse());
								hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
							}
						}
						//if response is empty but expected and actual response codes are same
						else
						{
							System.out.println("The API Call executed is : \n" + mapRequestParameters.get("strURI") + "\n");
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							System.out.println("It's a Bad Request \n");
							hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}
					//if actual status code is not equal to expected status code
					else if(! (objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND))
					{
						Log.info("We have recieved the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2));
						softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + ExcelReaderWriter.getIntCellValue(BaseTest_obsl.mapExecutionConfigs.get("TestDataWorkBook"), strClassName, k, 2) + " ");
						Log.info(objHandler.getStrResponse());
						hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Pass");
					}
					else
					{
						softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server ");
						hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						objHandler.setStrRequestStatus("Fail");
					}
				}
				//if objHandler is null meaning bad type of request method
				else
				{

					boolTestcaseStatus = false;
					log.info("The Request could not be sent as the Request Type is not a valid Http Method");
					softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
					hashMapResult.put(String.valueOf(k), new Object[]{"personId=" + personId ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
				}

				if (objHandler.getStrRequestStatus().contains("Fail"))
				{
					boolTestcaseStatus = false;
					softAssert.fail("Test case fails with data: "+"personId=" + personId);
				}

				objHandler = null;
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
}*/