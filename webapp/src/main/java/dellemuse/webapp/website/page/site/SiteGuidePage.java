package dellemuse.webapp.website.page.site;

import java.util.ArrayList;
import java.util.List;

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
import dellemuse.webapp.ServiceLocator;
import dellemuse.webapp.component.global.GlobalFooterPanel;
import dellemuse.webapp.component.global.GlobalTopPanel;
import dellemuse.webapp.component.global.PageHeaderPanel;
import dellemuse.webapp.db.DBService;
import dellemuse.webapp.website.guide.GuideContentListItemPanel;
import dellemuse.webapp.website.page.BasePage;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.struct.list.ListPanel;

/**
 * 
 * site foto Info - exhibitions
 * 
 */


@MountPath("/siteguide/${id}")
public class SiteGuidePage extends BasePage {

    private static final long serialVersionUID = 1L;

    static private Logger logger = Logger.getLogger(SiteGuidePage.class.getName());

    private IModel<SiteModel> model;

    private StringValue stringValue;
    
    private List<IModel<ArtExhibitionModel>> listPermanent;
    private List<IModel<ArtExhibitionModel>> listTemporary;
    
    
    public SiteGuidePage(PageParameters parameters) {
        super(parameters);
        stringValue = getPageParameters().get("id");
    }

    /**
     * @param model
     */
    public SiteGuidePage(IModel<SiteModel> model) {
        super();
        this.model = model;
        super.getPageParameters().set("id", getModel().getObject().getId());
    }

    
    /**
     * 
     * Exhibition ->  intro
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     */
    @Override
    public void onInitialize() {
        super.onInitialize();

        if (stringValue != null) {
            SiteModel siteModel = getSite(stringValue);
            setModel(new Model<>(siteModel));
        }

        BreadCrumb<Void> bc = new BreadCrumb<>();
        bc.addElement(new HREFBCElement("/home", new Model<String>("Portada")));
        bc.addElement(new HREFBCElement("/siteguide/" + getModel().getObject().getId().toString(),
                                        new Model<String>(getModel().getObject().getDisplayname())));

        PageHeaderPanel<SiteModel> ph = new PageHeaderPanel<SiteModel>("page-header", getModel());
        ph.setBreadCrumb(bc);
        add(ph);

        Label welcomeTitle = new Label("welcomeTitle", new Model<String>("Bienvenido al " + getModel().getObject().getShortName()));
        add(welcomeTitle);
        IModel<String> w = getModel().getObject().getIntro() !=null ? new Model<String>( TextCleaner.clean(getModel().getObject().getIntro())): null;
        Label welcome = new Label("welcome", w!=null ? w : new Model<String>(""));
        
        welcome.setEscapeModelStrings(false);
        welcome.setVisible(w!=null);
        add(welcome);
        
        Label opens = new Label("opens", new Model<String>(TextCleaner.clean(getModel().getObject().getOpens())));
        opens.setEscapeModelStrings(false);
        add(opens);


        
        //List<ArtExhibitionModel> sites = getArtExhibitions();
        //List<IModel<ArtExhibitionModel>> list = new ArrayList<IModel<ArtExhibitionModel>>();
        //sites.forEach(s -> list.add(new Model<ArtExhibitionModel>(s)));
        

        ListPanel<ArtExhibitionModel> panel = new ListPanel<>("exhibitionsPermanent", getArtExhibitionsPermanent()) {
            private static final long serialVersionUID = 1L;
            @Override
            protected Panel getListItemPanel(IModel<ArtExhibitionModel> model) {
                GuideContentListItemPanel<ArtExhibitionModel> panel = new GuideContentListItemPanel<>("row-element", model) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected String getImageSrc(IModel<ArtExhibitionModel> model) {
                        if (model.getObject().getPhotoModel()!=null) {
                            return getPresignedThumbnailSmall(model.getObject().getPhotoModel());
                        }
                        return null;
                    }
                    
                    @Override
                    public void onClick() {
                        List<ArtExhibitionGuideModel> list = getArtExhibitionGuides(model);
                        if (list.size()>0)
                            setResponsePage(new ArtExhibitionGuidePage(new Model<ArtExhibitionGuideModel>(list.get(0))));
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

            @Override
            protected Panel getListItemPanel(IModel<ArtExhibitionModel> model) {
                GuideContentListItemPanel<ArtExhibitionModel> panel = new GuideContentListItemPanel<>("row-element", model) {
                    private static final long serialVersionUID = 1L;

                    
                    @Override
                    public void onClick() {
                        //setResponsePage(new ArtExhibitionPage(model));
                        List<ArtExhibitionGuideModel> list = getArtExhibitionGuides(model);
                        if (list.size()>0)
                            setResponsePage(new ArtExhibitionGuidePage(new Model<ArtExhibitionGuideModel>(list.get(0))));
                        
                    }

                    protected IModel<String> getInfo(IModel<ArtExhibitionModel> model) {
                        String str = TextCleaner.clean(model.getObject().getIntro());
                        return new Model<String>(str);
                    }
                    
                    
                    @Override
                    protected String getImageSrc(IModel<ArtExhibitionModel> model) {
                        if (model.getObject().getPhotoModel()!=null) {
                            return getPresignedThumbnailSmall(model.getObject().getPhotoModel());
                        }
                        return null;
                    }
                    
                    
                };
                return panel;
            }
        };
        add(collection);

        Link<SiteModel> map = new Link<SiteModel>("map", getModel()) {
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick() {
                    setResponsePage( new RedirectPage(getModel().getObject().getMapurl()));
            }
        };
        add(map);
        

        
        
        Label address = new Label("address", new Model<String>( TextCleaner.clean(getModel().getObject().getAddress())));
        address.setEscapeModelStrings(false);
        add(address);
        
        
        
        
        
        
        /**
        SimpleGridPanel<ArtExhibitionModel> news = new SimpleGridPanel<>("news", list) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Panel getListItemPanel(IModel<ArtExhibitionModel> model) {
                GridSimpleItemPanel<ArtExhibitionModel> panel = new GridSimpleItemPanel<>("row-element", model) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        setResponsePage(new ArtExhibitionPage(model));
                    }

                    protected IModel<String> getInfo(IModel<ArtExhibitionModel> model) {
                        String str = TextCleaner.clean(model.getObject().getInfo())
                                + TextCleaner.clean(model.getObject().getLocation());
                        return new Model<String>(str);
                    }
                };
                return panel;
            }
        };
        add(news);
        **/
        
        add(new GlobalTopPanel<>("top-panel"));
        add(new GlobalFooterPanel<>("footer-panel"));
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
        this.model = model;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (model != null)
            model.detach();
    }

    public IModel<SiteModel> getModel() {
        return model;
    }


    private void loadLists() {
        try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
            
            listPermanent = new ArrayList<IModel<ArtExhibitionModel>>();
            listTemporary = new ArrayList<IModel<ArtExhibitionModel>>();
            
            List<ArtExhibitionModel> la = db.getClient().getSiteClientHandler().listArtExhibitionsBySite(getModel().getObject());
            
            for (ArtExhibitionModel a: la) {
                if (a.isPermanent()) 
                    listPermanent.add( new Model<ArtExhibitionModel>(a));
                else
                    listTemporary.add( new Model<ArtExhibitionModel>(a));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
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

    
    
    protected List<ArtExhibitionGuideModel> getArtExhibitionGuides(IModel<ArtExhibitionModel> model) {
        try {
            DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
            return db.getClient().getArtExhibitionClientHandler().listArtExhibitionGuides(model.getObject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    

}
