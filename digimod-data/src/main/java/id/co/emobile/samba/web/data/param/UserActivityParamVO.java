package id.co.emobile.samba.web.data.param;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class UserActivityParamVO extends ParamPagingVO {
	private static final long serialVersionUID = 1L; 

	private int userDataId; 
	private Date startDate;
	private Date endDate;
	
	@Override 
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
	@Override
	protected String getPrimaryKey() {
		return "id";
	}
	
	public int getUserDataId() {
		return userDataId;
	}
	public void setUserDataId(int userDataId) {
		this.userDataId = userDataId;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		if (startDate == null)
		{
			this.startDate = null;
		}
		else
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			this.startDate = cal.getTime();
		}
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		if (endDate == null)
		{
			this.endDate = null;
		}
		else
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			this.endDate = cal.getTime();
		}
	}
	
}