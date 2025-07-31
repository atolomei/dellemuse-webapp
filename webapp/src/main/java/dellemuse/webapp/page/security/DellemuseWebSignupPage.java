package dellemuse.webapp.page.security;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.GuideContentModel;
import dellemuse.webapp.branded.global.BrandedGlobalTopPanel;
import dellemuse.webapp.global.GlobalFooterPanel;
import dellemuse.webapp.global.PageHeaderPanel;
import dellemuse.webapp.page.BasePage;
import io.wktui.form.Form;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.TextField;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;

@MountPath("signup")
public class DellemuseWebSignupPage extends BasePage {

    private static final long serialVersionUID = 1L;

    private IModel<String> firstName 		=  new Model<String>();
    private IModel<String> lastName  		=  new Model<String>();
    private IModel<String> email     		=  new Model<String>();
    private IModel<String> phone      		=  new Model<String>();
    private IModel<String> password       	=  new Model<String>();
    private IModel<String> retypePassword 	=  new Model<String>();

    
    public DellemuseWebSignupPage() {
        this(null);
    }
    
    public DellemuseWebSignupPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

		BreadCrumb<GuideContentModel> bc = new BreadCrumb<>("breadcrumb", null);
		bc.addElement(new HREFBCElement("/home", getLabel("home")));
		PageHeaderPanel<GuideContentModel> ph = new PageHeaderPanel<>("page-header");
		ph.setBreadCrumb(bc);
		addOrReplace(ph);

		
        add(new BrandedGlobalTopPanel("top-panel"));
        add(new GlobalFooterPanel<>("footer-panel"));

        Form<Void> form = new Form<Void>("signupForm");
        add(form);

        TextField<String> nameField         		= new TextField<String>("firstName", firstName, getLabel("firstName"));
        TextField<String> lastnameField     		= new TextField<String>("lastName", lastName, getLabel("lastName"));
        TextField<String> emailField        		= new TextField<String>("email", email , getLabel("email"));
        TextField<String> phoneField        		= new TextField<String>("phone", phone, getLabel("phone") );
        TextField<String> passwordField 		    = new TextField<String>("password", password , getLabel("password"));
        TextField<String> retypePasswordField   	= new TextField<String>("retypePassword", retypePassword , getLabel("retypePassword"));
       
        form.add(nameField);
        form.add(lastnameField);
        form.add(emailField);
        form.add(phoneField);
        form.add(passwordField);
        form.add(retypePasswordField);
        

        SubmitButton<Void> button = new SubmitButton<Void>("button", form) {
			private static final long serialVersionUID = 1L;
			protected void onSubmit(AjaxRequestTarget target) {
				DellemuseWebSignupPage.this.onSubmit(target);	
			}
        };
        form.add(button);
  
    }
  
    protected void onSubmit(AjaxRequestTarget target) {
    	
    	
    }
    
    
}

