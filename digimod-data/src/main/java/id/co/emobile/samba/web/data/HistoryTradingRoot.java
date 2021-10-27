package id.co.emobile.samba.web.data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoryTradingRoot {
	@JsonProperty("error")
	public boolean getError() {
		return this.error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	boolean error;

	@JsonProperty("message")
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	String message;

	@JsonProperty("history")
	public List<History> getHistory() {
		return this.history;
	}

	public void setHistory(List<History> history) {
		this.history = history;
	}

	List<History> history;
}
