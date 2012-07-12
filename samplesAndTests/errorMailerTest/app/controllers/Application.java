package controllers;

import play.mvc.Controller;
import play.mvc.With;
import controllers.hermes.ErrorMailer;

@With(ErrorMailer.class)
public class Application extends Controller {

	public static void index() {
		Object nullObject = null;
		render(nullObject);
	}

}