package com.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

public class API_POST_RetObj
{	
	public static RequestResponseHandler sendPostRequest(CloseableHttpClient httpClient, String strURI, String strPayLoad)
	{
		HttpPost httpPost;
		CloseableHttpResponse httpResponse;
		Header[] httpHeaders;
		StringEntity entity;
		String strfinalSignature;
		RequestResponseHandler objDTO = new RequestResponseHandler();
		try
		{
			// Creating the POST Request that needs to be executed
			httpPost = new HttpPost(strURI);
			objDTO.setStrRequestURI(httpPost.getURI().toString());

			//Add the Request Headers
			httpPost.addHeader("Content-Type", "application/json");
			
			// The Pay load creation part
			entity = new StringEntity(strPayLoad, ContentType.APPLICATION_JSON);

			//Setting the Request body with the Pay Load
			httpPost.setEntity(entity);

			//Computing the final signature
			strfinalSignature = FinalSignature.ComputeSignature(FinalSignature.GetSigningString(System.getProperty("UserName"), GeneralUtils.getXNGDate() , "POST", FinalSignature.CanonicalURI(strURI), FinalSignature.CanonicalQueryString(""), FinalSignature.ComputeMD5(strPayLoad.getBytes())), System.getProperty("SessionToken"));
			httpPost.addHeader("Content-Type", "application/json");
			String strAuthHeader = "NEXTGEN-AMB-API Credential=" + System.getProperty("UserName") + ", Signature=" + strfinalSignature;
			httpPost.addHeader("Authorization", strAuthHeader);
			httpPost.addHeader("X-NG-Date", GeneralUtils.getXNGDate());

			//Reading all the Request Headers
			httpHeaders = httpPost.getAllHeaders();
			String requestHeaders = "";
			for (int i = 0; i < httpHeaders.length; i++)
			{
				requestHeaders = requestHeaders + httpHeaders[i].toString() + "\n";
			}
			objDTO.setStrRequestHeaders(requestHeaders);

			//Execute the Request
			httpResponse = httpClient.execute(httpPost);

			//Read all the Response Headers
			httpHeaders = httpResponse.getAllHeaders();
			String responseHeaders = "";
			for (int i = 0; i < httpHeaders.length; i++)
			{
				responseHeaders = responseHeaders + httpHeaders[i].toString() + "\n";
			}
			objDTO.setStrResponseHeaders(responseHeaders);

			String line = null;
			StringBuffer strResponseBuffer = new StringBuffer();
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK || httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT)
			{
				objDTO.setStrRequestStatus("Pass");
				objDTO.setIntResponseCode(httpResponse.getStatusLine().getStatusCode());
				String strStatus = "POST request processed successfully with status code of "+httpResponse.getStatusLine().getStatusCode()+" "+httpResponse.getStatusLine().getReasonPhrase();

				BufferedReader bReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				while((line = bReader.readLine())!= null)
				{
					strResponseBuffer .append(line);
				}
				objDTO.setStrResponse(strResponseBuffer.toString());
				Log.printArtifacts(strStatus, strURI, "POST", "", httpPost.getAllHeaders(), strPayLoad, httpResponse.getAllHeaders(), strResponseBuffer.toString());
			}
			else
			{
				objDTO.setStrRequestStatus("Fail");
				objDTO.setIntResponseCode(httpResponse.getStatusLine().getStatusCode());
				String strStatus = "POST request failed with a response code of: "+httpResponse.getStatusLine().getStatusCode()+" "+httpResponse.getStatusLine().getReasonPhrase();
				BufferedReader bReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				while((line = bReader.readLine())!= null)
				{
					strResponseBuffer.append(line);
				}
				objDTO.setStrResponse(strResponseBuffer.toString());
				Log.printArtifacts(strStatus, strURI, "POST", "", httpPost.getAllHeaders(), strPayLoad, httpResponse.getAllHeaders(), strResponseBuffer.toString());
			}
			return objDTO;
		}catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
