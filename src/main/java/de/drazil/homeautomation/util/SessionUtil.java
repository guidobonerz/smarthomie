package de.drazil.homeautomation.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionUtil
{
	public static HttpServletRequest getHttpServletRequest()
	{
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	public static HttpSession getSession()
	{
		return getHttpServletRequest().getSession();
	}

	public static void setSessionAttribute(final String key, final Object value) {
		getSession().setAttribute(key, value);
	}

	public static Object getSessionAttribute(final String key)
	{
		return getSession().getAttribute(key);
	}
}
