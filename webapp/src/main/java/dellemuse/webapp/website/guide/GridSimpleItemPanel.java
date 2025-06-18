package dellemuse.webapp.website.guide;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.DelleMuseModelObject;
import dellemuse.model.GuideContentModel;
import io.wktui.nav.menu.DropDownMenu;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavBar;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.menu.SeparatorMenuItem;
import wktui.base.BasePanel;
import wktui.base.LabelPanel;
import wktui.base.ModelPanel;


public class GridSimpleItemPanel<T extends DelleMuseModelObject> extends ModelPanel<T> {

	private static final long serialVersionUID = 1L;

	public GridSimpleItemPanel(String id) {
		this(id, null);
	}
	
	public GridSimpleItemPanel(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		Link<T> imageLink = new Link<>("image-link", getModel()) {
            @Override
            public void onClick() {
                GridSimpleItemPanel.this.onClick();
            }
        };
        add(imageLink);
        
		
		
		Link<T> titleLink = new Link<>("title-link", getModel()) {
            @Override
            public void onClick() {
                GridSimpleItemPanel.this.onClick();
            }
		};
		add(titleLink);
		
		
		Label title = new Label( "title", getModel().getObject().getDisplayname());
		titleLink.add(title);
		
		
		Label text = new Label( "text", getInfo(getModel()));
		text.setEscapeModelStrings(false);
		add(text);
	}
	
	
	protected IModel<String> getInfo(IModel<T> model) {
        return new Model<String>( model.getObject().toJSON());
    }

    public void onClick() {
	    
	    
	}
	

	
	
	

}
