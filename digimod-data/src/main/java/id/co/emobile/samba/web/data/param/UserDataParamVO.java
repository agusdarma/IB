package id.co.emobile.samba.web.data.param;

import java.util.Date;

public class UserDataParamVO extends ParamPagingVO{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected String getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private int id;  // id INT NOT NULL,
    private String userCode; // user_code VARCHAR(16) NOT NULL,
    private String userName;  // user_name VARCHAR(32) NOT NULL,
    private String userPassword;  // user_password VARCHAR(64) NOT NULL,
    private int invalidCount;  // invalid_count INT NOT NULL,
    private int userStatus;  // user_status INT NOT NULL,
    private int levelId;  // level_id INT NOT NULL,
    private Date lastLoginOn;  // last_login_on DATETIME,
    private Date passChangedOn;  // pass_changed_on DATETIME,
    private boolean hasChangePass;  // has_change_pass TINYINT NOT NULL,
    private String sessionId;   // session_id VARCHAR(32) NOT NULL,
    private Date createdOn;  // created_on DATETIME NOT NULL,
    private int createdBy;  // created_by INT NOT NULL,
    private Date updatedOn;  // updated_on DATETIME NOT NULL,
    private int updatedBy;  // updated_by INT NOT NULL,
      
    private String levelName; //Update 17 December 2014 by Grace
    private String levelDesc; //Update 17 December 2014 by Grace
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public int getInvalidCount() {
		return invalidCount;
	}
	public void setInvalidCount(int invalidCount) {
		this.invalidCount = invalidCount;
	}
	public int getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}
	public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}
	public Date getLastLoginOn() {
		return lastLoginOn;
	}
	public void setLastLoginOn(Date lastLoginOn) {
		this.lastLoginOn = lastLoginOn;
	}
	public Date getPassChangedOn() {
		return passChangedOn;
	}
	public void setPassChangedOn(Date passChangedOn) {
		this.passChangedOn = passChangedOn;
	}
	public boolean isHasChangePass() {
		return hasChangePass;
	}
	public void setHasChangePass(boolean hasChangePass) {
		this.hasChangePass = hasChangePass;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public String getLevelDesc() {
		return levelDesc;
	}
	public void setLevelDesc(String levelDesc) {
		this.levelDesc = levelDesc;
	}
	
}
