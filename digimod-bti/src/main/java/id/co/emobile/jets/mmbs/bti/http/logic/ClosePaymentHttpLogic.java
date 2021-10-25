//package id.co.emobile.jets.mmbs.bti.http.logic;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.TimeZone;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//
//import com.emobile.jets.mmbs.lib.data.StateConstant;
//import com.emobile.jets.mmbs.lib.data.TermConstant;
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//import com.emobile.jets.mmbs.lib.util.CommonUtil;
//import com.emobile.jets.mmbs.lib.util.ResultCode;
//import com.emobile.jets.mmbs.lib.util.TelcoMatrix;
//
//import id.co.emobile.jets.mmbs.bti.http.HttpConstant;
//import id.co.emobile.jets.mmbs.bti.http.HttpMsg;
//import id.co.emobile.jets.mmbs.bti.http.data.AccountAndBalanceInqRequestVO;
//import id.co.emobile.jets.mmbs.bti.http.data.AccountAndBalanceInqResponseVO;
//import id.co.emobile.jets.mmbs.bti.http.data.FundTransferRequestVO;
//import id.co.emobile.jets.mmbs.bti.iso.ISODate;
//
//public class ClosePaymentHttpLogic extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(ClosePaymentHttpLogic.class);
//
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//	
//	@Autowired
//	private MessageSource messageSource;
//	
//	@Autowired
//	private TelcoMatrix telcoMatrix;
//	
//	@Override
//	protected void processSuccessResponse(TransactionTO task, HttpMsg rspHttpMsg) throws JetsException {
//		try {
//			if (task.getState() == StateConstant.BILL_PAYMENT_INQ) {
//				AccountAndBalanceInqResponseVO resp = mapper.readValue(rspHttpMsg.getResponse(), AccountAndBalanceInqResponseVO.class);
//				
//				task.setResultCode(ResultCode.SUCCESS_CODE);
//				task.setTerm(TermConstant.DSAC_NAME, resp.getResult().get(HttpConstant.NTB_PARAM_FULL_NAME));
//			} else if (task.getState() == StateConstant.BILL_PAYMENT_SETTLE) {
//				
//				task.setResultCode(ResultCode.SUCCESS_CODE);
//				
//			} else {
//				throw new JetsException("Unknown State!", 
//						ResultCode.BTI_UNKNOWN_STATE);
//			}			
//		} catch (Exception e) {
//			getLogger().error("Error parsing JSON response: " + e);
//			throw new JetsException("Error parsing JSON response", 
//					ResultCode.BTI_ERROR_PARSE_ISO);
//		}
//	}
//	
//	@Override
//	protected HttpMsg buildHttp(TransactionTO task) throws JetsException {
//		HttpMsg httpMsg = new HttpMsg();
//		Date now = timeService.getCurrentTime();
//		FundTransferRequestVO closePaymentRequestVO = new FundTransferRequestVO();
//		String trxDate = ISODate.formatDate(now, "yyyyMMdd");
////		String trxHour = ISODate.formatDate(now, "HHmmss");
//		String trxHour1 = ISODate.formatDate(now, "HHmmss");
//		getLogger().info("trxHour1 : " + trxHour1);
//		String trxHour2 = ISODate.formatDate(now, "HHmmss",TimeZone.getTimeZone("GMT+8:00"));
//		getLogger().info("trxHour2 : " + trxHour2);
//		String trxHour = ISODate.formatDate(now, "HHmmss",TimeZone.getTimeZone("GMT+8:00"));
//		getLogger().info("userGateway : " + userGateway);
//		if (task.getState() == StateConstant.BILL_PAYMENT_INQ) {
//			AccountAndBalanceInqRequestVO accountAndBalanceInqRequestVO = new AccountAndBalanceInqRequestVO();
//			String reqId = "00002";
//			String authKey = composeHmac(reqId);
////			String authKey = "ssssss";
//			int flgSaldo = 0;
//			int flgNsb = 1;
//
//			accountAndBalanceInqRequestVO.setAuthKey(authKey); // required
//			accountAndBalanceInqRequestVO.setReqId(reqId); // required
//			accountAndBalanceInqRequestVO.setTxDate(trxDate); // required yyyymmdd
//			accountAndBalanceInqRequestVO.setTxHour(trxHour); // required hhmmss
//			accountAndBalanceInqRequestVO.setUserGtw(userGateway); // required
//			accountAndBalanceInqRequestVO.setChannelId(channelId); // required
//			accountAndBalanceInqRequestVO.setAccnbr(task.getTerm(TermConstant.DSAC)); // required
//			accountAndBalanceInqRequestVO.setFlgSaldo(flgSaldo); // required
//			accountAndBalanceInqRequestVO.setFlgNsb(flgNsb); // required
//			
//			try {
//				String jsonRequest = mapper.writeValueAsString(accountAndBalanceInqRequestVO);	
//				getLogger().info("Req Json BILL_PAYMENT_INQ: " + jsonRequest);
//				httpMsg.setEntity(jsonRequest);
//			} catch (Exception e) {
//				throw new JetsException("Failed compose JSON HTTP Entity!", 
//						ResultCode.BTI_ERROR_COMPOSE_ISO);
//			}
//		} else if (task.getState() == StateConstant.BILL_PAYMENT_SETTLE) {
//			String reqId = "00005";
//			String authKey = composeHmac(reqId);
////			String authKey = "ssssss";
//			String date = ISODate.formatDate(now, "dd-MM-yyyy");
//			
//			closePaymentRequestVO.setAuthKey(authKey); // required
//			closePaymentRequestVO.setReqId(reqId); // required
//			closePaymentRequestVO.setUserGtw(userGateway); // required
//			closePaymentRequestVO.setChannelId(channelId); // required
//			closePaymentRequestVO.setTxDate(trxDate); // required yyyymmdd
//			closePaymentRequestVO.setTxHour(trxHour); // required hhmmss
//			closePaymentRequestVO.setCorpId("128");
//			closePaymentRequestVO.setProdId("MMBS");
//			closePaymentRequestVO.setDate(date);
//			closePaymentRequestVO.setDate_rk(date);
//			closePaymentRequestVO.setBranchId("001");
//			closePaymentRequestVO.setTxCcy("IDR");
//			closePaymentRequestVO.setNbrOfAcc("2");
//			closePaymentRequestVO.setTotalAmount(task.getTerm(TermConstant.AMOUNT));
//			closePaymentRequestVO.setProsesId("0005");
//			closePaymentRequestVO.setUserId(userGateway);
//			closePaymentRequestVO.setSpvId("");
//			closePaymentRequestVO.setRevSts("0");
//			if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
//				closePaymentRequestVO.setRevSts("1");
//			}
//			closePaymentRequestVO.setTxType("O");
//			closePaymentRequestVO.setRefAcc(task.getTermAsString(TermConstant.SRAC));
//			
//			// tagihan yang dibayar
//			Map<String, String> sracParam = new HashMap<String, String>();
//			sracParam.put(HttpConstant.NTB_PARAM_ACCNBR, task.getTermAsString(TermConstant.SRAC));
//			sracParam.put(HttpConstant.NTB_PARAM_DBCR, "0");
//			sracParam.put(HttpConstant.NTB_PARAM_REFACC, "");
//			sracParam.put(HttpConstant.NTB_PARAM_TXAMT, task.getTerm(TermConstant.AMOUNT));
//			sracParam.put(HttpConstant.NTB_PARAM_TXCODE, "199");
//			sracParam.put(HttpConstant.NTB_PARAM_TXID, CommonUtil.getSysLogNoSubstring(task.getSysLogNo()));
////			if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
////				sracParam.put(HttpConstant.NTB_PARAM_TXMSG, "REV_PAY_SMS_"+ task.getTerm(TermConstant.BILLER_NAME)+"_"+ task.getTerm(TermConstant.BILL_KEY1)+"_"+ task.getTerm(TermConstant.SRAC));
////			}else{
//				sracParam.put(HttpConstant.NTB_PARAM_TXMSG, "PAY_SMS_"+ task.getTerm(TermConstant.BILLER_NAME)+"_"+ task.getTerm(TermConstant.BILL_KEY1)+"_"+ task.getTerm(TermConstant.SRAC));	
////			}
//			
//			sracParam.put(HttpConstant.NTB_PARAM_ISFEE, "0");
//			sracParam.put(HttpConstant.NTB_PARAM_CHQBKNBR, "AC");
//			sracParam.put(HttpConstant.NTB_PARAM_PAGENBR, "");
//			sracParam.put(HttpConstant.NTB_PARAM_TXDTDOC, "");
//			if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
//				sracParam.put(HttpConstant.NTB_PARAM_TXID, task.getTerm(TermConstant.TXID));
//			}
//			
//			Map<String, String> dsacParam = new HashMap<String, String>();
//			dsacParam.put(HttpConstant.NTB_PARAM_ACCNBR, task.getTermAsString(TermConstant.DSAC));
//			dsacParam.put(HttpConstant.NTB_PARAM_DBCR, "1");
//			dsacParam.put(HttpConstant.NTB_PARAM_REFACC, "");
//			dsacParam.put(HttpConstant.NTB_PARAM_TXAMT, task.getTerm(TermConstant.AMOUNT));
//			dsacParam.put(HttpConstant.NTB_PARAM_TXCODE, "299");
//			dsacParam.put(HttpConstant.NTB_PARAM_TXID, CommonUtil.getSysLogNoSubstring(task.getSysLogNo()));
////			if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
////				dsacParam.put(HttpConstant.NTB_PARAM_TXMSG, "REV_PAY_SMS_"+ task.getTerm(TermConstant.BILLER_NAME)+"_"+ task.getTerm(TermConstant.BILL_KEY1)+"_"+ task.getTerm(TermConstant.SRAC));
////			}else{
//				dsacParam.put(HttpConstant.NTB_PARAM_TXMSG, "PAY_SMS_"+ task.getTerm(TermConstant.BILLER_NAME)+"_"+ task.getTerm(TermConstant.BILL_KEY1)+"_"+ task.getTerm(TermConstant.SRAC));	
////			}
//			
//			dsacParam.put(HttpConstant.NTB_PARAM_ISFEE, "0");
//			if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
//				dsacParam.put(HttpConstant.NTB_PARAM_TXID, task.getTerm(TermConstant.TXID));
//			}
//			
//			List<Map<String, String>> listParam = new ArrayList<Map<String, String>>();
//			listParam.add(sracParam);
//			listParam.add(dsacParam);
//			
//			if(task.getTermAsDouble(TermConstant.FEE_TRX) > 0){
//				// fee yang dibayar
//				Map<String, String> sracParamFee = new HashMap<String, String>();
//				sracParamFee.put(HttpConstant.NTB_PARAM_ACCNBR, task.getTermAsString(TermConstant.SRAC));
//				sracParamFee.put(HttpConstant.NTB_PARAM_DBCR, "0");
//				sracParamFee.put(HttpConstant.NTB_PARAM_REFACC, "");
//				sracParamFee.put(HttpConstant.NTB_PARAM_TXAMT, task.getTerm(TermConstant.FEE_TRX));
//				sracParamFee.put(HttpConstant.NTB_PARAM_TXCODE, "199");
//				sracParamFee.put(HttpConstant.NTB_PARAM_TXID, CommonUtil.getSysLogNoSubstring(task.getSysLogNo()));
////				if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
////					sracParamFee.put(HttpConstant.NTB_PARAM_TXMSG, "REV_FEE_PAY_SMS_"+ task.getTerm(TermConstant.BILLER_NAME)+"_"+ task.getTerm(TermConstant.BILL_KEY1)+"_"+ task.getTerm(TermConstant.SRAC));
////				}else{
//					sracParamFee.put(HttpConstant.NTB_PARAM_TXMSG, "FEE_PAY_SMS_"+ task.getTerm(TermConstant.BILLER_NAME)+"_"+ task.getTerm(TermConstant.BILL_KEY1)+"_"+ task.getTerm(TermConstant.SRAC));	
////				}
//				
//				sracParamFee.put(HttpConstant.NTB_PARAM_ISFEE, "1");
//				sracParamFee.put(HttpConstant.NTB_PARAM_CHQBKNBR, "AC");
//				sracParamFee.put(HttpConstant.NTB_PARAM_PAGENBR, "");
//				sracParamFee.put(HttpConstant.NTB_PARAM_TXDTDOC, "");
//				if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
//					sracParamFee.put(HttpConstant.NTB_PARAM_TXID, task.getTerm(TermConstant.TXID));
//				}
//				
//				Map<String, String> dsacParamFee = new HashMap<String, String>();
//				dsacParamFee.put(HttpConstant.NTB_PARAM_ACCNBR, task.getTermAsString(TermConstant.DSAC_FEE));
//				dsacParamFee.put(HttpConstant.NTB_PARAM_DBCR, "1");
//				dsacParamFee.put(HttpConstant.NTB_PARAM_REFACC, "");
//				dsacParamFee.put(HttpConstant.NTB_PARAM_TXAMT, task.getTerm(TermConstant.FEE_TRX));
//				dsacParamFee.put(HttpConstant.NTB_PARAM_TXCODE, "299");
//				dsacParamFee.put(HttpConstant.NTB_PARAM_TXID, CommonUtil.getSysLogNoSubstring(task.getSysLogNo()));
////				if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
////					dsacParamFee.put(HttpConstant.NTB_PARAM_TXMSG, "REV_FEE_PAY_SMS_"+ task.getTerm(TermConstant.BILLER_NAME)+"_"+ task.getTerm(TermConstant.BILL_KEY1)+"_"+ task.getTerm(TermConstant.SRAC));
////				}else{
//					dsacParamFee.put(HttpConstant.NTB_PARAM_TXMSG, "FEE_PAY_SMS_"+ task.getTerm(TermConstant.BILLER_NAME)+"_"+ task.getTerm(TermConstant.BILL_KEY1)+"_"+ task.getTerm(TermConstant.SRAC));	
////				}
//				
//				dsacParamFee.put(HttpConstant.NTB_PARAM_ISFEE, "1");
//				if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
//					dsacParamFee.put(HttpConstant.NTB_PARAM_TXID, task.getTerm(TermConstant.TXID));
//				}
//				
//				listParam.add(sracParamFee);
//				listParam.add(dsacParamFee);
//				closePaymentRequestVO.setNbrOfAcc("4");
//				double amount = task.getTermAsDouble(TermConstant.AMOUNT);
//				double fee = task.getTermAsDouble(TermConstant.FEE_TRX);
//				double total = amount+fee;				
//				closePaymentRequestVO.setTotalAmount(Double.toString(total));
//			}
//			
//			
//			
//			
//			
//			
//			closePaymentRequestVO.setParam(listParam);
//			
//			try {
//				String jsonRequest = mapper.writeValueAsString(closePaymentRequestVO);	
//				if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
//					getLogger().debug("buildHttp Reversal");
//				}
//				getLogger().info("Req Json: " + jsonRequest);
//				httpMsg.setEntity(jsonRequest);
//				} catch (Exception e) {
//				throw new JetsException("Failed compose JSON HTTP Entity!", ResultCode.BTI_ERROR_COMPOSE_ISO);
//			}
//		} else {
//			throw new JetsException("Unknown State!", 
//					ResultCode.BTI_UNKNOWN_STATE);
//		}
//		return httpMsg;
//	}
//	
//
//}
