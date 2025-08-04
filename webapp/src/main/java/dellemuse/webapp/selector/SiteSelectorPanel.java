package dellemuse.webapp.selector;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.DelleMuseModelObject;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.model.ref.RefResourceModel;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.webapp.ServiceLocator;
import dellemuse.webapp.Settings;
import dellemuse.webapp.db.DBService;
import dellemuse.webapp.guide.GuideContentListItemPanel;
import dellemuse.webapp.page.site.SitePage;
import io.wktui.model.TextCleaner;
import io.wktui.struct.list.ListPanel;
import wktui.base.LabelPanel;
import wktui.base.ModelPanel;

public class SiteSelectorPanel extends ModelPanel<Void> {

	private static final long serialVersionUID = 1L;

	final boolean isServerSimulated;

	public SiteSelectorPanel(String id) {
		super(id, null);
		this.isServerSimulated = ServiceLocator.getInstance().getApplicationContext().getBean(Settings.class)
				.isSimulateServer();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		if (this.isServerSimulated) {
			add(new LabelPanel("sites", new Model<String>("Server is simulated")));
			return;
		}

		List<SiteModel> sites = getSites();
		List<IModel<SiteModel>> list = new ArrayList<IModel<SiteModel>>();
		sites.forEach(s -> list.add(new Model<SiteModel>(s)));

		ListPanel<SiteModel> panel = new ListPanel<>("sites", list) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Panel getListItemPanel(IModel<SiteModel> model) {
				GuideContentListItemPanel<SiteModel> panel = new GuideContentListItemPanel<>("row-element", model) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new SitePage(model));
					}

					protected IModel<String> getInfo(IModel<SiteModel> model) {
						String info = model.getObject().getIntro() != null
								? TextCleaner.clean(model.getObject().getIntro(), 120)
								: "";
						return new Model<String>((info));
					}

					@Override
					protected String getImageSrc(IModel<SiteModel> model) {
						if (model.getObject().getRefPhotoModel() != null) {
							return getPresignedThumbnailSmall(model.getObject().getRefPhotoModel());
						}
						return null;
					}
				};
				return panel;
			}
		};
		add(panel);

	}
	
	
	protected String getPresignedThumbnailSmall(RefResourceModel photo) {
		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			Long id = Long.valueOf(photo.getId());
			return db.getClient().getPresignedThumbnailUrl(id, ThumbnailSize.SMALL);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	protected String getPresignedThumbnailSmall(ResourceModel photo) {
		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			Long id = Long.valueOf(photo.getId());
			return db.getClient().getPresignedThumbnailUrl(id, ThumbnailSize.SMALL);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List<SiteModel> getSites() {
		try {
			if (this.isServerSimulated)
				return new ArrayList<SiteModel>();
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			return db.getClient().getSiteClientHandler().findAll();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
