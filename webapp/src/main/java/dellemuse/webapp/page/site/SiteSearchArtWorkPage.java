package dellemuse.webapp.page.site;

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

/**
 * site foto Info - exhibitions
 */

@MountPath("/site/finder/${id}")
public class SiteSearchArtWorkPage extends BasePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteSearchArtWorkPage.class.getName());

	private WebMarkupContainer container;
	private WebMarkupContainer formContainer;

	private TextField<String> inputField;
	private Form<Void> form;
	private IModel<String> inputString = new Model<String>();

	private IModel<SiteModel> model;

	private StringValue stringValue;


	public SiteSearchArtWorkPage(PageParameters parameters) {
		super(parameters);
		super.setOutputMarkupId(true);
		this.stringValue = getPageParameters().get("id");
	}

	public SiteSearchArtWorkPage(IModel<SiteModel> siteModel) {
		super();
		super.setOutputMarkupId(true);
		setModel(siteModel);
		stringValue=StringValue.valueOf( Long.valueOf( siteModel.getObject().getId()).toString());
		super.getPageParameters().set("id", stringValue);
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();

		add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<GuideContentModel>("footer-panel"));

		
		container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		add(container);
		container.add(new InvisiblePanel("results"));
		
		if (getModel()==null && this.stringValue != null) {
			SiteModel siteModel = getSite(stringValue);
			setModel(new Model<>(siteModel));
		}
		
		if (getModel()==null || getModel().getObject()==null) {
	
			ErrorPanel alert = new ErrorPanel("error", null, new Model<String>("Site is missing"));
			add(alert);
			
			formContainer = new WebMarkupContainer("formContainer");
			add(formContainer);
			this.form = new Form<Void>("findForm");
			formContainer.add(this.form);
			formContainer.setVisible(false);
			this.form.setVisible(false);
			addOrReplace( new PageHeaderPanel<>("page-header", null, getLabel("find-content")));
			return;
		}


		PageHeaderPanel<SiteModel> ph = new PageHeaderPanel<>("page-header", null, getLabel("find-content"));
	
		
		BreadCrumb<Void> bc = createBreadCrumb();

		bc.addElement(new HREFBCElement("/site/" + String.valueOf( getModel().getObject().getId()), new Model<String> (getModel().getObject().getDisplayname())));
		bc.addElement(new BCElement(getLabel("find-content")));

		ph.setBreadCrumb(bc);
		addOrReplace(ph);
	
		add(new InvisiblePanel("error"));
		
		formContainer = new WebMarkupContainer("formContainer");
		add(formContainer);
		this.form = new Form<Void>("findForm");
		formContainer.add(this.form);

		this.inputField = new TextField<String>("text", inputString, getLabel("enter-code-or-text"));
		this.form.add(this.inputField);
		SubmitButton<Void> submit = new SubmitButton<Void>("submit") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				SiteSearchArtWorkPage.this.onSubmit(target);
			}
			
			public IModel<String> getLabel() {
				return SiteSearchArtWorkPage.this.getLabel("search");
			}
		};
		form.add(submit);
	}

	protected void onSubmit(AjaxRequestTarget target) {

		inputString = new Model<String>(this.inputField.getValue());

		if (inputString == null || inputString.getObject() == null || inputString.getObject().length() == 0) {
			return;
		}

		
		addResultsPanel(inputString.getObject().toLowerCase().trim());
		target.add(container);

	}

	protected void addResultsPanel(String filter) {

		//DummyBlockPanel panel = new DummyBlockPanel("results", inputString);
		//container.addOrReplace(panel);

		List<IModel<ArtWorkModel>> list = new ArrayList<IModel<ArtWorkModel>>();

       getArtWorks( getModel().getObject()).forEach( item -> {
    	   if (item.getDisplayname().toLowerCase().contains(filter))
    		   list.add( new Model<ArtWorkModel> (item) );   
       });
		 
		ListPanel<ArtWorkModel> listPanel = new ListPanel<>("results", list) {

			private static final long serialVersionUID = 1L;

			protected Panel getListItemPanel(IModel<ArtWorkModel> model) {

				GuideContentListItemPanel<ArtWorkModel> panel = new GuideContentListItemPanel<>("row-element",
						model) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						//setResponsePage(new GuideContentPage(model, getList()));
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

		
		container.addOrReplace( listPanel );
	}

	/**
	protected List<GuideContentModel> getArtWorks() {
		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			return db.getClient().listSiteGuideContents(getModel().getObject());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
**/

	protected List<ArtWorkModel> getArtWorks(SiteModel siteModel) {
		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			return db.getClient().listSiteArtWorks(siteModel);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
/**
	protected SiteModel getArtExhibitionGuide(StringValue stringValue) {
		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			Long id = Long.valueOf(stringValue.toLong());
			return db.getClient().getSite(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
**/
	
	public IModel<SiteModel> getModel() {
		return this.model;
	}

	public void setModel(IModel<SiteModel> model) {
		this.model = model;
	}

	protected SiteModel getSite(StringValue stringValue) {
		try {
			
			if (stringValue==null || stringValue.isEmpty())
				return null;
			
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			Long id = Long.valueOf(stringValue.toLong());
			return db.getClient().getSiteClientHandler().get(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
