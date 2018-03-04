package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class GetHealthConcernsTobaccoUsageStatus 
{

public static Boolean getHealthConcernsTobaccoUsageStatus_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
{
Boolean status=false;
if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
{
try
{
String strQuery = "SELECT top 1 pe.person_id AS personId, pe.enterprise_id AS enterpriseId, pe.practice_id AS practiceId,"
+ " pe.enc_timestamp AS encounterDate, tu.txt_tobacco_type AS tobaccoType, "
+ " tu.txt_smoking_status as smokingStatus, tu.txt_usage_per_day "
+ "AS usagePerDay, tu.txt_pack_years AS packYears, "
+ "CASE WHEN tu.txt_usage_per_day <> '' THEN 'TobaccoUseStatus' ELSE '' END AS category, tu.seq_no AS id "
+ "FROM patient_encounter pe INNER JOIN tobacco_usage_ tu ON pe.person_id = tu.person_id "
+ "WHERE pe.person_id = '"+parameters.get("personId")+"'";
//

String[] jsonMetaData = {"personId","enterpriseId","practiceId","encounterDate;Date_YYYYMMDD HH:MM:SS","tobaccoType","smokingStatus","usagePerDay",
"packYears","tobaccoCategory","id"
};
try
{
ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
Log.info("JSON LIST is :\n" + jsonList);
status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..personId", jsonList);
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