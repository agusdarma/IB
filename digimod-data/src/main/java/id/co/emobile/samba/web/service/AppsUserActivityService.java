package id.co.emobile.samba.web.service;

import id.co.emobile.samba.web.data.GenerateSummaryVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.entity.AppsUserActivity;
import id.co.emobile.samba.web.entity.SummaryDailyTrx;
import id.co.emobile.samba.web.mapper.AppsUserActivityMapper;
import id.co.emobile.samba.web.mapper.SummaryReportMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppsUserActivityService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppsUserActivityService.class);

	private final SimpleDateFormat sdfSuffix = new SimpleDateFormat("yyyyMMdd");
	
	@Autowired
	private AppsTimeService timeService;

	@Autowired
	private SettingService settingService;
	
	@Autowired
	private AppsUserActivityMapper appsUserActivityMapper;
	
	@Autowired
	private LookupService lookupService;
	
	public void generateAppsUserActivity(Date date)
	{
		LOGGER.debug("generate apps user activity with date {}", date);
		try {
			String tableName = makeTableName(WebConstants.TABLE_TRXLOGH, date);
			GenerateSummaryVO generateSummaryVO = new GenerateSummaryVO();
			generateSummaryVO.setTableName(tableName);
			generateSummaryVO.setTrxDate(date);
			LOGGER.info("inserting apps user activity from table  :" + tableName);
			appsUserActivityMapper.deleteAppsUserActivity(generateSummaryVO);
			appsUserActivityMapper.insertAppsUserActivity(generateSummaryVO);
			LOGGER.info("generate apps user activity for date " + date + " successfull");
		} catch (Exception e) {
			LOGGER.info("insert apps user activity failed, cause : " + e.getMessage());
		}
	}
	
	public List<AppsUserActivity> generateAppsUserActivityRanged(Date startDate, Date endDate)
	{
		if(startDate==null)startDate = new Date();
		if(endDate==null)endDate = new Date();		
		LOGGER.debug("generateAppsUserActivity with date ranged from {} to {}", startDate, endDate);
		List<AppsUserActivity> result = new ArrayList<AppsUserActivity>();
		try {
			long diff=0;
			diff=endDate.getTime()-startDate.getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);
			LOGGER.debug("diffDays = " + diffDays);
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			for (int i = 0; i <= diffDays; i++) 
			{
				Date theDay = cal.getTime();
				String tableName = makeTableName(WebConstants.TABLE_TRXLOGH, theDay);
				int existTable = checkTable(tableName);
				if(existTable==0)
				{
					LOGGER.debug("Table {}  does not exist", tableName);
				}
				else
				{
					LOGGER.debug("make summary daily trx for table  : " + tableName);
					GenerateSummaryVO generateSummaryVO = new GenerateSummaryVO();
					generateSummaryVO.setTableName(tableName);
					generateSummaryVO.setTrxDate(theDay);
					appsUserActivityMapper.deleteAppsUserActivity(generateSummaryVO);
					appsUserActivityMapper.insertAppsUserActivity(generateSummaryVO);					
				}				
				cal.add(cal.DATE, 1);
			}
			GenerateSummaryVO generateSummaryVO = new GenerateSummaryVO();
			generateSummaryVO.setStartDate(startDate);
			generateSummaryVO.setEndDate(endDate);
			result = appsUserActivityMapper.findAppsUserActivity(generateSummaryVO);
		} catch (Exception e) {
			LOGGER.info("insert apps user activity ranged failed, cause : " + e.getMessage());
		}
		return result;
	}
	
	public String makeTableName(String prefix, Date date)
	{
		return prefix + "_" + sdfSuffix.format(date);
	}
	
	public int checkTable(String tableName)
	{
		int result = appsUserActivityMapper.checkTable(tableName);
		return result;
	}
	
}
