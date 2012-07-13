package helper.hermes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import play.Play;

/**
 * Checks a String against regexes defined in application.conf thanks to
 * ripper234 and his co-workers
 */
public class ExclusionHandler {

	private static Object[] exclusionRegexes;

	private static Pattern DO_NOT_EMAIL_PATTERN = null;

	// isExcluded() will return true if this is true
	private static boolean exclusionsEnabled = "true".equalsIgnoreCase(Play.configuration
			.getProperty("errorMailer.exclusionsenabled"));

	static {
		List<String> exclusions = new ArrayList<String>();
		// try to read exclusionpatterns from the application.conf
		Set<Object> propKeys = Play.configuration.keySet();
		for (Object key : propKeys) {
			if (((String) key).startsWith("errorMailer.exclusion.")) {
				exclusions.add((String) Play.configuration.get(key));
			}
		}
		exclusionRegexes = exclusions.toArray();
		if (exclusionsEnabled && exclusionRegexes.length == 0) {
			throw new RuntimeException(
					"no exclusionregexes defined in application.conf, but exclusions are enabled.");
		}
		// build pattern
		DO_NOT_EMAIL_PATTERN = ExclusionHandler.matchAny(exclusionRegexes, Pattern.DOTALL
				| Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
	}


	private ExclusionHandler() {
	}


	// Creates a combined 'or' pattern with flag 0
	public static Pattern matchAny(Object[] patterns) {
		return matchAny(patterns, 0);
	}


	// Creates a combined 'or' pattern with given flags
	public static Pattern matchAny(Object[] patterns, int flags) {
		String uber = StringUtils.join(patterns, ")|(?:");
		if (patterns.length > 1) {
			uber = "(?:" + uber + ")";
		}
		return Pattern.compile(uber, flags);
	}


	// Checks if this email shall not be sent.
	public static boolean isExcluded(String checkThis) {
		if (!exclusionsEnabled) {
			return false;
		}
		return DO_NOT_EMAIL_PATTERN.matcher(checkThis).find();
	}

}
