package id.co.emobile.samba.web.mapper;

import java.util.List;

import id.co.emobile.samba.web.entity.HistoryWithdraw;

public interface HistoryWithdrawMapper {
	public int createHistoryWithdraw(HistoryWithdraw historyWithdraw);

	public List<HistoryWithdraw> findHistoryWithdrawByIbUserCode(String ibUserCode);
}
