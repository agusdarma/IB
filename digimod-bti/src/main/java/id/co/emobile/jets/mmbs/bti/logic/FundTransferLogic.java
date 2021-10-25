package id.co.emobile.jets.mmbs.bti.logic;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import id.co.emobile.jets.mmbs.bti.BtiIsoDelegateAgent;
import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
import id.co.emobile.samba.web.data.JetsConstant;
import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.data.StateConstant;
import id.co.emobile.samba.web.data.TermConstant;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.service.JetsException;

public class FundTransferLogic extends HostBtiLogic {
	private static final Logger LOG = LoggerFactory.getLogger(FundTransferLogic.class);

//	private Set<String> listPrimaNetwork;

	@Autowired
	private FundTransferInterbankLogic fundTransferInterbankLogic;
	
	@Autowired
	private FundTransferInHouseLogic fundTransferInHouseLogic;
	
//	private boolean isInterbankLink(TransactionTO task) {
//		if (listPrimaNetwork.contains(task.getTerm(TermConstant.BANK_CODE)))
//			return true;
//		else
//			return false;
//	}

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
		if (interbankType == JetsConstant.FUND_TRANSFER_INHOUSE) { // sesama
			fundTransferInHouseLogic.solve(agent, task);
		} else if (interbankType == JetsConstant.FUND_TRANSFER_INTERBANK) { //other
			fundTransferInterbankLogic.solve(agent, task);
		} else {
			throw new JetsException("Unsupported interbank fund transfer type: "
					+ task.getTerm(TermConstant.INTERBANK_TYPE), ResultCode.BTI_UNSUPPORTED_TRX);
		}

	}

	@Override
	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
		// no implementation as solve is overidden.
		return null;
	}

//	public void setListPrimaNetwork(Set<String> listPrimaNetwork) {
//		this.listPrimaNetwork = listPrimaNetwork;
//	}

	@Override
	protected Logger getLogger() {
		return LOG;
	}

	@Override
	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg)
			throws JetsException {
	}
	
}
