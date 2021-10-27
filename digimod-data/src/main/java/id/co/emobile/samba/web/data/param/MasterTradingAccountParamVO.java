package id.co.emobile.samba.web.data.param;

import org.apache.commons.lang3.StringUtils;

public class MasterTradingAccountParamVO extends ParamPagingVO {

	@Override
	protected String getPrimaryKey() {
		return "mta.id";
	}

	private String name;
	private String ibUserCode;

	public String getNameLike() {
		if (StringUtils.isNotBlank(name)) {
			return "%" + name.toUpperCase() + "%";
		}
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIbUserCode() {
		return ibUserCode;
	}

	public void setIbUserCode(String ibUserCode) {
		this.ibUserCode = ibUserCode;
	}
}
