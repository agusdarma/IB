package id.co.emobile.samba.web.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.WebResultVO;
import id.co.emobile.samba.web.data.param.SystemSettingParamVO;
import id.co.emobile.samba.web.entity.SystemSetting;
import id.co.emobile.samba.web.mapper.SystemSettingMapper;

@Service
public class SettingService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SettingService.class);

	public static final String TYPE_STRING 		= "STRING";
	public static final String TYPE_INT 		= "INT";
	public static final String TYPE_DATE 		= "DATE";
	public static final String TYPE_DATETIME 	= "DATETIME";
	public static final String TYPE_DOUBLE 		= "DOUBLE";
	public static final String TYPE_BOOLEAN 	= "BOOLEAN";
	
	public static final int SETTING_TRANSACTION_TIMEOUT		= 20; // int, Transaction timeout in ms
	public static final int SETTING_BTI_TIMEOUT				= 21; // int, BTI timeout in ms
	// int, max wrong pin allowed
	public static final int SETTING_MAX_INVALID_LOGIN = 51;
	public static final int SETTING_PASSWORD_MIN_LENGTH = 52;
	public static final int SETTING_EXPIRY_DAY_PASSWORD = 53;
	public static final int SETTING_DOUBLE_LOGIN_TIME = 54;
	public static final int SETTING_LIMIT_USER_ACTIVITY	= 55;
	
	public static final int SETTING_OTP_LENGTH	= 101;
	public static final int SETTING_SMS_GATEWAY_URL = 102;
	public static final int SETTING_SMS_GATEWAY_ENC_KEY = 103;	
	public static final int SETTING_USSD_MMBS_URL = 104;
	public static final int SETTING_USSD_MMBS_ENC_KEY = 105;	
	public static final int SETTING_USSD_BALANCE_SYNTAX = 106;
	public static final int SETTING_USSD_TRANSFER_SYNTAX = 107;
	public static final int SETTING_CHECK_BALANCE_INTERVAL = 108;
	
	public static final int SETTING_COLLEGA_URL		= 110;
	public static final int SETTING_LOCAL_IP_ADDRESS	= 111;
	public static final int SETTING_COLLEGA_AUTH_KEY	= 112;
	public static final int SETTING_COLLEGA_PARAM_DATA	= 113;
	
	public static final String COLLEGA_PARAM_USERGTW		= "usergtw";
	public static final String COLLEGA_PARAM_CHANNEL		= "channel";
	public static final String COLLEGA_PARAM_CORPID			= "corpid";
	public static final String COLLEGA_PARAM_PRODID			= "prodid";
	public static final String COLLEGA_PARAM_BRANCH			= "branch";
	public static final String COLLEGA_PARAM_DSAC_PAJAK			= "dsacPajak";
	public static final String COLLEGA_PARAM_DSAC_PAJAK_NAME			= "dsacPajakName";
	
	
	private static final int[][] LIST_SETTING_DEFAULT_INT = {
			new int[] { SETTING_MAX_INVALID_LOGIN, 3 },
			new int[] { SETTING_PASSWORD_MIN_LENGTH, 6 },
			new int[] { SETTING_EXPIRY_DAY_PASSWORD, 180 },
			new int[] { SETTING_DOUBLE_LOGIN_TIME, 10 },
			new int[] { SETTING_TRANSACTION_TIMEOUT, 50000 },			
			new int[] { SETTING_BTI_TIMEOUT, 5000 },
			new int[] { SETTING_OTP_LENGTH, 6 },
	};
	
	private ObjectMapper objectMapper = new ObjectMapper();
	private TypeReference<HashMap<String, String>> typeRefMap = new TypeReference<HashMap<String, String>>() {};
	
	@Autowired
	private SystemSettingMapper settingMapper;

	@Autowired
	private BizMessageService messageService;

	private List<SystemSetting> settings;

	private SimpleDateFormat sdfShort = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat sdfLong = new SimpleDateFormat("yyyyMMddHHmmss");

	private int getDefaultInt(int settingId) {
		for (int i = 0; i < LIST_SETTING_DEFAULT_INT.length; i++) {
			int[] data = LIST_SETTING_DEFAULT_INT[i];
			if (data[0] == settingId)
				return data[1];
		}
		return 0;
	}

	private String getDefaultString(int settingId) {
		return "";
	}

	private SystemSetting getSetting(int id) throws SambaWebException {
		if (settings == null) {
			reload();
		}
		for (SystemSetting setting : settings) {
			if (setting.getId() == id)
				return setting;
		}
		getLogger().warn("No Data for Setting for Id: {}", id);
		throw new SambaWebException(
				SambaWebException.NE_DATA_NOT_FOUND,
				new String[] { messageService.getMessageFor("l.setting"),
						"" + id });
	}

	// need to synchronize here to provide thread safe operation
	// NOT TESTED YET
	public synchronized void reload() throws SambaWebException {
		settings = settingMapper.findSystemSettingAll();
		if (settings == null) {
			getLogger().warn("No Data for Setting");
			throw new SambaWebException(
					SambaWebException.NE_SETTING_EMPTY);
		}
		if (getLogger().isInfoEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append("Setting has been loaded [");
			int i = 0;
			for (SystemSetting setting : settings) {
				sb.append(setting.getId() + ":" + setting.getSettingName()
						+ "=" + setting.getSettingValue());
				if (++i < settings.size())
					sb.append(",");
			}
			sb.append("]");
			getLogger().info(sb.toString());
		}
	}

	public boolean getSettingAsBoolean(int id) throws SambaWebException {
		SystemSetting setting = getSetting(id);
		if (setting.getValueType().equalsIgnoreCase(TYPE_BOOLEAN))
			return "TRUE".equalsIgnoreCase(setting.getSettingValue())
					|| "1".equals(setting.getSettingValue())
					|| "T".equalsIgnoreCase(setting.getSettingValue());
		getLogger().debug("Invalid setting type, setting: {}", setting);
		throw new SambaWebException(
				SambaWebException.NE_SETTING_INVALID_TYPE, new String[] {
						"" + setting.getId(), setting.getValueType() });
	}

	public Date getSettingAsDate(int id) throws SambaWebException {
		SystemSetting setting = getSetting(id);
		try {
			if (setting.getValueType().equalsIgnoreCase(TYPE_DATE)) {
				return sdfShort.parse(setting.getSettingValue());
			} else if (setting.getValueType().equalsIgnoreCase(TYPE_DATETIME)) {
				return sdfLong.parse(setting.getSettingValue());
			}
		} catch (NumberFormatException pe) {
			getLogger().warn("Unable to parse to date/time, setting: {}",
					setting);
			throw new SambaWebException(
					SambaWebException.NE_SETTING_INVALID_TYPE,
					new String[] { "" + setting.getId(), setting.getValueType() });
		} catch (Exception e) {
			getLogger()
					.warn("Unknown error in getSettingAsDate, setting: {}, exception: {}",
							setting, e.getMessage());
			throw new SambaWebException(e);
		}
		getLogger().debug("Invalid setting type, setting: {}", setting);
		throw new SambaWebException(
				SambaWebException.NE_SETTING_INVALID_TYPE, new String[] {
						"" + setting.getId(), setting.getValueType() });
	}

	public double getSettingAsDouble(int id) throws SambaWebException {
		SystemSetting setting = getSetting(id);
		try {
			if (setting.getValueType().equalsIgnoreCase(TYPE_DOUBLE)) {
				return Double.parseDouble(setting.getSettingValue());
			}
		} catch (NumberFormatException pe) {
			getLogger().warn("Unable to parse to double, setting: {}", setting);
			throw new SambaWebException(
					SambaWebException.NE_SETTING_INVALID_TYPE,
					new String[] { "" + setting.getId(), setting.getValueType() });
		} catch (Exception e) {
			getLogger()
					.warn("Unknown error in getSettingAsDouble, setting: {}, exception: {}",
							setting, e.getMessage());
			throw new SambaWebException(e);
		}
		getLogger().debug("Invalid setting type, setting: {}", setting);
		throw new SambaWebException(
				SambaWebException.NE_SETTING_INVALID_TYPE, new String[] {
						"" + setting.getId(), setting.getValueType() });
	}

	public int getSettingAsInt(int id) {
		SystemSetting setting=null;
		try {
			setting = getSetting(id);
			if (setting.getValueType().equalsIgnoreCase(TYPE_INT)) {
				return Integer.parseInt(setting.getSettingValue());
			}
		} catch (SambaWebException gwe) {
			return getDefaultInt(id);
		} catch (NumberFormatException pe) {
			getLogger().warn("Unable to parse {} to int, setting: {}", setting, id);
//			throw new SambaWebException(SambaWebException.NE_SETTING_INVALID_TYPE, 
//					new String[] { "" + setting.getId(), setting.getValueType() });
		} catch (Exception e) {
			getLogger().warn("Unknown error in getSettingAsInt, setting: {}, exception: {}", setting, e.getMessage());
		}
		return getDefaultInt(id);
	}

	public String getSettingAsString(int id) {
		SystemSetting setting = null;
		try {
			setting = getSetting(id);
			if (setting.getValueType().equalsIgnoreCase(TYPE_STRING))
				return setting.getSettingValue();
			getLogger().debug("Invalid setting type, setting: {}", setting);
		} catch (SambaWebException jwe) {
		} catch (Exception e) {
			getLogger()
					.warn("Unknown error in getSettingAsInt, setting: {}, exception: {}",
							setting, e.getMessage());
		}
		return getDefaultString(id);
	}

	public Map<String, String> getSettingAsJson(int id) {
		String data = getSettingAsString(id);
		if (StringUtils.isEmpty(data)) {
			return new HashMap<String, String>();
		}
		if (data.startsWith("{") && data.endsWith("}")) {
			try {
				Map<String, String> map = objectMapper.readValue(data, typeRefMap);
				return map;
			} catch (Exception e) {
				LOGGER.warn("Unable to parse " + data + ", exception: " + e.getMessage());
			}
		} 
		// default behaviour
		Map<String, String> map = new HashMap<String, String>();
		map.put("" + id, data);
		return map;
	}
	
	public List<SystemSetting> getSettings() throws SambaWebException {
		if (settings == null)
			reload();
		return settings;
	}

	@Transactional(rollbackFor = Exception.class)
	public WebResultVO update(SystemSetting setting, UserDataLoginVO loginVO, Locale language)
			throws SambaWebException {
		WebResultVO wrv = new WebResultVO();
		getLogger().debug("saveSetting: {}", setting);
		setting.setUpdatedBy(loginVO.getId());
		if(StringUtils.isEmpty(setting.getSettingValue()))
		{
			LOGGER.warn("Setting value is empty!");
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.settingValue")});
		}
		// validate setting
		if (TYPE_STRING.equals(setting.getValueType())) {
			// nothing todo, accept all value
		} else if (TYPE_BOOLEAN.equals(setting.getValueType())) {
			String[] listS = new String[] { "TRUE", "T", "1", "FALSE", "F", "0" };
			boolean found = false;
			for (String s : listS) {
				if (s.equalsIgnoreCase(setting.getSettingValue())) {
					found = true;
					break;
				}
			}
			if (!found){
				LOGGER.warn("Not Found for Setting Id : {}, Setting Value Type: {}" + setting.getId(), setting.getValueType());
				throw new SambaWebException(
						SambaWebException.NE_SETTING_INVALID_TYPE,
						new String[] { "" + setting.getId(),
								setting.getValueType() });
			}
		} else if (TYPE_INT.equals(setting.getValueType())
				|| TYPE_DOUBLE.equals(setting.getValueType())
				|| TYPE_DATE.equals(setting.getValueType())
				|| TYPE_DATETIME.equals(setting.getValueType())) {
			try {
				if (TYPE_INT.equals(setting.getValueType()))
					Integer.parseInt(setting.getSettingValue());
				else if (TYPE_DOUBLE.equals(setting.getValueType()))
					Double.parseDouble(setting.getSettingValue());
				else if (TYPE_DATE.equals(setting.getValueType()))
					sdfShort.parse(setting.getSettingValue());
				else if (TYPE_DATETIME.equals(setting.getValueType()))
					sdfLong.parse(setting.getSettingValue());
			} catch (NumberFormatException pe) {
				getLogger().warn("Unable to parse setting: {}, NumberFormatException: {}", setting, pe.getMessage());
				throw new SambaWebException(
						SambaWebException.NE_SETTING_INVALID_TYPE,
						new String[] { "" + setting.getId(),
								setting.getValueType() });
			} catch (Exception e) {
				getLogger()
						.warn("Unknown error in getSettingAsXXX, setting: {}, exception: {}",
								setting, e.getMessage());
				throw new SambaWebException(e);
			}
		} else {
			LOGGER.warn("Unknown Type Setting for Setting Id : {}, Setting Value Type: {}" + setting.getId(), setting.getValueType());
			throw new SambaWebException(
					SambaWebException.NE_SETTING_INVALID_TYPE,
					new String[] { "" + setting.getId(), setting.getValueType() });
		}

		// save data
		try {
			settingMapper.updateSystemSetting(setting);
			
			wrv.setRc(WebConstants.RESULT_SUCCESS);
			wrv.setMessage(messageService.getMessageFor("rm.generalMessage",new String[] {messageService.getMessageFor("l.systemSetting", language), messageService.getMessageFor("l.updated", language)}, language));
			wrv.setType(WebConstants.TYPE_UPDATE);
			wrv.setPath(WebConstants.PATH_UPDATE_SYSTEM_SETTING);
			
			Collection<String> excludes = new ArrayList<String>();
			excludes.add("updatedBy");
			excludes.add("updatedOn");
			reload();
		} catch (Exception e) {
			getLogger().error("Exception: " + e, e);
			throw new SambaWebException(e);
		}
		
		return wrv;
	}

//	public SystemSetting findById(int settingId) throws MayapadaWebException {
//		try {
//			SystemSetting SystemSetting = settingMapper.findSystemSettingById(settingId);
//			if (SystemSetting == null)
//				throw new MayapadaWebException(
//						MayapadaWebException.NE_DATA_NOT_FOUND, new String[] {
//								"Setting", "" + settingId });
//			return SystemSetting;
//		} catch (MayapadaWebException jme) {
//			throw jme;
//		} catch (Exception e) {
//			getLogger().warn("Exception: " + e, e);
//			throw new MayapadaWebException(e);
//		}
//	}

	protected Logger getLogger() {
		return LOGGER;
	}

	public List<SystemSetting> findByParam(SystemSettingParamVO systemParamVO) {
		try {
			List<SystemSetting> listSetting = settingMapper.findSystemSettingByParam(systemParamVO);
			return listSetting;
		} catch (Exception e) {
			LOGGER.error("ERROR: " + e, e);
			return new ArrayList<SystemSetting>();
		}
	}

	public int countByParam(SystemSettingParamVO systemParamVO) {
		try {
			int record = settingMapper.countSystemSettingByParam(systemParamVO);
			return record;
		} catch (Exception e) {
			LOGGER.error("ERROR: " + e, e);
			return 0;
		}
	}

	public SystemSetting findSystemSettingBySettingName(String settingName) {
		return settingMapper.findSystemSettingBySettingName(settingName);
	}

	public SystemSetting getSettingById(int id) throws SambaWebException {
		if (settings == null)
			reload();
		for (SystemSetting systemSetting : settings) {
			if(systemSetting.getId() == id)
				return systemSetting;
		}
		return null;
	}
}
