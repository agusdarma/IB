//package id.co.emobile.jets.mmbs.bti.http.logic;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
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
//public class OpenPaymentHttpLogic extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(OpenPaymentHttpLogic.class);
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
//				AccountAndBalanceInqResponseVO resp = mapper.readValue(rspHttpMsg.getResponse(), AccountAndBalanceInqResponseVO.class);
//				getLogger().debug("respon host settlement: " + resp);
//				task.setResultCode(ResultCode.SUCCESS_CODE);
//				task.setTerm(TermConstant.TXID, resp.getResult().get(HttpConstant.NTB_PARAM_TXID_FOR_REVERSAL));
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
//		FundTransferRequestVO openPaymentRequestVO = new FundTransferRequestVO();
//		String trxDate = ISODate.formatDate(now, "yyyyMMdd");
//		String trxHour = ISODate.formatDate(now, "HHmmss");
//		
//		if (task.getState() == StateConstant.BILL_PAYMENT_INQ) {
//			AccountAndBalanceInqRequestVO accountAndBalanceInqRequestVO = new AccountAndBalanceInqRequestVO();
//			String reqId = "00002";
//			String authKey = composeHmac(reqId);
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
//				getLogger().info("Req Json: " + jsonRequest);
//				httpMsg.setEntity(jsonRequest);
//			} catch (Exception e) {
//				throw new JetsException("Failed compose JSON HTTP Entity!", 
//						ResultCode.BTI_ERROR_COMPOSE_ISO);
//			}
//		} else if (task.getState() == StateConstant.BILL_PAYMENT_SETTLE) {
//			String reqId = "00005";
//			String authKey = composeHmac(reqId);
//			String date = ISODate.formatDate(now, "dd-MM-yyyy");
//			
//			openPaymentRequestVO.setAuthKey(authKey); // required
//			openPaymentRequestVO.setReqId(reqId); // required
//			openPaymentRequestVO.setUserGtw(userGateway); // required
//			openPaymentRequestVO.setChannelId(channelId); // required
//			openPaymentRequestVO.setTxDate(trxDate); // required yyyymmdd
//			openPaymentRequestVO.setTxHour(trxHour); // required hhmmss
//			openPaymentRequestVO.setCorpId("128");
//			openPaymentRequestVO.setProdId("MMBS");
//			openPaymentRequestVO.setDate(date);
//			openPaymentRequestVO.setDate_rk(date);
//			openPaymentRequestVO.setBranchId("001");
//			openPaymentRequestVO.setTxCcy("IDR");
//			openPaymentRequestVO.setNbrOfAcc("2");
//			openPaymentRequestVO.setTotalAmount(task.getTerm(TermConstant.AMOUNT));
//			openPaymentRequestVO.setProsesId("0005");
//			openPaymentRequestVO.setUserId(userGateway);
//			openPaymentRequestVO.setSpvId("");
//			openPaymentRequestVO.setRevSts("0");
//			if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
//				openPaymentRequestVO.setRevSts("1");
//			}
//			openPaymentRequestVO.setTxType("O");
//			openPaymentRequestVO.setRefAcc(task.getTermAsString(TermConstant.SRAC));
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
//				openPaymentRequestVO.setNbrOfAcc("4");
//				double amount = task.getTermAsDouble(TermConstant.AMOUNT);
//				double fee = task.getTermAsDouble(TermConstant.FEE_TRX);
//				double total = amount+fee;				
//				openPaymentRequestVO.setTotalAmount(Double.toString(total));
//			}
//			
//			
//			
//			
//			
//			
//			openPaymentRequestVO.setParam(listParam);
//			
//			try {
//				String jsonRequest = mapper.writeValueAsString(openPaymentRequestVO);	
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
