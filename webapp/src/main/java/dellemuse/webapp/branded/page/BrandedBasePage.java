package dellemuse.webapp.branded.page;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.SiteModel;
import dellemuse.webapp.page.BasePage;
import dellemuse.webapp.page.site.ArtWorkImagePage;
import dellemuse.webapp.page.site.GuideContentPage;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;

public abstract class BrandedBasePage extends BasePage {

	private static final long serialVersionUID = 1L;

	    
	public BrandedBasePage() {
		super();
	}

	public BrandedBasePage(PageParameters parameters) {
		super(parameters);
	}


	public void onInitialize() {
		super.onInitialize();
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
	}
	
	
	@Override
	public WebPage getArtExhibitionGuidePage(IModel<ArtExhibitionGuideModel> m) {
	     return new BrandedArtExhibitionGuidePage(m);
	}
	    
	@Override	
	public WebPage getGuideContentPage(IModel<GuideContentModel> model, List<IModel<GuideContentModel>> list) {
		return new BrandedGuideContentPage(model, list);
	}
	
	@Override	
	public WebPage getArtWorkImagePage(IModel<GuideContentModel> m, String srcUrl) {
		return new BrandedArtWorkImagePage(new Model<ArtWorkModel>(getArtWork(m.getObject())), srcUrl);
	}
	
	
	@Override
	public String breadCrumbPrefix() {
		return "/dem";
	}

	public abstract String getHomeUrl();
	
	@Override
	public BreadCrumb<Void> createBreadCrumb() {
		BreadCrumb<Void> bc = new BreadCrumb<>();
		bc.addElement(new HREFBCElement(getHomeUrl(), getLabel("home")));
		return bc;
	}

}
