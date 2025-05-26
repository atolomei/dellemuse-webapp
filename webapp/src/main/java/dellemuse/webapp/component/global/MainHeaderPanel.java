package dellemuse.webapp.component.global;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import wktui.base.InvisiblePanel;
import wktui.base.ModelPanel;

public class MainHeaderPanel<T> extends ModelPanel<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IModel<String> title;
	private Panel breadCrumb;
	
	
	public MainHeaderPanel() {
		this("main-header", null, null, null);
	}

	public MainHeaderPanel(String id, IModel<T> model, IModel<String> title) {
		this(id, model, title, null);
	}

	public MainHeaderPanel(String id, IModel<T> model, IModel<String> title, Panel bc) { 
		super(id, model);
		this.title=title;
		if (bc!=null)
			setBreadCrumb(bc);
	}
	

	public void setBreadCrumb(Panel breadCrumb) {
		
		if (breadCrumb==null) {
			return;
		}
			
		if (!breadCrumb.getId().equals("breadcrumb"))
			throw new IllegalArgumentException("id must be 'breadcrumb'");

		this.breadCrumb=breadCrumb;
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		Label tit = new Label ("title", getTitle()) {
			private static final long serialVersionUID = 1L;
			public boolean isVisible() {
				return (getTitle()!=null);
			};
		};
		add(tit);
		
		if (this.breadCrumb!=null) {
			addOrReplace(this.breadCrumb);
		}
		else
			addOrReplace(new InvisiblePanel("breadcrumb"));
	}

	public IModel<String> getTitle() {
		return title;
	}

		
}
