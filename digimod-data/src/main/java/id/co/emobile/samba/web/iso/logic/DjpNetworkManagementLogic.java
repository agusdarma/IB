package id.co.emobile.samba.web.iso.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.co.emobile.samba.web.bti.DjpBtiIsoDelegateAgent;
import id.co.emobile.samba.web.data.TermConstant;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.iso.DjpIsoMsg;
import id.co.emobile.samba.web.iso.IsoMsg;
import id.co.emobile.samba.web.iso.SultengIsoMsgFactory;
import id.co.emobile.samba.web.service.JetsException;

public class DjpNetworkManagementLogic extends DjpHostBtiLogic {
	private static final Logger LOG = LoggerFactory.getLogger(DjpNetworkManagementLogic.class);
	
	@Override
	protected Logger getLogger() {
		return LOG;
	}

	@Override
	protected DjpIsoMsg buildIso(TransactionTO task) throws JetsException {
		// in this logic, task only have data in ISO_SUB_TYPE
		return SultengIsoMsgFactory.createHostNetworkManagementDjp(this, task.getTerm(TermConstant.ISO_SUB_TYPE));
	}

	@Override
	protected void processSuccessResponse(TransactionTO task, DjpIsoMsg rspIsoMsg) throws JetsException {
		// nothing todo
	}

	@Override
	protected void processHostRef(TransactionTO task, DjpIsoMsg rspIsoMsg) {
		// nothing todo here
	}
	
	public void processNetworkRequest(DjpBtiIsoDelegateAgent agent, IsoMsg reqIso) {
		try {
			String sysTraceNo = reqIso.getItem(IsoMsg.SYS_TRACE_NO);
			String networkInfo = reqIso.getItem(IsoMsg.NETWORK_INFO_CODE);
			IsoMsg resp = SultengIsoMsgFactory.responseHostNetworkManagement(sysTraceNo, networkInfo);
			agent.sendData(resp.toString());
		} catch (Exception e) {
			LOG.warn("processNetworkRequest error", e);
		}
	}
}
