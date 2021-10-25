//package id.co.emobile.samba.web.action.operational;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import id.co.emobile.samba.web.action.BaseListAction;
//import id.co.emobile.samba.web.data.LinkTableVO;
//import id.co.emobile.samba.web.data.SourceAccountVO;
//import id.co.emobile.samba.web.data.UserDataLoginVO;
//import id.co.emobile.samba.web.data.WebConstants;
//import id.co.emobile.samba.web.data.WebResultVO;
//import id.co.emobile.samba.web.data.param.ParamPagingVO;
//import id.co.emobile.samba.web.data.param.SourceAccountParamVO;
//import id.co.emobile.samba.web.entity.DistributionDetail;
//import id.co.emobile.samba.web.entity.DistributionHeader;
//import id.co.emobile.samba.web.helper.WebModules;
//import id.co.emobile.samba.web.interceptor.ModuleCheckable;
//import id.co.emobile.samba.web.service.DistMoneyService;
//import id.co.emobile.samba.web.service.SambaWebException;
//import id.co.emobile.samba.web.service.SettingService;
//import id.co.emobile.samba.web.service.SourceAccountService;
//import id.co.emobile.samba.web.utils.CommonUtil;
//
//public class DistCallbackAction extends BaseListAction implements ModuleCheckable {
//	private static final long serialVersionUID = 1L;
//	private static final Logger LOG = LoggerFactory.getLogger(DistCallbackAction.class);
//
//	@Autowired
//	private SourceAccountService sourceAccountService;
//	
//	@Autowired
//	private DistMoneyService distMoneyService;
//	
//	@Autowired
//	private SettingService settingService;
//	
//	private List<SourceAccountVO> listSourceAccount;
//	private WebResultVO wrv;
//	private String message;
//	
//	private List<DistributionHeader> listHeader;
//	private String sysLogNo;
//	private DistributionHeader header;
//	private List<DistributionDetail> listDetail;
//	private String remarks;
//	private String otp;
//	private String processType;
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
//		//setMessage(getFlashMessage());
//		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
//		listHeader = distMoneyService.findDistHeaderByGroupAndStatus(loginVO.getGroupId(), WebConstants.DIST_STATUS_CHECKED);
//		return INPUT;
//	}
//	
//	public String detail() {
//		getLogger().info("detail with sysLogNo: {}", sysLogNo);
//		// called when user needs to edit, to display input form
//		try {
//			header = distMoneyService.findDistHeaderBySysLogNo(sysLogNo);
//			if (header == null) {
//				LOG.warn("Unable to find transaction with sysLogNo {}", sysLogNo);
//				throw new SambaWebException(SambaWebException.NE_INVALID_SYSLOGNO);
//			}
//			UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
//			if (header.getGroupId() != loginVO.getGroupId()) {
//				LOG.warn("GroupId {} is different with Transaction {}", loginVO.getGroupId(), header);
//				throw new SambaWebException(SambaWebException.NE_INVALID_SYSLOGNO);
//			}
//			if (header.getStatus() != WebConstants.DIST_STATUS_CHECKED) {
//				LOG.warn("Transaction {} is not for callback", loginVO.getGroupId(), header);
//				throw new SambaWebException(SambaWebException.NE_INVALID_SYSLOGNO);
//			}
//			int otpLength = settingService.getSettingAsInt(SettingService.SETTING_OTP_LENGTH);
//			String otp = CommonUtil.generateRandomPin(otpLength);
//			header.setOtp(otp);
//			distMoneyService.sendDistCheckerOtp(loginVO.getPhoneNo(), header);
//			
//			session.put(WEB_CONTENT_KEY, header);
//			return "detail";
//		} catch (SambaWebException fae) {
////			LOG.warn("SambaWebException", fae);
//			WebResultVO wrv = handleJsonException(fae);
//			setMessage(wrv.getMessage());
//		} catch (Exception e) {
//			LOG.warn("Exception", e);
//			WebResultVO wrv = handleJsonException(e);
//			setMessage(wrv.getMessage());
//		}
//		return execute();
//	}
//
//	public String verify() {
//		LOG.debug("Verify with processType: {}, remarks {}, otp {}", processType, remarks, otp);
//		try {
//			UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
//			getHeader().setCallbackRemarks(remarks);
//			if ("PROCESS".equalsIgnoreCase(processType))
//				getHeader().setStatus(WebConstants.DIST_STATUS_CALLBACK);
//			else if ("REJECT".equalsIgnoreCase(processType))
//				getHeader().setStatus(WebConstants.DIST_STATUS_REJECTED_CALLBACK);
//			else {
//				LOG.warn("Unknown type " + processType);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT);
//			}
//			distMoneyService.verifyDistChecker(loginVO, getHeader(), otp);
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
//		return "detail";
//	}
//	
//	public String finish() {
//		Object o = session.remove(WEB_CONTENT_KEY);
//		if (o instanceof DistributionHeader) {
//			header = (DistributionHeader) o;
//			listDetail = distMoneyService.findDistDetailBySysLogNo(header.getSysLogNo());
//			return FINISH;
//		}
//		return execute();
//	}
//	
//	public String back() {
//		session.remove(WEB_CONTENT_KEY);
//		return execute();
//	}
//	
//	@Override
//	public int getMenuId() {
//		return WebModules.MODULE_APPS_OPERATIONAL_DIST_CALLBACK;
//	}
//	
//	public String otp() {
//		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
//		distMoneyService.sendDistCheckerOtp(loginVO.getPhoneNo(), getHeader());
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
//	public String getSysLogNo() {
//		return sysLogNo;
//	}
//
//	public void setSysLogNo(String sysLogNo) {
//		this.sysLogNo = sysLogNo;
//	}
//
//	public String getRemarks() {
//		return remarks;
//	}
//	public void setRemarks(String remarks) {
//		this.remarks = remarks;
//	}
//
//	public List<DistributionHeader> getListHeader() {
//		return listHeader;
//	}
//
//	public DistributionHeader getHeader() {
//		if (header == null) {
//			Object o = session.get(WEB_CONTENT_KEY);
//			if (o instanceof DistributionHeader) {
//				header = (DistributionHeader) o;
//			}
//			if (header == null) {
//				header = new DistributionHeader();
//			}
//		}
//		return header;
//	}
//
//	public List<DistributionDetail> getListDetail() {
//		if (listDetail == null) {
//			listDetail = distMoneyService.findDistDetailBySysLogNo(getHeader().getSysLogNo());
//		}
//		return listDetail;
//	}
//
//	public void setOtp(String otp) {
//		this.otp = otp;
//	}
//	
//	public void setProcessType(String processType) {
//		this.processType = processType;
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
//	
//	/**************************************   ESSENTIAL FOR SEARCH  *******************************************/
//
//}
