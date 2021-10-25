//package id.co.emobile.samba.web.action.operational;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import id.co.emobile.samba.web.action.BaseListAction;
//import id.co.emobile.samba.web.data.DistMakerVO;
//import id.co.emobile.samba.web.data.LinkTableVO;
//import id.co.emobile.samba.web.data.SourceAccountVO;
//import id.co.emobile.samba.web.data.UserDataLoginVO;
//import id.co.emobile.samba.web.data.WebConstants;
//import id.co.emobile.samba.web.data.WebResultVO;
//import id.co.emobile.samba.web.data.param.ParamPagingVO;
//import id.co.emobile.samba.web.data.param.SourceAccountParamVO;
//import id.co.emobile.samba.web.helper.WebModules;
//import id.co.emobile.samba.web.interceptor.ModuleCheckable;
//import id.co.emobile.samba.web.service.DistMoneyService;
//import id.co.emobile.samba.web.service.SambaWebException;
//import id.co.emobile.samba.web.service.SourceAccountService;
//
//public class DistMakerAction extends BaseListAction implements ModuleCheckable {
//	private static final long serialVersionUID = 1L;
//	private static final Logger LOG = LoggerFactory.getLogger(DistMakerAction.class);
//
//	@Autowired
//	private SourceAccountService sourceAccountService;
//
//	@Autowired
//	private DistMoneyService distMoneyService;
//	
//	private List<SourceAccountVO> listSourceAccount;
//	private WebResultVO wrv;
//	private int sracId;
//	private String message;
//	private String json;
//	
//	private String remarks;
//	private File fileDist;
//	private String fileDistFileName;
//	private String fileDistContentType;
//	private File fileAssignment;
//	private String fileAssignmentFileName;
//	private String fileAssignmentContentType;
//	
//	private DistMakerVO distMakerVO;
//	private String otp;
//	
//	@Override
//	public String[] getSessionKeyToHandle() {
//		return new String[]{ WEB_CONTENT_KEY };
//	}
//
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//
//	//SAVE & INITIALIZATION
//	@Override
//	public String execute() {
//		setMessage(getFlashMessage());
//		return INPUT;
//	}
//
//	@Override
//	public int getMenuId() {
//		return WebModules.MODULE_APPS_OPERATIONAL_DIST_MAKER;
//	}
//	
//	public String process() {
//		LOG.debug("Process sracId: {}, remarks {}, fileDist {}, fileAssignment {}",
//				sracId, remarks, fileDistFileName, fileAssignmentFileName);
//		try {
//			UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
//			distMakerVO = distMoneyService.processDistMaker(loginVO.getUserCode(), sracId, remarks, fileDistFileName, fileDist, fileAssignmentFileName, fileAssignment);
//			distMakerVO.setTrxCode("1");
//			session.put(WEB_CONTENT_KEY, distMakerVO);
//			if (distMakerVO.isDetailCheckSuccess())
//				distMoneyService.sendDistMakerOtp(loginVO.getPhoneNo(), distMakerVO);
//			return "verify";
//		} catch (SambaWebException fae) {
//			LOG.warn("SambaWebException", fae);
//			WebResultVO wrv = handleJsonException(fae);
//			setMessage(wrv.getMessage());
//		} catch (Exception e) {
//			LOG.warn("Exception", e);
//			WebResultVO wrv = handleJsonException(e);
//			setMessage(wrv.getMessage());
//		}
//		return INPUT; // "verify";
//	}
//
//	public String verify() {
//		LOG.debug("Verify otp {}", otp);
//		try {
//			UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
//			distMoneyService.verifyDistMaker(loginVO, getDistMakerVO(), otp);
//			return SUCCESS;
//		} catch (SambaWebException fae) {
////			LOG.warn("SambaWebException", fae);
//			WebResultVO wrv = handleJsonException(fae);
//			setMessage(wrv.getMessage());
//		} catch (Exception e) {
//			LOG.warn("Exception", e);
//			WebResultVO wrv = handleJsonException(e);
//			setMessage(wrv.getMessage());
//		}
//		return "verify";
//	}
//	
//	public String finish() {
//		Object o = session.remove(WEB_CONTENT_KEY);
//		if (o instanceof DistMakerVO) {
//			distMakerVO = (DistMakerVO) o;
//			return FINISH;
//		}
//		return execute();
//	}
//	
//	public String back() {
//		if (StringUtils.isNotEmpty(getDistMakerVO().getRemarks()))
//			remarks = getDistMakerVO().getRemarks();
//		if (getDistMakerVO().getSracId() != 0)
//			sracId = getDistMakerVO().getSracId();
//		session.remove(WEB_CONTENT_KEY);
//		return INPUT;
//	}
//	
//	public String otp() {
//		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
//		distMoneyService.sendDistMakerOtp(loginVO.getPhoneNo(), getDistMakerVO());
//		wrv = new WebResultVO();
//		wrv.setRc(WebConstants.RESULT_SUCCESS);
//		return "otp";
//	}
//	
//	public String getMessage() {
//		return message;
//	}
//
//	public void setMessage(String message) {
//		this.message = message;
//	}
//
//	public List<SourceAccountVO> getListSourceAccount() {
//		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);	
//		return sourceAccountService.getListSourceAccountByGroup(loginVO.getGroupId());
//	}
//	
//	public DistMakerVO getDistMakerVO() {
//		if (distMakerVO == null) {
//			Object o = session.get(WEB_CONTENT_KEY);
//			if (o instanceof DistMakerVO)
//				distMakerVO = (DistMakerVO) o;
//			if (distMakerVO == null)
//				distMakerVO = new DistMakerVO();
//		}
//		return distMakerVO;
//	}
//
//	public void setFileDist(File fileDist) {
//		this.fileDist = fileDist;
//	}
//
//	public void setFileDistFileName(String fileDistFileName) {
//		this.fileDistFileName = fileDistFileName;
//	}
//
//	public void setFileDistContentType(String fileDistContentType) {
//		this.fileDistContentType = fileDistContentType;
//	}
//
//	public void setFileAssignment(File fileAssignment) {
//		this.fileAssignment = fileAssignment;
//	}
//
//	public void setFileAssignmentFileName(String fileAssignmentFileName) {
//		this.fileAssignmentFileName = fileAssignmentFileName;
//	}
//
//	public void setFileAssignmentContentType(String fileAssignmentContentType) {
//		this.fileAssignmentContentType = fileAssignmentContentType;
//	}
//	
//	public int getSracId() {
//		return sracId;
//	}
//	public void setSracId(int sracId) {
//		this.sracId = sracId;
//	}
//	
//	public String getRemarks() {
//		return remarks;
//	}
//	public void setRemarks(String remarks) {
//		this.remarks = remarks;
//	}
//
//	public void setOtp(String otp) {
//		this.otp = otp;
//	}
//
//	public WebResultVO getWrv() {
//		return wrv;
//	}
//	
//	/**************************************   ESSENTIAL FOR SEARCH  *******************************************/
//	private String resultSearchJson;
//
//	public void makeTableContent()
//	{
//		prepareParamVO(new SourceAccountParamVO(), WEB_PARAM_KEY + WebModules.MODULE_APPS_SETTING_SOURCE_ACCOUNT,
//				"ud.id", WebConstants.SORT_ORDER_ASC);
//		String[] arrayHeader={getText("l.recordNo"), getText("l.phoneNo"), getText("l.sracNumber"), getText("l.sracName"), getText("l.status")};
//		String[] arrayBody={"rowNum", "phoneNo", "sracNumber", "sracName", "sracStatusDisplay"};
//		String[] arrayDbVariable={"", "phoneNo", "sracNumber", "sracName", "sracStatus"};
//		List<LinkTableVO> listLinkTable=new ArrayList<LinkTableVO>();
//		listLinkTable.add(new LinkTableVO("SourceAccount!detail.web", "phoneNo", new String[]{"sracId"}, new String[]{"id"}));
//
//		SourceAccountParamVO sracParamVO = (SourceAccountParamVO) paramVO;
//		int totalRow = sourceAccountService.countSourceAccountByParam(sracParamVO);
//		listSourceAccount = sourceAccountService.findSourceAccountByParam(sracParamVO);
//		Locale language =  (Locale) session.get(WEB_LOCALE_KEY);
//		try {
//			String bodyContent = objectMapper.writeValueAsString(listSourceAccount);
//			resultSearchJson = webSearchResultService.composeSearchResult(getText("l.listSourceAccount"), arrayHeader, arrayBody,
//					arrayDbVariable, bodyContent, getCurrentPage(),
//					totalRow, listLinkTable, language, listSourceAccount.size(), paramVO);
//		} catch (Exception e) {
//			LOG.warn("Exception when serializing " + listSourceAccount, e);
//		}
//		
//	}
//
//	@Override
//	public ParamPagingVO getParamVO() {
//		if (paramVO == null)
//			paramVO = new SourceAccountParamVO();
//		return paramVO;
//	}
//
//	public InputStream getWsr() {
//		return new ByteArrayInputStream(resultSearchJson.toString().getBytes());
//	}
//		
//	/**************************************   ESSENTIAL FOR SEARCH  *******************************************/
//	
//}
