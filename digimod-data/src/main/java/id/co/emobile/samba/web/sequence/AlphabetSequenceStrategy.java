package id.co.emobile.samba.web.sequence;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import id.co.emobile.samba.web.service.AppsTimeService;

public class AlphabetSequenceStrategy implements SequenceStrategy {
	
	@Autowired
	private AppsTimeService timeService;
	
	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final int ALPHABET_LENGTH = 26;
	private String prefix = "X";
	private final int limit = ALPHABET_LENGTH * ALPHABET_LENGTH;
	
	
	/*
	 * return prefix with 2 char of Alphabet, eg: XAA, XDG, etc
	 * id start from 1
	 * @see com.emobile.jets.mayapada.lib.sequence.SequenceStrategy#formatSequence(int, java.util.Date)
	 */
	@Override
	public String formatSequence(int id, Date period) {
		id--;
		int idx1 = id / ALPHABET_LENGTH;
		int idx2 = id % ALPHABET_LENGTH;
		StringBuffer sb = new StringBuffer();
		sb.append(prefix);
		sb.append(ALPHABET.substring(idx1, idx1 + 1));
		sb.append(ALPHABET.substring(idx2, idx2 + 1));
		return sb.toString();
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public int getLimit() {
		return limit;
	}

	@Override
	public boolean needResetSequence(Date period) {
		if (period == null) return true;
		return !timeService.isToday(period);
	}

}
