package dellemuse.webapp.page.site;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;


import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.webapp.ServiceLocator;
import dellemuse.webapp.branded.global.BrandedGlobalTopPanel;
import dellemuse.webapp.db.DBService;
import dellemuse.webapp.global.GlobalFooterPanel;
import dellemuse.webapp.global.PageHeaderPanel;
import dellemuse.webapp.guide.GuideContentListItemPanel;
import dellemuse.webapp.page.BasePage;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.BreadcrumbBasePanel;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.struct.list.ListPanel;

/**
 * 
 * site foto Info - exhibitions
 * 
 */

public abstract class BaseSitePage extends BasePage {

    private static final long serialVersionUID = 1L;

    static private Logger logger = Logger.getLogger(BaseSitePage.class.getName());

    private IModel<SiteModel> siteModel;

    private StringValue siteStringValue;
    
    public BaseSitePage() {
    	super(); 
    }
    
    public BaseSitePage(PageParameters parameters) {
        super(parameters);
        siteStringValue = getPageParameters().get("siteid");
    }

    /**
     * @param model
     */
    public BaseSitePage(IModel<SiteModel> model) {
        super();
        this.siteModel = model;       siteStringValue = null;
        super.getPageParameters().set("siteid", getSiteModel().getObject().getId());
    }
    
    /**
     * Exhibition ->  intro
     */
    @Override
    public void onInitialize() {
        super.onInitialize();

        if ( (getSiteModel()== null) && (siteStringValue!=null) && (!siteStringValue.isEmpty())) {
            SiteModel siteModel = getSite(siteStringValue);
            setSiteModel(new Model<>(siteModel));
        }
    }

    protected BreadcrumbBasePanel getSiteHomeElement() {
		return  new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(), new Model<String>(getSiteModel().getObject().getDisplayname() ));
	}
    
    protected SiteModel getSite(StringValue stringValue) {
        try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
            Long id = Long.valueOf(stringValue.toLong());
            return db.getClient().getSiteClientHandler().get(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setSiteModel(IModel<SiteModel> model) {
        this.siteModel = model;
    }

    public IModel<SiteModel> getSiteModel() {
        return siteModel;
    }
    
    @Override
    public void onDetach() {
        super.onDetach();

        if (siteModel != null)
            siteModel.detach();
        
    }


    
    

}
