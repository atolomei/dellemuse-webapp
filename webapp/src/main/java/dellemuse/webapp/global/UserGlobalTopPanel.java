package dellemuse.webapp.global;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.UserModel;
import dellemuse.webapp.page.DellemuseWebHomePage;
import dellemuse.webapp.page.security.DellemuseWebSigninPage;
import dellemuse.webapp.page.security.DellemuseWebSignupPage;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavBar;
import io.wktui.nav.menu.NavDropDownMenu;
import wktui.base.LabelLinkPanel;
import wktui.base.ModelPanel;


public class UserGlobalTopPanel extends ModelPanel<UserModel> {

	private static final long serialVersionUID = 1L;

	public UserGlobalTopPanel(String id) {
		this(id, null);
	}
	
	public UserGlobalTopPanel(String id, IModel<UserModel> model) {
		super(id, model);
		super.setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
	}

	public void onDetach() {
		super.onDetach();
		
		if (getModel()!=null)
			getModel().detach();
	
	}

}
