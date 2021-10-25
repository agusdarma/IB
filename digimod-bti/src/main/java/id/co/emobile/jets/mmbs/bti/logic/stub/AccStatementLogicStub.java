//package id.co.emobile.jets.mmbs.bti.logic.stub;
//
//import java.io.IOException;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.emobile.jets.mmbs.lib.data.TermConstant;
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//import com.emobile.jets.mmbs.lib.util.ResultCode;
//
//import id.co.emobile.jets.mmbs.bti.BtiIsoDelegateAgent;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//import id.co.emobile.jets.mmbs.bti.logic.HostBtiLogic;
//
//public class AccStatementLogicStub extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(AccStatementLogicStub.class);
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
//		StringBuilder sb = new StringBuilder();
//		sb.append("03/06/15 C Rp.8.500.000,00\n");
//		sb.append("04/06/15 D Rp.300.000,00\n");
//		sb.append("05/06/15 D Rp.25.000,00\n");
//		sb.append("05/06/15 D Rp.700.000,00\n");
//		sb.append("10/06/15 C Rp.7.671,45");
//		
//		task.setResultCode(ResultCode.SUCCESS_CODE);
//		task.setTerm(TermConstant.TRX_HISTORY, sb.toString());
//	}
//
//	@Override
//	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
//		return null;
//	}
//
//}
