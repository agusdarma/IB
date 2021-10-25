package id.co.emobile.jets.mmbs.bti.logic;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import id.co.emobile.jets.mmbs.bti.BtiIsoDelegateAgent;
import id.co.emobile.jets.mmbs.bti.helper.CommonUtil;
import id.co.emobile.jets.mmbs.bti.iso.HostIsoMsgFactory;
import id.co.emobile.jets.mmbs.bti.iso.ISODate;
import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
import id.co.emobile.jets.mmbs.bti.iso.IsoMsgHeader;
import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.data.TermConstant;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.service.AppsTimeService;
import id.co.emobile.samba.web.service.JetsException;
import id.co.emobile.samba.web.service.SambaWebException;
import id.co.emobile.samba.web.service.SequenceGeneratorService;

public abstract class HostBtiLogic extends AbstractIsoLogic {

	private String terminalId;
	private String acquirerId;
	
//	@Autowired
//	protected AppsMessageService messageService;
	
	@Autowired
	private AppsTimeService timeService;
	
//	@Autowired
//	private BtiSequenceManager sequenceManager;
	
	@Autowired
	@Qualifier("btiSequenceManager")
	private SequenceGeneratorService sequenceManager;
	
	@Autowired
	@Qualifier("btiSequenceRefNumber")
	private SequenceGeneratorService sequenceRefNumber;
	
	/*
	 * The function will be called by IsoTcpDelegateWorker
	 * @see com.emobile.common.iso.IsoLogic#analyzeResponse(java.lang.String,
	 * java.lang.String)
	 */
	public IsoMsg analyzeResponse(String agentId, String response, String seqNo) throws JetsException {
		getLogger().debug(agentId + ": Analyze response, expected seqNo: " + seqNo);
		
		if (response == null)
			return null;
		IsoMsg isoMsg = HostIsoMsgFactory.createHostIsoMsg().parse(response);
		if (isoMsg.getMsgType().equals(IsoMsgHeader.UNKNOWN_MSG_TYPE)) {
			// get error reply from DSP, response code in bit2
			String responseCode = isoMsg.getItem(IsoMsg.PAN);
			if (responseCode != null) {
				getLogger().error(agentId + ": Receive error from DSP: " + responseCode);
				throw new JetsException(responseCode.substring(0, 3));
			}
		}
		else if (!seqNo.equals(isoMsg.getItem(IsoMsg.SYS_TRACE_NO))) {
			getLogger().error(agentId + ": Unexpected sys trace no: " + 
					isoMsg.getItem(IsoMsg.SYS_TRACE_NO) + ", expected: "
					+ seqNo);
			return null;
		}
		return isoMsg;
	}

	/**
	 * @param task
	 * @param rspIsoMsg
	 */
	protected void processHostRef(TransactionTO task, IsoMsg rspIsoMsg) {
		try {
			//yyMMdd99151020123456
			Date currentTime = timeService.getCurrentTime();
			String terminalId = rspIsoMsg.getItem(IsoMsg.TERMINAL_ID).trim();
			task.setTerm(TermConstant.HREF, ISODate.getANSIDate(currentTime)
					+ CommonUtil.padRight(terminalId, '0', 8) 
					+ rspIsoMsg.getItem(IsoMsg.SYS_TRACE_NO));
		}
		catch (Exception e) {
			getLogger().warn("[#" + task.getMsgLogNo() + "] Failed compose HREF: " + task, e);
		}
	}

	public String getNextSequence() throws SambaWebException {
		return sequenceManager.getNextSequence();
	}
	
	public String getNextSequenceRefNumber() throws SambaWebException {
		return sequenceRefNumber.getNextSequence();
	}
	
	public boolean isUseHostHeader() {
		return true;
	}
	
	protected String getResponseCode(IsoMsg rspIsoMsg) {
		if (rspIsoMsg == null) 
			return ResultCode.BTI_MISSING_RESPONSE_CODE;
		String responseCode = rspIsoMsg.getItem(IsoMsg.RESPONSE_CODE);
		if (HOST_SUCCESS_CODE.equals(responseCode)) {
			return ResultCode.SUCCESS_CODE;
		}
		else {
			return responseCode;
		}
	}
	
	// Logic Section
	protected String getDefaultAccount(BtiIsoDelegateAgent agent, TransactionTO task) throws JetsException, IOException {
		IsoMsg isoMsg = HostIsoMsgFactory.createList8Account(this, task);
		IsoMsg rspIsoMsg = processToHost(agent, isoMsg);
		if (isSuccess(rspIsoMsg)) {
			task.setResultCode(ResultCode.SUCCESS_CODE);
			String bit61 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_DATA);
			String srac = "";
			if (bit61.length() > 19){
				srac = bit61.substring(0, 19);
			} else {
				srac = bit61.substring(0, 13);
			}
			task.setTerm(TermConstant.IS_GET_DEF_ACCOUNT, true);
			return srac.trim();
		}
		else {
			throw new JetsException("Get default account failed!",
					getResponseCode(rspIsoMsg));
		}
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getAcquirerId() {
		return acquirerId;
	}

	public void setAcquirerId(String acquirerId) {
		this.acquirerId = acquirerId;
	}
	
}
