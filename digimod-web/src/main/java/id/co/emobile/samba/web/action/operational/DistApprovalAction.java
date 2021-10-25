//package id.co.emobile.samba.web.action.operational;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
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
//import id.co.emobile.samba.web.entity.Lookup;
//import id.co.emobile.samba.web.entity.UserGroup;
//import id.co.emobile.samba.web.helper.WebModules;
//import id.co.emobile.samba.web.interceptor.ModuleCheckable;
//import id.co.emobile.samba.web.service.DistMoneyService;
//import id.co.emobile.samba.web.service.LookupService;
//import id.co.emobile.samba.web.service.SambaWebException;
//import id.co.emobile.samba.web.service.SettingService;
//import id.co.emobile.samba.web.service.SourceAccountService;
//import id.co.emobile.samba.web.service.UserGroupService;
//import id.co.emobile.samba.web.utils.CommonUtil;
//
//public class DistApprovalAction extends BaseListAction implements ModuleCheckable {
//	private static final long serialVersionUID = 1L;
//	private static final Logger LOG = LoggerFactory.getLogger(DistApprovalAction.class);
//
//	@Autowired
//	private SourceAccountService sourceAccountService;
//	
//	@Autowired
//	private UserGroupService userGroupService;
//	
//	@Autowired
//	private DistMoneyService distMoneyService;
//	
//	@Autowired
//	private SettingService settingService;
//	
//	@Autowired
//	private LookupService lookupService;
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
//	// for processStatus
//	private String serverSysLogNo;
//	private int serverDetailId;
//	private int serverProcessStatus;
//	private String serverProcessRemarks;
//	
//	@Override
//	public String[] getSessionKeyToHandle() {
//		return new String[]{ WEB_CONTENT_KEY, WEB_CONTENT_KEY_2 };
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
//		try {
//			UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
//			int[] listStatus = {
//				WebConstants.DIST_STATUS_PARTIAL_FIN, WebConstants.DIST_STATUS_FAILED, WebConstants.DIST_STATUS_CALLBACK
//			};
//			List<UserGroup> listGroup = userGroupService.findUserGroupForApproval(loginVO.getId());
//			listHeader = new ArrayList<>();
//			for (UserGroup group: listGroup) {
//				List<DistributionHeader> listData = distMoneyService.findDistHeaderByGroupAndStatus(group.getId(), listStatus);
//				if (listData != null && listData.size() > 0)
//					listHeader.addAll(listData);
//			}
//		} catch (Exception e) {
//			LOG.warn("Exception in execute", e);
//		}
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
//			List<UserGroup> listGroup = userGroupService.findUserGroupForApproval(loginVO.getId());
//			boolean groupValid = false;
//			StringBuilder sb = new StringBuilder();
//			for (UserGroup group: listGroup) {
//				if (group.getId() == header.getGroupId()) {
//					groupValid = true;
//					break;
//				}
//				sb.append(group.getId()).append(",");
//			}
//			if (!groupValid) {
//				LOG.warn("GroupId {} is different with Transaction {}", sb.toString(), header);
//				throw new SambaWebException(SambaWebException.NE_INVALID_SYSLOGNO);
//			}
//			if (header.getStatus() != WebConstants.DIST_STATUS_PARTIAL_FIN &&
//					header.getStatus() != WebConstants.DIST_STATUS_CALLBACK &&
//					header.getStatus() != WebConstants.DIST_STATUS_FAILED) {
//				LOG.warn("Transaction {} is not for approval", loginVO.getGroupId(), header);
//				throw new SambaWebException(SambaWebException.NE_INVALID_SYSLOGNO);
//			}
//			listDetail = distMoneyService.findDistDetailBySysLogNo(header.getSysLogNo());
//			int otpLength = settingService.getSettingAsInt(SettingService.SETTING_OTP_LENGTH);
//			String otp = CommonUtil.generateRandomPin(otpLength);
//			header.setOtp(otp);
//			remarks = header.getApprovalRemarks();
//			distMoneyService.sendDistApprovalOtp(loginVO.getPhoneNo(), header);
//			
//			session.put(WEB_CONTENT_KEY, header);
//			session.put(WEB_CONTENT_KEY_2, listDetail);
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
//			getHeader().setApprovalRemarks(remarks);
//			if ("PROCESS".equalsIgnoreCase(processType))
//				getHeader().setStatus(WebConstants.DIST_STATUS_APPROVED);
//			else if ("REJECT".equalsIgnoreCase(processType))
//				getHeader().setStatus(WebConstants.DIST_STATUS_REJECTED_APPROVAL);
//			else {
//				LOG.warn("Unknown type " + processType);
//				throw new SambaWebException(SambaWebException.NE_INVALID_INPUT);
//			}
//			distMoneyService.verifyDistApproval(loginVO, getHeader(), getListDetail(), otp);
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
//		session.remove(WEB_CONTENT_KEY_2);
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
//		session.remove(WEB_CONTENT_KEY_2);
//		return execute();
//	}
//	
//	@Override
//	public int getMenuId() {
//		return WebModules.MODULE_APPS_OPERATIONAL_DIST_APPROVAL;
//	}
//	
//	public String otp() {
//		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
//		distMoneyService.sendDistApprovalOtp(loginVO.getPhoneNo(), getHeader());
//		wrv = new WebResultVO();
//		wrv.setRc(WebConstants.RESULT_SUCCESS);
//		return "otp";
//	}
//	
//	public String processStatus() {
//		LOG.debug("sysLogNo: {}, detailId: {}, processStatus: {}, processRemarks: {}",
//				serverSysLogNo, serverDetailId, serverProcessStatus, serverProcessRemarks);
//		List<DistributionDetail> listSelected = getListDetail();
//		for (DistributionDetail d: listSelected) {
//			if (d.getSysLogNo().equals(serverSysLogNo) && d.getDetailId() == serverDetailId) {
//				d.setProcessStatus(serverProcessStatus);
//				d.setProcessRemarks(serverProcessRemarks);
//				Lookup l = lookupService.findLookupByCatValue(LookupService.CAT_PROCESS_STATUS, "" + d.getProcessStatus());
//				if (l != null)
//					d.setProcessStatusDisplay(l.getLookupDesc());
//			}
//		}
//		wrv = new WebResultVO();
//		wrv.setRc(WebConstants.RESULT_SUCCESS);
//		return "process";
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
//	@SuppressWarnings("unchecked")
//	public List<DistributionDetail> getListDetail() {
//		if (listDetail == null) {
//			Object o = session.get(WEB_CONTENT_KEY_2);
//			if (o instanceof List) {
//				listDetail = (List<DistributionDetail>) o;
//			}
//			if (listDetail == null)
//				listDetail = distMoneyService.findDistDetailBySysLogNo(getHeader().getSysLogNo());
//		}
//		return listDetail;
//	}
//	
//	public double getProcessedAmount() {
//		double amount = 0;
//		List<DistributionDetail> listSelected = getListDetail();
//		for (DistributionDetail d: listSelected) {
//			if (d.getProcessStatus() == WebConstants.PROCESS_STATUS_INIT ||
//					d.getProcessStatus() == WebConstants.PROCESS_STATUS_FAILED) {
//				amount += d.getMoneyValue();
//			}
//		}
//		return amount;
//	}
//
//	public Map<String, String> getListProcessStatus() {
//		Map<String, String> mapProcessStatus = new LinkedHashMap<>();
//		List<Lookup> list = lookupService.findLookupByCat(LookupService.CAT_PROCESS_STATUS);
//		for (Lookup l: list) {
//			int v = Integer.parseInt(l.getLookupValue());
//			if (v== WebConstants.PROCESS_STATUS_FAILED ||
//					v == WebConstants.PROCESS_STATUS_SKIP_SUCCESS ||
//					v == WebConstants.PROCESS_STATUS_SKIP_FAILED) {
//				mapProcessStatus.put(l.getLookupValue(), l.getLookupDesc());
//			}
//		}
//		return mapProcessStatus;
//	}
//	
//	public WebResultVO getWrv() {
//		return wrv;
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
//	public void setServerSysLogNo(String serverSysLogNo) {
//		this.serverSysLogNo = serverSysLogNo;
//	}
//
//	public void setServerDetailId(int serverDetailId) {
//		this.serverDetailId = serverDetailId;
//	}
//
//	public void setServerProcessStatus(int serverProcessStatus) {
//		this.serverProcessStatus = serverProcessStatus;
//	}
//
//	public void setServerProcessRemarks(String serverProcessRemarks) {
//		this.serverProcessRemarks = serverProcessRemarks;
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
