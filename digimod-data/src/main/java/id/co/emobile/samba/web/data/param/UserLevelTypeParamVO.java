package id.co.emobile.samba.web.data.param;


public class UserLevelTypeParamVO extends ParamPagingVO{

	@Override
	protected String getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	private int levelId;
	private String levelName;
	private String levelDesc;
	
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}
	public String getLevelDesc() {
		return levelDesc;
	}
	public void setLevelDesc(String levelDesc) {
		this.levelDesc = levelDesc;
	}

}
