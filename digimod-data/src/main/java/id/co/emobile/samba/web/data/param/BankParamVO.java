package id.co.emobile.samba.web.data.param;

public class BankParamVO extends ParamPagingVO{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected String getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	private String bankCode; //bank_code 3 char
	private String bankName; //bank_name
	private String bankInitial;

	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public String getBankInitial() {
		return bankInitial;
	}
	public void setBankInitial(String bankInitial) {
		this.bankInitial = bankInitial;
	}
	public String getBankCodeLike(){
		if(bankCode!=null)
		{
			return "%"+bankCode.toUpperCase()+"%";
		}
		return bankCode;
	}
	
	public String getBankInitialLike(){
		if(bankInitial!=null)
		{
			return "%"+bankInitial.toUpperCase()+"%";
		}
		return bankInitial;
	}
	
	public String getBankNameLike(){
		if(bankName!=null)
		{
			return "%"+bankName.toUpperCase()+"%";
		}
		return bankName;
	}
}
