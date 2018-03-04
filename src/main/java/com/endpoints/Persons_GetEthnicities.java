package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Persons_GetEthnicities {

	public static Boolean persons_GetEthnicities_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery = "SELECT "
						   + "pex.person_id AS person_id, "
					       + "pex.ethnicity_item_id AS id, "
					       + "ml.mstr_list_item_desc AS [description], "
					       + "pex.ethnicity_order AS [order], "
					       + "ecm.code AS categoryId, "
					       + "ct.description AS categoryName, "
					       + "iml.external_rec_id AS cdcEthnicityCode "
					        
					       + "FROM     [person_ethnicity_xref] AS pex "
					       + "INNER JOIN [mstr_lists] AS ml ON pex.[ethnicity_item_id] = ml.[mstr_list_item_id] "
					       + "LEFT OUTER JOIN [ethnicity_category_mapping] AS ecm ON ml.[mstr_list_item_id] = ecm.[mstr_list_item_id] "
					       + "LEFT OUTER JOIN [code_tables] AS ct ON (ecm.[code] = ct.[code]) AND (N'ethnicity_category' = ct.[code_type]) "
					       + "LEFT OUTER JOIN [intrf_mstr_lists] AS iml ON (ml.[mstr_list_item_id] = iml.[internal_rec_id]) AND (80080 = iml.[external_system_id]) "
					       + "WHERE (N'ethnicity' = ml.[mstr_list_type]) AND (pex.[person_id] = '"+parameters.get("personId")+"') "
					     
					       + "ORDER BY pex.ethnicity_item_id ASC ";
				
				String[] jsonMetaData = {"personId","id","description","order","categoryId","categoryName","cdcEthnicityCode"};
				
				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..personId", jsonList);
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
