package id.co.emobile.samba.web.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class MyFxBookLoginResponseVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private boolean error;
	private String message;
	private String session;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

}
