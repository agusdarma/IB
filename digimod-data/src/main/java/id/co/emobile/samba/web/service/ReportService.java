package id.co.emobile.samba.web.service;

import id.co.emobile.samba.web.data.MbankingUserVO;
import id.co.emobile.samba.web.data.param.ReportParamVO;
import id.co.emobile.samba.web.entity.Advertising;
import id.co.emobile.samba.web.entity.AppsUserActivity;
import id.co.emobile.samba.web.entity.SummaryDailyTrx;
import id.co.emobile.samba.web.entity.TrxLogH;
import id.co.emobile.samba.web.mapper.ReportMapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
//	private DateFormat df = new SimpleDateFormat("dd/MM");

	@Autowired
	private ReportMapper reportMapper;
	
	public List<Advertising> findAdvertisingReport(ReportParamVO paramVO){
		try {
			List<Advertising> listAdv = reportMapper.findAdvertisingReport(paramVO);
			return listAdv;
		} catch (Exception e) {
			LOGGER.error("ERROR: " + e, e);
			List<Advertising> listAdv = new ArrayList<Advertising>();
			return listAdv;
		}		
	}
	
	public int countAdvertisingReport(ReportParamVO paramVO){
		try {
			int count = reportMapper.countAdvertisingReport(paramVO);
			return count;
		} catch (Exception e) {
			LOGGER.error("ERROR: " + e, e);
			return 0;
		}		
	}
	
	public List<AppsUserActivity> findAppsUserActivity(ReportParamVO paramVO){
		try {
			List<AppsUserActivity> listAppsUserActivity = reportMapper.findAppsUserActivity(paramVO);
			return listAppsUserActivity;
		} catch (Exception e) {
			LOGGER.error("ERROR: " + e, e);
			List<AppsUserActivity> listApps = new ArrayList<AppsUserActivity>();
			return listApps;
		}		
	}
	
	public int countAppsUserActivity(ReportParamVO paramVO){
		try {
			int count = reportMapper.countAppsUserActivity(paramVO);
			return count;
		} catch (Exception e) {
			LOGGER.error("ERROR: " + e, e);
			return 0;
		}		
	}
	
	public List<TrxLogH> findDailyTransaction(ReportParamVO paramVO){
		try {
			List<TrxLogH> listDailyTransaction = reportMapper.findDailyTrx(paramVO);
			return listDailyTransaction;
		} catch (Exception e) {
			LOGGER.error("ERROR: " + e, e);
			List<TrxLogH> listDailyTransaction = new ArrayList<TrxLogH>();
			return listDailyTransaction;
		}
	}
	
	public int countDailyTransaction(ReportParamVO paramVO){
		try {
			int count = reportMapper.countDailyTrx(paramVO);
			return count;
		} catch (Exception e) {
			LOGGER.error("ERROR: " + e, e);
			return 0;
		}		
	}
	
	public List<SummaryDailyTrx> findSummaryDailyTransaction(ReportParamVO paramVO){
		try {
			List<SummaryDailyTrx> listDailyTransaction = reportMapper.findSummaryDailyTrx(paramVO);
			return listDailyTransaction;
		} catch (Exception e) {
			LOGGER.error("ERROR: " + e, e);
			List<SummaryDailyTrx> listDailyTransaction = new ArrayList<SummaryDailyTrx>();
			return listDailyTransaction;
		}
	}
	
	public int countSummaryDailyTransaction(ReportParamVO paramVO){
		try {
			int count = reportMapper.countSummaryDailyTrx(paramVO);
			return count;
		} catch (Exception e) {
			LOGGER.error("ERROR: " + e, e);
			return 0;
		}		
	}
	
	public List<MbankingUserVO> findMbankingUser(ReportParamVO paramVO){
		try {
			List<MbankingUserVO> listMbankingUser = new ArrayList<MbankingUserVO>();
			if(paramVO.getUserType()==0){
				listMbankingUser = reportMapper.findAllMbankingUser(paramVO);				
			}
			else if(paramVO.getUserType()==1){
				listMbankingUser = reportMapper.findRegisteredMbankingUser(paramVO);								
			}
			else{
				listMbankingUser = reportMapper.findNotRegisteredMbankingUser(paramVO);
			}
			return listMbankingUser;
		} catch (Exception e) {
			LOGGER.error("ERROR: " + e, e);
			List<MbankingUserVO> listMbankingUser = new ArrayList<MbankingUserVO>();
			return listMbankingUser;
		}
	}
	
	public int countMbankingUser(ReportParamVO paramVO){
		try {
			int count = 0;
			if(paramVO.getUserType()==0){
				count = reportMapper.countAllMbankingUser(paramVO);				
			}
			else if(paramVO.getUserType()==1){
				count = reportMapper.countRegisteredMbankingUser(paramVO);								
			}
			else{
				count = reportMapper.countNotRegisteredMbankingUser(paramVO);
			}
			return count;
		} catch (Exception e) {
			LOGGER.error("ERROR: " + e, e);
			return 0;
		}		
	}
	
	public String makeTableName(String prefix, Date date)
	{
		Calendar calFr = Calendar.getInstance();
		calFr.setTime(date);
		int month = calFr.get(Calendar.MONTH);
		String monthString = String.valueOf(month+1);
		if (monthString.length() == 1)
			monthString = "0" + monthString;
		int year = calFr.get(Calendar.YEAR);
		String yearInString = String.valueOf(year);
		int dateInt = calFr.get(Calendar.DATE);
		String dateInString = String.valueOf(dateInt);
		if (dateInString.length() == 1)
			dateInString = "0" + dateInString;		
		String tableName = prefix + yearInString + monthString + dateInString;
		return tableName;
	}
}
