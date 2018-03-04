package com.endpoints;

import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;

public class Chart_DeleteObservationPanel 
{


	public static Boolean chart_DeleteObservationPanel(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "Select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
					+ "FROM lab_results_obr_p "
					+ "WHERE person_id = '"+parameters.get("personId")+"'  "
					+ "AND unique_obr_num = '"+parameters.get("panelId")+"'";
					
					
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "Select delete_ind "
							+ "FROM lab_results_obr_p "
							+ "WHERE person_id = '"+parameters.get("personId")+"'  "
							+ "AND unique_obr_num = '"+parameters.get("panelId")+"'  ";
							
					String DeleteInd = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strQuery1, "delete_ind").toString();
					if (DeleteInd.equals("Y"))
					{
						status = true;
						Log.info("Lab Panel deleted for patient with personId : " +parameters.get("personId") + "PanelId: "+ parameters.get("panelId"));
					}
					else
					{
						status = false;
						Log.error("Lab Panel not deleted for patient with personId : " +parameters.get("personId") + "PanelId: "+ parameters.get("panelId"));
						softAssert.fail("Lab Panel not deleted for patient with personId : " +parameters.get("personId") + "PanelId: "+ parameters.get("panelId"));
					}
					
				}
				else
				{
					Log.error("Lab Panel not deleted for patient with personId :="+parameters.get("personId")+", was not Updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Lab Panel not deleted for patient with personId :="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("Lab Panel not deleted for patient with personId :="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("Lab Panel not deleted for patient with personId :="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}






}
