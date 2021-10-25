//package id.co.emobile.samba.web.service;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.MessageSource;
//import org.springframework.stereotype.Service;
//
//import id.co.emobile.samba.web.bti.DjpIsoBtiManager;
//import id.co.emobile.samba.web.bti.IsoBtiManager;
//import id.co.emobile.samba.web.bti.SddIsoBtiManager;
//import id.co.emobile.samba.web.data.CheckAccountVO;
//import id.co.emobile.samba.web.data.CreateIdBillingPaymentVO;
//import id.co.emobile.samba.web.data.CreateIdBillingVO;
//import id.co.emobile.samba.web.data.DistDetailVO;
//import id.co.emobile.samba.web.data.DistMakerVO;
//import id.co.emobile.samba.web.data.ResultCode;
//import id.co.emobile.samba.web.data.TermConstant;
//import id.co.emobile.samba.web.data.TransactionTO;
//import id.co.emobile.samba.web.data.WebConstants;
//import id.co.emobile.samba.web.entity.DistributionDetail;
//import id.co.emobile.samba.web.entity.DistributionHeader;
//import id.co.emobile.samba.web.entity.Lookup;
//import id.co.emobile.samba.web.entity.UserData;
//import id.co.emobile.samba.web.utils.CommonUtil;
//
//@Service
//public class TrxIsoService {
//	private static final Logger LOG = LoggerFactory.getLogger(TrxIsoService.class);
//
//	private static final String HOST_SUCCESS_CODE = "00";
//
//	private String channel = "WEB";
//	private String trxCode = "FUND_TRF_OTH";
//	private String trxCodeCreateIdBilling = "CREATE_ID_BILLING";
//	private String trxCodeCreateIdBillingPayment = "CREATE_ID_BILLING_PAYMENT";
//
//	public static final int FUND_TRANSFER_SETTLEMENT = 0;
//	public static final int FUND_TRANSFER_INQUIRY = 1;
//
//	@Autowired
//	private SettingService settingService;
//
//	@Autowired
//	private MessageSource messageSource;
//
//	@Autowired
//	private AppsTimeService timeService;
//
//	@Autowired
//	private LookupService lookupService;
//	
//	@Autowired
//	private UserDataService userDataService;
//
//	@Autowired
//	@Qualifier("collegaSequenceGenerator")
//	private SequenceGeneratorService collegaSeqGen;
//
//	@Autowired
//	@Qualifier("btiSequenceManager")
//	private SequenceGeneratorService btiSeqGen;
//
//	@Autowired
//	@Qualifier("btiManager")
//	private IsoBtiManager isoBtiManager;
//
//	@Autowired
//	@Qualifier("djpBtiManager")
//	private DjpIsoBtiManager isoDjpBtiManager;
//
//	@Autowired
//	@Qualifier("sddBtiManager")
//	private SddIsoBtiManager isoSddBtiManager;
//
//	public CreateIdBillingPaymentVO createIDBillingPaymentSett(DistributionHeader header, DistributionDetail detail) {
//		CreateIdBillingPaymentVO createIdBillingPaymentVO = new CreateIdBillingPaymentVO();
//		TransactionTO task = new TransactionTO();
//		try {
//			// create TransactionTO
//			String msgLogNo = btiSeqGen.getNextSequence();
//			long startTime = timeService.getCurrentTimemillis();
//			task.setMsgLogNo(msgLogNo);
//			task.setTrxCode(trxCodeCreateIdBillingPayment);
//			task.setReceivedTime(startTime);
//			task.setPhoneNo(CommonUtil.formatPhoneIntl(detail.getPhoneNo()));
//			task.setChannelType(channel);
//			task.setSmiInput(channel);
////			task.setMessageInput(maskedMsg);
////			task.setClientRef(gwRef);
////			Lookup lookup = lookupService.findLookupByCatValue(LookupService.CAT_BANK_CODE,detail.getBankCode());
//
////			task.setTerm(TermConstant.DSAC, detail.getAccNumber());
////			task.setTerm(TermConstant.DSAC_NAME, detail.getAccName());
////			task.setTerm(TermConstant.SRAC, distMakerVO.getSracNo());
////			task.setTerm(TermConstant.SRAC_NAME, distMakerVO.getSracName());
////			task.setTerm(TermConstant.BANK_CODE, detail.getBankCode());
////			task.setTerm(TermConstant.BANK_NAME, lookup.getLookupDesc());
//			task.setTerm(TermConstant.BIT48, detail.getBit48());
//			task.setTerm(TermConstant.NPWP_SSP, detail.getNpwp());
//			task.setTerm(TermConstant.NAMA_WP, detail.getNama());
//			task.setTerm(TermConstant.ALAMAT_WP, detail.getAlamat());
//			task.setTerm(TermConstant.MASA_KADALUARSA_BILLING, detail.getMasaAktifBilling());
//			task.setTerm(TermConstant.NPWP_PENYETOR, detail.getNpwpPenyetor());
//			task.setTerm(TermConstant.KODE_JENIS_SETOR, detail.getJenisSetoran());
//			task.setTerm(TermConstant.KODE_CABANG, "000"); // kantor pusat
//			task.setTerm(TermConstant.TAHUN_PAJAK, detail.getTahunPajak());
//			task.setTerm(TermConstant.KODE_MAP, detail.getJenisPajak());
//			task.setTerm(TermConstant.MASA_PAJAK_1, CommonUtil.displayMonthOnly(detail.getStartDatePbb()));
//			task.setTerm(TermConstant.MASA_PAJAK_2, CommonUtil.displayMonthOnly(detail.getEndDatePbb()));
//			task.setTerm(TermConstant.URAIAN_SSP, detail.getUraianSsp());
//			task.setTerm(TermConstant.KODE_KPP_SSP, detail.getKodeKkpSsp());
//			task.setTerm(TermConstant.NO_SK, detail.getNoSk());
//			task.setTerm(TermConstant.NOP, detail.getNop());
//			task.setTerm(TermConstant.JUMLAH_SETOR, detail.getJumlahSetor());
//			task.setTerm(TermConstant.ID_BILLING, detail.getIdBilling());
//			task.setTerm(TermConstant.SRAC, header.getSracNumber());
//			
//			UserData userData = userDataService.findUserById(header.getApprovedBy());
//			task.setTerm(TermConstant.LOGIN_USER, userData.getUserCode());
//			task.setTerm(TermConstant.BRANCH_CODE, userData.getBranchId());
//			LOG.debug("createIDBillingPaymentSett userData: {}", userData);
//			
//
//			task.setResultCode(ResultCode.SUCCESS_CODE);
//			task.setState(FUND_TRANSFER_SETTLEMENT);
////			task.setTrxValue(detail.getAmount());
////			task.setTerm(TermConstant.AMOUNT, detail.getAmount());
//			LOG.debug("createIDBillingPaymentSett TransactionTO: {}", task);
//			task = isoSddBtiManager.processDistribution(task);
//			LOG.debug("Hasil sendTransactionToHost: {}", task);
//			detail.setHostRc(task.getResultCode());
//			detail.setHostRefNo(task.getClientRef());
//			LOG.debug("Hasil createIDBillingPaymentSett: {}", task);
//			createIdBillingPaymentVO.setRc(task.getResultCode());
//			detail.setNtpn(task.getTerm(TermConstant.NTPN));
//			detail.setNop(task.getTerm(TermConstant.NOP));
//			detail.setNtb(task.getTerm(TermConstant.NTB));
//			detail.setStan(task.getTerm(TermConstant.STAN));
//
//			detail.setTanggalBuku(task.getTerm(TermConstant.TANGGAL_BUKU));
//			detail.setTglJamBayar(task.getTerm(TermConstant.TANGGAL_JAM_BAYAR));
//			detail.setKodeCabang(task.getTerm(TermConstant.KODE_CABANG));
//			detail.setTahunPajak(task.getTerm(TermConstant.MASA_PAJAK));
//			detail.setAlamat(task.getTerm(TermConstant.ALAMAT_WP));
//			LOG.debug("NTPN: {}", detail.getNtpn());
//			LOG.debug("NOP: {}", detail.getNop());
//			LOG.debug("NTB: {}", detail.getNtb());
//			LOG.debug("STAN: {}", detail.getStan());
//			LOG.debug("TANGGAL_JAM_BAYAR: {}", detail.getTglJamBayar());
//			LOG.debug("TANGGAL_JAM_BAYAR: {}", detail.getTglJamBayar());
//			LOG.debug("MASA_PAJAK: {}", detail.getTahunPajak());
//			LOG.debug("Kode Cabang: {}", detail.getKodeCabang());
//			LOG.debug("ALAMAT_WP: {}", detail.getAlamat());
//		} catch (Exception e) {
//			LOG.warn("Exception createIDBillingPaymentSett for " + task, e);
//		}
//		LOG.debug("Hasil createIDBillingPaymentSett: {}", createIdBillingPaymentVO);
//		return createIdBillingPaymentVO;
//	}
//
//	public CreateIdBillingPaymentVO createIDBillingPaymentInq(DistributionHeader header, DistributionDetail detail) {
//		CreateIdBillingPaymentVO createIdBillingPaymentVO = new CreateIdBillingPaymentVO();
//		TransactionTO task = new TransactionTO();
//		try {
//			// create TransactionTO
//			String msgLogNo = btiSeqGen.getNextSequence();
//			long startTime = timeService.getCurrentTimemillis();
//			task.setMsgLogNo(msgLogNo);
//			task.setTrxCode(trxCodeCreateIdBillingPayment);
//			task.setReceivedTime(startTime);
//			task.setPhoneNo(CommonUtil.formatPhoneIntl(detail.getPhoneNo()));
//			task.setChannelType(channel);
//			task.setSmiInput(channel);
////			task.setMessageInput(maskedMsg);
////			task.setClientRef(gwRef);
////			Lookup lookup = lookupService.findLookupByCatValue(LookupService.CAT_BANK_CODE,detail.getBankCode());
//
////			task.setTerm(TermConstant.DSAC, detail.getAccNumber());
////			task.setTerm(TermConstant.DSAC_NAME, detail.getAccName());
////			task.setTerm(TermConstant.SRAC, distMakerVO.getSracNo());
////			task.setTerm(TermConstant.SRAC_NAME, distMakerVO.getSracName());
////			task.setTerm(TermConstant.BANK_CODE, detail.getBankCode());
////			task.setTerm(TermConstant.BANK_NAME, lookup.getLookupDesc());
//			task.setTerm(TermConstant.IS_NON_NPWP, "0");
//			if(detail.isNonNpwp()) {
//				task.setTerm(TermConstant.IS_NON_NPWP, "1");
//			}
//			task.setTerm(TermConstant.NPWP_SSP, detail.getNpwp());
//			task.setTerm(TermConstant.NAMA_WP, detail.getNama());
//			task.setTerm(TermConstant.ALAMAT_WP, detail.getAlamat());
//			task.setTerm(TermConstant.MASA_KADALUARSA_BILLING, detail.getMasaAktifBilling());
//			task.setTerm(TermConstant.NPWP_PENYETOR, detail.getNpwpPenyetor());
//			task.setTerm(TermConstant.KODE_JENIS_SETOR, detail.getJenisSetoran());
//			task.setTerm(TermConstant.KODE_CABANG, "000"); // kantor pusat
//			task.setTerm(TermConstant.TAHUN_PAJAK, detail.getTahunPajak());
//			task.setTerm(TermConstant.KODE_MAP, detail.getJenisPajak());
//			task.setTerm(TermConstant.MASA_PAJAK_1, CommonUtil.displayMonthOnly(detail.getStartDatePbb()));
//			task.setTerm(TermConstant.MASA_PAJAK_2, CommonUtil.displayMonthOnly(detail.getEndDatePbb()));
//			task.setTerm(TermConstant.URAIAN_SSP, detail.getUraianSsp());
//			task.setTerm(TermConstant.KODE_KPP_SSP, detail.getKodeKkpSsp());
//			task.setTerm(TermConstant.NO_SK, detail.getNoSk());
//			task.setTerm(TermConstant.NOP, detail.getNop());
//			task.setTerm(TermConstant.NIK, detail.getNik());
//			task.setTerm(TermConstant.JUMLAH_SETOR, detail.getJumlahSetor());
//			task.setTerm(TermConstant.ID_BILLING, detail.getIdBilling());
//			
//			UserData userData = userDataService.findUserById(header.getApprovedBy());
//			task.setTerm(TermConstant.LOGIN_USER, userData.getUserCode());
//			task.setTerm(TermConstant.BRANCH_CODE, userData.getBranchId());
//			LOG.debug("createIDBillingPaymentInq userData: {}", userData);
//
//			task.setResultCode(ResultCode.SUCCESS_CODE);
//			task.setState(FUND_TRANSFER_INQUIRY);
////			task.setTrxValue(detail.getAmount());
////			task.setTerm(TermConstant.AMOUNT, detail.getAmount());
//			LOG.debug("createIDBillingPaymentInq TransactionTO: {}", task);
//			task = isoSddBtiManager.processDistribution(task);
//			LOG.debug("Hasil sendTransactionToHost: {}", task);
//			detail.setHostRc(task.getResultCode());
//			detail.setHostRefNo(task.getClientRef());
//			detail.setBit48(task.getTerm(TermConstant.BIT48));
//			LOG.debug("Hasil createIDBillingPaymentInq: {}", task);
//			createIdBillingPaymentVO.setRc(task.getResultCode());
//		} catch (Exception e) {
//			LOG.warn("Exception createIDBillingPaymentInq for " + task, e);
//		}
//		LOG.debug("Hasil createIDBillingPaymentInq: {}", createIdBillingPaymentVO);
//		return createIdBillingPaymentVO;
//	}
//
//	public CreateIdBillingVO createIDBilling(DistMakerVO distMakerVO, DistDetailVO detail) {
//		CreateIdBillingVO createIdBillingVO = new CreateIdBillingVO();
//		TransactionTO task = new TransactionTO();
//		try {
//			// create TransactionTO
//			String msgLogNo = btiSeqGen.getNextSequence();
//			long startTime = timeService.getCurrentTimemillis();
//			task.setMsgLogNo(msgLogNo);
//			task.setTrxCode(trxCodeCreateIdBilling);
//			task.setReceivedTime(startTime);
//			task.setPhoneNo(CommonUtil.formatPhoneIntl(detail.getPhoneNo()));
//			task.setChannelType(channel);
//			task.setSmiInput(channel);
////			task.setMessageInput(maskedMsg);
////			task.setClientRef(gwRef);
////			Lookup lookup = lookupService.findLookupByCatValue(LookupService.CAT_BANK_CODE,detail.getBankCode());
//
//			task.setTerm(TermConstant.DSAC, detail.getAccNumber());
//			task.setTerm(TermConstant.DSAC_NAME, detail.getAccName());
//			task.setTerm(TermConstant.SRAC, distMakerVO.getSracNo());
//			task.setTerm(TermConstant.SRAC_NAME, distMakerVO.getSracName());
////			task.setTerm(TermConstant.BANK_CODE, detail.getBankCode());
////			task.setTerm(TermConstant.BANK_NAME, lookup.getLookupDesc());
//			task.setTerm(TermConstant.IS_NON_NPWP, "0");
//			if(detail.isNonNpwp()) {
//				task.setTerm(TermConstant.IS_NON_NPWP, "1");
//			}
//			task.setTerm(TermConstant.NPWP_SSP, detail.getNpwp());
//			task.setTerm(TermConstant.NPWP_PENYETOR, detail.getNpwpPenyetor());
//			task.setTerm(TermConstant.KODE_JENIS_SETOR, detail.getJenisSetoran());
//			task.setTerm(TermConstant.KODE_CABANG, "000"); // kantor pusat
//			task.setTerm(TermConstant.TAHUN_PAJAK, detail.getTahunPajak());
//			task.setTerm(TermConstant.KODE_MAP, detail.getJenisPajak());
//			task.setTerm(TermConstant.MASA_PAJAK_1, CommonUtil.displayMonthOnly(detail.getStartDatePbb()));
//			task.setTerm(TermConstant.MASA_PAJAK_2, CommonUtil.displayMonthOnly(detail.getEndDatePbb()));
//			task.setTerm(TermConstant.URAIAN_SSP, detail.getUraianSsp());
//			task.setTerm(TermConstant.KODE_KPP_SSP, detail.getKodeKkpSsp());
//			task.setTerm(TermConstant.NO_SK, detail.getNoSk());
//			task.setTerm(TermConstant.NOP, detail.getNop());
//			task.setTerm(TermConstant.JUMLAH_SETOR, detail.getJumlahSetor());
//			
//			task.setTerm(TermConstant.NAMA_WP, detail.getNama());
//			task.setTerm(TermConstant.ALAMAT_WP, detail.getAlamat());
//			task.setTerm(TermConstant.KOTA_WP, detail.getKotaWp());
//			task.setTerm(TermConstant.NIK, detail.getNik());
//
//			task.setResultCode(ResultCode.SUCCESS_CODE);
//			task.setState(FUND_TRANSFER_INQUIRY);
//			task.setTrxValue(detail.getAmount());
//			task.setTerm(TermConstant.AMOUNT, detail.getAmount());
//			LOG.debug("createIDBilling TransactionTO: {}", task);
//			task = isoDjpBtiManager.processDistribution(task);
//			LOG.debug("Hasil Create Id Billing: {}", task);
//			createIdBillingVO.setRc(task.getResultCode());
//			String rcMessage = messageSource.getMessage("rc." + task.getResultCode(), null, null);
//			createIdBillingVO.setRcMessage(rcMessage);
//			createIdBillingVO.setIdBilling(task.getTerm(TermConstant.ID_BILLING));
//			createIdBillingVO.setNamaWp(task.getTerm(TermConstant.NAMA_WP));
//			createIdBillingVO.setAlamatWp(task.getTerm(TermConstant.ALAMAT_WP));
//			createIdBillingVO.setMasaKadaluarsa(task.getTerm(TermConstant.MASA_KADALUARSA_BILLING));
//		} catch (Exception e) {
//			LOG.warn("Exception createIDBilling for " + task, e);
//		}
//		LOG.debug("Hasil Create Id Billing: {}", createIdBillingVO);
//		return createIdBillingVO;
//	}
//
//	public void sendTransactionToHost(DistributionHeader header, DistributionDetail detail, int state) {
//		TransactionTO task = new TransactionTO();
//		try {
//			// create TransactionTO
//			LOG.debug("sendTransactionToHost: {}", header);
//			String msgLogNo = btiSeqGen.getNextSequence();
//			long startTime = timeService.getCurrentTimemillis();
//			task.setMsgLogNo(msgLogNo);
//			task.setTrxCode(trxCode);
//			task.setReceivedTime(startTime);
//			task.setPhoneNo(CommonUtil.formatPhoneIntl(detail.getPhoneNo()));
//			task.setChannelType(channel);
//			task.setSmiInput(channel);
//			Lookup lookup = lookupService.findLookupByCatValue(LookupService.CAT_BANK_CODE, detail.getBankCode());
//
//			task.setTerm(TermConstant.DSAC, detail.getAccountNo());
//			task.setTerm(TermConstant.DSAC_NAME, detail.getAccountName());
//			task.setTerm(TermConstant.SRAC, header.getSracNumber());
//			task.setTerm(TermConstant.SRAC_NAME, header.getSracName());
//			task.setTerm(TermConstant.BANK_CODE, detail.getBankCode());
//			task.setTerm(TermConstant.BANK_NAME, lookup.getLookupDesc());
//			task.setResultCode(ResultCode.SUCCESS_CODE);
//
//			task.setState(FUND_TRANSFER_INQUIRY);
//			task.setTrxValue(detail.getMoneyValue());
//			task.setTerm(TermConstant.AMOUNT, detail.getMoneyValue());
//			LOG.debug("request inquery: {}", task);
//			task = isoBtiManager.processDistribution(task);
//			LOG.debug("Hasil inquery: {}", task);
//			detail.setHostRc(task.getResultCode());
//			detail.setHostRefNo(task.getClientRef());
//			LOG.debug("request state: {}", state);
//			if (FUND_TRANSFER_SETTLEMENT == state) {
//				if ("0".equalsIgnoreCase(task.getResultCode())) {
//					LOG.debug("request settle: {}", task);
//					task.setState(FUND_TRANSFER_SETTLEMENT);
//					task = isoBtiManager.processDistribution(task);
//					LOG.debug("Hasil settle: {}", task);
//					detail.setHostRc(task.getResultCode());
////					detail.setHostRefNo(task.getClientRef());
//					detail.setHostRefNo(task.getTerm(TermConstant.BIT37));
//				}
//			}
//		} catch (Exception e) {
//			LOG.warn("Exception sendTransactionToHost for " + task, e);
//		}
//	}
//
//	public CheckAccountVO checkAccountForNo(DistMakerVO distMakerVO, DistDetailVO detail) {
//		CheckAccountVO accountVO = new CheckAccountVO();
//		TransactionTO task = new TransactionTO();
//		try {
//			// create TransactionTO
//			String msgLogNo = btiSeqGen.getNextSequence();
//			long startTime = timeService.getCurrentTimemillis();
//			task.setMsgLogNo(msgLogNo);
//			task.setTrxCode(trxCode);
//			task.setReceivedTime(startTime);
//			task.setPhoneNo(CommonUtil.formatPhoneIntl(detail.getPhoneNo()));
//			task.setChannelType(channel);
//			task.setSmiInput(channel);
////			task.setMessageInput(maskedMsg);
////			task.setClientRef(gwRef);
//			Lookup lookup = lookupService.findLookupByCatValue(LookupService.CAT_BANK_CODE, detail.getBankCode());
//
//			task.setTerm(TermConstant.DSAC, detail.getAccNumber());
//			task.setTerm(TermConstant.DSAC_NAME, detail.getAccName());
//			task.setTerm(TermConstant.SRAC, distMakerVO.getSracNo());
//			task.setTerm(TermConstant.SRAC_NAME, distMakerVO.getSracName());
//			task.setTerm(TermConstant.BANK_CODE, detail.getBankCode());
//			task.setTerm(TermConstant.BANK_NAME, lookup.getLookupDesc());
//			task.setResultCode(ResultCode.SUCCESS_CODE);
//			task.setState(FUND_TRANSFER_INQUIRY);
//			task.setTrxValue(detail.getAmount());
//			task.setTerm(TermConstant.AMOUNT, detail.getAmount());
//			LOG.debug("checkAccountForNo TransactionTO: {}", task);
//			task = isoBtiManager.processDistribution(task);
//			LOG.debug("Hasil Cek Account: {}", task);
//			accountVO.setRc(task.getResultCode());
//			if ("0".equals(task.getResultCode())) {
//				accountVO.setAccountStatus(WebConstants.ACC_STATUS_ACTIVE);
//			}
//			accountVO.setAccountName(task.getTerm(TermConstant.DSAC_NAME));
//			accountVO.setFullName(task.getTerm(TermConstant.DSAC_NAME));
//			accountVO.setAccountNo(detail.getAccNumber());
//			accountVO.setAccountType("Undefined");
//			accountVO.setRcDisplay(task.getResultCode());
//		} catch (Exception e) {
//			LOG.warn("Exception checkAccountForNo for " + task, e);
//		}
//		LOG.debug("Hasil Cek Account: {}", accountVO);
//		return accountVO;
//	}
//
////	@Override
////	public void checkBalanceForAccount(SourceAccountVO sourceAccount) {
////		LOG.debug("checkBalanceForAccount: {}", sourceAccount);
////		try {
////			int maxInterval = settingService.getSettingAsInt(SettingService.SETTING_CHECK_BALANCE_INTERVAL) * 1000;
////			long delta = System.currentTimeMillis() - sourceAccount.getLastChecked();
////			if (delta <= maxInterval) {
////				Date dtLastChecked = new Date(sourceAccount.getLastChecked());
////				LOG.warn("No need to check balance, last checked on {}", dtLastChecked);
////				return;
////			}
////			Map<String, String> mapData = settingService.getSettingAsJson(SettingService.SETTING_COLLEGA_PARAM_DATA);
////			String userGtw = mapData.get(SettingService.COLLEGA_PARAM_USERGTW);
////			String channelId = mapData.get(SettingService.COLLEGA_PARAM_CHANNEL);
////			
////			String serverUrl = settingService.getSettingAsString(SettingService.SETTING_COLLEGA_URL);
////			if (StringUtils.isEmpty(serverUrl)) {
////				LOG.warn("No collegaUrl, setting id {} is defined. NOT SENDING REQUEST",
////						SettingService.SETTING_COLLEGA_URL);
////				return;
////			}
////			// compose request
////			Date now = timeService.getCurrentTime();
////			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
////			sdfDate.setTimeZone(TimeZone.getTimeZone("GMT+8"));
////			
////			SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
////			sdfTime.setTimeZone(TimeZone.getTimeZone("GMT+8"));
////			
////			String trxDate = sdfDate.format(now);
////			String trxHour = sdfTime.format(now);
////			
////			AccBalanceInqRequestVO requestVO = new AccBalanceInqRequestVO();
////			String reqId = "00002";
////			String authKey = composeHmac(reqId, now);
////			
////			requestVO.setAuthKey(authKey); // required
////			requestVO.setReqId(reqId); // required
////			requestVO.setTxDate(trxDate); // required yyyymmdd
////			requestVO.setTxHour(trxHour); // required hhmmss
////			requestVO.setUserGtw(userGtw); // required
////			requestVO.setChannelId(channelId); // required
////			requestVO.setAccnbr(sourceAccount.getSracNumber());
////			requestVO.setFlgSaldo(1); // required
////			requestVO.setFlgNsb(0); // required
////			
////			String jsonRequest = objectMapper.writeValueAsString(requestVO);
////			LOG.debug("CheckBalance with request: {}", jsonRequest);
////			
////			// send request
////			ResponseData responseData = transmitterAgent.sendPostMessageWithEntity(serverUrl, jsonRequest);
////			sourceAccount.setBalanceRc(ResultCode.ERR_FAILED_CHECK);
////			if (responseData.getResultCode() == 0) {
////				// success, parsing response
////				AccBalanceInqResponseVO resp = objectMapper.readValue(responseData.getMsgToUser(), AccBalanceInqResponseVO.class);
////				if (HOST_SUCCESS_CODE.equals(resp.getrCode())) {
////					String bal = StringUtils.trim(resp.getResult().get("SALDO_EFEKTIF")); //SALDO_AKHIR"));
////					if (bal.startsWith("-")) {
////						bal = bal.substring(1);
////					}
////					Double dblBalance = Double.parseDouble(bal);
////					sourceAccount.setBalanceRc("0");
////					sourceAccount.setSracBalance(dblBalance);
////					sourceAccount.setLastChecked(System.currentTimeMillis());
////				} else {
////					LOG.warn("Failed checkBalance with response: {}", responseData.getMsgToUser());
////				}			
////			} else {
////				LOG.warn("Failed checkBalance with message: {}", responseData);
////			}
////			
////		} catch (Exception e) {
////			LOG.warn("Exception checkBalance for " + sourceAccount, e);
////		}
////		
////	}
////	
////	@Override
////	public AccStatementVO checkStatementForAccount(SourceAccountVO sourceAccount, Date startDate, Date endDate) {
////		LOG.debug("checkStatementForAccount: {}", sourceAccount);
////		AccStatementVO vo = new AccStatementVO();
////		vo.setStatementRc(ResultCode.ERR_FAILED_CHECK);
////		vo.setMessage("System error. Please contact administrator");
////		
////		try {
////			Map<String, String> mapData = settingService.getSettingAsJson(SettingService.SETTING_COLLEGA_PARAM_DATA);
////			String userGtw = mapData.get(SettingService.COLLEGA_PARAM_USERGTW);
////			String channelId = mapData.get(SettingService.COLLEGA_PARAM_CHANNEL);
////			
////			String serverUrl = settingService.getSettingAsString(SettingService.SETTING_COLLEGA_URL);
////			if (StringUtils.isEmpty(serverUrl)) {
////				LOG.warn("No collegaUrl, setting id {} is defined. NOT SENDING REQUEST",
////						SettingService.SETTING_COLLEGA_URL);
////				vo.setStatementRc(ResultCode.ERR_MISSING_DATA);
////				return vo;
////			}
////			// compose request
////			Date now = timeService.getCurrentTime();
////			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
////			sdfDate.setTimeZone(TimeZone.getTimeZone("GMT+8"));
////			
////			SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
////			sdfTime.setTimeZone(TimeZone.getTimeZone("GMT+8"));
////			
////			String trxDate = sdfDate.format(now);
////			String trxHour = sdfTime.format(now);
////			
////			SimpleDateFormat sdfTxDate = new SimpleDateFormat("yyyy-MM-dd");
////			AccountStatementRequestVO requestVO = new AccountStatementRequestVO();
////			String reqId = "00008";
////			String authKey = composeHmac(reqId, now);
////			
////			requestVO.setAuthKey(authKey); // required
////			requestVO.setReqId(reqId); // required
////			requestVO.setTxDate(trxDate); // required yyyymmdd
////			requestVO.setTxHour(trxHour); // required hhmmss
////			requestVO.setUserGtw(userGtw); // required
////			requestVO.setChannelId(channelId); // required
////			requestVO.setAccNbr(sourceAccount.getSracNumber());
////			requestVO.setStartDate(sdfTxDate.format(startDate)); // required
////			requestVO.setEndDate(sdfTxDate.format(endDate)); // required
////			
////			String jsonRequest = objectMapper.writeValueAsString(requestVO);
////			LOG.debug("checkStatementForAccount with request: {}", jsonRequest);
////			
////			// send request
////			ResponseData responseData = transmitterAgent.sendPostMessageWithEntity(serverUrl, jsonRequest);
////			if (responseData.getResultCode() == 0) {
////				// success, parsing response
////				AccountStatementResponseVO resp = objectMapper.readValue(responseData.getMsgToUser(), AccountStatementResponseVO.class);
////				if (HOST_SUCCESS_CODE.equals(resp.getrCode())) {
////					vo.setStatementRc("0"); // resp.getrCode());
////					vo.setMessage(resp.getMessage());
////					vo.setStartBalance(Double.parseDouble(resp.getSaldoAwal()));
////					vo.setEndBalance(Double.parseDouble(resp.getSaldoAkhir()));
////					
////					SimpleDateFormat sdfTxSettle = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
////					List<Map<String, String>> listMapDetail = resp.getResult();
////					for (Map<String, String> mapDetail: listMapDetail) {
////						AccStatementDetailVO detailVO = new AccStatementDetailVO();
////						detailVO.setTxCode(mapDetail.get("TXCODE")); // TXCODE : String Transaction Code
////						detailVO.setTxDate(sdfTxDate.parse(mapDetail.get("TXDATE"))); // TXDATE : Date Transaction Date(yyyy-MM-dd )
////						detailVO.setTxAmount(Double.parseDouble(mapDetail.get("TXAMT"))); // TXAMT : Float Transaction Amount
////						detailVO.setTxDateSettle(sdfTxSettle.parse(mapDetail.get("TXDTSTLMN")));  // TXDTSTLMN : Date Transaction Settlement Date (yyyy-MM-dd HH:mm:Sss )
////						detailVO.setAccNumber(mapDetail.get("ACCNBR"));;  // ACCNBR : String Account Number
////						detailVO.setTxId(mapDetail.get("TXID"));  // TXID : String Transaction ID
////						detailVO.setDebetCredit(Integer.parseInt(mapDetail.get("DBCR")));; //DBCR : Integer Transaction Type (0 – Debit, 1- Credit)
////						detailVO.setTxMsg(mapDetail.get("TXMSG"));  // TXMSG : String Transaction Message
////						detailVO.setTxBranch(mapDetail.get("TXBRANCH"));  // TXBRANCH : String Transaction Branch Location
////						detailVO.setTxCurrency(mapDetail.get("TXCCY"));  // TXCCY : String Transaction Currency
////						detailVO.setUserId(mapDetail.get("USERID"));;  // USERID : String
////						vo.getListDetail().add(detailVO);
////					}
////				} else {
////					LOG.warn("Failed checkStatementForAccount with response: {}", responseData.getMsgToUser());
////					vo.setStatementRc(resp.getrCode());
////					vo.setMessage(resp.getMessage());
////				}			
////			} else {
////				LOG.warn("Failed checkStatementForAccount with message: {}", responseData);
////			}
////			
////		} catch (Exception e) {
////			LOG.warn("Exception checkStatementForAccount for " + sourceAccount, e);
////		}
////		
////		return vo;
////	}
////	
////	@Override
////	public void sendTransactionToHost(DistributionHeader header, DistributionDetail detail) {
////		LOG.debug("sendTransactionToHost: {}", detail);
////		try {
////			String serverUrl = settingService.getSettingAsString(SettingService.SETTING_COLLEGA_URL);
////			if (StringUtils.isEmpty(serverUrl)) {
////				LOG.warn("No collegaUrl, setting id {} is defined. NOT SENDING REQUEST",
////						SettingService.SETTING_COLLEGA_URL);
////				return;
////			}
////			Map<String, String> mapData = settingService.getSettingAsJson(SettingService.SETTING_COLLEGA_PARAM_DATA);
////			String userGtw = mapData.get(SettingService.COLLEGA_PARAM_USERGTW);
////			String channelId = mapData.get(SettingService.COLLEGA_PARAM_CHANNEL);
////			String corpId = mapData.get(SettingService.COLLEGA_PARAM_CORPID);
////			String prodId = mapData.get(SettingService.COLLEGA_PARAM_PRODID);
////			String branch = mapData.get(SettingService.COLLEGA_PARAM_BRANCH);
////			
////			Date now = timeService.getCurrentTime();
////			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
////			sdfDate.setTimeZone(TimeZone.getTimeZone("GMT+8"));
////			
////			SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
////			sdfTime.setTimeZone(TimeZone.getTimeZone("GMT+8"));
////			
////			SimpleDateFormat sdfDate2 = new SimpleDateFormat("dd-MM-yyyy");
////			sdfDate2.setTimeZone(TimeZone.getTimeZone("GMT+8"));
////			
////			String reqId = "00005";
////			String authKey = composeHmac(reqId, now);
////			
////			String trxDate = sdfDate.format(now);
////			String trxHour = sdfTime.format(now);
////			String date = sdfDate2.format(now);
////			
////			FundTransferRequestVO fundTransferRequestVO = new FundTransferRequestVO();
////			
////			fundTransferRequestVO.setAuthKey(authKey); // required
////			fundTransferRequestVO.setReqId(reqId); // required
////			fundTransferRequestVO.setUserGtw(userGtw); // required
////			fundTransferRequestVO.setChannelId(channelId); // required
////			fundTransferRequestVO.setTxDate(trxDate); // required yyyymmdd
////			fundTransferRequestVO.setTxHour(trxHour); // required hhmmss
////			
////			fundTransferRequestVO.setCorpId(corpId);
////			fundTransferRequestVO.setProdId(prodId);
////			fundTransferRequestVO.setDate(date);
////			fundTransferRequestVO.setDate_rk(date);
////			fundTransferRequestVO.setBranchId(branch);
////			fundTransferRequestVO.setTxCcy("IDR");
////			
////			int numOfAcc = 2;
////			fundTransferRequestVO.setNbrOfAcc(Integer.toString(numOfAcc));
////			fundTransferRequestVO.setProsesId("0005");
////			fundTransferRequestVO.setUserId(userGtw); //header.getApprovedUserCode());
////			
////			fundTransferRequestVO.setSpvId("");
////			fundTransferRequestVO.setRevSts("0"); // 0: not reversal, 1: reversal
////			fundTransferRequestVO.setTxType("O"); // O: Overbooking
////			fundTransferRequestVO.setRefAcc(header.getSracNumber()); // SourceAcc // task.getTermAsString(TermConstant.SRAC));
////			
////			String fileNameOnly = FilenameUtils.getBaseName(header.getFileData());
////			String txMsg = "KNT-" + fileNameOnly + "-" + detail.getAccountName();
////			List<Map<String, Object>> listParam = new ArrayList<Map<String, Object>>();
////			
////			String txId = collegaSeqGen.getNextSequence();
////			// 0: Debet, 1: Credit
////			// debet first
////			Map<String, Object> debetVO = new HashMap<String, Object>();
////			debetVO.put("accNbr", (String) header.getSracNumber());
////			debetVO.put("txMsg", (String) txMsg);
////			debetVO.put("txId", (String) txId);
////			debetVO.put("isFee", (Integer) 0);
////	//		debetVO.put("chqBkNbr", (String) ""); // minta di hide ramadhian noor
////			debetVO.put("pageNbr", (Integer) 0);
////			debetVO.put("txDtDoc", (String) "");
////			debetVO.put("refAcc", (String) "");
////			debetVO.put("valMinBal", (Integer) 1);
////			// txCode 199=Penarikan, 299=Setoran
////			debetVO.put("dbCr", (String) "0");
////			debetVO.put("txCode", (String) "199");
////			debetVO.put("txAmt", (Double) detail.getMoneyValue());
////			listParam.add(debetVO);
////			
////			// then credit 
////			Map<String, Object> creditVO = new HashMap<String, Object>();
////			creditVO.put("accNbr", (String) detail.getAccountNo());
////			creditVO.put("txMsg", (String) txMsg);
////			creditVO.put("txId", (String) txId);
////			creditVO.put("isFee", (Integer) 0);
////	//		creditVO.put("chqBkNbr", (String) ""); // minta di hide ramadhian noor
////			creditVO.put("pageNbr", (Integer) 0);
////			creditVO.put("txDtDoc", (String) "");
////			creditVO.put("refAcc", (String) "");
////			creditVO.put("valMinBal", (Integer) 1);
////			// txCode 199=Penarikan, 299=Setoran
////			creditVO.put("dbCr", (String) "1");
////			creditVO.put("txCode", (String) "299");
////			creditVO.put("txAmt", (Double) detail.getMoneyValue());
////			listParam.add(creditVO);
////			
////			NumberFormat nf = new DecimalFormat("###0");
////			String totalAmount = nf.format(detail.getMoneyValue());
////			fundTransferRequestVO.setTotalAmount(totalAmount);
////			fundTransferRequestVO.setParam(listParam);
////			
////			String jsonRequest = objectMapper.writeValueAsString(fundTransferRequestVO);
////			LOG.debug("Transaction with request: {}", jsonRequest);
////			
////			// send request
////			ResponseData responseData = transmitterAgent.sendPostMessageWithEntity(serverUrl, jsonRequest);
////			if (responseData.getResultCode() == 0) {
////				// success, parsing response
////				FundTransferResponseVO resp = objectMapper.readValue(responseData.getMsgToUser(), FundTransferResponseVO.class);
////				if (HOST_SUCCESS_CODE.equals(resp.getrCode())) {
////					String hostRef = StringUtils.trim(resp.getResult().get("TXID"));
////					detail.setHostRc("0");
////					detail.setHostMessage(resp.getMessage());
////					detail.setHostRefNo(hostRef);
////				} else {
////					LOG.warn("Failed checkBalance with response: {}", responseData.getMsgToUser());
////					detail.setHostRc(resp.getrCode());
////					detail.setHostMessage(resp.getMessage());
////				}			
////			} else {
////				LOG.warn("Failed checkBalance with message: {}", responseData);
////				detail.setHostRc(ResultCode.ERR_UNABLE_CONNECT_HOST);
////				detail.setHostMessage("Error when connecting to HOST: " + responseData.getSysMessage());
////			}
////		} catch (Exception e) {
////			LOG.warn("Exception sendTransactionToHost for " + detail, e);
////			detail.setHostRc(ResultCode.ERR_UNKNOWN);
////			detail.setHostMessage("Error processing data, error: " + e.getMessage());
////		}
////	}
////	
////	private String composeHmac(String reqId, Date reqTime) {
////		String ipAddress = settingService.getSettingAsString(SettingService.SETTING_LOCAL_IP_ADDRESS);
////		String authSecretKey = settingService.getSettingAsString(SettingService.SETTING_COLLEGA_AUTH_KEY);
////		
////		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
////		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8")); // timezone for sulteng
////		
////		String formatDt = sdf.format(reqTime);
////		
//////		String plainAuthKey = reqId + ipAddress + ISODate.formatDate(now, "yyyy-MM-ddHH:mm:ss");
//////		String plainAuthKey = reqId + ipAddress + ISODate.formatDate(formatWIT(now), "yyyy-MM-ddHH:mm");
//////		String plainAuthKey = reqId + ipAddress + ISODate.formatDate(formatSulteng(now), "yyyy-MM-ddHH:mm:ss");
////		String plainAuthKey = reqId + ipAddress + formatDt;
////		LOG.debug("Plain authKey: {}, secret key: {}", plainAuthKey, authSecretKey);
////		return hmacDigest(plainAuthKey, authSecretKey);
////	}
////	
////	private String hmacDigest(String msg, String keyString) {
////		String digest = null;
////		try {
////			SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacSHA1");
////			Mac mac = Mac.getInstance("HmacSHA1");
////			mac.init(key);
////
////			byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));
////
////			StringBuffer hash = new StringBuffer();
////			for (int i = 0; i < bytes.length; i++) 
////			{
////				String hex = Integer.toHexString(0xFF & bytes[i]);
////				if (hex.length() == 1) 
////				{
////					hash.append('0');
////				}
////				hash.append(hex);
////			}
////			digest = hash.toString();
////		} catch (Exception e) {
////			LOG.warn("Exception in hmacDigest " + msg, e);
////		}
////		return digest;
////	}
////	
////	public void setTransmitterAgent(HttpTransmitterAgent transmitterAgent) {
////		this.transmitterAgent = transmitterAgent;
////	}
//
//}
