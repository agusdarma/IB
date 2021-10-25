package id.co.emobile.samba.web.sequence;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePrefixSequenceStrategy extends DigitsSequenceStrategy {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
	
	private String prefix = "";
	
	@Override
	public String formatSequence(int id, Date period) {
		StringBuffer sb = new StringBuffer();
		sb.append(getDatePrefix(period));
		sb.append(prefix);
		sb.append(super.formatSequence(id, period));
		return sb.toString();
	}

	private String getDatePrefix(Date period) {
		return sdf.format(period);
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
