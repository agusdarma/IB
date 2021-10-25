package id.co.emobile.samba.web.data.param;

import java.util.Date;

public class AdvertisingParamVO extends ParamPagingVO{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected String getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Date periodStart;
	private Date periodEnd;
	private String adsLocation;
	private String adsName;

	public Date getPeriodStart() {
		return periodStart;
	}
	public void setPeriodStart(Date periodStart) {
		this.periodStart = periodStart;
	}
	public Date getPeriodEnd() {
		return periodEnd;
	}
	public void setPeriodEnd(Date periodEnd) {
		this.periodEnd = periodEnd;
	}
	public String getAdsLocation() {
		return adsLocation;
	}
	public void setAdsLocation(String adsLocation) {
		this.adsLocation = adsLocation;
	}
	public String getAdsName() {
		return adsName;
	}
	public void setAdsName(String adsName) {
		this.adsName = adsName;
	}
	
	public String getAdsNameLike(){
		if(adsName!=null)
		{
			return "%"+adsName.toUpperCase()+"%";
		}
		return adsName;
	}
}
