package id.co.emobile.samba.web.iso.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.co.emobile.samba.web.bti.SddBtiIsoDelegateAgent;
import id.co.emobile.samba.web.data.TermConstant;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.iso.IsoMsg;
import id.co.emobile.samba.web.iso.SddIsoMsg;
import id.co.emobile.samba.web.iso.SultengIsoMsgFactory;
import id.co.emobile.samba.web.service.JetsException;

public class SddNetworkManagementLogic extends SddHostBtiLogic {
	private static final Logger LOG = LoggerFactory.getLogger(SddNetworkManagementLogic.class);
	
	private String bit47;
	private String bit48;
	
	@Override
	protected Logger getLogger() {
		return LOG;
	}

	@Override
	protected SddIsoMsg buildIso(TransactionTO task) throws JetsException {
		// in this logic, task only have data in ISO_SUB_TYPE
		LOG.info("bit47 : " + bit47);
		LOG.info("bit48 : " + bit48);
		return SultengIsoMsgFactory.createHostNetworkManagementSdd(this, task.getTerm(TermConstant.ISO_SUB_TYPE),bit47,bit48);
	}

	@Override
	protected void processSuccessResponse(TransactionTO task, SddIsoMsg rspIsoMsg) throws JetsException {
		// nothing todo
	}

	@Override
	protected void processHostRef(TransactionTO task, SddIsoMsg rspIsoMsg) {
		// nothing todo here
	}
	
	public void processNetworkRequest(SddBtiIsoDelegateAgent agent, IsoMsg reqIso) {
		try {
			String sysTraceNo = reqIso.getItem(IsoMsg.SYS_TRACE_NO);
			String networkInfo = reqIso.getItem(IsoMsg.NETWORK_INFO_CODE);
			IsoMsg resp = SultengIsoMsgFactory.responseHostNetworkManagementSdd(networkInfo,sysTraceNo);
			agent.sendData(resp.toString());
		} catch (Exception e) {
			LOG.warn("processNetworkRequest error", e);
		}
	}

	public String getBit47() {
		return bit47;
	}

	public void setBit47(String bit47) {
		this.bit47 = bit47;
	}

	public String getBit48() {
		return bit48;
	}

	public void setBit48(String bit48) {
		this.bit48 = bit48;
	}
}
