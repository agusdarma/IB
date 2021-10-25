package id.co.emobile.samba.web.mapper;

import id.co.emobile.samba.web.data.MbankingUserVO;
import id.co.emobile.samba.web.data.param.ReportParamVO;
import id.co.emobile.samba.web.entity.Advertising;
import id.co.emobile.samba.web.entity.AppsUserActivity;
import id.co.emobile.samba.web.entity.SummaryDailyTrx;
import id.co.emobile.samba.web.entity.TrxLogH;

import java.util.List;

public interface ReportMapper {

	public List<Advertising> findAdvertisingReport(ReportParamVO paramVO);
	public int countAdvertisingReport(ReportParamVO paramVO);
	
	public List<AppsUserActivity> findAppsUserActivity(ReportParamVO paramVO);	
	public int countAppsUserActivity(ReportParamVO paramVO);
	
	public List<TrxLogH> findDailyTrx(ReportParamVO paramVO);	
	public int countDailyTrx(ReportParamVO paramVO);
	
	public List<SummaryDailyTrx> findSummaryDailyTrx(ReportParamVO paramVO);	
	public int countSummaryDailyTrx(ReportParamVO paramVO);
	
	public List<MbankingUserVO> findRegisteredMbankingUser(ReportParamVO paramVO);
	int countRegisteredMbankingUser(ReportParamVO paramVO);
	public List<MbankingUserVO> findAllMbankingUser(ReportParamVO paramVO);
	int countAllMbankingUser(ReportParamVO paramVO);
	public List<MbankingUserVO> findNotRegisteredMbankingUser(ReportParamVO paramVO);
	int countNotRegisteredMbankingUser(ReportParamVO paramVO);
	
	
}
