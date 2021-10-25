package id.co.emobile.samba.web.interceptor;

import id.co.emobile.samba.web.action.BaseAction;
import id.co.emobile.samba.web.action.security.ChangePasswordAction;
import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.mapper.UserDataMapper;
import id.co.emobile.samba.web.service.AppsTimeService;
import id.co.emobile.samba.web.service.SettingService;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SecurityInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = LoggerFactory.getLogger(SecurityInterceptor.class);
	
	@Autowired
	private UserDataMapper userDataMapper;
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private AppsTimeService timeService;
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String, Object> session = invocation.getInvocationContext().getSession();
		UserDataLoginVO userData = (UserDataLoginVO) session.get(BaseAction.LOGIN_KEY);

		if (userData == null || userData.getId() == 0) {
			LOG.debug("Session is expired or no session");
			return BaseAction.SESSION_EXPIRED;
		}
		Object o = invocation.getAction();
	
		if (o instanceof ModuleCheckable) {
			int menuId = ((ModuleCheckable) o).getMenuId();
			if (userData.getLevelVO() == null) {
				LOG.debug("No rank for user: {}", userData);
				return BaseAction.MODULE_ACCESS_DENIED;
			}
			if (!userData.getLevelVO().isMenuAllowed(menuId)) {
				LOG.debug("User does not have access to menu: {}", menuId );
				if (o instanceof ActionSupport) {
					ActionSupport a = (ActionSupport) o;
					a.addActionError(a.getText("err.accessDenied"));
					
				}
				return BaseAction.MODULE_ACCESS_DENIED;
			}
		}
		if (userDataMapper.checkSessionLogin(userData.getId(), userData.getSessionId()) == 0) {
			LOG.debug("User Session is over: " + userData);
			if (o instanceof ActionSupport) {
				ActionSupport a = (ActionSupport) o;
				a.addActionError(a.getText("err.needLoginAgain"));
			}
			return BaseAction.SESSION_EXPIRED;
		}
		
		
		if (!userData.isHasChangePass()) {
			String actionName = o.getClass().getName();
			String changePassword = ChangePasswordAction.class.getName();
			if (!actionName.equals(changePassword)) {
				if (o instanceof ActionSupport) {
					ActionSupport a = (ActionSupport) o;
					// a.addActionError(a.getText("err.needChangePassword"));
					a.addActionError("Change Password is required");
				}
				return BaseAction.CHANGE_PASSWORD_EXPIRED; // CHANGE_PASSWORD
			}
		}
		userData.setLastAccessOn(timeService.getCurrentTime());
		userDataMapper.updateLastAccess(userData);
		return invocation.invoke();
	}

}
