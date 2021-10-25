package id.co.emobile.samba.web.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerPerformance implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	
	private Date serverTime; //trx_date
	private String serverName;
	private String cpuUsage;
	private String memoryUsage;
	private String diskAvailable;
	private int totalTrx;
	
	public Date getServerTime() {
		return serverTime;
	}
	public void setServerTime(Date serverTime) {
		this.serverTime = serverTime;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getCpuUsage() {
		return cpuUsage;
	}
	public void setCpuUsage(String cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	public String getMemoryUsage() {
		return memoryUsage;
	}
	public void setMemoryUsage(String memoryUsage) {
		this.memoryUsage = memoryUsage;
	}
	public String getDiskAvailable() {
		return diskAvailable;
	}
	public void setDiskAvailable(String diskAvailable) {
		this.diskAvailable = diskAvailable;
	}	
	public int getTotalTrx() {
		return totalTrx;
	}
	public void setTotalTrx(int totalTrx) {
		this.totalTrx = totalTrx;
	}
	
	@Override
	public String toString() {
		return "ServerPerformance@" + Integer.toHexString(hashCode()) 
			+ "[serverTime=" + serverTime 
			+ ", serverName=" + serverName 
			+ ", cpuUsage=" + cpuUsage
			+ ", memoryUsage=" + memoryUsage 
			+ ", diskAvailable=" + diskAvailable 
			+ ", totalTrx=" + totalTrx 
			+ "]";
	}
}
