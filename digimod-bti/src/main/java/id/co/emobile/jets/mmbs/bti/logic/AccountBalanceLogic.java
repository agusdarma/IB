package id.co.emobile.jets.mmbs.bti.logic;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.co.emobile.jets.mmbs.bti.BtiIsoDelegateAgent;
import id.co.emobile.jets.mmbs.bti.CommonUtils;
import id.co.emobile.jets.mmbs.bti.iso.HostIsoMsgFactory;
import id.co.emobile.jets.mmbs.bti.iso.IsoMsg;
import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.data.TermConstant;
import id.co.emobile.samba.web.data.TransactionTO;
import id.co.emobile.samba.web.service.JetsException;

public class AccountBalanceLogic extends HostBtiLogic {
	private static final Logger LOG = LoggerFactory.getLogger(AccountBalanceLogic.class);
	
	@Override
	protected Logger getLogger() {
		return LOG;
	}

	@Override
	public void solve(BtiIsoDelegateAgent agent, TransactionTO task) 
			throws JetsException, IOException {
		super.solve(agent, task);
	}

	@Override
	protected IsoMsg buildIso(TransactionTO task) throws JetsException {
		return HostIsoMsgFactory.createAccountBalanceSulteng(this, task);
	}

	@Override
	protected void processSuccessResponse(TransactionTO task, IsoMsg rspIsoMsg) throws JetsException {
//		final int LENGTH_ACCOUNT_TYPE 		= 2;
//		final int LENGTH_BALANCE_CODE 		= 2;
//		final int LENGTH_CURR_CODE 			= 3;
//		final int LENGTH_TYPE 				= 1;
//		final int LENGTH_BALANCE 			= 12;
		
		String bit49 = rspIsoMsg.getItem(IsoMsg.CURR_CODE);
		task.setTerm(TermConstant.CURR, bit49);
		
		try {
			String bit54 = rspIsoMsg.getItem(IsoMsg.ADDITIONAL_AMOUNT);
			task.setTerm(TermConstant.BALANCE, CommonUtils.getBalance(bit54.substring(7)));
		}
		catch (Exception e) {
			LOG.error("Unable to parse bit 54: " + e);
			throw new JetsException(ResultCode.BTI_ERROR_PARSE_ISO);
		}
	}

}
