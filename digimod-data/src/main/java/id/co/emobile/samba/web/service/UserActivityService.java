package id.co.emobile.samba.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import id.co.emobile.samba.web.data.ActivityFieldVO;
import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.param.UserActivityParamVO;
import id.co.emobile.samba.web.entity.DistributionHeader;
import id.co.emobile.samba.web.entity.UserActivity;
import id.co.emobile.samba.web.entity.UserData;
import id.co.emobile.samba.web.mapper.UserActivityMapper;
import id.co.emobile.samba.web.utils.AttributeCheckerUtil;

public class UserActivityService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserActivityService.class);
	
	public static final String ACTION_LOGIN				= "Login";
	public static final String ACTION_LOGOUT			= "Logout";
	public static final String ACTION_CHANGE_PASS 		= "Change Password";
	public static final String ACTION_RESET_PASS 		= "Reset Password";
	public static final String ACTION_CREATE_DATA		= "Create New Data";
	public static final String ACTION_UPDATE_DATA		= "Update Data";
	public static final String ACTION_MAKE_DIST				= "Make Distribution";
	public static final String ACTION_CHECK_DIST			= "Check Distribution";
	public static final String ACTION_CALLBACK_DIST			= "Callback Distribution";
	public static final String ACTION_APPROVAL_DIST			= "Approval Distribution";
	
//	public static final String TRX_TYPE_USER_EDITOR 		= "User editor";
//	public static final String TRX_TYPE_ADD_USER 			= "Add New User";
//	public static final String TRX_TYPE_USER_LEVEL_EDITOR 	= "User level editor";
//	public static final String TRX_TYPE_RESET_STK			= "Reset STK";
//	public static final String TRX_TYPE_BILL_PAYMENT		= "Bill Payment";
//	public static final String TRX_TYPE_CARD_SETTING		= "Card Setting";
//	public static final String TRX_TYPE_CHANGE_SIM			= "Change Sim";
//	public static final String TRX_TYPE_TOPUP_VALUE			= "Top Up Value";
	
//	public static final String TRX_ACTION_SUCCESS	= "Success";
//	public static final String TRX_ACTION_FAILED	= "Failed";
	
	public static final String MODULE_SECURITY		= "Security"; // for login / logout
	public static final String MODULE_RESET_PASS	= "Reset Password";
	public static final String MODULE_CHANGE_PASS	= "Change Password";
	public static final String MODULE_UPDATE_USER_PREF	= "User Preference";
	public static final String MODULE_MANAGE_USER	= "Manage User";
	public static final String MODULE_MAKER				= "Maker";
	public static final String MODULE_CHECKER			= "Checker";
	public static final String MODULE_CALLBACK			= "Callback";
	public static final String MODULE_APPROVAL			= "Approval";
	public static final String MODULE_MANAGE_SOURCE_ACC	= "Manage Source Acc";
	public static final String MODULE_MANAGE_USER_LEVEL	= "Manage User Level";
	public static final String MODULE_MANAGE_USER_GROUP	= "Manage User Group";
	public static final String MODULE_MANAGE_USER_BRANCH	= "Manage User Branch";
	public static final String MODULE_MANAGE_GROUP_APPROVAL	= "Manage Group Approval";
	public static final String MODULE_MANAGE_MASTER_TRADING_ACCOUNT	= "Manage Master Trading Account";
	
	public static final String TABLE_USER_DATA	= "user_data";
	public static final String TABLE_USER_PREF = "user_preference";
	public static final String TABLE_DISTRIBUTION	= "distribution_header";
	public static final String TABLE_SOURCE_ACCOUNT = "source_account";
	public static final String TABLE_USER_LEVEL		= "user_level";
	public static final String TABLE_USER_GROUP		= "user_group";
	public static final String TABLE_MASTER_TRADING_ACCOUNT		= "master_trading_account";
	public static final String TABLE_USER_BRANCH		= "user_branch";
	public static final String TABLE_USER_GROUP_APPROVAL = "user_group_approval";
	
	private Map<String, String[]> mapCheckedAttribute = new HashMap<>();
	{
		mapCheckedAttribute.put(TABLE_USER_DATA, 
				"userCode,userName,phoneNo,userStatus,levelId,groupId".split(","));
		mapCheckedAttribute.put(TABLE_USER_PREF, 
				"fontFamily,fontSize,language,theme".split(","));
		mapCheckedAttribute.put(TABLE_SOURCE_ACCOUNT, 
				"phoneNo,sracNumber,sracName,sracPin,groupId,sracStatus".split(","));
		mapCheckedAttribute.put(TABLE_USER_LEVEL, 
				"levelName,levelType,levelDesc".split(","));
	}
	
	@Autowired
	private UserActivityMapper userActivityMapper;
	
	@Autowired
	private AppsTimeService timeService;
	
	@Autowired
	private SettingService settingService;
	
	private ObjectMapper objectMapper = new ObjectMapper();

	private ExecutorService executor;
	private int agentCount = 20;
	
	public void init() {
		executor = Executors.newFixedThreadPool(agentCount);
		LOGGER.info("UserActivityService is started with agentCount: {}", agentCount);
	}
	
	public void shutdown() {
		try {
			executor.shutdown();
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (Exception e) {
			LOGGER.warn("Exception in shutdown", e);
		}
		LOGGER.info("UserActivityService is shutdown");
	}
	
	public void createUserActivityLogin(UserDataLoginVO loginVO) {
		if (loginVO == null) return;
		ActivityFieldVO fieldVO = new ActivityFieldVO(ACTION_LOGIN, 
				MODULE_SECURITY, "Login", TABLE_USER_DATA, "" + loginVO.getId());
		createUserActivity(loginVO, fieldVO, null, null, null);
	}
	public void createUserActivityLogout(UserDataLoginVO loginVO) {
		if (loginVO == null) return;
		ActivityFieldVO fieldVO = new ActivityFieldVO(ACTION_LOGOUT, 
				MODULE_SECURITY, "Logout", TABLE_USER_DATA, "" + loginVO.getId());
		createUserActivity(loginVO, fieldVO, null, null, null);
	}
	public void createUserActivityChangePass(UserDataLoginVO loginVO) {
		if (loginVO == null) return;
		ActivityFieldVO activity = new ActivityFieldVO(UserActivityService.ACTION_CHANGE_PASS, 
				UserActivityService.MODULE_RESET_PASS, "Change password", 
				UserActivityService.TABLE_USER_DATA, "" + loginVO.getId());
		createUserActivity(loginVO, activity, null, null, null);
	}
	public void createUserActivityResetPass(UserDataLoginVO loginVO, UserData userData) {
		ActivityFieldVO activity = new ActivityFieldVO(UserActivityService.ACTION_RESET_PASS, 
				UserActivityService.MODULE_RESET_PASS, "Reset password for " + userData.getUserCode(), 
				UserActivityService.TABLE_USER_DATA, "" + userData.getId());
		createUserActivity(loginVO, activity, null, null, null);
	}
	public void createUserActivityCreateData(UserDataLoginVO loginVO, String moduleName, 
			String desc, String tableName, int tableId, Object newObject) {
		ActivityFieldVO fieldVO = new ActivityFieldVO(ACTION_CREATE_DATA, 
				moduleName, desc, tableName, "" + tableId);
		String[] listChanged = mapCheckedAttribute.get(tableName);
		createUserActivity(loginVO, fieldVO, newObject, null, listChanged);
	}
	public void createUserActivityUpdateData(UserDataLoginVO loginVO, String moduleName, 
			String desc, String tableName, int tableId, Object newObject, Object oldObject) {
		ActivityFieldVO fieldVO = new ActivityFieldVO(ACTION_UPDATE_DATA, 
				moduleName, desc, tableName, "" + tableId);
		String[] listChanged = mapCheckedAttribute.get(tableName);
		createUserActivity(loginVO, fieldVO, newObject, oldObject, listChanged);
	}
	public void createUserActivityMaker(UserDataLoginVO loginVO, DistributionHeader header) {
		ActivityFieldVO activity = new ActivityFieldVO(UserActivityService.ACTION_MAKE_DIST, 
				UserActivityService.MODULE_MAKER, "Make Distribution for file " + header.getFileData(), 
				UserActivityService.TABLE_DISTRIBUTION, header.getSysLogNo());
		createUserActivity(loginVO, activity, null, null, null);
	}
	public void createUserActivityChecker(UserDataLoginVO loginVO, DistributionHeader header, boolean approve) {
		String desc = (approve?"":"Reject ");
		desc = desc + "Check Distribution for file " + header.getFileData(); 
		ActivityFieldVO activity = new ActivityFieldVO(UserActivityService.ACTION_CHECK_DIST, 
				UserActivityService.MODULE_CHECKER, desc, 
				UserActivityService.TABLE_DISTRIBUTION, header.getSysLogNo());
		createUserActivity(loginVO, activity, null, null, null);
	}
	public void createUserActivityCallback(UserDataLoginVO loginVO, DistributionHeader header, boolean approve) {
		String desc = (approve?"":"Reject ");
		desc = desc + "Callback Distribution for file " + header.getFileData(); 
		ActivityFieldVO activity = new ActivityFieldVO(UserActivityService.ACTION_CALLBACK_DIST, 
				UserActivityService.MODULE_CALLBACK, desc, 
				UserActivityService.TABLE_DISTRIBUTION, header.getSysLogNo());
		createUserActivity(loginVO, activity, null, null, null);
	}
	public void createUserActivityApproval(UserDataLoginVO loginVO, DistributionHeader header, boolean approve) {
		String desc = (approve?"":"Reject ");
		desc = desc + "Approval Distribution for file " + header.getFileData(); 
		ActivityFieldVO activity = new ActivityFieldVO(UserActivityService.ACTION_APPROVAL_DIST, 
				UserActivityService.MODULE_APPROVAL, desc, 
				UserActivityService.TABLE_DISTRIBUTION, header.getSysLogNo());
		createUserActivity(loginVO, activity, null, null, null);
	}
	
	private void createUserActivity(UserDataLoginVO loginVO, ActivityFieldVO fieldVO,
			Object newObject, Object oldObject, String[] listAttribute) {
		if (loginVO == null) return;
		
		List<List<String>> listChanged = null;
		if (newObject == null && oldObject == null)
			listChanged = null;
		else 
			listChanged = AttributeCheckerUtil.checkForObject(newObject, oldObject, listAttribute);
		
		UserActivityCreatorAgent agent = new UserActivityCreatorAgent(loginVO, fieldVO, listChanged);
		executor.execute(agent);
	}
	
	public List<UserActivity> findLastUserActivityForUser(int userId) {
		int limit = settingService.getSettingAsInt(SettingService.SETTING_LIMIT_USER_ACTIVITY);
		List<UserActivity> listActivity = userActivityMapper.findLastUserActivityForUser(userId, limit);
		if (listActivity == null)
			return new ArrayList<UserActivity>();
		return listActivity;
	}
	
	public int countUserActivityByParam(UserActivityParamVO paramVO)
	{
		int countUserActivity = userActivityMapper.countUserActivityByParam(paramVO);
		return countUserActivity;
	}
	
	public List<UserActivity> selectUserActivityByParam(UserActivityParamVO paramVO)
	{
		List<UserActivity> listUserActivity = userActivityMapper.selectUserActivityByParam(paramVO);
		if(listUserActivity == null)
		{
			listUserActivity = new ArrayList<UserActivity>();
		}
		return listUserActivity;
	}
	
	public List<UserActivity> selectUserActivityByParamNoPaging(UserActivityParamVO paramVO)
	{
		List<UserActivity> listUserActivity = userActivityMapper.selectUserActivityByParamNoPaging(paramVO);
		if(listUserActivity == null)
		{
			listUserActivity = new ArrayList<UserActivity>();
		}
		return listUserActivity;
	}
	
//	public void generateHistoryActivity(Collection<String> excludes,Object ori,Object current,
//			int userDataId, String userCode, String userName, 
//			String moduleName,String action,String targetTable,int targetId, String ipAddress)
//	{
		/*
		 * added history activity
		 * */
//		try{
//			HashMap<String, Collection<String>> resultCompare =  DifferenceFinder.getDifferences(ori,current,excludes);
//			LOGGER.debug("resultCompare: " + resultCompare.toString());
//			Iterator<String> listField = resultCompare.keySet().iterator();
//			List<UserActivityVO> listChangedValue = new ArrayList<UserActivityVO>();
//			UserActivity userActivity = new UserActivity();
//			userActivity.setAction(action);
//			userActivity.setTargetId(targetId);
//			userActivity.setTargetTable(targetTable);
//			userActivity.setModuleName(moduleName);
//			userActivity.setUserDataId(userDataId);
//			userActivity.setUserCode(userCode);
//			userActivity.setUserName(userName);
//			userActivity.setIpAddress(ipAddress);
//			
//			while (listField.hasNext()) {
//				String field = listField.next();
//				Iterator<String> listValue = resultCompare.get(field).iterator();
//				while (listValue.hasNext()) {
//					String value = listValue.next();
//					String[] splitValue = value.split("(;|,)");
//		)			if(splitValue.length == 2){
//						LOGGER).debug("Field Name : " + field + " Original Value : " + splitValue[0]+ " New Value: " + splitValue[1]);
//						try {
//							UserActivityVO userActivityVO = new Us)erActivityVO();
//							HashMap<String, String> newHash= new HashMap<String, String>();
//							if(splitValue[0].contains("="))
//							{
//								splitValue[0]=splitValue[0].replaceAll("=", ",");
//							}
//							if(splitValue[1].contains("="))
//							{
//								splitValue[1]=splitValue[1].replaceAll("=", ",");
//							}
//							newHash.put(splitValue[0], splitValue[1]);
//							userActivityVO.setField(field);
//							userActivityVO.setOldValue(splitValue[0]);
//							userActivityVO.setNewValue(splitValue[1]);
//							listChangedValue.add(userActivityVO);							
//						} catch (Exception e) {
//							LOGGER.warn("Unable to Create History Activity: " + e.getMessage());
//						}
//					}
//				}// end loop cek detail yang berubah
//			}// end loop cek field apa yang berubah
//			
//			mapper = new ObjectMapper();
//			// faster this way, not default
////			mapper.configure(SerializationConfig.Feature.USE_STATIC_TYPING, true); 
//			try {
//				String detailInfo = mapper.writeValueAsString(listChangedValue);
//				userActivity.setChangedAttribute(detailInfo);
//				createHistoryActivity(userActivity);
//			} catch (JsonGenerationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (JsonMappingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			
//		} catch (DifferenceException e) {
//			LOGGER.warn("DifferenceException " + e.toString());
//		}		
//	}
	
	private class UserActivityCreatorAgent implements Runnable {
		private final UserDataLoginVO loginVO;
		private final ActivityFieldVO fieldVO;
		private final List<List<String>> listChanged;
		
		UserActivityCreatorAgent(UserDataLoginVO loginVO, ActivityFieldVO fieldVO, List<List<String>> listChanged) {
			this.loginVO = loginVO;
			this.fieldVO = fieldVO;
			this.listChanged = listChanged;
		}
		
		@Override
		public void run() {
			try {
				String changedAttribute;
				if (listChanged == null) {
					changedAttribute = "";
				} else {
					try {
						changedAttribute = objectMapper.writeValueAsString(listChanged);
					} catch (Exception e) {
						LOGGER.warn("Error serialized " + listChanged, e);
						changedAttribute = "ERROR: " + e.getMessage();
					}
				}
				UserActivity ua = new UserActivity();
				ua.setUserDataId(loginVO.getId()); // user_data_id int NOT NULL,
				ua.setUserCode(loginVO.getUserCode()); // user_code VARCHAR(16) NOT NULL,
				ua.setUserName(loginVO.getUserName()); // user_name VARCHAR(32) NOT NULL,
				ua.setAction(fieldVO.getAction()); // action Varchar(50) NULL,
				ua.setModuleName(fieldVO.getModuleName()); // module_name Varchar(50) NULL,
				ua.setChangedAttribute(changedAttribute); // changed_attribute Text NULL,
				ua.setDescription(fieldVO.getDescription()); //description Varchar(200) NULL,
				ua.setTargetTable(fieldVO.getTargetTable());  // target_table Varchar(50) NULL,
				ua.setTargetId(fieldVO.getTargetId());  // target_id Varchar(50) NULL,
				ua.setIpAddress(loginVO.getIpAddress()); // ip_address Varchar(50) DEFAULT '0.0.0.0',
				ua.setCreatedOn(timeService.getCurrentTime());  //created_on Datetime NOT NULL,
				LOGGER.debug("create {}", ua);
				int created = userActivityMapper.createUserActivity(ua);
				LOGGER.debug("Created {} {}", created, ua);
			} catch (Exception e) {
				LOGGER.warn("Unable to createUserActivity for " + loginVO, e);
			}
		}
	}

	public void setAgentCount(int agentCount) {
		this.agentCount = agentCount;
	}
}
