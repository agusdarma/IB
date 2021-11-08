package id.co.emobile.samba.web.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.co.emobile.samba.web.entity.HistoryWithdraw;
import id.co.emobile.samba.web.mapper.HistoryWithdrawMapper;

@Service
public class HistoryWithdrawService {
	private static final Logger LOGGER = LoggerFactory.getLogger(HistoryWithdrawService.class);
	@Autowired
	private HistoryWithdrawMapper historyWithdrawMapper;

	public void createHistoryWithdraw(HistoryWithdraw historyWithdraw) throws SambaWebException {
		LOGGER.info("createHistoryWithdraw : " + historyWithdraw);
		historyWithdrawMapper.createHistoryWithdraw(historyWithdraw);
	}

	public List<HistoryWithdraw> findHistoryWithdrawByIbUserCode(String ibUserCode) throws SambaWebException {
		LOGGER.info("findHistoryWithdrawByIbUserCode : " + ibUserCode);
		List<HistoryWithdraw> listHistoryWithdraws = historyWithdrawMapper.findHistoryWithdrawByIbUserCode(ibUserCode);
		return listHistoryWithdraws;
	}
}
