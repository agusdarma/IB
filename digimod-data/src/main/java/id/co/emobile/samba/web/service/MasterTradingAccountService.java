package id.co.emobile.samba.web.service;

import java.util.ArrayList;
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
import id.co.emobile.samba.web.data.param.MasterTradingAccountParamVO;
import id.co.emobile.samba.web.entity.MasterTradingAccount;
import id.co.emobile.samba.web.mapper.MasterTradingAccountMapper;

@Service
public class MasterTradingAccountService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MasterTradingAccountService.class);

	@Autowired
	private AppsTimeService timeService;

	@Autowired
	private MasterTradingAccountMapper masterTradingAccountMapper;

	@Autowired
	private BizMessageService messageService;

	@Autowired
	private UserActivityService userActivityService;

	@Transactional(rollbackFor = Exception.class)
	public WebResultVO insertOrUpdateMasterTradingAccount(MasterTradingAccount masterTradingAccount,
			UserDataLoginVO loginVO, Locale language) throws SambaWebException {
		WebResultVO wrv = new WebResultVO();
		Date now = timeService.getCurrentTime();

		masterTradingAccount.setUpdatedBy(loginVO.getId());
		masterTradingAccount.setUpdatedOn(now);
		if (StringUtils.isEmpty(masterTradingAccount.getTradingAccountNo())) {
			LOGGER.warn("Trading Account No is empty !");
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,
					new String[] { messageService.getMessageFor("l.tradingAccountNo") });
		}
		if (StringUtils.isEmpty(masterTradingAccount.getTradingServer())) {
			LOGGER.warn("Trading Server is empty !");
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,
					new String[] { messageService.getMessageFor("l.tradingServer") });
		}
		if (StringUtils.isEmpty(masterTradingAccount.getName())) {
			LOGGER.warn("Account Name is empty !");
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,
					new String[] { messageService.getMessageFor("l.accountName") });
		}
		if (masterTradingAccount.getId() == 0) {
			masterTradingAccount.setCreatedBy(loginVO.getId());
			masterTradingAccount.setCreatedOn(now);
			try {
				masterTradingAccountMapper.createMasterTradingAccount(masterTradingAccount);

				String desc = "Create Master Trading Account " + masterTradingAccount.getName();
				userActivityService.createUserActivityCreateData(loginVO,
						UserActivityService.MODULE_MANAGE_MASTER_TRADING_ACCOUNT, desc,
						UserActivityService.TABLE_MASTER_TRADING_ACCOUNT, masterTradingAccount.getId(),
						masterTradingAccount);

				wrv.setRc(WebConstants.RESULT_SUCCESS);
				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
						new String[] { messageService.getMessageFor("l.masterTradingAccount", language),
								messageService.getMessageFor("l.created", language) },
						language));
				wrv.setType(WebConstants.TYPE_INSERT);
				return wrv;
			} catch (Exception swe) {
				LOGGER.warn("error when saving master trading account {}" + masterTradingAccount, swe);
				throw swe;
			}
		} else {
			LOGGER.debug("update master trading account with data :" + masterTradingAccount);
			try {
				MasterTradingAccount original = findMasterTradingAccountById(masterTradingAccount.getId());

				masterTradingAccountMapper.updateMasterTradingAccount(masterTradingAccount);

				String desc = "Update MasterTradingAccount " + masterTradingAccount.getName();
				userActivityService.createUserActivityUpdateData(loginVO,
						UserActivityService.MODULE_MANAGE_MASTER_TRADING_ACCOUNT, desc,
						UserActivityService.TABLE_MASTER_TRADING_ACCOUNT, masterTradingAccount.getId(),
						masterTradingAccount, original);

				wrv.setRc(WebConstants.RESULT_SUCCESS);
				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
						new String[] { messageService.getMessageFor("l.masterTradingAccount", language),
								messageService.getMessageFor("l.updated", language) },
						language));
				wrv.setType(WebConstants.TYPE_UPDATE);
				wrv.setPath(WebConstants.PATH_UPDATE_MASTER_TRADING_ACCOUNT);
				return wrv;
			} catch (Exception e) {
				LOGGER.warn("Gagal update master trading account :" + masterTradingAccount, e);
				throw new SambaWebException(e);
			}
		}
	}

	public List<MasterTradingAccount> findMasterTradingAccountAll() {
		List<MasterTradingAccount> listUser = masterTradingAccountMapper.findAllMasterTradingAccount();
		if (listUser == null)
			listUser = new ArrayList<>();
		return listUser;
	}

	public List<MasterTradingAccount> findMasterTradingAccountByParam(MasterTradingAccountParamVO paramVO) {
		try {
			List<MasterTradingAccount> listUser = masterTradingAccountMapper.findMasterTradingAccountByParam(paramVO);
			return listUser;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return null;
		}
	}

	public int countUserGroupByParam(MasterTradingAccountParamVO paramVO) {
		try {
			int count = masterTradingAccountMapper.countMasterTradingAccountByParam(paramVO);
			return count;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return 0;
		}
	}

	public MasterTradingAccount findMasterTradingAccountById(int masterTradingAccountId) {
		try {
			MasterTradingAccount masterTradingAccount = masterTradingAccountMapper
					.findMasterTradingAccountById(masterTradingAccountId);
			return masterTradingAccount;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return null;
		}
	}

}
