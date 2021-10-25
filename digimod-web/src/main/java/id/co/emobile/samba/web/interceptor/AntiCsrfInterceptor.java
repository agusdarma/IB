package id.co.emobile.samba.web.interceptor;

import id.co.emobile.samba.web.action.BaseAction;
import id.co.emobile.samba.web.data.WebConstants;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AntiCsrfInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(AntiCsrfInterceptor.class);

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String pathInfo = request.getServletPath();
		String pathTranslated = request.getPathTranslated();
		String[] segments = pathInfo.split("/");
		pathInfo = "/" + segments[segments.length-1];
//		Log.debug("last Segments :" + pathInfo);
		LOG.debug("pathInfo: {}, pathTranslated: {}", pathInfo, pathTranslated);
		
		if(Arrays.asList(WebConstants.AJAX_PROCESS).contains(pathInfo))
		{
			LOG.debug("Checking CSRF for pathInfo: {}", pathInfo);
			Enumeration e = request.getHeaderNames();
			int score = 0;
			while (e.hasMoreElements()) {
				String name = (String) e.nextElement();
				String value = request.getHeader(name);
				if(name.equals("x-requested-with"))
				{
					score++;
				}
//				LOG.debug("{} = {}", name, value);
			}
			if(score==0)
			{
				LOG.warn("URL direct attack detected, redirecting to main menu");
				return BaseAction.ILLEGAL_SUBMIT_METHOD;
			}
		}

		return invocation.invoke();
	}
	
	/*/UserData!processInput.web*/
	//kalo dia ada dalam enum process 
	//cek ada gak x-requested-with = XMLHttpRequest, jika tidak redirect invalid module
	//else cek token

}
