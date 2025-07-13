package dellemuse.webapp.website.page;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.webapp.component.global.GlobalFooterPanel;
import dellemuse.webapp.component.global.GlobalTopPanel;
import io.wktui.form.Form;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.TextField;

@MountPath("signup")
public class DellemuseWebSignupPage extends BasePage {

    private static final long serialVersionUID = 1L;

    private IModel<String> firstName;
    private IModel<String> lastName;
    private IModel<String> email;
    private IModel<String> phone;
    private IModel<String> password;

    
    public DellemuseWebSignupPage() {
        this(null);
    }
    
    public DellemuseWebSignupPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        add(new GlobalTopPanel<>("top-panel"));
        add(new GlobalFooterPanel<>("footer-panel"));

        Form<Void> form = new Form<Void>("signupForm");
        add(form);

        TextField<String> nameField         = new TextField<String>("firstName", firstName, getLabel("firstName"));
        TextField<String> lastnameField     = new TextField<String>("lastName", lastName, getLabel("lastName"));
        TextField<String> emailField        = new TextField<String>("email", email , getLabel("email"));
        TextField<String> passwordField     = new TextField<String>("password", password , getLabel("password"));
        TextField<String> phoneField        = new TextField<String>("phone", phone, getLabel("phone") );

        form.add(nameField);
        form.add(lastnameField);
        form.add(emailField);
        form.add(phoneField);
        form.add(passwordField);
        
        EditButtons<Void> buttons = new EditButtons<Void>("buttons");
        add( buttons);
                

    }
    
    
    
    
}
