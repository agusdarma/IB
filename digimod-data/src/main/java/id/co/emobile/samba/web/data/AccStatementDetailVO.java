package id.co.emobile.samba.web.data;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class AccStatementDetailVO implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	private String txCode; // TXCODE : String Transaction Code
	private Date txDate; // TXDATE : Date Transaction Date(yyyy-MM-dd )
	private double txAmount; // TXAMT : Float Transaction Amount
	private Date txDateSettle;  // TXDTSTLMN : Date Transaction Settlement Date (yyyy-MM-dd HH:mm:Sss )
	private String accNumber;  // ACCNBR : String Account Number
	private String txId;  // TXID : String Transaction ID
	private int debetCredit; //DBCR : Integer Transaction Type (0 – Debit, 1- Credit)
	private String txMsg;  // TXMSG : String Transaction Message
	private String txBranch;  // TXBRANCH : String Transaction Branch Location
	private String txCurrency;  // TXCCY : String Transaction Currency
	private String userId;  // USERID : String
	public String getTxCode() {
		return txCode;
	}
	public void setTxCode(String txCode) {
		this.txCode = txCode;
	}
	public Date getTxDate() {
		return txDate;
	}
	public void setTxDate(Date txDate) {
		this.txDate = txDate;
	}
	public double getTxAmount() {
		return txAmount;
	}
	public void setTxAmount(double txAmount) {
		this.txAmount = txAmount;
	}
	public Date getTxDateSettle() {
		return txDateSettle;
	}
	public void setTxDateSettle(Date txDateSettle) {
		this.txDateSettle = txDateSettle;
	}
	public String getAccNumber() {
		return accNumber;
	}
	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}
	public String getTxId() {
		return txId;
	}
	public void setTxId(String txId) {
		this.txId = txId;
	}
	public int getDebetCredit() {
		return debetCredit;
	}
	public void setDebetCredit(int debetCredit) {
		this.debetCredit = debetCredit;
	}
	public String getTxMsg() {
		return txMsg;
	}
	public void setTxMsg(String txMsg) {
		this.txMsg = txMsg;
	}
	public String getTxBranch() {
		return txBranch;
	}
	public void setTxBranch(String txBranch) {
		this.txBranch = txBranch;
	}
	public String getTxCurrency() {
		return txCurrency;
	}
	public void setTxCurrency(String txCurrency) {
		this.txCurrency = txCurrency;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public double getDebetAmount() {
		if (debetCredit == 0) return txAmount;
		else return 0;
	}
	public double getCreditAmount() {
		if (debetCredit == 1) return txAmount;
		else return 0;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
