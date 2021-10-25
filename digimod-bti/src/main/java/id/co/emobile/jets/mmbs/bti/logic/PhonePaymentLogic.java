//package id.co.emobile.jets.mmbs.bti.logic;
//
//import java.io.IOException;
//import java.util.Set;
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.emobile.jets.mmbs.lib.data.StateConstant;
//import com.emobile.jets.mmbs.lib.data.TermConstant;
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//import com.emobile.jets.mmbs.lib.util.ResultCode;
//
//import id.co.emobile.jets.mmbs.bti.BtiIsoDelegateAgent;
//import id.co.emobile.jets.mmbs.bti.CommonUtils;
//import id.co.emobile.jets.mmbs.bti.iso.HostIsoMsgFactory;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//
//public class PhonePaymentLogic extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(PhonePaymentLogic.class);
//	
//	private Set<String> prefixSet;
//	private String internalPhoneNo;
//	private String internalPin;
//
//	@Autowired
//	private AccountBalanceLogic accountBalanceInquiryLogic;
//
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//	
//	@Override
//	public void solve(BtiIsoDelegateAgent agent, TransactionTO task) 
//			throws JetsException, IOException {
//		//State : GetDefaultAccount, Inquiry, Settlement
//		int state = task.getState();
//		if (state == StateConstant.BILL_PAYMENT_GET_AMOUNT) {
//			accountBalanceInquiryLogic.solve(agent, task);
//		} else if (state == StateConstant.BILL_PAYMENT_INQ) {
//			solveDefaultAccountInquiry(agent, task);
//			solveInquiry(agent, task);
//		} else if (state == StateConstant.BILL_PAYMENT_SETTLE) {
//			super.solve(agent, task);
//		} else {
//			LOG.error("Unknown state: " + state);
//			throw new JetsException("Unknown phone paymnet state: " + state, ResultCode.BTI_UNKNOWN_STATE);
//		}
//	}
//
//	private void solveInquiry(BtiIsoDelegateAgent agent, TransactionTO task) throws JetsException,
//			IOException {
//		String billAccountNo = formatBit61Phone(task.getTerm(TermConstant.BILL_KEY1));
//		task.setTerm(TermConstant.BIT61, billAccountNo);
//		//pin is null when it is bill presentment transaction
//		IsoMsg isoMsg = null;
////		if (task.getTerm(JetsConstant.PIN) == null) {
//		if (StringUtils.isEmpty(task.getTermAsString(TermConstant.PIN))) {
//			isoMsg = HostIsoMsgFactory.createPhoneBillInquiry(this, task, internalPhoneNo, internalPin);
////			System.out.println("Send To DSP: " + isoMsg);
//		} else {
//			isoMsg = HostIsoMsgFactory.createPhoneBillInquiry(this, task);
////			System.out.println("Send To DSP: " + isoMsg);
//		}
//		IsoMsg rspIsoMsg = processToHost(agent, isoMsg);
//		if (isSuccess(rspIsoMsg)) {
//			// bit61 format: MSISDN<20>+AreaCode<4>+RRNO<22>
//			String bit61 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA);
//			task.setTerm(TermConstant.BREF, bit61.substring(24, 46));
//			String bit62 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA_2);
//			// bit62 format: Amount<17> + D/C <1> + phone holder name <40>
//			task.setTerm(TermConstant.AMOUNT, CommonUtils.getAmount(bit62));
//			// do we need pass phone holder name ?
//			// keep bit61 and bit126, to be reused in settlement
//			task.setTerm(TermConstant.BIT61, rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA));
//			String bit126 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA_3);
//			task.setTerm(TermConstant.BIT126, bit126);
//			task.setTerm(TermConstant.CUSTOMER_NAME, bit126.substring(74, 99).trim());
//			task.setTerm(TermConstant.ISO_SUB_TYPE, "phone");
//		} else {
//			throw new JetsException(getResponseCode(rspIsoMsg));
//		}
//		task.setResultCode(getResponseCode(rspIsoMsg));
//	}
//	
//	private void solveDefaultAccountInquiry(BtiIsoDelegateAgent agent, TransactionTO task) throws JetsException, IOException {
//		// Check for default account if empty
//		String srac = task.getTerm(TermConstant.SRAC);
//		if (srac.length() < 3) {
//			task.setTerm(TermConstant.ACC_INDEX, srac);
//			srac = getDefaultAccount(agent, task);
//			task.setTerm(TermConstant.SRAC, srac);
//		}
//	}
//
//	@Override
//	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
//		return HostIsoMsgFactory.createPhoneBillSettlement(this, task);
//	}
//
//	/*
//	 * phone no start with: 021, 022, 024, 031, 061, will be added with 0
//	 */
//	private String formatBit61Phone(String phone) throws JetsException {
//		if (phone == null) {
//			throw new JetsException("Buyer phone no is null!" , ResultCode.BTI_ERROR_COMPOSE_ISO);
//		}
//		if (phone.startsWith("+")) {
//			phone = phone.substring(1);
//		}
//		if (phone.startsWith("62")) {
//			phone = 0 + phone.substring(2);
//		}
//		String prefix = phone.substring(0, 3);
//		if (prefixSet.contains(prefix)) {
//			phone = "0" + phone;
//		}
//		return phone;
//	}
//
//	public void setPrefixSet(Set<String> prefixSet) {
//		this.prefixSet = prefixSet;
//	}
//
//	@Override
//	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg)
//			throws JetsException {
//		try {
//			String bit61 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA);
//			task.setTerm(TermConstant.PHONE_PREFIX, bit61.substring(40, 44));
//			
//			String hostRef = task.getTerm(TermConstant.HREF);
//			String refNo = hostRef.substring(9, 13) + hostRef.substring(15, 20);
//			task.setTerm(TermConstant.HOST_REF_NO, refNo);
//		} catch (Exception e) {
//			throw new JetsException(ResultCode.BTI_ERROR_PARSE_ISO);
//		}
////		super.processSuccessResponse(task, rspIsoMsg);
//	}
//
//	public void setInternalPhoneNo(String internalPhoneNo) {
//		this.internalPhoneNo = internalPhoneNo;
//	}
//
//	public void setInternalPin(String internalPin) {
//		this.internalPin = internalPin;
//	}
//}
