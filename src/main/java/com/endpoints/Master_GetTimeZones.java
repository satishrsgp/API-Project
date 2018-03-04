/**
 * API 1.x route_Abhishek
 */

package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Master_GetTimeZones {

	public static Boolean master_GetTimeZones(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{


				String strQuery="SELECT TOP (25) zn.[zone_name] AS [zone_name], zn.[utc_offset] AS [utc_offset] FROM (SELECT DISTINCT zn1.[zone_name] AS [zone_name] "
						+ "FROM [dbo].[timezone_zones] AS zn1 WHERE zn1.[until_year] >= (DATEPART (year, SysDateTime())) ) AS [Distinct1] "
						+ "OUTER APPLY (SELECT TOP (1) [Project2].[zone_name] AS [zone_name], [Project2].[until_year] AS [until_year], "
						+ "[Project2].[utc_offset] AS [utc_offset] FROM ( SELECT zn2.[zone_name] AS [zone_name], zn2.[until_year] AS [until_year], "
						+ "zn2.[utc_offset] AS [utc_offset] FROM [dbo].[timezone_zones] AS zn2 WHERE (zn2.[until_year] >= (DATEPART (year, SysDateTime()))) "
						+ "AND ([Distinct1].[zone_name] = zn2.[zone_name]) ) AS [Project2] ORDER BY [Project2].[until_year] ASC ) AS zn "
						+ "ORDER BY zn.[utc_offset] ASC, zn.[zone_name] ASC";

				String[] jsonMetaData = {"zoneName","utcOffset"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..zoneName", jsonList);
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
