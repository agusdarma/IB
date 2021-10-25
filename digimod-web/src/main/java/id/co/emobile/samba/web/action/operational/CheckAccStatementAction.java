//package id.co.emobile.samba.web.action.operational;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import id.co.emobile.samba.web.action.BaseAction;
//import id.co.emobile.samba.web.data.AccStatementParamVO;
//import id.co.emobile.samba.web.data.AccStatementVO;
//import id.co.emobile.samba.web.data.SourceAccountVO;
//import id.co.emobile.samba.web.data.UserDataLoginVO;
//import id.co.emobile.samba.web.helper.WebModules;
//import id.co.emobile.samba.web.interceptor.ModuleCheckable;
//import id.co.emobile.samba.web.service.AppsTimeService;
//import id.co.emobile.samba.web.service.SmsSenderService;
//import id.co.emobile.samba.web.service.SourceAccountService;
//
//public class CheckAccStatementAction extends BaseAction implements ModuleCheckable {
//	private static final long serialVersionUID = 1L;
//	private static final Logger LOG = LoggerFactory.getLogger(CheckAccStatementAction.class);
//
//	@Autowired
//	private AppsTimeService timeService;
//	
//	@Autowired
//	private SourceAccountService sourceAccountService;
//	
//	@Autowired
//	private SmsSenderService smsSenderService;
//
//	private AccStatementVO accStatement;
//	
//	private AccStatementParamVO paramVO;
//
//	private String message;
//	
//	//for reporting
//	private String exportType;
//	
//	// for JASPER
//	private Map<String, String> reportParameters;
//	private String documentName;
//	
//	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//	
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//
//	@Override
//	public String execute(){
//		Date current = timeService.getCurrentTime();
//		getLogger().debug("execute with {}", current);
//		paramVO = getParamVO();
//		paramVO.setStartDate(current);
//		paramVO.setEndDate(current);
//		
//		return SEARCH;
//	}
//	
//	public String processSearch() {
//		try {
//			getLogger().debug("processSearch: {}", paramVO);
////			listUserActivity = userActivityService.selectUserActivityByParamNoPaging(paramVO);
//			accStatement = smsSenderService.checkStatementForAccountId(paramVO);
//			if (accStatement == null) {
//				message = "System error. Please contact system administrator";
//			} else if (!"0".equals(accStatement.getStatementRc())) {
//				// only set message if error, because message will be displayed in style error
//				message = accStatement.getMessage();
//			}
//		} catch (Exception e) {
//			getLogger().warn("Exception in processSearch " + paramVO, e);
//		}
//		return SEARCH;
//	}
//	
////	public String exportReport(){
////		LOG.debug("processSearch with exportType {}", exportType);
////		listUserActivity = userActivityService.selectUserActivityByParamNoPaging(paramVO);
////		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
////		String today = sdf.format(timeService.getCurrentTime());
////		documentName = "user_activity-"+today;	 	
////		
////		LOG.info("exporting userActivity with param:" +  paramVO +", with exportType : " + exportType);
////		return "EXPORT";
////	}
//	
//	@Override
//	public int getMenuId() {
//		return WebModules.MODULE_APPS_OPERATIONAL_CHECK_ACC_STATEMNT;
//	}
//	
//	public AccStatementVO getAccStatementVO() {
//		if (accStatement == null)
//			accStatement = new AccStatementVO();
//		return accStatement;
//	}
//
//	public Map<String, String> getReportParameters() {
//		if (reportParameters == null)
//			return new HashMap<String, String>();
//		return reportParameters;
//	}
//
//	public String getExportType() {
//		return exportType;
//	}
//	public void setExportType(String exportType) {
//		this.exportType = exportType;
//	}
//
//	public Map<String, String> getListSourceAcc() {
//		Map<String, String> mapSourceAcc = new LinkedHashMap<String, String>();
//		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);	
//		List<SourceAccountVO> listAcc = sourceAccountService.getListSourceAccountByGroup(loginVO.getGroupId(), false);
//		for (SourceAccountVO acc: listAcc) {
//			mapSourceAcc.put("" + acc.getId(), acc.getSracNumber() + " - " + acc.getSracName());
//		}
//		return mapSourceAcc;
//	}
//	
//	public String getDocumentName() {
//		return documentName;
//	}
//	
//	public void setStartDate(String date) {
//		paramVO = getParamVO();
//		if (StringUtils.isEmpty(date)) {
//			paramVO.setStartDate(null);
//		} else {
//			try {
//				paramVO.setStartDate(sdf.parse(date));
//			} catch (Exception e) {
//				LOG.warn("Unable convert " + date + " to date: " + e.getMessage());
//			}
//		}
//	}
//	public String getStartDate() {
//		paramVO = getParamVO();
//		if (paramVO.getStartDate() == null) 
//			return "";
//		return sdf.format(paramVO.getStartDate());
//	}
//	
//	public void setEndDate(String date) {
//		paramVO = getParamVO();
//		if (StringUtils.isEmpty(date)) {
//			paramVO.setEndDate(null);
//		} else {
//			try {
//				paramVO.setEndDate(sdf.parse(date));
//			} catch (Exception e) {
//				LOG.warn("Unable convert " + date + " to date: " + e.getMessage());
//			}
//		}
//	}
//	public String getEndDate() {
//		paramVO = getParamVO();
//		if (paramVO.getEndDate() == null) 
//			return "";
//		return sdf.format(paramVO.getEndDate());
//	}
//
//	public AccStatementParamVO getParamVO() {
//		if (paramVO == null)
//			paramVO = new AccStatementParamVO();
//		return paramVO;
//	}
//	
//	public String getMessage() {
//		return message;
//	}
//}
