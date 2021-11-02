package id.co.emobile.samba.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.WebResultVO;
import id.co.emobile.samba.web.data.param.UserDataParamVO;
import id.co.emobile.samba.web.entity.UserData;
import id.co.emobile.samba.web.entity.UserLevel;
import id.co.emobile.samba.web.entity.UserPreference;
import id.co.emobile.samba.web.mapper.UserDataMapper;
import id.co.emobile.samba.web.mapper.UserPreferenceMapper;
import id.co.emobile.samba.web.utils.SecureUtils;

@Service
public class UserDataService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDataService.class);

	@Autowired
	private AppsTimeService timeService;

	@Autowired
	private UserDataMapper userDataMapper;

	@Autowired
	private BizMessageService messageService;

	@Autowired
	private SettingService settingService;

	@Autowired
	private UserPreferenceMapper userPreferenceMapper;

	@Autowired
	private UserActivityService userActivityService;
	
	public List<UserData> getListUserIbActive() {
		List<UserData> listUserIb = userDataMapper.findListUserIbActive();
		return listUserIb;
	}
	

	@Transactional(rollbackFor=Exception.class)
	public WebResultVO changePassword(int userId, String oldPassword, String newPassword, String confirmPassword, Locale language, UserDataLoginVO loginVO)
			throws SambaWebException {
		WebResultVO wrv = new WebResultVO();
		UserData userData = userDataMapper.findUserDataById(userId);
		// validate password
		if (StringUtils.isEmpty(oldPassword)) {
			LOGGER.warn("old password is empty!");
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.oldPassword", language)});
		}
		if (StringUtils.isEmpty(newPassword)) {
			LOGGER.warn("new password is empty!");
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.newPassword", language)});
		}
		if (!newPassword.equals(confirmPassword)) {
			LOGGER.warn("new password and confirm password is different! new pass: {}, confirm pass: {}", newPassword, confirmPassword);
			throw new SambaWebException(SambaWebException.NE_PASSWORD_DIFFERENT);
		}
		if (oldPassword.equals(newPassword)) {
			LOGGER.warn("Old password and new password is same! new pass: {}, old pass: {}", newPassword, oldPassword);
			throw new SambaWebException(SambaWebException.NE_PASSWORD_NEW_OLD_SAME);
		}
		if(!SecureUtils.regexPasswordChecker(settingService.getSettingAsInt(SettingService.SETTING_PASSWORD_MIN_LENGTH), newPassword)) {
			LOGGER.warn("Password need to contain alphanumeric and special symbol");
			throw new SambaWebException(SambaWebException.NE_INVALID_PASS_FORMAT);
		}
		if (userData == null) {
			LOGGER.debug("Invalid userInfo with Id: [{}]", userId);
			throw new SambaWebException(SambaWebException.NE_INVALID_USER);
		}
		String encOldPassword = SecureUtils.passwordDigest(userData.getUserCode(), oldPassword);
		if (!encOldPassword.equals(userData.getUserPassword())) {
			LOGGER.warn("Invalid password for {}", userData.getUserCode() );
			throw new SambaWebException(SambaWebException.NE_WRONG_PASSWORD);
		}
		String encNewPassword = SecureUtils.passwordDigest(userData.getUserCode(), newPassword);
		userData.setUserPassword(encNewPassword);
		userData.setHasChangePass(true);
		userData.setPassChangedOn(timeService.getCurrentTime());

		userDataMapper.updateUserData(userData);
		
		userActivityService.createUserActivityChangePass(loginVO);

		wrv.setRc(WebConstants.RESULT_SUCCESS);
		wrv.setMessage(messageService.getMessageFor("success.changePassword", language));
		wrv.setType(WebConstants.TYPE_INSERT);
		wrv.setPath(WebConstants.PATH_UPDATE_CHANGE_PASSWORD);
		return wrv;
	}

//	private void checkForOldPassword(String encNewPassword, String encOldPassword, String userId)
//			throws SambaWebException {
//		if (encNewPassword.equals(encOldPassword))
//			throw new SambaWebException(SambaWebException.NE_PASSWORD_NEW_OLD_SAME);
//
//		int checkAmount = 4;
//
//		if (checkAmount > 6) checkAmount = 6;
//		else if (checkAmount < 0) checkAmount = 0;
//
//		// no need to access password history if checkAmount = 0
//		if (checkAmount == 0) return;
//		PasswordHistory history = passHistoryMapper.findPassHistoryByUserId(userId);
//		if (history == null) {
//			history = new PasswordHistory();
//			history.setUserId(userId);
//			history.setPassHistory1(encNewPassword);
//			passHistoryMapper.createPassHistory(history);
//		} else {
//			if (checkAmount >= 1 && encNewPassword.equals(history.getPassHistory1()))
//				throw new SambaWebException(SambaWebException.NE_PASSWORD_NEW_OLD_SAME);
//			if (checkAmount >= 2 && encNewPassword.equals(history.getPassHistory2()))
//				throw new SambaWebException(SambaWebException.NE_PASSWORD_NEW_OLD_SAME);
//			if (checkAmount >= 3 && encNewPassword.equals(history.getPassHistory3()))
//				throw new SambaWebException(SambaWebException.NE_PASSWORD_NEW_OLD_SAME);
//			if (checkAmount >= 4 && encNewPassword.equals(history.getPassHistory4()))
//				throw new SambaWebException(SambaWebException.NE_PASSWORD_NEW_OLD_SAME);
//			if (checkAmount >= 5 && encNewPassword.equals(history.getPassHistory5()))
//				throw new SambaWebException(SambaWebException.NE_PASSWORD_NEW_OLD_SAME);
//			if (checkAmount >= 6 && encNewPassword.equals(history.getPassHistory6()))
//				throw new SambaWebException(SambaWebException.NE_PASSWORD_NEW_OLD_SAME);
//			if (!StringUtils.isEmpty(history.getPassHistory5()))
//				history.setPassHistory6(history.getPassHistory5());
//			if (!StringUtils.isEmpty(history.getPassHistory4()))
//				history.setPassHistory5(history.getPassHistory4());
//			if (!StringUtils.isEmpty(history.getPassHistory3()))
//				history.setPassHistory4(history.getPassHistory3());
//			if (!StringUtils.isEmpty(history.getPassHistory2()))
//				history.setPassHistory3(history.getPassHistory2());
//			if (!StringUtils.isEmpty(history.getPassHistory1()))
//				history.setPassHistory2(history.getPassHistory1());
//			history.setPassHistory1(encNewPassword);
//			history.setUpdatedBy(userId);
//			passHistoryMapper.updatePassHistory(history);
//		}
//	}

	@Transactional(rollbackFor=Exception.class)
	public WebResultVO insertOrUpdateUserData(UserData userData, UserDataLoginVO loginVO, String confirmPassword, Locale language) throws SambaWebException 
	{
		WebResultVO wrv = new WebResultVO();
		Date now = timeService.getCurrentTime();
		userData.setUpdatedBy(loginVO.getId());
		userData.setUpdatedOn(now);
		if(StringUtils.isEmpty(userData.getUserName())) {
			LOGGER.warn("User name is empty !");
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.userName")});
		}
		if(userData.getId()==0)
		{
			try {
				userData.setCreatedBy(loginVO.getId());
				userData.setCreatedOn(now);
				userData.setTotalClientCommission("0");
				userData.setClientCommissionWithdrawn("0");
				validateInput(userData, userData.getUserPassword(), confirmPassword);
				userData.setUserPassword(SecureUtils.passwordDigest(userData.getUserCode().trim(), userData.getUserPassword()));
				userData.setHasChangePass(false);
				userDataMapper.insertNewUser(userData);
				// called to create default user preference
				UserPreference userPreference = getUserPreferenceForUser(userData.getId());
				LOGGER.debug("Created default: {}", userPreference);
				
				String desc = "Create User [" + userData.getUserCode() + "] " + userData.getUserName();
				userActivityService.createUserActivityCreateData(loginVO, 
						UserActivityService.MODULE_MANAGE_USER, desc, UserActivityService.TABLE_USER_DATA, 
						userData.getId(), userData);
				
				wrv.setRc(WebConstants.RESULT_SUCCESS);
				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
						new String[] {messageService.getMessageFor("l.userData", language),
						messageService.getMessageFor("l.created", language)}, language));
				wrv.setType(WebConstants.TYPE_INSERT);
				return wrv;
			} catch (SambaWebException swe) {
				LOGGER.warn("error when saving user data {} with errorCode {}", userData, swe.getErrorCode());
				throw swe;
			}
		}
		else
		{
			LOGGER.debug("update user data with data :" + userData);
			try{
				UserData original = findUserById(userData.getId());
				
				userDataMapper.updateNewUser(userData);
				
				String desc = "Update User [" + userData.getUserCode() + "] " + userData.getUserName();
				userActivityService.createUserActivityUpdateData(loginVO, 
						UserActivityService.MODULE_MANAGE_USER, desc, UserActivityService.TABLE_USER_DATA, 
						userData.getId(), userData, original);

				wrv.setRc(WebConstants.RESULT_SUCCESS);
				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
						new String[] {messageService.getMessageFor("l.userData", language),
						messageService.getMessageFor("l.updated", language)}, language));
				wrv.setType(WebConstants.TYPE_UPDATE);
				wrv.setPath(WebConstants.PATH_UPDATE_USER_DATA);
				return wrv;
			}catch(Exception e){
				LOGGER.warn("Gagal update user data :" + e);
				throw new SambaWebException(e);
			}
		}
	}
	

	/*-----------------Reset-Password-----------------*/
	public WebResultVO resetPassword(UserData userData, UserDataLoginVO loginVO, String newPassword, String confirmPassword, Locale language)
			throws SambaWebException {
		WebResultVO wrv = new WebResultVO();
		Date now = new Date();
		userData.setUpdatedBy(loginVO.getId());
		userData.setUpdatedOn(now);
//		UserData original = findUserById(userData.getId());
		if (StringUtils.isEmpty(newPassword)) {
			LOGGER.warn("New Password is empty");
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.newPassword", language)});
		}
		if (!newPassword.equals(confirmPassword)) {
			LOGGER.warn("Confirm Password is empty");
			throw new SambaWebException(SambaWebException.NE_PASSWORD_DIFFERENT);
		}
		if(!SecureUtils.regexPasswordChecker(settingService.getSettingAsInt(SettingService.SETTING_PASSWORD_MIN_LENGTH), newPassword)) {
			LOGGER.warn("Password need to contain alphanumeric and special symbol");
			throw new SambaWebException(SambaWebException.NE_INVALID_PASS_FORMAT);
		}

		String encNewPassword = SecureUtils.passwordDigest(userData.getUserCode(), newPassword);
		userData.setUserPassword(encNewPassword);
		userData.setHasChangePass(false);
		userData.setUpdatedOn(now);
		userData.setUpdatedBy(loginVO.getId());
		userData.setInvalidCount(0);
		userDataMapper.updateResetPassword(userData);
		
		userActivityService.createUserActivityResetPass(loginVO, userData);
		
		wrv.setRc(WebConstants.RESULT_SUCCESS);
		wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
				new String[] {messageService.getMessageFor("l.resetPassword", language),
				messageService.getMessageFor("l.updated", language)}, language));
		wrv.setType(WebConstants.TYPE_UPDATE);
		wrv.setPath(WebConstants.PATH_UPDATE_RESET_PASSWORD);
		return wrv;
	}

	public Integer countLevelUsedByName(int levelId){
		try {
			int count = userDataMapper.countLevelUsedByName(levelId);
			return count;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return 0;
		}
	}

	public Integer countUserValidate(String userID){
		try {
			int count = userDataMapper.countUserValidate(userID);
			return count;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return 0;
		}
	}
	
	public List<UserData> findUserDataActive() {
		List<UserData> listUser = userDataMapper.findUserDataActive();
		if (listUser == null)
			return new ArrayList<UserData>();
		return listUser;
	}

	public List<UserData> findUserByParam(UserDataParamVO addNewUserParamVO) {
		try {
			List<UserData> listUser = userDataMapper.findUserByParam(addNewUserParamVO);
			return listUser;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return null;
		}
	}

	public int countUserByParam(UserDataParamVO addNewUserParamVO) {
		try {
			int count = userDataMapper.countUserByParam(addNewUserParamVO);
			return count;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return 0;
		}
	}

	public UserData findUserById(int userId) {
		try {
			UserData user = userDataMapper.findUserById(userId);
			return user;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return null;
		}
	}

	public void validateInput(UserData userData, String newPassword, CharSequence confirmPassword) throws SambaWebException
	{
		LOGGER.debug("validate properties user data!");
		if(StringUtils.isEmpty(userData.getUserCode())) {
			LOGGER.warn("user code is empty!");
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.userCode")});
		}
		if(StringUtils.isEmpty(userData.getUserPassword())) {
			LOGGER.warn("password is empty!");
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.password")});
		}
		int minimumLength = settingService.getSettingAsInt(SettingService.SETTING_PASSWORD_MIN_LENGTH);
		if(userData.getUserPassword().length() < minimumLength){
			LOGGER.warn("password length is smaller than {} !", minimumLength);
			throw new SambaWebException(SambaWebException.NE_INVALID_PASSWORD_LENGTH, Integer.toString(minimumLength));
		}
		if(StringUtils.isEmpty(confirmPassword)) {
			LOGGER.warn("confirm password is empty!");
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.confirmPassword")});
		}
		if(!(userData.getUserPassword()).equals(confirmPassword)) {
			LOGGER.warn("confirm password and password is different!. pass : {}, confirmPass : {}", userData.getUserPassword(), confirmPassword);
			throw new SambaWebException(SambaWebException.NE_PASSWORD_DIFFERENT);
		}
		/*if (!newPassword.equals(confirmPassword())) {
			throw new GatewayWebException(GatewayWebException.NE_PASSWORD_DIFFERENT);
		}*/
		if(!SecureUtils.regexPasswordChecker(settingService.getSettingAsInt(SettingService.SETTING_PASSWORD_MIN_LENGTH), userData.getUserPassword())) {
			LOGGER.warn("password does not match regex requirement!");
			throw new SambaWebException(SambaWebException.NE_INVALID_PASS_FORMAT, settingService.getSettingAsString(SettingService.SETTING_PASSWORD_MIN_LENGTH));
		}
		int countUser = countUserValidate(userData.getUserCode().toUpperCase());
		if(countUser!=0) {
			LOGGER.warn("user code already used! userCode : {}", userData.getUserCode());
			throw new SambaWebException(SambaWebException.NE_DUPLICATE_DATA,new String[] {messageService.getMessageFor("l.userCode")});
		}
	}

	// USER PREFERENCES
	@Transactional(rollbackFor=Exception.class)
	public void insertUserPreference(UserPreference userPreference) throws SambaWebException
	{
		try {
			userPreferenceMapper.insertUserPreference(userPreference);
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			throw new SambaWebException(e);
		}
	}

	public UserPreference getUserPreferenceForUser(int userId)
	{
		UserPreference userPreference = userPreferenceMapper.findUserPreferenceByID(userId);
		if (userPreference == null) {
			userPreference = new UserPreference();
			userPreference.setFontFamily(WebConstants.DEFAULT_FONTFAMILY);
			userPreference.setFontSize(WebConstants.DEFAULT_FONTSIZE);
			userPreference.setLanguage(WebConstants.DEFAULT_LANGUAGE);
			userPreference.setTheme(WebConstants.DEFAULT_THEME);
			userPreference.setUserID(userId);
			
			int created = userPreferenceMapper.insertUserPreference(userPreference);
			LOGGER.debug("Created {} {}", created, userPreference);
		}		
		return userPreference;
	}

//	public UserPreference findUserPreferenceByID(int userID)
//	{
//		UserPreference userPreference = userPreferenceMapper.findUserPreferenceByID(userID);
//		return userPreference;
//	}

	public void updateUserPreferenceByID(UserDataLoginVO loginVO, UserPreference userPreference) 
			throws SambaWebException
	{
		try {
			UserPreference original = userPreferenceMapper.findUserPreferenceByID(loginVO.getId());
			String desc = "Update User Preference";
			userActivityService.createUserActivityUpdateData(loginVO, 
					UserActivityService.MODULE_UPDATE_USER_PREF, desc, 
					UserActivityService.TABLE_USER_PREF, userPreference.getUserID(), 
					userPreference, original);
			
			userPreference.setUserID(loginVO.getId());
			userPreferenceMapper.updateUserPreferenceByID(userPreference);
			
			loginVO.setUserPreference(userPreference);
		} catch (Exception e) {
			LOGGER.warn("Exception in updateUserPreferenceByID: " + userPreference, e);
			throw new SambaWebException(e);
		}
	}
	
	public List<UserData> findListUserApproval() {
		List<UserData> listApproval = userDataMapper.findUserDataActiveByGroupAndLevelType(0, WebConstants.LEVEL_TYPE_APPROVAL);
		if (listApproval == null)
			listApproval = new ArrayList<>();
		return listApproval;
	}
}
