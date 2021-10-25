package id.co.emobile.samba.web.data.param;


public class TrxCodeInfoParamVO extends ParamPagingVO{
	@Override
	protected String getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	private String trxCode;
	private String trxGroup;
	private String groupWebChart;
	private String bankCode;

	public String getTrxCode() {
		return trxCode;
	}
	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}
	public String getTrxGroup() {
		return trxGroup;
	}
	public void setTrxGroup(String trxGroup) {
		this.trxGroup = trxGroup;
	}
	public String getGroupWebChart() {
		return groupWebChart;
	}
	public void setGroupWebChart(String groupWebChart) {
		this.groupWebChart = groupWebChart;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

}
