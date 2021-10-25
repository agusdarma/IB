package id.co.emobile.samba.web.mapper;

import id.co.emobile.samba.web.data.UserLevelMenuVO;
import id.co.emobile.samba.web.data.param.UserLevelParamVO;
import id.co.emobile.samba.web.entity.UserLevel;
import id.co.emobile.samba.web.entity.UserLevelMenu;
import id.co.emobile.samba.web.entity.UserMenu;

import java.util.List;

public interface UserLevelMapper {

	public UserLevel findUserLevelById(int levelId);;
	public UserLevel findUserLevelByName(String levelName);

	public List<UserLevel> findUserLevelAll();
	public List<UserLevel> findUserLevelForTrxType();

	public List<UserLevelMenuVO> findModuleLeaf();
	//
	public void createUserLevel(UserLevel userLevel);
	public void updateUserLevel(UserLevel userLevel);
	public void deleteUserLevelMenu(int levelId);
	public void insertUserLevelMenu(UserLevelMenu userLevelMenu);

	public List<UserMenu> findUserMenuAll();

	public int countUserLevelValidate(String levelName);

	public void deleteLevelMenuById(int levelId);

	public void deleteUserLevelByLevelId(int levelId);

	public List<UserLevel> findUserLevelByParam(UserLevelParamVO paramVO);

	public int countUserLevelByParam(UserLevelParamVO paramVO);

	public UserLevel findLevelById(int levelId);

	public List<UserLevel> getAllUserLevel();

	public List<UserMenu> findUserModulesAll();

	public void createUserLevelData(UserLevel userLevel);

	public void updateUserLevelData(UserLevel userLevel);
	
	public List<UserLevelMenu> findUserLevelMenuByLevelId(int levelId);
}
