package id.co.emobile.samba.web.mapper;

import org.apache.ibatis.annotations.Param;

import id.co.emobile.samba.web.entity.HistoryTrading;

public interface HistoryTradingMapper {
	public int createHistoryTrading(HistoryTrading historyTrading);

	public int updateHistoryTrading(HistoryTrading historyTrading);

	public HistoryTrading findHistoryTrading(@Param("symbol") String symbol, @Param("openTime") String openTime,
			@Param("closeTime") String closeTime,@Param("myfxbookId") String myfxbookId);

}
