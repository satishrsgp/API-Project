package com.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import com.testscripts.BaseTest;

public class API_Request_RetObj 
{
	public static RequestResponseHandler sendRequest(CloseableHttpClient httpClient, Map<String, Object> mapRequestParameters)
	{
		try
		{
			CloseableHttpResponse httpResponse = null;
			Header[] httpHeaders;
			StringEntity entity;
			String strfinalSignature;
			RequestResponseHandler objDTO = new RequestResponseHandler();
			Boolean flag = false;
			strfinalSignature = FinalSignature.ComputeSignature(
					FinalSignature.GetSigningString(System.getProperty("LoggedInUserName").toString().toLowerCase(), 
							GeneralUtils.getXNGDate() , 
							mapRequestParameters.get("strRequestType").toString(), 
							FinalSignature.CanonicalURI(mapRequestParameters.get("strURI").toString()), 
							FinalSignature.ComputeMD5(FinalSignature.CanonicalQueryString(mapRequestParameters.get("strQueryString").toString()).getBytes()), 
							FinalSignature.ComputeMD5(mapRequestParameters.get("strPayLoad").toString().getBytes())), 
					HashPassword.sha1(BaseTest.strPrePasswordHash));

			//Calculating the Authentication Header to be added to Every Request Header
			String strAuthHeader = "NEXTGEN-AMB-API-V2 Credential=" + System.getProperty("LoggedInUserName") + ", Signature=" + strfinalSignature;
			//added to handle POST Requests
			objDTO.setStrRequestType(mapRequestParameters.get("strRequestType").toString());
			
			switch(mapRequestParameters.get("strRequestType").toString())
			{
			case "GET" : 
			{
				// Creating the GET Request that needs to be executed
				HttpGet httpGet;
				//To Handle Patient Lookup Test Data Sheet
				//System.setProperty("strQueryString", mapRequestParameters.get("strQueryString").toString());
				
				if(mapRequestParameters.get("strQueryString").toString().isEmpty())
				{
					// Creating the GET Request that needs to be executed
					httpGet = new HttpGet(mapRequestParameters.get("strURI").toString());
					//objDTO.setStrRequestURI(httpGet.getURI().toString());
				}
				else
				{
					//To the strURI appending the QueryStringParameters
					String strFinalURI = mapRequestParameters.get("strURI").toString() + mapRequestParameters.get("strQueryString");
					System.out.println("FinalURI after taking into consideration QSP is:"+strFinalURI);
					httpGet = new HttpGet(strFinalURI);
					//objDTO.setStrRequestURI(httpGet.getURI().toString());
				}
				/*System.out.println("strURI in httpget:"+mapRequestParameters.get("strURI").toString());
				System.out.println("HttpGet will not have any PayLoad");*/
				objDTO.setStrRequestURI(httpGet.getURI().toString());


				//Add the Request Headers
				//httpGet.addHeader("Content-Type", "application/json");
				//strAuthHeader = "NEXTGEN-AMB-API Credential=" + System.getProperty("UserName") + ", Signature=" + strfinalSignature;
				httpGet.addHeader("Accept", "*/*");
				httpGet.addHeader("Authorization", strAuthHeader);
				httpGet.addHeader("X-NG-Date", GeneralUtils.getXNGDate());

				//Reading all the Request Headers
				httpHeaders = httpGet.getAllHeaders();

				objDTO.setStrRequestHeaders(loadHeaders(httpHeaders));

				//Execute the Request
				httpResponse = httpClient.execute(httpGet);

				//Read all the Response Headers
				httpHeaders = httpResponse.getAllHeaders();

				objDTO.setStrResponseHeaders(loadHeaders(httpHeaders));

				flag = true;
				break;
			}

			case "POST" :
			{
				// Creating the POST Request that needs to be executed
				HttpPost httpPost = new HttpPost(mapRequestParameters.get("strURI").toString());
				/*	System.out.println("strURI in httppost:"+mapRequestParameters.get("strURI").toString());
				System.out.println("strPayload in httppost:"+mapRequestParameters.get("strPayLoad").toString());*/
				objDTO.setStrRequestURI(httpPost.getURI().toString());

				//Add the Request Headers
				//httpPost.addHeader("Content-Type", "application/json");
				//strAuthHeader = "NEXTGEN-AMB-API Credential=" + System.getProperty("UserName") + ", Signature=" + strfinalSignature;
				httpPost.addHeader("Accept", "*/*");
				httpPost.addHeader("Authorization", strAuthHeader);
				httpPost.addHeader("X-NG-Date", GeneralUtils.getXNGDate());

				entity = entityGeneration(mapRequestParameters.get("strPayLoad").toString());

				//Setting the Request body with the Pay Load
				httpPost.setEntity(entity);
				//System.out.println("Entity is:"+httpPost.getEntity().toString());

				//Reading all the Request Headers
				httpHeaders = httpPost.getAllHeaders();

				objDTO.setStrRequestHeaders(loadHeaders(httpHeaders));

				//Execute the Request
				httpResponse = httpClient.execute(httpPost);

				//Read all the Response Headers
				httpHeaders = httpResponse.getAllHeaders();

				objDTO.setStrResponseHeaders(loadHeaders(httpHeaders));

				flag = true;

				break;
			}


			case "PUT" :
			{
				// Creating the PUT Request that needs to be executed
				HttpPut httpPut = new HttpPut(mapRequestParameters.get("strURI").toString());
				/*System.out.println("strURI in httpput:"+mapRequestParameters.get("strURI").toString());
				System.out.println("strPayload in httpput:"+mapRequestParameters.get("strPayLoad").toString());*/
				objDTO.setStrRequestURI(httpPut.getURI().toString());

				//Add the Request Headers
				//httpPut.addHeader("Content-Type", "application/json");
				//String strAuthHeader = "NEXTGEN-AMB-API Credential=" + System.getProperty("UserName") + ", Signature=" + strfinalSignature;
				httpPut.addHeader("Accept", "*/*");
				httpPut.addHeader("Authorization", strAuthHeader);
				httpPut.addHeader("X-NG-Date", GeneralUtils.getXNGDate());

				entity = entityGeneration(mapRequestParameters.get("strPayLoad").toString());

				//Setting the Request body with the Pay Load
				httpPut.setEntity(entity);
				//System.out.println("Entity is:"+httpPut.getEntity().toString());

				//Reading all the Request Headers
				httpHeaders = httpPut.getAllHeaders();

				objDTO.setStrRequestHeaders(loadHeaders(httpHeaders));

				//Execute the Request
				httpResponse = httpClient.execute(httpPut);

				//Read all the Response Headers
				httpHeaders = httpResponse.getAllHeaders();

				objDTO.setStrResponseHeaders(loadHeaders(httpHeaders));

				flag = true;

				break;
			}



			case "PATCH" :
			{
				// Creating the PATCH Request that needs to be executed
				HttpPatch httpPatch = new HttpPatch(mapRequestParameters.get("strURI").toString());
				/*System.out.println("strURI in httpput:"+mapRequestParameters.get("strURI").toString());
				System.out.println("strPayload in httpput:"+mapRequestParameters.get("strPayLoad").toString());*/
				objDTO.setStrRequestURI(httpPatch.getURI().toString());

				//Add the Request Headers
				//httpPatch.addHeader("Content-Type", "application/json");
				//String strAuthHeader = "NEXTGEN-AMB-API Credential=" + System.getProperty("UserName") + ", Signature=" + strfinalSignature;
				httpPatch.addHeader("Accept", "*/*");
				httpPatch.addHeader("Authorization", strAuthHeader);
				httpPatch.addHeader("X-NG-Date", GeneralUtils.getXNGDate());

				entity = entityGeneration(mapRequestParameters.get("strPayLoad").toString());

				//Setting the Request body with the Pay Load
				httpPatch.setEntity(entity);
				//System.out.println("Entity is:"+httpPatch.getEntity().toString());

				//Reading all the Request Headers
				httpHeaders = httpPatch.getAllHeaders();

				objDTO.setStrRequestHeaders(loadHeaders(httpHeaders));

				//Execute the Request
				httpResponse = httpClient.execute(httpPatch);

				//Read all the Response Headers
				httpHeaders = httpResponse.getAllHeaders();

				objDTO.setStrResponseHeaders(loadHeaders(httpHeaders));

				flag = true;

				break;
			}

			case "DELETE" : 
			{
				// Creating the GET Request that needs to be executed
				HttpDelete httpDelete = new HttpDelete(mapRequestParameters.get("strURI").toString());
				/*System.out.println("strURI in httpget:"+mapRequestParameters.get("strURI").toString());
				System.out.println("HttpGet will not have any PayLoad");*/
				objDTO.setStrRequestURI(httpDelete.getURI().toString());


				//Add the Request Headers
				//httpDelete.addHeader("Content-Type", "application/json");
				//String strAuthHeader = "NEXTGEN-AMB-API Credential=" + System.getProperty("UserName") + ", Signature=" + strfinalSignature;
				httpDelete.addHeader("Accept", "*/*");
				httpDelete.addHeader("Authorization", strAuthHeader);
				httpDelete.addHeader("X-NG-Date", GeneralUtils.getXNGDate());

				//Reading all the Request Headers
				httpHeaders = httpDelete.getAllHeaders();

				objDTO.setStrRequestHeaders(loadHeaders(httpHeaders));

				//Execute the Request
				httpResponse = httpClient.execute(httpDelete);

				//Read all the Response Headers
				httpHeaders = httpResponse.getAllHeaders();

				objDTO.setStrResponseHeaders(loadHeaders(httpHeaders));

				flag = true;

				break;
			}


			default : 
			{
				System.out.println("The Http Request Type passed "+ mapRequestParameters.get("strRequestType").toString() + "is either invalid or empty");
				break;
			}

			}

			if(!flag)
			{
				objDTO = null;
				Log.info("The Http Request Type passed " + mapRequestParameters.get("strRequestType").toString() + "is either invalid or empty, please check it up");
			}
			else
			{
				String line = null;
				StringBuffer strResponseBuffer = new StringBuffer();
				//objDTO.setStrRequestStatus("Pass");
				objDTO.setIntResponseCode(httpResponse.getStatusLine().getStatusCode());
				//@SuppressWarnings("unused")
				String strStatus = mapRequestParameters.get("strRequestType").toString() + " request processed with status code of "+httpResponse.getStatusLine().getStatusCode()+" "+httpResponse.getStatusLine().getReasonPhrase();

				BufferedReader bReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				while((line = bReader.readLine())!= null)
				{
					strResponseBuffer .append(line);
				}
				objDTO.setStrResponse(strResponseBuffer.toString());
				Log.printArtifacts(strStatus, mapRequestParameters.get("strURI").toString(), mapRequestParameters.get("strRequestType").toString(), mapRequestParameters.get("strQueryString").toString(), objDTO.getStrRequestHeaders(), mapRequestParameters.get("strPayLoad").toString(), objDTO.getStrResponseHeaders(), strResponseBuffer.toString());
			}
			return objDTO;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}


	//Entity Generation to be added along with Payload to the Request
	public static StringEntity entityGeneration(String strPayLoad)
	{
		// The Pay load creation part
		StringEntity entity = new StringEntity(strPayLoad, ContentType.APPLICATION_JSON);
		return entity;
	}

	//Processing the Request and Response Headers
	public static String loadHeaders(Header[] httpHeaders )
	{
		String strHeaders = "";
		for (int i = 0; i < httpHeaders.length; i++)
		{
			strHeaders = strHeaders + httpHeaders[i].toString() + "\n";
		}
		return strHeaders;
	}


	public static RequestResponseHandler sendFile(CloseableHttpClient httpClient, Map<String, Object> mapRequestParameters, String strFilePath)
	{
		try
		{
			CloseableHttpResponse httpResponse = null;
			Header[] httpHeaders;
			StringEntity entity;
			String strfinalSignature;
			RequestResponseHandler objDTO = new RequestResponseHandler();
			@SuppressWarnings("unused")
			Boolean flag = false;
			strfinalSignature = FinalSignature.ComputeSignature(
					FinalSignature.GetSigningString(System.getProperty("LoggedInUserName").toString().toLowerCase(), 
							GeneralUtils.getXNGDate() , 
							mapRequestParameters.get("strRequestType").toString(), 
							FinalSignature.CanonicalURI(mapRequestParameters.get("strURI").toString()), 
							FinalSignature.ComputeMD5(FinalSignature.CanonicalQueryString(mapRequestParameters.get("strQueryString").toString()).getBytes()), 
							FinalSignature.ComputeMD5(mapRequestParameters.get("strPayLoad").toString().getBytes())), 
					HashPassword.sha1(BaseTest.strPrePasswordHash));

			//Calculating the Authentication Header to be added to Every Request Header
			String strAuthHeader = "NEXTGEN-AMB-API-V2 Credential=" + System.getProperty("LoggedInUserName") + ", Signature=" + strfinalSignature;


			// Creating the POST Request that needs to be executed
			HttpPost httpPost = new HttpPost(mapRequestParameters.get("strURI").toString());
			/*	System.out.println("strURI in httppost:"+mapRequestParameters.get("strURI").toString());
			System.out.println("strPayload in httppost:"+mapRequestParameters.get("strPayLoad").toString());*/
			objDTO.setStrRequestURI(httpPost.getURI().toString());

			//Add the Request Headers
			//httpPost.addHeader("Content-Type", "application/json");
			//strAuthHeader = "NEXTGEN-AMB-API Credential=" + System.getProperty("UserName") + ", Signature=" + strfinalSignature;
			httpPost.addHeader("Accept", "*/*");
			httpPost.addHeader("Authorization", strAuthHeader);
			httpPost.addHeader("X-NG-Date", GeneralUtils.getXNGDate());

			entity = entityGeneration(mapRequestParameters.get("strPayLoad").toString());

			//Setting the Request body with the Pay Load
			httpPost.setEntity(entity);
			//System.out.println("Entity is:"+httpPost.getEntity().toString());

			//Reading all the Request Headers
			httpHeaders = httpPost.getAllHeaders();

			objDTO.setStrRequestHeaders(loadHeaders(httpHeaders));

			//Execute the Request
			httpResponse = httpClient.execute(httpPost);

			//Read all the Response Headers
			httpHeaders = httpResponse.getAllHeaders();

			objDTO.setStrResponseHeaders(loadHeaders(httpHeaders));


			String line = null;
			StringBuffer strResponseBuffer = new StringBuffer();
			//objDTO.setStrRequestStatus("Pass");
			objDTO.setIntResponseCode(httpResponse.getStatusLine().getStatusCode());
			//@SuppressWarnings("unused")
			String strStatus = mapRequestParameters.get("strRequestType").toString() + " request processed with status code of "+httpResponse.getStatusLine().getStatusCode()+" "+httpResponse.getStatusLine().getReasonPhrase();

			BufferedReader bReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			while((line = bReader.readLine())!= null)
			{
				strResponseBuffer .append(line);
			}
			objDTO.setStrResponse(strResponseBuffer.toString());
			Log.printArtifacts(strStatus, mapRequestParameters.get("strURI").toString(), mapRequestParameters.get("strRequestType").toString(), mapRequestParameters.get("strQueryString").toString(), objDTO.getStrRequestHeaders(), mapRequestParameters.get("strPayLoad").toString(), objDTO.getStrResponseHeaders(), strResponseBuffer.toString());

			return objDTO;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
