package id.co.emobile.samba.web.mapper;

import java.util.List;

import id.co.emobile.samba.web.data.GenerateSummaryVO;
import id.co.emobile.samba.web.entity.SummaryDailyTrx;

public interface SummaryReportMapper {

	public int insertSummaryDailyTrx(GenerateSummaryVO  summaryVO);
	public void deleteSummaryDailyTrx(GenerateSummaryVO summaryVO);
	public List<SummaryDailyTrx> findSummaryDailyTrx(GenerateSummaryVO summaryVO);
	public int checkTable(String tableName);
}
