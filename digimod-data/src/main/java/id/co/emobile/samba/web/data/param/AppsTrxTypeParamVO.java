package id.co.emobile.samba.web.data.param;

public class AppsTrxTypeParamVO extends ParamPagingVO{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected String getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	private String trxName;
	private int trxGroup;
	
	public String getTrxName() {
		return trxName;
	}
	public void setTrxName(String trxName) {
		this.trxName = trxName;
	} 
	public String getTrxNameLike(){
		if(trxName!=null)
		{
			return "%"+trxName.toUpperCase()+"%";
		}
		return trxName;
	}
	public int getTrxGroup() {
		return trxGroup;
	}
	public void setTrxGroup(int trxGroup) {
		this.trxGroup = trxGroup;
	}
}