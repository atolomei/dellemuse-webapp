package dellemuse.webapp.page.security;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.webapp.branded.global.BrandedGlobalTopPanel;
import dellemuse.webapp.global.GlobalFooterPanel;
import dellemuse.webapp.page.BasePage;
import io.wktui.form.Form;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.TextField;
import wktui.base.InvisiblePanel;

//@WicketSignInPage
@MountPath("resetpassword")
public class DellemuseWebPasswordResetPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
    static private Logger logger = Logger.getLogger(DellemuseWebPasswordResetPage.class.getName());
	

	String username;
	String password;
    TextField<String> passwordField;
    
    Form<Void> form;
    
    
    public DellemuseWebPasswordResetPage() {
            this(null);
    }
    
	public DellemuseWebPasswordResetPage(PageParameters parameters) {
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

	        
	        if( isValidToken()) {
		        this.form = new Form<Void>("passwordResetForm");
		        add(this.form);
		        
		        this.passwordField = new TextField<String>("password", Model.of(password), getLabel("new-password"));
		        
		        this.form.add(this.passwordField);
		        
		        SubmitButton<Void> submit = new SubmitButton<Void>("submit") {
	                private static final long serialVersionUID = 1L;
	                @Override
	                protected void onSubmit(AjaxRequestTarget target) {
	                    DellemuseWebPasswordResetPage.this.onSubmit();
	                }
		        };
		        form.add(submit);
	        }
	        else {
	        
	        	add( new InvisiblePanel("passwordResetForm"));
	        }
	}
	   
    private boolean isValidToken() {
		return false;
	}

	protected void onSubmit() {
        
    	this.password=passwordField.getValue();
	    logger.debug(this.password);
	    logger.debug("done");
        logger.debug("");
    }
}
