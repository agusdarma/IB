package id.co.emobile.samba.web.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import id.co.emobile.samba.web.data.param.MemberTrxReportParamVO;
import id.co.emobile.samba.web.entity.HistoryTrading;

public interface HistoryTradingMapper {
	public int createHistoryTrading(HistoryTrading historyTrading);

	public int updateHistoryTrading(HistoryTrading historyTrading);

	public HistoryTrading findHistoryTrading(@Param("symbol") String symbol, @Param("openTime") Date openTime,
			@Param("closeTime") Date closeTime, @Param("myfxbookId") String myfxbookId);

	public List<HistoryTrading> selectHistoryTradingByParam(MemberTrxReportParamVO paramVO);

	public List<HistoryTrading> selectHistoryTradingByParamNoPaging(MemberTrxReportParamVO paramVO);

}
