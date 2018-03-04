package com.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

public class API_PUT_RetObj
{

	public static RequestResponseHandler sendPutRequest(CloseableHttpClient httpClient, String strURI, String strPayLoad)
	{
		HttpPut httpPut;
		CloseableHttpResponse httpResponse;
		Header[] httpHeaders;
		StringEntity entity;
		String strfinalSignature;
		RequestResponseHandler objDTO = new RequestResponseHandler();
		try
		{
			// Creating the PUT Request that needs to be executed
			httpPut = new HttpPut(strURI);
			objDTO.setStrRequestURI(httpPut.getURI().toString());

			//Add the Request Headers
			httpPut.addHeader("Content-Type", "application/json");
			
			// The Pay load creation part
			entity = new StringEntity(strPayLoad, ContentType.APPLICATION_JSON);

			//Setting the Request body with the Pay Load
			httpPut.setEntity(entity);

			//Computing the final signature
			strfinalSignature = FinalSignature.ComputeSignature(FinalSignature.GetSigningString(System.getProperty("UserName"), GeneralUtils.getXNGDate() , "PUT", FinalSignature.CanonicalURI(strURI), FinalSignature.CanonicalQueryString(""), FinalSignature.ComputeMD5(strPayLoad.getBytes())), System.getProperty("SessionToken"));
			httpPut.addHeader("Content-Type", "application/json");
			String strAuthHeader = "NEXTGEN-AMB-API Credential=" + System.getProperty("UserName") + ", Signature=" + strfinalSignature;
			httpPut.addHeader("Authorization", strAuthHeader);
			httpPut.addHeader("X-NG-Date", GeneralUtils.getXNGDate());

			//Reading all the Request Headers
			httpHeaders = httpPut.getAllHeaders();
			String requestHeaders = "";
			for (int i = 0; i < httpHeaders.length; i++)
			{
				requestHeaders = requestHeaders + httpHeaders[i].toString() + "\n";
			}
			objDTO.setStrRequestHeaders(requestHeaders);

			//Execute the Request
			httpResponse = httpClient.execute(httpPut);

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
				String strStatus = "PUT request processed successfully with status code of "+httpResponse.getStatusLine().getStatusCode()+" "+httpResponse.getStatusLine().getReasonPhrase();
				
				if (!(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_NO_CONTENT))
				{
					BufferedReader bReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
					while((line = bReader.readLine())!= null)
					{
						strResponseBuffer .append(line);
					}
					objDTO.setStrResponse(strResponseBuffer.toString());
				}
				else
					objDTO.setStrResponse("For PUT there is no JSON Response");
				
				Log.printArtifacts(strStatus, strURI, "PUT", "", httpPut.getAllHeaders(), strPayLoad, httpResponse.getAllHeaders(), strResponseBuffer.toString());
			}
			else
			{
				objDTO.setStrRequestStatus("Fail");
				objDTO.setIntResponseCode(httpResponse.getStatusLine().getStatusCode());
				String strStatus = "PUT request failed with a response code of: "+httpResponse.getStatusLine().getStatusCode()+" "+httpResponse.getStatusLine().getReasonPhrase();
				BufferedReader bReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				while((line = bReader.readLine())!= null)
				{
					strResponseBuffer.append(line);
				}
				objDTO.setStrResponse(strResponseBuffer.toString());
				Log.printArtifacts(strStatus, strURI, "PUT", "", httpPut.getAllHeaders(), strPayLoad, httpResponse.getAllHeaders(), strResponseBuffer.toString());
			}
			return objDTO;
		}catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
