package id.co.emobile.samba.web.mapper;

import id.co.emobile.samba.web.data.param.UserDataParamVO;
import id.co.emobile.samba.web.entity.UserData;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserDataMapper {
	public int createUserData(UserData userData);
	public int updateUserData(UserData userData);
	
	public int updateResetPassword(UserData userData);
	
	public UserData findUserDataByUserCode(String userCode);
	public UserData findUserDataById(int userId);
	
	public int checkSessionLogin(@Param("userId") int userId, @Param("sessionId") String sessionId);
	
	public void clearSessionLogin(int userId);
	
	public void insertNewUser(UserData userData);

	public void updateNewUser(UserData userData);
	
	public int countLevelUsedByName(int LevelId);
	
	public int countUserValidate(String userID);
	
	public List<UserData> findUserByParam(UserDataParamVO addNewUserParamVO);
	
	public int countUserByParam(UserDataParamVO addNewUserParamVO);

	public UserData findUserById(int userId);
	
	public int updateLastAccess(UserData userData);
	
	public List<UserData> findUserDataActive();
	
	public List<UserData> findListUserIbActive();
	
	public List<UserData> findUserDataActiveByGroupAndLevelType(@Param("groupId") int groupId, @Param("levelType") int levelType);
	public List<UserData> findUserDataApprovalActiveForGroup(int groupId);
}
