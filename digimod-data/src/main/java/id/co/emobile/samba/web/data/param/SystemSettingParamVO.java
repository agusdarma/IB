package id.co.emobile.samba.web.data.param;

public class SystemSettingParamVO extends ParamPagingVO{
	
	@Override
	protected String getPrimaryKey() {
		return "updatedOn";
	}
	
//	@Override
//	protected String getAliasTable() {
//		return "";
//	}
}
