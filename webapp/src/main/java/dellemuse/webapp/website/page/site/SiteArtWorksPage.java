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

import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.webapp.ServiceLocator;
import dellemuse.webapp.component.global.GlobalFooterPanel;
import dellemuse.webapp.component.global.GlobalTopPanel;
import dellemuse.webapp.component.global.PageHeaderPanel;
import dellemuse.webapp.db.DBService;
import dellemuse.webapp.website.guide.GuideContentListItemPanel;
import dellemuse.webapp.website.page.BasePage;
import io.wktui.model.TextCleaner;
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

@MountPath("/site/artwork/${id}")
public class SiteArtWorksPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(SiteArtWorksPage.class.getName());

	private IModel<SiteModel> model;
	private StringValue stringValue;
	
	
	public  SiteArtWorksPage(PageParameters parameters) {
		 super(parameters);
		 stringValue = getPageParameters().get("id");
	 }

	/**
	 * @param model
	 */
	public SiteArtWorksPage(IModel<SiteModel> model) {
		 super();
		 this.model=model;
		 super.getPageParameters().set("id", getModel().getObject().getId());
	 }

	@Override
	public void onInitialize() {
		super.onInitialize();

		if (stringValue!=null) {
		    SiteModel siteModel = getSiteModel(stringValue);
		    setModel(new Model<>(siteModel));
		}

		

		BreadCrumb<Void> bc = new BreadCrumb<>();
		bc.addElement(new HREFBCElement("/sites", new Model<String>("Sites")));
		
		bc.addElement(new HREFBCElement("/site/"+getModel().getObject().getId().toString(), 
		        new Model<String>( getModel().getObject().getDisplayname())));
		
		
		bc.addElement(new BCElement(new Model<String>("Artworks")));
		
		PageHeaderPanel<SiteModel> ph = new PageHeaderPanel<SiteModel>("page-header", getModel(), new Model<String>("Site Artworks"));
		ph.setBreadCrumb(bc);
		
		add(ph);
		
		
		
        		
		List<ArtWorkModel> sites = getArtWorks();
        List<IModel<ArtWorkModel>> list = new ArrayList<IModel<ArtWorkModel>>();

        sites.forEach( s -> list.add( new Model<ArtWorkModel>(s)));
        
        ListPanel<ArtWorkModel> panel = new ListPanel<>("contents", list) {
            private static final long serialVersionUID = 1L;
            
            @Override
            protected Panel getListItemPanel(IModel<ArtWorkModel> model) {

                GuideContentListItemPanel<ArtWorkModel> panel = new GuideContentListItemPanel<>("row-element", model) {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void onClick() {
                        //setResponsePage( new ArtExhibitionPage(model));
                    }
                    
                    protected IModel<String> getInfo(IModel<ArtWorkModel> model) {
                        String str = TextCleaner.clean(model.getObject().getInfo());
                        return new Model<String>(str);
                    }
                };
                return panel;
            }
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            protected void onClick(IModel<ArtWorkModel> model) {
                //setResponsePage( new GuideContentPage(model));
            }
            @Override
            public IModel<String> getItemLabel(IModel<ArtWorkModel> model) {
                return new Model<String>(model.getObject().getDisplayname());
            }
            
            
        };
        add(panel);
		
		
		add(new GlobalTopPanel<>("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));
	}
	


    protected List<ArtWorkModel> getArtWorks() {
	    try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);    
            return db.getClient().listArtWorks();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	  }

	
    protected SiteModel getSiteModel(StringValue stringValue) {
	    try {
	        DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);    
	        Long id = Long.valueOf(stringValue.toLong());
	        return db.getClient().getSite(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    public void setModel(IModel<SiteModel> model) {
	    this.model=model;
    }

    @Override
	public void onDetach() {
	    super.onDetach();
	
	    if (model!=null)
	        model.detach();
	    
	    
	  
	    
	    
	    
	}
	
	public IModel<SiteModel> getModel() {
	    return model;
	}

}

/**
<section class="container-fluid main">

<div  style="float:left; width:100%;" wicket:id="page-header"></div>    

<div  style="float:left; width:100%;">
    <div class="row">
        <div class="col-lg-12 col-md-12 col-xs-12">
            description
        </div>
    </div>
</div>

<div style="float:left; width:100%;">
    <div class="row">
        <div class="col-lg-12 col-md-12 col-xs-12">
            <div wicket:id="contents"></div>
        </div>
    </div>
</div>

</section>
*/
