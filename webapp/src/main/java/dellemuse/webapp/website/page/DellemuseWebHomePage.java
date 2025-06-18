package dellemuse.webapp.website.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.context.ApplicationContext;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import dellemuse.model.GuideContentModel;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.webapp.ServiceLocator;
import dellemuse.webapp.Settings;
import dellemuse.webapp.component.global.GlobalFooterPanel;
import dellemuse.webapp.component.global.GlobalTopPanel;
import dellemuse.webapp.db.DBService;
import dellemuse.webapp.website.guide.GuideContentListItemPanel;
import dellemuse.webapp.website.page.site.SiteGuidePage;
import dellemuse.webapp.website.page.site.SitePage;
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
        isServerSimulated  = ServiceLocator.getInstance().getApplicationContext().getBean(Settings.class).isSimulateServer();
    }

    public DellemuseWebHomePage() {
        super();
        isServerSimulated  = ServiceLocator.getInstance().getApplicationContext().getBean(Settings.class).isSimulateServer();
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        add(new GlobalTopPanel<>("top-panel"));
        add(new GlobalFooterPanel<>("footer-panel"));
        
        
        if (isServerSimulated) {
            add(new LabelPanel("sites", new Model<String>("Server is simulated")));
            return;
        }

        List<SiteModel> sites = getSites();
        List<IModel<SiteModel>> list = new ArrayList<IModel<SiteModel>>();
        sites.forEach(s -> list.add(new Model<SiteModel>(s)));

        ListPanel<SiteModel> panel = new ListPanel<>("sites", list) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Panel getListItemPanel(IModel<SiteModel> model) {
                GuideContentListItemPanel<SiteModel> panel = new GuideContentListItemPanel<>("row-element", model) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        setResponsePage(new SiteGuidePage(model));
                    }

                    protected IModel<String> getInfo(IModel<SiteModel> model) {
                        String info = model.getObject().getIntro() != null ? TextCleaner.clean(model.getObject().getIntro()) : "";
                        return new Model<String>((info));
                    }

                    @Override
                    protected String getImageSrc(IModel<SiteModel> model) {
                        if (model.getObject().getPhotoModel() != null) {
                            return getPresignedThumbnailSmall(model.getObject().getPhotoModel());
                        }
                        return null;
                    }
                };
                return panel;
            }
        };
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

    /**
    private String getResourcePresignedUrl(ResourceModel photo) {
        try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
            Long id = Long.valueOf(photo.getId());
            return db.getClient().getPresignedThumbnailUrl(id, ThumbnailSize.SMALL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
**/
    
}
