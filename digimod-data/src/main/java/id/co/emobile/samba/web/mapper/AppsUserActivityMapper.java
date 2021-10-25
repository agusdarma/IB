package id.co.emobile.samba.web.mapper;

import java.util.List;

import id.co.emobile.samba.web.data.GenerateSummaryVO;
import id.co.emobile.samba.web.entity.AppsUserActivity;

public interface AppsUserActivityMapper {

	public int insertAppsUserActivity(GenerateSummaryVO  summaryVO);
	public void deleteAppsUserActivity(GenerateSummaryVO summaryVO);
	public List<AppsUserActivity> findAppsUserActivity(GenerateSummaryVO summaryVO);
	public int checkTable(String tableName);
}
