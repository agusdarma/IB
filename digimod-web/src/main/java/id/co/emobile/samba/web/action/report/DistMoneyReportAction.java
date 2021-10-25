//package id.co.emobile.samba.web.action.report;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import id.co.emobile.samba.web.action.BaseListAction;
//import id.co.emobile.samba.web.data.LinkTableVO;
//import id.co.emobile.samba.web.data.UserDataLoginVO;
//import id.co.emobile.samba.web.data.WebConstants;
//import id.co.emobile.samba.web.data.param.ParamPagingVO;
//import id.co.emobile.samba.web.data.param.ReportParamVO;
//import id.co.emobile.samba.web.data.param.UserActivityParamVO;
//import id.co.emobile.samba.web.entity.AppsTrxType;
//import id.co.emobile.samba.web.entity.DistributionDetail;
//import id.co.emobile.samba.web.entity.DistributionHeader;
//import id.co.emobile.samba.web.entity.SummaryDailyTrx;
//import id.co.emobile.samba.web.helper.WebModules;
//import id.co.emobile.samba.web.interceptor.ModuleCheckable;
//import id.co.emobile.samba.web.service.AppsTimeService;
//import id.co.emobile.samba.web.service.AppsTrxTypeService;
//import id.co.emobile.samba.web.service.DistMoneyService;
//import id.co.emobile.samba.web.service.ReportService;
//import id.co.emobile.samba.web.utils.CommonUtil;
//
//public class DistMoneyReportAction extends BaseListAction implements ModuleCheckable{
//	private static final long serialVersionUID = 1L;
//	private static final Logger LOG = LoggerFactory.getLogger(DistMoneyReportAction.class);
//
//	@Autowired
//	private ReportService reportService;
//
//	@Autowired
//	private DistMoneyService distMoneyService;
//
//	private List<SummaryDailyTrx> listSummary;
//	
//	private List<DistributionHeader> listHeader;
//	private List<DistributionDetail> listDetail;
//	private String sysLogNo;
//	private ReportParamVO paramVO;
//	
//	
//	@Autowired
//	private AppsTrxTypeService appsTrxTypeService;
//	
//	@Autowired
//	private AppsTimeService timeService;
//	
//	//for reporting
//	private String exportType;
//	private HashMap<String, String> reportParameters;
//	private String trxCodeJson;
//	private String exportFormat;
//	private String documentName;
//	
//	public String exportReport(){
//		LOG.debug("processSearch with exportType {}", exportType);
//		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
////		listHeader = distMoneyService.findDistHeaderForReport(loginVO);
//		listHeader = distMoneyService.findDistHeaderForReport(loginVO,paramVO);
//		exportFormat = exportType;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		String today = sdf.format(timeService.getCurrentTime());
//		documentName = "dist_money_report-"+today;	 	
//		
//		LOG.info("exporting distribute money with param:" +  listHeader +", with exportType : " + exportType);
//		return "EXPORT";
//	}
//	
//	public String exportReportDetail(){
//		LOG.debug("processSearch with exportType pdf with syslogno {}" , sysLogNo);
////		UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
////		listHeader = distMoneyService.findDistHeaderForReport(loginVO);
////		listHeader = distMoneyService.findDistHeaderForReport(loginVO,paramVO);
//		listDetail = distMoneyService.findDistDetailBySysLogNo(sysLogNo);
//		String statusDisplay = "This is a computer generated message and requires no signature\r\n" + 
//				"Informasi ini hasil cetakan komputer dan tidak memerlukan tanda tangan";
//		
//		for (DistributionDetail distributionDetail : listDetail) {
//			String terbilang = CommonUtil.terbilang(new BigDecimal(distributionDetail.getMoneyValue()));
//			distributionDetail.setTerbilangDisplayReport(terbilang);
//			LOG.debug("terbilang {}" , terbilang);
//			LOG.debug("alamat {}" , distributionDetail.getAlamat());
//			if(distributionDetail.getHostRc().equalsIgnoreCase("44")||
//					distributionDetail.getHostRc().equalsIgnoreCase("CA")) {
//				statusDisplay = "Transaksi sedang dalam proses";
//			}
//			LOG.debug("statusDisplay {}" , statusDisplay);
//			distributionDetail.setStatusDisplayReport(statusDisplay);
//		}
//		
//		LOG.debug("listDetail {}" , listDetail);
//		exportFormat = "PDF";
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		String today = sdf.format(timeService.getCurrentTime());
//		documentName = "dist_money_report_detail-"+today;	 	
//		try {
//			String tanggalBuku = listDetail.get(0).getTanggalBuku();
//			LOG.info("tanggalBuku:" +  tanggalBuku);
//			String day = tanggalBuku.substring(0, 2);
//			String month = tanggalBuku.substring(2, 4);
//			String year = tanggalBuku.substring(4, 8);
//			tanggalBuku = day+"/"+month+"/"+year;
//			LOG.info("tanggalBuku baru:" +  tanggalBuku);
//			for (DistributionDetail distributionDetail : listDetail) {
//				distributionDetail.setTanggalBuku(tanggalBuku);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		
//		
//		LOG.info("exporting distribute money with param:" +  listDetail +", with exportType : " + exportFormat);
//		return "EXPORTDETAIL";
//	}
//	
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//
//	@Override
//	public String execute(){
//		getLogger().debug("Processing --> execute()");
//		session.remove(WEB_CONTENT_KEY_2);
//		Date current = timeService.getCurrentTime();
//		getLogger().debug("execute with {}", current);
//		paramVO = (ReportParamVO) getParamVO();
//		paramVO.setStartDate(current);
//		paramVO.setEndDate(current);
//		return SEARCH;
//	}
//	
//	public String detail() {
//		return "detail";
//	}
//
////	public String processSearch(){
////		LOG.debug("processSearch with exportType {}", exportType);
////		if(StringUtils.isEmpty(exportType))
////		{
////			makeTableContent();
////			return "searchJson";
////		}
////		else
////		{
////			if(exportType.equals("CSV")){
////				return "CSV";
////			}
////			prepareParamVO(new UserActivityParamVO(), WEB_PARAM_KEY + WebModules.MODULE_REPORT_DIST_MONEY,
////					"trxCode", WebConstants.SORT_ORDER_ASC);
////			ReportParamVO rpVO = (ReportParamVO) paramVO;
////			LOG.info("exporting summary transaction with param:" +  rpVO +", with exportType : " + exportType);
////			int totalRow = reportService.countSummaryDailyTransaction(rpVO);
////			rpVO.setRowPerPage(totalRow);
////			listSummary = reportService.findSummaryDailyTransaction(rpVO);
////			reportParameters=new HashMap<String, String>();
////			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
////			generateExportParameter(rpVO, reportParameters);
////			exportFormat = exportType;
////			Date now = new Date();
////			String today = sdf.format(now);
////			documentName = "summary_transaction_report-"+today;	 		
////			return "EXPORT";
////		}
////	}
//	
//	public String processSearch() {
//		try {
//			getLogger().debug("processSearch: {}", paramVO);
//			UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
////			listHeader = distMoneyService.findDistHeaderForReport(loginVO);
//			listHeader = distMoneyService.findDistHeaderForReport(loginVO,paramVO);
//			
//		} catch (Exception e) {
//			getLogger().warn("Exception in processSearch " + paramVO, e);
//		}
//		return SEARCH;
//	}
//	
//	private void generateExportParameter(ReportParamVO paramVO, HashMap<String, String> reportParam)
//	{	
//		if(paramVO.getTrxCode().equals("0"))
//		{
//			reportParam.put("trxCode", "All Type");
//		}
//		else
//		{
//			List<AppsTrxType> listAppsTrxType = appsTrxTypeService.findAllAppsTrxType();
//			for (AppsTrxType appsTrxType : listAppsTrxType) {
//				if(appsTrxType.getTrxName().equals(paramVO.getTrxCode()))
//				{
//					reportParam.put("trxCode", appsTrxType.getTrxName());
//				}
//			}
//		}
//		
//		if(paramVO.getTrxGroup()==0)
//		{
//			reportParam.put("trxGroup", "All Category");
//		}
//		else if(paramVO.getTrxGroup()==1)
//		{
//			reportParam.put("trxGroup", "Financial");			
//		}
//		else if(paramVO.getTrxGroup()==2)
//		{
//			reportParam.put("trxGroup", "Non Financial");
//		}		
//		
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//		String reportDate = "";
//		if(paramVO.getStartDate()!=null)
//		{
//			reportDate = sdf.format(paramVO.getStartDate());
//		}
//		else
//		{
//			reportDate = "All";
//		}
//		if(paramVO.getEndDate()!=null)
//		{
//			reportDate += " - " +  sdf.format(paramVO.getEndDate());
//		}
//		else
//		{
//			Date now = new Date();
//			reportDate += " - " + sdf.format(now);
//		}
//		if(paramVO.getStartDate()==null && paramVO.getEndDate()==null)
//		{
//			reportDate = "All Time";
//		}
//
//		reportParameters.put("paramReportDate", reportDate);
//	}
//	
//	public InputStream getStreamTrxCode() {
//		return new ByteArrayInputStream(trxCodeJson.toString().getBytes());
//	}
//
//	public InputStream getCsvStream() {
//		StringBuilder sb = new StringBuilder();
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//		Date now = new Date();
//		String today = sdf.format(now);
//		documentName = "summary_transaction_report-"+today+".csv";	
//		
//		prepareParamVO(new UserActivityParamVO(), WEB_PARAM_KEY + WebModules.MODULE_REPORT_DIST_MONEY,
//				"trxCode", WebConstants.SORT_ORDER_ASC);
//		ReportParamVO rpVO = (ReportParamVO) paramVO;
//		LOG.info("exporting summary transaction with param:" +  rpVO +", with exportType : " + exportType);
//		int totalRow = reportService.countSummaryDailyTransaction(rpVO);
//		rpVO.setRowPerPage(totalRow);
//		listSummary = reportService.findSummaryDailyTransaction(rpVO);
//		
//		sb.append("Record Number").append(",");
//		sb.append("Bank").append(",");
//		sb.append("Transaction Type").append(",");
//		sb.append("Transction Category").append(",");
//		sb.append("Total");		
//		sb.append("\n");		
//
//		int i=1;
//		for (SummaryDailyTrx dataVO : listSummary){
//			sb.append(i + ",");
//			i++;
//			if (dataVO.getBankName() == null)
//				sb.append(",");
//			else
//				sb.append(dataVO.getBankName() + ",");
//
//			if (dataVO.getTrxCode() == null)
//				sb.append(",");
//			else
//				sb.append(dataVO.getTrxCode() + ",");
//
//			if (dataVO.getTrxGroup() == null)
//				sb.append(",");
//			else
//				sb.append(dataVO.getTrxGroup() + ",");
//
//			sb.append(dataVO.getAmount()+"");
//			sb.append("\n");
//		}
//		return new ByteArrayInputStream(sb.toString().getBytes());
//	}
//	
//	@Override
//	public int getMenuId() {
//		return WebModules.MODULE_REPORT_DIST_MONEY;
//	}
//
//	public HashMap<String, String> getReportParameters() {
//		return reportParameters;
//	}
//	//new a
//	public void setReportParameters(HashMap<String, String> reportParameters) {
//		this.reportParameters = reportParameters;
//	}
//	//new z
//
//	public String getExportType() {
//		return exportType;
//	}
//
//	public void setExportType(String exportType) {
//		this.exportType = exportType;
//	}
//
//	// new z
//
//	public void setParamVO(ReportParamVO paramVO) {
//		this.paramVO = paramVO;
//	}
//
//	public String getExportFormat() {
//		return exportFormat;
//	}
//
//	public void setExportFormat(String exportFormat) {
//		this.exportFormat = exportFormat;
//	}
//	
//	public String getDocumentName() {
//		return documentName;
//	}
//
//	public void setDocumentName(String documentName) {
//		this.documentName = documentName;
//	}
//	
//	public List<AppsTrxType> getListTrxType()
//	{
//		List<AppsTrxType> listAppsTrxType = appsTrxTypeService.findAllAppsTrxType();
//		if(listAppsTrxType==null)listAppsTrxType = new ArrayList<AppsTrxType>();
//		return listAppsTrxType;
//	}
//	
//	public HashMap<String, String> getListJenisTrx()
//	{
//		HashMap<String, String> listJenis = new HashMap<String, String>();
//		listJenis.put("", "All");
//		listJenis.put("1", "Transfer Sesama");
//		listJenis.put("2", "Transfer Antar Bank");
//		listJenis.put("3", "Pembayaran Pajak");
//		return listJenis;
//	}
//		
//	public List<SummaryDailyTrx> getListSummary() {
//		return listSummary;
//	}
//
//	public void setListSummary(List<SummaryDailyTrx> listSummary) {
//		this.listSummary = listSummary;
//	}
//
//	public List<DistributionHeader> getListHeader() {
//		if (listHeader == null) {
////			UserDataLoginVO loginVO = (UserDataLoginVO) session.get(LOGIN_KEY);
////			listHeader = distMoneyService.findDistHeaderForReport(loginVO);
//			listHeader = new ArrayList<DistributionHeader>();
//		}
//		return listHeader;
//	}
//	public void setSysLogNo(String sysLogNo) {
//		this.sysLogNo = sysLogNo;
//	}
//	public List<DistributionDetail> getListDetail() {
//		if (listDetail == null) {
//			listDetail = distMoneyService.findDistDetailBySysLogNo(sysLogNo);
//			if (listDetail == null)
//				listDetail = new ArrayList<>();
//		}
//		return listDetail;
//	}
//	public DistributionHeader getHeader() {
//		return distMoneyService.findDistHeaderBySysLogNo(sysLogNo);
//	}
//
//	/**************************************   ESSENTIAL FOR SEARCH  *******************************************/
//	private String resultSearchJson;
//
//	public void makeTableContent()
//	{		
//		prepareParamVO(new ReportParamVO(), WEB_PARAM_KEY + WebModules.MODULE_REPORT_DIST_MONEY,
//				"trxCode", WebConstants.SORT_ORDER_ASC);		
//		String[] arrayHeader={getText("l.recordNo"), getText("l.bank"), getText("l.trxType"), getText("l.trxCategory"), getText("l.total")};
//		String[] arrayBody={"rowNum", "bankName", "trxCode", "trxGroup",  "amount"};
//		String[] arrayDbVariable={"", "bankName", "trxCode", "trxGroup", "amount"};
//		
//		List<LinkTableVO> listLinkTable=new ArrayList<LinkTableVO>();
//
//		ReportParamVO reportParamVO = (ReportParamVO) paramVO;
//		LOG.info("Search daily transaction activity by param : " + reportParamVO);
//		int totalRow = reportService.countSummaryDailyTransaction(reportParamVO);		
//		listSummary = reportService.findSummaryDailyTransaction(reportParamVO);
//		Locale language =  (Locale) session.get(WEB_LOCALE_KEY);
//		try {
//			String bodyContent = objectMapper.writeValueAsString(listSummary);
//			resultSearchJson=webSearchResultService.composeSearchResultWithExport(getText("l.listTransaction"), arrayHeader, arrayBody,
//					arrayDbVariable, bodyContent, getCurrentPage(),
//					totalRow, listLinkTable, language, listSummary.size(), paramVO);
//		} catch (Exception e) {
//			LOG.warn("Exception when serializing " + listSummary, e);
//		}
//		
//	}
//
//
//	@Override
//	public ParamPagingVO getParamVO() {
//		if (paramVO == null)
//			paramVO = new ReportParamVO();
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
