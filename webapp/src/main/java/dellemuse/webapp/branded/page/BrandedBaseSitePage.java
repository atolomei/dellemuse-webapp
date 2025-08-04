package dellemuse.webapp.branded.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
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
import dellemuse.model.GuideContentModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.webapp.ServiceLocator;
import dellemuse.webapp.branded.global.BrandedGlobalTopPanel;
import dellemuse.webapp.db.DBService;
import dellemuse.webapp.global.GlobalFooterPanel;
import dellemuse.webapp.global.PageHeaderPanel;
import dellemuse.webapp.guide.GuideContentListItemPanel;
import dellemuse.webapp.page.BasePage;
import dellemuse.webapp.page.site.GuideContentPage;
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

public abstract class BrandedBaseSitePage extends BrandedBasePage {

    private static final long serialVersionUID = 1L;

    static private Logger logger = Logger.getLogger(BrandedBaseSitePage.class.getName());

    private IModel<SiteModel> siteModel;

  
 
    protected abstract Long getSiteId();
    
     protected SiteModel getSite() {
        try {
        	
        	//if (getSiteModel()!=null && getSiteModel().getObject()!=null)
        	//	return getSiteModel().getObject();
        	
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);    
            SiteModel site=db.getClient().getSite(getSiteId());
            mark(site);
            return site; 
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	
    public BrandedBaseSitePage() {
    	super();
    }
    
    public BrandedBaseSitePage(PageParameters parameters) {
        super(parameters);
    }

    /**
     * @param model
     */
    public BrandedBaseSitePage(IModel<SiteModel> model) {
        super();
        this.siteModel = model;
     }
    
    

    /**
     * Exhibition ->  intro
     */
    @Override
    public void onInitialize() {
        super.onInitialize();
    }

    
    
    protected BreadcrumbBasePanel getSiteHomeElement() {
		return  new HREFBCElement("/dem/site/" + getSiteModel().getObject().getId().toString(), getLabel("audioguides"));
	}
    
    
    @Override
	public String getHomeUrl() {
		return getSiteModel().getObject().getWebsite(); 
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



    /**
    protected SiteModel getSite(StringValue stringValue) {
        try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
            Long id = Long.valueOf(stringValue.toLong());
            SiteModel site=db.getClient().getSiteClientHandler().get(id);
            mark(site);
            return site;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    


    protected SiteModel getSite(Long id) {
        try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
            SiteModel site=db.getClient().getSiteClientHandler().get(id);
            mark(site);
            return site;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
**/
   
    
    

}
