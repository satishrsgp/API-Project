package com.utils;

public class RequestResponseHandler
{
	private String strRequestURI;
	private String strRequestType; //added to handle the post requests
	private String strRequestHeaders;
	private String strResponseHeaders;
	private String strResponse;
	private String strRequestStatus;
	private int intResponseCode;

	public String getStrRequestURI()
	{
		return strRequestURI;
	}

	public void setStrRequestURI(String strRequestURI)
	{
		this.strRequestURI = strRequestURI;
	}

	public String getStrRequestHeaders()
	{
		return strRequestHeaders;
	}

	public void setStrRequestHeaders(String strRequestHeaders)
	{
		this.strRequestHeaders = strRequestHeaders;
	}

	public String getStrResponseHeaders()
	{
		return strResponseHeaders;
	}

	public void setStrResponseHeaders(String strResponseHeaders)
	{
		this.strResponseHeaders = strResponseHeaders;
	}

	public String getStrResponse()
	{
		return strResponse;
	}

	public void setStrResponse(String strResponse)
	{
		this.strResponse = strResponse;
	}

	public String getStrRequestStatus()
	{
		return strRequestStatus;
	}

	public void setStrRequestStatus(String strRequestStatus)
	{
		this.strRequestStatus = strRequestStatus;
	}
	
	public int getIntResponseCode()
	{
		return intResponseCode;
	}

	public void setIntResponseCode(int intResponseCode)
	{
		this.intResponseCode = intResponseCode;
	}

	public String getStrRequestType() {
		return strRequestType;
	}

	public void setStrRequestType(String strRequestType) {
		this.strRequestType = strRequestType;
	}
}