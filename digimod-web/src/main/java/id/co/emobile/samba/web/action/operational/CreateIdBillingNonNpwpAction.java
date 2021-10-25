//package id.co.emobile.samba.web.action.operational;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Date;
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
//import id.co.emobile.samba.web.entity.Lookup;
//import id.co.emobile.samba.web.helper.WebModules;
//import id.co.emobile.samba.web.interceptor.ModuleCheckable;
//import id.co.emobile.samba.web.service.DistMoneyService;
//import id.co.emobile.samba.web.service.LookupService;
//import id.co.emobile.samba.web.service.SambaWebException;
//import id.co.emobile.samba.web.service.SourceAccountService;
//import id.co.emobile.samba.web.utils.CommonUtil;
//
//public class CreateIdBillingNonNpwpAction extends BaseListAction implements ModuleCheckable {
//	private static final long serialVersionUID = 1L;
//	private static final Logger LOG = LoggerFactory.getLogger(CreateIdBillingNonNpwpAction.class);
//
//	@Autowired
//	private SourceAccountService sourceAccountService;
//
//	@Autowired
//	private DistMoneyService distMoneyService;
//
//	@Autowired
//	private LookupService lookupService;
//
//	private List<SourceAccountVO> listSourceAccount;
//	private WebResultVO wrv;
//	private int sracId;
//	private String message;
//	private String json;
//
//	private String namaWp;
//	private String alamatWp;
//	private String kotaWp;
//
//	private String npwpSsp;
//	private String nama;
//	private String alamat;
//	private String jenisPajak;
//	private String jenisSetoran;
//	private Date startDatePbb;
//	private Date endDatePbb;
//	private String tahunPajak;
//	private String jumlahSetor;
//	private String uraianSsp;
//	private String npwpPenyetor;
//	private String noSk;
//	private String nop;
//	private String kodeKKPNpwp;
//	private String nik;
//
////	private String nominal;
////	private String dsac;
////	private String dsacName;	
////	private String kodeBank;
//	private String remarks;
////	private String phoneNo;
//	private File fileAssignment;
//	private String fileAssignmentFileName;
//	private String fileAssignmentContentType;
//
//	private DistMakerVO distMakerVO;
//	private String otp;
//
//	@Override
//	public String[] getSessionKeyToHandle() {
//		return new String[] { WEB_CONTENT_KEY };
//	}
//
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//
//	// SAVE & INITIALIZATION
//	@Override
//	public String execute() {
//		setMessage(getFlashMessage());
//		return INPUT;
//	}
//
//	@Override
//	public int getMenuId() {
//		return WebModules.MODULE_APPS_OPERATIONAL_CREATE_ID_BILLING_NON_NPWP;
//	}
//
//	public String process() {
//		LOG.debug("Process CreateIdBillingAction npwpSsp: {}, nama {}, alamat {}, "
//				+ "fileAssignment {}, remarks {}, jenis pajak {}, jenis setoran {}, startDatePbb {}, endDatePbb {}, tahun pajak {}, "
//				+ "jumlah Setor {},sracid {},uraianSsp {},npwpPenyetor {},noSk {},kodeKKPNpwp {} ,kotaWp {}, nik {}", CommonUtil.convertNumericFormatToString(npwpSsp),
//				nama, alamat, fileAssignmentFileName, remarks, jenisPajak, jenisSetoran, startDatePbb, endDatePbb,
//				tahunPajak, CommonUtil.convertNumericFormatToString(jumlahSetor), sracId, uraianSsp, CommonUtil.convertNumericFormatToString(npwpPenyetor), CommonUtil.convertNumericFormatToString(noSk),
//				kodeKKPNpwp, kotaWp, nik);
//		try {
//			UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
//
//			npwpSsp = "000000000" + npwpSsp + "000";
//			LOG.debug(String.format("npwpSsp : %s", npwpSsp));
//			LOG.debug(String.format("npwpPenyetor : %s", npwpPenyetor));
//			distMakerVO = distMoneyService.processCreateIdBilling(loginVO.getUserCode(), CommonUtil.convertNumericFormatToString(npwpSsp), namaWp, alamatWp,
//					remarks, fileAssignmentFileName, fileAssignment, jenisPajak, jenisSetoran, startDatePbb, endDatePbb,
//					tahunPajak, CommonUtil.convertNumericFormatToString(jumlahSetor), sracId, uraianSsp, CommonUtil.convertNumericFormatToString(npwpPenyetor),
//					CommonUtil.convertNumericFormatToString(noSk), CommonUtil.convertNumericFormatToString(nop), kotaWp, true,nik);
//			distMakerVO.setTrxCode("3");
//			session.put(WEB_CONTENT_KEY, distMakerVO);
//			LOG.debug(String.format("verify data : %s", distMakerVO));
//			if (distMakerVO.isDetailCheckSuccess())
//				distMoneyService.sendDistMakerOtp(loginVO.getPhoneNo(), distMakerVO);
//			return "verify";
//		} catch (SambaWebException fae) {
////			LOG.warn("SambaWebException", fae);
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
//			distMoneyService.verifyDistMakerPajak(loginVO, getDistMakerVO(), otp);
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
//	public List<Lookup> getListJenisPajak() {
//		List<Lookup> listLookup = lookupService.findLookupByCat(LookupService.CAT_JENIS_PAJAK);
//		return listLookup;
//	}
//
//	public List<Lookup> getListJenisSetoran() {
//		List<Lookup> listLookup = lookupService.findLookupByCat(LookupService.CAT_JENIS_SETORAN);
//		return listLookup;
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
//
//	public void setSracId(int sracId) {
//		this.sracId = sracId;
//	}
//
//	public String getRemarks() {
//		return remarks;
//	}
//
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
//	/**************************************
//	 * ESSENTIAL FOR SEARCH
//	 *******************************************/
//	private String resultSearchJson;
//
//	public void makeTableContent() {
//		prepareParamVO(new SourceAccountParamVO(), WEB_PARAM_KEY + WebModules.MODULE_APPS_SETTING_SOURCE_ACCOUNT,
//				"ud.id", WebConstants.SORT_ORDER_ASC);
//		String[] arrayHeader = { getText("l.recordNo"), getText("l.phoneNo"), getText("l.sracNumber"),
//				getText("l.sracName"), getText("l.status") };
//		String[] arrayBody = { "rowNum", "phoneNo", "sracNumber", "sracName", "sracStatusDisplay" };
//		String[] arrayDbVariable = { "", "phoneNo", "sracNumber", "sracName", "sracStatus" };
//		List<LinkTableVO> listLinkTable = new ArrayList<LinkTableVO>();
//		listLinkTable.add(new LinkTableVO("SourceAccount!detail.web", "phoneNo", new String[] { "sracId" },
//				new String[] { "id" }));
//
//		SourceAccountParamVO sracParamVO = (SourceAccountParamVO) paramVO;
//		int totalRow = sourceAccountService.countSourceAccountByParam(sracParamVO);
//		listSourceAccount = sourceAccountService.findSourceAccountByParam(sracParamVO);
//		Locale language = (Locale) session.get(WEB_LOCALE_KEY);
//		try {
//			String bodyContent = objectMapper.writeValueAsString(listSourceAccount);
//			resultSearchJson = webSearchResultService.composeSearchResult(getText("l.listSourceAccount"), arrayHeader,
//					arrayBody, arrayDbVariable, bodyContent, getCurrentPage(), totalRow, listLinkTable, language,
//					listSourceAccount.size(), paramVO);
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
//	public String getNpwpSsp() {
//		return npwpSsp;
//	}
//
//	public void setNpwpSsp(String npwp) {
//		this.npwpSsp = npwp;
//	}
//
//	public String getNama() {
//		return nama;
//	}
//
//	public void setNama(String nama) {
//		this.nama = nama;
//	}
//
//	public String getAlamat() {
//		return alamat;
//	}
//
//	public void setAlamat(String alamat) {
//		this.alamat = alamat;
//	}
//
//	public String getJenisPajak() {
//		return jenisPajak;
//	}
//
//	public void setJenisPajak(String jenisPajak) {
//		this.jenisPajak = jenisPajak;
//	}
//
//	public String getJenisSetoran() {
//		return jenisSetoran;
//	}
//
//	public void setJenisSetoran(String jenisSetoran) {
//		this.jenisSetoran = jenisSetoran;
//	}
//
//	public Date getStartDatePbb() {
//		return startDatePbb;
//	}
//
//	public void setStartDatePbb(Date startDatePbb) {
//		this.startDatePbb = startDatePbb;
//	}
//
//	public Date getEndDatePbb() {
//		return endDatePbb;
//	}
//
//	public void setEndDatePbb(Date endDatePbb) {
//		this.endDatePbb = endDatePbb;
//	}
//
//	public String getTahunPajak() {
//		return tahunPajak;
//	}
//
//	public void setTahunPajak(String tahunPajak) {
//		this.tahunPajak = tahunPajak;
//	}
//
//	public String getJumlahSetor() {
//		return jumlahSetor;
//	}
//
//	public void setJumlahSetor(String jumlahSetor) {
//		this.jumlahSetor = jumlahSetor;
//	}
//
//	public String getUraianSsp() {
//		return uraianSsp;
//	}
//
//	public void setUraianSsp(String uraianSsp) {
//		this.uraianSsp = uraianSsp;
//	}
//
//	public String getNpwpPenyetor() {
//		return npwpPenyetor;
//	}
//
//	public void setNpwpPenyetor(String npwpSsp) {
//		this.npwpPenyetor = npwpSsp;
//	}
//
//	public String getNoSk() {
//		return noSk;
//	}
//
//	public void setNoSk(String noSk) {
//		this.noSk = noSk;
//	}
//
//	public String getNop() {
//		return nop;
//	}
//
//	public void setNop(String nop) {
//		this.nop = nop;
//	}
//
//	public String getNamaWp() {
//		return namaWp;
//	}
//
//	public void setNamaWp(String namaWp) {
//		this.namaWp = namaWp;
//	}
//
//	public String getAlamatWp() {
//		return alamatWp;
//	}
//
//	public void setAlamatWp(String alamatWp) {
//		this.alamatWp = alamatWp;
//	}
//
//	public String getKotaWp() {
//		return kotaWp;
//	}
//
//	public void setKotaWp(String kotaWp) {
//		this.kotaWp = kotaWp;
//	}
//
//	public String getKodeKKPNpwp() {
//		return kodeKKPNpwp;
//	}
//
//	public void setKodeKKPNpwp(String kodeKKPNpwp) {
//		this.kodeKKPNpwp = kodeKKPNpwp;
//	}
//
//	public String getNik() {
//		return nik;
//	}
//
//	public void setNik(String nik) {
//		this.nik = nik;
//	}
//
//	/**************************************
//	 * ESSENTIAL FOR SEARCH
//	 *******************************************/
//
//}
