//package id.co.emobile.jets.mmbs.bti.logic.stub;
//
//import java.io.IOException;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.emobile.jets.mmbs.lib.data.StateConstant;
//import com.emobile.jets.mmbs.lib.data.TermConstant;
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//
//import id.co.emobile.jets.mmbs.bti.BtiIsoDelegateAgent;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//import id.co.emobile.jets.mmbs.bti.logic.HostBtiLogic;
//
//public class FundTransferLogicStub extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(FundTransferLogicStub.class);
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
//		if (task.getState() == StateConstant.FUND_TRANSFER_INQUIRY) {
//			task.setTerm(TermConstant.SRAC_CURR, "IDR");
//			task.setTerm(TermConstant.DSAC_CURR, "IDR");
//			task.setTerm(TermConstant.DSAC_NAME, "Grace");
//		} else if (task.getState() == StateConstant.FUND_TRANSFER_SETTLEMENT) {
//			task.setTerm(TermConstant.HOST_REF_NO, "123450010290");
//		}
//	}
//
//	@Override
//	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
//		return null;
//	}
//	
//}
