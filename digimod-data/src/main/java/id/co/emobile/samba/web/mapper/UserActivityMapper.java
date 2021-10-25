package id.co.emobile.samba.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import id.co.emobile.samba.web.data.param.UserActivityParamVO;
import id.co.emobile.samba.web.entity.UserActivity;

public interface UserActivityMapper {
	
	public int createUserActivity(UserActivity historyActivity);

	public List<UserActivity> findLastUserActivityForUser(@Param("userId") int userId, @Param("limit") int limit);
	
	public List<UserActivity> selectUserActivityByParam(UserActivityParamVO paramVO);
	
	public int countUserActivityByParam(UserActivityParamVO paramVO);
	
	public List<UserActivity> selectUserActivityByParamNoPaging(UserActivityParamVO paramVO);
	
}
