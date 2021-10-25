package id.co.emobile.samba.web.service;

import java.sql.SQLIntegrityConstraintViolationException;
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
import id.co.emobile.samba.web.data.param.UserGroupParamVO;
import id.co.emobile.samba.web.entity.UserData;
import id.co.emobile.samba.web.entity.UserGroup;
import id.co.emobile.samba.web.mapper.UserGroupMapper;

@Service
public class UserGroupService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupService.class);

	@Autowired
	private AppsTimeService timeService;

	@Autowired
	private UserGroupMapper userGroupMapper;

	@Autowired
	private BizMessageService messageService;

	@Autowired
	private UserActivityService userActivityService;
	
	@Transactional(rollbackFor=Exception.class)
	public WebResultVO insertOrUpdateUserGroup(UserGroup userGroup, UserDataLoginVO loginVO, Locale language) throws SambaWebException 
	{
		WebResultVO wrv = new WebResultVO();
		Date now = timeService.getCurrentTime();
		
		userGroup.setUpdatedBy(loginVO.getId());
		userGroup.setUpdatedOn(now);
		if(StringUtils.isEmpty(userGroup.getGroupName())) {
			LOGGER.warn("Group name is empty !");
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.groupName")});
		}
		if(userGroup.getId()==0)
		{
			userGroup.setCreatedBy(loginVO.getId());
			userGroup.setCreatedOn(now);
			try {
				userGroupMapper.createUserGroup(userGroup);
				
				String desc = "Create UserGroup " + userGroup.getGroupName();
				userActivityService.createUserActivityCreateData(loginVO, 
						UserActivityService.MODULE_MANAGE_USER_GROUP, desc, UserActivityService.TABLE_USER_GROUP, 
						userGroup.getId(), userGroup);
				
				wrv.setRc(WebConstants.RESULT_SUCCESS);
				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
						new String[] {messageService.getMessageFor("l.userGroup", language),
						messageService.getMessageFor("l.created", language)}, language));
				wrv.setType(WebConstants.TYPE_INSERT);
				return wrv;
			} catch (Exception swe) {
				LOGGER.warn("error when saving user group {}" + userGroup, swe);
				throw swe;
			}
		}
		else
		{
			LOGGER.debug("update user group with data :" + userGroup);
			try{
				UserGroup original = findUserGroupById(userGroup.getId());
				
				userGroupMapper.updateUserGroup(userGroup);
				
				String desc = "Update UserGroup " + userGroup.getGroupName();
				userActivityService.createUserActivityUpdateData(loginVO, 
						UserActivityService.MODULE_MANAGE_USER_GROUP, desc, UserActivityService.TABLE_USER_GROUP, 
						userGroup.getId(), userGroup, original);
				
				wrv.setRc(WebConstants.RESULT_SUCCESS);
				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
						new String[] {messageService.getMessageFor("l.userGroup", language),
						messageService.getMessageFor("l.updated", language)}, language));
				wrv.setType(WebConstants.TYPE_UPDATE);
				wrv.setPath(WebConstants.PATH_UPDATE_USER_GROUP);
				return wrv;
			}catch(Exception e){
				LOGGER.warn("Gagal update user group :" + userGroup, e);
				throw new SambaWebException(e);
			}
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	public WebResultVO saveUserGroupForApproval(UserData userApproval, List<Integer> listSelected, UserDataLoginVO loginVO, Locale language) throws SambaWebException 
	{
		WebResultVO wrv = new WebResultVO();
//		Date now = timeService.getCurrentTime();
		
//		userGroup.setUpdatedBy(loginVO.getId());
//		userGroup.setUpdatedOn(now);
//		if(StringUtils.isEmpty(userGroup.getGroupName())) {
//			LOGGER.warn("Group name is empty !");
//			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.groupName")});
//		}
//		if(userGroup.getId()==0)
//		{
//			userGroup.setCreatedBy(loginVO.getId());
//			userGroup.setCreatedOn(now);
			try {
//				userGroupMapper.createUserGroup(userGroup);
//				
				int deleted = userGroupMapper.deleteGroupFor(userApproval.getId());
				int created = 0;
				for (Integer groupId: listSelected) {
					if (groupId == null) continue;
					created += userGroupMapper.createGroupFor(userApproval.getId(), groupId.intValue());
				}
				LOGGER.debug("Deleted {} old group, created {} new group", deleted, created);
				
				String desc = "Create UserGroup Approval for User " + userApproval.getUserCode();
				userActivityService.createUserActivityCreateData(loginVO, 
						UserActivityService.MODULE_MANAGE_GROUP_APPROVAL, desc, UserActivityService.TABLE_USER_GROUP_APPROVAL, 
						userApproval.getId(), null);
				
				wrv.setRc(WebConstants.RESULT_SUCCESS);
				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
						new String[] {messageService.getMessageFor("l.userGroup", language),
						messageService.getMessageFor("l.created", language)}, language));
				wrv.setType(WebConstants.TYPE_UPDATE);
				wrv.setPath(WebConstants.PATH_UPDATE_USER_GROUP_APPROVAL);
				return wrv;
			} catch (Exception swe) {
				LOGGER.warn("error when saving user group approval {}" + userApproval, swe);
				if (swe instanceof SQLIntegrityConstraintViolationException) {
					// catch constraint error
					throw new SambaWebException(SambaWebException.NE_DUPLICATE_DATA, new String[] { "Group Approval" });
				}
				throw swe;
			}
//		}
//		else
//		{
//			LOGGER.debug("update user group with data :" + userGroup);
//			try{
//				userGroupMapper.updateUserGroup(userGroup);
//				
//				/** SET TO USER ACTIVITY **/
//				try 
//				{			
//					Collection<String> excludes = new ArrayList<String>();
//					UserGroup original = findUserGroupById(userGroup.getId());
//					excludes.add("createdOn");
//					excludes.add("createdBy");
//					excludes.add("updatedOn");
//					excludes.add("updatedBy");
//					userActivityService.generateHistoryActivity(excludes, original, userGroup, 
//							loginVO.getId(), loginVO.getUserCode(), loginVO.getUserName(),
//							WebConstants.ACT_MODULE_USER_GROUP, 
//							WebConstants.ACT_TYPE_UPDATE, WebConstants.ACT_TABLE_USER_GROUP, loginVO.getId(), loginVO.getIpAddress());
//				} 
//				catch (Exception e) 
//				{
//					LOGGER.warn("Unable to Create History Activity: " + e.getMessage());
//				}
//				/** SET TO USER ACTIVITY **/
//				wrv.setRc(WebConstants.RESULT_SUCCESS);
//				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
//						new String[] {messageService.getMessageFor("l.userGroup", language),
//						messageService.getMessageFor("l.updated", language)}, language));
//				wrv.setType(WebConstants.TYPE_UPDATE);
//				wrv.setPath(WebConstants.PATH_UPDATE_USER_GROUP);
//				return wrv;
//			}catch(Exception e){
//				LOGGER.warn("Gagal update user group :" + userGroup, e);
//				throw new SambaWebException(e);
//			}
//		}
	}
	
	public List<UserGroup> findUserGroupAll() {
		List<UserGroup> listUser = userGroupMapper.findAllUserGroup();
		if (listUser == null)
			listUser = new ArrayList<>();
		return listUser;
	}

	public List<UserGroup> findUserGroupByParam(UserGroupParamVO paramVO) {
		try {
			List<UserGroup> listUser = userGroupMapper.findUserGroupByParam(paramVO);
			return listUser;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return null;
		}
	}

	public int countUserGroupByParam(UserGroupParamVO paramVO) {
		try {
			int count = userGroupMapper.countUserGroupByParam(paramVO);
			return count;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return 0;
		}
	}

	public UserGroup findUserGroupById(int groupId) {
		try {
			UserGroup user = userGroupMapper.findUserGroupById(groupId);
			return user;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return null;
		}
	}
	
	public List<UserGroup> findUserGroupForApproval(int userId) {
		List<UserGroup> listUserGroup = userGroupMapper.findUserGroupForApproval(userId);
		if (listUserGroup == null)
			listUserGroup = new ArrayList<>();
		return listUserGroup;
	}

}
