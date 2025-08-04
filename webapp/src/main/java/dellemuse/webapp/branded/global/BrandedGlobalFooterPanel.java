package dellemuse.webapp.branded.global;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.webapp.page.security.DellemuseWebSigninPage;
import dellemuse.webapp.page.security.DellemuseWebSignupPage;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavBar;
import wktui.base.BasePanel;
import wktui.base.LabelPanel;
import wktui.base.ModelPanel;


/**
 * 
 * 
 */
public class BrandedGlobalFooterPanel<T> extends ModelPanel<T> {
			
	private static final long serialVersionUID = 1L;

	public BrandedGlobalFooterPanel(String id) {
		this(id, null);
	}
	
	public BrandedGlobalFooterPanel(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		 
		        

		
		
		
		
	}	
		
}
