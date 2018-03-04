
package com.endpoints;

import java.util.HashMap;

import org.testng.asserts.SoftAssert;

import com.testscripts.BaseTest;
import com.utils.DatabaseConnection;
import com.utils.Log;

public class Provider_AddPaqSignoffComment
{
	public static Boolean provider_AddPaqSignoffComment(HashMap<String, String> parameters,String strResponse, HashMap<String, Object> mapPostDetails, SoftAssert softAssert, String strQuery)
	{
		Boolean status=false;
		try
		{
			Object itemNotesValue = DatabaseConnection.fetchColumnAsObject(BaseTest.connNGA, strQuery,"item_notes");
			
		
			if(itemNotesValue.toString().contains(parameters.get("Comment")))
			{
               
                status =true;
                Log.info("Comment is updated and is present in DataBase");
			}
			else
			{
				Log.info("The Difference between records count before and after post is more than one, please reexcute once more");
				softAssert.fail("The Difference between records count before and after post is more than one. Please reexecute once more");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}
}