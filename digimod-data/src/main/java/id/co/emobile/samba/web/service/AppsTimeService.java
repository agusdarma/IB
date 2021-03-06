package id.co.emobile.samba.web.service;

import id.co.emobile.samba.web.mapper.MiscMapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppsTimeService {
	private static final Logger LOG = LoggerFactory.getLogger(AppsTimeService.class);
	
	private SimpleDateFormat dfDate = new SimpleDateFormat("yyyyMMdd");
	
	private long delta = 0;
	
	@Autowired
	private MiscMapper miscMapper;
	
//	public Date getCurrentTime() {
//		long localTime = System.currentTimeMillis();
//		Date serverTime = new Date(localTime + delta);
//		return serverTime;
//	}
	public Date getCurrentTime() {
//		long localTime = System.currentTimeMillis();
		Date serverTime = new Date();
		return serverTime;
	}
	
	public long getCurrentTimemillis() {
		long localTime = System.currentTimeMillis();
		return localTime + delta;
	}
	
	public synchronized void updateCurrentTime() {
		try {
			long localTime = System.currentTimeMillis();
			long serverTime = miscMapper.selectCurrentTime().getTime();
			delta = serverTime - localTime;
		} catch (Exception e) {
			LOG.warn("Exception in updateCurrentTime", e);
		}
	}
	
	public boolean isToday(Date aDate) {
		Date now = getCurrentTime();
		return dfDate.format(now).equals(dfDate.format(aDate));
	}
	
	public long secondsBetween(Date aDate, Date otherDate) {
		long l1 = aDate.getTime();
		long l2 = otherDate.getTime();
		
		return (l2 - l1) / 1000;
	}
	
	public Date addTime(int field, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentTime());
		cal.add(field, amount);		
		return cal.getTime();
	}
}
