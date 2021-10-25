//package id.co.emobile.jets.mmbs.bti.other.channel.iso;
//
//import java.util.Date;
//
//import com.emobile.jets.mmbs.lib.data.TermConstant;
//import com.emobile.jets.mmbs.lib.data.TransactionTO;
//import com.emobile.jets.mmbs.lib.service.JetsException;
//import com.emobile.jets.mmbs.lib.util.ResultCode;
//
//import id.co.emobile.jets.mmbs.bti.iso.HostIsoMsgFactory;
//import id.co.emobile.jets.mmbs.bti.iso.ISODate;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsgHeader;
//
//public class ChangePinFromAtm implements BaseIsoLogic{
//	
//	private String trxCode;
//
//	public void setTrxCode(String trxCode) {
//		this.trxCode = trxCode;
//	}
//
//	@Override
//	public void composeTermIso(IsoMsg isoReq, TransactionTO task)
//			throws JetsException {
//		task.setTrxCode(trxCode);
//		task.setTerm(TermConstant.PIN, isoReq.getItem(IsoMsg.ADDITIONAL_DATA)); //old pin 16 digits with hex Left padded space
//		task.setTerm(TermConstant.NEW_PIN, isoReq.getItem(IsoMsg.PIN_BLOCK)); //new pin 16 digits with 3DES hexa
//		task.setTerm(TermConstant.CARD_NUMBER, isoReq.getItem(IsoMsg.PAN)); //card no		
//	}
//
//	@Override
//	public IsoMsg composeIsoResponse(IsoMsg isoReq, TransactionTO task)
//			throws Exception {
//		IsoMsg isoMsg = (IsoMsg) HostIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_RESPONSE);
//		isoMsg.setItemWithLength(IsoMsg.PAN, isoReq.getItem(IsoMsg.PAN), 2); // bit2
//		isoMsg.setItem(IsoMsg.PROCESSING_CODE, isoReq.getItem(IsoMsg.PROCESSING_CODE)); // bit3
//		isoMsg.setItem(IsoMsg.TIMESTAMP, isoReq.getItem(IsoMsg.TIMESTAMP)); // bit7
//		isoMsg.setItem(IsoMsg.SYS_TRACE_NO, isoReq.getItem(IsoMsg.SYS_TRACE_NO)); // bit11
//		isoMsg.setItem(IsoMsg.TIME_LOCAL, ISODate.getTime(new Date())); // bit12
//		isoMsg.setItem(IsoMsg.DATE_LOCAL, ISODate.getDate(new Date())); // bit13
//		isoMsg.setItem(IsoMsg.SETTLEMENT_DATE, ISODate.getDate(new Date())); // bit15
//		isoMsg.setItem(IsoMsg.MERCHANT_TYPE, isoReq.getItem(IsoMsg.MERCHANT_TYPE)); // bit18
//		isoMsg.setItemWithLength(IsoMsg.ACQUIRING_INSTITUTION_ID, isoReq.getItem(IsoMsg.ACQUIRING_INSTITUTION_ID), 2); //bit 32
//		isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, isoReq.getItem(IsoMsg.RETRIEVAL_REF_NO)); //bit 37
//		isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, isoReq.getItem(IsoMsg.TERMINAL_ID), 8); // bit41
//		isoMsg.setItemWithPad(IsoMsg.PIN_BLOCK, isoReq.getItem(IsoMsg.PIN_BLOCK), 16);
//		isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, isoReq.getItem(IsoMsg.ADDITIONAL_DATA), 3);
//		
//		String rc = "";
//		if (ResultCode.SUCCESS_CODE.equals(task.getResultCode())) 
//			rc = "00";
//		else if (ResultCode.JETS_UNKNOWN_ERROR.equals(rc)) 
//			rc = "99";
//		else
//			rc = task.getResultCode().substring(2, 4);
//		
//		isoMsg.setIsoItem(IsoMsg.RESPONSE_CODE, rc); //Bit 39
//		return isoMsg;
//	}
//
//}
