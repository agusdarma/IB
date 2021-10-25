package id.co.emobile.samba.web.sequence;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import id.co.emobile.samba.web.service.AppsTimeService;
import id.co.emobile.samba.web.utils.CommonUtil;

public class DigitsSequenceStrategy implements SequenceStrategy {
	
	private NumberFormat nf = new DecimalFormat("###0");
	
	@Autowired
	private AppsTimeService timeService;
	
	private String prefix = "";
	
	private int limit;
	private int digitLength;
	
	@Override
	public String formatSequence(int id, Date period) {
		return prefix + CommonUtil.zeroPadLeft(nf.format(id), digitLength);
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public boolean needResetSequence(Date period) {
		if (period == null) return true;
		return !timeService.isToday(period);
	}

	public void setLimit(int limit) {
		this.limit = limit;
		this.digitLength = nf.format(limit).length();
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}