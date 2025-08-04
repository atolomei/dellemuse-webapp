package dellemuse.webapp.branded.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Site;
import org.apache.wicket.ajax.AjaxRequestTarget;
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
import dellemuse.webapp.branded.global.BrandedGlobalFooterPanel;
import dellemuse.webapp.branded.global.BrandedGlobalTopPanel;
import dellemuse.webapp.db.DBService;
import dellemuse.webapp.global.GlobalFooterPanel;
import dellemuse.webapp.global.GlobalTopPanel;
import dellemuse.webapp.global.PageHeaderPanel;
import dellemuse.webapp.guide.GuideContentListItemPanel;
import dellemuse.webapp.page.BasePage;
import dellemuse.webapp.page.security.DellemuseWebSigninPage;
import io.wktui.error.ErrorPanel;
import io.wktui.form.Form;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.TextField;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.listNavigator.ListNavigator;
import io.wktui.struct.list.ListPanel;
import io.wktui.text.ExpandableReadPanel;
import wktui.base.DummyBlockPanel;
import wktui.base.InvisiblePanel;
import wktui.base.LabelPanel;

/**
 * site foto Info - exhibitions
 */

@MountPath("/dem/site/finder/${siteid}")
public class BrandedSiteSearchArtWorkPage extends BrandedBaseSitePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(BrandedSiteSearchArtWorkPage.class.getName());

	private WebMarkupContainer container;
	private WebMarkupContainer formContainer;

	private TextField<String> inputField;
	private Form<Void> form;
	private IModel<String> inputString = new Model<String>();

	private StringValue siteStringValue;

	public BrandedSiteSearchArtWorkPage(PageParameters parameters) {
		super(parameters);
		super.setOutputMarkupId(true);
		this.siteStringValue = parameters.get("siteid");
	}

	public BrandedSiteSearchArtWorkPage(IModel<SiteModel> siteModel) {
		super(siteModel);
		super.setOutputMarkupId(true);
		super.getPageParameters().set("siteid",
				StringValue.valueOf(Long.valueOf(siteModel.getObject().getId()).toString()));
	}

	@Override
	protected Long getSiteId() {
		if (this.siteStringValue != null && !this.siteStringValue.isEmpty())
			return Long.valueOf(this.siteStringValue.toLong());

		if (getSiteModel() != null && getSiteModel().getObject() != null)
			return getSiteModel().getObject().getId();

		return null;
	}

	@Override
	protected SiteModel getSite() {
		return getSiteModel().getObject();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		if ((getSiteModel() == null) && (siteStringValue != null) && (!siteStringValue.isEmpty())) {
			SiteModel siteModel = getSite(siteStringValue);
			setSiteModel(new Model<SiteModel>(siteModel));
		}

		add(new BrandedGlobalTopPanel("top-panel", this.getSiteModel()));
		add(new BrandedGlobalFooterPanel<>("footer-panel"));

		container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);
		container.add(new InvisiblePanel("results"));

		if (getSiteModel() == null || getSiteModel().getObject() == null) {

			ErrorPanel alert = new ErrorPanel("error", null, new Model<String>("Site is missing"));
			add(alert);
			this.formContainer = new WebMarkupContainer("formContainer");
			add(this.formContainer);
			this.form = new Form<Void>("findForm");
			formContainer.add(this.form);
			formContainer.setVisible(false);
			this.form.setVisible(false);
			addOrReplace(new PageHeaderPanel<>("page-header", null, getLabel("find-content")));
			return;
		}

		PageHeaderPanel<SiteModel> ph = new PageHeaderPanel<>("page-header", null, getLabel("find-content"));

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(getSiteHomeElement());
		bc.addElement(new BCElement(getLabel("find-content")));

		ph.setBreadCrumb(bc);
		addOrReplace(ph);

		add(new InvisiblePanel("error"));

		this.formContainer = new WebMarkupContainer("formContainer");
		add(formContainer);
		this.form = new Form<Void>("findForm");
		this.formContainer.add(this.form);

		this.inputField = new TextField<String>("text", inputString, getLabel("enter-code-or-text"));
		this.form.add(this.inputField);
		SubmitButton<Void> submit = new SubmitButton<Void>("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				BrandedSiteSearchArtWorkPage.this.onSubmit(target);
			}

			public IModel<String> getLabel() {
				return BrandedSiteSearchArtWorkPage.this.getLabel("search");
			}
		};
		this.form.add(submit);
	}

	protected void onSubmit(AjaxRequestTarget target) {

		this.inputString = new Model<String>(this.inputField.getValue());

		if (this.inputString == null || this.inputString.getObject() == null || inputString.getObject().length() == 0) {
			return;
		}

		addResultsPanel(this.inputString.getObject().toLowerCase().trim());
		target.add(container);

	}

	protected void searchTitle() {

	}

	protected void addResultsPanel(String filter) {

		// DummyBlockPanel panel = new DummyBlockPanel("results", inputString);
		// container.addOrReplace(panel);

		List<IModel<ArtWorkModel>> list = new ArrayList<IModel<ArtWorkModel>>();

		if (isCode(filter)) {
			final Long code = Long.valueOf(filter.trim());
			
			/**for (ArtWorkModel aw : getArtWorks(getSiteModel().getObject())) {
				if (aw.getId().equals(code)) {
					list.add(new Model<ArtWorkModel>(aw));
					break;
				}
			}**/
			try {
				ArtWorkModel a = getArtWork(Long.valueOf(code));
				list.add(new Model<ArtWorkModel>(a));
			} catch (Exception e) {
				logger.error(e);
			}
		} else {
			
			getArtWorks(getSiteModel().getObject()).forEach(item -> {
				if (item.getDisplayname().toLowerCase().contains(filter))
					list.add(new Model<ArtWorkModel>(item));
			});
		
		}

		if (list.size() == 0) {
			this.container.addOrReplace(new LabelPanel("results", getLabel("no-results")));
		}
		else {

			ListPanel<ArtWorkModel> listPanel = new ListPanel<>("results", list) {

				private static final long serialVersionUID = 1L;

				protected Panel getListItemPanel(IModel<ArtWorkModel> model) {

					GuideContentListItemPanel<ArtWorkModel> panel = new GuideContentListItemPanel<>("row-element",
							model) {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick() {
							// setResponsePage(new GuideContentPage(model, getList()));
						}

						protected String getImageSrc(IModel<ArtWorkModel> model) {
							RefResourceModel photo = model.getObject().getRefPhotoModel();
							if (photo != null) {
								return getPresignedThumbnailSmall(photo);
							}
							return null;
						}

						@Override
						public IModel<String> getIcon() {
							if (getModel().getObject().getRefAudioModel() == null)
								return null;
							else
								return new Model<String>("fa-solid fa-headphones");
						}

						@Override
						public IModel<String> getSubtitle() {
							ArtWorkModel aw = getModel().getObject();
							if (aw != null) {
								StringBuilder info = new StringBuilder();
								int n = 0;
								for (RefPersonModel p : aw.getRefArtists()) {
									if (n++ > 0)
										info.append(", ");
									info.append(p.getDisplayname());
								}
								String str = TextCleaner.truncate(info.toString(), 160);
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
						protected IModel<String> getInfo(IModel<ArtWorkModel> model) {
							// StringBuilder info = new StringBuilder();

							/**
							 * if ( model.getObject().getInfo()!=null) { return new
							 * Model<String>(TextCleaner.clean(model.getObject().getInfo(), 320)); }
							 * 
							 * ArtExhibitionItemModel item = getArtExhibitionItemModel(model.getObject());
							 * if (item.getInfo()!=null) { info.append(TextCleaner.clean(item.getInfo(),
							 * 320)); return new Model<String>(TextCleaner.clean(info.toString(), 320)); }
							 * else {
							 * 
							 * ArtWorkModel aw = getArtWork(model.getObject()); if (aw!=null &&
							 * (aw.getInfo()!=null)) { return new
							 * Model<String>(TextCleaner.clean(info.toString(), 320)); } }
							 **/

							ArtWorkModel aw = model.getObject();
							if (aw != null && (aw.getIntro() != null)) {
								return new Model<String>(TextCleaner.clean(aw.getIntro(), 240));
							}

							return null;
						}

					};
					return panel;
				}
			};

			this.container.addOrReplace(listPanel);
		}

	}

	protected boolean isCode(String filter) {

		if (filter == null)
			return false;

		try {
			@SuppressWarnings("unused")
			Long code = Long.valueOf(filter.trim());
			return true;

		} catch (Exception e) {

		}
		return false;
	}

	protected List<ArtWorkModel> getArtWorks(SiteModel siteModel) {
		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			return db.getClient().listSiteArtWorks(siteModel);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
