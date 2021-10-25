package id.co.emobile.samba.web.iso.logic;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.co.emobile.samba.web.bti.SddBtiIsoDelegateAgent;
import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.data.StateConstant;
import id.co.emobile.samba.web.data.TermConstant;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.iso.IsoMsg;
import id.co.emobile.samba.web.iso.SddIsoMsg;
import id.co.emobile.samba.web.iso.SultengIsoMsgFactory;
import id.co.emobile.samba.web.service.JetsException;

public class CreateIdBillingPaymentLogic extends SddHostBtiLogic {
	private static final Logger LOG = LoggerFactory.getLogger(CreateIdBillingPaymentLogic.class);
	
	@Override
	protected Logger getLogger() {
		return LOG;
	}
	
	@Override
	public void solve(SddBtiIsoDelegateAgent agent, TransactionTO task) 
			throws JetsException, IOException {
		LOG.debug("solve: " + task);
		super.solve(agent, task);

	}

	@Override
	public SddIsoMsg buildIso(TransactionTO task) throws JetsException {
		if(StateConstant.FUND_TRANSFER_INQUIRY == task.getState()){
			return SultengIsoMsgFactory.sddBillingInq(this, task);
		}
		else if(StateConstant.FUND_TRANSFER_SETTLEMENT == task.getState()) {
			SddIsoMsg isoMsg = null;
			if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
//				isoMsg = SultengIsoMsgFactory.createList8AccountDjp(this, task);
			} else {
				isoMsg = SultengIsoMsgFactory.sddBillingSett(this, task);
				LOG.debug("sddBillingSett ISO: " + isoMsg);
				
				//save for reversal
				task.setTerm(TermConstant.BIT7, isoMsg.getItem(IsoMsg.TIMESTAMP));
				task.setTerm(TermConstant.BIT11, isoMsg.getItem(IsoMsg.SYS_TRACE_NO));
				task.setTerm(TermConstant.BIT12, isoMsg.getItem(IsoMsg.TIME_LOCAL));
				task.setTerm(TermConstant.BIT32, isoMsg.getItem(IsoMsg.ACQUIRING_INSTITUTION_ID));
				task.setTerm(TermConstant.BIT37, isoMsg.getItem(IsoMsg.RETRIEVAL_REF_NO));
				task.setTerm(TermConstant.HOST_REF_BIT_37, isoMsg.getItem(IsoMsg.RETRIEVAL_REF_NO));
				task.setTerm(TermConstant.BIT48, isoMsg.getItem(IsoMsg.DATA_PRIVATE));
			}
			return isoMsg;
		} else {
			LOG.error("Unknown state : " + task.getTermAsInteger(TermConstant.ISO_TYPE));
			throw new JetsException("Unknown state!", ResultCode.BTI_UNKNOWN_STATE);
		}
	}

	@Override
	protected void processSuccessResponse(TransactionTO task, SddIsoMsg rspIsoMsg)
			throws JetsException {
		try {
			String bit48 = rspIsoMsg.getItem(IsoMsg.DATA_PRIVATE);
			LOG.error(String.format("processSuccessResponse : %s", bit48));
			if(!StringUtils.isEmpty(bit48)){
//				task.setTerm(TermConstant.DSAC_NAME, bit48.substring(0,30).trim());
				task.setTerm(TermConstant.BIT48, bit48);
				// 17234240407500041112530006062020000771051107511172342404075000124105921919039BENNY DARMAWAN LAY            JL POS UTR II NO 1 C RT  004 RW 001, PS BARU - KOT20200716094620
				int indexStart = 62;
				int lenghtIdBilling = 15;
				int lenghtNamaWp = 30;
				int lenghtAlamatWp = 50;
				int lenghtMasaKadaluarsaBilling = 14;
				String idBilling = bit48.substring(indexStart, indexStart+lenghtIdBilling);
				String namaWp = bit48.substring(indexStart+lenghtIdBilling, indexStart+lenghtIdBilling+lenghtNamaWp);
				String alamatWp = bit48.substring(indexStart+lenghtIdBilling+lenghtNamaWp, indexStart+lenghtIdBilling+lenghtNamaWp+lenghtAlamatWp);
				String masaKadaluarsaBilling = bit48.substring(indexStart+lenghtIdBilling+lenghtNamaWp+lenghtAlamatWp, indexStart+lenghtIdBilling+lenghtNamaWp+lenghtAlamatWp+lenghtMasaKadaluarsaBilling);
				
				task.setTerm(TermConstant.ID_BILLING, idBilling);
				task.setTerm(TermConstant.NAMA_WP, namaWp);
				task.setTerm(TermConstant.ALAMAT_WP, alamatWp);
				task.setTerm(TermConstant.MASA_KADALUARSA_BILLING, masaKadaluarsaBilling);
				
			}			

		} catch (Exception e) {
			LOG.error("Unable to parse bit 48: " + e , e);
			throw new JetsException(ResultCode.BTI_ERROR_PARSE_ISO);
		}
	}


}
