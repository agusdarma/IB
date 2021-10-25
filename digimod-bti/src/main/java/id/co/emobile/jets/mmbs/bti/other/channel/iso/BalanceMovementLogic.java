//package id.co.emobile.jets.mmbs.bti.other.channel.iso;
//
//import java.util.Scanner;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
//import id.co.emobile.jets.mmbs.bti.helper.CommonUtil;
//import id.co.emobile.jets.mmbs.bti.iso.HostIsoMsgFactory;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
//import id.co.emobile.jets.mmbs.bti.iso.IsoMsgHeader;
//import id.co.emobile.samba.web.data.ResultCode;
//import id.co.emobile.samba.web.data.TermConstant;
//import id.co.emobile.samba.web.data.TransactionTO;
//import id.co.emobile.samba.web.service.JetsException;
//import id.co.emobile.samba.web.service.SequenceGeneratorService;
//
//public class BalanceMovementLogic implements BaseIsoLogic {
//	
//	private String trxCode;
//
//	@Autowired
//	@Qualifier("btiSequenceRefNumber")
//	private SequenceGeneratorService sequenceRefNumber;
//	
//	public void setTrxCode(String trxCode) {
//		this.trxCode = trxCode;
//	}
//
//	@Override
//	public void composeTermIso(IsoMsg isoReq, TransactionTO task)
//			throws JetsException {
//		task.setTrxCode(trxCode);
//		task.setTerm(TermConstant.CARD_NUMBER, isoReq.getItem(IsoMsg.PAN)); //card no		
//		
//		String bit61 = isoReq.getItem(IsoMsg.ADDITIONAL_DATA);
//		String srac = bit61.substring(0, 13);
//		bit61 = bit61.substring(bit61.indexOf(":") + 2, bit61.length());
//		
//		Scanner scan = new Scanner(bit61);
//		String date = scan.next();
//		String time = scan.next();
//		String amount = scan.next();
//		String type = scan.next();
//		
//		scan.close();
//		
//		String typeDef = "D".equals(type.substring(0, 1)) ? "keluar" : "masuk";
//		
//		task.setTerm(TermConstant.SRAC, CommonUtil.trim(srac));
//		task.setTerm(TermConstant.TRX_DATE_TIME, date + " " + time);
//		task.setTerm(TermConstant.AMOUNT, CommonUtil.parseMoney(amount));
//		task.setTerm(TermConstant.TRX_TYPE, typeDef);	
//	}
//
//	@Override
//	public IsoMsg composeIsoResponse(IsoMsg isoReq, TransactionTO task)
//			throws Exception {
//		IsoMsg isoMsg = (IsoMsg) HostIsoMsgFactory.createHostIsoMsg(IsoMsgHeader.FINANCIAL_TRANSACTION_RESPONSE);
//		isoMsg.setItemWithLength(IsoMsg.PAN, isoReq.getItem(IsoMsg.PAN), 2); // bit2
//		isoMsg.setItem(IsoMsg.PROCESSING_CODE, isoReq.getItem(IsoMsg.PROCESSING_CODE)); // bit3
//		isoMsg.setItem(IsoMsg.TRANSACTION_AMT, isoReq.getItem(IsoMsg.TRANSACTION_AMT)); // bit4
//		isoMsg.setItem(IsoMsg.TIMESTAMP, isoReq.getItem(IsoMsg.TIMESTAMP)); // bit7
//		isoMsg.setItem(IsoMsg.SYS_TRACE_NO, isoReq.getItem(IsoMsg.SYS_TRACE_NO)); // bit11
//		isoMsg.setItem(IsoMsg.TIME_LOCAL, isoReq.getItem(IsoMsg.TIME_LOCAL)); // bit12
//		isoMsg.setItem(IsoMsg.DATE_LOCAL, isoReq.getItem(IsoMsg.DATE_LOCAL)); // bit13
//		isoMsg.setItem(IsoMsg.SETTLEMENT_DATE, isoReq.getItem(IsoMsg.SETTLEMENT_DATE)); // bit15
//		isoMsg.setItem(IsoMsg.MERCHANT_TYPE,  isoReq.getItem(IsoMsg.MERCHANT_TYPE)); // bit18
//		isoMsg.setItemWithLength(IsoMsg.ACQUIRING_INSTITUTION_ID, isoReq.getItem(IsoMsg.ACQUIRING_INSTITUTION_ID), 2); // bit32
//		isoMsg.setItem(IsoMsg.RETRIEVAL_REF_NO, isoReq.getItem(IsoMsg.RETRIEVAL_REF_NO)); // bit37
//		isoMsg.setItem(IsoMsg.RESPONSE_CODE, "00"); // bit39
//		isoMsg.setItemWithPad(IsoMsg.TERMINAL_ID, isoReq.getItem(IsoMsg.TERMINAL_ID), 8); // bit41
//		isoMsg.setItem(IsoMsg.CARD_ACCEPTOR_NAME_LOCATION, isoReq.getItem(IsoMsg.CARD_ACCEPTOR_NAME_LOCATION)); // bit43
//		isoMsg.setItem(IsoMsg.CURR_CODE, isoReq.getItem(IsoMsg.CURR_CODE)); // bit49
//		isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA, isoReq.getItem(IsoMsg.ADDITIONAL_DATA), 3); // bit 61
//		isoMsg.setItemWithLength(IsoMsg.ADDITIONAL_DATA_4, isoReq.getItem(IsoMsg.ADDITIONAL_DATA_4), 3); // bit 63
//		
//		String rc = "99";
//		if (ResultCode.SUCCESS_CODE.equals(task.getResultCode())) 
//			rc = "00";
//		else if (ResultCode.JETS_UNKNOWN_ERROR.equals(rc)) 
//			rc = "99";
//		else if (StringUtils.isNotEmpty(task.getResultCode()))
//			rc = task.getResultCode().substring(2);
//		
//		isoMsg.setIsoItem(IsoMsg.RESPONSE_CODE, rc); //Bit 39
//		return isoMsg;
//	}
//
//}
