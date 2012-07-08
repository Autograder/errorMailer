package controllers.errorMailer;

import notifier.errorMailer.ErrorMailSender;
import play.mvc.Catch;
import play.mvc.Controller;

public class ErrorMailer extends Controller {

	@Catch(Exception.class)
	public static void sendErrorMail(Throwable t) {
		ErrorMailSender.sendErrorMail(t);
		//Logger.error("ErrorMailer recieved: %s", Arrays.toString(t.getStackTrace()));
	}
		
}
