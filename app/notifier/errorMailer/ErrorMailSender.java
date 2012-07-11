package notifier.errorMailer;

import java.util.ArrayList;
import java.util.List;

import play.Play;
import play.Play.Mode;
import play.i18n.Messages;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Mailer;
import play.mvc.Scope.Params;
import play.mvc.Scope.RenderArgs;
import play.mvc.Scope.Session;

public class ErrorMailSender extends Mailer {

	private static boolean sendOnProd = true;
	private static boolean sendOnDev = false;
	private static String from;

	private static String[] to = null;

	static {
		String prop = Play.configuration.getProperty("errormailer.sendondev");
		if (prop.equalsIgnoreCase("true")) {
			sendOnDev = true;
		}
		prop = Play.configuration.getProperty("errormailer.sendonprod");
		if (prop.equalsIgnoreCase("false")) {
			sendOnProd = false;
		}
		from = Play.configuration.getProperty("errormailer.from");
		prop = Play.configuration.getProperty("errormailer.to");
		to = prop.split(",");
		for (String addr : to) {
			addr = addr.trim();
		}
	}

	/**
	 * sends an errormail with information to the given exception
	 * 
	 * @param exception
	 * @param response2
	 * @param renderArgs2
	 * @param params2
	 * @param request
	 */
	public static void sendErrorMail(final Throwable exception,
			Request request, Params params, RenderArgs renderArgs,
			Response response, Session session) {
		if (Play.mode == Mode.DEV && sendOnDev == false) {
			return;
		}
		if (Play.mode == Mode.PROD && sendOnProd == false) {
			return;
		}
		setFrom(from);
		for (String addr : to) {
			addRecipient(addr);
		}
		String errorInApp = Messages.get("%serrorIn%s", exception.getClass()
				.getSimpleName(), Play.configuration
				.getProperty("application.name"));
		if (request != null) {
			errorInApp += " on server " + request.host;
		}
		setSubject(errorInApp);
		List<String> lines = null;
		if (exception != null) {
			lines = getThrowableMessage(exception);
		}
		send(request, params, renderArgs, response, exception, lines,
				errorInApp, session);
	}

	/**
	 * goes recursively through the throwable over getCause() and returns a
	 * List<Throwable.getStackTrace> with html tags
	 * 
	 * @param t
	 * @return List<throwable.getMessage()> with html tags
	 */
	public static List<String> getThrowableMessage(final Throwable t) {
		List<String> lines = new ArrayList<String>();
		Throwable cause = t;
		lines.add(cause.getMessage() + "<br/>\n");
		do {
			lines.add(getStackTrace(cause));
			cause = cause.getCause();
		} while (cause != null);
		return lines;
	}

	/**
	 * helperfunction for getThrowableMessage(final Throable t);
	 * 
	 * @param t
	 * @return
	 */
	public static String getStackTrace(final Throwable t) {
		boolean paginate = true;
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement elem : t.getStackTrace()) {
			sb.append("<div class=\"");
			if (paginate) {
				sb.append("odd");
			} else {
				sb.append("even");
			}
			paginate = !paginate;
			sb.append("\">")
					.append(elem.toString().replaceAll("\\.", ".<wbr/>"))
					.append("</div>\n");
		}
		return sb.append("<hr/>").toString();
	}
}