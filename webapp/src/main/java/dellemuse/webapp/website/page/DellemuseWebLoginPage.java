package dellemuse.webapp.website.page;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;
import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;

import dellemuse.model.logging.Logger;



@WicketSignInPage
@MountPath("login")
public class DellemuseWebLoginPage extends WebPage {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(DellemuseWebLoginPage.class.getName());
	
	public DellemuseWebLoginPage(PageParameters parameters) {
		super(parameters);

		if (((AbstractAuthenticatedWebSession) getSession()).isSignedIn()) {
			continueToOriginalDestination();
		}
		add(new LoginForm("loginForm"));
	}

	
	private class LoginForm extends StatelessForm<LoginForm> {

		private static final long serialVersionUID = 1L;
		
		private String username;
		private String password;

		public LoginForm(String id) {
			super(id);
			setModel(new CompoundPropertyModel<>(this));
			add(new FeedbackPanel("feedback"));
			add(new RequiredTextField<String>("username"));
			add(new PasswordTextField("password"));
		}

		@Override
		protected void onSubmit() {
			AuthenticatedWebSession session = AuthenticatedWebSession.get();
			if (session.signIn(username, password)) {
				setResponsePage(DellemuseWebLoginPage.class);
			} else {
				error("Login failed");
			}
		}
	}
	 

}
