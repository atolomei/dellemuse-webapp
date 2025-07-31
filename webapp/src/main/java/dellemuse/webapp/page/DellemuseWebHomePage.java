package dellemuse.webapp.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import dellemuse.model.GuideContentModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.webapp.ServiceLocator;
import dellemuse.webapp.Settings;
import dellemuse.webapp.branded.global.BrandedGlobalTopPanel;
import dellemuse.webapp.branded.page.BrandedSitePage;
import dellemuse.webapp.db.DBService;
import dellemuse.webapp.global.GlobalFooterPanel;
import dellemuse.webapp.global.GlobalTopPanel;
import dellemuse.webapp.guide.GuideContentListItemPanel;
import dellemuse.webapp.page.site.SitePage;
import io.wktui.model.TextCleaner;
import io.wktui.struct.list.ListPanel;
import wktui.base.LabelPanel;

@WicketHomePage
@MountPath("home")
public class DellemuseWebHomePage extends BasePage {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(DellemuseWebHomePage.class.getName());
    
    final boolean isServerSimulated;
    
    public DellemuseWebHomePage(PageParameters parameters) {
        super(parameters);
        this.isServerSimulated  = ServiceLocator.getInstance().getApplicationContext().getBean(Settings.class).isSimulateServer();
    }

    public DellemuseWebHomePage() {
        super();
        this.isServerSimulated  = ServiceLocator.getInstance().getApplicationContext().getBean(Settings.class).isSimulateServer();
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        
        Link<Void> mnba =new Link<Void>("mnba") {
			 
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
			IModel<SiteModel> siteModel = new Model<SiteModel>( getSiteByShortName("mnba"));
			setResponsePage( new BrandedSitePage(siteModel));
			}
        };
        add(mnba);
        
        add(new GlobalTopPanel("top-panel"));
        add(new GlobalFooterPanel<>("footer-panel"));
        
        if (this.isServerSimulated) {
            add(new LabelPanel("sites", new Model<String>("Server is simulated")));
            return;
        }

        List<SiteModel> sites = getSites();
        List<IModel<SiteModel>> list = new ArrayList<IModel<SiteModel>>();
        sites.forEach(s -> list.add(new Model<SiteModel>(s)));

        ListPanel<SiteModel> panel = new ListPanel<>("sites", list) {

        	private static final long serialVersionUID = 1L;

        	@Override
        	protected IModel<String> getLiveSearchTitle() {
        		return new Model<String>("Filtrar por nombre");
        	}
        	
        	
        	@Override
            protected List<IModel<SiteModel>> filter(List<IModel<SiteModel>> initialList, String filter) {
            	List<IModel<SiteModel>> list = new ArrayList<IModel<SiteModel>>();
            	final String str = filter.trim().toLowerCase();
            	initialList.forEach(
                		s -> {
				                if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
				                	 list.add(s);
				                }
                			 }
               );
               return list; 
            }
            
            @Override
            protected Panel getListItemPanel(IModel<SiteModel> model) {
                GuideContentListItemPanel<SiteModel> panel = new GuideContentListItemPanel<>("row-element", model) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        setResponsePage(new SitePage(model));
                    }

                    protected IModel<String> getInfo(IModel<SiteModel> model) {
                        String info = model.getObject().getIntro() != null ? TextCleaner.clean(model.getObject().getIntro(), 120) : "";
                        return new Model<String>((info));
                    }

                    @Override
                    protected String getImageSrc(IModel<SiteModel> model) {
                        if (model.getObject().getRefPhotoModel() != null) {
                            return getPresignedThumbnailSmall(model.getObject().getRefPhotoModel());
                        }
                        return null;
                    }
                };
                return panel;
            }
        };
        
        panel.setVisible(false);
        panel.setLiveSearch(true);
        add(panel);
    }

    private List<SiteModel> getSites() {
        try {
            if (this.isServerSimulated)
                return new ArrayList<SiteModel>();
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
            return db.getClient().getSiteClientHandler().findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
