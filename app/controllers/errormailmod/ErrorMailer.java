package controllers.errormailmod;

import jobs.errormailmod.AsyncErrorMailer;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Scope.Session;

public class ErrorMailer extends Controller {

	@Catch(Exception.class)
	public static void sendErrorMail(Throwable t) {
		new AsyncErrorMailer(request, params, t, renderArgs, response, Session.current()).now();
	}

}
