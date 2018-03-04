package com.endpoints;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.jayway.jsonpath.JsonPath;
import com.utils.API_Request_RetObj;
import com.utils.DatabaseConnection;
import com.utils.Log;
import com.utils.RequestResponseHandler;
import com.utils.TestResults;
import com.utils.ValidateResults;

import net.minidev.json.JSONArray;

public class Medication_MasterFiles_GET extends BaseTest_obsl
{


	SoftAssert softAssert = new SoftAssert();

	@SuppressWarnings("null")
	@Test
	public void medication_MasterFiles_GET()
	{
		String strClassName = getClass().getSimpleName();
		System.out.println("Running test case " + getClass().getSimpleName()+ " with Thread Id:- " + Thread.currentThread().getId());
		Log.info("Running test case " + getClass().getSimpleName()+ " with Thread Id:- " + Thread.currentThread().getId());
		boolean boolTestcaseStatus = true;
		//ResultSet rs = null;
		HashMap<String, Object[]> hashMapResult = new HashMap<>();
		hashMapResult.put("0", new Object[] {"Parameters","RequestURI", "RequestHeaders","ResponseCode","ResponseHeaders", "Response", "Status"});
		hashMapResult.put("0", new Object[] {"Parameters","RequestURI", "RequestHeaders","ResponseCode","ResponseHeaders", "Response", "Status"});
		//Fetch the URL from Route Repository
		String strURI = BaseTest_obsl.mapURLRepo.get(strClassName);
		//String strQueryString = strURI.substring(strURI.indexOf("?"), strURI.length());
		Map<String, Object> mapRequestParrameters = new HashMap<String, Object>();
		mapRequestParrameters.put("strRequestType", "GET");
		mapRequestParrameters.put("strPayLoad", "");
		mapRequestParrameters.put("strQueryString", "");
		mapRequestParrameters.put("strURI",strURI);


		RequestResponseHandler objHandler = new RequestResponseHandler();

		try
		{
			objHandler = API_Request_RetObj.sendRequest(httpClient,mapRequestParrameters);

			if (objHandler != null)
			{
				String strResponse = objHandler.getStrResponse();
				ValidateResults.validateStatusCode(objHandler,200);

				if (!strResponse.isEmpty())
				{
					if (!strResponse.contains("The request is invalid"))
					{

						//logic seems to be like when it is 0 it indicates its a generic medicine and when it is NOT 0 its a brand medicine and generic_medid value(e.g 157915 )is the medid for its generic medication.

						String strSQLQuery="SELECT top 25 n.med_name_id, n.med_name, n.medid,n.medid_desc, "
								+ "CASE n.GENERIC_MEDID WHEN 0 THEN 'true' ELSE 'false' END AS is_generic_ind,CASE m.hidden_flag WHEN ' ' THEN 'true' ELSE 'false' END as hidden_flag "
								+ "FROM fdb_med_name_search_mstr n  "
								+ "left outer JOIN medication_hidden m ON n.medid = m.medid where (m.hidden_flag = 'N' or m.hidden_flag IS NULL) order by med_name_id,medid_desc asc";


						Object objItems = JsonPath.read(strResponse, "$.items");
						System.out.println(objItems);
						System.out.println(((JSONArray)objItems).size());
						ArrayList<Object> jsonList = new ArrayList<Object>();

						if(objItems instanceof JSONArray)
						{
							for(int m=0; m< ((JSONArray)objItems).size(); m++)
							{
								jsonList.add(JsonPath.read(strResponse, "$.items["+m+"].brandId"));
								jsonList.add(JsonPath.read(strResponse, "$.items["+m+"].medicationName"));
								jsonList.add(JsonPath.read(strResponse, "$.items["+m+"].medicationId"));
								jsonList.add(JsonPath.read(strResponse, "$.items["+m+"].medicationIdDesccription"));
								jsonList.add(JsonPath.read(strResponse, "$.items["+m+"].isGeneric"));
								jsonList.add(JsonPath.read(strResponse, "$.items["+m+"].isHidden"));


							}

							ResultSet rs = DatabaseConnection.statementExecution(connNGA, strSQLQuery);
							ValidateResults.resultsvalidation(jsonList, rs, objHandler);
							hashMapResult.put("1", new Object[]{"",objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});

						}
						else
						{
							Log.info("It's a Bad Request");
							Log.info(objHandler.getStrResponse());
							hashMapResult.put("1", new Object[]{" " ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
						}
					}
					else
					{
						Log.info("It's a Bad Request");
						Log.info(objHandler.getStrResponse());
						hashMapResult.put("1", new Object[]{" " ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"Bad Request no Response Headers", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
					}
				}

				else if(! (objHandler.getIntResponseCode() == HttpStatus.SC_NOT_FOUND))
				{
					//System.out.println("The API Call executed is : \n" + mapRequestParrameters.get("strURI") + "\n");
					Log.info("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + HttpStatus.SC_OK);
					softAssert.fail("We have received the Status Code " + objHandler.getIntResponseCode() + " where as the expected Status Code is " + HttpStatus.SC_OK + " ");

					//Log.info("It's a Bad Request");
					Log.info(objHandler.getStrResponse());
					hashMapResult.put("1", new Object[]{"", objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
					objHandler.setStrRequestStatus("Pass");
				}
				else
				{
					softAssert.fail("The Request URI: " + objHandler.getStrRequestURI() +  " could not be found on the Server ");
					hashMapResult.put("1", new Object[]{"", objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),objHandler.getStrResponseHeaders(), strResponse, objHandler.getStrRequestStatus()});
					objHandler.setStrRequestStatus("Fail");
				}
			}
			else
			{
				boolTestcaseStatus = false;
				log.info("The Request could not be sent as the Request Type is not a valid Http Method");
				softAssert.fail("The Request could not be sent as the Request Type is not a valid Http Method ");
				hashMapResult.put("1", new Object[]{"" ,objHandler.getStrRequestURI(), objHandler.getStrRequestHeaders(),objHandler.getIntResponseCode(),"The requested resource could not be found", objHandler.getStrResponse(), objHandler.getStrRequestStatus()});
			}
			if (objHandler.getStrRequestStatus().contains("Fail"))
			{
				boolTestcaseStatus = false;
				softAssert.fail("Test case fails ");
			}
			objHandler = null;

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
