package dellemuse.webapp.global;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.SiteModel;
import dellemuse.model.UserModel;

import dellemuse.webapp.page.security.DellemuseWebSigninPage;
import dellemuse.webapp.page.security.DellemuseWebSignupPage;
import dellemuse.webapp.page.site.SiteSearchArtWorkPage;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavBar;
import io.wktui.nav.menu.NavDropDownMenu;
import wktui.base.InvisiblePanel;
import wktui.base.LabelLinkPanel;
import wktui.base.ModelPanel;


public class GlobalTopPanel extends ModelPanel<UserModel> {

	private static final long serialVersionUID = 1L;

	
	private IModel<SiteModel> siteModel;
	private boolean isSearch = true;
	String srcUrl;
	
	public GlobalTopPanel(String id) {
		this(id, null, null, null);
	}
	
	
	public GlobalTopPanel(String id, IModel<SiteModel> siteModel) {
		super(id, null);
		this.siteModel=siteModel;
	}
	
	
	public GlobalTopPanel(String id, IModel<SiteModel> siteModel,  IModel<UserModel> userModel, String srcUrl) {
		super(id, userModel);
		this.siteModel=siteModel;
		this.srcUrl=srcUrl;
	}

	public void onDetach() {
		super.onDetach();
		
		if (siteModel!=null)
			siteModel.detach();
	}
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		NavBar<Void> nav = new NavBar<Void>("navbarLeft");

		LabelLinkPanel logo = new LabelLinkPanel("item", new Model<String>("DeM")) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void onClick() {
                setResponsePage(new RedirectPage("https://www.bellasartes.gob.ar/"));
            }
		};
		
		
		nav.addNoCollapseLeft(logo);
		
		logo.setLinkStyle("text-decoration: none;");
		logo.setStyle("font-size: 0.85em; font-weight: 500; color: orange; font-style: normal;");
		
		add(nav);

		Link<Void> find = new Link<Void>("find") {
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick() {
                    setResponsePage(new SiteSearchArtWorkPage(getSiteModel()));   
            }
            public boolean isVisible() {
            	return isSearch() && getSiteModel()!=null;
            }
        };
        add(find);

        
		Link<Void> sup = new Link<Void>("signup") {
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick() {
                    setResponsePage( new DellemuseWebSignupPage());   
            }
            
        };
        
        sup.setVisible(false);
        add(sup);
        
        
        Link<Void> si = new Link<Void>("signin") {
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick() {
                   setResponsePage( new DellemuseWebSigninPage());
            }
        };
        
        si.setVisible(false);
        add(si);
        
        if (getModel()!=null) {
        	UserGlobalTopPanel userPanel = new UserGlobalTopPanel("userGlobalTopPanel", getModel());
    		add(userPanel);
       }
        else {
        	add(new InvisiblePanel("userGlobalTopPanel"));
       }
	
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

	public IModel<SiteModel> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<SiteModel> siteModel) {
		this.siteModel = siteModel;
	}


	public boolean isSearch() {
		return isSearch;
	}


	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}
	
	
	
	

}
