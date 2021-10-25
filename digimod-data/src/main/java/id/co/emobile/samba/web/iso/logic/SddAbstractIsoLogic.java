package id.co.emobile.samba.web.iso.logic;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import id.co.emobile.samba.web.bti.SddBtiIsoDelegateAgent;
import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.iso.IsoMsg;
import id.co.emobile.samba.web.iso.SddIsoMsg;
import id.co.emobile.samba.web.service.JetsException;

public abstract class SddAbstractIsoLogic {
	protected static final String HOST_SUCCESS_CODE = "00";
	
	private boolean reversalEnabled = false;
//	private boolean reversal = false;
	private List<String> listReversalRc;
	
	private List<String> listBillerNo;
	
	protected abstract Logger getLogger();
	
	public abstract SddIsoMsg analyzeResponse(String agentId, String response, String seqNo)
			throws JetsException;

//	public abstract String getSysTraceNo() throws JetsException;

	protected abstract void processHostRef(TransactionTO task, SddIsoMsg rspIsoMsg);

	protected abstract void processSuccessResponse(TransactionTO task,
			SddIsoMsg rspIsoMsg) throws JetsException;

	protected abstract SddIsoMsg buildIso(TransactionTO task) throws JetsException;
	
	public void solve(SddBtiIsoDelegateAgent agent, TransactionTO task)
			throws JetsException, IOException {
		SddIsoMsg isoMsg = buildIso(task);
		SddIsoMsg rspIsoMsg = processToHost(agent, isoMsg);
		processResponse(task, rspIsoMsg);
	}

	/*
	 * Other logic may override this function if the information is confidential
	 */
	protected SddIsoMsg processToHost(SddBtiIsoDelegateAgent agent, SddIsoMsg isoMsg)
			throws IOException, JetsException {
		if (agent == null) {
			throw new JetsException("No Agent is defined for this Logic",
					ResultCode.JETS_SYSTEM_ERROR);
		}
		long start = System.currentTimeMillis();
		agent.getTrxLogger().info(
				String.format("%s[SND]: %s", agent.getId(), isoMsg.printIso()));
		SddIsoMsg rspIsoMsg = agent.processToHost(this, isoMsg.toString(), 
				isoMsg.getItem(IsoMsg.SYS_TRACE_NO));
		long delta = System.currentTimeMillis() - start;
		if (rspIsoMsg == null) {
			agent.getTrxLogger().info(String.format("%s[RCV]: NULL - %d[ms]", 
					agent.getId(), delta));
		} else {
			agent.getTrxLogger().info(
					String.format("%s[RCV]: %s - %d[ms]", agent.getId(),
							rspIsoMsg.getRawMessage(), delta));
		}
		return rspIsoMsg;
	}

	protected void processResponse(TransactionTO task, SddIsoMsg rspIsoMsg)
			throws JetsException {
		if (rspIsoMsg == null) {
			getLogger().warn("[#{}] No IsoMessage Response from HOST",
					task.getMsgLogNo());
			throw new JetsException("No Response from HOST", 
					ResultCode.BTI_NO_RESPONSE_FROM_HOST);
		}
		String responseCode = rspIsoMsg.getItem(IsoMsg.RESPONSE_CODE);
		if (HOST_SUCCESS_CODE.equals(responseCode)) {
			responseCode = ResultCode.SUCCESS_CODE;
		} else if (StringUtils.isBlank(responseCode)){
			getLogger().warn("[#{}] No response code in ISO <{}>",
				task.getMsgLogNo(), rspIsoMsg.printIso());
			responseCode = ResultCode.BTI_MISSING_RESPONSE_CODE;
		}
		setResponseCode(task, responseCode);
		
		processHostRef(task, rspIsoMsg);
		if (isSuccess(rspIsoMsg)) {
			processSuccessResponse(task, rspIsoMsg);
		} else {
			processFailedResponse(task, rspIsoMsg);
		}
	}

	protected void setResponseCode(TransactionTO task, String responseCode) {
		task.setResultCode(responseCode);
	}
	
	protected boolean isSuccess(SddIsoMsg rspIsoMsg) {
		String responseCode = rspIsoMsg.getItem(IsoMsg.RESPONSE_CODE);
		//for pln reversal if rc = 94 it is success reversal
		return (HOST_SUCCESS_CODE.equals(responseCode)) ? true : false;
	}

	protected void processFailedResponse(TransactionTO task, SddIsoMsg rspIsoMsg)
			throws JetsException {
		//nothing todo
	}

	public boolean isReversalEnabled() {
		return reversalEnabled;
	}

	public void setReversalEnabled(boolean reversalEnabled) {
		this.reversalEnabled = reversalEnabled;
	}

	public List<String> getListReversalRc() {
		return listReversalRc;
	}

	public void setListReversalRc(List<String> listReversalRc) {
		this.listReversalRc = listReversalRc;
		getLogger().debug("ReversalRC: " + Arrays.toString(listReversalRc.toArray(new String[0])));
	}

	public List<String> getListBillerNo() {
		return listBillerNo;
	}

	public void setListBillerNo(List<String> listBillerNo) {
		this.listBillerNo = listBillerNo;
		getLogger().debug("ReversalBillerNo: " + Arrays.toString(listBillerNo.toArray(new String[0])));
	}

}
