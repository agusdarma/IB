//package id.co.emobile.jets.mmbs.bti.logic;
//
//import java.io.IOException;
//import java.util.Map;
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
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//
//public class BillPaymentLogic extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(BillPaymentLogic.class);
//	
//	@Autowired
//	private PhonePaymentLogic phonePaymentLogic;
//	
//	@Autowired
//	private NewPaymentLogic newPaymentLogic;
//	
//	@Autowired
//	private OpenPaymentLogic openPaymentLogic;
//	
//	@Autowired
//	private UnifiedPaymentLogic unifiedPaymentLogic;
//	
//	private Set<String> listOpenPayment;
//
//	private Set<String> listPhonePayment;
//	
//	private Set<String> listNewPayment;
//	
//	private Set<String> listUBPayment;
//	
//	private Map<String, String> billerMap;
//	
//	private Map<String, String> billTypeMap;
//
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//	
//	@Override
//	public void solve(BtiIsoDelegateAgent agent, TransactionTO task) 
//			throws JetsException, IOException {
//		if (task.getTerm(TermConstant.BANK_REF) == null) {
//			throw new JetsException("Unknown bill payment bank ref: " + task.getTerm(TermConstant.BANK_REF), ResultCode.BTI_UNDEFINED_PAYEE_CODE);
//		}
//		if (task.getState() == StateConstant.BILL_PAYMENT_INQ) {
//			String srac = task.getTerm(TermConstant.SRAC);
//			if (srac.length() < 3) {
//				task.setTerm(TermConstant.ACC_INDEX, srac);
//				srac = getDefaultAccount(agent, task);
//				task.setTerm(TermConstant.SRAC, srac);
//			}
//		}
//		String isoType = task.getTermAsString(TermConstant.ISO_TYPE);
//		if (JetsConstant.BILL_PAYMENT_UBP_INQ.equalsIgnoreCase(isoType)) {
//			task.setTerm(TermConstant.IS_UBP, true);
//			unifiedPaymentLogic.solve(agent, task);
//		} else if (JetsConstant.BILL_PAYMENT_OPEN.equalsIgnoreCase(isoType)) {
//			openPaymentLogic.solve(agent, task);
//		} else if (JetsConstant.BILL_PAYMENT_PHONE.equalsIgnoreCase(isoType)) {
//			phonePaymentLogic.solve(agent, task);
//		} else if (JetsConstant.BILL_PAYMENT_H2H.equalsIgnoreCase(isoType)) {
//			newPaymentLogic.solve(agent, task);
//		} else {
//			throw new JetsException("Unknown bill payment iso type: " + 
//					task.getTerm(TermConstant.ISO_TYPE), ResultCode.BTI_UNSUPPORTED_TRX);
//		}
//	}
//
//	@Override
//	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
//		// no implementation as solve is overidden.
//		return null;
//	}
//	
//	public void setListOpenPayment(Set<String> listOpenPayment) {
//		this.listOpenPayment = listOpenPayment;
//	}
//
//	public void setListPhonePayment(Set<String> listPhonePayment) {
//		this.listPhonePayment = listPhonePayment;
//	}
//
//	public void setListNewPayment(Set<String> listNewPayment) {
//		this.listNewPayment = listNewPayment;
//	}
//
//	public void setListUBPayment(Set<String> listUBPayment) {
//		this.listUBPayment = listUBPayment;
//	}
//
//	public void setBillerMap(Map<String, String> billerMap) {
//		this.billerMap = billerMap;
//	}
//
//	public void setBillTypeMap(Map<String, String> billTypeMap) {
//		this.billTypeMap = billTypeMap;
//	}
//
//	@Override
//	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg)
//			throws JetsException {
//	}
//}
