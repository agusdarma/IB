//package id.co.emobile.jets.mmbs.bti.logic.stub;
//
//import java.io.IOException;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.emobile.jets.mmbs.lib.data.TermConstant;
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//import com.emobile.jets.mmbs.lib.util.CommonUtil;
//import com.emobile.jets.mmbs.lib.util.TelcoMatrix;
//
//import id.co.emobile.jets.mmbs.bti.BtiIsoDelegateAgent;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//import id.co.emobile.jets.mmbs.bti.logic.HostBtiLogic;
//
//public class AirtimeRefillLogicStub extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(AirtimeRefillLogicStub.class);
//
//	@Autowired
//	private TelcoMatrix telcoMatrix;
//	
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//
//	@Override
//	public void solve(BtiIsoDelegateAgent agent, TransactionTO task) 
//			throws JetsException, IOException {
//		processSuccessResponse(task, null);
//	}
//	
//	@Override
//	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg)
//			throws JetsException {
//		String buyerPhoneNo = CommonUtil.formatPhoneLocal(task.getTerm(TermConstant.BUYER_PHONE));
//		
//		task.setTerm(TermConstant.PRODUCT_NAME, telcoMatrix.getProductName(buyerPhoneNo));
//		
//		task.setTerm(TermConstant.VOUCHER_CODE, "1234567890123456789012345");
//		task.setTerm(TermConstant.PHONE_PREFIX, buyerPhoneNo.substring(0, 4));
//		task.setTerm(TermConstant.HOST_REF_NO, "123450010290");
//	}
//
//	@Override
//	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
//		return null;
//	}
//
//}
