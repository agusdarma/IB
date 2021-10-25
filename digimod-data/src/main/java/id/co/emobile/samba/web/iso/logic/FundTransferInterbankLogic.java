package id.co.emobile.samba.web.iso.logic;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.co.emobile.samba.web.bti.BtiIsoDelegateAgent;
import id.co.emobile.samba.web.data.JetsConstant;
import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.data.StateConstant;
import id.co.emobile.samba.web.data.TermConstant;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.iso.IsoMsg;
import id.co.emobile.samba.web.iso.SultengIsoMsgFactory;
import id.co.emobile.samba.web.service.JetsException;
import id.co.emobile.samba.web.utils.CommonUtil;

public class FundTransferInterbankLogic extends HostBtiLogic {
	private static final Logger LOG = LoggerFactory.getLogger(FundTransferInterbankLogic.class);
	
//	private Map<String, BankInfo> bankInfos;

	@Override
	protected Logger getLogger() {
		return LOG;
	}
	
	/*
	 * support multi currency: local to local, local to foreign, foreign to
	 * foreign, foreign to foreign, foreign1 to foreign2
	 * 
	 * @see com.emobile.common.iso.AbstractIsoLogic#solve(com.emobile.common.data.TransactionTO)
	 */
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

	private void solveInquiry(BtiIsoDelegateAgent agent, TransactionTO task) throws JetsException,
			IOException {
		retrieveSracInfo(agent, task);
		String sracCurr = task.getTerm(TermConstant.SRAC_CURR);
		
		if (!JetsConstant.CURR_IDR.equals(sracCurr)) {
			task.setTerm(TermConstant.RATE1, 
					getForexRate(agent, task, task.getTerm(TermConstant.SRAC_CURR)));
		}
		IsoMsg isoResponse = inquiryAtmBersama(agent, task);
		// only success inquiry can reached here
		task.setResultCode(ResultCode.SUCCESS_CODE);
		
		String bit126 = isoResponse.getItem(IsoMsg.ADDITIONAL_DATA_3);
//		task.setTerm(JetsConstant.DSAC_NAME, bit126.substring(26, 56).trim());
		//update because util code is remove in bit 126 
		task.setTerm(TermConstant.DSAC_NAME, StringUtils.trim(bit126.substring(22, 52)));
		//task.setTerm(JetsConstant.RRNCODE, bit126.substring(168, 180).trim());
		
	}

	private IsoMsg inquiryAtmBersama(BtiIsoDelegateAgent agent, TransactionTO task) throws JetsException,
			IOException {
		IsoMsg isoMsg = SultengIsoMsgFactory.createFundTransferAtmBersamaNew(this, task, "690000", "");
		IsoMsg rspIsoMsg = processToHost(agent, isoMsg);
		if (isSuccess(rspIsoMsg)) {
			return rspIsoMsg;
		} else {
			throw new JetsException("inquiry atm bersama failed",
					getResponseCode(rspIsoMsg));
		}
	}

	/*
	 * this function MUST be called after isInterbankFundTransfer to prevent
	 * null exception
	 */
	private void retrieveSracInfo(BtiIsoDelegateAgent agent, TransactionTO task) throws JetsException,
			IOException {
		IsoMsg rspIsoMsg = checkAccountInfo(agent, task, task.getTerm(TermConstant.SRAC));
		task.setTerm(TermConstant.SRAC_CURR, CommonUtil.rightSubstring(rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA), 3));
		task.setTerm(TermConstant.SRAC_NAME, StringUtils.trim(rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA_2).substring(0, 40)));
	}

	/*
	 * return IsoMsg response from host
	 */
	private IsoMsg checkAccountInfo(BtiIsoDelegateAgent agent, TransactionTO task, String accountNo)
			throws JetsException, IOException {
		IsoMsg isoMsg = SultengIsoMsgFactory.createAccountInfo(this, task, accountNo);
		IsoMsg rspIsoMsg = processToHost(agent, isoMsg);
		if (isSuccess(rspIsoMsg)) {
			return rspIsoMsg;
		} else {
			throw new JetsException("checking account info: " + accountNo + " failed", getResponseCode(rspIsoMsg));
		}
	}

	/*
	 * build IsoMsg for Fund Transfer settlement
	 * 
	 * @see com.emobile.common.iso.AbstractIsoLogic#buildIso(com.emobile.common.data.TransactionTO)
	 */
	@Override
	public IsoMsg buildIso(TransactionTO task) throws JetsException {
		if(StateConstant.FUND_TRANSFER_INQUIRY == task.getState()){
			return SultengIsoMsgFactory.createFundTransferOtherInquiry(this, task);
		}
		else if(StateConstant.FUND_TRANSFER_SETTLEMENT == task.getState()) {
			IsoMsg isoMsg = null;
			if (task.getTermAsBoolean(TermConstant.REVERSAL)) {
				isoMsg = SultengIsoMsgFactory.createFundTransferOtherReversal(this, task);
			} else {
				isoMsg = SultengIsoMsgFactory.createFundTransferOtherSettle(this, task);
				LOG.debug("createFundTransferOtherSettle ISO: " + isoMsg);
				
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

	/*
	 * return bit62 of ForexRate
	 */
	public String getForexRate(BtiIsoDelegateAgent agent, TransactionTO task, String curr)
			throws JetsException, IOException {
		IsoMsg isoMsg = SultengIsoMsgFactory.createForexInfo(this, task, curr);
		IsoMsg rspIsoMsg = processToHost(agent, isoMsg);
		if (isSuccess(rspIsoMsg)) {
			return rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA_2);
		} else {
			throw new JetsException("unable to get forex rate for: " + curr,
					getResponseCode(rspIsoMsg));
		}
	}

	@Override
	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg)
			throws JetsException {
		try {
			String bit48 = rspIsoMsg.getItem(IsoMsg.DATA_PRIVATE);
			if(!StringUtils.isEmpty(bit48)){
				task.setTerm(TermConstant.DSAC_NAME, bit48.substring(0,30).trim());
				task.setTerm(TermConstant.BIT48, bit48);
			}			
			String bit61 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA);
			if(!StringUtils.isEmpty(bit61)){
				task.setTerm(TermConstant.BIT61, bit61);
			}
		} catch (Exception e) {
			LOG.error("Unable to parse bit 48: " + e);
			throw new JetsException(ResultCode.BTI_ERROR_PARSE_ISO);
		}
	}
}
