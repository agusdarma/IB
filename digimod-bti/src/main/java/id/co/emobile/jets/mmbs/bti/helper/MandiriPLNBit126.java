package id.co.emobile.jets.mmbs.bti.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MandiriPLNBit126 {
	private static final Logger LOG = LoggerFactory.getLogger(MandiriPLNBit126.class);

	private String tokenHeader;

	private String utilityCode;

	private String customerID;

	private String customerName;

	private String outstandingBills;

	private String billPayable;

	private String paidBill;

	private String customerSegmentation;

	private String powerCategory;

	private String mlpo;

	private String customerUnit;

	private String adminCharge;

	private BillDetailInfo billDetailInfo;

	private class BillDetailInfo {
		private String period;

		private String billAmount;

		private String vat;

		private String penaltyFee;

		private String other;

		public BillDetailInfo(String info) throws Exception {
			try {
				period = info.substring(0, 4);
				billAmount = info.substring(4, 17);
				vat = info.substring(17, 29);
				penaltyFee = info.substring(29, 40);
				other = info.substring(40);
			}
			catch (Exception e) {
				LOG.error("Failed creating bill detail info: " + e);
				throw e;
			}
		}

		public String toBit126Str() {
			return period + billAmount + vat + penaltyFee + other;
		}

		public String getBillAmount() {
			return billAmount;
		}

//		public void setBillAmount(String billAmount) {
//			this.billAmount = billAmount;
//		}

		public String getPenaltyFee() {
			return penaltyFee;
		}

//		public void setPenaltyFee(String penaltyFee) {
//			this.penaltyFee = penaltyFee;
//		}

		public String getPeriod() {
			return period;
		}

//		public void setPeriod(String period) {
//			this.period = period;
//		}

		public String getVat() {
			return vat;
		}

//		public void setVat(String vat) {
//			this.vat = vat;
//		}

		public String getOther() {
			return other;
		}

//		public void setOther(String other) {
//			this.other = other;
//		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("period:");
			sb.append(period);
			sb.append(",billAmount:");
			sb.append(billAmount);
			sb.append(",vat:");
			sb.append(vat);
			sb.append(",penaltyFee:");
			sb.append(penaltyFee);
			sb.append(",other:");
			sb.append(other);
			return sb.toString();
		}
	}

	public void parse(String bit126) throws Exception {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Parse bit126: " + bit126);
			}
			tokenHeader = bit126.substring(0, 22);
			utilityCode = bit126.substring(22, 26);
			customerID = bit126.substring(26, 38);
			customerName = bit126.substring(38, 58);
			outstandingBills = bit126.substring(58, 60);
			billPayable = bit126.substring(60, 61);
			paidBill = bit126.substring(61, 62);
			customerSegmentation = bit126.substring(62, 66);
			powerCategory = bit126.substring(66, 75);
			mlpo = bit126.substring(75, 94);
			customerUnit = bit126.substring(94, 99);
			adminCharge = bit126.substring(99, 108);
			billDetailInfo = new BillDetailInfo(bit126.substring(108));
		}
		catch (Exception e) {
			LOG.error("Exception in parsing bit 126: " + e);
			throw e;
		}
	}

	public String getAdminCharge() {
		return adminCharge;
	}

	public void setAdminCharge(String adminCharge) {
		this.adminCharge = adminCharge;
	}

	public int getBillPayable() {
		return Integer.parseInt(billPayable);
	}

	public void setBillPayable(int billPayable) {
		this.billPayable = String.valueOf(billPayable);
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getCustomerSegmentation() {
		return customerSegmentation;
	}

	public void setCustomerSegmentation(String customerSegmentation) {
		this.customerSegmentation = customerSegmentation;
	}

	public String getMlpo() {
		return mlpo;
	}

	public void setMlpo(String mlpo) {
		this.mlpo = mlpo;
	}

	public int getOutstandingBills() {
		return Integer.parseInt(outstandingBills);
	}

	public void setOutstandingBills(int outstandingBills) {
		if (outstandingBills > 9) {
			this.outstandingBills = String.valueOf(outstandingBills);
		}
		else {
			this.outstandingBills = '0' + String.valueOf(outstandingBills);
		}
	}

	public int getPaidBill() {
		return Integer.parseInt(paidBill);
	}

	public void setPaidBill(int paidBill) {
		this.paidBill = String.valueOf(paidBill);
	}

	public String getPowerCategory() {
		return powerCategory;
	}

	public void setPowerCategory(String powerCategory) {
		this.powerCategory = powerCategory;
	}

	public String getTokenHeader() {
		return tokenHeader;
	}

	public void setTokenHeader(String tokenHeader) {
		this.tokenHeader = tokenHeader;
	}

	public String getUtilityCode() {
		return utilityCode;
	}

	public void setUtilityCode(String utilityCode) {
		this.utilityCode = utilityCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerUnit() {
		return customerUnit;
	}

	public void setCustomerUnit(String customerUnit) {
		this.customerUnit = customerUnit;
	}

	public BillDetailInfo getBillDetailInfo() {
		return billDetailInfo;
	}

	public void setBillDetailInfo(BillDetailInfo billDetailInfo) {
		this.billDetailInfo = billDetailInfo;
	}

	public double getBillAmount() {
		double amt = 0.0;
		if (getBillPayable() > 0) {
			amt = Double.parseDouble(billDetailInfo.getBillAmount());
			double fee = Double.parseDouble(billDetailInfo.getPenaltyFee());
			amt = (amt + fee) / 100;
		}
		return amt;
	}

	public String getPeriod() {
		return billDetailInfo.getPeriod();
	}

	public String toBit126Str() {
		StringBuilder sb = new StringBuilder(tokenHeader);
		sb.append(utilityCode).append(customerID).append(customerName).append(outstandingBills).append(
				billPayable).append(paidBill).append(customerSegmentation).append(powerCategory).append(mlpo)
				.append(customerUnit).append(adminCharge).append(billDetailInfo.toBit126Str());
		return sb.toString();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("tokenHeader;");
		sb.append(tokenHeader);
		sb.append(",utilityCode:");
		sb.append(utilityCode);
		sb.append(",customerID:");
		sb.append(customerID);
		sb.append(",outstandingBills:");
		sb.append(outstandingBills);
		sb.append(",billPayable");
		sb.append(billPayable);
		sb.append(",paidBill:");
		sb.append(paidBill);
		sb.append(",customerSegmentation:");
		sb.append(customerSegmentation);
		sb.append(",powerCategory:");
		sb.append(powerCategory);
		sb.append(",mlpo:");
		sb.append(mlpo);
		sb.append(",customerUnit:");
		sb.append(customerUnit);
		sb.append(",adminCharge:");
		sb.append(adminCharge);
		sb.append(",billdetailInfo: ");
		sb.append(billDetailInfo.toString());
		return sb.toString();
	}
}
