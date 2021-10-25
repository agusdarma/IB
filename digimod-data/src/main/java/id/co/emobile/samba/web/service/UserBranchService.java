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
import id.co.emobile.samba.web.data.param.UserBranchParamVO;
import id.co.emobile.samba.web.entity.UserBranch;
import id.co.emobile.samba.web.mapper.UserBranchMapper;

@Service
public class UserBranchService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserBranchService.class);

	@Autowired
	private AppsTimeService timeService;

	@Autowired
	private UserBranchMapper userBranchMapper;

	@Autowired
	private BizMessageService messageService;

	@Autowired
	private UserActivityService userActivityService;

	@Transactional(rollbackFor = Exception.class)
	public WebResultVO insertOrUpdateUserBranch(UserBranch userBranch, UserDataLoginVO loginVO, Locale language)
			throws SambaWebException {
		WebResultVO wrv = new WebResultVO();
		Date now = timeService.getCurrentTime();

		userBranch.setUpdatedBy(loginVO.getId());
		userBranch.setUpdatedOn(now);
		if (StringUtils.isEmpty(userBranch.getBranchName())) {
			LOGGER.warn("Branch name is empty !");
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,
					new String[] { messageService.getMessageFor("l.branchName") });
		}
		if (userBranch.getId() == 0) {
			userBranch.setCreatedBy(loginVO.getId());
			userBranch.setCreatedOn(now);
			try {
				userBranchMapper.createUserBranch(userBranch);

				String desc = "Create UserBranch " + userBranch.getBranchName();
				userActivityService.createUserActivityCreateData(loginVO, UserActivityService.MODULE_MANAGE_USER_BRANCH,
						desc, UserActivityService.TABLE_USER_BRANCH, userBranch.getId(), userBranch);

				wrv.setRc(WebConstants.RESULT_SUCCESS);
				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
						new String[] { messageService.getMessageFor("l.userBranch", language),
								messageService.getMessageFor("l.created", language) },
						language));
				wrv.setType(WebConstants.TYPE_INSERT);
				return wrv;
			} catch (Exception swe) {
				LOGGER.warn("error when saving user branch {}" + userBranch, swe);
				throw swe;
			}
		} else {
			LOGGER.debug("update user branch with data :" + userBranch);
			try {
				UserBranch original = findUserBranchById(userBranch.getId());

				userBranchMapper.updateUserBranch(userBranch);

				String desc = "Update UserBranch " + userBranch.getBranchName();
				userActivityService.createUserActivityUpdateData(loginVO, UserActivityService.MODULE_MANAGE_USER_BRANCH,
						desc, UserActivityService.TABLE_USER_BRANCH, userBranch.getId(), userBranch, original);

				wrv.setRc(WebConstants.RESULT_SUCCESS);
				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
						new String[] { messageService.getMessageFor("l.userBranch", language),
								messageService.getMessageFor("l.updated", language) },
						language));
				wrv.setType(WebConstants.TYPE_UPDATE);
				wrv.setPath(WebConstants.PATH_UPDATE_USER_BRANCH);
				return wrv;
			} catch (Exception e) {
				LOGGER.warn("Gagal update user branch :" + userBranch, e);
				throw new SambaWebException(e);
			}
		}
	}

	public List<UserBranch> findUserBranchAll() {
		List<UserBranch> listUser = userBranchMapper.findAllUserBranch();
		if (listUser == null)
			listUser = new ArrayList<>();
		return listUser;
	}

	public List<UserBranch> findUserBranchByParam(UserBranchParamVO paramVO) {
		try {
			List<UserBranch> listUser = userBranchMapper.findUserBranchByParam(paramVO);
			return listUser;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return null;
		}
	}

	public int countUserBranchByParam(UserBranchParamVO paramVO) {
		try {
			int count = userBranchMapper.countUserBranchByParam(paramVO);
			return count;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return 0;
		}
	}

	public UserBranch findUserBranchById(int branchId) {
		try {
			UserBranch user = userBranchMapper.findUserBranchById(branchId);
			return user;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return null;
		}
	}

}
