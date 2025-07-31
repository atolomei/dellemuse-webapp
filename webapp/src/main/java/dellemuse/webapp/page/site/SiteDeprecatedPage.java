package dellemuse.webapp.page.site;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Site;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.webapp.ServiceLocator;
import dellemuse.webapp.branded.global.BrandedGlobalTopPanel;
import dellemuse.webapp.db.DBService;
import dellemuse.webapp.global.GlobalFooterPanel;
import dellemuse.webapp.global.PageHeaderPanel;
import dellemuse.webapp.guide.GridSimpleItemPanel;
import dellemuse.webapp.guide.GuideContentListItemPanel;
import dellemuse.webapp.page.BasePage;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.SimpleGridPanel;


/**
 * 
 * site 
 * foto 
 * Info - exhibitions
 * 
 */


@MountPath("/sitedeprecated/${id}")
public class SiteDeprecatedPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(SiteDeprecatedPage.class.getName());

	private IModel<SiteModel> model;

	private StringValue stringValue;
	
	public  SiteDeprecatedPage(PageParameters parameters) {
		 super(parameters);
		 stringValue = getPageParameters().get("id");
	 }

	/**
	 * 
	 * 
	 * @param model
	 */
	public SiteDeprecatedPage(IModel<SiteModel> model) {
		 super();
		 this.model=model;
		 super.getPageParameters().set("id", getModel().getObject().getId());
	 }

	@Override
	public void onInitialize() {
		super.onInitialize();

		if (stringValue!=null) {
		    SiteModel siteModel = getSite(stringValue);
		    setModel( new Model<>(siteModel));
		}
	
		BreadCrumb<Void> bc = new BreadCrumb<>();
		bc.addElement(new HREFBCElement("/home", new Model<String>("Portada")));
		bc.addElement(new BCElement(new Model<String>( getModel().getObject().getDisplayname())));

		PageHeaderPanel<SiteModel> ph = new PageHeaderPanel<SiteModel>("page-header", getModel());
		ph.setBreadCrumb(bc);
		add(ph);
		
		Link<SiteModel> guide = new Link<SiteModel>("guide", getModel()) {
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick() {
                setResponsePage( new SitePage(getModel()));
            }
		};
		
		add(guide);
		
		String info     =   model.getObject().getInfo()!=null?    model.getObject().getInfo().replaceAll("[\\n]+", "<br/>").replace("<br/><br/>", "<br/>"): "";
		
        //String add      =   model.getObject().getAddress()!=null? model.getObject().getAddress().replace("\\n", "<br/>"): "";
        String website  =   model.getObject().getWebsite()!=null? 
                            ("<br/><a href=\" " + model.getObject().getWebsite() + "\" target=\":_blank\">" + model.getObject().getWebsite().replace("\\n", "<br/>") + "</a>"): "";

        add( (new Label("site", new Model<String>(("<p>" + info + "<br/>" +  website + "</p>")))).setEscapeModelStrings(false));
		
        
        /**
		List<ArtExhibitionModel> sites = getArtExhibitions();
        List<IModel<ArtExhibitionModel>> list = new ArrayList<IModel<ArtExhibitionModel>>();

        sites.forEach( s -> list.add( new Model<ArtExhibitionModel>(s)));
        
        
        ListPanel<ArtExhibitionModel> panel = new ListPanel<>("exhibitions", list) {
            private static final long serialVersionUID = 1L;
            @Override
            protected Panel getListItemPanel(IModel<ArtExhibitionModel> model) {
                GuideContentListItemPanel<ArtExhibitionModel> panel = new GuideContentListItemPanel<>("row-element", model) {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void onClick() {
                        setResponsePage( new ArtExhibitionPage(model));
                    }
                    
                    protected IModel<String> getInfo(IModel<ArtExhibitionModel> model) {
                        
                        String str = TextCleaner.clean(model.getObject().getInfo()) + TextCleaner.clean(model.getObject().getLocation()); 
                        
                        if (model.getObject().getLocation()!=null && model.getObject().getLocation().length()>0) 
                            str = str + "<br/> <b>Sala</b>. " + TextCleaner.clean(model.getObject().getLocation()); 
                        
                        //String info     =   model.getObject().getInfo()!=null?    model.getObject().getInfo().replaceAll("(\\r?\\n){2,}", "<br/>"): "";
                        //String location =   model.getObject().getLocation()!=null? ("<br/><br/><b>Sala</b>: " + model.getObject().getLocation().replaceAll("(\\r?\\n){2,}", "<br/>")): "<br/><b>Sala</b>: Primer piso salas 34 y 38";
                        
                        return new Model<String>(str);
                    }
                };
                return panel;
            }
        };
        add(panel);
        

        
        ListPanel<ArtExhibitionModel> collection = new ListPanel<>("collection", list) {
            private static final long serialVersionUID = 1L;
            @Override
            protected Panel getListItemPanel(IModel<ArtExhibitionModel> model) {
                GuideContentListItemPanel<ArtExhibitionModel> panel = new GuideContentListItemPanel<>("row-element", model) {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void onClick() {
                        setResponsePage( new ArtExhibitionPage(model));
                    }
                    
                    protected IModel<String> getInfo(IModel<ArtExhibitionModel> model) {
                        String str = TextCleaner.clean(model.getObject().getInfo()) + TextCleaner.clean(model.getObject().getLocation()); 
                        if (model.getObject().getLocation()!=null && model.getObject().getLocation().length()>0) 
                            str = str + "<br/> <b>Sala</b>. " + TextCleaner.clean(model.getObject().getLocation()); 
                        return new Model<String>(str);
                    }
                };
                return panel;
            }
        };
        add(collection);

        

                                            
        
        SimpleGridPanel<ArtExhibitionModel> news = new SimpleGridPanel<>("news", list) {
            private static final long serialVersionUID = 1L;
            @Override
            protected Panel getListItemPanel(IModel<ArtExhibitionModel> model) {
                GridSimpleItemPanel<ArtExhibitionModel> panel = new GridSimpleItemPanel<>("row-element", model) {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void onClick() {
                        setResponsePage( new ArtExhibitionPage(model));
                    }
                    protected IModel<String> getInfo(IModel<ArtExhibitionModel> model) {
                        String str = TextCleaner.clean(model.getObject().getInfo()) + TextCleaner.clean(model.getObject().getLocation()); 
                        return new Model<String>(str);
                    }
                };
                return panel;
            }
        };
        add(news);
        */
        
        
        
        
        
        
		
		
		add(new BrandedGlobalTopPanel("top-panel", getModel()));
		add(new GlobalFooterPanel<>("footer-panel"));
	}
    
	
	private List<ArtExhibitionModel> getArtExhibitions() {
	    try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);    
            return db.getClient().getSiteClientHandler().listArtExhibitionsBySite( getModel().getObject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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


