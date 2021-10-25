// PACKAGE

package id.co.emobile.samba.web.action.security;


import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

// IMPORT

import id.co.emobile.samba.web.action.BaseAction;
import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.entity.UserPreference;
import id.co.emobile.samba.web.helper.WebModules;
import id.co.emobile.samba.web.interceptor.ModuleCheckable;
import id.co.emobile.samba.web.service.SambaWebException;
import id.co.emobile.samba.web.service.UserDataService;
import id.co.emobile.samba.web.utils.CommonUtil;

// LOGIC
public class UserPreferenceAction extends BaseAction  implements ModuleCheckable{
	// INITIALIZATION
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(UserPreferenceAction.class);
	
	@Override
	protected Logger getLogger() 
	{
		return LOG;
	}
	
	@Autowired
	private UserDataService userDataService;
	
	// FROM JSP LAYOUT			
	private UserPreference userPreference;
	private String message;
	
	// CONDITION
	public String execute() 
	{
		UserDataLoginVO userDataLoginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
		setUserPreference(userDataLoginVO.getUserPreference());
		return EDIT;
	}
	
	public String update() 
	{
		LOG.info("processing update user preference with param : " + userPreference);
		try 
		{
			UserDataLoginVO userDataLoginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
			userDataService.updateUserPreferenceByID(userDataLoginVO, userPreference);
//			LOG.debug("User Preference - Update process : User ID " + userDataLoginVO.getId());
//			LOG.debug("User Preference - Update process : User Preference " + userPreference);
			userDataLoginVO.setUserPreference(userPreference);
			session.put(LOGIN_KEY, userDataLoginVO);
			Locale localeID = CommonUtil.localeFinder(userDataLoginVO.getUserPreference().getLanguage());
			session.put(WEB_LOCALE_KEY, localeID);
		} 
		catch(SambaWebException gwe) 
		{
			handleException(gwe);
			LOG.warn("User Preference - Update failed : Error [{}] {}", gwe.getErrorCode(), gwe.getMessage());
		} 
		catch (Exception e) 
		{
			handleException(e);
			LOG.warn("User Preference - Update failed", e);
		}
		return SUCCESS;
	}

	public String finish()
	{
		addActionMessage(getText("rm.generalMessage", new String[]{getText("t.userPreference"), getText("l.updated")}));
		return FINISH;
	}
	
	// SETTER
	public void setUserPreference(UserPreference userPreference) 
	{
		this.userPreference = userPreference;
	}
	
	public void setMessage(String message) 
	{
		this.message = message;
	}
	
	// GETTER
	public static long getSerialversionuid() 
	{
		return serialVersionUID;
	}
	
	public static Logger getLog() 
	{
		return LOG;
	}
	
	public UserPreference getUserPreference() 
	{
		return userPreference;
	}

	public String getMessage() 
	{
		return message;
	}
	
	@Override
	public int getMenuId() {
		return WebModules.MODULE_SECURITY_USER_PREFERENCE;
	}
}