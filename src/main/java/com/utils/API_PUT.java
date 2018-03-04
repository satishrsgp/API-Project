package com.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import com.jayway.jsonpath.JsonPath;

public class API_PUT
{

	public static HttpPut httpPut;
	public static CloseableHttpResponse httpResponse;
	public static Header[] httpHeaders;
	public static StringEntity entity;
	public static String strfinalSignature;

	public static String sendPutRequest(CloseableHttpClient httpClient, String strURI, String strPayLoad, String strRequestType)
	{
		try
		{
			// Creating the PUT Request that needs to be executed
			httpPut = new HttpPut(strURI);

			//Add the Request Headers
			httpPut.addHeader("Content-Type", "application/json");
			// The Pay load creation part

			//Print the Request URI on the Console
			System.setProperty("RequestURI", httpPut.getURI().toString());

			entity = new StringEntity(strPayLoad, ContentType.APPLICATION_JSON);

			//Setting the Request body with the Pay Load
			httpPut.setEntity(entity);

			//Verify whether it's a Login Put Request or others
			if(!strRequestType.equalsIgnoreCase("Login"))
			{
				strfinalSignature = FinalSignature.ComputeSignature(FinalSignature.GetSigningString(System.getProperty("UserName"), GeneralUtils.getXNGDate() , "POST", FinalSignature.CanonicalURI(strURI), FinalSignature.CanonicalQueryString(""), FinalSignature.ComputeMD5(strPayLoad.getBytes())), System.getProperty("SessionToken"));
				httpPut.addHeader("Content-Type", "application/json");
				String strAuthHeader = "NEXTGEN-AMB-API Credential=" + System.getProperty("UserName") + ", Signature=" + strfinalSignature;
				httpPut.addHeader("Authorization", strAuthHeader);
				httpPut.addHeader("X-NG-Date", GeneralUtils.getXNGDate());
			}

			//Reading all the Request Headers
			httpHeaders = httpPut.getAllHeaders();
			String requestHeaders = "";

			//Print all the Request Headers on the Console
			for (int i = 0; i < httpHeaders.length; i++)
			{
				requestHeaders = requestHeaders + httpHeaders[i].toString() + "\n";
			}
			System.setProperty("RequestHeaders", requestHeaders);
			
			//Execute the Request
			httpResponse = httpClient.execute(httpPut);

			//Read all the Response Headers
			httpHeaders = httpResponse.getAllHeaders();
			
			String line = null;
			StringBuffer strResponseBuffer = new StringBuffer();
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				System.setProperty("Result", "Pass");
				System.setProperty("ResponseCode", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
				String strStatus = "Perform Authentication is fine with status code of "+httpResponse.getStatusLine().getStatusCode()+" "+httpResponse.getStatusLine().getReasonPhrase();
				
				BufferedReader bReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				while((line = bReader.readLine())!= null)
				{
					strResponseBuffer.append(line);
				}
				Log.printArtifacts(strStatus, strURI, "PUT", "", httpPut.getAllHeaders(), strPayLoad, httpResponse.getAllHeaders(), strResponseBuffer.toString());
				
				List<String> strSessionToken = JsonPath.read(strResponseBuffer.toString(), "$..SessionToken");
				System.setProperty("SessionToken", strSessionToken.get(0).toString());
				
				List<String> strUserName = JsonPath.read(strResponseBuffer.toString(), "$..NextGenUserName");
				System.setProperty("LoggedInUserName", strUserName.get(0).toString());
			}
			else
			{
				System.setProperty("Result", "Fail with status code of "+httpResponse.getStatusLine().getStatusCode());
				System.setProperty("ResponseCode", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
				String strStatus = "PUT request failed with a response code of: "+httpResponse.getStatusLine().getStatusCode()+" "+httpResponse.getStatusLine().getReasonPhrase();
				BufferedReader bReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				while((line = bReader.readLine())!= null)
				{
					strResponseBuffer.append(line);
				}
				Log.printArtifacts(strStatus, strURI, "PUT", "", httpPut.getAllHeaders(), strPayLoad, httpResponse.getAllHeaders(), strResponseBuffer.toString());
			}
			return GeneralUtils.prettyprintJsonResponse(strResponseBuffer.toString());
		}catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
