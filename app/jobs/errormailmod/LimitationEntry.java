package jobs.errormailmod;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to check if the number of maximum sended similar emails in a time period
 * is exceeded.
 */
public class LimitationEntry {

	static Map<String, List<LimitationEntry>> entries = new HashMap<String, List<LimitationEntry>>();

	Calendar date = null;


	private LimitationEntry(Calendar now) {
		this.date = now;
	}


	public static boolean limitNotExceeded(String action, String path, Throwable exception,
			int minutes, int limitCount) {
		String key = action + path + exception.getClass().getName();
		List<LimitationEntry> entrylist = entries.get(key);
		if (entrylist == null) {
			entrylist = new ArrayList<LimitationEntry>();
		}

		Calendar now = GregorianCalendar.getInstance();

		// delete old entries
		for (LimitationEntry entry : entrylist) {
			long diff = entry.date.getTimeInMillis()
					- (now.getTimeInMillis() - 60 * 1000 * minutes);
			if (diff < 0) {
				entrylist.remove(entry);
			}
		}

		// check if limit is exceeded
		entries.put(key, entrylist);
		if (entrylist.size() <= limitCount + 1) {
			entrylist.add(new LimitationEntry(now));
			return true;
		}
		return false;
	}

}
