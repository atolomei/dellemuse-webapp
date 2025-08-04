package dellemuse.webapp.branded.global;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.SiteModel;
import dellemuse.model.UserModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.ref.RefResourceModel;
import dellemuse.webapp.ServiceLocator;
import dellemuse.webapp.branded.page.BrandedSiteSearchArtWorkPage;
import dellemuse.webapp.db.DBService;
import io.wktui.nav.menu.NavBar;
import wktui.base.LogoPanel;
import wktui.base.ModelPanel;

/**
 * 
 */
public class BrandedMediaGlobalTopPanel extends ModelPanel<UserModel> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(BrandedMediaGlobalTopPanel.class.getName());
	private IModel<SiteModel> siteModel;
	private boolean isSearch = true;
	private String srcUrl;

	public BrandedMediaGlobalTopPanel(String id) {
		this(id, null, null, null);
	}

	public BrandedMediaGlobalTopPanel(String id, IModel<SiteModel> siteModel, String srcUrl) {
		super(id, null);
		this.siteModel = siteModel;
		this.srcUrl = srcUrl;
	}

	public BrandedMediaGlobalTopPanel(String id, IModel<SiteModel> siteModel, IModel<UserModel> userModel,
			String srcUrl) {
		super(id, userModel);
		this.siteModel = siteModel;
		this.srcUrl = srcUrl;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (this.siteModel != null)
			this.siteModel.detach();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		NavBar<Void> nav = new NavBar<Void>("navbarLeft");

		String logoUrl = getPresignedUrl(getSiteModel().getObject().getRefLogoModel());
		Url url = Url.parse(logoUrl);
		UrlResourceReference resourceReference = new UrlResourceReference(url);
		Image image = new Image("logo", resourceReference);

		image.add(new AttributeModifier("style", "height:15px;"));

		LogoPanel<Void> logoPanel = new LogoPanel<Void>("item", image) {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(new RedirectPage(getSiteModel().getObject().getWebsite()));
			}
		};

		nav.addNoCollapseLeft(logoPanel);
		add(nav);

		Link<Void> find = new Link<Void>("find") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new BrandedSiteSearchArtWorkPage(getSiteModel()));
			}

			public boolean isVisible() {
				return isSearch() && getSiteModel() != null;
			}
		};
		add(find);

		Link<Void> back = new Link<Void>("back") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				logger.debug(getSrcUrl());
				setResponsePage(new RedirectPage(getSrcUrl()));
			}

			public boolean isVisible() {
				return getSrcUrl() != null;
			}
		};
		add(back);

	}

	public String getSrcUrl() {
		return this.srcUrl;
	}

	public IModel<SiteModel> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<SiteModel> siteModel) {
		this.siteModel = siteModel;
	}

	public boolean isSearch() {
		return isSearch;
	}

	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}

	protected String getPresignedUrl(RefResourceModel photoModel) {
		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			Long id = Long.valueOf(photoModel.getId());
			return db.getClient().getPresignedUrl(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
