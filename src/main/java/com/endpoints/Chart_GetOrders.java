package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetOrders
{


	public static Boolean Chart_GetOrders_Test(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;
		
		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				String strQuery = "select top 25"
									+" LN.order_num as id,"
									+ "LN.test_desc as testDescription,"
									+ "LN.test_status as testStatus,"
									+ "LN.ngn_status as nextgenStatus,"
									+ "LN.lab_id as labId,"
									+ "LN.enc_id as encounterId,"
									+ "LN.enc_timestamp as encounterTimestamp,"
									+ "LN.person_id as personId,"
									+ "LN.ufo_num as ufoNumber,"
									+ "LN.generated_by as generatedBy,"
									+ "LN.order_type as orderType,"
									+ "LN.test_location as testLocation,"
									+ "case LN.order_control "
									+ "when 'NW' then 'New' "
									+ "when 'CA' then 'Cancel' "
									+ "when 'RP' then 'Replace' "
									+ "when  'XO' then 'Change'"
									+ "when 'HD' then 'Hold' end as orderControl,"
									+ "case LN.order_priority "
									+ "when 'R' then 'Routine' "
									+ "when 'A' then 'ASAP' "
									+ "when 'S' then 'STAT' "
									+ "when 'C' then 'Callback' "
									+ "when 'T' then 'TimingCritial' "
									+ "end as orderPriority,"
									+ "LN.ordering_provider as orderingProvider,"
									+ "LN.create_timestamp as orderDate,"
									+ "LN.create_timestamp_tz as orderDateTimezone,"
									+ "LN.sign_off_date as signOffDate,"
									+ "LN.intrf_msg as intrfMessage,"
									+ "LN.enterprise_id as enterpriseId,"
									+ "LN.practice_id as practiceId,"
									+ "case when LN.delete_ind = 'N' then 'false' else 'true' end as isDeleted,"
									+ "LN.sign_off_person as signOffPerson,"
									+ "case OS.future_ind "
									+ "when 'R' then 'false' "
									+ "when 'F' then 'true' "
									+ "else null "
									+ "end as isFutureOrder,"
									+ "OS.start_date as startDate,"
									+ "OS.nextdue_date as nextDueDate "									
									+ "From lab_nor LN  "
									+ "Left outer join order_schedule OS on LN.order_num  = OS.order_id "
									+ "Where LN.person_id = '" + parameters.get("personId") + "' "
									+ "AND LN.enterprise_id = '" + System.getProperty("LoggedInEnterpriseId") + "' "
									+ "AND LN.practice_id = '" + System.getProperty("LoggedInPracticeId") + "' "
									+ "and delete_ind='N'  "
									+ "order by LN.order_num";
									
														
				
				String[] jsonMetaData = {"id","testDescription","testStatus","nextgenStatus","labId","encounterId","encounterTimestamp;Date_YYYYMMDD HH:MM:SS","personId","ufoNumber","generatedBy",
						"orderType","testLocation","orderControl","orderPriority","orderingProvider","orderDate;Date_YYYYMMDD HH:MM:SS","orderDateTimezone","signOffDate;Date_YYYYMMDD HH:MM:SS","intrfMessage",
						"enterpriseId","practiceId","isDeleted","signOffPerson","isFutureOrder","startDate;Date_YYYYMMDD HH:MM:SS","nextDueDate;Date_YYYYMMDD HH:MM:SS","expectedResultDate';Date_YYYYMMDD HH:MM:SS"};
						
				
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
