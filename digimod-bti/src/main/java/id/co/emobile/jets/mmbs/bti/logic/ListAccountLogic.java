//package id.co.emobile.jets.mmbs.bti.logic;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.emobile.jets.mmbs.lib.data.StateConstant;
//import com.emobile.jets.mmbs.lib.data.TermConstant;
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//
//import id.co.emobile.jets.mmbs.bti.iso.HostIsoMsgFactory;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//
//public class ListAccountLogic extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(ListAccountLogic.class);
//
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//	
//	@Override
//	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
//		if (task.getState() == StateConstant.STATE_CIF_REG_CHECK_ACC) {
//			return HostIsoMsgFactory.createInqAccSulteng(this, task);
////			return HostIsoMsgFactory.createList8Account(this, task);
//		}
//		return null;
////		task.setTerm(TermConstant.ISO_TYPE, 0);
////		return HostIsoMsgFactory.createAccountBalance(this, task, task.getTerm(TermConstant.SRAC));
//	}
//
//	@Override
//	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg) throws JetsException {
//		if (task.getState() == StateConstant.STATE_CIF_REG_CHECK_ACC) {
//
//			String bit61 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA);
//			String custName = "Nama:" + bit61.substring(26, bit61.length()).trim(); 
//			task.setTerm(TermConstant.REMARKS, custName);
////			StringBuilder sb = new StringBuilder();
////			int i=1;
////			while (bit61.length() > 12) {
////				if (bit61.length() > 19){
////					sb.append(i + ". ")
////					.append(bit61.substring(0,19).trim())
////					.append("\n");
////					bit61 = bit61.substring(19);
////				} else if (bit61.length() < 19) {
////					sb.append(i + ". ")
////					.append(bit61.substring(0,13).trim());
////					bit61 = bit61.substring(13);
////				}
////				i++;
////			}
////			task.setTerm(TermConstant.LIST_SRAC, sb.toString());
//		}
//	}
//}
