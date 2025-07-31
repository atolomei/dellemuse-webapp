package dellemuse.webapp.page.site;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Site;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.webapp.ServiceLocator;
import dellemuse.webapp.branded.global.BrandedGlobalTopPanel;
import dellemuse.webapp.db.DBService;
import dellemuse.webapp.global.GlobalFooterPanel;
import dellemuse.webapp.global.GlobalTopPanel;
import dellemuse.webapp.global.PageHeaderPanel;
import dellemuse.webapp.page.BasePage;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.struct.list.ListPanel;


/**
 * 
 * site 
 * foto 
 * Info - exhibitions
 * 
 */

@MountPath("/exhibition/${id}")
public class ArtExhibitionPage extends BaseSitePage {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(ArtExhibitionPage.class.getName());

	private IModel<ArtExhibitionModel> model;

	private StringValue stringValue;
	
	public  ArtExhibitionPage(PageParameters parameters) {
		 super(parameters);
		 stringValue = getPageParameters().get("id");
	 }

	/**
	 * 
	 * @param model
	 */
	public ArtExhibitionPage(IModel<ArtExhibitionModel> model) {
		 super();
		 this.model=model;
		 super.getPageParameters().set("id", getModel().getObject().getId());
	 }

	 
	 
	protected SiteModel getSite() {
        try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);    
            return db.getClient().getSite(getModel().getObject().getRefSite().getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
   
	
	@Override
	public void onInitialize() {
		super.onInitialize();

		if (stringValue!=null) {
		    ArtExhibitionModel siteModel = getArtExhibition(stringValue);
		    setModel( new Model<>(siteModel));
		    setSiteModel(new Model<SiteModel> (getSite()));
		}
	
		
        BreadCrumb<Void> bc = createBreadCrumb();;
		// bc.addElement(new HREFBCElement("/home", new Model<String>("home")));
	    // bc.addElement(new HREFBCElement("/sites", new Model<String>("Instituciones")));
		
		bc.addElement(new HREFBCElement("/site/"+getModel().getObject().getRefSite().getId().toString(), 
		        new Model<String>(getModel().getObject().getRefSite().getDisplayname())));
		
		bc.addElement(new BCElement(new Model<String>( getModel().getObject().getDisplayname())));
		
		PageHeaderPanel<ArtExhibitionModel> ph = new PageHeaderPanel<ArtExhibitionModel>("page-header", getModel());
		ph.setBreadCrumb(bc);
		
		
		add(ph);
		
		//add(new Label("site", new Model<String>(getModel().getObject().toString())));
		
		
		List<ArtExhibitionGuideModel> sites = getArtExhibitionGuides();
        List<IModel<ArtExhibitionGuideModel>> list = new ArrayList<IModel<ArtExhibitionGuideModel>>();

        sites.forEach( s -> list.add( new Model<ArtExhibitionGuideModel>(s)));
        
        ListPanel<ArtExhibitionGuideModel> panel = new ListPanel<>("guides", list) {

        	
        	private static final long serialVersionUID = 1L;
            
        	
        	protected List<IModel<ArtExhibitionGuideModel>> filter(List<IModel<ArtExhibitionGuideModel>> initialList, String filter) {
            	List<IModel<ArtExhibitionGuideModel>> list = new ArrayList<IModel<ArtExhibitionGuideModel>>();
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
            public IModel<String> getItemLabel(IModel<ArtExhibitionGuideModel> model) {
                return new Model<String>(model.getObject().getDisplayname());
            }
            
            protected void onClick(IModel<ArtExhibitionGuideModel> model) {
                    setResponsePage( new ArtExhibitionGuidePage(model));
            }
        };
        add(panel);
		
		add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));
	}
    
	


 

	protected List<ArtExhibitionGuideModel> getArtExhibitionGuides() {
	    try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);    
            return db.getClient().getArtExhibitionClientHandler().listArtExhibitionGuides(getModel().getObject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	  }

	
    protected ArtExhibitionModel getArtExhibition(StringValue stringValue) {
	    try {
	        DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);    
	        Long id = Long.valueOf(stringValue.toLong());
	        return db.getClient().getArtExhibition(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    public void setModel(IModel<ArtExhibitionModel> model) {
	    this.model=model;
    }

    @Override
	public void onDetach() {
	    super.onDetach();
	    
	    
	    
	    if (model!=null)
	        model.detach();
	}
	
	public IModel<ArtExhibitionModel> getModel() {
	    return model;
	}

}
