package id.co.emobile.samba.web.data;

import id.co.emobile.samba.web.entity.UserData;
import id.co.emobile.samba.web.entity.UserLevelMenu;
import id.co.emobile.samba.web.entity.UserPreference;

import java.util.List;

public class UserDataLoginVO extends UserData {
	private static final long serialVersionUID = 1L;
	
	private UserLevelVO levelVO;
	private UserPreference userPreference;
	private String ipAddress;
	private List<UserLevelMenu> listUserLevelMenu;

	public boolean isNeverLogin() {
		return getLastLoginOn() == null;
	}

	public UserLevelVO getLevelVO() {
		return levelVO;
	}
	public void setLevelVO(UserLevelVO levelVO) {
		this.levelVO = levelVO;
	}

	public UserPreference getUserPreference() {
		return userPreference;
	}

	public void setUserPreference(UserPreference userPreference) {
		this.userPreference = userPreference;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public List<UserLevelMenu> getListUserLevelMenu() {
		return listUserLevelMenu;
	}

	public void setListUserLevelMenu(List<UserLevelMenu> listUserLevelMenu) {
		this.listUserLevelMenu = listUserLevelMenu;
	}
}
