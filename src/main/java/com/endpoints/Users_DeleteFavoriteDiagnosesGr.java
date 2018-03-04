package com.endpoints;

import java.util.HashMap;
import java.util.Objects;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;

public class Users_DeleteFavoriteDiagnosesGr 
{

	public static Boolean users_DeleteFavoriteDiagnosesGr_Test(HashMap<String, String> parameters, SoftAssert softAssert)
	{
		Boolean status=false;
		try
		{
			String strSQLQuery = "select datediff(second,create_timestamp,modify_timestamp) as diff,modified_by as mb "
					+ "FROM diagnosis_favorite_xref "
					+ "where created_by='"+System.getProperty("LoggedInUserId")+"' and diag_favorite_id='"+parameters.get("groupId")+"' ";
				
					
					
			Object objIntDateDiff = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "diff");
			Object objIntModifiedBy = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strSQLQuery, "mb");
			if(Integer.valueOf(objIntDateDiff.toString()) > 0)
			{
				if(Objects.equals(objIntModifiedBy.toString(), System.getProperty("LoggedInUserId")))
				{
					String strQuery1 = "select delete_ind from diagnosis_favorite_xref  where created_by = '"+System.getProperty("LoggedInUserId")+"' and diag_favorite_id= '"+parameters.get("groupId")+"' "; 
							
					String DeleteInd = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strQuery1, "delete_ind").toString();
					if (DeleteInd.equals("Y"))
					{
						status = true;
						Log.info("care plan Outcome deleted for patient with personId : " +parameters.get("personId") + "EncounterID: "+ parameters.get("encounterId"));
					}
					else
					{
						status = false;
						Log.error("care plan Outcome not deleted for patient with personId : " +parameters.get("personId") + "EncounterID: "+ parameters.get("encounterId"));
						softAssert.fail("care plan goal not deleted for patient with personId : " +parameters.get("personId") + "EncounterID: "+ parameters.get("encounterId"));
					}
					
				}
				else
				{
					Log.error("procedure Outcome not deleted for patient with personId :="+parameters.get("personId")+", was not Updated correctly-modified_by is not updated with logged in user's credentials.");
					Log.error("Expected:"+System.getProperty("LoggedInUserId")+", Actual:"+objIntModifiedBy.toString());
					softAssert.fail("Favorite group not deleted for patient with personId :="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				}
			}
			else
			{
				Log.error("procedure goal not deleted for patient with personId :="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
				softAssert.fail("procedure goal not deleted for patient with personId :="+parameters.get("personId")+", was not updated correctly-modify_timestamp is not updated with latest timesatmp");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}
}
