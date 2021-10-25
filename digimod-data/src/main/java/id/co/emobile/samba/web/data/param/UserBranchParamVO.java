package id.co.emobile.samba.web.data.param;

import org.apache.commons.lang3.StringUtils;

public class UserBranchParamVO extends ParamPagingVO{

	@Override
	protected String getPrimaryKey() {
		return "ub.id";
	}
	private String branchName;

	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBranchNameLike() {
		if(StringUtils.isNotBlank(branchName))
		{
			return "%"+branchName.toUpperCase()+"%";
		}
		return branchName;
	}
}
