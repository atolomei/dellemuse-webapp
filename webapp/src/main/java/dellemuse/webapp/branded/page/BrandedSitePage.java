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
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import dellemuse.webapp.ServiceLocator;
import dellemuse.webapp.branded.global.BrandedGlobalFooterPanel;
import dellemuse.webapp.branded.global.BrandedGlobalTopPanel;
import dellemuse.webapp.db.DBService;
import dellemuse.webapp.global.GlobalFooterPanel;
import dellemuse.webapp.global.PageHeaderPanel;
import dellemuse.webapp.guide.GuideContentListItemPanel;
import dellemuse.webapp.page.site.ArtExhibitionGuidePage;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.struct.list.ListPanel;

/**
 * 
 * site foto Info - exhibitions
 * 
 */


@MountPath("/dem/site/${siteid}")
public class BrandedSitePage extends BrandedBaseSitePage {

    private static final long serialVersionUID = 1L;

    static private Logger logger = Logger.getLogger(BrandedSitePage.class.getName());

    private List<IModel<ArtExhibitionModel>> listPermanent;
    private List<IModel<ArtExhibitionModel>> listTemporary;
    private StringValue siteStringValue;

  
    
    public BrandedSitePage(PageParameters parameters) {
        super(parameters);
        siteStringValue = getPageParameters().get("siteid");
    }

    /**
     * @param model
     */
    public BrandedSitePage(IModel<SiteModel> model) {
        super(model);
        siteStringValue = null;
        super.getPageParameters().set("siteid", getSiteModel().getObject().getId());
    }

    /**
    protected List<IModel<?>> delleMuseFilter(List<IModel<?>> initialList, String filter) {
    	List<IModel<?>> list = new ArrayList<IModel<?>>();
    	final String str = filter.trim().toLowerCase();
    	initialList.forEach(
        		s -> {
		                if ( (s.getObject()) instanceof DelleMuseModelObject) {
		                		if ( ((DelleMuseModelObject) s.getObject()).
		                getDisplayname().toLowerCase().contains(str) ) {
		                	 list.add(s);
		                }
    				}
    	}
       );
       return list; 
    }
    **/
    
    
  
    
    /**
     * Exhibition ->  intro
     */
    @Override
    public void onInitialize() {
        super.onInitialize();
        
        if ( (getSiteModel()== null) && (siteStringValue!=null) && (!siteStringValue.isEmpty())) {
        	SiteModel site = getSite(siteStringValue);
            setSiteModel(new Model<SiteModel>(site));
        }

        
        this.siteId = getSiteModel().getObject().getId();
        
        add(new BrandedGlobalTopPanel("top-panel", getSiteModel()));
        add(new BrandedGlobalFooterPanel<>("footer-panel"));
   
        
        BreadCrumb<Void> bc = createBreadCrumb();
        bc.addElement( new HREFBCElement(breadCrumbPrefix()+"/site/" + getSiteModel().getObject().getId().toString(),
        			   getLabel("audioguides")));

        PageHeaderPanel<SiteModel> ph = new PageHeaderPanel<SiteModel>("page-header", getSiteModel(), getLabel("audioguides"));
        ph.setBreadCrumb(bc);
        add(ph);
        
        ListPanel<ArtExhibitionModel> panel = new ListPanel<>("exhibitionsPermanent", getArtExhibitionsPermanent()) {
        	
            private static final long serialVersionUID = 1L;
            
            protected List<IModel<ArtExhibitionModel>> filter(List<IModel<ArtExhibitionModel>> initialList, String filter) {
            	List<IModel<ArtExhibitionModel>> list = new ArrayList<IModel<ArtExhibitionModel>>();
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
            protected Panel getListItemPanel(IModel<ArtExhibitionModel> model) {
                GuideContentListItemPanel<ArtExhibitionModel> panel = new GuideContentListItemPanel<>("row-element", model) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected String getImageSrc(IModel<ArtExhibitionModel> model) {
                        if (model.getObject().getRefPhotoModel()!=null) {
                            return getPresignedThumbnailSmall(model.getObject().getRefPhotoModel());
                        }
                        return null;
                    }
                    
                    @Override
                    public void onClick() {
                        List<ArtExhibitionGuideModel> list = getArtExhibitionGuides(model);
                        if (list.size()>0)
                            setResponsePage(getArtExhibitionGuidePage(new Model<ArtExhibitionGuideModel>(list.get(0))));
                    }

                    protected IModel<String> getInfo(IModel<ArtExhibitionModel> model) {
                        String str = TextCleaner.clean(model.getObject().getIntro());
                        return new Model<String>(str);
                    }
                };
                return panel;
            }
        };
        add(panel);

        ListPanel<ArtExhibitionModel> collection = new ListPanel<>("exhibitionsTemporary", getArtExhibitionsTemporary()) {
            private static final long serialVersionUID = 1L;

            protected List<IModel<ArtExhibitionModel>> filter(List<IModel<ArtExhibitionModel>> initialList, String filter) {
            	List<IModel<ArtExhibitionModel>> list = new ArrayList<IModel<ArtExhibitionModel>>();
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
            protected Panel getListItemPanel(IModel<ArtExhibitionModel> model) {
                GuideContentListItemPanel<ArtExhibitionModel> panel = new GuideContentListItemPanel<>("row-element", model) {

                	private static final long serialVersionUID = 1L;
                    
                    @Override
                    public void onClick() {
                        List<ArtExhibitionGuideModel> list = getArtExhibitionGuides(model);
                        if (list.size()>0)
                            setResponsePage(getArtExhibitionGuidePage(new Model<ArtExhibitionGuideModel>(list.get(0))));
                        
                    }

                    protected IModel<String> getInfo(IModel<ArtExhibitionModel> model) {
                        String str = TextCleaner.clean(model.getObject().getIntro());
                        return new Model<String>(str);
                    }
                    
                    
                    @Override
                    protected String getImageSrc(IModel<ArtExhibitionModel> model) {
                        if (model.getObject().getRefPhotoModel()!=null) {
                            return getPresignedThumbnailSmall(model.getObject().getRefPhotoModel());
                        }
                        return null;
                    }
                    
                    
                };
                return panel;
            }
        };
        add(collection);

        
      }

    

   

    @Override
    public void onDetach() {
        super.onDetach();
        
        if (listPermanent!=null)
        	listPermanent.forEach( i -> i.detach());
    
        if (listTemporary!=null)
        	listTemporary.forEach( i -> i.detach());

    }
   
    protected List<ArtExhibitionGuideModel> getArtExhibitionGuides(IModel<ArtExhibitionModel> model) {
        try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
        	List<ArtExhibitionGuideModel> list = db.getClient().getArtExhibitionClientHandler().listArtExhibitionGuides(model.getObject());
        	mark("getArtExhibitionGuides -  " + model.getObject().getDisplayname() );
        	return list;
        	
    } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    private Long siteId;

    protected Long getSiteId() {
		Check.requireTrue(siteId!=null, "can not call getSiteId() before setting siteId attribute");
    	return siteId;
    }
    
    
    /**	protected SiteModel getSite(StringValue stringValue) {
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
*/
    private void loadLists() {
        try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
            
            listPermanent = new ArrayList<IModel<ArtExhibitionModel>>();
            listTemporary = new ArrayList<IModel<ArtExhibitionModel>>();
            
            List<ArtExhibitionModel> la = db.getClient().getSiteClientHandler().listArtExhibitionsBySite(getSiteModel().getObject());
            
            for (ArtExhibitionModel a: la) {
                if (a.isPermanent()) 
                    listPermanent.add( new Model<ArtExhibitionModel>(a));
                else
                    listTemporary.add( new Model<ArtExhibitionModel>(a));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        listPermanent.forEach( item -> logger.debug(item.getObject().getDisplayname()));
        logger.debug("");
        listTemporary.forEach( item -> logger.debug(item.getObject().getDisplayname()));
        logger.debug("");
        
        
    }
    
    private List<IModel<ArtExhibitionModel>> getArtExhibitionsPermanent() {
        if (listPermanent==null || listTemporary==null) {
            loadLists();
        }
        return listPermanent;
    }
    

    private List<IModel<ArtExhibitionModel>> getArtExhibitionsTemporary() {
        if (listPermanent==null || listTemporary==null) {
            loadLists();
        }
        return listTemporary;
    }

    
   
    
    

}


 