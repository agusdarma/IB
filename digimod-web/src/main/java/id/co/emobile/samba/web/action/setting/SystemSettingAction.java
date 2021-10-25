package id.co.emobile.samba.web.action.setting;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;
import com.opensymphony.xwork2.interceptor.ParameterNameAware;

import id.co.emobile.samba.web.action.BaseListAction;
import id.co.emobile.samba.web.data.LinkTableVO;
import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.WebResultVO;
import id.co.emobile.samba.web.data.param.ParamPagingVO;
import id.co.emobile.samba.web.data.param.SystemSettingParamVO;
import id.co.emobile.samba.web.entity.SystemSetting;
import id.co.emobile.samba.web.helper.WebModules;
import id.co.emobile.samba.web.interceptor.ModuleCheckable;
import id.co.emobile.samba.web.service.SambaWebException;
import id.co.emobile.samba.web.service.SettingService;

public class SystemSettingAction extends BaseListAction implements ModuleCheckable, ParameterNameAware {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(SystemSettingAction.class);

	private int settingId;
	private String message;
	private SystemSetting systemSetting;
	private List<SystemSetting> listSystemSetting;

	private WebResultVO wrv;
	private String json;

	private Map<String, String> settingMap;

	@Autowired
	private SettingService settingService;

	@Override
	public String execute(){
		getLogger().debug("Processing -> execute()");
		setMessage(getFlashMessage());
		session.remove(WEB_CONTENT_KEY);
		return SEARCH;
	}

	public String gotoSearch(){
		return SEARCH;
	}

	public String list(){
		getLogger().debug("Processing -> list()");
		setMessage(getFlashMessage());
		return SEARCH;
	}

	public String reload() {
		wrv = new WebResultVO();
		
		try {
			settingService.reload();
			wrv.setRc(0);
			wrv.setMessage("System Setting sudah di reload");
		} catch (Exception e) {
			getLogger().warn("Exception when reloading system setting", e);
			wrv.setRc(99);
			wrv.setMessage("Gagal melakukan reload System Setting");
		}
		try {
			json = objectMapper.writeValueAsString(wrv);
		} catch (Exception e) {
			LOG.warn("Exception in serializing " + wrv, e);
		}
		return "inputJson";
	}
	
	public String detail() {
		getLogger().debug("Processing -> edit()");
		try {
			systemSetting = settingService.getSettingById(settingId);
			if (systemSetting == null) {
				getLogger().warn("Invalid system setting with id {}", settingId);
				return execute();
			}
			session.put(WEB_CONTENT_KEY, systemSetting);

			if (isSettingInJson()) {
				TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String,String>>() {};
				settingMap = objectMapper.readValue(getSystemSetting().getSettingValue(), typeRef);
			}
			return INPUT;
		} catch (Exception e) {
			handleException(e);
		}
		return execute();
	}

	public String processInput(){
		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
		Locale language=(Locale) session.get(WEB_LOCALE_KEY);
		try {
			LOG.info("processing update system setting with param : " +systemSetting+ ", and language: " + language);
			if (isSettingInJson()) {
				try {
					String map = objectMapper.writeValueAsString(settingMap);
					systemSetting.setSettingValue(map);
					LOG.debug("Updated value JSON: {}", systemSetting);
				} catch (Exception e) {
					LOG.warn("Exception in serializing " + wrv, e);
				}
			}
			wrv = settingService.update(systemSetting, loginVO, language);
			if(wrv.getType() == WebConstants.TYPE_UPDATE)
			{
				setFlashMessage(wrv.getMessage());
			}
		} catch (SambaWebException gwe) {
			wrv = handleJsonException(gwe);
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

	public String processList(){
		makeTableContent();
		return "searchJson";
	}

	public InputStream getWrv() {
		return new ByteArrayInputStream(json.toString().getBytes());
	}

	@Override
	protected Logger getLogger() {
		return LOG;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<SystemSetting> getListSystemSetting() {
		return listSystemSetting;
	}

	public void setListSystemSetting(List<SystemSetting> listSystemSetting) {
		this.listSystemSetting = listSystemSetting;
	}

	@Override
	public int getMenuId() {
		return WebModules.MODULE_SYSTEM_SUPPORT_SYSTEM_SETTING;
	}

	//paging start
	public int getSettingId() {
		return settingId;
	}
	public void setSettingId(int settingId) {
		this.settingId = settingId;
	}

	public void setWrv(WebResultVO wrv) {
		this.wrv = wrv;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public SystemSetting getSystemSetting() {
		if (systemSetting == null) {
			Object obj = session.get(WEB_CONTENT_KEY);
			if (obj != null && obj instanceof SystemSetting) {
				systemSetting = (SystemSetting) obj;
			}
			if (systemSetting == null)
				systemSetting = new SystemSetting();
		}
		return systemSetting;
	}

	public boolean isSettingInJson() {
		if (getSystemSetting().getSettingValue().startsWith("{\"") &&
				getSystemSetting().getSettingValue().endsWith("\"}")) {
			return true;
		} else {
			return false;
		}
	}
	
	public List<SystemSetting> getListSystemSettingAll() {
		SystemSettingParamVO settingParamVO = new SystemSettingParamVO();
		settingParamVO.setRowStart(1);
		settingParamVO.setRowEnd(100);
		settingParamVO.setRowPerPage(100);
		settingParamVO.setSortVariable("id");
		settingParamVO.setSortOrder(WebConstants.SORT_ORDER_ASC);
		
		List<SystemSetting> listData = settingService.findByParam(settingParamVO);
		LOG.debug("ListData size: {}", listData.size());
		return listData;
	}

	public Map<String, String> getSettingMap() {
		return settingMap;
	}
	public void setSettingMap(Map<String, String> settingMap) {
		this.settingMap = settingMap;
	}

	/**************************************   ESSENTIAL FOR SEARCH  *******************************************/
	private String resultSearchJson;

	public void makeTableContent()
	{
		prepareParamVO(new SystemSettingParamVO(), WEB_PARAM_KEY + WebModules.MODULE_SYSTEM_SUPPORT_SYSTEM_SETTING,
				"setting_name", WebConstants.SORT_ORDER_ASC);
		String[] arrayHeader={getText("l.recordNo"), getText("l.settingName"), getText("l.settingDesc"), getText("l.valueType"),
				getText("l.settingValue"), getText("l.updatedOn")};
		String[] arrayBody={"rowNum", "settingName", "settingDesc", "valueType", "settingValue", "updatedOn"};
		String[] arrayDbVariable={"", "setting_name", "setting_desc", "setting_value", "value_type",
				"updated_on", "updated_by"};
		List<LinkTableVO> listLinkTable=new ArrayList<LinkTableVO>();
		listLinkTable.add(new LinkTableVO("SystemSetting!detail.web", "settingName", new String[]{"settingId"}, new String[]{"id"}));

		SystemSettingParamVO systemSettingParamVO = (SystemSettingParamVO) paramVO;
		int totalRow = settingService.countByParam(systemSettingParamVO);
		try
		{
			listSystemSetting = settingService.findByParam(systemSettingParamVO);
		} catch (Exception e) {

		}
		Locale language =  (Locale) session.get(WEB_LOCALE_KEY);
		try {
			String bodyContent = objectMapper.writeValueAsString(listSystemSetting);
			resultSearchJson=webSearchResultService.composeSearchResult(getText("l.listSystemSetting"), arrayHeader, arrayBody,
					arrayDbVariable, bodyContent, getCurrentPage(),
					totalRow, listLinkTable, language, listSystemSetting.size(), paramVO);
		} catch (Exception e) {
			LOG.warn("Exception when serializing " + listSystemSetting, e);
		}
		

	}

	@Override
	public ParamPagingVO getParamVO() {
		if (paramVO == null)
			paramVO = new SystemSettingParamVO();
		return paramVO;
	}

	public InputStream getWsr() {
		return new ByteArrayInputStream(resultSearchJson.toString().getBytes());
	}

	@Override
	public boolean acceptableParameterName(String arg0) {
		LOG.debug("ParameterName: {}", arg0);
		return true;
	}

	/**************************************   ESSENTIAL FOR SEARCH  *******************************************/

}
