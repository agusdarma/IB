//package id.co.emobile.jets.mmbs.bti.logic;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//
//public class ValidatePinLogic extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(ValidatePinLogic.class);
//
//	@Override
//	public Logger getLogger() {
//		return LOG;
//	}
//	
//	@Override
//	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
//		return null;
////		return MandiriIsoMsgFactory.createAccountBalance(this, task, "1");
//	}
//	
//	@Override
//	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg)
//			throws JetsException {
//	}
//
//
//}
