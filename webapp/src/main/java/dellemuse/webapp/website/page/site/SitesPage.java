package dellemuse.webapp.website.page.site;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.springframework.context.ApplicationContextAware;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.webapp.ServiceLocator;
import dellemuse.webapp.component.global.GlobalFooterPanel;
import dellemuse.webapp.component.global.GlobalTopPanel;
import dellemuse.webapp.component.global.PageHeaderPanel;
import dellemuse.webapp.db.DBService;
import dellemuse.webapp.website.guide.GuideContentListItemPanel;
import dellemuse.webapp.website.page.BasePage;
import io.wktui.struct.list.ListPanel;


/**
 * 
 * site 
 * foto 
 * Info - exhibitions
 * 
 */

@MountPath("/sites")
public class SitesPage extends BasePage  {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(SitesPage.class.getName());

		
	public SitesPage(PageParameters parameters) {
		 super(parameters);
	 }

	/**
	 * 
	 * @param model
	 */
	public SitesPage(IModel<SiteModel> model) {
		 super();
	 }

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		add(new PageHeaderPanel<Void>("page-header", null, new Model<String>("Instituciones")));

		List<SiteModel> sites = getSites();
		List<IModel<SiteModel>> list = new ArrayList<IModel<SiteModel>>();
		sites.forEach( s -> list.add( new Model<SiteModel>(s)));
		
		ListPanel<SiteModel> panel = new ListPanel<>("sites", list) {
            private static final long serialVersionUID = 1L;
            
            @Override
            protected Panel getListItemPanel(IModel<SiteModel> model) {
                GuideContentListItemPanel<SiteModel> panel = new GuideContentListItemPanel<>("row-element", model) {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void onClick() {
                        setResponsePage(new SitePage(model));
                    }
                    
                    protected IModel<String> getInfo(IModel<SiteModel> model) {
                        
                        
                        String info     =   model.getObject().getInfo()!=null?    model.getObject().getInfo().replaceAll("(\\Q\\\\En)+", "<br/>"): "";
                        
                        
                        if (info.length()>200)
                            info  = info.substring(0, 200)+"...";
                        
                        String add      =   model.getObject().getAddress()!=null ?
                                
                        ("<br/>"+model.getObject().getAddress().replace("(\\Q\\\\En)+", "<br/>")): "";
                        
                        String website  =   model.getObject().getWebsite()!=null ? 
                        ("<br/><a href=\" " + model.getObject().getWebsite() + "\" target=\":_blank\"> ver website" + "<br/>"+ "</a>") :  ""; 
                                
                        //.replaceAll("(<br>){2,}"
                        // model.getObject().getWebsite().replaceAll("(\\Q\\\\En)+
                                
                        return new Model<String>( (info +  add +  website));
                    }
                };
                return panel;
            }
		};
		add(panel);
		
		add(new GlobalTopPanel<>("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));

	}
    
	/**
	 * 
	 * 
	 */
    @Override
	public void onDetach() {
	    super.onDetach();
	}

    private List<SiteModel> getSites() {
        try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);    
            return db.getClient().getSiteClientHandler().findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
