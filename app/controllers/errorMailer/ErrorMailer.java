package controllers.errorMailer;

import jobs.errorMailer.AsyncErrorMailer;
import notifier.errorMailer.ErrorMailSender;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Scope.Session;

public class ErrorMailer extends Controller {

	@Catch(Exception.class)
	public static void sendErrorMail(Throwable t) {
		new AsyncErrorMailer(request, params, t, renderArgs, response, Session.current()).now();
	}

}
