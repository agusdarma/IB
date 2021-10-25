package id.co.emobile.samba.web.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class UserMenu implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int menuId;  // menu_id INT NOT NULL,
	private int parentId;  // parent_id INT NOT NULL,
	private int menuLevel;  // menu_level INT NOT NULL,
	private int showOrder;  // show_order INT NOT NULL,
	private String menuIcon;  // menu_icon VARCHAR(32) NOT NULL,
	private String menuText;  //  menu_text VARCHAR(64) NOT NULL,
	private String menuUrl;  //  menu_url VARCHAR(64) NOT NULL,
	private String menuGroup;  //  menu_group VARCHAR(64) NOT NULL,
	private int alwaysInclude;  //  always_include INT NOT NULL,
	private int accessLevel;
	
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getMenuLevel() {
		return menuLevel;
	}
	public void setMenuLevel(int menuLevel) {
		this.menuLevel = menuLevel;
	}
	public int getShowOrder() {
		return showOrder;
	}
	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}
	public String getMenuIcon() {
		return menuIcon;
	}
	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}
	public String getMenuText() {
		return menuText;
	}
	public void setMenuText(String menuText) {
		this.menuText = menuText == null? null: menuText.trim();
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl == null? null: menuUrl.trim();
	}
	public String getMenuGroup() {
		return menuGroup;
	}
	public void setMenuGroup(String menuGroup) {
		this.menuGroup = menuGroup == null? null: menuGroup.trim();
	}
	public int getAlwaysInclude() {
		return alwaysInclude;
	}
	public void setAlwaysInclude(int alwaysInclude) {
		this.alwaysInclude = alwaysInclude;
	}
	
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	public int getAccessLevel() {
		return accessLevel;
	}
	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}

}
