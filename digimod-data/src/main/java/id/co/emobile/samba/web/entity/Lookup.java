package id.co.emobile.samba.web.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Lookup implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int lookupCat;
	private String lookupValue;
	private int lookupOrder;
	private String lookupDesc;
	private int updatedBy;
	private Date updatedOn;
	private String updatedByDisplay;
	private	int lookupCatOld;
	private String lookupValueOld;


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

	public int getLookupOrder() {
		return lookupOrder;
	}
	public void setLookupOrder(int lookupOrder) {
		this.lookupOrder = lookupOrder;
	}

	public String getLookupDesc() {
		return lookupDesc;
	}
	public void setLookupDesc(String lookupDesc) {
		this.lookupDesc = lookupDesc;
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
	public String getUpdatedByDisplay() {
		return updatedByDisplay;
	}
	public void setUpdatedByDisplay(String updatedByDisplay) {
		this.updatedByDisplay = updatedByDisplay;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	public int getLookupCatOld() {
		return lookupCatOld;
	}
	public void setLookupCatOld(int lookupCatOld) {
		this.lookupCatOld = lookupCatOld;
	}
	public String getLookupValueOld() {
		return lookupValueOld;
	}
	public void setLookupValueOld(String lookupValueOld) {
		this.lookupValueOld = lookupValueOld;
	}

}
