//package id.co.emobile.jets.mmbs.bti.logic;
//
//import java.io.IOException;
//import java.util.Set;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.emobile.jets.mmbs.lib.data.JetsConstant;
//import com.emobile.jets.mmbs.lib.data.StateConstant;
//import com.emobile.jets.mmbs.lib.data.TermConstant;
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//import com.emobile.jets.mmbs.lib.util.ResultCode;
//
//import id.co.emobile.jets.mmbs.bti.BtiIsoDelegateAgent;
//import id.co.emobile.jets.mmbs.bti.iso.HostIsoMsgFactory;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//
//public class OpenPaymentLogic extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(OpenPaymentLogic.class);
//	
//	@Autowired
//	private AccountBalanceLogic accountBalanceInquiryLogic;
//
//	private Set<String> listCCNumber;
//
//	public void setListCCNumber(Set<String> listCCNumber) {
//		this.listCCNumber = listCCNumber;
//	}
//
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//	
//	@Override
//	public void solve(BtiIsoDelegateAgent agent, TransactionTO task) 
//			throws JetsException, IOException {
//		// State : GetDefaultAccount, Inquiry, Settlement
//		int state = task.getState();
//		if (state == StateConstant.BILL_PAYMENT_GET_AMOUNT) {
//			accountBalanceInquiryLogic.solve(agent, task);
//		} else if (state == StateConstant.BILL_PAYMENT_SETTLE) {
//			// solveDefaultAccountInquiry(task);
//			super.solve(agent, task);
//		} else if (state == StateConstant.BILL_PAYMENT_INQ) {
//			String srac = task.getTerm(TermConstant.SRAC);
//			if (srac.length() < 3) {
//				task.setTerm(TermConstant.ACC_INDEX, srac);
//				srac = getDefaultAccount(agent, task);
//				task.setTerm(TermConstant.SRAC, srac);
//			}
//			task.setResultCode(ResultCode.SUCCESS_CODE);
//		} else {
//			LOG.error("Unknown state: " + state);
//			throw new JetsException("Unknown Open Payment state: " + state, ResultCode.BTI_UNKNOWN_STATE);
//		}
//	}
//
//	@Override
//	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
//		if (task.getTermAsString(TermConstant.BILL_KEY1).length() >= 6) {
//			String ccNumber = task.getTermAsString(TermConstant.BILL_KEY1).substring(0, 6);
//			if (listCCNumber.contains(ccNumber)) {
//				task.setTerm(TermConstant.PAYMENT_TYPE, JetsConstant.BILL_PAYMENT_TYPE_MANDIRI_CC);
//				return HostIsoMsgFactory.createCCPaymentSettlement(this, task);
//				// return MandiriIsoMsgFactory.createCreditCardPayment(this, task);
//			} else {
//				return HostIsoMsgFactory.createOpenBillSettlement(this, task);
//			}
//		} else {
//			return HostIsoMsgFactory.createOpenBillSettlement(this, task);
//		}
//	}
//
//	@Override
//	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg)
//			throws JetsException {
//		try {
//			String hostRef = task.getTerm(TermConstant.HREF);
//			String refNo = hostRef.substring(9, 13) + hostRef.substring(15, 20);
//			task.setTerm(TermConstant.HOST_REF_NO, refNo);
//		} catch (Exception e) {
//			throw new JetsException(ResultCode.BTI_ERROR_PARSE_ISO);
//		}
////		super.processSuccessResponse(task, rspIsoMsg);
//	}
//}
