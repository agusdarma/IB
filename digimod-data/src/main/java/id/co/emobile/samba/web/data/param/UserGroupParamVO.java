package id.co.emobile.samba.web.data.param;

import org.apache.commons.lang3.StringUtils;

public class UserGroupParamVO extends ParamPagingVO{

	@Override
	protected String getPrimaryKey() {
		return "ug.id";
	}
	private String groupName;

	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupNameLike() {
		if(StringUtils.isNotBlank(groupName))
		{
			return "%"+groupName.toUpperCase()+"%";
		}
		return groupName;
	}
}
