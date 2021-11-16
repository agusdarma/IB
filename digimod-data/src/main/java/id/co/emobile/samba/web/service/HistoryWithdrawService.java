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
import id.co.emobile.samba.web.entity.UserData;
import id.co.emobile.samba.web.mapper.HistoryWithdrawMapper;
import id.co.emobile.samba.web.mapper.UserDataMapper;

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

	@Autowired
	private SendEmailService sendEmailService;

	@Autowired
	private UserDataMapper userDataMapper;

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
			amountDouble = Double.parseDouble(amount);
			double maxAvailableWd = Double.parseDouble(loginVO.getClientCommissionAvailable());
			if (amountDouble > maxAvailableWd) {
				LOGGER.warn("Insufficient balance ! Max withdraw : " + maxAvailableWd);
				throw new SambaWebException(SambaWebException.NE_INSUFFICIENT_BALANCE,
						new String[] { messageService.getMessageFor("e.amountMaxWd",
								new String[] { loginVO.getClientCommissionAvailable() }, language) });
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
			UserData userData = userDataMapper.findUserDataByUserCode(loginVO.getUserCode());
			updateWithdrawAvailable(amount, userData);
			historyWithdraw.setDateWithdrawOn(now);
			historyWithdraw.setIbUserCode(loginVO.getUserCode());
			historyWithdraw.setStatus(WebConstants.WD_STATUS_PENDING);
			historyWithdraw.setAmount(Double.parseDouble(amount));
			historyWithdrawMapper.createHistoryWithdraw(historyWithdraw);

			String desc = "Withdraw with amount " + historyWithdraw.getAmount() + " created.";
			userActivityService.createUserActivityCreateData(loginVO,
					UserActivityService.MODULE_MANAGE_WITHDRAW_COMMISSION, desc,
					UserActivityService.TABLE_HISTORY_WITHDRAW, historyWithdraw.getId(), historyWithdraw);

			wrv.setRc(WebConstants.RESULT_SUCCESS);
			wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
					new String[] { messageService.getMessageFor("l.withdrawCommissionMenu", language),
							messageService.getMessageFor("l.created", language) },
					language));
			wrv.setType(WebConstants.TYPE_INSERT);
//			sendEmailService.sendEmail(historyWithdraw, "Withdrawal Request Received", "agusdk2011@gmail.com", "Agus",
//					"Pending");
			sendEmailService.sendEmailAsync(historyWithdraw, "Withdrawal Request Received", "agusdk2011@gmail.com", "Agus",
					"Pending");
			return wrv;
		} catch (Exception swe) {
			LOGGER.warn("error when saving withdraw {}" + historyWithdraw, swe);
			throw swe;
		}

	}

	private void updateWithdrawAvailable(String amount, UserData userData) {
		try {
			double clientCommissionWithdrawn = Double.parseDouble(userData.getClientCommissionWithdrawn());
			double amountWd = Double.parseDouble(amount);
			clientCommissionWithdrawn = clientCommissionWithdrawn + amountWd;
			userData.setClientCommissionWithdrawn(Double.toString(clientCommissionWithdrawn));
			LOGGER.info("Update Withdraw Available " + userData);
			userDataMapper.updateCommission(userData);
		} catch (Exception e) {
			throw e;
		}
	}

	public List<HistoryWithdraw> findHistoryWithdrawByIbUserCode(String ibUserCode) throws SambaWebException {
		LOGGER.info("findHistoryWithdrawByIbUserCode : " + ibUserCode);
		List<HistoryWithdraw> listHistoryWithdraws = historyWithdrawMapper.findHistoryWithdrawByIbUserCode(ibUserCode);
		return listHistoryWithdraws;
	}
}
