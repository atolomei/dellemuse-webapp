package dellemuse.webapp.website.page;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import dellemuse.model.logging.Logger;
import dellemuse.webapp.component.global.GlobalFooterPanel;
import dellemuse.webapp.component.global.GlobalTopPanel;



@WicketHomePage
@MountPath("home")
public class DellemuseWebHomePage extends BasePage {

	private static final long serialVersionUID = 1L;

	
	static private Logger logger = Logger.getLogger(DellemuseWebHomePage.class.getName());
	
	public  DellemuseWebHomePage(PageParameters parameters) {
		 super(parameters);
		 logger.info("here ss");
	 }
	
	public  DellemuseWebHomePage() {
		 super();
		 logger.info("here");
	 }
	 
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		WebMarkupContainer bck =  new WebMarkupContainer("bck");
		
		
		
		add(new GlobalTopPanel<>("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));
		
		
	
		
	}

}
