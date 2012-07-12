package helper.hermes;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class ExclusionHandler {

	private static Object[] exclusionRegexes;

	private static Pattern DO_NOT_EMAIL_PATTERN = null;
	
	static {
		List<String> exclusions = new ArrayList<String>();
		
//		Hashtable table = (Hashtable) Play.configuration.clone();
//		Set<Object> propKeys = Play.configuration.keySet();
//		for(Object key: propKeys) {
//			if(!((String) key).startsWith("errorMailer.exclusion.")) {
//				table.remove(key);
//			}
//		}
//		for(Object key: propKeys) {
//			exclusions.add((String) table.get(key));
//		}
		
		exclusionRegexes = exclusions.toArray();
		if(exclusionRegexes.length == 0) {
			exclusionRegexes = new Object[]{".*"};
		}
		DO_NOT_EMAIL_PATTERN = ExclusionHandler.matchAny(exclusionRegexes,
				Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
	}


	private ExclusionHandler() {
	}


	protected static Pattern matchAny(Object[] patterns) {
		return matchAny(patterns, 0);
	}


	protected static Pattern matchAny(Object[] patterns, int flags) {
		String uber = StringUtils.join(patterns, ")|(?:");
		if (patterns.length > 1) {
			uber = "(?:" + uber + ")";
		}
		return Pattern.compile(uber, flags);
	}
	
	
	public static boolean isExcluded(String checkThis) {
		return DO_NOT_EMAIL_PATTERN.matcher(checkThis).find();
	}


	public static void main(String[] args) {
		assertTrue(isExcluded(""));
		
		
		Pattern pattern = ExclusionHandler.matchAny(new String[] { "one" });
		assertTrue(pattern.matcher("zzz one zzz").find());
		assertFalse(pattern.matcher("zzz two zzz").find());

		pattern = ExclusionHandler.matchAny(new String[] { "79", "two" });
		assertTrue(pattern.matcher("zzz one zzz").find());
		assertTrue(pattern.matcher("zzz two zzz").find());
		assertFalse(pattern.matcher("zzz three zzz").find());
		System.out.println("this");
		assertTrue(pattern.matcher("dfhjdsfghf79hs9e74t6hp4oiu6h5io6hfdn").find());
//		for(Object str: exclusionRegexes) {
//			assertTrue(DO_NOT_EMAIL_PATTERN.matcher((String) str).find());
//		}
//		assertTrue(DO_NOT_EMAIL_PATTERN.matcher("exception").find());
	}


	public static void assertTrue(boolean bool) {
		if (bool) {
			System.out.println("ok");
		} else {
			System.out.println("wrong");
		}
	}


	public static void assertFalse(boolean bool) {
		assertTrue(!bool);
	}

}
