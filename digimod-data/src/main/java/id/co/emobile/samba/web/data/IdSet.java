package id.co.emobile.samba.web.data;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class IdSet implements Serializable{
	private static final long serialVersionUID = 1L;

	private final int min;
	private final int max;
	private final Date period;

	public IdSet(int min, int max, Date period) {
		this.min = min;
		this.max = max;
		this.period = period;
	}
	
	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

	public Date getPeriod() {
		return period;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
}
