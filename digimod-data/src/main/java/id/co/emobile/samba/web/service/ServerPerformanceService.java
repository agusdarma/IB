package id.co.emobile.samba.web.service;

import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.entity.ServerPerformance;
import id.co.emobile.samba.web.mapper.ServerPerformanceMapper;
import id.co.emobile.samba.web.mapper.SummaryReportMapper;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class ServerPerformanceService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerPerformanceService.class);

	@Autowired
	private ServerPerformanceMapper serverPerformanceMapper;
	
	@Autowired
	private AppsTimeService timeService;
	
	private String serverNameForTrx;
	
	@Autowired
	private SummaryReportMapper summaryReportMapper;
	
	private final SimpleDateFormat sdfSuffix = new SimpleDateFormat("yyyyMMdd");
	private final SimpleDateFormat sdfMonthYear = new SimpleDateFormat("MMyyyy");
	
	@Transactional(rollbackFor=Exception.class)
	public void insertServerPerformance(String serverName, String memory, String cpu, String diskAvailable) throws SambaWebException {
		try {
			Date serverTime = timeService.getCurrentTime();
			ServerPerformance serverPerformance = new ServerPerformance();
			serverPerformance.setCpuUsage(cpu);
			serverPerformance.setDiskAvailable(diskAvailable);
			serverPerformance.setMemoryUsage(memory);
			serverPerformance.setServerName(serverName);
			serverPerformance.setServerTime(serverTime);
			serverPerformance.setTotalTrx(0);
			if(serverNameForTrx.equalsIgnoreCase(serverName))
			{
				String tableName = makeTableName(WebConstants.TABLE_TRXLOGH, serverTime);
				int existTable = checkTable(tableName);
				if(existTable==0)
				{
					LOGGER.debug("Table {}  does not exist", tableName);
				}
				else
				{
					int totalTrx = serverPerformanceMapper.findTotalTrx(tableName, serverTime);
					serverPerformance.setTotalTrx(totalTrx);
				}
			}			
			serverPerformanceMapper.insertServerPerformance(serverPerformance);			
		} catch (Exception e) {
			LOGGER.warn("Exception when saving serverPerformance", e);
		}
	}

	public String getServerNameForTrx() {
		return serverNameForTrx;
	}

	public void setServerNameForTrx(String serverNameForTrx) {
		this.serverNameForTrx = serverNameForTrx;
	}

	public String makeTableName(String prefix, Date date)
	{
		return prefix + "_" + sdfSuffix.format(date);
	}
	
	public int checkTable(String tableName)
	{
		int result = summaryReportMapper.checkTable(tableName);
		return result;
	}
	
}