package id.co.emobile.samba.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import id.co.emobile.samba.web.data.param.UserGroupParamVO;
import id.co.emobile.samba.web.entity.UserGroup;

public interface UserGroupMapper {
	public int createUserGroup(UserGroup userGroup);
	public int updateUserGroup(UserGroup userGroup);

	public UserGroup findUserGroupById(int id);
	public List<UserGroup> findAllUserGroup();
	public List<UserGroup> findUserGroupByParam(UserGroupParamVO paramVO);
	public int countUserGroupByParam(UserGroupParamVO paramVO);
	
	public List<UserGroup> findUserGroupForApproval(int userId);
	public int deleteGroupFor(int userId);
	public int createGroupFor(@Param("userId") int userId, @Param("groupId") int groupId);
	
}
