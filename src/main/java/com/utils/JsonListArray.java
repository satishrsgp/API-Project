package com.utils;

import java.util.ArrayList;
import java.util.Objects;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

public class JsonListArray 
{
	public static ArrayList<Object> computeJsonList(String[] jsonMetaData,String strResponse, String strUniqueJsonToken)
	{

		ArrayList<Object> jsonList = new ArrayList<>();
		//jsonList.clear();
		Object obj = JsonPath.read(strResponse, "$.." + strUniqueJsonToken);
		if (obj instanceof JSONArray)
		{
			for(int j = 0; j< ((JSONArray) obj).size(); j++)
			{
				for(int k=0; k<jsonMetaData.length; k++)
				{
					func(jsonList, strResponse, jsonMetaData[k], j);
				}
			}
		}

		return jsonList;
	}

	public static void func(ArrayList<Object> jsonList, String strResponse, String strJsonToken, int occurance)
	{
		String strJsonPath="";

		if(strJsonToken.contains(";"))
		{
			if(strResponse.contains("\"items\":"))
			{
				strJsonPath="$.items["+occurance+"]."+ strJsonToken.split(";")[0];
			}
			else
			{
				if(strResponse.trim().startsWith("["))
				{
					strJsonPath = "$.["+occurance+"]."+strJsonToken.split(";")[0];
				}
				else
				{
					if(strResponse.trim().startsWith("{"))
						strJsonPath = "$."+strJsonToken.split(";")[0];
				}
			}
			populateArray(jsonList, strResponse, strJsonPath, strJsonToken.split(";")[1]);
		}
		else
		{
			if(strResponse.contains("\"items\":"))
			{
				strJsonPath="$.items["+occurance+"]."+ strJsonToken;
			}
			else
			{
				if(strResponse.trim().startsWith("["))
				{
					strJsonPath = "$.["+occurance+"]."+strJsonToken;
				}
				else
				{
					if(strResponse.trim().startsWith("{"))
						strJsonPath = "$."+strJsonToken;
				}
			}
			//Check if it is a Valid GUID
			if(!Objects.equals(JsonPath.read(strResponse,strJsonPath), null) && (JsonPath.read(strResponse,strJsonPath).toString().length() == 36) && (JsonPath.read(strResponse,strJsonPath).toString().substring(8, 9).equals("-") && JsonPath.read(strResponse,strJsonPath).toString().substring(13, 14).equals("-") && JsonPath.read(strResponse,strJsonPath).toString().substring(18, 19).equals("-") && JsonPath.read(strResponse,strJsonPath).toString().substring(23,24).equals("-")))
			{
				jsonList.add(JsonPath.read(strResponse,strJsonPath).toString().toUpperCase());
			}

			//added  || Objects.equals(strJsonToken, "surescriptsEnrollDate") || Objects.equals(strJsonToken, "surescriptsUnenrollDate") on 23-05-2017
			//special handling for expectedResultDate of Chart_GetOrderedTestSummaries
			//special handling for collectionTime,scheduledTime of Chart_GetOrderedTestDetail
			//special handling for startDate,endDate,nextDueDate tokens of Chart_GetOrderSchedule
			else if(strJsonToken.endsWith("Timestamp") || strJsonToken.endsWith("TimeStamp") || Objects.equals(strJsonToken, "timeEntered") || strJsonToken.endsWith("DateTime") 
					|| Objects.equals(strJsonToken, "dateTimeReported") || Objects.equals(strJsonToken, "observationDateTime")
					|| Objects.equals(strJsonToken, "enrollDate") || Objects.equals(strJsonToken, "unenrollDate") || Objects.equals(strJsonToken, "rxHubEnrollDate") 
					|| Objects.equals(strJsonToken, "surescriptsEnrollDate") || Objects.equals(strJsonToken, "surescriptsUnenrollDate") 
					|| Objects.equals(strJsonToken, "expectedResultDate") || Objects.equals(strJsonToken, "collectionTime") || Objects.equals(strJsonToken, "scheduledTime") 
					|| Objects.equals(strJsonToken, "startDate") || Objects.equals(strJsonToken, "endDate") || Objects.equals(strJsonToken, "nextDueDate") )
			{
				jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath).toString().toUpperCase().replace("T", " ").substring(0,12));
			}
			//Added expirationDate handling for Chart_GetImmunizations route
			else if(strJsonToken.endsWith("Timestamp") || strJsonToken.endsWith("TimeStamp") || Objects.equals(strJsonToken, "timeEntered") || strJsonToken.endsWith("DateTime") 
					|| Objects.equals(strJsonToken, "dateTimeReported") || Objects.equals(strJsonToken, "observationDateTime") || Objects.equals(strJsonToken, "startDate") || Objects.equals(strJsonToken, "endDate")
					|| Objects.equals(strJsonToken, "enrollDate") || Objects.equals(strJsonToken, "unenrollDate") || Objects.equals(strJsonToken, "rxHubEnrollDate")  || Objects.equals(strJsonToken, "surescriptsEnrollDate") || Objects.equals(strJsonToken, "surescriptsUnenrollDate") || Objects.equals(strJsonToken, "expirationDate"))
			{
				jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath).toString().toUpperCase().replace("T", " "));
			}
			else if ((strJsonToken.endsWith("Date") || strJsonToken.startsWith("date")) && !Objects.equals("vitalSignsDate", strJsonToken) && !Objects.equals("appointmentDate", strJsonToken) )
			{
				jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath).toString().substring(0, 10).replace("T", " ").replace("-", ""));
			}
			else if (JsonPath.read(strResponse,strJsonPath) instanceof java.lang.Boolean)
			{
				jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath).toString());
			}
			//Anil has added this to handle a reasons token of Chart_GetExcludedVaccines (eg: "["refused"]" to refused as a string)
			/*else if(!Objects.equals(JsonPath.read(strResponse,strJsonPath), null) && (JsonPath.read(strResponse,strJsonPath).toString().startsWith("[") && JsonPath.read(strResponse,strJsonPath).toString().endsWith("]")))
			{
				int length = JsonPath.read(strResponse,strJsonPath).toString().length();
				jsonList.add(JsonPath.read(strResponse,strJsonPath).toString().substring(2, length-2));

			}*/
			
			else if(!Objects.equals(JsonPath.read(strResponse,strJsonPath), null) && (JsonPath.read(strResponse,strJsonPath).toString().startsWith("[") && JsonPath.read(strResponse,strJsonPath).toString().endsWith("]")))
			{
				int length = JsonPath.read(strResponse,strJsonPath).toString().length();
				if(length > 2)
					jsonList.add(JsonPath.read(strResponse,strJsonPath).toString().substring(2, length-2));
				else
					jsonList.add(null);
			}
			
			//--satish added this for the script Master_GetRelatedMedications_TC1
			else if(Objects.equals(strJsonToken,"hiclSequenceNumber"))
			{
				jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath).toString().split("\\.")[0]);
			}
			//--satish added this for the script Chart_GetImmunizations_TC1  - 9/25/2017
			else if(Objects.equals(strJsonToken,"modifyTimestampTimezone"))
			{
				jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath).toString().split("\\.")[0]);
			}
			//--satish added this for the script Chart_GetVisHistory
			else if(Objects.equals(strJsonToken,"visGiveDateTimeZone"))
			{
				jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath).toString().split("\\.")[0]);
			}
			//--satish added this for the script Chart_GetOrderedVaccine_TC1
			else if(Objects.equals(strJsonToken,"createTimestampTimezone") || Objects.equals(strJsonToken,"encounterTimestampTimezone"))
			{
				jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath).toString().split("\\.")[0]);
			}
			else if(strJsonToken.endsWith("TimestampTimezone"))
			{
				jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath).toString().toUpperCase().replace("T", " "));
			}
			//--satish added this for the script Chart_GetOrderResults_TC1 (11/21/2017)
			else if(strJsonToken.endsWith("resultDateTimezone"))
			{
				jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath).toString().split("\\.")[0]);
			}
			else if(strJsonToken.endsWith("order"))
			{
				jsonList.add(JsonPath.read(strResponse, strJsonPath).toString().split("\\.")[0]);
			}
			else if(Objects.equals(strJsonToken,"orderNumber"))
			{
				jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath).toString().toUpperCase());
			}

			// 07/11/2017 --  added to make CreateTime stamp as a unique for Chart_GetSocialHistory_TC1 - Satish added
			/*
			else if(strJsonToken.endsWith("alcoholUsage"))
			{
				switch (JsonPath.read(strResponse,"$.items["+occurance+"]."+ strJsonToken).toString()) {
				case "NotSet":
					jsonList.add("Not");
					break;
				default:
					jsonList.add(JsonPath.read(strResponse,"$.items["+occurance+"]."+ strJsonToken).toString());
					break;
				}

			}
			*/
			// 07/11/2017 --  added to make CreateTime stamp as a unique for Chart_GetSocialHistory_TC1 - Satish added
			else if(strJsonToken.endsWith("radonInHome"))
			{
				switch (JsonPath.read(strResponse,"$.items["+occurance+"]."+ strJsonToken).toString()) {
				case "NotSet":
					jsonList.add("1");
					break;
				default:
					jsonList.add(JsonPath.read(strResponse,"$.items["+occurance+"]."+ strJsonToken).toString());
					break;
				}

			}
			else
				//special handling for vitalSignsDate in "Chart_Person_Vital_GET_By_Id"
				//special handling for onsetDate, admitDate, dischargeDate in "Person_GET_Encounter_ById"
				//special handling for dateOfBirth in "Persons_Get_personID"
				//special handling for appointmentDate in "Resource_Appointment_GET_By_Id"
				if(Objects.equals(strJsonToken,"vitalSignsDate") || Objects.equals(strJsonToken,"onsetDate") || Objects.equals(strJsonToken,"admitDate") 
						|| Objects.equals(strJsonToken,"dischargeDate") || Objects.equals(strJsonToken,"dateOfBirth") || Objects.equals(strJsonToken,"appointmentDate"))
				{
					jsonList.add(Objects.equals(JsonPath.read(strResponse, strJsonPath), null) ? null : GeneralUtils.formatDateasYYYYMMDDWhenDateDoesNotHaveT(JsonPath.read(strResponse, strJsonPath).toString()));
				}
			//special handling for patientDateOfBirth in "Encounters_ProviderId_GET"
				else if(Objects.equals(strJsonToken,"patientDateOfBirth"))
				{
					jsonList.add(Objects.equals(JsonPath.read(strResponse, strJsonPath), null) ? null : JsonPath.read(strResponse, strJsonPath).toString().substring(0,10).replace("-", ""));
				}
			//special handling for orderingProvider in "Person_GET_All_Orders"
			//special handling for testLocation in "Person_GET_An_Order"
			//special handling for nextgenOrderNumber, uniqueObrNumber in "Person_GET_OrdrDetailedResults"
			//special handling for orderNumber in route Chart_GetOrderedTestSummaries
				else if(Objects.equals(strJsonToken,"orderingProvider") || Objects.equals(strJsonToken,"testLocation") || Objects.equals(strJsonToken,"nextgenOrderNumber") || Objects.equals(strJsonToken,"uniqueObrNumber") || Objects.equals(strJsonToken,"orderNumber"))
				{
					jsonList.add(Objects.equals(JsonPath.read(strResponse, strJsonPath), null) ? null : JsonPath.read(strResponse, strJsonPath).toString().toUpperCase());
				}
				else
				{
					jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath));
				}
		}
	}

	public static void populateArray(ArrayList<Object> jsonList, String strResponse, String strJsonPath, String strJsonTokenSchema)
	{
		try
		{
			switch(strJsonTokenSchema)
			{
			case "GUID" : 
				if(!Objects.equals(JsonPath.read(strResponse,strJsonPath), null) && (JsonPath.read(strResponse,strJsonPath).toString().length() == 36) && (JsonPath.read(strResponse,strJsonPath).toString().substring(8, 9).equals("-") && JsonPath.read(strResponse,strJsonPath).toString().substring(13, 14).equals("-") && JsonPath.read(strResponse,strJsonPath).toString().substring(18, 19).equals("-") && JsonPath.read(strResponse,strJsonPath).toString().substring(23,24).equals("-")))
				{
					jsonList.add(JsonPath.read(strResponse,strJsonPath).toString().toUpperCase());
				}
				// Pavana - 13Feb2018 - Added else part to handle SendAuditId token for endpoint -Chart_GetPatienMedSumCollective 
				else
				{
					jsonList.add(JsonPath.read(strResponse,strJsonPath));
				}
				break;

			case "Boolean" :
				jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath).toString());
				break;

			case "Date_YYYYMMDD HH:MM:SS" :
				jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath).toString().toUpperCase().replace("T", " "));
				break;

			case "Date_YYYYMMDD" :
				jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath).toString().substring(0, 10).replace("T", "").replace("-", ""));
				break;

			case "Normal":
				jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath));
				break;

			default:
				jsonList.add(Objects.equals(JsonPath.read(strResponse,strJsonPath), null) ? null : JsonPath.read(strResponse,strJsonPath));
				break;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.error(e.getMessage());
		}
	}

}