//package id.co.emobile.jets.mmbs.bti.logic;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.emobile.jets.mmbs.lib.data.TermConstant;
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//import com.emobile.jets.mmbs.lib.util.ResultCode;
//
//import id.co.emobile.jets.mmbs.bti.iso.HostIsoMsgFactory;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//
//public class ForexInfoLogic extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(ForexInfoLogic.class);
//
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//
//	@Override
//	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
//		return HostIsoMsgFactory.createForexInfo(this, task);
//	}
//
//	@Override
//	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg) throws JetsException {
//		//System.out.print("\n Receive Iso createForexRate "+ rspIsoMsg);
//		String bit62 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA_2);
//		try {
//			//System.out.print("\n Bit 62 : " + bit62);
//			String sellingChannel = bit62.substring(0, 13).trim();
//			String buyingChannel = bit62.substring(13, 26).trim();
//			String sellingIBT = bit62.substring(26, 39).trim();
//			String buyingIBT = bit62.substring(39, 52).trim();
//			//String bookRate = bit62.substring(52, 65);
//			//System.out.print("\n sellingChannel : " + sellingChannel + " buyingChannel " + buyingChannel 
//			//		+ " sellingIBT " + sellingIBT + " buyingIBT " + buyingIBT + " bookRate " + bookRate);
//			
//			if ("".equals(sellingChannel)) sellingChannel = "-";
//			if ("".equals(buyingChannel)) buyingChannel = "-";
//			if ("".equals(sellingIBT)) sellingIBT = "-";
//			if ("".equals(buyingIBT)) buyingIBT = "-";
//			
//			task.setTerm(TermConstant.ECHANNEL_SELL_RATE, sellingChannel);
//			task.setTerm(TermConstant.ECHANNEL_BUY_RATE, buyingChannel);
//			task.setTerm(TermConstant.IBT_SELL_RATE, sellingIBT);
//			task.setTerm(TermConstant.IBT_BUY_RATE, buyingIBT);
//		}
//		catch (Exception e) {
//			LOG.error("Unable to parse bit 62: " + e);
//			throw new JetsException(ResultCode.BTI_ERROR_PARSE_ISO);
//		}
//	}
//
//
//}
