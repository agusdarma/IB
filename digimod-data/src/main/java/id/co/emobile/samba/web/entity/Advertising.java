package id.co.emobile.samba.web.entity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Advertising implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private int id;					//  id              int auto_increment not null,
	private String adsName;			//  adv_desc        varchar(160),
	private String adsLocation;		//  1. promotion program page, 2.on final transaction page, 3.push ads
	private String displayImage;	//  bank_id         int not null,
	private String fullImage;		//  image_name      varchar(50) not null,
	private File displayImageFile;
	private File fullImageFile;
	private String textContent;
	private String latitude;
	private String longitude;
	private String adsUrl;
	private float radius;
	private int bankType;		// 1:all bank 2:specify bank
	private int hitCount;
	private int adsPriority;
	
	private Date periodStart;	//  period_start    datetime,
	private Date periodEnd;		//  period_end      datetime,
	private int createdBy;		//  created_by      int not null,
	private Date createdOn;		//  created_on      datetime not null,
	private int updatedBy;		//	updated_by      int not null,
	private Date updatedOn;		//	updated_on      datetime not null,
	
	private String bankName;
	private String bankTypeDisplay;
	private String adsLocationDisplay;
	private String updatedByDisplay;
	private String createdByDisplay;
	private String periodStartDisplay;
	private String periodEndDisplay;
	private String status;
	private String radiusStr;
	private int showOrder;
	private int rowNum;
	private int pushStatus;
	
	private String statusDisplay;
	private String pushStatusDisplay;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getPeriodStart() {
		return periodStart;
	}
	public void setPeriodStart(Date periodStart) {
		periodStartDisplay = sdf.format(periodStart);
		this.periodStart = periodStart;
	}
	
	public Date getPeriodEnd() {
		return periodEnd;
	}
	
	public void setPeriodEnd(Date periodEnd) {
		periodEndDisplay = sdf.format(periodEnd);
		this.periodEnd = periodEnd;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public String getAdsName() {
		return adsName;
	}
	public void setAdsName(String adsName) {
		this.adsName = adsName;
	}
	public String getAdsLocation() {
		return adsLocation;
	}
	public void setAdsLocation(String adsLocation) {
		this.adsLocation = adsLocation;
	}
	public String getDisplayImage() {
		return displayImage;
	}
	public void setDisplayImage(String displayImage) {
		this.displayImage = displayImage;
	}
	public String getFullImage() {
		return fullImage;
	}
	public void setFullImage(String fullImage) {
		this.fullImage = fullImage;
	}
	public String getTextContent() {
		return textContent;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getAdsUrl() {
		return adsUrl;
	}
	public float getRadius() {
		return radius;
	}
	public void setRadius(float radius) {
		this.radius = radius;
	}
	public void setAdsUrl(String adsUrl) {
		this.adsUrl = adsUrl;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public int getBankType() {
		return bankType;
	}
	public void setBankType(int bankType) {
		this.bankType = bankType;
	}
	public int getHitCount() {
		return hitCount;
	}
	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}
	public String getBankTypeDisplay() {
		return bankTypeDisplay;
	}
	public void setBankTypeDisplay(String bankTypeDisplay) {
		this.bankTypeDisplay = bankTypeDisplay;
	}
	public String getAdsLocationDisplay() {
		return adsLocationDisplay;
	}
	public void setAdsLocationDisplay(String adsLocationDisplay) {
		this.adsLocationDisplay = adsLocationDisplay;
	}
	public String getUpdatedByDisplay() {
		return updatedByDisplay;
	}
	public void setUpdatedByDisplay(String updatedByDisplay) {
		this.updatedByDisplay = updatedByDisplay;
	}
	public String getCreatedByDisplay() {
		return createdByDisplay;
	}
	public void setCreatedByDisplay(String createdByDisplay) {
		this.createdByDisplay = createdByDisplay;
	}
	public File getDisplayImageFile() {
		return displayImageFile;
	}
	public void setDisplayImageFile(File displayImageFile) {
		this.displayImageFile = displayImageFile;
	}
	public File getFullImageFile() {
		return fullImageFile;
	}
	public void setFullImageFile(File fullImageFile) {
		this.fullImageFile = fullImageFile;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPeriodStartDisplay() {
		return periodStartDisplay;
	}
	public void setPeriodStartDisplay(String periodStartDisplay) {
		this.periodStartDisplay = periodStartDisplay;
	}
	public String getPeriodEndDisplay() {
		return periodEndDisplay;
	}
	public void setPeriodEndDisplay(String periodEndDisplay) {
		this.periodEndDisplay = periodEndDisplay;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public int getShowOrder() {
		return showOrder;
	}
	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}
	public String getRadiusStr() {
		return radiusStr;
	}
	public void setRadiusStr(String radiusStr) {
		this.radiusStr = radiusStr;
	}
	public int getAdsPriority() {
		return adsPriority;
	}
	public void setAdsPriority(int adsPriority) {
		this.adsPriority = adsPriority;
	}
	public int getPushStatus() {
		return pushStatus;
	}
	public void setPushStatus(int pushStatus) {
		this.pushStatus = pushStatus;
		if(pushStatus==0)
		{
			pushStatusDisplay="Not Yet Pushed";
		}
		else
		{
			pushStatusDisplay="Already Pushed";
		}			
	}
	
	public String getPushStatusDisplay() {
		return pushStatusDisplay;
	}
	public void setPushStatusDisplay(String pushStatusDisplay) {
		this.pushStatusDisplay = pushStatusDisplay;
	}
	public String getStatusDisplay() {
		return statusDisplay;
	}
	public void setStatusDisplay(String statusDisplay) {
		this.statusDisplay = statusDisplay;
	}
		
}