package dellemuse.webapp.branded.page;

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


import dellemuse.model.ArtWorkModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import dellemuse.webapp.branded.global.BrandedMediaGlobalTopPanel;
import io.wktui.error.ErrorPanel;

/**
 * 
 * site foto Info - exhibitions
 * 
 */


@MountPath("/dem/artwork/image/${id}")
public class BrandedArtWorkImagePage extends BrandedBaseSitePage {

    private static final long serialVersionUID = 1L;

    static private Logger logger = Logger.getLogger(BrandedArtWorkImagePage.class.getName());

    WebMarkupContainer photoContainer;

     
    private IModel<ArtWorkModel> model;

    private StringValue stringValueArtWork;
    
    private Image image;
    private String srcUrl;
    
    public BrandedArtWorkImagePage(PageParameters parameters) {
        super(parameters);
        stringValueArtWork = getPageParameters().get("id");
        
    }

    /**
     * @param model
     */
    public BrandedArtWorkImagePage(IModel<ArtWorkModel> model, String srcUrl) {
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

        
        if (getModel()==null && stringValueArtWork== null) {
        	//add(new InvisiblePanel("page-header"));
            photoContainer = new ErrorPanel("photoContainer"); 
            add(photoContainer);
        	return;
        }
        	
        if (getModel()==null && stringValueArtWork != null) {
            ArtWorkModel a_model = getArtWork(stringValueArtWork);
            setModel(new Model<ArtWorkModel>(a_model));
        }
        

        setSiteModel( new Model<SiteModel> (getSite()));
        
        BrandedMediaGlobalTopPanel tp = new BrandedMediaGlobalTopPanel("top-panel", getSiteModel(), getSrcUrl());
        tp.setSearch(false);
        add(tp);
        //add(new GlobalFooterPanel<>("footer-panel"));
        
        if (getModel()==null && getModel().getObject()==null) {
        	//add(new InvisiblePanel("page-header"));
            photoContainer = new ErrorPanel("photoContainer"); 
            add(photoContainer);
        	return;
        }

        
        SiteModel siteModel =  getSite();
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



	public String getSrcUrl() {
		return this.srcUrl;
	}

	
	@Override
	protected Long getSiteId() {
		Check.requireTrue(getModel()!=null && getModel().getObject()!=null, "can not call getSiteId() before setting the Page Model");
		return getModel().getObject().getRefSite().getId();
	}

	/**
	protected SiteModel getSite(IModel<ArtWorkModel> model) {
        try {
            DBSe rvice db = (DBServ ice) ServiceLocator.getInstance().getBean(DBS ervice.class);
            Long id = Long.valueOf(model.getObject().getSiteModel().getId());
            return db.getClient().getSiteClientHandler().get(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
**/
	
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
