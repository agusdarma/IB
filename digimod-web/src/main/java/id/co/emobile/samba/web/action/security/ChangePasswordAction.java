package id.co.emobile.samba.web.action.security;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import id.co.emobile.samba.web.action.BaseAction;
import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.WebResultVO;
import id.co.emobile.samba.web.helper.WebModules;
import id.co.emobile.samba.web.interceptor.ModuleCheckable;
import id.co.emobile.samba.web.service.AppsTimeService;
import id.co.emobile.samba.web.service.SambaWebException;
import id.co.emobile.samba.web.service.UserDataService;

public class ChangePasswordAction extends BaseAction implements ModuleCheckable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ChangePasswordAction.class);
	
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;

	private WebResultVO wrv;
	private String json;
	private String message;
	
	@Autowired
	private AppsTimeService timeService;
	
	@Autowired
	private UserDataService userDataService;
	
	@Override
	protected Logger getLogger() {
		return LOG;
	}
	
	public String execute() {
		LOG.debug("Processing -> execute()");
		setMessage(getFlashMessage());
		return INPUT;
	}
	
	public String forceChgPassword(){
		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
		LOG.debug("Processing -> Force change password for {}", loginVO.getUserCode());
		return INPUT;
	}
	
	public String process() {
		LOG.debug("Processing -> Change Password..");
		try {
			Locale language=(Locale) session.get(WEB_LOCALE_KEY);
			UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);			
			
			LOG.info("change password with user id:" +loginVO.getId()+
					", old password:" + oldPassword+ 
					", new password:" +newPassword+
					", language:" +language);
			wrv = userDataService.changePassword(loginVO.getId(), oldPassword, newPassword, confirmPassword, language, loginVO);
			loginVO.setPassChangedOn(timeService.getCurrentTime());
			session.put(LOGIN_KEY, loginVO);
/*			if(wrv.getType() == WebConstants.TYPE_INSERT)
			{
				setFlashMessage(wrv.getMessage());
			}*/
		} catch(SambaWebException mwe) {
			wrv = handleJsonException(mwe);
		} catch (Exception e) {
			wrv = handleJsonException(e);
		}
		try {
			json = objectMapper.writeValueAsString(wrv);
		} catch (Exception e) {
			LOG.warn("Exception in serializing " + wrv, e);
		}
		return "inputJson";
	}

	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public int getMenuId() {
		return WebModules.MODULE_SECURITY_CHANGE_PASSWORD;
	}
	
	public InputStream getWrv() {
		return new ByteArrayInputStream(json.toString().getBytes());
	}


}
