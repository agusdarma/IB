package id.co.emobile.samba.web.data.param;


public class LookupDataParamVO extends ParamPagingVO{
	private static final long serialVersionUID = 1L; 
	
	@Override
	protected String getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	private int lookupCat;
	private String lookupValue;

	public int getLookupCat() {
		return lookupCat;
	}
	public void setLookupCat(int lookupCat) {
		this.lookupCat = lookupCat;
	}
	public String getLookupValue() {
		return lookupValue;
	}
	public void setLookupValue(String lookupValue) {
		this.lookupValue = lookupValue;
	}


}
