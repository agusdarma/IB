//package id.co.emobile.jets.mmbs.bti.logic.stub;
//
//import java.io.IOException;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.emobile.jets.mmbs.lib.data.StateConstant;
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//import com.emobile.jets.mmbs.lib.util.ResultCode;
//
//import id.co.emobile.jets.mmbs.bti.BtiIsoDelegateAgent;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//import id.co.emobile.jets.mmbs.bti.logic.HostBtiLogic;
//
//public class RegistrationLogicStub extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(RegistrationLogicStub.class);
//
//	@Autowired
//	private AccountInfoLogicStub accountInfoLogic;
//	
//	private int delay = 0;
//	
//	@Override
//	public void solve(BtiIsoDelegateAgent agent, TransactionTO task) 
//			throws JetsException, IOException {
//		if (delay > 0) {
//			try {
//				Thread.sleep(delay);
//			} catch (InterruptedException e) {
//				LOG.error("Sleep deprivation!");
//			}
//		}
//		if (task.getState() == StateConstant.REGISTRATION_INFO) {
//			accountInfoLogic.solve(agent, task);
//		}
//		processSuccessResponse(task, null);
//	}
//
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//
//	@Override
//	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg)
//			throws JetsException {
//		task.setResultCode(ResultCode.SUCCESS_CODE);
//	}
//
//	@Override
//	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
//		return null;
//	}
//
//}
