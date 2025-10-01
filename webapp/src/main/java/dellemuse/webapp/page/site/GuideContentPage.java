package dellemuse.webapp.page.site;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Site;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.media.audio.Audio;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionItemModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.GuideContentModel;
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
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.listNavigator.ListNavigator;
import io.wktui.struct.list.ListPanel;
import io.wktui.text.ExpandableReadPanel;
import wktui.base.InvisiblePanel;

/**
 * site foto Info - exhibitions
 */

@MountPath("/content/${id}")
public class GuideContentPage extends BaseSitePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(GuideContentPage.class.getName());

	private IModel<GuideContentModel> model;
	private IModel<ArtExhibitionGuideModel> artExhibitionGuideModel;
	private IModel<ArtWorkModel> artWorkModel;
	private List<IModel<GuideContentModel>> list;

	private Link<GuideContentModel> imageLink;
	private Image image;
	private WebMarkupContainer imageContainer;
	private int current = 0;

	private StringValue stringValue;

	public GuideContentPage(PageParameters parameters) {
		super(parameters);
		stringValue = getPageParameters().get("id");
	}

	/**
	 * 
	 * @param model
	 */
	public GuideContentPage(IModel<GuideContentModel> model, List<IModel<GuideContentModel>> list) {
		super();
		this.model = model;
		setList(list);
		if (list != null) {
			int n = 0;
			for (IModel<GuideContentModel> m : list) {
				if (model.getObject().getId().equals(m.getObject().getId())) {
					current = n;
					break;
				}
				n++;
			}
		}
		super.getPageParameters().set("id", getModel().getObject().getId());
	}

	public  GuideContentPage(IModel<GuideContentModel> model, 
			List<IModel<GuideContentModel>> list,
			IModel<ArtExhibitionGuideModel> artExhibitionGuideModel) {
		super();
		this.model = model;
		setList(list);

		if (list != null) {
			int n = 0;
			for (IModel<GuideContentModel> m : list) {
				if (model.getObject().getId().equals(m.getObject().getId())) {
					current = n;
					break;
				}
				n++;
			}
		}

		this.artExhibitionGuideModel = artExhibitionGuideModel;
		super.getPageParameters().set("id", getModel().getObject().getId());
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		if (this.stringValue != null) {
			GuideContentModel siteModel = getGuideContent(this.stringValue);
			setModel(new Model<>(siteModel));
		}

		setSiteModel(new Model<SiteModel>(getSite()));
		
		
		BreadCrumb<Void> bc = createBreadCrumb();
		
		bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(),
				new Model<String>(getSiteModel().getObject().getDisplayname())));

		bc.addElement(new HREFBCElement("/guide/" + getArtExhibitionGuideModel().getObject().getId().toString(),
				new Model<String>(getArtExhibitionGuideModel().getObject().getDisplayname())));

		bc.addElement(new BCElement(new Model<String>(getModel().getObject().getDisplayname())));

		PageHeaderPanel<GuideContentModel> ph = new PageHeaderPanel<>("page-header", getModel());

		if (getArtWorkModel() != null) {
			StringBuilder info = new StringBuilder();
			int n = 0;
			for (RefPersonModel p : getArtWorkModel().getObject().getRefArtists()) {
				if (n++ > 0)
					info.append(", ");
				info.append(p.getDisplayname());
			}
			ph.setTagline(new Model<String>(info.toString()));
		}

		
		if (isInfoGral()) {
			Label infoGral = new Label("infoGral", getInfoGral());
			infoGral.setEscapeModelStrings(false);
			addOrReplace(infoGral);
		} else {
			addOrReplace(new InvisiblePanel("infoGral"));
		}

		ph.setBreadCrumb(bc);
		addOrReplace(ph);

		add(new GlobalTopPanel("top-panel", getSiteModel()));
		add(new GlobalFooterPanel<GuideContentModel>("footer-panel", getModel()));

		if (getArtWorkModel() != null) {
			ExpandableReadPanel infoPanel = new ExpandableReadPanel("info",
					new Model<String>(getArtWorkModel().getObject().getInfo()));
			addOrReplace(infoPanel);
		} else {
			addOrReplace(new InvisiblePanel("info"));
		}

		if (getList() != null) {
			ListNavigator<GuideContentModel> list = new ListNavigator<GuideContentModel>("navigator", this.current,
					getList()) {
				private static final long serialVersionUID = 1L;

				@Override
				protected IModel<String> getLabel(IModel<GuideContentModel> model) {
					return new Model<String>(model.getObject().getDisplayname());
				}

				@Override
				protected void navigate(int current) {
					setResponsePage(new GuideContentPage(getList().get(current), getList()));
				}
			};
			add(list);
		} else {
			add(new InvisiblePanel("navigator"));
		}

		this.imageContainer = new WebMarkupContainer("imageContainer");
		this.imageContainer.setVisible(getArtWorkModel().getObject().getRefPhotoModel() != null);
		addOrReplace(this.imageContainer);

		this.imageLink = new Link<>("image-link", getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				GuideContentPage.this.onImageClick(getModel());
			}
		};
		this.imageContainer.add(this.imageLink);

		if (getImageSrc(getModel()) != null) {
			Url url = Url.parse(getImageSrc(getModel()));
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			this.image = new Image("image", resourceReference);
			this.imageLink.addOrReplace(this.image);
		} else {
			this.image = new Image("image", new UrlResourceReference(Url.parse("")));
			this.image.setVisible(false);
			this.imageLink.addOrReplace(image);
		}

		WebMarkupContainer audioContainer = new WebMarkupContainer("audioContainer");
		addOrReplace(audioContainer);
		audioContainer.setVisible(isAudio());

		if (isAudio()) {
			RefResourceModel au = getModel().getObject().getRefAudioModel() != null ? getModel().getObject().getRefAudioModel()
					: this.getModel().getObject().getRefAudioModel();
			String as = getPresignedUrl(au);
			Url url = Url.parse(as);
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			Audio audio = new Audio("audio", resourceReference);
			audioContainer.add(audio);
		} else {
			audioContainer.addOrReplace(new InvisiblePanel("intro-audio"));
		}

	}



	public void setList(List<IModel<GuideContentModel>> list) {
		this.list = list;
	}

	public List<IModel<GuideContentModel>> getList() {
		return list;
	}


	public IModel<GuideContentModel> getModel() {
		return model;
	}

	/**
	 * @param model
	 */
	public void setModel(IModel<GuideContentModel> model) {
		this.model = model;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (model != null)
			model.detach();

		if (artExhibitionGuideModel != null)
			artExhibitionGuideModel.detach();

		if (artWorkModel != null)
			artWorkModel.detach();

		if (list != null)
			list.forEach(t -> t.detach());
	}

	private boolean isInfoGral() {
		return getArtWorkModel()!=null &&
				getArtWorkModel().getObject()!=null && 
				getArtWorkModel().getObject().getId()!=null ||
				getArtWorkModel().getObject().getSpec()!=null;
	}

	private String getInfoGral() {
		return  TextCleaner.clean(getArtWorkModel().getObject().getSpec()+" <br/>" + " id: " + getArtWorkModel().getObject().getId().toString());
	}

	/**
	 * 
	 * 
	 * @param stringValue
	 * @return
	 */
	protected GuideContentModel getGuideContent(StringValue stringValue) {
		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			Long id = Long.valueOf(stringValue.toLong());
			return db.getClient().getGuideContentClientHandler().get(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 * @return
	 */
	protected IModel<ArtExhibitionGuideModel> getArtExhibitionGuideModel() {

		if (this.artExhibitionGuideModel == null) {
			try {
				DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
				this.artExhibitionGuideModel = new Model<ArtExhibitionGuideModel>(db.getClient()
						.getArtExhibitionGuide(getModel().getObject().getRefArtExhibitionGuide().getId()));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return this.artExhibitionGuideModel;
	}

	/**
	 * 
	 * 
	 * 
	 * @return
	 */
	protected SiteModel getSite() {
		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);

			ArtExhibitionGuideModel guide = getArtExhibitionGuideModel().getObject();
			ArtExhibitionModel ae = db.getClient().getArtExhibition(guide.getRefArtExhibition().getId());

			SiteModel site = db.getClient().getSite(ae.getRefSite().getId());

			return site;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected IModel<ArtWorkModel> getArtWorkModel() {

		if (this.artWorkModel == null) {
			try {
				DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
				ArtExhibitionItemModel ae = db.getClient()
						.getArtExhibitionItem(getModel().getObject().getArtExhibitionItem().getId());
				this.artWorkModel = new Model<ArtWorkModel>(db.getClient().getArtWork(ae.getRefArtWork().getId()));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return this.artWorkModel;
	}

	protected void onImageClick(IModel<GuideContentModel> guideModel) {
		setResponsePage(new ArtWorkImagePage(
				new Model<>(getArtWork(guideModel.getObject())), 
						getPageUrl()));
	}

	
	protected String getPageUrl() {
		return "/content/${id}".replace("${id}", getModel().getObject().getId().toString());
    }
	
	private String getImageSrc(IModel<GuideContentModel> model) {
		DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
		try {
			if (getArtWorkModel().getObject().getRefPhotoModel() == null)
				return null;
			return db.getClient().getPresignedThumbnailUrl(getArtWorkModel().getObject().getRefPhotoModel().getId(),
					ThumbnailSize.MEDIUM);
		} catch (DelleMuseClientException e) {
			logger.error(e);
			return null;
		}
	}

	private boolean isAudio() {
		return getModel().getObject().getRefAudioModel() != null;
	}

}
