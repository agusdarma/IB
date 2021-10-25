package id.co.emobile.samba.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;

import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.VersionData;
import id.co.emobile.samba.web.data.WebResultVO;
import id.co.emobile.samba.web.entity.UserActivity;
import id.co.emobile.samba.web.entity.UserLevelMenu;
import id.co.emobile.samba.web.interceptor.ModuleStateAware;
import id.co.emobile.samba.web.service.SambaWebException;
import id.co.emobile.samba.web.service.UserActivityService;
import id.co.emobile.samba.web.utils.Constants;

public abstract class BaseAction extends ActionSupport implements SessionAware, ModuleStateAware  {
	private static final long serialVersionUID = 1L;
	
	public static final String LOGIN_KEY			= "LOGIN_KEY";
	public static final String WEB_CONTENT_KEY		= "WEB_CONTENT_KEY";
	public static final String WEB_CONTENT_KEY_2	= "WEB_CONTENT_KEY_2";
	public static final String WEB_CONTENT_CONTACT	= "WEB_CONTENT_CONTACT";
	public static final String WEB_LOCALE_KEY		= "WW_TRANS_I18N_LOCALE";
	public static final String WEB_FLASH_MESSAGE	= "WEB_FLASH_MESSAGE";
	public static final String WEB_PARAM_KEY		= "WEB_PARAM_KEY";
	public static final String BIT48				= "BIT48";
	
	
	public static final String LIST			= "list";
	public static final String EDIT			= "edit";
	public static final String FINISH		= "finish";
	public static final String SEARCH		= "search";
	public static final String CONFIRM		= "confirm";
	public static final String DETAIL		= "detail";
	public static final String POP_UP		= "pop_up";
	
	//dealer
	public static final String DEALER_BILLPAYMENT = "billPayment";
	public static final String DISTRIBUTOR = "distributor";
	public static final String RETAILER ="retailer";
	
	// result from interceptor
	public static final String SESSION_EXPIRED		= "sessionExpired";
	public static final String MODULE_ACCESS_DENIED	= "moduleAccessDenied";
	public static final String CHANGE_PASSWORD_EXPIRED	= "changePasswordExpired";
	public static final String ILLEGAL_SUBMIT_METHOD	= "illegalSubmitMethod";

	//export report
	public static final String PDF = "PDF";
	public static final String XLS = "XLS";
	public static final String CSV = "CSV";
	public static final String HTML = "HTML";	
	
	protected Map<String, Object> session;
	
	private String moduleState;

	@Autowired
	private VersionData versionData;
	
	@Autowired
	private UserActivityService userActivityService;
	
	protected abstract Logger getLogger();
	
	protected ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	@Override
	public String[] getSessionKeyToHandle() {
		return new String[] {};
	}
	
	@Override
	public String getModuleState() {
		return moduleState;
	}
	@Override
	public void setModuleState(String moduleState) {
		this.moduleState = moduleState;
	}
	
	protected void handleException(Exception e) {
		if (e instanceof SambaWebException) {
			SambaWebException nwe = (SambaWebException) e;
			if (nwe.hasInfo())
				this.addActionError(getText("rc." + nwe.getErrorCode(), nwe.getInfo()));
			else
				this.addActionError(getText("rc." + nwe.getErrorCode()));
		} else {
			LOG.warn("Unknown exception: " + e.getMessage(), e);
			this.addActionError(getText("rc." + SambaWebException.NE_UNKNOWN_ERROR));
		}
	}
	
	protected WebResultVO handleJsonException(Exception e)
	{
		WebResultVO wrv = new WebResultVO();
		if (e instanceof SambaWebException)
		{
			SambaWebException nwe = (SambaWebException) e;
			if (nwe.hasInfo())
			{
				wrv.setMessage(getText("rc." + nwe.getErrorCode(), nwe.getInfo()));
				wrv.setRc(nwe.getErrorCode());
				return wrv;
			}
			else
			{
				wrv.setMessage(getText("rc." + nwe.getErrorCode()));
				wrv.setRc(nwe.getErrorCode());
				return wrv;
			}
		}
		else
		{
			LOG.warn("Unknown exception: " + e.getMessage(), e);
			wrv.setMessage(getText("rc." + SambaWebException.NE_UNKNOWN_ERROR));
			wrv.setRc(SambaWebException.NE_UNKNOWN_ERROR);
			return wrv;
		}
	}
	
	public List<UserActivity> getLastUserActivityForUser() {
		Object o = session.get(LOGIN_KEY);
		if (o instanceof UserDataLoginVO) {
			UserDataLoginVO loginVO = (UserDataLoginVO) o;
			return userActivityService.findLastUserActivityForUser(loginVO.getId());
		}
		return new ArrayList<UserActivity>();
	}
	
	public String getVersionInfo() {
		return getText("label.versionInfo", new String[] { versionData.getAppsName(), 
				versionData.getVersion(), versionData.getBuildDate() } );
	}
	
	public String getFormatDate() {
		return Constants.NE_FORMAT_DATE_JSP;  
	};
	
	public String getFormatTime() {
		return Constants.NE_FORMAT_TIME_JSP;  
	};
	
	protected void setFlashMessage(String message) {
		session.put(WEB_FLASH_MESSAGE, message);
	}
	
	protected String getFlashMessage() {
		Object o = session.remove(WEB_FLASH_MESSAGE);
		if (o instanceof String)
			return (String) o;
		else
			return null;
	}
	
	public boolean checkPrevileges(int menuId)
	{
		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
		for (UserLevelMenu ulm : loginVO.getListUserLevelMenu()) 
		{
			if(menuId==ulm.getMenuId())
			{
				if(ulm.getAccessLevel()==1)
					return true;
				else 
					return false;
			}
		}
		return false;
	}
}