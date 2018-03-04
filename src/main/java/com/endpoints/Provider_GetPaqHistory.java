package com.endpoints;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.FinalAssertions;
import com.utils.JsonListArray;
import com.utils.Log;

public class Provider_GetPaqHistory {

	public static Boolean provider_GetPaqHistory(HashMap<String, String> parameters, String strResponse, String strUniqueJsonToken, SoftAssert softAssert)
	{
		Boolean status=false;

		if(!strResponse.isEmpty() && !strResponse.contains("is not valid for Guid"))
		{
			try
			{
				String strQuery="SELECT [Project1].[item_id] AS [item_id], case when [Project1].[item_type]='L' then 'Order' when "
						+ "[Project1].[item_type]='I' then 'Image' when [Project1].[item_type]='D' then 'Document' "
						+ "when [Project1].[item_type]='S' then 'IcsImage' end as [item_type] , [Project1].[item_name] AS [description], "
						+ " [Project1].[C2] AS [fileName], [Project1].[item_format] AS [item_format], case when [Project1].[signoff_action]='R' "
						+ " then 'Rejected' when [Project1].[signoff_action]='A' then 'SignedOff' when [Project1].[signoff_action]='E'  "
						+ "then 'Reassigned' end as [signoff_action] , [Project1].[signoff_desc] AS [signoff_desc], [Project1].[practice_id]  "
						+ "AS [practice_id], [Project1].[practice_name] AS [practice_name], [Project1].[provider_id] AS [provider_id], [Project1].[enc_id]   "
						+ "AS [enc_id], [Project1].[C1] AS [enc_timestamp], [Project1].[enc_timestamp_tz] AS [enc_timestamp_tz], [Project1].[person_id]  "
						+ "AS [person_id], [Project1].[first_name] AS [first_name], [Project1].[last_name] AS [last_name], [Project1].[middle_name] AS [middle_name], "
						+ " [Project1].[app_created_by] AS [app_created_by], [Project1].[signoff_user_id] AS [createdBy], [Project1].[create_timestamp]  "
						+ "AS [create_timestamp], [Project1].[create_timestamp_tz] AS [create_timestamp_tz], [Project1].[signoff_user_id] AS [modifiedBy],  "
						+ "[Project1].[modify_timestamp] AS [modify_timestamp], [Project1].[modify_timestamp_tz] AS [modify_timestamp_tz], [Project1].[C3] AS [labPrecedence],  "
						+ "case when [Project1].[excl_ind]='Y' then 'true' else 'false' end as [isRestricted], [Project1].[C4] AS [C4], [Project1].[privacy_level] AS [privacy_level]  "
						+ "FROM ( SELECT [Extent1].[item_id] AS [item_id], [Extent1].[enterprise_id] AS [enterprise_id], [Extent1].[practice_id] AS [practice_id],  "
						+ "[Extent1].[provider_id] AS [provider_id], [Extent1].[person_id] AS [person_id], [Extent1].[item_type] AS [item_type], [Extent1].[item_name] AS [item_name],  "
						+ "[Extent1].[item_format] AS [item_format], [Extent1].[signoff_user_id] AS [signoff_user_id], [Extent1].[signoff_desc] AS [signoff_desc],  "
						+ "[Extent1].[signoff_action] AS [signoff_action], [Extent1].[app_created_by] AS [app_created_by], [Extent1].[create_timestamp] AS [create_timestamp],  "
						+ "[Extent1].[create_timestamp_tz] AS [create_timestamp_tz], [Extent1].[modify_timestamp] AS [modify_timestamp], [Extent1].[modify_timestamp_tz]  "
						+ "AS [modify_timestamp_tz], [Extent2].[first_name] AS [first_name], [Extent2].[middle_name] AS [middle_name], [Extent2].[last_name] AS [last_name],  "
						+ "[Extent3].[practice_name] AS [practice_name], CAST( [Extent5].[enc_timestamp] AS datetime2) AS [C1], CASE WHEN ( NOT ((N'L' = [Extent1].[item_type])  "
						+ "AND ([Extent1].[item_type] IS NOT NULL))) THEN [Extent1].[item_file] END AS [C2], CASE WHEN (N'L' = [Extent1].[item_type]) THEN [Extent1].[item_file] END AS [C3],  "
						+ "CASE WHEN (N'D' = [Extent1].[item_type]) THEN [Extent7].[perm_pos] WHEN (N'N' = [Extent1].[item_type]) THEN [Extent8].[perm_pos] ELSE 0 END AS [C4], [Extent4].[privacy_level]  "
						+ "AS [privacy_level], [Extent5].[enc_id] AS [enc_id], [Extent5].[enc_timestamp_tz] AS [enc_timestamp_tz], [Extent6].[incl_ind] AS [incl_ind], [Extent6].[excl_ind] AS [excl_ind] "
						+ " FROM [dbo].[paq_signoff_history] AS [Extent1] INNER JOIN [dbo].[person] AS [Extent2] ON [Extent1].[person_id] = [Extent2].[person_id] INNER JOIN [dbo].[practice] AS [Extent3]  "
						+ "ON [Extent1].[practice_id] = [Extent3].[practice_id] LEFT OUTER JOIN [dbo].[patient] AS [Extent4] ON ([Extent1].[person_id] = [Extent4].[person_id]) AND ('"+System.getProperty("LoggedInEnterpriseId")+"' = [Extent4].[enterprise_id])  "
						+ "AND ('"+System.getProperty("LoggedInPracticeId")+"' = [Extent4].[practice_id]) LEFT OUTER JOIN [dbo].[patient_encounter] AS [Extent5] ON ([Extent1].[enc_id] = [Extent5].[enc_id]) AND ([Extent1].[practice_id] = [Extent5].[practice_id])  "
						+ "LEFT OUTER JOIN [dbo].[user_person_filter] AS [Extent6] ON ([Extent1].[person_id] = [Extent6].[person_id]) AND ('"+System.getProperty("LoggedInUserId")+"' = [Extent6].[user_id]) AND ('"+System.getProperty("LoggedInEnterpriseId")+"' = [Extent6].[enterprise_id]) AND ('"+System.getProperty("LoggedInPracticeId")+"' = [Extent6].[practice_id])  "
						+ "LEFT OUTER JOIN [dbo].[security_items] AS [Extent7] ON ([Extent1].[item_name] = [Extent7].[description]) AND (N'D' = [Extent7].[item_type]) AND (N'N' = [Extent7].[delete_ind]) LEFT OUTER JOIN [dbo].[templates] AS [Extent8]  "
						+ "ON ((SUBSTRING([Extent1].[item_name], 0 + 1, CASE WHEN ((( CAST(CHARINDEX(N'_.', [Extent1].[item_name]) AS int)) - 1) < 0) THEN 0 ELSE ( CAST(CHARINDEX(N'_.', [Extent1].[item_name]) AS int)) - 1 END)) = [Extent8].[template_name])  "
						+ "OR ((SUBSTRING([Extent1].[item_name], 0 + 1, CASE WHEN ((( CAST(CHARINDEX(N'_.', [Extent1].[item_name]) AS int)) - 1) < 0) THEN 0 ELSE ( CAST(CHARINDEX(N'_.', [Extent1].[item_name]) AS int)) - 1 END) IS NULL)  "
						+ "AND ([Extent8].[template_name] IS NULL)) WHERE ([Extent1].[signoff_action] IN (N'A', N'R', N'E')) AND ([Extent1].[item_type] IN (N'D', N'N', N'I', N'S', N'L')) AND ([Extent1].[item_type] IS NOT NULL)  "
						+ "AND ([Extent1].[enterprise_id] ='"+System.getProperty("LoggedInEnterpriseId")+"') AND ([Extent1].[practice_id] = '"+System.getProperty("LoggedInPracticeId")+"') AND ([Extent1].[provider_id] ='"+parameters.get("providerId")+"') ) AS [Project1]  "
						+ "ORDER BY [Project1].[modify_timestamp] DESC, [Project1].[item_id] ASC";

				String[] jsonMetaData = {"itemId","secondaryId","type","description","fileName","format","signoffStatus","signoffDescription","practiceId","practiceName","providerId","encounterId","encounterTimestamp","encounterTimestampTimezone","personId","firstName","lastName","middleName","appCreatedBy","createdBy","createTimestamp","createTimestampTimezone","modifiedBy","modifyTimestamp","modifyTimestampTimezone","isCompleted","labPrecedence","isRestricted"};

				try
				{	
					ArrayList<Object> jsonList = JsonListArray.computeJsonList(jsonMetaData, strResponse, strUniqueJsonToken);
					Log.info("JSON LIST is :\n" + jsonList);
					status = FinalAssertions.assertResponseCount(DatabaseConnection.statementExecution(BaseTest.connNGA, strQuery), strResponse, "$..itemId", jsonList);
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
