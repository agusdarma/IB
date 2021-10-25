package id.co.emobile.samba.web.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.co.emobile.samba.web.data.LoginData;
import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.UserLevelVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.entity.UserData;
import id.co.emobile.samba.web.entity.UserLevel;
import id.co.emobile.samba.web.entity.UserLevelMenu;
import id.co.emobile.samba.web.entity.UserPreference;
import id.co.emobile.samba.web.mapper.UserDataMapper;
import id.co.emobile.samba.web.mapper.UserLevelMapper;
import id.co.emobile.samba.web.utils.SecureUtils;

@Service
public class SecurityService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);

	@Autowired
	private UserDataMapper userDataMapper;

	@Autowired
	private UserDataService userDataService;

	@Autowired
	private UserLevelMapper userLevelMapper;

	@Autowired
	private AppsTimeService timeService;

	@Autowired
	private SettingService settingService;

	@Autowired
	private UserActivityService userActivityService;
	
	@Transactional
	public UserDataLoginVO validateUserLogin(LoginData loginData, String sessionId, String ipAddress)
			throws SambaWebException {
		LOGGER.info("Validate Login for: {}", loginData);
		try {
			UserData userData = userDataMapper.findUserDataByUserCode(loginData.getUserCode());
			if (userData == null) {
				LOGGER.warn("Invalid user data with Code: [{}]", loginData.getUserCode());
				throw new SambaWebException(SambaWebException.NE_INVALID_USER);
			}
			
			UserData original = new UserData();
			BeanUtils.copyProperties(userData, original);
			
			// check user status
			if (WebConstants.STATUS_ACTIVE != userData.getUserStatus() ) {
				LOGGER.warn("[{}] Invalid Status: [{}]", userData.getUserCode(), userData.getUserStatus());
				throw new SambaWebException(SambaWebException.NE_USER_DATA_INACTIVE);
			}

			int invalidCount = userData.getInvalidCount();
			if (invalidCount >= settingService.getSettingAsInt(SettingService.SETTING_MAX_INVALID_LOGIN))
			{
				// block user
				LOGGER.warn("number of invalid login exceed limit : " + settingService.getSettingAsInt(SettingService.SETTING_MAX_INVALID_LOGIN));
				throw new SambaWebException(SambaWebException.NE_USER_DATA_BLOCKED);
			}

			String plainPass = loginData.getPassword();  // SecureUtils.decodeUserPassword(loginData.getPassword());
			String encPassword = SecureUtils.passwordDigest(userData.getUserCode(), plainPass);
			if (!encPassword.equals(userData.getUserPassword())) {
				LOGGER.warn("Invalid password for {}", userData.getUserCode());
				invalidCount = invalidCount + 1;
				userData.setInvalidCount(invalidCount);

				userDataMapper.updateUserData(userData);
				throw new SambaWebException(SambaWebException.NE_WRONG_PASSWORD);
			}  // end if check password
			
			long lastAccessTime = 0 ;
			if(userData.getLastAccessOn()!=null) lastAccessTime = userData.getLastAccessOn().getTime();
			long diffAccess = timeService.getCurrentTime().getTime() - lastAccessTime;
			long minutes =  (diffAccess / (1000*60));
			
			if(!userData.getSessionId().equals(""))
			{
				if(minutes <= settingService.getSettingAsInt(SettingService.SETTING_DOUBLE_LOGIN_TIME))
				{
					LOGGER.warn("User {} has already login, unable to login again", userData);
					throw new SambaWebException(SambaWebException.NE_USER_ALREADY_LOGIN);
				}	
			}			
			userData.setSessionId(sessionId);

			// get user level
			UserLevel userLevel = userLevelMapper.findUserLevelById(userData.getLevelId());
			if (userLevel == null || userLevel.getListMenu().size() == 0) {
				LOGGER.warn("User level not found for {} with level name {}", userData.getUserCode(), userData.getUserLevelDisplay());
				throw new SambaWebException(SambaWebException.NE_USER_DATA_INVALID_LEVEL,
						new String[] { userData.getUserCode(), userData.getUserLevelDisplay() });
			}
			UserLevelVO levelVO = new UserLevelVO(userLevel.getListMenu());
			levelVO.setLevelName(userLevel.getLevelName());
			levelVO.setLevelDesc(userLevel.getLevelDesc());
			levelVO.setLevelType(userLevel.getLevelType());
			
			UserDataLoginVO loginVO = new UserDataLoginVO();
			BeanUtils.copyProperties(userData, loginVO);
			loginVO.setLevelVO(levelVO);
			List<UserLevelMenu> listLevelMenu = userLevelMapper.findUserLevelMenuByLevelId(userData.getLevelId()); 
			loginVO.setListUserLevelMenu(listLevelMenu);
			
			userData.setInvalidCount(0);
			userData.setLastLoginOn(timeService.getCurrentTime());
			userData.setUpdatedOn(timeService.getCurrentTime());

			if(userData.getPassChangedOn()!=null && !userData.getUserCode().equals("root"))
			{
				long diff = timeService.getCurrentTime().getTime() - userData.getPassChangedOn().getTime();
				long days =  (diff / (1000*60*60*24));
				if(days >= settingService.getSettingAsInt(SettingService.SETTING_EXPIRY_DAY_PASSWORD))
				{
					userData.setHasChangePass(false);
				}
			}
			userDataMapper.updateUserData(userData);

			UserPreference userPreference = userDataService.getUserPreferenceForUser(loginVO.getId());
			loginVO.setUserPreference(userPreference);
			loginVO.setIpAddress(ipAddress);
			
			userActivityService.createUserActivityLogin(loginVO);

			return loginVO;
		} catch (SambaWebException jme) {
			throw jme;
		} catch (Exception e) {
			LOGGER.error("Exception: " + e, e);
			String msgError = e.getMessage();
			if (msgError.length() > 160)
				msgError = msgError.substring(0, 157) + "..";
			throw new SambaWebException(e);
		}
	}

	public void logoutUser(UserDataLoginVO loginVO) {
		if (loginVO == null) return;
		LOGGER.info("[{}] User Logout", loginVO.getUserCode());
		try {
			
			userDataMapper.clearSessionLogin(loginVO.getId());
			
			userActivityService.createUserActivityLogout(loginVO);
		} catch (Exception e) {
			LOGGER.error("Exception: " + e, e);
		}
	}

}
