package id.co.emobile.samba.web.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.databind.ObjectMapper;

import id.co.emobile.samba.web.collega.AccBalanceInqRequestVO;
import id.co.emobile.samba.web.collega.AccBalanceInqResponseVO;
import id.co.emobile.samba.web.collega.AccountStatementRequestVO;
import id.co.emobile.samba.web.collega.AccountStatementResponseVO;
import id.co.emobile.samba.web.collega.FundTransferRequestVO;
import id.co.emobile.samba.web.collega.FundTransferResponseVO;
import id.co.emobile.samba.web.data.AccStatementDetailVO;
import id.co.emobile.samba.web.data.AccStatementVO;
import id.co.emobile.samba.web.data.CheckAccountVO;
import id.co.emobile.samba.web.data.ResponseData;
import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.data.SourceAccountVO;
import id.co.emobile.samba.web.entity.DistributionDetail;
import id.co.emobile.samba.web.entity.DistributionHeader;
import id.co.emobile.samba.web.http.HttpTransmitterAgent;

public class TrxCollegaService implements BaseTrxService {
	private static final Logger LOG = LoggerFactory.getLogger(TrxCollegaService.class);
	
	private static final String HOST_SUCCESS_CODE	= "00";
	private String timeServer = "";
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private AppsTimeService timeService;
	
	@Autowired
	@Qualifier("collegaSequenceGenerator")
	private SequenceGeneratorService collegaSeqGen;
	
	private HttpTransmitterAgent transmitterAgent;
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public void init() {
		LOG.info("TrxCollegaService is started");
	}
	public void shutdown() {
		LOG.info("TrxCollegaService is stopped");		
	}
	
	@Override
	public String getServiceName() {
		return "Service Collega";
	}
	
	@Override 
	public CheckAccountVO checkAccountForNo(String accountNo) {
		CheckAccountVO accountVO = new CheckAccountVO();
		accountVO.setRc(ResultCode.ERR_FAILED_CHECK);
		
		LOG.debug("checkAccountForNo: {}", accountNo);
		try {
			Map<String, String> mapData = settingService.getSettingAsJson(SettingService.SETTING_COLLEGA_PARAM_DATA);
			String userGtw = mapData.get(SettingService.COLLEGA_PARAM_USERGTW);
			String channelId = mapData.get(SettingService.COLLEGA_PARAM_CHANNEL);
			
			String serverUrl = settingService.getSettingAsString(SettingService.SETTING_COLLEGA_URL);
			if (StringUtils.isEmpty(serverUrl)) {
				LOG.warn("No collegaUrl, setting id {} is defined. NOT SENDING REQUEST",
						SettingService.SETTING_COLLEGA_URL);
				return accountVO;
			}
			// compose request
			Date now = timeService.getCurrentTime();
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
			sdfDate.setTimeZone(TimeZone.getTimeZone(timeServer));
			
			SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
			sdfTime.setTimeZone(TimeZone.getTimeZone(timeServer));
			
			String trxDate = sdfDate.format(now);
			String trxHour = sdfTime.format(now);
			
			AccBalanceInqRequestVO requestVO = new AccBalanceInqRequestVO();
			String reqId = "00002";
			String authKey = composeHmac(reqId, now);
			
			requestVO.setAuthKey(authKey); // required
			requestVO.setReqId(reqId); // required
			requestVO.setTxDate(trxDate); // required yyyymmdd
			requestVO.setTxHour(trxHour); // required hhmmss
			requestVO.setUserGtw(userGtw); // required
			requestVO.setChannelId(channelId); // required
			requestVO.setAccnbr(accountNo);
			requestVO.setFlgSaldo(0); // required
			requestVO.setFlgNsb(1); // required
			
			String jsonRequest = objectMapper.writeValueAsString(requestVO);
			LOG.debug("checkAccountForNo with request: {}", jsonRequest);
			
			// send request
			ResponseData responseData = transmitterAgent.sendPostMessageWithEntity(serverUrl, jsonRequest);
			if (responseData.getResultCode() == 0) {
				// success, parsing response
				AccBalanceInqResponseVO resp = objectMapper.readValue(responseData.getMsgToUser(), AccBalanceInqResponseVO.class);
				if (HOST_SUCCESS_CODE.equals(resp.getrCode())) {
					String accountStatus = resp.getResult().get("ACCSTS");
					int accStat = Integer.parseInt(accountStatus);
//					Application Code (01-Giro, 02-Tabungan)
					String accountType = resp.getResult().get("APPLID");
					String accType = "N/A";
					if ("01".equals(accountType))
						accType = "Giro";
					else if ("02".equals(accountType))
						accType = "Tabungan";
					String nickName = resp.getResult().get("NICKNM");
					String fullName = resp.getResult().get("FULLNM");
					accountVO.setRc("0");
					accountVO.setAccountName(nickName);
					if(StringUtils.isNotEmpty(fullName)) {
						accountVO.setFullName(fullName);	
					}else if(StringUtils.isNotEmpty(nickName)) {
						accountVO.setFullName(nickName);
					}
					accountVO.setAccountStatus(accStat);
					accountVO.setAccountNo(accountNo);
					accountVO.setAccountType(accType);
				} else {
					LOG.warn("Failed checkBalance with response: {}", responseData.getMsgToUser());
				}			
			} else {
				LOG.warn("Failed checkBalance with message: {}", responseData);
			}
		} catch (Exception e) {
			LOG.warn("Exception checkAccountForNo for " + accountNo, e);
		}
		return accountVO;
	}
	
	@Override
	public void checkBalanceForAccount(SourceAccountVO sourceAccount) {
		LOG.debug("checkBalanceForAccount: {}", sourceAccount);
		try {
			int maxInterval = settingService.getSettingAsInt(SettingService.SETTING_CHECK_BALANCE_INTERVAL) * 1000;
			long delta = System.currentTimeMillis() - sourceAccount.getLastChecked();
			if (delta <= maxInterval) {
				Date dtLastChecked = new Date(sourceAccount.getLastChecked());
				LOG.warn("No need to check balance, last checked on {}", dtLastChecked);
				return;
			}
			Map<String, String> mapData = settingService.getSettingAsJson(SettingService.SETTING_COLLEGA_PARAM_DATA);
			String userGtw = mapData.get(SettingService.COLLEGA_PARAM_USERGTW);
			String channelId = mapData.get(SettingService.COLLEGA_PARAM_CHANNEL);
			
			String serverUrl = settingService.getSettingAsString(SettingService.SETTING_COLLEGA_URL);
			if (StringUtils.isEmpty(serverUrl)) {
				LOG.warn("No collegaUrl, setting id {} is defined. NOT SENDING REQUEST",
						SettingService.SETTING_COLLEGA_URL);
				return;
			}
			// compose request
			Date now = timeService.getCurrentTime();
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
			sdfDate.setTimeZone(TimeZone.getTimeZone(timeServer));
			
			SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
			sdfTime.setTimeZone(TimeZone.getTimeZone(timeServer));
			
			String trxDate = sdfDate.format(now);
			String trxHour = sdfTime.format(now);
			
			AccBalanceInqRequestVO requestVO = new AccBalanceInqRequestVO();
			String reqId = "00002";
			String authKey = composeHmac(reqId, now);
			
			requestVO.setAuthKey(authKey); // required
			requestVO.setReqId(reqId); // required
			requestVO.setTxDate(trxDate); // required yyyymmdd
			requestVO.setTxHour(trxHour); // required hhmmss
			requestVO.setUserGtw(userGtw); // required
			requestVO.setChannelId(channelId); // required
			requestVO.setAccnbr(sourceAccount.getSracNumber());
			requestVO.setFlgSaldo(1); // required
			requestVO.setFlgNsb(0); // required
			
			String jsonRequest = objectMapper.writeValueAsString(requestVO);
			LOG.debug("CheckBalance with request: {}", jsonRequest);
			
			// send request
			ResponseData responseData = transmitterAgent.sendPostMessageWithEntity(serverUrl, jsonRequest);
			sourceAccount.setBalanceRc(ResultCode.ERR_FAILED_CHECK);
			if (responseData.getResultCode() == 0) {
				// success, parsing response
				AccBalanceInqResponseVO resp = objectMapper.readValue(responseData.getMsgToUser(), AccBalanceInqResponseVO.class);
				if (HOST_SUCCESS_CODE.equals(resp.getrCode())) {
					String bal = StringUtils.trim(resp.getResult().get("SALDO_EFEKTIF")); //SALDO_AKHIR"));
					if (bal.startsWith("-")) {
						bal = bal.substring(1);
					}
					Double dblBalance = Double.parseDouble(bal);
					sourceAccount.setBalanceRc("0");
					sourceAccount.setSracBalance(dblBalance);
					sourceAccount.setLastChecked(System.currentTimeMillis());
				} else {
					LOG.warn("Failed checkBalance with response: {}", responseData.getMsgToUser());
				}			
			} else {
				LOG.warn("Failed checkBalance with message: {}", responseData);
			}
			
		} catch (Exception e) {
			LOG.warn("Exception checkBalance for " + sourceAccount, e);
		}
		
	}
	
	@Override
	public AccStatementVO checkStatementForAccount(SourceAccountVO sourceAccount, Date startDate, Date endDate) {
		LOG.debug("checkStatementForAccount: {}", sourceAccount);
		AccStatementVO vo = new AccStatementVO();
		vo.setStatementRc(ResultCode.ERR_FAILED_CHECK);
		vo.setMessage("System error. Please contact administrator");
		
		try {
			Map<String, String> mapData = settingService.getSettingAsJson(SettingService.SETTING_COLLEGA_PARAM_DATA);
			String userGtw = mapData.get(SettingService.COLLEGA_PARAM_USERGTW);
			String channelId = mapData.get(SettingService.COLLEGA_PARAM_CHANNEL);
			
			String serverUrl = settingService.getSettingAsString(SettingService.SETTING_COLLEGA_URL);
			if (StringUtils.isEmpty(serverUrl)) {
				LOG.warn("No collegaUrl, setting id {} is defined. NOT SENDING REQUEST",
						SettingService.SETTING_COLLEGA_URL);
				vo.setStatementRc(ResultCode.ERR_MISSING_DATA);
				return vo;
			}
			// compose request
			Date now = timeService.getCurrentTime();
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
			sdfDate.setTimeZone(TimeZone.getTimeZone(timeServer));
			
			SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
			sdfTime.setTimeZone(TimeZone.getTimeZone(timeServer));
			
			String trxDate = sdfDate.format(now);
			String trxHour = sdfTime.format(now);
			
			SimpleDateFormat sdfTxDate = new SimpleDateFormat("yyyy-MM-dd");
			AccountStatementRequestVO requestVO = new AccountStatementRequestVO();
			String reqId = "00008";
			String authKey = composeHmac(reqId, now);
			
			requestVO.setAuthKey(authKey); // required
			requestVO.setReqId(reqId); // required
			requestVO.setTxDate(trxDate); // required yyyymmdd
			requestVO.setTxHour(trxHour); // required hhmmss
			requestVO.setUserGtw(userGtw); // required
			requestVO.setChannelId(channelId); // required
			requestVO.setAccNbr(sourceAccount.getSracNumber());
			requestVO.setStartDate(sdfTxDate.format(startDate)); // required
			requestVO.setEndDate(sdfTxDate.format(endDate)); // required
			
			String jsonRequest = objectMapper.writeValueAsString(requestVO);
			LOG.debug("checkStatementForAccount with request: {}", jsonRequest);
			
			// send request
			ResponseData responseData = transmitterAgent.sendPostMessageWithEntity(serverUrl, jsonRequest);
			if (responseData.getResultCode() == 0) {
				// success, parsing response
				AccountStatementResponseVO resp = objectMapper.readValue(responseData.getMsgToUser(), AccountStatementResponseVO.class);
				if (HOST_SUCCESS_CODE.equals(resp.getrCode())) {
					vo.setStatementRc("0"); // resp.getrCode());
					vo.setMessage(resp.getMessage());
					vo.setStartBalance(Double.parseDouble(resp.getSaldoAwal()));
					vo.setEndBalance(Double.parseDouble(resp.getSaldoAkhir()));
					
					SimpleDateFormat sdfTxSettle = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					List<Map<String, String>> listMapDetail = resp.getResult();
					for (Map<String, String> mapDetail: listMapDetail) {
						AccStatementDetailVO detailVO = new AccStatementDetailVO();
						detailVO.setTxCode(mapDetail.get("TXCODE")); // TXCODE : String Transaction Code
						detailVO.setTxDate(sdfTxDate.parse(mapDetail.get("TXDATE"))); // TXDATE : Date Transaction Date(yyyy-MM-dd )
						detailVO.setTxAmount(Double.parseDouble(mapDetail.get("TXAMT"))); // TXAMT : Float Transaction Amount
						detailVO.setTxDateSettle(sdfTxSettle.parse(mapDetail.get("TXDTSTLMN")));  // TXDTSTLMN : Date Transaction Settlement Date (yyyy-MM-dd HH:mm:Sss )
						detailVO.setAccNumber(mapDetail.get("ACCNBR"));;  // ACCNBR : String Account Number
						detailVO.setTxId(mapDetail.get("TXID"));  // TXID : String Transaction ID
						detailVO.setDebetCredit(Integer.parseInt(mapDetail.get("DBCR")));; //DBCR : Integer Transaction Type (0 – Debit, 1- Credit)
						detailVO.setTxMsg(mapDetail.get("TXMSG"));  // TXMSG : String Transaction Message
						detailVO.setTxBranch(mapDetail.get("TXBRANCH"));  // TXBRANCH : String Transaction Branch Location
						detailVO.setTxCurrency(mapDetail.get("TXCCY"));  // TXCCY : String Transaction Currency
						detailVO.setUserId(mapDetail.get("USERID"));;  // USERID : String
						vo.getListDetail().add(detailVO);
					}
				} else {
					LOG.warn("Failed checkStatementForAccount with response: {}", responseData.getMsgToUser());
					vo.setStatementRc(resp.getrCode());
					vo.setMessage(resp.getMessage());
				}			
			} else {
				LOG.warn("Failed checkStatementForAccount with message: {}", responseData);
			}
			
		} catch (Exception e) {
			LOG.warn("Exception checkStatementForAccount for " + sourceAccount, e);
		}
		
		return vo;
	}
	
	@Override
	public void sendTransactionToHostPajak(DistributionHeader header, DistributionDetail detail) {
		LOG.debug("sendTransactionToHostPajak: {}", detail);
		try {
			String serverUrl = settingService.getSettingAsString(SettingService.SETTING_COLLEGA_URL);
			if (StringUtils.isEmpty(serverUrl)) {
				LOG.warn("No collegaUrl, setting id {} is defined. NOT SENDING REQUEST",
						SettingService.SETTING_COLLEGA_URL);
				return;
			}
			Map<String, String> mapData = settingService.getSettingAsJson(SettingService.SETTING_COLLEGA_PARAM_DATA);
			String userGtw = mapData.get(SettingService.COLLEGA_PARAM_USERGTW);
			String channelId = mapData.get(SettingService.COLLEGA_PARAM_CHANNEL);
			String corpId = mapData.get(SettingService.COLLEGA_PARAM_CORPID);
			String prodId = mapData.get(SettingService.COLLEGA_PARAM_PRODID);
			String branch = mapData.get(SettingService.COLLEGA_PARAM_BRANCH);
			
			Date now = timeService.getCurrentTime();
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
			sdfDate.setTimeZone(TimeZone.getTimeZone(timeServer));
			
			SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
			sdfTime.setTimeZone(TimeZone.getTimeZone(timeServer));
			
			SimpleDateFormat sdfDate2 = new SimpleDateFormat("dd-MM-yyyy");
			sdfDate2.setTimeZone(TimeZone.getTimeZone(timeServer));
			
			String reqId = "00005";
			String authKey = composeHmac(reqId, now);
			
			String trxDate = sdfDate.format(now);
			String trxHour = sdfTime.format(now);
			String date = sdfDate2.format(now);
			
			FundTransferRequestVO fundTransferRequestVO = new FundTransferRequestVO();
			
			fundTransferRequestVO.setAuthKey(authKey); // required
			fundTransferRequestVO.setReqId(reqId); // required
			fundTransferRequestVO.setUserGtw(userGtw); // required
			fundTransferRequestVO.setChannelId(channelId); // required
			fundTransferRequestVO.setTxDate(trxDate); // required yyyymmdd
			fundTransferRequestVO.setTxHour(trxHour); // required hhmmss
			
			fundTransferRequestVO.setCorpId(corpId);
			fundTransferRequestVO.setProdId(prodId);
			fundTransferRequestVO.setDate(date);
			fundTransferRequestVO.setDate_rk(date);
			fundTransferRequestVO.setBranchId(branch);
			fundTransferRequestVO.setTxCcy("IDR");
			
			int numOfAcc = 2;
			fundTransferRequestVO.setNbrOfAcc(Integer.toString(numOfAcc));
			fundTransferRequestVO.setProsesId("0005");
			fundTransferRequestVO.setUserId(userGtw); //header.getApprovedUserCode());
			
			fundTransferRequestVO.setSpvId("");
			fundTransferRequestVO.setRevSts("0"); // 0: not reversal, 1: reversal
			fundTransferRequestVO.setTxType("O"); // O: Overbooking
			fundTransferRequestVO.setRefAcc(header.getSracNumber()); // SourceAcc // task.getTermAsString(TermConstant.SRAC));
			
			String fileNameOnly = FilenameUtils.getBaseName(header.getFileData());
			String txMsg = "KNT-" + fileNameOnly + "-" + detail.getAccountName();
			List<Map<String, Object>> listParam = new ArrayList<Map<String, Object>>();
			
			String txId = collegaSeqGen.getNextSequence();
			// 0: Debet, 1: Credit
			// debet first
			Map<String, Object> debetVO = new HashMap<String, Object>();
			debetVO.put("accNbr", (String) header.getSracNumber());
			debetVO.put("txMsg", (String) txMsg);
			debetVO.put("txId", (String) txId);
			debetVO.put("isFee", (Integer) 0);
	//		debetVO.put("chqBkNbr", (String) ""); // minta di hide ramadhian noor
			debetVO.put("pageNbr", (Integer) 0);
			debetVO.put("txDtDoc", (String) "");
			debetVO.put("refAcc", (String) "");
			debetVO.put("valMinBal", (Integer) 1);
			// txCode 199=Penarikan, 299=Setoran
			debetVO.put("dbCr", (String) "0");
			debetVO.put("txCode", (String) "199");
			debetVO.put("txAmt", (Double) detail.getMoneyValue());
			listParam.add(debetVO);
			
			// then credit 
			Map<String, Object> creditVO = new HashMap<String, Object>();
			creditVO.put("accNbr", (String) detail.getAccountNo());
			creditVO.put("txMsg", (String) txMsg);
			creditVO.put("txId", (String) txId);
			creditVO.put("isFee", (Integer) 0);
	//		creditVO.put("chqBkNbr", (String) ""); // minta di hide ramadhian noor
			creditVO.put("pageNbr", (Integer) 0);
			creditVO.put("txDtDoc", (String) "");
			creditVO.put("refAcc", (String) "");
			creditVO.put("valMinBal", (Integer) 1);
			// txCode 199=Penarikan, 299=Setoran
			creditVO.put("dbCr", (String) "1");
			creditVO.put("txCode", (String) "299");
			creditVO.put("txAmt", (Double) detail.getMoneyValue());
			listParam.add(creditVO);
			
			NumberFormat nf = new DecimalFormat("###0");
			String totalAmount = nf.format(detail.getMoneyValue());
			fundTransferRequestVO.setTotalAmount(totalAmount);
			fundTransferRequestVO.setParam(listParam);
			
			String jsonRequest = objectMapper.writeValueAsString(fundTransferRequestVO);
			LOG.debug("Transaction with request: {}", jsonRequest);
			
			// send request
			ResponseData responseData = transmitterAgent.sendPostMessageWithEntity(serverUrl, jsonRequest);
			if (responseData.getResultCode() == 0) {
				// success, parsing response
				FundTransferResponseVO resp = objectMapper.readValue(responseData.getMsgToUser(), FundTransferResponseVO.class);
				if (HOST_SUCCESS_CODE.equals(resp.getrCode())) {
					String hostRef = StringUtils.trim(resp.getResult().get("TXID"));
					detail.setHostRc("0");
					detail.setHostMessage(resp.getMessage());
					detail.setHostRefNo(hostRef);
				} else {
					LOG.warn("Failed checkBalance with response: {}", responseData.getMsgToUser());
					detail.setHostRc(resp.getrCode());
					detail.setHostMessage(resp.getMessage());
				}			
			} else {
				LOG.warn("Failed checkBalance with message: {}", responseData);
				detail.setHostRc(ResultCode.ERR_UNABLE_CONNECT_HOST_PAJAK);
				detail.setHostMessage("Error when connecting to HOST PAJAK: " + responseData.getSysMessage());
			}
		} catch (Exception e) {
			LOG.warn("Exception sendTransactionToHost for " + detail, e);
			detail.setHostRc(ResultCode.ERR_UNKNOWN_PAJAK);
			detail.setHostMessage("Error processing data, error: " + e.getMessage());
		}
	}
	
	@Override
	public void sendTransactionToHost(DistributionHeader header, DistributionDetail detail) {
		LOG.debug("sendTransactionToHost: {}", detail);
		try {
			String serverUrl = settingService.getSettingAsString(SettingService.SETTING_COLLEGA_URL);
			if (StringUtils.isEmpty(serverUrl)) {
				LOG.warn("No collegaUrl, setting id {} is defined. NOT SENDING REQUEST",
						SettingService.SETTING_COLLEGA_URL);
				return;
			}
			Map<String, String> mapData = settingService.getSettingAsJson(SettingService.SETTING_COLLEGA_PARAM_DATA);
			String userGtw = mapData.get(SettingService.COLLEGA_PARAM_USERGTW);
			String channelId = mapData.get(SettingService.COLLEGA_PARAM_CHANNEL);
			String corpId = mapData.get(SettingService.COLLEGA_PARAM_CORPID);
			String prodId = mapData.get(SettingService.COLLEGA_PARAM_PRODID);
			String branch = mapData.get(SettingService.COLLEGA_PARAM_BRANCH);
			
			Date now = timeService.getCurrentTime();
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
			sdfDate.setTimeZone(TimeZone.getTimeZone(timeServer));
			
			SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
			sdfTime.setTimeZone(TimeZone.getTimeZone(timeServer));
			
			SimpleDateFormat sdfDate2 = new SimpleDateFormat("dd-MM-yyyy");
			sdfDate2.setTimeZone(TimeZone.getTimeZone(timeServer));
			
			String reqId = "00005";
			String authKey = composeHmac(reqId, now);
			
			String trxDate = sdfDate.format(now);
			String trxHour = sdfTime.format(now);
			String date = sdfDate2.format(now);
			
			FundTransferRequestVO fundTransferRequestVO = new FundTransferRequestVO();
			
			fundTransferRequestVO.setAuthKey(authKey); // required
			fundTransferRequestVO.setReqId(reqId); // required
			fundTransferRequestVO.setUserGtw(userGtw); // required
			fundTransferRequestVO.setChannelId(channelId); // required
			fundTransferRequestVO.setTxDate(trxDate); // required yyyymmdd
			fundTransferRequestVO.setTxHour(trxHour); // required hhmmss
			
			fundTransferRequestVO.setCorpId(corpId);
			fundTransferRequestVO.setProdId(prodId);
			fundTransferRequestVO.setDate(date);
			fundTransferRequestVO.setDate_rk(date);
			fundTransferRequestVO.setBranchId(branch);
			fundTransferRequestVO.setTxCcy("IDR");
			
			int numOfAcc = 2;
			fundTransferRequestVO.setNbrOfAcc(Integer.toString(numOfAcc));
			fundTransferRequestVO.setProsesId("0005");
			fundTransferRequestVO.setUserId(userGtw); //header.getApprovedUserCode());
			
			fundTransferRequestVO.setSpvId("");
			fundTransferRequestVO.setRevSts("0"); // 0: not reversal, 1: reversal
			fundTransferRequestVO.setTxType("O"); // O: Overbooking
			fundTransferRequestVO.setRefAcc(header.getSracNumber()); // SourceAcc // task.getTermAsString(TermConstant.SRAC));
			
			String fileNameOnly = FilenameUtils.getBaseName(header.getFileData());
			String txMsg = "KNT-" + fileNameOnly + "-" + detail.getAccountName();
			List<Map<String, Object>> listParam = new ArrayList<Map<String, Object>>();
			
			String txId = collegaSeqGen.getNextSequence();
			// 0: Debet, 1: Credit
			// debet first
			Map<String, Object> debetVO = new HashMap<String, Object>();
			debetVO.put("accNbr", (String) header.getSracNumber());
			debetVO.put("txMsg", (String) txMsg);
			debetVO.put("txId", (String) txId);
			debetVO.put("isFee", (Integer) 0);
	//		debetVO.put("chqBkNbr", (String) ""); // minta di hide ramadhian noor
			debetVO.put("pageNbr", (Integer) 0);
			debetVO.put("txDtDoc", (String) "");
			debetVO.put("refAcc", (String) "");
			debetVO.put("valMinBal", (Integer) 1);
			// txCode 199=Penarikan, 299=Setoran
			debetVO.put("dbCr", (String) "0");
			debetVO.put("txCode", (String) "199");
			debetVO.put("txAmt", (Double) detail.getMoneyValue());
			listParam.add(debetVO);
			
			// then credit 
			Map<String, Object> creditVO = new HashMap<String, Object>();
			creditVO.put("accNbr", (String) detail.getAccountNo());
			creditVO.put("txMsg", (String) txMsg);
			creditVO.put("txId", (String) txId);
			creditVO.put("isFee", (Integer) 0);
	//		creditVO.put("chqBkNbr", (String) ""); // minta di hide ramadhian noor
			creditVO.put("pageNbr", (Integer) 0);
			creditVO.put("txDtDoc", (String) "");
			creditVO.put("refAcc", (String) "");
			creditVO.put("valMinBal", (Integer) 1);
			// txCode 199=Penarikan, 299=Setoran
			creditVO.put("dbCr", (String) "1");
			creditVO.put("txCode", (String) "299");
			creditVO.put("txAmt", (Double) detail.getMoneyValue());
			listParam.add(creditVO);
			
			NumberFormat nf = new DecimalFormat("###0");
			String totalAmount = nf.format(detail.getMoneyValue());
			fundTransferRequestVO.setTotalAmount(totalAmount);
			fundTransferRequestVO.setParam(listParam);
			
			String jsonRequest = objectMapper.writeValueAsString(fundTransferRequestVO);
			LOG.debug("Transaction with request: {}", jsonRequest);
			
			// send request
			ResponseData responseData = transmitterAgent.sendPostMessageWithEntity(serverUrl, jsonRequest);
			LOG.debug("Transaction with responseData: {}", responseData);
			if (responseData.getResultCode() == 0) {
				// success, parsing response
				FundTransferResponseVO resp = objectMapper.readValue(responseData.getMsgToUser(), FundTransferResponseVO.class);
				if (HOST_SUCCESS_CODE.equals(resp.getrCode())) {
					String hostRef = StringUtils.trim(resp.getResult().get("TXID"));
					detail.setHostRc("0");
					detail.setHostMessage(resp.getMessage());
					detail.setHostRefNo(hostRef);
				} else {
					LOG.warn("Failed checkBalance with response: {}", responseData.getMsgToUser());
					detail.setHostRc(resp.getrCode());
					detail.setHostMessage(resp.getMessage());
				}			
			} else {
				LOG.warn("Failed checkBalance with message: {}", responseData);
				detail.setHostRc(ResultCode.ERR_UNABLE_CONNECT_HOST);
				detail.setHostMessage("Error when connecting to HOST: " + responseData.getSysMessage());
			}
		} catch (Exception e) {
			LOG.warn("Exception sendTransactionToHost for " + detail, e);
			detail.setHostRc(ResultCode.ERR_UNKNOWN);
			detail.setHostMessage("Error processing data, error: " + e.getMessage());
		}
	}
	
	private String composeHmac(String reqId, Date reqTime) {
		String ipAddress = settingService.getSettingAsString(SettingService.SETTING_LOCAL_IP_ADDRESS);
		String authSecretKey = settingService.getSettingAsString(SettingService.SETTING_COLLEGA_AUTH_KEY);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone(timeServer)); // timezone for sulteng
		
		String formatDt = sdf.format(reqTime);
		
//		String plainAuthKey = reqId + ipAddress + ISODate.formatDate(now, "yyyy-MM-ddHH:mm:ss");
//		String plainAuthKey = reqId + ipAddress + ISODate.formatDate(formatWIT(now), "yyyy-MM-ddHH:mm");
//		String plainAuthKey = reqId + ipAddress + ISODate.formatDate(formatSulteng(now), "yyyy-MM-ddHH:mm:ss");
		String plainAuthKey = reqId + ipAddress + formatDt;
		LOG.debug("Plain authKey: {}, secret key: {}", plainAuthKey, authSecretKey);
		return hmacDigest(plainAuthKey, authSecretKey);
	}
	
	private String hmacDigest(String msg, String keyString) {
		String digest = null;
		try {
			SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(key);

			byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

			StringBuffer hash = new StringBuffer();
			for (int i = 0; i < bytes.length; i++) 
			{
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) 
				{
					hash.append('0');
				}
				hash.append(hex);
			}
			digest = hash.toString();
		} catch (Exception e) {
			LOG.warn("Exception in hmacDigest " + msg, e);
		}
		return digest;
	}
	
	public void setTransmitterAgent(HttpTransmitterAgent transmitterAgent) {
		this.transmitterAgent = transmitterAgent;
	}
	public String getTimeServer() {
		return timeServer;
	}
	public void setTimeServer(String timeServer) {
		this.timeServer = timeServer;
	}

}
