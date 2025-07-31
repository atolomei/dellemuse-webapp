package dellemuse.webapp.page;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.MetaDataHeaderItem;
import org.apache.wicket.markup.head.PriorityHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.string.StringValue;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionItemModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.DelleMuseModelObject;
import dellemuse.model.GuideContentModel;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.ref.RefResourceModel;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.webapp.ServiceLocator;
import dellemuse.webapp.db.DBModelService;
import dellemuse.webapp.db.DBService;
import dellemuse.webapp.icons.FontAwesome;
import dellemuse.webapp.page.site.ArtExhibitionGuidePage;
import dellemuse.webapp.page.site.ArtWorkImagePage;
import dellemuse.webapp.page.site.GuideContentPage;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.BreadcrumbBasePanel;
import io.wktui.nav.breadcrumb.HREFBCElement;
import wktui.bootstrap.Bootstrap;

public abstract class BasePage extends WebPage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(BasePage.class.getName());

	static private IModel<String> model_xtitle;
	static private String xtitle;
	static private IModel<String> xdescription;
	static private String xfavicon;
	static private String xlanguage;
	static private String xrobots;
	static private String xrating;
	static private String xkeywords = "dellemuse web";

	private static final String XUA_Compatible = "IE=Edge";

	private Map<String, Integer> serverCall;

	// 1 Day
	static private final int COOKIE_DURATION = 86400 * 1;

	// public static JavaScriptResourceReference
	// getJavaScriptPopperResourceReference() {
	// return new JavaScriptResourceReference(BasePage.class,"popper.min.js");
	// }

	public static JavaScriptResourceReference getJavaScriptKbeeResourceReference() {
		return new JavaScriptResourceReference(BasePage.class, "kbee.js");
	}

	static {
		// xfavicon =
		// PropertiesFactory.getInstance("kbee").getProperties().getProperty("com.novamens.content.web.favicon",
		// "/images/favicon.gif");
		xlanguage = "English";
		xrobots = "NOINDEX, NOFOLLOW";
		xrating = "General";
	}

	// private static final String
	// DEFAULT_LATO_FONTS="https://fonts.googleapis.com/css2?family=Lato:ital,wght@0,400;0,900;1,400&display=swap";

	private static final ResourceReference BOOTSTRAP_CSS = Bootstrap.getCssResourceReference();
	private static final ResourceReference BOOTSTRAP_JS = Bootstrap.getJavaScriptResourceReference();

	// private static final ResourceReference POPPER_JS =
	// BasePage.getJavaScriptPopperResourceReference();
	private static final ResourceReference KBEE_JS = BasePage.getJavaScriptKbeeResourceReference();

	// private static final ResourceReference AW = new
	// CssResourceReference(BasePage.class, "./all.min.css");
	// private static final ResourceReference FONT_AWESOME_CSS = new
	// CssResourceReference(BasePage.class, "./dellemuse.css");

	private static final ResourceReference CSS = new CssResourceReference(BasePage.class, "./dellemuse.css");

	private String keywords = xkeywords;
	private String language = xlanguage;

	private String xuacompatible; // = "IE=8"; // IE=edge
	private String robots = xrobots;
	private String fonts = null;
	private String favicon = null;

	private ResourceReference rcss;

	private WebMarkupContainer wfont;
	private WebMarkupContainer wcss;
	private WebMarkupContainer fvicon;
	private WebMarkupContainer vp;
	private WebMarkupContainer desc;
	private WebMarkupContainer lang;
	private WebMarkupContainer kw;

	// private boolean initialized = false;

	Map<Long, IModel<ArtExhibitionItemModel>> cacheArtExhibitionItem = new HashMap<Long, IModel<ArtExhibitionItemModel>>();

	public BasePage() {
		super();
	}

	public BasePage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (cacheArtExhibitionItem != null)
			cacheArtExhibitionItem.forEach((k, v) -> v.detach());
	}

	public String breadCrumbPrefix() {
		return "";
	}

	public BreadCrumb<Void> createBreadCrumb() {
		BreadCrumb<Void> bc = new BreadCrumb<>();
		bc.addElement(new HREFBCElement("/home", getLabel("home")));
		return bc;
	}

	public void setFonts(String fonts) {
		this.fonts = fonts;
	}

	public String getServerUrl() {
		String protocol = ((WebRequest) RequestCycle.get().getRequest()).getUrl().getProtocol();
		String host = ((WebRequest) RequestCycle.get().getRequest()).getUrl().getHost();
		Integer iport = ((WebRequest) RequestCycle.get().getRequest()).getUrl().getPort();
		String port = (iport.equals(80) || iport.equals(443) ? "" : (":" + iport.toString()));
		return protocol + "://" + host + port;
	}

	@Override
	public void onAfterRender() {
		super.onAfterRender();
		cacheArtExhibitionItem.clear();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		cacheArtExhibitionItem = new HashMap<Long, IModel<ArtExhibitionItemModel>>();

		if (logger.isDebugEnabled())
			serverCall = new HashMap<String, Integer>();

		// ()!=null? getSession().getLocale().getLanguage() : null;

		this.language = Locale.forLanguageTag("es").getLanguage();

		if (getSession() != null)
			getSession().setLocale(Locale.forLanguageTag(this.language));

		this.wfont = new WebMarkupContainer("google-font");
		this.wfont.add(new AttributeModifier("rel", "stylesheet"));
		this.wfont.add(new AttributeModifier("href", getPageFonts()));
		this.wfont.setVisible(getPageFonts() != null);
		add(this.wfont);

		this.fvicon = new WebMarkupContainer("favicon");
		this.fvicon.add(new AttributeModifier("rel", "icon"));
		this.fvicon.add(new AttributeModifier("type", "image/x-icon"));
		this.fvicon.add(new AttributeModifier("href", getFavicon()));
		add(this.fvicon);

		this.lang = new WebMarkupContainer("language");
		this.lang.add(new AttributeModifier("name", "language"));
		this.lang.add(new AttributeModifier("language", new Model<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				// return getPageLanguage();
				return xlanguage;
			}
		}));

		add(this.lang);

		this.kw = new WebMarkupContainer("keywords");
		this.kw.add(new AttributeModifier("name", "keywords"));
		this.kw.add(new AttributeModifier("content", new Model<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return getPageKeywords();
			}
		}));
		add(this.kw);

		this.desc = new WebMarkupContainer("header-description");
		this.desc.add(new AttributeModifier("name", "description"));
		// this.desc.add(new AttributeModifier("content", getPageDescription()));
		this.desc.add(new AttributeModifier("name", "viewport"));
		this.desc.add(new AttributeModifier("content", new Model<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				// return "width=device-width, initial-scale=1.0, minimum-scale=1.0,
				// user-scalable=yes";
				return "width=device-width, initial-scale=1, shrink-to-fit=no";

			}
		}));
		add(this.desc);

		WebMarkupContainer rating = new WebMarkupContainer("rating");
		rating.add(new AttributeModifier("name", "rating"));
		rating.add(new AttributeModifier("content", xrating));
		add(rating);

		WebMarkupContainer wrobots = new WebMarkupContainer("robots");
		wrobots.add(new AttributeModifier("name", "robots"));
		wrobots.add(new AttributeModifier("content", new Model<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return getPageRobots();
			}
		}));

		add(wrobots);

		setPageKeywords(keywords);

		this.wcss = new WebMarkupContainer("css");
		this.wcss.setVisible(false);
		add(this.wcss);

		this.vp = new WebMarkupContainer("viewport");
		add(vp);

		if (XUA_Compatible != null)
			setPageXUACompatible(XUA_Compatible);

	}

	@Override
	public void onBeforeRender() {
		super.onBeforeRender();
		// this.initialized=true;
		getServerCall().forEach((k, v) -> logger.debug(k + " -> " + v.toString()));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		if (getPageXUACompatible() != null) {
			MetaDataHeaderItem headerItem = new MetaDataHeaderItem("meta");
			headerItem.addTagAttribute("http-equiv", "X-UA-Compatible");
			headerItem.addTagAttribute("content", getPageXUACompatible());

			response.render(new PriorityHeaderItem(headerItem));

			headerItem = new MetaDataHeaderItem("meta");
			headerItem.addTagAttribute("http-equiv", "Content-Security-Policy");
			headerItem.addTagAttribute("content", "img-src *");

			response.render(new PriorityHeaderItem(headerItem));
		}

		// response.render(JavaScriptHeaderItem.forUrl("popper.min.js"));
		// response.render(JavaScriptHeaderItem.forReference(POPPER_JS));
		response.render(JavaScriptHeaderItem.forReference(KBEE_JS));

		response.render(JavaScriptHeaderItem
				.forReference(getApplication().getJavaScriptLibrarySettings().getJQueryReference()));
		response.render(JavaScriptHeaderItem
				.forReference(getApplication().getJavaScriptLibrarySettings().getWicketAjaxReference()));

		response.render(CssHeaderItem.forReference(BOOTSTRAP_CSS));
		response.render(JavaScriptHeaderItem.forReference(BOOTSTRAP_JS));

		// response.render(CssHeaderItem.forReference(AW));

		response.render(CssHeaderItem.forReference(CSS));

		// response.render(JavaScriptHeaderItem.forReference(NotyJSReference.INSTANCE));
		// response.render(JavaScriptHeaderItem.forReference(NotyPackagedJSReference.INSTANCE));
		// response.render(JavaScriptHeaderItem.forReference(NotyThemeBootstrapJSReference.INSTANCE));

		if (getCssResource() != null)
			response.render(CssHeaderItem.forReference(getCssResource()));

		// if (!hasLateralMenu()) {
		// response.render(OnDomReadyHeaderItem.forScript("$('body').removeClass('sidebar-xs');"));
		// response.render(OnDomReadyHeaderItem.forScript("$('body').addClass('nosidebar');"));
		// }

	}

	protected void setPageFonts(String s) {
		fonts = s;
	}

	protected String getPageFonts() {
		return fonts;
	}

	protected void setPageRobots(String s) {
		robots = s;
	}

	protected String getPageRobots() {
		return robots;
	}

	protected void setPageXUACompatible(String uax) {
		this.xuacompatible = uax;
	}

	protected String getPageXUACompatible() {
		return xuacompatible;
	}

	protected void setPageLanguage(String language) {
		this.language = language;
	}

	protected String getPageLanguage() {
		return this.language;
	}

	protected void setFavicon(String desc) {
		this.favicon = desc;
	}

	protected String getFavicon() {
		return favicon != null ? favicon : xfavicon;
	}

	protected void setPageKeywords(String desc) {
		this.keywords = desc;
	}

	protected String getPageKeywords() {
		return keywords;
	}

	protected void setCss(ResourceReference rcss) {
		this.rcss = rcss;
	}

	protected ResourceReference getCssResource() {
		return rcss;
	}

	protected void mark(DelleMuseModelObject obj) {
		if (obj == null)
			return;
		mark(obj.getClass().getSimpleName() + "-" + obj.getDisplayname());
	}

	protected void mark(String apiCall) {
		if (!logger.isDebugEnabled())
			return;

		if (!serverCall.containsKey(apiCall))
			serverCall.put(apiCall, Integer.valueOf(0));

		serverCall.put(apiCall, Integer.valueOf((serverCall.get(apiCall).intValue() + 1)));
	}

	protected Map<String, Integer> getServerCall() {
		if (serverCall == null)
			serverCall = new HashMap<String, Integer>();
		return this.serverCall;
	}

	protected String getPresignedUrl(ResourceModel photoModel) {
		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			Long id = Long.valueOf(photoModel.getId());
			String url = db.getClient().getPresignedUrl(id);
			mark("url - " + photoModel.getDisplayname());
			return url;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String getPresignedThumbnailSmall(ResourceModel photo) {
		try {

			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			Long id = Long.valueOf(photo.getId());

			if (photo.isUsethumbnail()) {
				String url = db.getClient().getPresignedThumbnailUrl(id, ThumbnailSize.SMALL);
				mark("PresignedThumbnailUrl - " + photo.getDisplayname());
				return url;
			} else {
				mark("PresignedUrl - " + photo.getDisplayname());
				return db.getClient().getPresignedUrl(id);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String getPresignedUrl(RefResourceModel photoModel) {
		try {
			DBModelService db = (DBModelService) ServiceLocator.getInstance().getBean(DBModelService.class);

			// DBService db = (DBService)
			// ServiceLocator.getInstance().getBean(DBService.class);
			// Long id = Long.valueOf(photoModel.getId());

			String url = db.getPresignedUrl(photoModel);
			mark("url - " + photoModel.getDisplayname());
			return url;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String getPresignedThumbnailSmall(RefResourceModel photo) {

		DBModelService db = (DBModelService) ServiceLocator.getInstance().getBean(DBModelService.class);

		if (photo.isUsethumbnail()) {
			String url = db.getPresignedThumbnailUrl(photo, ThumbnailSize.SMALL);
			mark("PresignedThumbnailUrl - " + photo.getDisplayname());
			return url;
		} else {
			mark("PresignedUrl - " + photo.getDisplayname());
			return db.getPresignedUrl(photo);
		}

		/**
		 * try {
		 * 
		 * DBService db = (DBService)
		 * ServiceLocator.getInstance().getBean(DBService.class); Long id =
		 * Long.valueOf(photo.getId());
		 * 
		 * if (photo.isUsethumbnail()) { String
		 * url=db.getClient().getPresignedThumbnailUrl(id, ThumbnailSize.SMALL);
		 * mark("PresignedThumbnailUrl - "+photo.getDisplayname()); return url; } else {
		 * mark("PresignedUrl - "+photo.getDisplayname()); return
		 * db.getClient().getPresignedUrl(id); } } catch (Exception e) { throw new
		 * RuntimeException(e); }
		 **/
	}

	protected InputStream getResourceStream(ResourceModel model) {
		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			Long id = Long.valueOf(model.getId());
			InputStream is = db.getClient().getResourceStreamById(id);
			mark("Inpustream - " + model.getDisplayname());
			return is;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected ArtExhibitionModel getArtExhibition(Long id) {
		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			ArtExhibitionModel item = db.getClient().getArtExhibition(id);
			mark(item);
			return item;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	protected ArtExhibitionItemModel getArtExhibitionItem(GuideContentModel guide) {
		try {

			if (cacheArtExhibitionItem.containsKey(guide.getArtExhibitionItem().getId()))
				return cacheArtExhibitionItem.get(guide.getArtExhibitionItem().getId()).getObject();

			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			ArtExhibitionItemModel item = db.getClient().getArtExhibitionItem(guide.getArtExhibitionItem().getId());
			cacheArtExhibitionItem.put(item.getId(), new Model<ArtExhibitionItemModel>(item));
			mark(item);
			return item;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * @return
	 */
	protected ArtWorkModel getArtWork(GuideContentModel guideContent) {
		try {

			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);

			ArtExhibitionItemModel item;

			if (this.cacheArtExhibitionItem.containsKey(guideContent.getArtExhibitionItem().getId()))
				item = this.cacheArtExhibitionItem.get(guideContent.getArtExhibitionItem().getId()).getObject();
			else {
				item = db.getClient().getArtExhibitionItem(guideContent.getArtExhibitionItem().getId());
				cacheArtExhibitionItem.put(item.getId(), new Model<ArtExhibitionItemModel>(item));
				mark(item);
			}
			return db.getClient().getArtWork(item.getRefArtWork().getId());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected ArtExhibitionGuideModel getArtExhibitionGuide(StringValue stringValue) {
		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			Long id = Long.valueOf(stringValue.toLong());
			ArtExhibitionGuideModel aeg = db.getClient().getArtExhibitionGuide(id);
			mark(aeg);
			return aeg;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	protected List<GuideContentModel> getGuideContents(Long artExhibitionGuideId) {
		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			List<GuideContentModel> list = db.getClient().getArtExhibitionClientHandler().getClient()
					.listArtExhibitionGuideContents(artExhibitionGuideId);
			mark("-guideContents");
			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return
	 */
	protected ArtWorkModel getArtWork(StringValue stringValue) {

		if (stringValue == null)
			return null;
		Long id = null;
		try {
			id = Long.valueOf(stringValue.toLong());
		} catch (Exception e) {
			throw new RuntimeException(stringValue.toString() + "can not be converted to Long");
		}
		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			ArtWorkModel item = db.getClient().getArtWork(id);
			mark(item);
			return item;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return
	 */
	protected SiteModel getSite(StringValue stringValue) {

		if (stringValue == null)
			return null;
		Long id = null;
		try {
			id = Long.valueOf(stringValue.toLong());
		} catch (Exception e) {
			throw new RuntimeException(stringValue.toString() + "can not be converted to Long");
		}
		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			SiteModel item = db.getClient().getSite(id);
			mark(item);
			return item;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return
	 */
	protected SiteModel getSiteByShortName(String shortName) {
		DBModelService db = (DBModelService) ServiceLocator.getInstance().getBean(DBModelService.class);
		SiteModel item = db.getSiteByShortName(shortName);
		mark(item);
		return item;

		/**
		 * if (shortName==null) return null;
		 * 
		 * try { DBService db = (DBService)
		 * ServiceLocator.getInstance().getBean(DBService.class); SiteModel item =
		 * db.getClient().getSiteByShortName(shortName); mark(item); return item; }
		 * catch (Exception e) { throw new RuntimeException(e); }
		 **/
	}

	protected String getImageSrc(RefResourceModel photo) {
		DBModelService db = (DBModelService) ServiceLocator.getInstance().getBean(DBModelService.class);
		return db.getImageSrc(photo);

		/**
		 * try { DBService db = (DBService)
		 * ServiceLocator.getInstance().getBean(DBService.class); if
		 * (photo.isUsethumbnail()) return
		 * db.getClient().getPresignedThumbnailUrl(photo.getId(), ThumbnailSize.MEDIUM);
		 * else return db.getClient().getPresignedUrl(photo.getId());
		 * 
		 * } catch (DelleMuseClientException e) { logger.error(e); return null; }
		 */
	}

	protected String getImageSrc(ResourceModel photo) {
		DBModelService db = (DBModelService) ServiceLocator.getInstance().getBean(DBModelService.class);
		return db.getImageSrc(photo);

		/**
		 * try { DBService db = (DBService)
		 * ServiceLocator.getInstance().getBean(DBService.class); if
		 * (photo.isUsethumbnail()) return
		 * db.getClient().getPresignedThumbnailUrl(photo.getId(), ThumbnailSize.MEDIUM);
		 * else return db.getClient().getPresignedUrl(photo.getId());
		 * 
		 * } catch (DelleMuseClientException e) { logger.error(e);
		 * 
		 * return null; }
		 */
	}

	/**
	 * response.render(JavaScriptHeaderItem.forReference(getApplication().getJavaScriptLibrarySettings().getJQueryReference()));
	 * response.render(JavaScriptHeaderItem.forReference(getApplication().getJavaScriptLibrarySettings().getWicketAjaxReference()));
	 * response.render(JavaScriptHeaderItem.forReference(NotyJSReference.INSTANCE));
	 * response.render(JavaScriptHeaderItem.forReference(NotyPackagedJSReference.INSTANCE));
	 * response.render(JavaScriptHeaderItem.forReference(NotyThemeBootstrapJSReference.INSTANCE));
	 * String bootstrapPrefixPath = "bootstrap/current";
	 * response.render(JavaScriptHeaderItem.forReference(new
	 * WebjarsJavaScriptResourceReference(bootstrapPrefixPath +
	 * "/js/bootstrap.js"))); response.render(CssHeaderItem.forReference(new
	 * WebjarsJavaScriptResourceReference(bootstrapPrefixPath +
	 * "/css/bootstrap.css")));
	 **/

	protected StringResourceModel getLabel(String key) {
		return new StringResourceModel(key, this);
	}

	protected IModel<String> getLabel(String key, String shortName) {
		return new StringResourceModel(key, this).setParameters(new Object[] { shortName });
	}

	public WebPage getGuideContentPage(IModel<GuideContentModel> model, List<IModel<GuideContentModel>> list) {
		return new GuideContentPage(model, list);
	}

	public WebPage getArtExhibitionGuidePage(IModel<ArtExhibitionGuideModel> m) {
		return new ArtExhibitionGuidePage(m);
	}

	public WebPage getArtWorkImagePage(IModel<GuideContentModel> m, String srcUrl) {
		return new ArtWorkImagePage(new Model<ArtWorkModel>(getArtWork(m.getObject())), srcUrl);
	}

}
