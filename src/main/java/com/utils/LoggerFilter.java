package com.utils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class LoggerFilter extends Filter<ILoggingEvent>
{
	@Override
	public FilterReply decide(ILoggingEvent event)
	{
		if(event.getLoggerName().startsWith("com.jayway.jsonpath.JsonPath"))
		{
			return FilterReply.DENY;
		}
		else
		{
			return FilterReply.ACCEPT;
		}
	}

}
