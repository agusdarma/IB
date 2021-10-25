package id.co.emobile.samba.web.mapper;

import java.util.List;

import id.co.emobile.samba.web.data.param.UserBranchParamVO;
import id.co.emobile.samba.web.entity.UserBranch;

public interface UserBranchMapper {
	public int createUserBranch(UserBranch userBranch);
	public int updateUserBranch(UserBranch userBranch);

	public UserBranch findUserBranchById(int id);
	public List<UserBranch> findAllUserBranch();
	public List<UserBranch> findUserBranchByParam(UserBranchParamVO paramVO);
	public int countUserBranchByParam(UserBranchParamVO paramVO);
	
	
	
}
