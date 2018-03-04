package com.utils;

import java.util.Objects;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Log
{
	//Initializing a log object which is static of the type log4j
		private static Logger log = LoggerFactory.getLogger(Log.class);
	
	public static synchronized void beginTestCase(String strTestCaseName)
	{
		log.info("***************************************************************************************************************************");
		log.info("---------------------------------------Beginning of "+strTestCaseName+"----------------------------------------------------");
		log.info("***************************************************************************************************************************");
	}
	
	public static synchronized void endTestCase(String strTestCaseName)
	{
		log.info("***************************************************************************************************************************");
		log.info("---------------------------------------Ending of "+strTestCaseName+"----------------------------------------------------");
		log.info("***************************************************************************************************************************"); 
	}

	public static synchronized void info(String message)
	{
		log.info(message);
	}
	
	public static synchronized void warn(String message)
	{
		log.warn(message);
	}
	
	public static synchronized void error(String message)
	{
		log.error(message);
	}

	public static synchronized  void trace(String message)
	{
		log.trace(message);
	}

	public static synchronized void debug(String message)
	{
		log.debug(message);
	}
	
	public static synchronized void printArtifacts(String strStatus, String strURI, String strMethod, String strParams, Header[] requestHttpHeaders,String strRequestBody ,Header[] responseHttpHeaders, String strResponseBody)
	{
		try
		{
			if(strMethod.equalsIgnoreCase("GET") || strMethod.equalsIgnoreCase("DELETE"))
			{
				//System.out.println("***************************************************************************************************************************");
				log.info("***************************************************************************************************************************");
				//System.out.println("The request URI is: "+strURI);
				log.info("The request URI is: "+strURI);
				//System.out.println("The request method is: "+strMethod);
				log.info("The request method is: "+strMethod);
				//System.out.println("The request parameters are: "+strParams);
				log.info("The request parameters are: "+strParams);
				//System.out.println("The header length is "+requestHttpHeaders.length+"\nThe header content of the request is: \n");
				for (int i = 0; i < requestHttpHeaders.length; i++)
				{
					//System.out.println(requestHttpHeaders[i].toString());
				}
				log.info("The header length is "+requestHttpHeaders.length+"\nThe header content of the request is: \n");
				for (int i = 0; i < requestHttpHeaders.length; i++)
				{
					log.info(requestHttpHeaders[i].toString());
				}
				//System.out.println("The status of the request is :\n"+strStatus);
				log.info("The status of the request is :\n"+strStatus);
				//System.out.println("The header length is "+responseHttpHeaders.length+"\nThe header content of the response is: \n");
				for (int i = 0; i < responseHttpHeaders.length; i++)
				{
					//System.out.println(responseHttpHeaders[i].toString());
				}
				log.info("The header length is "+responseHttpHeaders.length+"\nThe header content of the response is: \n");
				for (int i = 0; i < responseHttpHeaders.length; i++)
				{
					log.info(responseHttpHeaders[i].toString());
				}
				//System.out.println("The response body is :\n"+GeneralUtils.prettyprintJsonResponse(strResponseBody));
				if(! Objects.equals(strResponseBody, null))
				{
					log.info("The response body is :\n"+GeneralUtils.prettyprintJsonResponse(strResponseBody));
				}
				//System.out.println("***************************************************************************************************************************");
				log.info("***************************************************************************************************************************");
			}
			else
			{
				//System.out.println("***************************************************************************************************************************");
				log.info("***************************************************************************************************************************");
				//System.out.println("The request URI is: "+strURI);
				log.info("The request URI is: "+strURI);
				//System.out.println("The request method is: "+strMethod);
				log.info("The request method is: "+strMethod);
				//System.out.println("The request parameters are: "+strParams);
				log.info("The request parameters are: "+strParams);
				//System.out.println("The header length is "+requestHttpHeaders.length+"\nThe header content of the request is: \n");
				for (int i = 0; i < requestHttpHeaders.length; i++)
				{
					//System.out.println(requestHttpHeaders[i].toString());
				}
				log.info("The header length is "+requestHttpHeaders.length+"\nThe header content of the request is: \n");
				for (int i = 0; i < requestHttpHeaders.length; i++)
				{
					log.info(requestHttpHeaders[i].toString());
				}
				//System.out.println("The request body is :\n"+GeneralUtils.prettyprintJsonResponse(strRequestBody));
				log.info("The request body is :\n"+ (strRequestBody.isEmpty() || strRequestBody == null ? "No Response": GeneralUtils.prettyprintJsonResponse(strRequestBody) ));
				//System.out.println("The status of the request is :\n"+strStatus);
				log.info("The status of the request is :\n"+strStatus);
				//System.out.println("The header length is "+responseHttpHeaders.length+"\nThe header content of the response is: \n");
				for (int i = 0; i < responseHttpHeaders.length; i++)
				{
					//System.out.println(responseHttpHeaders[i].toString());
				}
				log.info("The header length is "+responseHttpHeaders.length+"\nThe header content of the response is: \n");
				for (int i = 0; i < responseHttpHeaders.length; i++)
				{
					log.info(responseHttpHeaders[i].toString());
				}
				//System.out.println("The response body is :\n"+GeneralUtils.prettyprintJsonResponse(strResponseBody));
				log.info("The response body is :\n"+ (strResponseBody.isEmpty() || strResponseBody == null ? "No Response": GeneralUtils.prettyprintJsonResponse(strResponseBody) ));
				//System.out.println("***************************************************************************************************************************");
				log.info("***************************************************************************************************************************");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static synchronized void printArtifactsForBadRequest(String strStatus, String strURI, String strMethod, String strParams, Header[] requestHttpHeaders,String strRequestBody)
	{
		try
		{
			if(strMethod.equalsIgnoreCase("GET") || strMethod.equalsIgnoreCase("DELETE"))
			{
				//System.out.println("***************************************************************************************************************************");
				log.info("***************************************************************************************************************************");
				//System.out.println("The request URI is: "+strURI);
				log.info("The request URI is: "+strURI);
				//System.out.println("The request method is: "+strMethod);
				log.info("The request method is: "+strMethod);
				//System.out.println("The request parameters are: "+strParams);
				log.info("The request parameters are: "+strParams);
				//System.out.println("The header length is "+requestHttpHeaders.length+"\nThe header content of the request is: \n");
				for (int i = 0; i < requestHttpHeaders.length; i++)
				{
					//System.out.println(requestHttpHeaders[i].toString());
				}
				log.info("The header length is "+requestHttpHeaders.length+"\nThe header content of the request is: \n");
				for (int i = 0; i < requestHttpHeaders.length; i++)
				{
					log.info(requestHttpHeaders[i].toString());
				}
				//System.out.println("The status of the request is :\n"+strStatus);
				log.info("The status of the request is :\n"+strStatus);

				log.info("***************************************************************************************************************************");
			}
			else
			{
				//System.out.println("***************************************************************************************************************************");
				log.info("***************************************************************************************************************************");
				//System.out.println("The request URI is: "+strURI);
				log.info("The request URI is: "+strURI);
				//System.out.println("The request method is: "+strMethod);
				log.info("The request method is: "+strMethod);
				//System.out.println("The request parameters are: "+strParams);
				log.info("The request parameters are: "+strParams);
				//System.out.println("The header length is "+requestHttpHeaders.length+"\nThe header content of the request is: \n");
				for (int i = 0; i < requestHttpHeaders.length; i++)
				{
					//System.out.println(requestHttpHeaders[i].toString());
				}
				log.info("The header length is "+requestHttpHeaders.length+"\nThe header content of the request is: \n");
				for (int i = 0; i < requestHttpHeaders.length; i++)
				{
					log.info(requestHttpHeaders[i].toString());
				}
				//System.out.println("The request body is :\n"+GeneralUtils.prettyprintJsonResponse(strRequestBody));
				log.info("The request body is :\n"+GeneralUtils.prettyprintJsonResponse(strRequestBody));
				//System.out.println("The status of the request is :\n"+strStatus);
				log.info("The status of the request is :\n"+strStatus);

				log.info("***************************************************************************************************************************");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static synchronized void printArtifacts(String strStatus, String strURI, String strMethod, String strParams, String strRequestHeaders, String strRequestBody, String strResponseHeaders, String strResponseBody)
	{

		try
		{
			if(strMethod.equalsIgnoreCase("GET") || strMethod.equalsIgnoreCase("DELETE"))
			{
				log.info("***************************************************************************************************************************");
				log.info("The request URI is: "+strURI);
				log.info("The request method is: "+strMethod);
				log.info("The request parameters are: "+strParams);
				log.info("The header content of the request is:\n"+strRequestHeaders);
				log.info("The status of the request is :\n"+strStatus);
				log.info("The header content of the response is:\n"+strResponseHeaders);
				log.info("The response body is :\n"+ (strResponseBody.isEmpty() || strResponseBody == null ? "No Response": GeneralUtils.prettyprintJsonResponse(strResponseBody) ));
				log.info("***************************************************************************************************************************");
			}
			else
			{
				log.info("***************************************************************************************************************************");
				log.info("The request URI is: "+strURI);
				log.info("The request method is: "+strMethod);
				log.info("The request parameters are: "+strParams);
				log.info("The header content of the request is:\n"+strRequestHeaders);
				log.info("The request body is :\n"+  (strRequestBody.isEmpty() || strRequestBody == null ? "No Response": GeneralUtils.prettyprintJsonResponse(strRequestBody)));
				log.info("The status of the request is :\n"+strStatus);
				log.info("The header content of the response is:\n"+strResponseHeaders);
				log.info("The response body is :\n"+ (strResponseBody.isEmpty() || strResponseBody == null ? "No Response": GeneralUtils.prettyprintJsonResponse(strResponseBody) ));
				log.info("***************************************************************************************************************************");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
		
	}

	
	public static synchronized String intializeLoggin(String strClassName)
    {
        Log.beginTestCase(strClassName);
        return GeneralUtils.getSimpleName(strClassName);
    }
}
