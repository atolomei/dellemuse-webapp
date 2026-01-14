package dellemuse.webapp.branded.page;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.media.audio.Audio;
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
import dellemuse.model.GuideContentModel;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.ref.RefPersonModel;
import dellemuse.model.ref.RefResourceModel;
import dellemuse.model.util.Check;
import dellemuse.webapp.branded.global.BrandedGlobalFooterPanel;
import dellemuse.webapp.branded.global.BrandedGlobalTopPanel;
import dellemuse.webapp.global.PageHeaderPanel;
import dellemuse.webapp.guide.GuideContentListItemPanel;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.struct.list.ListPanel;
import io.wktui.text.ExpandableReadPanel;
import wktui.base.InvisiblePanel;


/**
 * <p>Pagina para la versión brandeada
 * </p>
 * Site 
 * Foto 
 * Info - exhibitions
 */

@MountPath("/dem/guide/${id}")
public class BrandedArtExhibitionGuidePage extends BrandedBaseSitePage {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(BrandedArtExhibitionGuidePage.class.getName());

	private IModel<ArtExhibitionGuideModel> model;
	private IModel<ArtExhibitionModel> artExhibitionModel;
	private Boolean isArtExhibitionGuideInfo;
    private List<IModel<GuideContentModel>> guideContentsModel;
	private StringValue guideStringValue;
    private Boolean isAudioIntro;

 	
	public  BrandedArtExhibitionGuidePage(PageParameters parameters) {
		 super(parameters);
		 guideStringValue = getPageParameters().get("id");
	 }

	/**
	 * @param model
	 */
	public BrandedArtExhibitionGuidePage(IModel<ArtExhibitionGuideModel> model) {
		 super();
		 this.model=model;
		 this.guideStringValue = null;
		 super.getPageParameters().set("id", getModel().getObject().getId());
	}
	
	  
	/**
	 * <p>This must be called after setting the Model</p>
	 */
	@Override
	protected Long getSiteId() {
		Check.requireTrue(getModel()!=null && getModel().getObject()!=null, "can not call getSiteId() before setting the Page Model");
		return getArtExhibitionModel().getObject().getRefSite().getId();
	}
	
	/**
	 *  Exhibition ->  intro
	 */
	@Override
	public void onInitialize() {
		super.onInitialize();

		if ( (getModel()==null) && (this.guideStringValue!=null) && (!this.guideStringValue.isEmpty())) {
		    ArtExhibitionGuideModel aemodel = getArtExhibitionGuide(guideStringValue);
		    setModel(new Model<ArtExhibitionGuideModel>(aemodel));
		}
		setSiteModel(new Model<SiteModel>(getSite()));	      
		
		add(new BrandedGlobalTopPanel("top-panel", this.getSiteModel()));
		add(new BrandedGlobalFooterPanel<>("footer-panel"));

		/** page header */
        BreadCrumb<Void> bc = createBreadCrumb();
        bc.addElement(getSiteHomeElement());
        bc.addElement(new BCElement(new Model<String>( getModel().getObject().getDisplayname())));
		PageHeaderPanel<ArtExhibitionGuideModel> ph = new PageHeaderPanel<ArtExhibitionGuideModel>("page-header", getModel());
		ph.setBreadCrumb(bc);
		add(ph);
		
		/** Intro **/
		IModel<String> intro = getArtExhibitionGuideIntro();
		add(((new Label("intro", intro)).setEscapeModelStrings(false)));
		
		/** website **/
		WebMarkupContainer moreinfoContainer = new WebMarkupContainer("moreinfoContainer");
		add(moreinfoContainer);
		Link<Void> moreInfoLink = new Link<Void>("moreinfoLink") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage( new RedirectPage( getArtExhibitionModel().getObject().getWebsite() ));
			}
		};
		moreinfoContainer.add(moreInfoLink);
		String moreinfo = getArtExhibitionModel().getObject().getWebsite()!=null ? getArtExhibitionModel().getObject().getWebsite() : "";
		moreInfoLink.add( (new Label("moreinfo", new Model<String>((moreinfo)))).setEscapeModelStrings(true));
		moreinfoContainer.setVisible(moreinfo!=null && moreinfo.length()>0);
		
		/**intro  audio guide */ 
		
		WebMarkupContainer audioContainer = new WebMarkupContainer("audioContainer");
	    addOrReplace(audioContainer);
		if (isAudioIntro()) {
		    WebMarkupContainer audioIntroContainer = new WebMarkupContainer("intro-audio");
		    audioContainer.add(audioIntroContainer);
	        RefResourceModel au= 	getModel().getObject().getRefAudioModel() != null ? 
	        						getModel().getObject().getRefAudioModel() : 
	        						getArtExhibitionModel().getObject().getRefAudioModel();
	        String as =  getPresignedUrl(au);
	        Url url = Url.parse(as);
            UrlResourceReference resourceReference = new UrlResourceReference(url);
	        Audio audio = new Audio("audioIntro", resourceReference);
	        audioIntroContainer.add(audio);
		}
		else {
		    audioContainer.addOrReplace(new InvisiblePanel("intro-audio"));
		}
	    audioContainer.setVisible(isAudioIntro());

	    
	    /** description */

		WebMarkupContainer descContainer = new WebMarkupContainer("descriptionContainer");
	    addOrReplace(descContainer);
        ExpandableReadPanel desc = new ExpandableReadPanel("description", getArtExhibitionGuideInfo());
        descContainer.add(desc);
        descContainer.setVisible(isArtExhibitionGuideInfo());

        
        /** guide contents */
        guideContentsModel = new ArrayList<IModel<GuideContentModel>>();
        guideContentsModel.sort(new Comparator<IModel<GuideContentModel>>() {
            @Override
            public int compare(IModel<GuideContentModel> o1,IModel<GuideContentModel> o2) {
                
            	if (o1.getObject().getGuideOrder()==o2.getObject().getGuideOrder())
            		return o1.getObject().getDisplayname().compareToIgnoreCase(o2.getObject().getDisplayname());
            	
            	return o1.getObject().getGuideOrder()<o2.getObject().getGuideOrder() ? -1 : 1;
            }
        });
        
        getGuideContents().forEach(s -> guideContentsModel.add(new Model<GuideContentModel>(s)));

        
        ListPanel<GuideContentModel> listPanel = new ListPanel<>("contents", guideContentsModel) {
        	private static final long serialVersionUID = 1L;
        	@Override
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
        	@Override
            protected Panel getListItemPanel(IModel<GuideContentModel> model) {
        		
                GuideContentListItemPanel<GuideContentModel> panel = new GuideContentListItemPanel<>("row-element", model) {
                
                	private static final long serialVersionUID = 1L;
                    @Override
                    public void onClick() {
                       setResponsePage(getGuideContentPage(model, getItems()));
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
                    	IModel<String> info=getGuideContentInfo( model);
                    	return info;
                    }
                 };
                return panel;
            }
        };
        add(listPanel);
   }
    
	/**
	 * 
	 * @return
	 */
	protected IModel<String> getArtExhibitionGuideIntro() {
		return getArtExhibitionModel().getObject().getIntro()!=null ? 
				new Model<String>( TextCleaner.clean(getArtExhibitionModel().getObject().getIntro() ) ): 
					new Model<String>("");	
	}

	/**
	 * 
	 * @return
	 */
	protected boolean isArtExhibitionGuideInfo() {
		
		if (isArtExhibitionGuideInfo==null)
			isArtExhibitionGuideInfo = Boolean.valueOf( 
			getArtExhibitionGuideInfo()!=null && 
			getArtExhibitionGuideInfo().getObject()!=null &&
			getArtExhibitionGuideInfo().getObject().length()>0);
		
		return this.isArtExhibitionGuideInfo.booleanValue();
	}

	/**
	 * 
	 * @return
	 */
	protected IModel<ArtExhibitionModel> getArtExhibitionModel() {
		if (this.artExhibitionModel==null) {
			this.artExhibitionModel = new Model<ArtExhibitionModel>(getArtExhibition());
		}
		return this.artExhibitionModel;
	}


	protected IModel<String> getGuideContentInfo(IModel<GuideContentModel> model) {
	  // StringBuilder info = new StringBuilder();
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
      if (aw!=null) {
      	if (aw.getIntro()!=null) {                                
      		return new Model<String>(TextCleaner.clean(aw.getIntro(), 320));
         }
      	else if (aw.getInfo()!=null) {
      		return new Model<String>(TextCleaner.clean(aw.getInfo(), 320));
      	}
      }
      return null;
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
	    
	    if (guideContentsModel!=null)
	    	guideContentsModel.forEach( i-> i.detach());
	    
	}
	
	public IModel<ArtExhibitionGuideModel> getModel() {
	    return model;
	}

	
	protected boolean isAudioIntro() {
		   if (isAudioIntro==null) {
		       if (getModel().getObject().getRefAudioModel()!=null)
		    	   isAudioIntro = Boolean.valueOf(true);

		       else if (getArtExhibition().getRefAudioModel()!=null)
		    	   isAudioIntro = Boolean.valueOf(true);
		       
		       else
		    	   isAudioIntro =  Boolean.valueOf(false);
		   }
		       return isAudioIntro.booleanValue();
	}
	
	
	protected ArtExhibitionModel getArtExhibition() {
      	return getArtExhibition(getModel().getObject().getRefArtExhibition().getId());
	}

    protected List<GuideContentModel> getGuideContents() {
	
    	return getGuideContents(getModel().getObject().getId());

    	/**
    	try {
            DBSer vice db = (DBServ ice) ServiceLocator.getInstance().getBean(DBService.class);    
            List<GuideContentModel> list = db.getClient().getArtExhibitionClientHandler().getClient().listArtExhibitionGuideContents(getModel().getObject());
            mark(getModel().getObject().getClass().getSimpleName()+"-"+getModel().getObject().getDisplayname()+"-guideContents");
           return list;
	    } catch (Exception e) {
            throw new RuntimeException(e);
        }
        **/
	  }

	
 
   protected ArtExhibitionGuideModel getArtExhibitionGuide(StringValue stringValue) {

	 return super.getArtExhibitionGuide(stringValue);
	 
	 /**
	 try {
	        DBSe rvice db = (DBSer vice) ServiceLocator.getInstance().getBean(DBService.class);    
	        Long id = Long.valueOf(stringValue.toLong());
	        ArtExhibitionGuideModel aeg=db.getClient().getArtExhibitionGuide(id);
	        mark(aeg);
	        return aeg;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    **/
    }
 
	/**
	 * Guide
	 * ArtExhibition
	 * 
	 * @return
	 */
	private IModel<String> getArtExhibitionGuideInfo() {

     if (getModel().getObject().getInfo()!=null)
         return new Model<String>(getModel().getObject().getInfo());
     
     if (getArtExhibitionModel().getObject().getInfo()!=null)
         return new Model<String>(getArtExhibitionModel().getObject().getInfo());
     
     return new Model<String>("");
	
	}


 
}
