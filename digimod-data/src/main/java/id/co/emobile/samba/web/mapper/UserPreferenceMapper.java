package id.co.emobile.samba.web.mapper;

import id.co.emobile.samba.web.entity.UserPreference;

public interface UserPreferenceMapper 
{
	public UserPreference findUserPreferenceByID(int userID);
	public int insertUserPreference(UserPreference userPreference);
	public void updateUserPreferenceByID(UserPreference userPreference);
	public void deleteUserPreferenceByID(UserPreference userPreference);
}