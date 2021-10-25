package id.co.emobile.samba.web.data.param;

public class UserLevelParamVO extends ParamPagingVO{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected String getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
    
    private String levelName; //Update 17 December 2014 by Grace

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
    
  
}
