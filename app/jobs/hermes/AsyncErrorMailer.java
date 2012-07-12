package jobs.hermes;

import notifier.hermes.ErrorMailSender;
import play.Play;
import play.jobs.Job;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Scope.Params;
import play.mvc.Scope.RenderArgs;
import play.mvc.Scope.Session;

/**
 * The job that is started everytime an errormail has to be sent.
 */
public class AsyncErrorMailer extends Job {

	static int limitCount = 0;

	static int minutes = 0;

	static boolean limitsenabled = false;

	static {
		if ("true".equalsIgnoreCase(Play.configuration.getProperty("errorMailer.limit.enabled"))) {
			Integer tmp = Integer.parseInt(Play.configuration
					.getProperty("errorMailer.limit.minutes"));
			if (tmp == null || tmp <= 0) {
				throw new RuntimeException(
						"configuration value for key 'errorMailer.limit.minutes' is incorrect");
			}
			minutes = tmp;
			tmp = Integer.parseInt(Play.configuration.getProperty("errorMailer.limit.count"));
			if (tmp == null || tmp <= 0) {
				throw new RuntimeException(
						"configuration value for key 'errorMailer.limit.count' is incorrect");
			}
			limitCount = tmp;
		} else {
			limitsenabled = false;
		}
	}

	/* 
	 * needed to have this variables in an async job. Request.current() for
	 * example wouldn't return any Request
	 */
	Request request;

	Params params;

	Throwable exception;

	RenderArgs renderArgs;

	Response response;

	Session session;


	public AsyncErrorMailer(Request request, Params params, Throwable exception,
			RenderArgs renderArgs, Response response, Session session) {
		this.request = request;
		this.params = params;
		this.exception = exception;
		this.renderArgs = renderArgs;
		this.response = response;
		this.session = session;
	}


	public void doJob() {
		// TODO: check exclusions

		// check limitations
		if (limitsenabled) {
			if (LimitationEntry.limitNotExceeded(request.action, request.path, exception, minutes,
					limitCount)) {
				ErrorMailSender.sendErrorMail(exception, request, params, renderArgs, response,
						session);
			}
		} else {
			ErrorMailSender
					.sendErrorMail(exception, request, params, renderArgs, response, session);
		}
	}

}
