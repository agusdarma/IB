//package id.co.emobile.jets.mmbs.bti.logic;
//
//import java.io.IOException;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//
//import id.co.emobile.jets.mmbs.bti.BtiIsoDelegateAgent;
//import id.co.emobile.jets.mmbs.bti.iso.HostIsoMsgFactory;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//
//public class ChangePinLogic extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(ChangePinLogic.class);
//
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//	
//	@Override
//	public void solve(BtiIsoDelegateAgent agent, TransactionTO task) 
//			throws JetsException, IOException {
//		super.solve(agent, task);
//	}
//	
//	@Override
//	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
//		return HostIsoMsgFactory.createChangePin(this, task);
//	}
//
//	@Override
//	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg)
//			throws JetsException {
//	}
//}
