package id.co.emobile.samba.web.action.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import id.co.emobile.samba.web.action.BaseListAction;
import id.co.emobile.samba.web.data.LinkTableVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.param.ParamPagingVO;
import id.co.emobile.samba.web.data.param.UserActivityParamVO;
import id.co.emobile.samba.web.entity.UserActivity;
import id.co.emobile.samba.web.entity.UserData;
import id.co.emobile.samba.web.helper.WebModules;
import id.co.emobile.samba.web.interceptor.ModuleCheckable;
import id.co.emobile.samba.web.service.AppsTimeService;
import id.co.emobile.samba.web.service.UserActivityService;
import id.co.emobile.samba.web.service.UserDataService;

public class UserActivityReportAction extends BaseListAction implements ModuleCheckable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(UserActivityReportAction.class);

	@Autowired
	private AppsTimeService timeService;
	
	@Autowired
	private UserActivityService userActivityService;
	
	@Autowired
	private UserDataService userDataService;

	private List<UserActivity> listUserActivity;
	
	private UserActivityParamVO paramVO;

	//for reporting
	private String exportType;
	
	// for JASPER
	private Map<String, String> reportParameters;
	private String documentName;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	protected Logger getLogger() {
		return LOG;
	}

	@Override
	public String execute(){
		Date current = timeService.getCurrentTime();
		getLogger().debug("execute with {}", current);
		paramVO = (UserActivityParamVO) getParamVO();
		paramVO.setStartDate(current);
		paramVO.setEndDate(current);
		
		return SEARCH;
	}
	
	public String processSearch() {
		try {
			getLogger().debug("processSearch: {}", paramVO);
			listUserActivity = userActivityService.selectUserActivityByParamNoPaging(paramVO);
			
		} catch (Exception e) {
			getLogger().warn("Exception in processSearch " + paramVO, e);
		}
		return SEARCH;
	}
	
	public String exportReport(){
		LOG.debug("processSearch with exportType {}", exportType);
		listUserActivity = userActivityService.selectUserActivityByParamNoPaging(paramVO);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(timeService.getCurrentTime());
		documentName = "user_activity-"+today;	 	
		
		LOG.info("exporting userActivity with param:" +  paramVO +", with exportType : " + exportType);
		return "EXPORT";
	}
	
	@Override
	public int getMenuId() {
		return WebModules.MODULE_REPORT_USER_ACTIVITY;
	}
	
	public List<UserActivity> getListUserActivity() {
		if (listUserActivity == null)
			listUserActivity = new ArrayList<UserActivity>();
		return listUserActivity;
	}

	public Map<String, String> getReportParameters() {
		if (reportParameters == null)
			return new HashMap<String, String>();
		return reportParameters;
	}

	public String getExportType() {
		return exportType;
	}
	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public Map<String, String> getListUserData() {
		Map<String, String> mapUser = new LinkedHashMap<String, String>();
		mapUser.put("0", "Semua User");
		List<UserData> listUser = userDataService.findUserDataActive();
		for (UserData userData: listUser) {
			mapUser.put("" + userData.getId(), "[" + userData.getUserCode() + "]" + userData.getUserName());
		}
		return mapUser;
	}
	
	public String getDocumentName() {
		return documentName;
	}
	
	public void setStartDate(String date) {
		paramVO = (UserActivityParamVO) getParamVO();
		if (StringUtils.isEmpty(date)) {
			paramVO.setStartDate(null);
		} else {
			try {
				paramVO.setStartDate(sdf.parse(date));
			} catch (Exception e) {
				LOG.warn("Unable convert " + date + " to date: " + e.getMessage());
			}
		}
	}
	public String getStartDate() {
		paramVO = (UserActivityParamVO) getParamVO();
		if (paramVO.getStartDate() == null) 
			return "";
		return sdf.format(paramVO.getStartDate());
	}
	
	public void setEndDate(String date) {
		paramVO = (UserActivityParamVO) getParamVO();
		if (StringUtils.isEmpty(date)) {
			paramVO.setEndDate(null);
		} else {
			try {
				paramVO.setEndDate(sdf.parse(date));
			} catch (Exception e) {
				LOG.warn("Unable convert " + date + " to date: " + e.getMessage());
			}
		}
	}
	public String getEndDate() {
		paramVO = (UserActivityParamVO) getParamVO();
		if (paramVO.getEndDate() == null) 
			return "";
		return sdf.format(paramVO.getEndDate());
	}

	/**************************************   ESSENTIAL FOR SEARCH  *******************************************/
	private String resultSearchJson;

	public void makeTableContent()
	{		
		prepareParamVO(new UserActivityParamVO(), WEB_PARAM_KEY + WebModules.MODULE_REPORT_USER_ACTIVITY,
				"ua.updated_on", WebConstants.SORT_ORDER_ASC);		
		String[] arrayHeader={getText("l.recordNo"), getText("l.time"), getText("l.userCode"), getText("l.activity"), 
				getText("l.moduleName"), getText("l.ipAddress")};
		String[] arrayBody={"rowNum", "updatedOnStr", "userCode", "action", "moduleName", "ipAddress"};
		String[] arrayDbVariable={"", "ua.updated_on", "ua.user_code","ua.action", 
				"ua.module_name", "ua.ip_address"};
		
		List<LinkTableVO> listLinkTable=new ArrayList<LinkTableVO>();

		UserActivityParamVO userActivityParamVO = (UserActivityParamVO) paramVO;
		LOG.info("Search cms user activity by param : " + userActivityParamVO);
		int totalRow = userActivityService.countUserActivityByParam(userActivityParamVO);		
		listUserActivity = userActivityService.selectUserActivityByParam(userActivityParamVO);
		Locale language =  (Locale) session.get(WEB_LOCALE_KEY);
		try {
			String bodyContent = objectMapper.writeValueAsString(listUserActivity);
			resultSearchJson=webSearchResultService.composeSearchResultWithExport(getText("l.listUserActivityCms"), arrayHeader, arrayBody,
					arrayDbVariable, bodyContent, getCurrentPage(),
					totalRow, listLinkTable, language, listUserActivity.size(), paramVO);
		} catch (Exception e) {
			LOG.warn("Exception when serializing " + listUserActivity, e);
		}
		
	}


	@Override
	public ParamPagingVO getParamVO() {
		if (paramVO == null)
			paramVO = new UserActivityParamVO();
		return paramVO;
	}

	public InputStream getWsr() {
		return new ByteArrayInputStream(resultSearchJson.toString().getBytes());
	}

	/**************************************   ESSENTIAL FOR SEARCH  *******************************************/

}
