//package id.co.emobile.samba.web.service;
//
//import java.text.DecimalFormat;
//import java.text.NumberFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//
//import id.co.emobile.samba.web.data.AccStatementParamVO;
//import id.co.emobile.samba.web.data.AccStatementVO;
//import id.co.emobile.samba.web.data.CheckAccountVO;
//import id.co.emobile.samba.web.data.CreateIdBillingVO;
//import id.co.emobile.samba.web.data.DistDetailVO;
//import id.co.emobile.samba.web.data.DistMakerVO;
//import id.co.emobile.samba.web.data.ResponseData;
//import id.co.emobile.samba.web.data.ResultCode;
//import id.co.emobile.samba.web.data.SourceAccountVO;
//import id.co.emobile.samba.web.data.WebConstants;
//import id.co.emobile.samba.web.entity.DistributionDetail;
//import id.co.emobile.samba.web.entity.DistributionHeader;
//import id.co.emobile.samba.web.entity.UserData;
//import id.co.emobile.samba.web.http.HttpTransmitterAgent;
//import id.co.emobile.samba.web.mapper.DistributionDetailMapper;
//import id.co.emobile.samba.web.mapper.DistributionHeaderMapper;
//import id.co.emobile.samba.web.utils.CipherUtils;
//import id.co.emobile.samba.web.utils.CommonUtil;
//
//public class SmsSenderService {
//	private static final Logger LOG = LoggerFactory.getLogger(SmsSenderService.class);
//
////	private HttpClient httpClient;
////	private int timeout = 10000;  // wait for 10s
////	private PoolingHttpClientConnectionManager cm;
//	private ExecutorService executor;
//	private int agentCount = 20;
//
//	@Autowired
//	private SettingService settingService;
//
//	@Autowired
//	private MessageSource messageSource;
//
//	@Autowired
//	private DistributionHeaderMapper distHeaderMapper;
//
//	@Autowired
//	private DistributionDetailMapper distDetailMapper;
//
//	@Autowired
//	private AppsTimeService timeService;
//
//	@Autowired
//	private UserDataService userDataService;
//
//	@Autowired
//	private SourceAccountService sourceAccountService;
//
//	private HttpTransmitterAgent transmitterAgent;
//	private BaseTrxService baseTrxService;
//
//	@Autowired
//	private TrxIsoService trxIsoService;
//
//	private String bit48;
//
//	public void validateBillPbb(DistMakerVO header, DistDetailVO detail) {
//		CreateIdBillingVO createIdBillingVO = trxIsoService.createIDBilling(header, detail);
//		LOG.info(String.format("validateBillPbb data: %s", createIdBillingVO));
//		if (ResultCode.SUCCESS_CODE.equals(createIdBillingVO.getRc())) {
////			String inputName = detail.getNama().replaceAll("\\s+", "");
//			String namaWp = createIdBillingVO.getNamaWp();
//			String alamatWp = createIdBillingVO.getAlamatWp();
////			if (!inputName.equalsIgnoreCase(checkedName)) {
////				detail.setCheckSuccess(false);
////				detail.setValidationDisplay("Mismatched Nama Wp: " + createIdBillingVO.getNamaWp());
////				return;
////			}
//			detail.setNama(namaWp);
//			detail.setAlamat(alamatWp);
//
//			detail.setCheckSuccess(true);
//			detail.setValidationDisplay("Create Id Billing Success: " + createIdBillingVO.getIdBilling());
//			detail.setIdBilling(createIdBillingVO.getIdBilling());
//			detail.setMasaAktifBilling(createIdBillingVO.getMasaKadaluarsa());
//			LOG.info(String.format("detail data: %s", detail));
//		} else {
//			detail.setCheckSuccess(false);
//			detail.setValidationDisplay("Create Id Billing Gagal : " + createIdBillingVO.getRcMessage());
//
//		}
//	}
//
//	public void validateAccountForIso(DistMakerVO header, DistDetailVO detail) {
//		CheckAccountVO accountVO = trxIsoService.checkAccountForNo(header, detail);
//		if (ResultCode.ERR_NOT_CHECK.equals(accountVO.getRc())) {
//			detail.setCheckSuccess(true);
//			detail.setValidationDisplay("NOT Checked");
//			return;
//		}
//		if (ResultCode.ERR_FAILED_CHECK.equals(accountVO.getRc())) {
//			detail.setCheckSuccess(false);
//			detail.setValidationDisplay("Checked Failed");
//			return;
//		}
//		// validate status
//		if (accountVO.getAccountStatus() != WebConstants.ACC_STATUS_ACTIVE) {
//			detail.setCheckSuccess(false);
//			detail.setValidationDisplay(
//					"Status is " + accountVO.getAccountStatusDisplay() + "RC: " + "(" + accountVO.getRcDisplay() + ")");
//			return;
//		}
//		// validate name
//		String inputName = detail.getAccName().replaceAll("\\s+", "");
//		String checkedName = accountVO.getFullName().replaceAll("\\s+", "");
//		if (!inputName.equalsIgnoreCase(checkedName)) {
//			detail.setCheckSuccess(false);
//			detail.setValidationDisplay("Mismatched name: " + accountVO.getFullName());
//			return;
//		}
//		// all seems good
//		detail.setCheckSuccess(true);
//		detail.setValidationDisplay("Acc. Type: " + accountVO.getAccountType());
//	}
//
//	public void init() {
//		executor = Executors.newFixedThreadPool(agentCount);
//		LOG.info("SmsSenderService is started with agentCount: {}, trxService: {}", agentCount,
//				baseTrxService.getServiceName());
//	}
//
//	public void shutdown() {
//		try {
//			executor.shutdown();
//			executor.awaitTermination(5, TimeUnit.SECONDS);
//		} catch (Exception e) {
//			LOG.warn("Exception in shutdown", e);
//		}
//		LOG.info("SmsSenderService is shutdown");
//	}
//
//	public void sendSms(String phoneNo, String message, String sysLogNo, String trxCode) {
//		LOG.debug("sendSms to phoneNo {}, message {}, trxCode {}", phoneNo, message, trxCode);
//		try {
//			InternalSmsSender sender = new InternalSmsSender(phoneNo, message, sysLogNo, trxCode);
//			executor.execute(sender);
//		} catch (Exception e) {
//			LOG.warn("Exception in sendSms to " + phoneNo, e);
//		}
//	}
//
//	public void processDistribution(String sysLogNo, int approvedBy) {
//		LOG.debug("processDistribution for sysLogNo {}", sysLogNo);
//		try {
//			if (baseTrxService == null) {
//				LOG.warn("No TrxService is defined, Unable to processDistribution sysLogNo: {}", sysLogNo);
//				return;
//			}
//
//			InternalDistProcessOriginal proc = new InternalDistProcessOriginal(sysLogNo, approvedBy);
//			executor.execute(proc);
//		} catch (Exception e) {
//			LOG.warn("Exception in processDistribution for " + sysLogNo, e);
//		}
//	}
//
//	public void processDistributionSett(String sysLogNo, int approvedBy, int state) throws SambaWebException {
//		LOG.debug("processDistributionSett for sysLogNo {}", sysLogNo);
//		try {
//			if (baseTrxService == null) {
//				LOG.warn("No TrxService is defined, Unable to processDistribution sysLogNo: {}", sysLogNo);
//				return;
//			}
//
//			InternalDistProcess proc = new InternalDistProcess(sysLogNo, approvedBy, state);
//			proc.run();
//			List<DistributionDetail> listDetail = distDetailMapper.findDistributionDetailBySysLogNo(sysLogNo);
//			for (DistributionDetail detail : listDetail) {
//				if (!"0".equals(detail.getHostRc())) {
//					String hostRc = detail.getHostRc();
//					int hostRcc = 9999;
//					try {
//						hostRcc = Integer.parseInt(hostRc);
//					} catch (Exception e) {
//					}
//
//					LOG.warn("Error while processDistributionSett with host rc : " + hostRcc);
//					throw new SambaWebException(hostRcc, detail.getHostMessage());
//				}
//			}
////			executor.execute(proc);
//		} catch (SambaWebException fae) {
//			throw fae;
//		} catch (Exception e) {
//			LOG.warn("Exception in processDistribution for " + sysLogNo, e);
//			throw e;
//		}
//	}
//
//	public Map<String, Object> processDistributionPajak(String sysLogNo, int approvedBy, int state, String bit48)
//			throws SambaWebException {
//		LOG.debug("processDistributionPajak for sysLogNo {} and state {} and bit48 {}", sysLogNo, state, bit48);
//		Map<String, Object> result = new HashMap<String, Object>();
//		try {
//			if (baseTrxService == null) {
//				LOG.warn("No TrxService is defined, Unable to processDistributionPajak sysLogNo: {}", sysLogNo);
//				return result;
//			}
//
//			InternalDistProcessPajak proc = new InternalDistProcessPajak(sysLogNo, approvedBy, state, bit48);
//			proc.run();
//			List<DistributionDetail> listDetail = distDetailMapper.findDistributionDetailBySysLogNo(sysLogNo);
//			for (DistributionDetail detail : listDetail) {
//				if (!"0".equals(detail.getHostRc())) {
//					LOG.warn("Error while debet kredit with host rc : " + detail.getHostRc());
//					throw new SambaWebException(Integer.parseInt(detail.getHostRc()), detail.getHostMessage());
//				}
//			}
//			LOG.warn("this bit48 : " + this.bit48);
//			LOG.warn("bit48 local : " + bit48);
//			result.put(WebConstants.WEB_PARAM_BIT48_KEY, this.bit48);
////			executor.execute(proc);
//		} catch (SambaWebException swe) {
//			throw swe;
//		} catch (Exception e) {
//			LOG.warn("Exception in processDistributionPajak for " + sysLogNo, e);
//		}
//		LOG.debug("bit48 {}", result);
//		return result;
//	}
//
//	public void checkBalanceForAccount(SourceAccountVO sourceAccount) {
//		if (baseTrxService == null) {
//			LOG.warn("No TrxService is defined, Unable to checkBalanceForAccount: {}", sourceAccount);
//			return;
//		}
//		baseTrxService.checkBalanceForAccount(sourceAccount);
//	}
//
//	public void validateAccountFor(DistDetailVO detail) {
//		CheckAccountVO accountVO = baseTrxService.checkAccountForNo(detail.getAccNumber());
//		if (ResultCode.ERR_NOT_CHECK.equals(accountVO.getRc())) {
//			detail.setCheckSuccess(true);
//			detail.setValidationDisplay("NOT Checked");
//			return;
//		}
//		if (ResultCode.ERR_FAILED_CHECK.equals(accountVO.getRc())) {
//			detail.setCheckSuccess(false);
//			detail.setValidationDisplay("Checked Failed");
//			return;
//		}
//		// validate status
//		if (accountVO.getAccountStatus() != WebConstants.ACC_STATUS_ACTIVE) {
//			detail.setCheckSuccess(false);
//			detail.setValidationDisplay("Status is " + accountVO.getAccountStatusDisplay());
//			return;
//		}
//		// validate name
//		String inputName = detail.getAccName().replaceAll("\\s+", "");
//		String checkedName = accountVO.getFullName().replaceAll("\\s+", "");
//		if (!inputName.equalsIgnoreCase(checkedName)) {
//			detail.setCheckSuccess(false);
//			detail.setValidationDisplay("Mismatched name: " + accountVO.getFullName());
//			return;
//		}
//		// all seems good
//		detail.setCheckSuccess(true);
//		detail.setValidationDisplay("Acc. Type: " + accountVO.getAccountType());
//	}
//
//	public AccStatementVO checkStatementForAccountId(AccStatementParamVO paramVO) {
//		LOG.debug("checkStatementForAccountId: {}", paramVO);
//		try {
//			SourceAccountVO account = sourceAccountService.findSourceAccountById(paramVO.getAccountId());
//			if (account == null) {
//				LOG.warn("Unable to find sourceAccount with id {}", paramVO.getAccountId());
//				AccStatementVO vo = new AccStatementVO();
//				vo.setStatementRc(ResultCode.ERR_MISSING_DATA);
//				vo.setMessage("Source Account tidak ditemukan");
//				return vo;
//			}
//			if (paramVO.getStartDate() == null || paramVO.getEndDate() == null) {
//				LOG.warn("Missing parameter startDate or endDate");
//				AccStatementVO vo = new AccStatementVO();
//				vo.setStatementRc(ResultCode.ERR_MISSING_DATA);
//				vo.setMessage("Tanggal harus diisi");
//				return vo;
//			}
//			if (paramVO.getStartDate().after(paramVO.getEndDate())) {
//				LOG.warn("Parameter startDate {} is greater than endDate {}", paramVO.getStartDate(),
//						paramVO.getEndDate());
//				AccStatementVO vo = new AccStatementVO();
//				vo.setStatementRc(ResultCode.ERR_INVALID_DATA_CHECK);
//				vo.setMessage("Tanggal tidak valid");
//				return vo;
//			}
//			return baseTrxService.checkStatementForAccount(account, paramVO.getStartDate(), paramVO.getEndDate());
//		} catch (Exception e) {
//			LOG.warn("Exception checkStatementForAccountId: " + paramVO, e);
//			return null;
//		}
//	}
//
//	private String encrypt(String input, String key) {
//		if (StringUtils.isEmpty(key))
//			return input;
//		return CipherUtils.encryptDESede(input, key);
//	}
//
////	private String decrypt(String input, String key) {
////		if (StringUtils.isEmpty(key))
////			return input;
////		return CipherUtils.decryptDESede(input, key);
////	}
//
//	private class InternalSmsSender implements Runnable {
//		final String phoneNo;
//		final String message;
//		final String sysLogNo;
//		final String trxCode;
//
//		InternalSmsSender(String phoneNo, String message, String sysLogNo, String trxCode) {
//			this.phoneNo = phoneNo;
//			this.message = message;
//			this.sysLogNo = sysLogNo;
//			this.trxCode = trxCode;
//		}
//
//		@Override
//		public void run() {
//			try {
//				String serverUrl = settingService.getSettingAsString(SettingService.SETTING_SMS_GATEWAY_URL);
//				String encKey = settingService.getSettingAsString(SettingService.SETTING_SMS_GATEWAY_ENC_KEY);
//				if (StringUtils.isEmpty(serverUrl)) {
//					LOG.warn("No serverUrl is defined. NOT SENDING SMS");
//					return;
//				}
//				SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
//				String logno = sdf.format(new Date()) + CommonUtil.generateRandomPin(1);
//				List<NameValuePair> listParam = new ArrayList<NameValuePair>();
//
//				listParam.add(new BasicNameValuePair("p", phoneNo));
//				listParam.add(new BasicNameValuePair("g", ""));
//				listParam.add(new BasicNameValuePair("mmbsref", ""));
//				listParam.add(new BasicNameValuePair("rc", "0"));
//				listParam.add(new BasicNameValuePair("dsacname", ""));
//				listParam.add(new BasicNameValuePair("trx", trxCode));
//				listParam.add(new BasicNameValuePair("state", "99"));
//				listParam.add(new BasicNameValuePair("syslogno", sysLogNo));
//				listParam.add(new BasicNameValuePair("msglogno", logno));
//				listParam.add(new BasicNameValuePair("c", "SMS"));
//				String hexaMessage = CommonUtil.toHexString(message.getBytes());
//				listParam.add(new BasicNameValuePair("m", hexaMessage));
//
//				StringBuilder sb = new StringBuilder();
//				for (NameValuePair nvp : listParam)
//					sb.append(nvp.getName()).append("=").append(nvp.getValue()).append("&");
//				LOG.debug("Param Message Send Notif To Gateway -> " + sb.toString());
//				String encMessage = encrypt(sb.toString(), encKey);
//
//				List<NameValuePair> params = new ArrayList<NameValuePair>();
//				params.add(new BasicNameValuePair("x", encMessage));
//
//				ResponseData responseData = transmitterAgent.sendGetMessage(serverUrl, params);
//				if (responseData.getResultCode() == 0) {
//					LOG.debug("OK, Response: {}", responseData.getMsgToUser());
//				} else {
//					LOG.warn("Error sending sms: {}", responseData);
//				}
//
//			} catch (Exception e) {
//				LOG.warn("Exception in sending SMS to " + phoneNo, e);
//			}
//		}
//
//	} // InternalSmsSender
//
//	private class InternalDistProcessOriginal implements Runnable {
//		final String sysLogNo;
//		final int approvedBy;
//
//		InternalDistProcessOriginal(String sysLogNo, int approvedBy) {
//			this.sysLogNo = sysLogNo;
//			this.approvedBy = approvedBy;
//		}
//
//		@Override
//		public void run() {
//			try {
//				DistributionHeader header = distHeaderMapper.findDistributionHeaderBySysLogNo(sysLogNo);
//				if (header == null || header.getStatus() != WebConstants.DIST_STATUS_APPROVED) {
//					LOG.warn("No transaction needs to be processed for sysLogNo {}: {}", sysLogNo, header);
//					return;
//				}
//				if (WebConstants.DIST_STATUS_PROCESSING == header.getStatus()
//						|| WebConstants.PAJAK_DIST_STATUS_PROCESSING == header.getStatus()) {
//					LOG.error("InternalDistProcessOriginal Existing process has been running, skip processing {}",
//							header);
//					return;
//				}
//				if (header.getApprovedBy() != approvedBy) {
//					LOG.warn("This header is already approved by {}, cannot be approved again by {}",
//							header.getApprovedBy(), approvedBy);
//					return;
//				}
//				List<DistributionDetail> listDetail = distDetailMapper.findDistributionDetailBySysLogNo(sysLogNo);
//				if (listDetail == null || listDetail.size() == 0) {
//					LOG.warn("No detail transaction needs to be processed for sysLogNo {}", sysLogNo);
//					return;
//				}
//
//				String serverUrl = settingService.getSettingAsString(SettingService.SETTING_USSD_MMBS_URL);
//				if (StringUtils.isEmpty(serverUrl)) {
//					LOG.warn("No serverUrl is defined. NOT SENDING USSD REQUEST");
//					return;
//				}
//				String ussdTransfer = settingService.getSettingAsString(SettingService.SETTING_USSD_TRANSFER_SYNTAX);
//				if (StringUtils.isEmpty(ussdTransfer)) {
//					LOG.warn("No USSD Transfer Syntax is defined. NOT SENDING USSD REQUEST");
//					return;
//				}
//				// update status to process first
//				int processSuccess = 0; // header.getProcessSuccess();
//				double processValue = 0; // header.getProcessValue();
//				int processFailed = 0;
//				int skipFailed = 0;
//				header.setStatus(WebConstants.DIST_STATUS_PROCESSING);
//				header.setProcessStarted(timeService.getCurrentTime());
//				int updated = distHeaderMapper.updateDistributionHeader(header);
//				LOG.debug("First Updated {} {}", updated, header);
//
//				NumberFormat nfDisplay = new DecimalFormat("#,##0");
//				SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//				for (DistributionDetail detail : listDetail) {
//					detail.setProcessStarted(timeService.getCurrentTime());
//					if (detail.getProcessStatus() == WebConstants.PROCESS_STATUS_SUCCESS) {
//						processSuccess++;
//						processValue += detail.getMoneyValue();
//
//						continue;
//					} else if (detail.getProcessStatus() == WebConstants.PROCESS_STATUS_SKIP_SUCCESS) {
//						processSuccess++;
//						processValue += detail.getMoneyValue();
//
//						continue;
//					} else if (detail.getProcessStatus() == WebConstants.PROCESS_STATUS_SKIP_FAILED) {
//						skipFailed++;
//
//						continue;
//					}
//
//					String dataCheck = detail.getSysLogNo() + "_" + detail.getDetailId() + "_" + detail.getPhoneNo()
//							+ "_" + detail.getAccountNo() + "_" + detail.getMoneyValue();
//					String check = CommonUtil.toHexString(CipherUtils.hashSHA256(dataCheck));
//					if (!check.equals(detail.getDataCheck())) {
//						String logCheck = check;
//						if (check.length() > 10)
//							logCheck = "***" + logCheck.substring(logCheck.length() - 10);
//						LOG.warn("Invalid dataCheck {} from original {}", logCheck, detail.getDataCheck());
//						detail.setHostRc(ResultCode.ERR_INVALID_DATA_CHECK);
//						detail.setHostMessage("Invalid dataCheck");
//					} else {
//						// checksum valid
//						baseTrxService.sendTransactionToHost(header, detail);
//						if ("0".equals(detail.getHostRc())) {
//							// send notif
//							try {
//								String[] notifParam = { header.getSysLogNo() + "_" + detail.getDetailId(),
//										header.getUploadedUserCode(), header.getCheckedUserCode(),
//										header.getApprovedUserCode(), header.getFileAssignment(),
//										nfDisplay.format(detail.getMoneyValue()), detail.getAccountNo(),
//										detail.getAccountName(), sdfDisplay.format(header.getApprovedOn()),
//										detail.getHostRefNo() };
//								String notifMsg = messageSource.getMessage("msg.notif.recipient", notifParam, null);
//								sendSms(detail.getPhoneNo(), notifMsg, header.getSysLogNo(), "NOTIF");
//							} catch (Exception e) {
//								LOG.warn("Exception sending notif to " + detail.getPhoneNo(), e);
//							}
//						} // if transaction success
//					}
//					if ("0".equals(detail.getHostRc())) {
//						detail.setProcessStatus(WebConstants.PROCESS_STATUS_SUCCESS);
//						processSuccess++;
//						processValue += detail.getMoneyValue();
//					} else {
//						detail.setProcessStatus(WebConstants.PROCESS_STATUS_FAILED);
//						processFailed++;
//					}
//					String hostMessage = detail.getHostMessage();
//					if (StringUtils.isNotEmpty(hostMessage) && hostMessage.length() > 500) {
//						hostMessage = hostMessage.substring(0, 498) + "..";
//						detail.setHostMessage(hostMessage);
//					}
//					detail.setProcessFinished(timeService.getCurrentTime());
//					int detailUpdated = distDetailMapper.updateDistributionDetail(detail);
//					LOG.debug("Updated {} {}", detailUpdated, detail);
//				} // looping detail
//
//				header.setProcessFailed(processFailed + skipFailed);
//				header.setProcessSuccess(processSuccess);
//				header.setProcessValue(processValue);
//				header.setProcessFinished(timeService.getCurrentTime());
//				if (processFailed == 0) {
//					header.setStatus(WebConstants.DIST_STATUS_FINISHED);
//				} else {
//					header.setStatus(WebConstants.DIST_STATUS_PARTIAL_FIN);
//					// send notif
//					UserData approval = userDataService.findUserById(header.getApprovedBy());
//					try {
//						String[] notifParam = { header.getSysLogNo(), header.getSracNumber(),
//								header.getUploadedUserCode(), header.getCheckedUserCode(), header.getApprovedUserCode(),
//								header.getFileAssignment(), nfDisplay.format(header.getUploadedAmount()),
//								nfDisplay.format(header.getUploadedValue()),
//								nfDisplay.format(header.getProcessSuccess()),
//								nfDisplay.format(header.getProcessValue()), nfDisplay.format(header.getProcessFailed()),
//								nfDisplay.format(header.getUploadedValue() - header.getProcessValue()),
//								sdfDisplay.format(header.getApprovedOn()) };
//						String notifMsg = messageSource.getMessage("msg.notif.failed", notifParam, null);
//						sendSms(approval.getPhoneNo(), notifMsg, header.getSysLogNo(), "NOTIF");
//					} catch (Exception e) {
//						LOG.warn("Exception sending notif to " + approval.getPhoneNo(), e);
//					}
//				}
//				updated = distHeaderMapper.updateDistributionHeader(header);
//				LOG.debug("Second Updated {} {}", updated, header);
//			} catch (Exception e) {
//				LOG.warn("Exception in processing dist money for " + sysLogNo, e);
//			}
//		} // run
//
//	} // InternalDistProcessOriginal
//
//	private class InternalDistProcess implements Runnable {
//		final String sysLogNo;
//		final int approvedBy;
//		int state;
//
//		InternalDistProcess(String sysLogNo, int approvedBy, int state) {
//			this.state = -1;
//			this.sysLogNo = sysLogNo;
//			this.approvedBy = approvedBy;
//			this.state = state;
//		}
//
//		@Override
//		public void run() {
//			try {
//				DistributionHeader header = distHeaderMapper.findDistributionHeaderBySysLogNo(sysLogNo);
//
//				if (header == null || header.getStatus() != WebConstants.DIST_STATUS_APPROVED) {
//					LOG.warn("No transaction needs to be processed for sysLogNo {}: {}", sysLogNo, header);
//					return;
//				}
//				if (WebConstants.DIST_STATUS_PROCESSING == header.getStatus()
//						|| WebConstants.PAJAK_DIST_STATUS_PROCESSING == header.getStatus()) {
//					LOG.error("InternalDistProcess Existing process has been running, skip processing {}", header);
//					return;
//				}
//				if (header.getApprovedBy() != approvedBy) {
//					LOG.warn("This header is already approved by {}, cannot be approved again by {}",
//							header.getApprovedBy(), approvedBy);
//					return;
//				}
//				List<DistributionDetail> listDetail = distDetailMapper.findDistributionDetailBySysLogNo(sysLogNo);
//				if (listDetail == null || listDetail.size() == 0) {
//					LOG.warn("No detail transaction needs to be processed for sysLogNo {}", sysLogNo);
//					return;
//				}
//
//				String serverUrl = settingService.getSettingAsString(SettingService.SETTING_USSD_MMBS_URL);
//				if (StringUtils.isEmpty(serverUrl)) {
//					LOG.warn("No serverUrl is defined. NOT SENDING USSD REQUEST");
//					return;
//				}
//				String ussdTransfer = settingService.getSettingAsString(SettingService.SETTING_USSD_TRANSFER_SYNTAX);
//				if (StringUtils.isEmpty(ussdTransfer)) {
//					LOG.warn("No USSD Transfer Syntax is defined. NOT SENDING USSD REQUEST");
//					return;
//				}
//				// update status to process first
//				int processSuccess = 0; // header.getProcessSuccess();
//				double processValue = 0; // header.getProcessValue();
//				int processFailed = 0;
//				int skipFailed = 0;
//				header.setStatus(WebConstants.DIST_STATUS_PROCESSING);
//				header.setProcessStarted(timeService.getCurrentTime());
//				int updated = distHeaderMapper.updateDistributionHeader(header);
//				LOG.debug("First Updated {} {}", updated, header);
//
//				NumberFormat nfDisplay = new DecimalFormat("#,##0");
//				SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//				for (DistributionDetail detail : listDetail) {
//					detail.setProcessStarted(timeService.getCurrentTime());
//					if (detail.getProcessStatus() == WebConstants.PROCESS_STATUS_SUCCESS) {
//						processSuccess++;
//						processValue += detail.getMoneyValue();
//
//						continue;
//					} else if (detail.getProcessStatus() == WebConstants.PROCESS_STATUS_SKIP_SUCCESS) {
//						processSuccess++;
//						processValue += detail.getMoneyValue();
//
//						continue;
//					} else if (detail.getProcessStatus() == WebConstants.PROCESS_STATUS_SKIP_FAILED) {
//						skipFailed++;
//
//						continue;
//					}
//
//					String dataCheck = detail.getSysLogNo() + "_" + detail.getDetailId() + "_" + detail.getPhoneNo()
//							+ "_" + detail.getAccountNo() + "_" + detail.getMoneyValue();
//					String check = CommonUtil.toHexString(CipherUtils.hashSHA256(dataCheck));
//					if (!check.equals(detail.getDataCheck())) {
//						String logCheck = check;
//						if (check.length() > 10)
//							logCheck = "***" + logCheck.substring(logCheck.length() - 10);
//						LOG.warn("Invalid dataCheck {} from original {}", logCheck, detail.getDataCheck());
//						detail.setHostRc(ResultCode.ERR_INVALID_DATA_CHECK);
//						detail.setHostMessage("Invalid dataCheck");
//					} else {
//
//						LOG.debug("Bank Code {}", detail.getBankCode());
//						if (StringUtils.isEmpty(detail.getBankCode())) {
//							// checksum valid
//							baseTrxService.sendTransactionToHost(header, detail);
//						} else {
//							trxIsoService.sendTransactionToHost(header, detail, state);
//						}
//
//						if ("0".equals(detail.getHostRc())) {
//							// send notif
//							try {
//								String[] notifParam = { header.getSysLogNo() + "_" + detail.getDetailId(),
//										header.getUploadedUserCode(), header.getCheckedUserCode(),
//										header.getApprovedUserCode(), header.getFileAssignment(),
//										nfDisplay.format(detail.getMoneyValue()), detail.getAccountNo(),
//										detail.getAccountName(), sdfDisplay.format(header.getApprovedOn()),
//										detail.getHostRefNo() };
//								String notifMsg = messageSource.getMessage("msg.notif.recipient", notifParam, null);
//								sendSms(detail.getPhoneNo(), notifMsg, header.getSysLogNo(), "NOTIF");
//							} catch (Exception e) {
//								LOG.warn("Exception sending notif to " + detail.getPhoneNo(), e);
//							}
//						} // if transaction success
//					}
//					LOG.info("*****----***** Perubahan balikan dari host ");
//					LOG.info("*****----***** Respon host " + detail.getHostRc());
//					if ("0".equals(detail.getHostRc())) {
//						detail.setProcessStatus(WebConstants.PROCESS_STATUS_SUCCESS);
//						processSuccess++;
//						processValue += detail.getMoneyValue();
//					} else {
////						detail.setProcessStatus(WebConstants.PROCESS_STATUS_FAILED);
//						String reversalRc = detail.getHostRc();
//						if ("R".equalsIgnoreCase(reversalRc.substring(0, 1))) {
//							String temp = reversalRc.substring(1, reversalRc.length());
//							LOG.info("*****----***** temp " + reversalRc);
//							reversalRc = "88" + temp;
//							detail.setProcessStatus(Integer.parseInt(reversalRc));
//							LOG.info("*****----***** reversalRc " + reversalRc);
//						} else {
//							detail.setProcessStatus(Integer.parseInt(detail.getHostRc()));
//						}
//
//						processFailed++;
//					}
//					LOG.info("*****----***** Detail " + detail.toString());
//					String hostMessage = detail.getHostMessage();
//					if (StringUtils.isNotEmpty(hostMessage) && hostMessage.length() > 500) {
//						hostMessage = hostMessage.substring(0, 498) + "..";
//						detail.setHostMessage(hostMessage);
//					}
//					detail.setProcessFinished(timeService.getCurrentTime());
//					int detailUpdated = distDetailMapper.updateDistributionDetail(detail);
//					LOG.debug("Updated {} {}", detailUpdated, detail);
//				} // looping detail
//
//				header.setProcessFailed(processFailed + skipFailed);
//				header.setProcessSuccess(processSuccess);
//				header.setProcessValue(processValue);
//				header.setProcessFinished(timeService.getCurrentTime());
//				if (processFailed == 0) {
//					header.setStatus(WebConstants.DIST_STATUS_FINISHED);
//				} else {
////					header.setStatus(WebConstants.DIST_STATUS_PARTIAL_FIN);
//					header.setStatus(WebConstants.DIST_STATUS_FAILED);
//					// send notif
//					UserData approval = userDataService.findUserById(header.getApprovedBy());
//					try {
//						String[] notifParam = { header.getSysLogNo(), header.getSracNumber(),
//								header.getUploadedUserCode(), header.getCheckedUserCode(), header.getApprovedUserCode(),
//								header.getFileAssignment(), nfDisplay.format(header.getUploadedAmount()),
//								nfDisplay.format(header.getUploadedValue()),
//								nfDisplay.format(header.getProcessSuccess()),
//								nfDisplay.format(header.getProcessValue()), nfDisplay.format(header.getProcessFailed()),
//								nfDisplay.format(header.getUploadedValue() - header.getProcessValue()),
//								sdfDisplay.format(header.getApprovedOn()) };
//						String notifMsg = messageSource.getMessage("msg.notif.failed", notifParam, null);
//						sendSms(approval.getPhoneNo(), notifMsg, header.getSysLogNo(), "NOTIF");
//					} catch (Exception e) {
//						LOG.warn("Exception sending notif to " + approval.getPhoneNo(), e);
//					}
//				}
//				updated = distHeaderMapper.updateDistributionHeader(header);
//				LOG.debug("Second Updated {} {}", updated, header);
//			} catch (Exception e) {
//				LOG.warn("Exception in processing dist money for " + sysLogNo, e);
//			}
//		} // run
//
//	} // InternalDistProcess
//
//	private class InternalDistProcessPajak {
//		final String sysLogNo;
//		final int approvedBy;
//		final int state;
//		final String paramBit48;
//
//		InternalDistProcessPajak(String sysLogNo, int approvedBy, int state, String paramBit48) {
//			this.sysLogNo = sysLogNo;
//			this.approvedBy = approvedBy;
//			this.state = state;
//			this.paramBit48 = paramBit48;
//		}
//
//		public void run() {
//			try {
//				DistributionHeader header = distHeaderMapper.findDistributionHeaderBySysLogNo(sysLogNo);
//
//				if (header == null || header.getStatus() != WebConstants.PAJAK_DIST_STATUS_APPROVED) {
//					LOG.warn("No transaction needs to be processed for sysLogNo {}: {}", sysLogNo, header);
//					return;
//				}
//				if (WebConstants.DIST_STATUS_PROCESSING == header.getStatus()
//						|| WebConstants.PAJAK_DIST_STATUS_PROCESSING == header.getStatus()) {
//					LOG.error("InternalDistProcessPajak Existing process has been running, skip processing {}", header);
//					return;
//				}
//				if (header.getApprovedBy() != approvedBy) {
//					LOG.warn("This header is already approved by {}, cannot be approved again by {}",
//							header.getApprovedBy(), approvedBy);
//					return;
//				}
//				List<DistributionDetail> listDetail = distDetailMapper.findDistributionDetailBySysLogNo(sysLogNo);
//				if (listDetail == null || listDetail.size() == 0) {
//					LOG.warn("No detail transaction needs to be processed for sysLogNo {}", sysLogNo);
//					return;
//				}
//
//				String serverUrl = settingService.getSettingAsString(SettingService.SETTING_USSD_MMBS_URL);
//				if (StringUtils.isEmpty(serverUrl)) {
//					LOG.warn("No serverUrl is defined. NOT SENDING USSD REQUEST");
//					return;
//				}
//				String ussdTransfer = settingService.getSettingAsString(SettingService.SETTING_USSD_TRANSFER_SYNTAX);
//				if (StringUtils.isEmpty(ussdTransfer)) {
//					LOG.warn("No USSD Transfer Syntax is defined. NOT SENDING USSD REQUEST");
//					return;
//				}
//				header.setBit48(paramBit48);
//				// update status to process first
//				int processSuccess = 0; // header.getProcessSuccess();
//				double processValue = 0; // header.getProcessValue();
//				int processFailed = 0;
//				int skipFailed = 0;
//				header.setStatus(WebConstants.PAJAK_DIST_STATUS_PROCESSING);
//				header.setProcessStarted(timeService.getCurrentTime());
//				int updated = distHeaderMapper.updateDistributionHeader(header);
//				LOG.debug("First Updated {} {}", updated, header);
//
//				NumberFormat nfDisplay = new DecimalFormat("#,##0");
//				SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//				for (DistributionDetail detail : listDetail) {
//					detail.setBit48(paramBit48);
//					LOG.debug("detail {}", detail);
//					detail.setProcessStarted(timeService.getCurrentTime());
//					if (detail.getProcessStatus() == WebConstants.PROCESS_STATUS_SUCCESS) {
//						processSuccess++;
//						processValue += detail.getMoneyValue();
//
//						continue;
//					} else if (detail.getProcessStatus() == WebConstants.PROCESS_STATUS_SKIP_SUCCESS) {
//						processSuccess++;
//						processValue += detail.getMoneyValue();
//
//						continue;
//					} else if (detail.getProcessStatus() == WebConstants.PROCESS_STATUS_SKIP_FAILED) {
//						skipFailed++;
//
//						continue;
//					}
//
//					String dataCheck = detail.getSysLogNo() + "_" + detail.getDetailId() + "_" + detail.getPhoneNo()
//							+ "_" + detail.getAccountNo() + "_" + detail.getMoneyValue();
//					String check = CommonUtil.toHexString(CipherUtils.hashSHA256(dataCheck));
//					LOG.debug("check : " + check);
//					LOG.debug("detail data check : " + detail.getDataCheck());
//
//					if (!check.equals(detail.getDataCheck())) {
//						String logCheck = check;
//						if (check.length() > 10)
//							logCheck = "***" + logCheck.substring(logCheck.length() - 10);
//						LOG.warn("Invalid dataCheck {} from original {}", logCheck, detail.getDataCheck());
//						detail.setHostRc(ResultCode.ERR_INVALID_DATA_CHECK_PAJAK);
//						detail.setHostMessage("Invalid dataCheck");
//					} else {
//						LOG.debug("process else ");
////						LOG.debug("Bank Code {}", detail.getBankCode());
////						if(StringUtils.isEmpty(detail.getBankCode())) {
////							// checksum valid
//						if (WebConstants.INQ == state) {
//							LOG.debug("Inq Sdd");
//							trxIsoService.createIDBillingPaymentInq(header, detail);
//							LOG.debug("Inq Sdd Success");
//							bit48 = detail.getBit48();
//							LOG.debug("bit 48 " + bit48);
//						} else if (WebConstants.SETT == state) {
//							LOG.debug("Bit 48 inq : " + detail.getBit48());
//							if ("0".equals(detail.getHostRc())) {
//								LOG.debug("Sett Sdd");
//								trxIsoService.createIDBillingPaymentSett(header, detail);
//								LOG.debug("Sett Sdd Success");
//								if ("0".equals(detail.getHostRc())) {
//									// send notif
//									try {
//										String[] notifParam = { header.getSysLogNo() + "_" + detail.getDetailId(),
//												header.getUploadedUserCode(), header.getCheckedUserCode(),
//												header.getApprovedUserCode(), header.getFileAssignment(),
//												nfDisplay.format(detail.getMoneyValue()), detail.getAccountNo(),
//												detail.getAccountName(), sdfDisplay.format(header.getApprovedOn()),
//												detail.getHostRefNo() };
//										String notifMsg = messageSource.getMessage("msg.notif.recipient", notifParam,
//												null);
//										sendSms(detail.getPhoneNo(), notifMsg, header.getSysLogNo(), "NOTIF");
//									} catch (Exception e) {
//										LOG.warn("Exception sending notif to " + detail.getPhoneNo(), e);
//									}
//								}
//							}
//						}
////							LOG.debug("Inq Sdd");
////							trxIsoService.createIDBillingPaymentInq(header, detail);
////							LOG.debug("Inq Sdd Success");
////							if ("0".equals(detail.getHostRc())) {
////								LOG.debug("Bit 48 inq : " + detail.getBit48());
////								if ("0".equals(detail.getHostRc())) {
////									LOG.debug("Sett Sdd");
////									trxIsoService.createIDBillingPaymentSett(header, detail);
////									LOG.debug("Sett Sdd Success");
////									if ("0".equals(detail.getHostRc())) {
////										// send notif
////										try {
////											String[] notifParam = {
////												header.getSysLogNo() + "_" + detail.getDetailId(),
////												header.getUploadedUserCode(),
////												header.getCheckedUserCode(),
////												header.getApprovedUserCode(),
////												header.getFileAssignment(),
////												nfDisplay.format(detail.getMoneyValue()),
////												detail.getAccountNo(),
////												detail.getAccountName(),
////												sdfDisplay.format(header.getApprovedOn()),
////												detail.getHostRefNo()
////											};
////											String notifMsg = messageSource.getMessage("msg.notif.recipient", notifParam, null);
////											sendSms(detail.getPhoneNo(), notifMsg, header.getSysLogNo(), "NOTIF");
////										} catch (Exception e) {
////											LOG.warn("Exception sending notif to " + detail.getPhoneNo(), e);
////										}
////									}								
////								}
////							}
//
//					}
//					if ("0".equals(detail.getHostRc()) && WebConstants.SETT == state) {
//						detail.setProcessStatus(WebConstants.PROCESS_STATUS_SUCCESS);
//						processSuccess++;
//						processValue += detail.getMoneyValue();
//					} else {
//						detail.setProcessStatus(WebConstants.PROCESS_STATUS_FAILED);
//						processFailed++;
//					}
//					String hostMessage = detail.getHostMessage();
//					if (StringUtils.isNotEmpty(hostMessage) && hostMessage.length() > 500) {
//						hostMessage = hostMessage.substring(0, 498) + "..";
//						detail.setHostMessage(hostMessage);
//					}
//					detail.setProcessFinished(timeService.getCurrentTime());
//					int detailUpdated = distDetailMapper.updateDistributionDetail(detail);
//					LOG.debug("Updated {} {}", detailUpdated, detail);
//				} // looping detail
//
//				header.setProcessFailed(processFailed + skipFailed);
//				header.setProcessSuccess(processSuccess);
//				header.setProcessValue(processValue);
//				header.setProcessFinished(timeService.getCurrentTime());
//				if (processFailed == 0 && WebConstants.SETT == state) {
//					LOG.debug("processFailed 0 and state {}", state);
//					header.setStatus(WebConstants.PAJAK_DIST_STATUS_FINISHED);
//				} else {
//					header.setStatus(WebConstants.PAJAK_DIST_STATUS_PARTIAL_FIN);
//					// send notif
//					UserData approval = userDataService.findUserById(header.getApprovedBy());
//					try {
//						String[] notifParam = { header.getSysLogNo(), header.getSracNumber(),
//								header.getUploadedUserCode(), header.getCheckedUserCode(), header.getApprovedUserCode(),
//								header.getFileAssignment(), nfDisplay.format(header.getUploadedAmount()),
//								nfDisplay.format(header.getUploadedValue()),
//								nfDisplay.format(header.getProcessSuccess()),
//								nfDisplay.format(header.getProcessValue()), nfDisplay.format(header.getProcessFailed()),
//								nfDisplay.format(header.getUploadedValue() - header.getProcessValue()),
//								sdfDisplay.format(header.getApprovedOn()) };
//						String notifMsg = messageSource.getMessage("msg.notif.failed", notifParam, null);
//						sendSms(approval.getPhoneNo(), notifMsg, header.getSysLogNo(), "NOTIF");
//					} catch (Exception e) {
//						LOG.warn("Exception sending notif to " + approval.getPhoneNo(), e);
//					}
//				}
//				updated = distHeaderMapper.updateDistributionHeader(header);
//				LOG.debug("Second Updated {} {}", updated, header);
//			} catch (Exception e) {
//				LOG.warn("Exception in processing dist money for " + sysLogNo, e);
//			}
//		} // run
//
//	} // InternalDistProcessPajak
//
//	public void setAgentCount(int agentCount) {
//		this.agentCount = agentCount;
//	}
//
//	public void setTransmitterAgent(HttpTransmitterAgent transmitterAgent) {
//		this.transmitterAgent = transmitterAgent;
//	}
//
//	public void setTrxService(BaseTrxService baseTrxService) {
//		this.baseTrxService = baseTrxService;
//	}
//}
