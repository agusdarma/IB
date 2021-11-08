package id.co.emobile.samba.web.service;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.WebResultVO;
import id.co.emobile.samba.web.entity.HistoryWithdraw;
import id.co.emobile.samba.web.mapper.HistoryWithdrawMapper;

@Service
public class HistoryWithdrawService {
	private static final Logger LOGGER = LoggerFactory.getLogger(HistoryWithdrawService.class);
	@Autowired
	private HistoryWithdrawMapper historyWithdrawMapper;

	@Autowired
	private AppsTimeService timeService;

	@Autowired
	private BizMessageService messageService;
	
	@Autowired
	private UserActivityService userActivityService;

	@Transactional(rollbackFor = Exception.class)
	public WebResultVO withdrawing(String amount, UserDataLoginVO loginVO, Locale language) throws SambaWebException {
		WebResultVO wrv = new WebResultVO();
		Date now = timeService.getCurrentTime();

		if (StringUtils.isEmpty(amount)) {
			LOGGER.warn("Amount is empty !");
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,
					new String[] { messageService.getMessageFor("e.amountEmpty") });
		}
		try {
			double amountDouble = Double.parseDouble(amount);
			if (amountDouble < 1) {
				LOGGER.warn("Minimum withdraw 1$ !");
				throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,
						new String[] { messageService.getMessageFor("e.amountMinWd") });
			}	
		} catch (SambaWebException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("Amount Must Numeric !");
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,
					new String[] { messageService.getMessageFor("e.amountNumeric") });
		}
		
		HistoryWithdraw historyWithdraw = new HistoryWithdraw();
		try {			
			historyWithdraw.setDateWithdrawOn(now);
			historyWithdraw.setIbUserCode(loginVO.getUserCode());
			historyWithdraw.setStatus(WebConstants.WD_STATUS_PENDING);
			historyWithdrawMapper.createHistoryWithdraw(historyWithdraw);

			String desc = "Withdraw with amount " + historyWithdraw.getAmount() + " created.";
			userActivityService.createUserActivityCreateData(loginVO,
					UserActivityService.MODULE_MANAGE_WITHDRAW_COMMISSION, desc,
					UserActivityService.TABLE_HISTORY_WITHDRAW, historyWithdraw.getId(),
					historyWithdraw);

			wrv.setRc(WebConstants.RESULT_SUCCESS);
			wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
					new String[] { messageService.getMessageFor("l.withdrawCommissionMenu", language),
							messageService.getMessageFor("l.created", language) },
					language));
			wrv.setType(WebConstants.TYPE_INSERT);
			return wrv;
		} catch (Exception swe) {
			LOGGER.warn("error when saving withdraw {}" + historyWithdraw, swe);
			throw swe;
		}

	}

	public List<HistoryWithdraw> findHistoryWithdrawByIbUserCode(String ibUserCode) throws SambaWebException {
		LOGGER.info("findHistoryWithdrawByIbUserCode : " + ibUserCode);
		List<HistoryWithdraw> listHistoryWithdraws = historyWithdrawMapper.findHistoryWithdrawByIbUserCode(ibUserCode);
		return listHistoryWithdraws;
	}
}
