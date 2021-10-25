//package id.co.emobile.samba.web.service;
//
//import java.net.SocketException;
//import java.text.DecimalFormat;
//import java.text.NumberFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Properties;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
//import id.co.emobile.samba.web.data.AccStatementDetailVO;
//import id.co.emobile.samba.web.data.AccStatementVO;
//import id.co.emobile.samba.web.data.CheckAccountVO;
//import id.co.emobile.samba.web.data.MmbsResponseVO;
//import id.co.emobile.samba.web.data.ResponseData;
//import id.co.emobile.samba.web.data.ResultCode;
//import id.co.emobile.samba.web.data.SourceAccountVO;
//import id.co.emobile.samba.web.entity.DistributionDetail;
//import id.co.emobile.samba.web.entity.DistributionHeader;
//import id.co.emobile.samba.web.http.HttpTransmitterAgent;
//import id.co.emobile.samba.web.utils.CipherUtils;
//import id.co.emobile.samba.web.utils.CommonUtil;
//
//public class TrxMmbsService implements BaseTrxService {
//	private static final Logger LOG = LoggerFactory.getLogger(TrxCollegaService.class);
//	
//	private ThreadLocal<NumberFormat> nf = new ThreadLocal<NumberFormat>() {
//		@Override
//		protected NumberFormat initialValue() {
//			return new DecimalFormat("###0");
//		}
//	};
//	
//	@Autowired
//	private SettingService settingService;
//	
//	@Autowired
//	@Qualifier("checkSequenceGenerator")
//	private SequenceGeneratorService checkSeqGenerator;
//	
//	@Autowired
//	private SourceAccountService sourceAccountService;
//	
//	private HttpTransmitterAgent transmitterAgent;
//	private Pattern patternTrf;
//	private Pattern patternBal;
//	private String channel = "WEB";
//	
////	private int requestCounter = 0;
////	private Random rnd = new Random();
//	
//	public void init() {
//		LOG.info("TrxMmbsService is started");
//	}
//	public void shutdown() {
//		LOG.info("TrxMmbsService is stopped");		
//	}
//	
//	@Override
//	public String getServiceName() {
//		return "Service MMBS";
//	}
//	
//	@Override 
//	public CheckAccountVO checkAccountForNo(String accountNo) {
//		CheckAccountVO accountVO = new CheckAccountVO();
//		
////		if ("00170002".equals(accountNo) || "00170003".equals(accountNo) || "00170005".equals(accountNo)) {
////			accountVO.setRc(ResultCode.ERR_FAILED_CHECK);
////		} else
//			accountVO.setRc(ResultCode.ERR_NOT_CHECK);
//		
//		LOG.warn("Unsupported checkAccountForNo: {}", accountNo);
//		return accountVO;
//	}
//	
//	@Override
//	public void checkBalanceForAccount(SourceAccountVO sourceAccount) {
//		LOG.debug("checkBalanceForAccount: {}", sourceAccount);
//		try {
//			int maxInterval = settingService.getSettingAsInt(SettingService.SETTING_CHECK_BALANCE_INTERVAL) * 1000;
//			long delta = System.currentTimeMillis() - sourceAccount.getLastChecked();
//			if (delta <= maxInterval) {
//				Date dtLastChecked = new Date(sourceAccount.getLastChecked());
//				LOG.warn("No need to check balance, last checked on {}", dtLastChecked);
//				return;
//			}
//			String serverUrl = settingService.getSettingAsString(SettingService.SETTING_USSD_MMBS_URL);
//			if (StringUtils.isEmpty(serverUrl)) {
//				LOG.warn("No serverUrl is defined. NOT SENDING USSD REQUEST");
//				return;
//			}
//			String ussdBalance = settingService.getSettingAsString(SettingService.SETTING_USSD_BALANCE_SYNTAX);
//			if (StringUtils.isEmpty(ussdBalance)) {
//				LOG.warn("No USSD Check Balance Syntax is defined. NOT SENDING USSD REQUEST");
//				return;	
//			}
//			String message = ussdBalance.replace("@{SRAC}", sourceAccount.getSracNumber());
//			String gatewayRef = checkSeqGenerator.getNextSequence();
//			MmbsResponseVO checkRespVO = sendRequestToMmbs(serverUrl, sourceAccount.getPhoneNo(), message, gatewayRef);
//			sourceAccount.setBalanceRc(checkRespVO.getHostRc());
//			if ("0".equals(checkRespVO.getHostRc())) {
//				Matcher m = patternBal.matcher(checkRespVO.getHostMessage());
//				if (m.find()) {
//					String strBalance = m.group(1);
//					strBalance = strBalance.replaceAll("\\.", "");
//					strBalance = strBalance.replaceAll(",", "");
//					double balance = Double.parseDouble(strBalance) / 100;
//					sourceAccount.setSracBalance(balance);
//					sourceAccount.setLastChecked(System.currentTimeMillis());
//				}
//			}
//		} catch (Exception e) {
//			LOG.warn("Exception checkBalance for " + sourceAccount, e);
//		}
//	}
//	
//	@Override
//	public void sendTransactionToHost(DistributionHeader header, DistributionDetail detail) {
//		LOG.debug("sendTransactionToHost: {}", detail);
//		try {
//			String serverUrl = settingService.getSettingAsString(SettingService.SETTING_USSD_MMBS_URL);
//			if (StringUtils.isEmpty(serverUrl)) {
//				LOG.warn("No serverUrl is defined. NOT SENDING USSD REQUEST");
//				return;
//			}
//			String ussdTransfer = settingService.getSettingAsString(SettingService.SETTING_USSD_TRANSFER_SYNTAX);
//			if (StringUtils.isEmpty(ussdTransfer)) {
//				LOG.warn("No USSD Transfer Syntax is defined. NOT SENDING USSD REQUEST");
//				return;	
//			}
//			
//			String message = ussdTransfer.replace("@{SRAC}", header.getSracNumber())
//					.replace("@{DSAC}", detail.getAccountNo())
//					.replace("@{AMOUNT}", nf.get().format(detail.getMoneyValue()));
//			String gatewayRef = detail.getSysLogNo() + "_" + detail.getDetailId() + "1";
//			MmbsResponseVO trfRespVO = sendRequestToMmbs(serverUrl, header.getPhoneNo(), message, gatewayRef);
//			if ("0".equals(trfRespVO.getHostRc()) && StringUtils.isNotEmpty(trfRespVO.getMmbsRef())) {
//				// parsing message
//				Matcher m = patternTrf.matcher(trfRespVO.getHostMessage());
//				if (m.find()) {
//					int idx1 = Integer.parseInt(m.group(1));
//					int idx2 = Integer.parseInt(m.group(2));
//					String pin = sourceAccountService.findChallengePinForAccount(header.getSourceAccountId(), idx1, idx2);
//					if (StringUtils.isEmpty(pin)) {
//						LOG.warn("Unable to find PIN for {} {}", header.getSourceAccountId(), header.getSracNumber());
//						detail.setHostRc(ResultCode.ERR_MISSING_DATA);
//						detail.setHostMessage("No PIN is defined");
//					} else {
//						String messageVerPin = trfRespVO.getMmbsRef() + " " + pin;
//						String gatewayRefVerPin = detail.getSysLogNo() + "_" + detail.getDetailId() + "2";
//						MmbsResponseVO verRespVO = sendRequestToMmbs(serverUrl, header.getPhoneNo(), messageVerPin, gatewayRefVerPin);
//						detail.setHostRc(verRespVO.getHostRc());
//						detail.setHostMessage(verRespVO.getHostMessage());
//						detail.setHostRefNo(verRespVO.getHostRef());
//					}
//				} else {
//					detail.setHostRc(ResultCode.ERR_UNABLE_PARSE_RESPONSE);
//					detail.setHostMessage("Unable to parse message " + trfRespVO.getHostMessage());
//				} // matcher
//			} else {
//				detail.setHostRc(trfRespVO.getHostRc());
//				detail.setHostMessage(trfRespVO.getHostMessage());
//				detail.setHostRefNo(trfRespVO.getHostRef());
//			} // rc == 0
//		} catch (Exception e) {
//		
//		}
//	}
//	
//	private MmbsResponseVO sendRequestToMmbs(String serverUrl, String phoneNo, String message, String gatewayRef) {
//		MmbsResponseVO respVO = new MmbsResponseVO();
//
//		try {
//
////			if (message.startsWith("TRF")) {
////				respVO.setHostRc("0");
////				respVO.setHostMessage("Transfer kepada abc/123xxx sebesar Rp.100.000,- Ketik pin ke 6 dan ke 1 dari SMS banking Anda");
////				respVO.setHostRef("123123123");
////				respVO.setMmbsRef("XAV");
////				// delay a bit to simulate real world scenario
////				try {
////					Thread.sleep(rnd.nextInt(50) * 100);
////				} catch (Exception e) {}
////				return respVO;
////			} else if (message.startsWith("XAV")) {
////				if ( (requestCounter++ % 2) == 0) {
////					respVO.setHostRc("0");
////					respVO.setHostMessage("Transfer kepada abc/123xxx sebesar Rp.100.000,- telah berhasil");
////				} else {
////					respVO.setHostRc("10");
////					respVO.setHostMessage("Transfer kepada abc/123xxx gagal");
////				}
////				respVO.setHostRef("123123124");
////				// delay a bit to simulate real world scenario
////				try {
////					Thread.sleep(rnd.nextInt(100) * 100);
////				} catch (Exception e) {}
////				return respVO;
////			} else if (message.startsWith("SAL")) {
////				respVO.setHostRc("0");
////				respVO.setHostMessage("Saldo rek. Anda no xxx123 pada tgl 07/06/2019 jam 13:35 Rp.50.000,00");
////				respVO.setHostRef("123123123");
////				// delay a bit to simulate real world scenario
////				try {
////					Thread.sleep(rnd.nextInt(50) * 100);
////				} catch (Exception e) {}
////				return respVO;
////			}
//			
//			StringBuilder sb = new StringBuilder();
//			String msgHexa = CommonUtil.toHexString(message.getBytes());
//			sb.append("p=").append(phoneNo);
//			sb.append("&m=").append(msgHexa);
//			sb.append("&g=").append(gatewayRef);
//			sb.append("&c=").append(channel);		
//			
//			LOG.debug("Param Transaction Sent To USSD MMBS -> " + sb.toString());
//			String encKey = settingService.getSettingAsString(SettingService.SETTING_USSD_MMBS_ENC_KEY);
//			String encMessage = encrypt(sb.toString(), encKey);
//			List<NameValuePair> params = new ArrayList<NameValuePair>();
//			params.add(new BasicNameValuePair("x", encMessage));
//			
//			ResponseData responseData = transmitterAgent.sendGetMessage(serverUrl, params);
//			if (0 == responseData.getResultCode()) {
//				// success
//				String respString = responseData.getMsgToUser();
//				String[] respData = respString.split("=");
//				if (respData.length != 2 || !respData[0].equals("x")) {
//					LOG.warn("Invalid response from HOST: " + respString);
//					respVO.setHostRc(ResultCode.ERR_UNABLE_PARSE_RESPONSE);
//					respVO.setHostMessage("Invalid response: " + respString);
//				} else {
//					String plainData = decrypt(respData[1], encKey);
//					LOG.debug("Plain Response: {}", plainData);
//					// got response from HOST
//					// rc=
//					// m=hexa --> encrypted 3DES(password 16byte) if encryptionKey param is set
//					// logno=msglogno
//					// inq=XAA 
//					// cr=hexa hash254(user.upperCase-password-rc-logno-inq)
//					// **for favorite only after success settlement**
//					// name=name (dsac name for transfer)
//					Properties respParams = new Properties();
//					CommonUtil.parseQueryString(plainData, respParams);
//					String rc = respParams.getProperty("rc");
//					String hexMsg = respParams.getProperty("m");
//					String asciiMsg = new String(CommonUtil.toHexByte(hexMsg));
//					String hostRefNo = respParams.getProperty("syslogno");
//					String mmbsRef = respParams.getProperty("mmbsref");
//					respVO.setHostRc(rc);
//					respVO.setHostMessage(asciiMsg);
//					respVO.setHostRef(hostRefNo);
//					respVO.setMmbsRef(mmbsRef);
//				}
//			} else {
//				// failed
//				LOG.warn("Invalid response from HOST: " + responseData);
//				respVO.setHostRc(ResultCode.ERR_UNABLE_PARSE_RESPONSE);
//				respVO.setHostMessage("Invalid response: " + responseData.getSysMessage());
//			}			
//		} catch (Exception e) {
//			if (e instanceof SocketException)
//				LOG.warn("SocketException when sending request for phoneNo {} with error: {}", phoneNo, e.getMessage());
//			else
//				LOG.warn("Exception when sending request for phoneNo " + phoneNo, e);
//			respVO.setHostRc(ResultCode.ERR_UNKNOWN);
//			String errMsg = e.getMessage();
//			if(errMsg != null && errMsg.length() > 500)
//				errMsg = errMsg.substring(0, 498) + "..";
//			respVO.setHostMessage(errMsg);
//		}  
//		return respVO;
//	}
//	
//	@Override
//	public AccStatementVO checkStatementForAccount(SourceAccountVO sourceAccount, Date startDate, Date endDate) {
//		AccStatementVO vo = new AccStatementVO();
////		vo.setStatementRc(ResultCode.ERR_NOT_CHECK);
////		vo.setMessage("Unsupported Feature");
//		vo.setStatementRc("0");
//		vo.setStartBalance(100000);
//		vo.setEndBalance(200000);
//		AccStatementDetailVO detail1 = new AccStatementDetailVO();
//		detail1.setTxCode("123"); // TXCODE : String Transaction Code
//		detail1.setTxDate(new Date()); // TXDATE : Date Transaction Date(yyyy-MM-dd )
//		detail1.setTxAmount(50000); // TXAMT : Float Transaction Amount
//		detail1.setTxDateSettle(new Date());;  // TXDTSTLMN : Date Transaction Settlement Date (yyyy-MM-dd HH:mm:Sss )
//		detail1.setAccNumber(sourceAccount.getSracNumber());  // ACCNBR : String Account Number
//		detail1.setTxId("123123123");  // TXID : String Transaction ID
//		detail1.setDebetCredit(1);; //DBCR : Integer Transaction Type (0 – Debit, 1- Credit)
//		detail1.setTxMsg("Transfer ke ABC");  // TXMSG : String Transaction Message
//		detail1.setTxBranch("000");  // TXBRANCH : String Transaction Branch Location
//		detail1.setTxCurrency("IDR");  // TXCCY : String Transaction Currency
//		detail1.setUserId("USER01");;  // USERID : String
//		vo.getListDetail().add(detail1);
//		AccStatementDetailVO detail2 = new AccStatementDetailVO();
//		detail2.setTxCode("123"); // TXCODE : String Transaction Code
//		detail2.setTxDate(new Date()); // TXDATE : Date Transaction Date(yyyy-MM-dd )
//		detail2.setTxAmount(150000); // TXAMT : Float Transaction Amount
//		detail2.setTxDateSettle(new Date());;  // TXDTSTLMN : Date Transaction Settlement Date (yyyy-MM-dd HH:mm:Sss )
//		detail2.setAccNumber(sourceAccount.getSracNumber());  // ACCNBR : String Account Number
//		detail2.setTxId("123123123");  // TXID : String Transaction ID
//		detail2.setDebetCredit(0);; //DBCR : Integer Transaction Type (0 – Debit, 1- Credit)
//		detail2.setTxMsg("Transfer dari XYZ");  // TXMSG : String Transaction Message
//		detail2.setTxBranch("000");  // TXBRANCH : String Transaction Branch Location
//		detail2.setTxCurrency("IDR");  // TXCCY : String Transaction Currency
//		detail2.setUserId("USER01");;  // USERID : String
//		vo.getListDetail().add(detail2);
//		return vo;
//	}
//	
//	private String encrypt(String input, String key) {
//		if (StringUtils.isEmpty(key))
//			return input;
//		return CipherUtils.encryptDESede(input, key);
//	}
//	
//	private String decrypt(String input, String key) {
//		if (StringUtils.isEmpty(key))
//			return input;
//		return CipherUtils.decryptDESede(input, key);
//	}
//	
//	public void setPatternTrf(String pattern) {
//		this.patternTrf = Pattern.compile(pattern);
//	}
//
//	public void setChannel(String channel) {
//		this.channel = channel;
//	}
//
//	public void setPatternBal(String pattern) {
//		this.patternBal = Pattern.compile(pattern);
//	}
//	
//	public void setTransmitterAgent(HttpTransmitterAgent transmitterAgent) {
//		this.transmitterAgent = transmitterAgent;
//	}
//	@Override
//	public void sendTransactionToHostPajak(DistributionHeader header, DistributionDetail detail) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	
//}
