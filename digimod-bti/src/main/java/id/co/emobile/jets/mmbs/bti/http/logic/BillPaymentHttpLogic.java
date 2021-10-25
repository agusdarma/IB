//package id.co.emobile.jets.mmbs.bti.http.logic;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//
//import com.emobile.jets.mmbs.lib.data.JetsConstant;
//import com.emobile.jets.mmbs.lib.data.StateConstant;
//import com.emobile.jets.mmbs.lib.data.TermConstant;
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//import com.emobile.jets.mmbs.lib.util.ResultCode;
//import com.emobile.jets.mmbs.lib.util.TelcoMatrix;
//
//import id.co.emobile.jets.mmbs.bti.http.HttpConstant;
//import id.co.emobile.jets.mmbs.bti.http.HttpMsg;
//import id.co.emobile.jets.mmbs.bti.http.data.AccountAndBalanceInqResponseVO;
//
//public class BillPaymentHttpLogic extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(BillPaymentHttpLogic.class);
////	
////	@Autowired
////	private WakafPaymentHttpLogic wakafPaymentHttpLogic;
//	
//	@Autowired
//	private ClosePaymentHttpLogic closePaymentHttpLogic;
//	
//	@Autowired
//	private OpenPaymentHttpLogic openPaymentHttpLogic;
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
//			if (task.getState() == StateConstant.FUND_TRANSFER_INQUIRY) {
//				AccountAndBalanceInqResponseVO resp = mapper.readValue(rspHttpMsg.getResponse(), AccountAndBalanceInqResponseVO.class);
//				
//				task.setResultCode(ResultCode.SUCCESS_CODE);
//				task.setTerm(TermConstant.DSAC_NAME, resp.getResult().get(HttpConstant.NTB_PARAM_FULL_NAME));
//			} else if (task.getState() == StateConstant.FUND_TRANSFER_SETTLEMENT) {
////				//FundTransferResponseVO resp = mapper.readValue(rspHttpMsg.getResponse(), FundTransferResponseVO.class);
////				
////				task.setResultCode(ResultCode.SUCCESS_CODE);
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
////		if (HOST_SUCCESS_CODE.equals(resp.getrCode())) {
////		} else {
////			task.setResultCode(resp.getrCode());
////			task.setMessageOutput(resp.getMessage());
////		}
//	}
//	
////	@Override
////	protected void processFailedResponse(TransactionTO task, HttpMsg rspHttpMsg) throws JetsException {
////		if (rspHttpMsg.getStatusCode() != HttpStatus.SC_OK ){
////			task.setResultCode(Integer.toString(rspHttpMsg.getStatusCode()));
////		}
////		
////		
////	}
//	
//	@Override
//	protected HttpMsg buildHttp(TransactionTO task) throws JetsException {
//		getLogger().debug("buildHttp: " + task.toString());
//		// validasi
//		if (task.getTerm(TermConstant.BANK_REF) == null) {
//			throw new JetsException("Unknown bill payment bank ref: " + task.getTerm(TermConstant.BANK_REF), ResultCode.BTI_UNDEFINED_PAYEE_CODE);
//		}
//		HttpMsg httpMsg = new HttpMsg();
//		String isoType = task.getTermAsString(TermConstant.ISO_TYPE);
////		if (JetsConstant.BILL_PAYMENT_OPEN.equalsIgnoreCase(isoType)) {
////			openPaymentLogic.solve(agent, task);
////		} else if (JetsConstant.BILL_PAYMENT_PHONE.equalsIgnoreCase(isoType)) {
////			phonePaymentLogic.solve(agent, task);
////		} else if (JetsConstant.BILL_PAYMENT_H2H.equalsIgnoreCase(isoType)) {
////			newPaymentLogic.solve(agent, task);
////		} else 
////		if (JetsConstant.ISO_TYPE_WAKAF.equalsIgnoreCase(isoType)) {
////			httpMsg =  wakafPaymentHttpLogic.buildHttp(task);
////		} else 
//			if (JetsConstant.BILL_PAYMENT_H2H.equalsIgnoreCase(isoType)) {
//			getLogger().debug("closePaymentHttpLogic: " + isoType);
//			httpMsg =  closePaymentHttpLogic.buildHttp(task);
//			getLogger().debug("closePaymentHttpLogic httpMsg: " + httpMsg);
//		}else if (JetsConstant.BILL_PAYMENT_OPEN.equalsIgnoreCase(isoType)) {
//			httpMsg =  openPaymentHttpLogic.buildHttp(task);
//		}else {
//			throw new JetsException("Unknown bill payment iso type: " + 
//					task.getTerm(TermConstant.ISO_TYPE), ResultCode.BTI_UNSUPPORTED_TRX);
//		}
//		
//		return httpMsg;
//	}
//	
//
//}
