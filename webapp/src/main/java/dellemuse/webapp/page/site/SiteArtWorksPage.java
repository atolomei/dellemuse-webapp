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

import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.GuideContentModel;
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

@MountPath("/site/artworks/${siteid}")
public class SiteArtWorksPage extends BaseSitePage {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(SiteArtWorksPage.class.getName());

 	
	public SiteArtWorksPage(PageParameters parameters) {
		 super(parameters);
	 }

	/**
	 * @param model
	 */
	public SiteArtWorksPage(IModel<SiteModel> model) {
		 super(model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		BreadCrumb<Void> bc = createBreadCrumb();

		bc.addElement(new HREFBCElement("/site/"+getSiteModel().getObject().getId().toString(), 
		        new Model<String>( getSiteModel().getObject().getDisplayname())));
		
		
		bc.addElement(new BCElement( getLabel("artworks")));
		
		PageHeaderPanel<SiteModel> ph = new PageHeaderPanel<SiteModel>("page-header", getSiteModel(), getLabel("site-artworks"));
		ph.setBreadCrumb(bc);
		
		add(ph);
				
		List<ArtWorkModel> sites = getArtWorks();
       
		List<IModel<ArtWorkModel>> list = new ArrayList<IModel<ArtWorkModel>>();

        sites.forEach(s -> list.add( new Model<ArtWorkModel>(s)));
        
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
                        String str = TextCleaner.clean(model.getObject().getInfo(), 280);
                        return new Model<String>(str);
                    }
                    
                    @Override
                    protected String getImageSrc(IModel<ArtWorkModel> model) {
                    	//logger.debug(model.getObject().toString());
                    	if (model.getObject().getRefPhotoModel()!=null) {
                            return getPresignedThumbnailSmall(model.getObject().getRefPhotoModel());
                        }
                        return null;
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
            
            @Override
            protected List<IModel<ArtWorkModel>> filter(List<IModel<ArtWorkModel>> initialList, String filter) {
            	
            	List<IModel<ArtWorkModel>> list = new ArrayList<IModel<ArtWorkModel>>();

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
            
        };
        add(panel);
		
		add(new GlobalTopPanel("top-panel", getSiteModel()));
		add(new GlobalFooterPanel<>("footer-panel"));
	}
	


    protected List<ArtWorkModel> getArtWorks() {
	    try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);    
            return db.getClient().listSiteArtWorks(getSiteModel().getObject());
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

     

    @Override
	public void onDetach() {
	    super.onDetach();
	
	    
	    
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
