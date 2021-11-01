package id.co.emobile.samba.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import id.co.emobile.samba.web.entity.HistoryTrading;
import id.co.emobile.samba.web.entity.MasterTradingAccount;

@Service
public class CalculateCommissionService {
	private static final Logger LOG = LoggerFactory.getLogger(CalculateCommissionService.class);

	protected Logger getLogger() {
		return LOG;
	}

	public String calculateTotalCommission(HistoryTrading historyTrading, MasterTradingAccount tradingAccount) {
//		getLogger().info("calculateTotalCommission " + historyTrading.getSizeLot() + " lot" + " with commission "
//				+ tradingAccount.getCommissionInDollar() + " per 1 lot");
		String totalCommission = "0";		
		try {
			double lot = Double.parseDouble(historyTrading.getSizeLot());
			double commissionDollar= new Double(tradingAccount.getCommissionInDollar());
			double commissionFinal = commissionDollar/100;
			double lotFinal = lot/0.01;
			double total = lotFinal * commissionFinal;
			totalCommission = Double.toString(total);
		} catch (Exception e) {
			getLogger().error("Error exception calculateTotalCommission " + e.toString());
		}
		getLogger().info("totalCommission " + totalCommission);
		return totalCommission;
	}
	
	public String calculateCompanyCommission(String totalCommission, MasterTradingAccount tradingAccount) {
		String companyCommission = "0";		
		try {
			double allCommission = Double.parseDouble(totalCommission);
			double pctCompany = Double.parseDouble(tradingAccount.getPctSharingProfit());
			double total = allCommission * pctCompany;
			companyCommission = Double.toString(total);
		} catch (Exception e) {
			getLogger().error("Error exception calculateCompanyCommission " + e.toString());
		}
		getLogger().info("companyCommission " + companyCommission);
		return companyCommission;
	}
	
	public String calculateClientCommission(String totalCommission, MasterTradingAccount tradingAccount) {
		String clientCommission = "0";		
		try {
			double allCommission = Double.parseDouble(totalCommission);			
			double pctCompany = Double.parseDouble(tradingAccount.getPctSharingProfit());
			double pctClient = new Double(1) - pctCompany ;			
			double total = allCommission * pctClient;
			clientCommission = Double.toString(total);
		} catch (Exception e) {
			getLogger().error("Error exception calculateClientCommission " + e.toString());
		}
		getLogger().info("clientCommission " + clientCommission);
		return clientCommission;
	}
}
