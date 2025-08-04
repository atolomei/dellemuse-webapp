package dellemuse.webapp.page.site;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;


import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.webapp.ServiceLocator;
import dellemuse.webapp.branded.global.BrandedGlobalTopPanel;
import dellemuse.webapp.db.DBService;
import dellemuse.webapp.global.GlobalFooterPanel;
import dellemuse.webapp.global.GlobalTopPanel;
import dellemuse.webapp.global.PageHeaderPanel;
import dellemuse.webapp.guide.GuideContentListItemPanel;
import dellemuse.webapp.page.BasePage;
import io.wktui.error.ErrorPanel;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.struct.list.ListPanel;
import wktui.base.InvisiblePanel;

/**
 * 
 * site foto Info - exhibitions
 * 
 */


@MountPath("/artwork/image/${id}")
public class ArtWorkImagePage extends BaseSitePage {

    private static final long serialVersionUID = 1L;

    static private Logger logger = Logger.getLogger(ArtWorkImagePage.class.getName());

    WebMarkupContainer photoContainer;

     
    private IModel<ArtWorkModel> model;

    private StringValue stringValue;
    
    Image image;
    
    public ArtWorkImagePage(PageParameters parameters) {
        super(parameters);
        stringValue = getPageParameters().get("id");
        
    }

    private String srcUrl;
    
    /**
     * @param model
     */
    public ArtWorkImagePage(IModel<ArtWorkModel> model, String srcUrl) {
        super();
        this.model = model;
        this.srcUrl=srcUrl;
        super.getPageParameters().set("id", getModel().getObject().getId());
    }


    
    /**
     * 
     * 
     * 
     */
    @Override
    public void onInitialize() {
        super.onInitialize();

        
        if (getModel()==null && stringValue == null) {
        	//add(new InvisiblePanel("page-header"));
            photoContainer = new ErrorPanel("photoContainer"); 
            add(photoContainer);
        	return;
        }
        	
        if (getModel()==null && stringValue != null) {
            ArtWorkModel a_model = getArtWork(stringValue);
            setModel(new Model<ArtWorkModel>(a_model));
        }
        

        setSiteModel( new Model<SiteModel> (getSite( getModel())));
        
        GlobalTopPanel tp = new GlobalTopPanel("top-panel", getSiteModel(), null, srcUrl);
        tp.setSearch(false);
        add(tp);
        //add(new GlobalFooterPanel<>("footer-panel"));
        
        if (getModel()==null && getModel().getObject()==null) {
        	//add(new InvisiblePanel("page-header"));
            photoContainer = new ErrorPanel("photoContainer"); 
            add(photoContainer);
        	return;
        }

        
        SiteModel siteModel =  getSite( getModel() );
        if (siteModel!=null)
        	setSiteModel ( new Model<SiteModel> (siteModel) );
        
        
        
        
        
        photoContainer = new WebMarkupContainer("photoContainer"); 
        add(photoContainer);

        String presignedUrl = getPresignedUrl( getModel().getObject().getRefPhotoModel());
        
        if (presignedUrl != null) {
            Url url = Url.parse(presignedUrl);
            UrlResourceReference resourceReference = new UrlResourceReference(url);
            image = new Image ("image", resourceReference);
            photoContainer.addOrReplace(image);
        }
        else {
            image = new Image ("image", new UrlResourceReference(Url.parse("")));
            image.setVisible(false);
            photoContainer.addOrReplace(image);
        }        
    }


	protected SiteModel getSite(IModel<ArtWorkModel> model) {
        try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
            Long id = Long.valueOf( model.getObject().getRefSite().getId());
            return db.getClient().getSiteClientHandler().get(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setModel(IModel<ArtWorkModel> model) {
        this.model = model;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        
        if (model != null)
            model.detach();
     
    }

    public IModel<ArtWorkModel> getModel() {
        return model;
    }


}
