package dellemuse.webapp.component.global;

import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.webapp.website.page.DellemuseWebHomePage;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavBar;
import io.wktui.nav.menu.NavDropDownMenu;
import wktui.base.LabelLinkPanel;
import wktui.base.ModelPanel;


public class GlobalTopPanel<T> extends ModelPanel<T> {

	private static final long serialVersionUID = 1L;

	public GlobalTopPanel(String id) {
		this(id, null);
	}
	
	public GlobalTopPanel(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		NavBar<Void> nav = new NavBar<Void>("navbarLeft");
		
		LabelLinkPanel logo = new LabelLinkPanel("item", new Model<String>("dellemuse")) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void onClick() {
                setResponsePage( new DellemuseWebHomePage());
            }
		};
		
		nav.addNoCollapseLeft(logo);
		
		logo.setLinkStyle("text-decoration: none;");
		logo.setStyle("font-size: 0.85em; font-weight: 500; color: #dd4508; font-style: normal;");
		
		add(nav);

		//NavBar<Void> navRight = new NavBar<Void>("navbarRight");
		//navRight.addCollapse(getSitesMenu());
		//navRight.addCollapse(getAboutMenu());
		//navRight.addCollapse(getContactMenu());
		//add(navRight);
	}
	

 


		private  NavDropDownMenu<Void> getSitesMenu() {
			
			NavDropDownMenu<Void> menu = new NavDropDownMenu<Void>("item", null, new Model<String>("Instituciones"));
			
			
			
			menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

				private static final long serialVersionUID = 1L;

				@Override
				public MenuItemPanel<Void> getItem(String id) {

					return new  LinkMenuItem<Void>(id) {

						private static final long serialVersionUID = 1L;
						
						@Override
						public void onClick() {
							setResponsePage(new RedirectPage("/sites"));
						}

						@Override
						public IModel<String> getLabel() {
							return new Model<String>("Instituciones");
						}

						@Override
						public String getBeforeClick() {
							return null;
						}
					};
				}
			});

		return menu;
	}
	
	private  NavDropDownMenu<Void> getAboutMenu() {
	
	    NavDropDownMenu<Void> menu = new NavDropDownMenu<Void>("item", null, new Model<String>("Acerca"));
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<Void> getItem(String id) {
				return new  LinkMenuItem<Void>(id) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("https://odilon.io/dellemuse"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Acerca");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});


		
		return menu;
	}
	
	private  NavDropDownMenu<Void> getContactMenu() {

		NavDropDownMenu<Void> menu = new NavDropDownMenu<Void>("item", null, new Model<String>("Contacto"));
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItem<Void>(id) {

					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick() {
					    setResponsePage(new RedirectPage("https://odilon.io/dellemuse"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Contáctenos");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});

		return menu;
	}
	
	
	
	

}
