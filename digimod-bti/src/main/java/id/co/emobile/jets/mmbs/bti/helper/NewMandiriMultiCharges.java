package id.co.emobile.jets.mmbs.bti.helper;

import java.util.Iterator;
import java.util.SortedSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.co.emobile.samba.web.data.FundFlowVO;
import id.co.emobile.samba.web.data.ResultCode;
import id.co.emobile.samba.web.service.JetsException;

/*
 * charges from single account to multi accounts
 */
public class NewMandiriMultiCharges {

	private static final Logger LOG = LoggerFactory.getLogger(NewMandiriMultiCharges.class);

	private String srac;

	private String dsac;

	private String chargeSrac;

	private double amount;

	private ChargesTo[] chargesTos = new ChargesTo[4];

	public NewMandiriMultiCharges(SortedSet<FundFlowVO> fundFlows) throws JetsException {
		parse(fundFlows);
	}

	private void parse(SortedSet<FundFlowVO> fundFlows) throws JetsException {
		FundFlowVO ff = fundFlows.first();
		if (ff == null) {
			throw new JetsException("No first settlement found",
					ResultCode.JETS_FUND_FLOW_NOT_FOUND);
		}
		this.srac = ff.getAcctFrom();
		this.dsac = ff.getAcctTo();
		this.amount = ff.getAmount();
		fillChargesTos(fundFlows);
	}

	private void fillChargesTos(SortedSet<FundFlowVO> fundFlows) throws JetsException {
		Iterator<FundFlowVO> iter = fundFlows.iterator();
		int i = 0;
		//skip the first one, see parse above
		iter.next();
		while (iter.hasNext()) {
			if (i > 3) {
				LOG.warn("The transaction has more than 4 charges: " + fundFlows);
				break;
			}
			FundFlowVO ff = iter.next();
			if (i == 0) {
				chargeSrac = ff.getAcctFrom();
				//3 is bank code, the first charges to must be an GL account
				if (!"3".equals(ff.getFlowType().substring(1, 2))) {
					//hardcoded GL account
					chargesTos[i] = new ChargesTo("000043011314", 0); //dummy charges
					i++;
					continue;
				}
			}
			if (!chargeSrac.equals(ff.getAcctFrom())) {
				throw new JetsException("Charges source account can not be different",
						ResultCode.JETS_FUND_FLOW_NOT_FOUND);
			}
			chargesTos[i] = new ChargesTo(ff.getAcctTo(), ff.getAmount());
			i++;
		}
	}

	public String getSrac() {
		return srac;
	}

	public String getDsac() {
		return dsac;
	}

	public double getAmount() {
		return amount;
	}

	public String getChargeSrac() {
		return chargeSrac;
	}

	public double getTotalCharges() {
		double total = 0;
		for (int i = 0; i < 4; i++) {
			total = total + getToAmount(i);
		}
		return total;
	}

	public String getToAccount(int index) {
		if (chargesTos[index] == null) {
			return " ";
		}
		return chargesTos[index].getAccount();
	}

	public double getToAmount(int index) {
		if (chargesTos[index] == null) {
			return 0;
		}
		return chargesTos[index].getAmount();
	}

	public double getSettlement(String accountNo) {
		double total = 0;
		if (accountNo.equals(srac)) {
			total = total - amount;
		}
		if (accountNo.equals(dsac)) {
			total = total + amount;
		}
		if (accountNo.equals(chargeSrac)) {
			total = total - getTotalCharges();
		}
		for (int i = 0; i < 4; i++) {
			if (accountNo.equals(getToAccount(i))) {
				total = total + getToAmount(i);
			}
		}
		return total;
	}

	private static class ChargesTo {
		private String account;

		private double amount;

		public ChargesTo(String account, double amount) {
			this.account = account;
			this.amount = amount;
		}

		public String getAccount() {
			return account;
		}

//		public void setAccount(String account) {
//			this.account = account;
//		}

		public double getAmount() {
			return amount;
		}

//		public void setAmount(double amount) {
//			this.amount = amount;
//		}
	}

}
