package dellemuse.webapp.page.security;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.GuideContentModel;
import dellemuse.model.logging.Logger;
import dellemuse.webapp.branded.global.BrandedGlobalTopPanel;
import dellemuse.webapp.global.GlobalFooterPanel;
import dellemuse.webapp.global.PageHeaderPanel;
import dellemuse.webapp.page.BasePage;
import io.wktui.form.Form;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.TextField;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;

//@WicketSignInPage
@MountPath("signin")
public class DellemuseWebSigninPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
    static private Logger logger = Logger.getLogger(DellemuseWebSigninPage.class.getName());
	
	String email;
    String password;
    
    TextField<String> emailField;
    TextField<String> passwordField;
    
    Form<Void> form;
    
    //org.apache.wicket.markup.html.form.TextField<String> i_email;
    
    public DellemuseWebSigninPage() {
            this(null);
    }
    
	public DellemuseWebSigninPage(PageParameters parameters) {
		super(parameters);
		//
		// if (((AbstractAuthenticatedWebSession) getSession()).isSignedIn()) {
		//	 continueToOriginalDestination();
		// }
		//add(new LoginForm("loginForm"));
		//
	}

	@Override
	public void onInitialize() {
	        super.onInitialize();

	        add(new BrandedGlobalTopPanel("top-panel"));
	        add(new GlobalFooterPanel<>("footer-panel"));


			BreadCrumb<GuideContentModel> bc = new BreadCrumb<>("breadcrumb", null);
			bc.addElement(new HREFBCElement("/home", getLabel("home")));
			PageHeaderPanel<GuideContentModel> ph = new PageHeaderPanel<>("page-header");
			ph.setBreadCrumb(bc);
			addOrReplace(ph);

	        
	        
	        this.form = new Form<Void>("signinForm");
	        add(this.form);
	        
	        this.emailField = new TextField<String>("email", Model.of(email), getLabel("email"));
	        this.passwordField = new TextField<String>("password", Model.of( password), getLabel("password"));
	        
	        this.form.add(this.emailField);
	        this.form.add(this.passwordField);
	        
	        SubmitButton<Void> submit = new SubmitButton<Void>("submit") {
                private static final long serialVersionUID = 1L;
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    DellemuseWebSigninPage.this.onSubmit();
                }
	        };
	        form.add(submit);
	}
	   
	
    protected void onSubmit() {
        
        logger.debug("");
	    logger.debug("onSubmit");
	    logger.debug( form.isSubmitted());
	    logger.debug( emailField.getValue());
	    logger.debug( passwordField.getValue());
        logger.debug("done");
        logger.debug("");
        
	    /**
	    emailField.updateModel();
	    logger.debug(emailField.getModel().getObject().toString());
	    logger.debug(emailField.getValue().toString());
	    logger.debug(((org.apache.wicket.markup.html.form.TextField) emailField.getInput()).getValue());
	    logger.debug( (String) emailField.getDefaultModelObject());
        **/
    }

	protected boolean authenticate( String userName, String password) {
		return true;
	}
	


	/**
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
	 
	 */

}
