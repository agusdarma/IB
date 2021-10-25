package id.co.emobile.samba.web.iso.logic;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.co.emobile.samba.web.bti.BtiIsoDelegateAgent;
import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.data.StateConstant;
import id.co.emobile.samba.web.data.TermConstant;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.iso.IsoMsg;
import id.co.emobile.samba.web.iso.SultengIsoMsgFactory;
import id.co.emobile.samba.web.service.JetsException;

public class FundTransferInHouseLogic extends HostBtiLogic {
	private static final Logger LOG = LoggerFactory.getLogger(FundTransferInHouseLogic.class);

	@Override
	protected Logger getLogger() {
		return LOG;
	}
	
	@Override
	public void solve(BtiIsoDelegateAgent agent, TransactionTO task) 
			throws JetsException, IOException {
		int interbankType = task.getTermAsInteger(TermConstant.INTERBANK_TYPE);
		// Type: 
		// -1=transfer inhouse, 1=transfer interbank
		LOG.debug("Interbank Type: " + interbankType);
		
		if (task.getState() == StateConstant.FUND_TRANSFER_INQUIRY) {
			String srac = task.getTerm(TermConstant.SRAC);
			if (srac.equals(task.getTerm(TermConstant.DSAC))) {
				throw new JetsException("Destination account the same as source",
						ResultCode.BTI_INVALID_DSAC);
			}
		}
		super.solve(agent, task);
	}
	
	@Override
	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
		if(StateConstant.FUND_TRANSFER_INQUIRY == task.getState()){
			return SultengIsoMsgFactory.createFundTransferInquiry(this, task);
		}
		else if(StateConstant.FUND_TRANSFER_SETTLEMENT == task.getState()){
			IsoMsg isoMsg = null;
			if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
				isoMsg = SultengIsoMsgFactory.createFundTransferReversal(this, task);
			} else {
				isoMsg = SultengIsoMsgFactory.createFundTransferSettle(this, task);
				
				//save for reversal
				task.setTerm(TermConstant.BIT7, isoMsg.getItem(IsoMsg.TIMESTAMP));
				task.setTerm(TermConstant.BIT11, isoMsg.getItem(IsoMsg.SYS_TRACE_NO));
				task.setTerm(TermConstant.BIT12, isoMsg.getItem(IsoMsg.TIME_LOCAL));
				task.setTerm(TermConstant.BIT32, isoMsg.getItem(IsoMsg.ACQUIRING_INSTITUTION_ID));
				task.setTerm(TermConstant.BIT37, isoMsg.getItem(IsoMsg.RETRIEVAL_REF_NO));
			}
			return isoMsg;
		} else {
			LOG.error("Unknown state : " + task.getTermAsInteger(TermConstant.ISO_TYPE));
			throw new JetsException("Unknown state!", ResultCode.BTI_UNKNOWN_STATE);
		}
	}

	
	
	@Override
	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg)
			throws JetsException {
		try {
			if (task.getState() == StateConstant.FUND_TRANSFER_INQUIRY) {
				String bit61 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA);
				task.setTerm(TermConstant.BIT61, bit61);
				String dsacName = bit61.substring(71, bit61.length());
				if(!StringUtils.isEmpty(dsacName))
					task.setTerm(TermConstant.DSAC_NAME, dsacName.trim());
			}
			else if (task.getState() == StateConstant.FUND_TRANSFER_SETTLEMENT) {
				try {
					String hostRef = task.getTerm(TermConstant.HREF);
					if(!StringUtils.isEmpty(hostRef)){
						String refNo = hostRef.substring(9, 13) + hostRef.substring(15, 20);
						task.setTerm(TermConstant.HOST_REF_NO, refNo);
					}
				} catch (Exception e) {
					LOG.error("Unable to parse bit 93: " + e);
					throw new JetsException(ResultCode.BTI_ERROR_PARSE_ISO);
				}
			}
		} catch(Exception e) {
			throw new JetsException("Error process success response: " + e, ResultCode.BTI_ERROR_PARSE_ISO);
		}
		
	}
	
}
