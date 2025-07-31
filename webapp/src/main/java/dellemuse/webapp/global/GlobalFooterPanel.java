package dellemuse.webapp.global;

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
public class GlobalFooterPanel<T> extends ModelPanel<T> {
			
	private static final long serialVersionUID = 1L;

	public GlobalFooterPanel(String id) {
		this(id, null);
	}
	
	public GlobalFooterPanel(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		NavBar<Void> nav = new NavBar<Void>("navbar");
		
		nav.addCollapse(new LinkMenuItem<Void>("item") {
							
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/home"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Home");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				});
		
		
		
		
		nav.addCollapse(new LinkMenuItem<Void>("item") {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(new RedirectPage("/contacto"));
			}

			@Override
			public IModel<String> getLabel() {
				return new Model<String>("Contact");
			}

			@Override
			public String getBeforeClick() {
				return null;
			}
		});
		
		
		
		
		add(nav);
		
		
		        

		
		
		
		
	}	
		
}
