package id.co.emobile.jets.mmbs.bti.http.logic;
//package com.emobile.jets.mmbs.bti.http.logic;
//
//import java.io.UnsupportedEncodingException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.util.Date;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//
//import org.codehaus.jackson.map.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
//import com.emobile.jets.mmbs.bti.http.HttpMsg;
//import com.emobile.jets.mmbs.bti.http.data.GeneralResponseVO;
//import com.emobile.jets.mmbs.bti.iso.ISODate;
//import com.emobile.jets.mmbs.lib.data.TermConstant;
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.service.AppsTimeService;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//import com.emobile.jets.mmbs.lib.service.SequenceGeneratorService;
//import com.emobile.jets.mmbs.lib.util.ResultCode;
//
//
//public abstract class HostBtiHttpLogic extends AbstractHttpLogic {
//	
//	protected static final String HOST_SUCCESS_CODE = "00";
//	
//	private String terminalId;
//	private String acquirerId;
//	
//	protected ObjectMapper mapper = new ObjectMapper();
//	
//	@Autowired
//	protected AppsTimeService timeService;
//	
//	@Autowired
//	@Qualifier("btiHttpSequenceManager")
//	protected SequenceGeneratorService sequenceManager;
//	
//	public String ipAddress;
//	public String userGateway;
//	public String authSecretKey;
//	public String channelId;
//	
//	@Override
//	protected void processSuccessResponse(TransactionTO task, HttpMsg rspHttpMsg)
//			throws JetsException {
//		try {
//			GeneralResponseVO resp = mapper.readValue(rspHttpMsg.getResponse(), GeneralResponseVO.class);
//			if (HOST_SUCCESS_CODE.equals(resp.getRc())) {
//				task.setResultCode(ResultCode.SUCCESS_CODE);
//			}else{
//				task.setResultCode("H" + resp.getRc());
//			}
//			task.setMessageOutput(resp.getJsonResponse());
//		} catch (Exception e) {
//			getLogger().error("Error parsing JSON response: " + e);
//			throw new JetsException("Error parsing JSON response", 
//					ResultCode.BTI_ERROR_PARSE_ISO);
//		}
//	}
//
//	protected void processHostRef(TransactionTO task, HttpMsg rspHttpMsg) {
//		task.setTerm(TermConstant.HREF, task.getSysLogNo());
//	}
//	
//	protected static String hmacDigest(String msg, String keyString) {
//		String digest = null;
//		try {
//			SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacSHA1");
//			Mac mac = Mac.getInstance("HmacSHA1");
//			mac.init(key);
//
//			byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));
//
//			StringBuffer hash = new StringBuffer();
//			for (int i = 0; i < bytes.length; i++) {
//				String hex = Integer.toHexString(0xFF & bytes[i]);
//				if (hex.length() == 1) {
//					hash.append('0');
//				}
//				hash.append(hex);
//			}
//			digest = hash.toString();
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (InvalidKeyException e) {
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//	    return digest;
//	}
//	
//	protected String composeHmac(String reqId) {
//		Date now = timeService.getCurrentTime();
//		
//		String plainAuthKey = reqId + ipAddress + ISODate.formatDate(now, "yyyy-MM-ddHH:mm:ss");
//		getLogger().debug("Plain authKey: {}, secret key: {}", plainAuthKey, authSecretKey);
//		return hmacDigest(plainAuthKey, authSecretKey);
//	}
//	
////	protected String getSysLogNoSubstring(String originalSysLogNo) {
////		String subStringSysLogNo = originalSysLogNo;
////		getLogger().info("originalSysLogNo: " + subStringSysLogNo);
////		if(subStringSysLogNo.length()>10){
////			subStringSysLogNo = subStringSysLogNo.substring(4, subStringSysLogNo.length());
////		}
////		getLogger().info("subStringSysLogNo: " + subStringSysLogNo);
////		return subStringSysLogNo;
////	}
//
//	public void setIpAddress(String ipAddress) {
//		this.ipAddress = ipAddress;
//	}
//
//	public void setUserGateway(String userGateway) {
//		getLogger().debug("SETTER userGateway: {}", userGateway);
//		this.userGateway = userGateway;
//	}
//
//	public void setAuthSecretKey(String authSecretKey) {
//		getLogger().debug("SETTER setAuthSecretKey: {}", authSecretKey);
//		this.authSecretKey = authSecretKey;
//	}
//
//	public void setChannelId(String channelId) {
//		this.channelId = channelId;
//	}
//
//	public String getTerminalId() {
//		return terminalId;
//	}
//
//	public void setTerminalId(String terminalId) {
//		this.terminalId = terminalId;
//	}
//
//	public String getAcquirerId() {
//		return acquirerId;
//	}
//
//	public void setAcquirerId(String acquirerId) {
//		this.acquirerId = acquirerId;
//	}
//
//	public String getUserGateway() {
//		return userGateway;
//	}
//
//	public String getAuthSecretKey() {
//		return authSecretKey;
//	}
//
//	public String getChannelId() {
//		return channelId;
//	}
//	
//}
