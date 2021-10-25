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

import id.co.emobile.samba.web.data.ModulesAccessVO;
import id.co.emobile.samba.web.data.UserDataLoginVO;
import id.co.emobile.samba.web.data.UserLevelMenuVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.WebResultVO;
import id.co.emobile.samba.web.data.param.UserLevelParamVO;
import id.co.emobile.samba.web.entity.UserLevel;
import id.co.emobile.samba.web.entity.UserLevelMenu;
import id.co.emobile.samba.web.entity.UserMenu;
import id.co.emobile.samba.web.mapper.UserLevelMapper;

@Service
public class UserLevelService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserLevelService.class);

	@Autowired
	private UserLevelMapper userLevelMapper;

	@Autowired
	private BizMessageService messageService;
	
	@Autowired
	private UserActivityService userActivityService;

	public List<UserLevel> findAllUserLevel()
	{
		List<UserLevel> listUserLevel = userLevelMapper.findUserLevelAll();
		return listUserLevel;
	}

	public List<UserLevel> findUserLevelForInsertTrxType()
	{
		List<UserLevel> listUserLevel = userLevelMapper.findUserLevelForTrxType();
		if(listUserLevel==null)
		{
			listUserLevel = new ArrayList<UserLevel>();
		}
		return listUserLevel;
	}


	public List<UserLevel> findUserLevelByParam(UserLevelParamVO paramVO){
		try {
			List<UserLevel> listUserLevel = userLevelMapper.findUserLevelByParam(paramVO);
			return listUserLevel;
		} catch (Exception e) {
			LOGGER.warn("findUserLevelByParam Exception: " + e, e);
			return null;
		}
	}

	public Integer countUserLevelByLevelParam(UserLevelParamVO paramVO){
		try {
			int count = userLevelMapper.countUserLevelByParam(paramVO);
			return count;
		} catch (Exception e) {
			LOGGER.warn("countUserLevelByLevelParam Exception: " + e, e);
			return 0;
		}
	}

	public Integer countUserLevelValidate(String levelName){
		try {
			int count2 = userLevelMapper.countUserLevelValidate(levelName);
			return count2;
		} catch (Exception e) {
			LOGGER.warn("countUserLevelValidate Exception: " + e, e);
			return 0;
		}
	}

	public List<UserLevelMenuVO> findModules() {
		List<UserLevelMenuVO> vos = userLevelMapper.findModuleLeaf();
		return vos;
	}

	public UserLevel findLevelById(int levelId){
		try {
			UserLevel userLevel = userLevelMapper.findLevelById(levelId);
			return userLevel;
		} catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			return null;
		}
	}

	public void deleteByLevelId(int levelId){
		userLevelMapper.deleteLevelMenuById(levelId);
		userLevelMapper.deleteUserLevelByLevelId(levelId);
	}

	@Transactional(rollbackFor=Exception.class)
	public WebResultVO saveLevelData(UserLevel userLevel,List<ModulesAccessVO> modulesAccess, UserDataLoginVO loginVO, Locale language) throws SambaWebException {
		WebResultVO wrv = new WebResultVO();
		Date now = new Date();
		userLevel.setCreatedBy(loginVO.getId());
		userLevel.setCreatedOn(now);
		userLevel.setUpdatedBy(loginVO.getId());
		userLevel.setUpdatedOn(now);

		if(StringUtils.isEmpty(userLevel.getLevelName())) {
			LOGGER.warn("Level name is empty!");
			throw new SambaWebException(SambaWebException.NE_MISSING_INPUT,new String[] {messageService.getMessageFor("l.levelName", language)});
		}

		List<ModulesAccessVO> filteredModulesAccess = new ArrayList<ModulesAccessVO>();
		for (ModulesAccessVO modulesAccessVO : modulesAccess) {
			if(modulesAccessVO!=null)
			{
				if(modulesAccessVO.getModulesSelected()> 0 || modulesAccessVO.getAccessLevel() > 0)
				{
					if(modulesAccessVO.getModulesSelected()==0)
					{
						String access = String.valueOf(modulesAccessVO.getAccessLevel());
						access = access.substring(0, 3);
						modulesAccessVO.setModulesSelected(Integer.parseInt(access));
						modulesAccessVO.setAccessLevel(1);
					}
					if(modulesAccessVO.getAccessLevel()>0)
						modulesAccessVO.setAccessLevel(1);
					filteredModulesAccess.add(modulesAccessVO);				
				}
			}
		}
		
		if(filteredModulesAccess.size()==0) {
			LOGGER.warn("Must at least choose 1 menu!");
			throw new SambaWebException(SambaWebException.NE_MUST_CHOOSE_MENU);
		}

		// create group module
		List<UserMenu> modules = constructModules(filteredModulesAccess);
		userLevel.setListMenu(modules);

		try {
			if(userLevel.getLevelId()==0)
			{
				int countUser=countUserLevelValidate(userLevel.getLevelName());
				if(countUser!=0)
				{
					LOGGER.warn("level name already used ! levelName : {}", userLevel.getLevelName());
					throw new SambaWebException(SambaWebException.NE_DUPLICATE_DATA,new String[] {messageService.getMessageFor("l.levelName")});
				}
				userLevelMapper.createUserLevelData(userLevel);
				
				String desc = "Create UserLevel " + userLevel.getLevelName();
				userActivityService.createUserActivityCreateData(loginVO, 
						UserActivityService.MODULE_MANAGE_USER_LEVEL, desc, UserActivityService.TABLE_USER_LEVEL, 
						userLevel.getLevelId(), userLevel);
				
				wrv.setRc(WebConstants.RESULT_SUCCESS);
				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
						new String[]{messageService.getMessageFor("l.userLevel", language),
						messageService.getMessageFor("l.created", language)}, language));
				wrv.setType(WebConstants.TYPE_INSERT);
				
			}
			else
			{
				UserLevel oriLevel=findLevelById(userLevel.getLevelId());
				if(!userLevel.getLevelName().equals(oriLevel.getLevelName()))
				{
					int countUser=countUserLevelValidate(userLevel.getLevelName());
					if(countUser!=0)
					{
						LOGGER.warn("level name already used!");
						throw new SambaWebException(SambaWebException.NE_DUPLICATE_DATA,new String[] {messageService.getMessageFor("l.levelName", language)});
					}
				}
				userLevelMapper.updateUserLevelData(userLevel);
				
				String desc = "Update UserLevel " + userLevel.getLevelName();
				userActivityService.createUserActivityUpdateData(loginVO, 
						UserActivityService.MODULE_MANAGE_USER_LEVEL, desc, UserActivityService.TABLE_USER_LEVEL, 
						userLevel.getLevelId(), userLevel, oriLevel);
				
				wrv.setRc(WebConstants.RESULT_SUCCESS);
				wrv.setMessage(messageService.getMessageFor("rm.generalMessage",
						new String[]{messageService.getMessageFor("l.userLevel", language),
						messageService.getMessageFor("l.updated", language)}, language));
				wrv.setType(WebConstants.TYPE_UPDATE);
				wrv.setPath(WebConstants.PATH_UPDATE_USER_LEVEL);

			}
			userLevelMapper.deleteUserLevelMenu(userLevel.getLevelId());
			// save group modules
			for (UserMenu module: modules)
			{
				UserLevelMenu userLevelMenu = new UserLevelMenu();
				userLevelMenu.setLevelId(userLevel.getLevelId());
				userLevelMenu.setMenuId(module.getMenuId());
				userLevelMenu.setAccessLevel(module.getAccessLevel());
				userLevelMapper.insertUserLevelMenu(userLevelMenu);				
			}
		}	catch (SambaWebException mwe) {
			LOGGER.warn("error when saving user level {} with errorCode {}", userLevel, mwe.getErrorCode());
			throw mwe;
		}	catch (Exception e) {
			LOGGER.warn("Exception: " + e, e);
			throw new SambaWebException(e);
		}
		return wrv;
	}

	private List<UserMenu> constructModules(List<ModulesAccessVO> modulesAccess) {
		List<UserMenu> modules = new ArrayList<UserMenu>();

		// GET ALL MODULES
		List<UserMenu> allModules = userLevelMapper.findUserModulesAll();

		// ADD MODULES
		for (UserMenu m : allModules) {
			if (m.getAlwaysInclude() == WebConstants.DB_TRUE){
				modules.add(m);
			} else {
				for (ModulesAccessVO ma : modulesAccess){
					if (ma.getModulesSelected() == m.getMenuId()){
						m.setAccessLevel(ma.getAccessLevel());
						modules.add(m);
						break;
					}
				}
			}
		}

		// ADD PARENTS
		UserMenu[] leafModules = modules.toArray(new UserMenu[0]);
		for (UserMenu m : leafModules) {
			int parentId = m.getParentId();
			while (parentId > 0) {
				// check if parentId already in modules
				if (getModule(modules, parentId) != null)
					break;

				UserMenu parentModule = getModule(allModules, parentId);
				if (parentModule == null)
					break;
				modules.add(parentModule);
				parentId = parentModule.getParentId();
			} // end while parent exists
		} // end for -> looping modules

		return modules;
	}

	private UserMenu getModule(List<UserMenu> modules, int moduleId) {
		for (UserMenu m : modules) {
			if (m.getMenuId() == moduleId)
				return m;
		}
		return null;
	}

	public List<UserLevel> getAllUserLevel() {
		List<UserLevel> allUserLevel = userLevelMapper.getAllUserLevel();
		return allUserLevel;
	}
	

}
