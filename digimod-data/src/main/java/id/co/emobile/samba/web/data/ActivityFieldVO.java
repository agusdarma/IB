package id.co.emobile.samba.web.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ActivityFieldVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String action;
	private final String moduleName;
	private final String description;
	private final String targetTable;
	private final String targetId;
	
	public ActivityFieldVO(String action, String moduleName, String description, String targetTable, String targetId) {
		super();
		this.action = action;
		this.moduleName = moduleName;
		this.description = description;
		this.targetTable = targetTable;
		this.targetId = targetId;
	}

	public String getAction() {
		return action;
	}

	public String getModuleName() {
		return moduleName;
	}

	public String getDescription() {
		return description;
	}

	public String getTargetTable() {
		return targetTable;
	}

	public String getTargetId() {
		return targetId;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
