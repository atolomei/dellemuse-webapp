package dellemuse.webapp.website.page.site;

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
import dellemuse.webapp.component.global.GlobalFooterPanel;
import dellemuse.webapp.component.global.GlobalTopPanel;
import dellemuse.webapp.component.global.PageHeaderPanel;
import dellemuse.webapp.db.DBService;
import dellemuse.webapp.website.page.BasePage;
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
public class ArtExhibitionPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
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

	@Override
	public void onInitialize() {
		super.onInitialize();

		if (stringValue!=null) {
		    ArtExhibitionModel siteModel = getArtExhibition(stringValue);
		    setModel( new Model<>(siteModel));
		}
	
		
		BreadCrumb<Void> bc = new BreadCrumb<>();
		//bc.addElement(new HREFBCElement("/home", new Model<String>("home")));
	    bc.addElement(new HREFBCElement("/sites", new Model<String>("Instituciones")));
		
		bc.addElement(new HREFBCElement("/siteguide/"+getModel().getObject().getSiteModel().getId().toString(), 
		        new Model<String>(getModel().getObject().getSiteModel().getDisplayname())));
		
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
            
            @Override
            public IModel<String> getItemLabel(IModel<ArtExhibitionGuideModel> model) {
                return new Model<String>(model.getObject().getDisplayname());
            }
            
            protected void onClick(IModel<ArtExhibitionGuideModel> model) {
                    setResponsePage( new ArtExhibitionGuidePage(model));
            }
        };
        add(panel);
		
		add(new GlobalTopPanel<>("top-panel"));
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
