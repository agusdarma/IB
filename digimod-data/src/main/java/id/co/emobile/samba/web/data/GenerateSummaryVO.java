package id.co.emobile.samba.web.data;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class GenerateSummaryVO  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private Date trxDate;
	private String tableName;
	private Date startDate;
	private Date endDate;
		
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public Date getTrxDate() {
		return trxDate;
	}
	
	public void setTrxDate(Date trxDate) {
		this.trxDate = trxDate;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
