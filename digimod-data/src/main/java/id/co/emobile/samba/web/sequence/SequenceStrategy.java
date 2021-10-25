package id.co.emobile.samba.web.sequence;

import java.util.Date;

public interface SequenceStrategy {
	// int max: (2^31 - 1) ~ 2.000.000.000
	public String formatSequence(int id, Date period);
	public int getLimit();
	public boolean needResetSequence(Date period);
}
