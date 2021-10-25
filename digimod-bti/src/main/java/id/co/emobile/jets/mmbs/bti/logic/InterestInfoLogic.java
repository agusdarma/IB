//package id.co.emobile.jets.mmbs.bti.logic;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
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
//public class InterestInfoLogic extends HostBtiLogic {
//	private static final Logger LOG = LoggerFactory.getLogger(InterestInfoLogic.class);
//	
//	private static final String[] STR_ISO_TYPE = {"Tabungan", "Deposito"};
//	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
//	
//	private static final String[] STR_NOMINAL  = {
//		"< 500000 : ",
//		">= 500000 s.d < 10000000 : ",
//		">= 10000000 s.d < 100000000 : ",
//		">= 100000000 s.d < 100000000 : ",
//		"> 100000000 : "};
//
//	@Override
//	protected Logger getLogger() {
//		return LOG;
//	}
//
//	@Override
//	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
//		if (task.getTermAsInteger(TermConstant.ISO_TYPE) == 0) {
//			//Tabungan
//			return HostIsoMsgFactory.createInterestInfo(this, task, "01003");
//		} 
////		else if (task.getTermAsInteger(TermConstant.ISO_TYPE) == 1) {
////			//Deposito
////			return HostIsoMsgFactory.createInterestInfo(this, task, "030300");
////		} 
//		else {
//			LOG.error("Unknown state : " + task.getTermAsInteger(TermConstant.ISO_TYPE));
//			throw new JetsException("Unknown state!", ResultCode.BTI_UNKNOWN_STATE);
//		}
//	}
//
//	@Override
//	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg) throws JetsException {
//		LOG.debug("\n Receive Iso createKursRate "+ rspIsoMsg);
////		String bit62 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA_2);
////		Date now = new Date();
//		String bit61 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA);
//		try {
//			LOG.debug("Bit 61: " + bit61);
//			bit61 = bit61.substring(27, bit61.length()).replace(";", "\n");
////			bit61 = bit61.replace(";", "\n");
//			task.setTerm(TermConstant.MESSAGE_HOST, bit61);
//			
////			//logger.debug("\n Bit 62 : " + bit62);
////			String infoMsg = STR_ISO_TYPE[task.getTermAsInteger(TermConstant.ISO_TYPE)] + " " + sdf.format(now) + " :\n";
////			
////			int i = 0;
////			while (bit62.length() > 0) {
////				String kurs1 = bit62.substring(0, 3).trim();
////				String rate1 = bit62.substring(3, 11).trim();
////				rate1 = rate1.substring(0, rate1.length() - 1);
////				if (rate1.startsWith("0"))
////					rate1 = rate1.substring(1);
////				
////				String pct1 = bit62.substring(11, 12);
////				System.out.print("\n kurs1 : " + kurs1 + " rate1 " + rate1 + " pct1 " + pct1 );
////				if (task.getTermAsInteger(TermConstant.ISO_TYPE) == 0) {
////					infoMsg = infoMsg + kurs1 + " : " + rate1 + "\n";
////				} else {
////					infoMsg = infoMsg + Integer.parseInt(kurs1) + " bln : " + rate1 + "\n";
////				}
////				
////				bit62 = bit62.substring(12);
////				i++;
////			}
////			
////			task.setTerm(TermConstant.INT_INFO_MSG, infoMsg);
//			
////			String sellingChannel = bit62.substring(0, 13);
////			String buyingChannel = bit62.substring(13, 26);
////			
////			String sellingIBT = bit62.substring(26, 39);
////			String buyingIBT = bit62.substring(39, 52);
////			String bookRate = bit62.substring(52, 65);
////			logger.debug("\n sellingChannel : " + sellingChannel + " buyingChannel " + buyingChannel 
////					+ " sellingIBT " + sellingIBT + " buyingIBT " + buyingIBT + " bookRate " + bookRate);
////			
////			task.setTerm(JetsConstant.ECHANNEL_SELL_RATE, MandiriUtil.getFormattedAmount(sellingChannel.trim()));
////			task.setTerm(JetsConstant.ECHANNEL_BUY_RATE, MandiriUtil.getFormattedAmount(buyingChannel.trim()));
//		} catch (Exception e) {
//			LOG.error("Unable to parse bit 62: " + e);
//			throw new JetsException(ResultCode.BTI_ERROR_PARSE_ISO);
//		}
//	}
//
//
//}
