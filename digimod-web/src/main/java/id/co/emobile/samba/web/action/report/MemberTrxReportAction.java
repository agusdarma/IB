package id.co.emobile.samba.web.action.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import id.co.emobile.samba.web.action.BaseListAction;
import id.co.emobile.samba.web.data.LinkTableVO;
import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.param.MemberTrxReportParamVO;
import id.co.emobile.samba.web.data.param.ParamPagingVO;
import id.co.emobile.samba.web.entity.HistoryTrading;
import id.co.emobile.samba.web.entity.MasterTradingAccount;
import id.co.emobile.samba.web.helper.WebModules;
import id.co.emobile.samba.web.interceptor.ModuleCheckable;
import id.co.emobile.samba.web.mapper.HistoryTradingMapper;
import id.co.emobile.samba.web.mapper.MasterTradingAccountMapper;
import id.co.emobile.samba.web.service.AppsTimeService;
import id.co.emobile.samba.web.service.UserDataService;

public class MemberTrxReportAction extends BaseListAction implements ModuleCheckable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MemberTrxReportAction.class);

	@Autowired
	private AppsTimeService timeService;

	@Autowired
	private HistoryTradingMapper historyTradingMapper;

	@Autowired
	private UserDataService userDataService;

	@Autowired
	private MasterTradingAccountMapper masterTradingAccountMapper;

	private List<HistoryTrading> listHistoryTrading;

	private MemberTrxReportParamVO paramVO;

	// for reporting
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
	public String execute() {
		Date current = timeService.getCurrentTime();
		getLogger().debug("execute with {}", current);
		paramVO = (MemberTrxReportParamVO) getParamVO();
		paramVO.setStartDate(current);
		paramVO.setEndDate(current);

		return SEARCH;
	}

	public String processSearch() {
		try {			
			getLogger().debug("startDate: {}", paramVO.getStartDate());
			getLogger().debug("endDate: {}", paramVO.getEndDate());
			getLogger().debug("userDataId: {}", paramVO.getUserDataId());
			listHistoryTrading = historyTradingMapper.selectHistoryTradingByParamNoPaging(paramVO);

		} catch (Exception e) {
			getLogger().warn("Exception in processSearch " + paramVO, e);
		}
		return SEARCH;
	}

	public String exportReport() {
		LOG.debug("processSearch with exportType {}", exportType);
		listHistoryTrading = historyTradingMapper.selectHistoryTradingByParamNoPaging(paramVO);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(timeService.getCurrentTime());
		documentName = "member_transaction-" + today;

		LOG.info("exporting userActivity with param:" + paramVO + ", with exportType : " + exportType);
		return "EXPORT";
	}

	@Override
	public int getMenuId() {
		return WebModules.MODULE_REPORT_MEMBER_TRX;
	}

	public List<HistoryTrading> getListHistoryTrading() {
		if (listHistoryTrading == null)
			listHistoryTrading = new ArrayList<HistoryTrading>();
		return listHistoryTrading;
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
		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
		Map<String, String> mapUser = new LinkedHashMap<String, String>();
		mapUser.put("0", "Semua Member");
		List<MasterTradingAccount> listMasterTradingAccounts = masterTradingAccountMapper
				.findAllMasterTradingAccountByIbUser(loginVO.getUserCode());
		for (MasterTradingAccount masterTradingAccount : listMasterTradingAccounts) {
			mapUser.put("" + masterTradingAccount.getMyfxbookId(),
					"[" + masterTradingAccount.getTradingAccountNo() + "]" + masterTradingAccount.getName());
		}
		return mapUser;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setStartDate(String date) {
		paramVO = (MemberTrxReportParamVO) getParamVO();
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
		paramVO = (MemberTrxReportParamVO) getParamVO();
		if (paramVO.getStartDate() == null)
			return "";
		return sdf.format(paramVO.getStartDate());
	}

	public void setEndDate(String date) {
		paramVO = (MemberTrxReportParamVO) getParamVO();
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
		paramVO = (MemberTrxReportParamVO) getParamVO();
		if (paramVO.getEndDate() == null)
			return "";
		return sdf.format(paramVO.getEndDate());
	}

	/**************************************
	 * ESSENTIAL FOR SEARCH
	 *******************************************/
	private String resultSearchJson;

	public void makeTableContent() {
		prepareParamVO(new MemberTrxReportParamVO(), WEB_PARAM_KEY + WebModules.MODULE_REPORT_MEMBER_TRX,
				"ua.updated_on", WebConstants.SORT_ORDER_ASC);
		String[] arrayHeader = { getText("l.recordNo"), getText("l.time"), getText("l.userCode"), getText("l.activity"),
				getText("l.moduleName"), getText("l.ipAddress") };
		String[] arrayBody = { "rowNum", "updatedOnStr", "userCode", "action", "moduleName", "ipAddress" };
		String[] arrayDbVariable = { "", "ua.updated_on", "ua.user_code", "ua.action", "ua.module_name",
				"ua.ip_address" };

		List<LinkTableVO> listLinkTable = new ArrayList<LinkTableVO>();

		MemberTrxReportParamVO memberTrxReportParamVO = (MemberTrxReportParamVO) paramVO;
		LOG.info("Search cms user activity by param : " + memberTrxReportParamVO);
//		int totalRow = userActivityService.countUserActivityByParam(memberTrxReportParamVO);		
//		listUserActivity = userActivityService.selectUserActivityByParam(memberTrxReportParamVO);
//		Locale language =  (Locale) session.get(WEB_LOCALE_KEY);
//		try {
//			String bodyContent = objectMapper.writeValueAsString(listUserActivity);
//			resultSearchJson=webSearchResultService.composeSearchResultWithExport(getText("l.listUserActivityCms"), arrayHeader, arrayBody,
//					arrayDbVariable, bodyContent, getCurrentPage(),
//					totalRow, listLinkTable, language, listUserActivity.size(), paramVO);
//		} catch (Exception e) {
//			LOG.warn("Exception when serializing " + listUserActivity, e);
//		}

	}

	@Override
	public ParamPagingVO getParamVO() {
		if (paramVO == null)
			paramVO = new MemberTrxReportParamVO();
		return paramVO;
	}

	public InputStream getWsr() {
		return new ByteArrayInputStream(resultSearchJson.toString().getBytes());
	}

	/**************************************
	 * ESSENTIAL FOR SEARCH
	 *******************************************/

}
