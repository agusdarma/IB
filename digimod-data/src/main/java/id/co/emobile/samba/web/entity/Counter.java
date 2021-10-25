package id.co.emobile.samba.web.entity;

import java.util.Date;

public class Counter implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String appName;  // app_name CHAR(10) NOT NULL,
    private int counter;  // counter INT NOT NULL,
    private Date period;  // period DATETIME NOT NULL,
    
    private int oldCounter;  // for reference when update counter
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public Date getPeriod() {
		return period;
	}
	public void setPeriod(Date period) {
		this.period = period;
	}
	public int getOldCounter() {
		return oldCounter;
	}
	public void setOldCounter(int oldCounter) {
		this.oldCounter = oldCounter;
	}
	@Override
	public String toString() {
		return "Counter@" + Integer.toHexString(hashCode()) 
			+ "[appName=" + appName 
			+ ", counter=" + counter 
			+ ", period=" + period 
			+ ", oldCounter=" + oldCounter 
			+ "]";
	}

}
