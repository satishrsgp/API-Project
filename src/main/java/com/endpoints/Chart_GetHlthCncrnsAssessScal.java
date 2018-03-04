package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Chart_GetHlthCncrnsAssessScal {

	public static Boolean chart_GetHlthCncrnsAssessScal(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				
				
				String strQuery="SELECT ex.seq_no,ex.enterprise_id,ex.practice_id,ex.person_id,ex.completeddate,ex.acttextdisplay,ex.obsvalue,ex.obsinterpretation,ex.encounterdate,ex.create_timestamp "
						+ "	FROM order_ ex,practice p "
						+ "WHERE ex.person_id = '"+parameters.get("personId")+"' AND ex.practice_id = '0001' AND ex.enterprise_id = '00001'  "
						+ "AND p.practice_id = ex.practice_id AND (ex.actsubclass in ('depression','bipolar','anxiety','Alcohol','Drugs','ascvd','ukpds')  "
						+ "OR ex.acttext in ('Veterans Rand 12 Item Health Survey (VR-12)','Framingham CHD 10 year risk' ,'Hwalek-Sengstock Elder Abuse Screening Test (H-S/EAST)' ))  "
						+ "AND ex.actstatus <> 'cancelled' AND Isnull(actmood, '''') <> 'RMD' AND ex.actstatus <> 'deleted' ";


				String[] jsonMetaData = {"id","enterpriseId","practiceId","personId","completedDate","instrument","score","severity","encounterDate","createTimestamp"};

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
