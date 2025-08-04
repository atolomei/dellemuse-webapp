package dellemuse.webapp.page.site;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.maven.model.Site;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.media.audio.Audio;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.apache.wicket.util.string.StringValue;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionItemModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.PersonModel;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.ref.RefPersonModel;
import dellemuse.model.ref.RefResourceModel;
import dellemuse.model.util.ThumbnailSize;
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
import io.wktui.nav.breadcrumb.BreadcrumbBasePanel;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.struct.list.ListPanel;
import io.wktui.text.ExpandableReadPanel;
import wktui.base.InvisiblePanel;


/**
 * Site 
 * Foto 
 * Info - exhibitions
 */

@MountPath("/guide/${id}")
public class ArtExhibitionGuidePage extends BaseSitePage {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(ArtExhibitionGuidePage.class.getName());

	private IModel<ArtExhibitionGuideModel> model;
	private IModel<ArtExhibitionItemModel> artExhibitionItemModel;
	private IModel<ArtWorkModel> artWorkModel;
	
	private IModel<ArtExhibitionModel> artExhibitionModel;
	private IModel<SiteModel> siteModel;

	private StringValue stringValue;

	public  ArtExhibitionGuidePage(PageParameters parameters) {
		 super(parameters);
		 stringValue = getPageParameters().get("id");
	 }

	/**
	 * @param model
	 */
	public ArtExhibitionGuidePage(IModel<ArtExhibitionGuideModel> model) {
		 super();
		 this.model=model;
		 stringValue = null;
		 super.getPageParameters().set("id", getModel().getObject().getId());
	 
	}

	
 
    
	/**
	 *  Exhibition ->  intro
	 */
	@Override
	public void onInitialize() {
		super.onInitialize();

		if ( (this.stringValue!=null) && (!this.stringValue.isEmpty())) {
		    ArtExhibitionGuideModel siteModel = getArtExhibitionGuide(stringValue);
		    setModel(new Model<>(siteModel));
		}

		this.artExhibitionModel    = new Model<ArtExhibitionModel> ( getArtExhibitionModel()  );
		this.siteModel             = new Model<SiteModel>          ( getSite()           );	      
        
        BreadCrumb<Void> bc = createBreadCrumb();
        
        bc.addElement(getSiteHomeElement());
        bc.addElement(new BCElement(new Model<String>( getModel().getObject().getDisplayname())));
		
		PageHeaderPanel<ArtExhibitionGuideModel> ph = new PageHeaderPanel<ArtExhibitionGuideModel>("page-header", getModel());
		ph.setBreadCrumb(bc);
		
		add(ph);
		
		String intro    =    getArtExhibitionModel() .getIntro()!=null?       TextCleaner.clean ( getArtExhibitionModel().getIntro() ): "";
		add( (new Label("intro", new Model<String>((intro)))).setEscapeModelStrings(false));
		
		
		//String info    =    getArtExhibitionModel() .getInfo()!=null?       TextCleaner.clean ( getArtExhibitionModel().getInfo() ): "";
        //add( (new Label("info", new Model<String>((info)))).setEscapeModelStrings(false));
		// "Del 7 de mayo de 2025 al 8 de junio de 2025 <br/>Segundo piso<br/>40 obras expuestas<br/>Curador/a: Ana Longoni y Carlos Herrera";
		/**
		WebMarkupContainer mic = new WebMarkupContainer ("masinfoContainer");
		String masinfo  =    getArtExhibitionModel().getMoreinfo()!=null?   TextCleaner.clean ( getArtExhibitionModel().getMoreinfo() ): "";
		add(mic);
		mic.setVisible(masinfo!=null && masinfo.length()>0);
		Label l_masinfo=new Label("masinfo", new Model<String>(( masinfo )));
		l_masinfo.setEscapeModelStrings(false);
		mic.add(l_masinfo);
		**/
		
		
		WebMarkupContainer audioContainer = new WebMarkupContainer("audioContainer");
	    addOrReplace(audioContainer);
	    audioContainer.setVisible(isAudioIntro());
		
		if (isAudioIntro()) {
		    WebMarkupContainer audioIntroContainer = new WebMarkupContainer("intro-audio");
		    audioContainer.add(audioIntroContainer);
	        RefResourceModel au= getModel().getObject().getRefAudioModel() != null ? getModel().getObject().getRefAudioModel() : this.getArtExhibitionModel().getRefAudioModel();
	        String as =  getPresignedUrl(au);
	        Url url = Url.parse(as);
            UrlResourceReference resourceReference = new UrlResourceReference(url);
	        Audio audio = new Audio("audioIntro", resourceReference);
	        audioIntroContainer.add(audio);
		}
		else {
		    audioContainer.addOrReplace(new InvisiblePanel("intro-audio"));
		}
        
		List<GuideContentModel> sites = getGuideContents();
        List<IModel<GuideContentModel>> list = new ArrayList<IModel<GuideContentModel>>();

        sites.forEach(s -> list.add( new Model<GuideContentModel>(s)));
        
        list.sort(new Comparator<IModel<GuideContentModel>>() {
            @Override
            public int compare(IModel<GuideContentModel> o1,IModel<GuideContentModel> o2) {
                return o1.getObject().getGuideOrder()<o2.getObject().getGuideOrder() ? -1 : 1;
            }
        });
        
        
        ListPanel<GuideContentModel> panel = new ListPanel<>("contents", list) {

        	private static final long serialVersionUID = 1L;
            
        	protected List<IModel<GuideContentModel>> filter(List<IModel<GuideContentModel>> initialList, String filter) {
            	List<IModel<GuideContentModel>> list = new ArrayList<IModel<GuideContentModel>>();
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
            
            protected Panel getListItemPanel(IModel<GuideContentModel> model) {
                
                GuideContentListItemPanel<GuideContentModel> panel = new GuideContentListItemPanel<>("row-element", model) {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void onClick() {
                       setResponsePage(getGuideContentPage(model, getList()));
                    }
                    
                    protected String getImageSrc(IModel<GuideContentModel> model) {
                        RefResourceModel photo = getArtWork (model.getObject()).getRefPhotoModel();
                        if (photo!=null) {
                            return getPresignedThumbnailSmall(photo);
                        }
                        return null;
                    }
                    
                    @Override
                    public IModel<String> getIcon() {
                        if (getModel().getObject().getRefAudioModel()==null)
                            return null;
                        else 
                            return new Model<String>("fa-solid fa-headphones");
                    }
                    @Override
                    public IModel<String> getSubtitle() {
                        ArtWorkModel aw = getArtWork(model.getObject());
                        if (aw!=null) {
                            StringBuilder info = new StringBuilder();
                            int n=0;
                            for (RefPersonModel p:aw.getRefArtists()) {
                                if (n++>0)
                                    info.append(", ");
                                info.append(p.getDisplayname());
                            }
                            String str = TextCleaner.truncate(info.toString(), 420); 
                            return new Model<String>(str);
                        }
                        return null;
                    }
                    
                    /**
                     * guideContent -> item -> artwork
                     * 
                     * @param model
                     * @return
                     */
                    protected IModel<String> getInfo(IModel<GuideContentModel> model) {
                        //StringBuilder info = new StringBuilder();
                      
                        /**
                        if ( model.getObject().getInfo()!=null) {
                             return new Model<String>(TextCleaner.clean(model.getObject().getInfo(), 320));
                        }
                        
                        ArtExhibitionItemModel item = getArtExhibitionItemModel(model.getObject());
                        if (item.getInfo()!=null) {
                           info.append(TextCleaner.clean(item.getInfo(), 320));
                           return new Model<String>(TextCleaner.clean(info.toString(), 320));
                        }
                        else {
                            
                            ArtWorkModel aw = getArtWork(model.getObject());
                            if (aw!=null && (aw.getInfo()!=null)) {                                
                                return new Model<String>(TextCleaner.clean(info.toString(), 320));
                               }
                        }
                        **/
                        
                        ArtWorkModel aw = getArtWork(model.getObject());
                        if (aw!=null && (aw.getIntro()!=null)) {                                
                            return new Model<String>(TextCleaner.clean(aw.getIntro(), 480));
                           }
                        
                        return null;
                    }
                 };
                return panel;
            }
        };
        add(panel);
        
        
        ExpandableReadPanel desc = new ExpandableReadPanel("description", getDescription());
        addOrReplace(desc);
        
        
		
		add(new GlobalTopPanel("top-panel", this.getSiteModel()));
		add(new GlobalFooterPanel<>("footer-panel"));
		
	}
    
	


	private IModel<String> getDescription() {

        if (getModel().getObject().getInfo()!=null)
            return new Model<String>(getModel().getObject().getInfo());
        
        if (getArtExhibitionModel().getInfo()!=null)
            return new Model<String>(getArtExhibitionModel().getInfo());
        
        return new Model<String>();
    }

    
    
    public IModel<SiteModel> getSiteModel() {
    	return this.siteModel;
    }
    
	protected SiteModel getSite() {
        try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);    
            return db.getClient().getSite(getArtExhibitionModel().getRefSite().getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	protected ArtExhibitionModel getArtExhibitionModel() {
        try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);    
            return db.getClient().getArtExhibition(getModel().getObject().getRefArtExhibition().getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected List<GuideContentModel> getGuideContents() {
	    try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);    
            return db.getClient().getArtExhibitionClientHandler().getClient().listArtExhibitionGuideContents(getModel().getObject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	  }

	
    protected ArtExhibitionGuideModel getArtExhibitionGuide(StringValue stringValue) {
	    try {
	        DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);    
	        Long id = Long.valueOf(stringValue.toLong());
	        return db.getClient().getArtExhibitionGuide(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void setModel(IModel<ArtExhibitionGuideModel> model) {
	    this.model=model;
    }

    @Override
	public void onDetach() {
	    super.onDetach();
	
	    if (model!=null)
	        model.detach();
	    
	    if (artExhibitionModel!=null)
	        artExhibitionModel.detach();
	    
	    if (siteModel!=null)
	        siteModel.detach();
	    
	}
	
	public IModel<ArtExhibitionGuideModel> getModel() {
	    return model;
	}
    
   protected boolean isAudioIntro() {
	       if (getModel().getObject().getRefAudioModel()!=null)
	           return true;

	       if (getArtExhibitionModel().getRefAudioModel()!=null)
	           return true;
	       
	       return false;
    }

    
}
