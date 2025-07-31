package dellemuse.webapp.guide;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.DelleMuseModelObject;
import dellemuse.model.GuideContentModel;
import dellemuse.model.SiteModel;
import io.wktui.nav.menu.DropDownMenu;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavBar;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.menu.SeparatorMenuItem;
import wktui.base.BasePanel;
import wktui.base.InvisiblePanel;
import wktui.base.LabelPanel;
import wktui.base.ModelPanel;

public class GuideContentListItemPanel<T extends DelleMuseModelObject> extends ModelPanel<T> {

    private static final long serialVersionUID = 1L;

    IModel<String> subtitle;
    IModel<String> icon;
    IModel<String> iconCss;
    Link<T> imageLink;
    Image image;

    public GuideContentListItemPanel(String id) {
        this(id, null);
    }

    public GuideContentListItemPanel(String id, IModel<T> model) {
        super(id, model);
    }

    public void onBeforeRender() {
        super.onBeforeRender();

        if (getSubtitle() != null) {
            WebMarkupContainer subtitleContainer = new WebMarkupContainer("subtitle-container");
            addOrReplace(subtitleContainer);
            Label subtitleLabel = new Label("subtitle", getSubtitle());
            subtitleContainer.add(subtitleLabel);
        } else {
            addOrReplace(new InvisiblePanel("subtitle-container"));
        }

        if (getIcon() != null) {
            WebMarkupContainer ic = new WebMarkupContainer("icon");
            imageLink.addOrReplace(ic);

        } else {
            imageLink.addOrReplace(new InvisiblePanel("icon"));
        }

        String imageSrc = getImageSrc(getModel());
        
        if (imageSrc != null) {
            Url url = Url.parse(imageSrc);
            UrlResourceReference resourceReference = new UrlResourceReference(url);
            image = new Image ("image", resourceReference);
            
            imageLink.addOrReplace(image);

        } else {
            imageLink.addOrReplace(new InvisiblePanel("image"));
            imageLink.setVisible(false);
        }
        
        WebMarkupContainer noimage = new WebMarkupContainer("noimage") {
            private static final long serialVersionUID = 1L;
            public boolean isVisible() {
                return !imageLink.isVisible(); 
            }
        };
        addOrReplace(noimage);
        
        
    }

    
    
    
    
    @Override
    public void onInitialize() {
        super.onInitialize();

        imageLink = new Link<>("image-link", getModel()) {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                GuideContentListItemPanel.this.onClick();
            }
        };
        add(imageLink);

        Link<T> titleLink = new Link<>("title-link", getModel()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                GuideContentListItemPanel.this.onClick();
            }
        };
        add(titleLink);

        Label title = new Label("title", getModel().getObject().getDisplayname());
        title.setEscapeModelStrings(false);
        titleLink.add(title);

        Label text = new Label("text", getInfo(getModel()));
        text.setEscapeModelStrings(false);
        add(text);
    }

    protected IModel<String> getInfo(IModel<T> model) {
        return new Model<String>(model.getObject().toJSON());
    }

    public void onClick() {

    }

    protected String getImageSrc(IModel<T> model) {
        return null;
    }
    

    public IModel<String> getSubtitle() {
        return subtitle;
    }

    public IModel<String> getIcon() {
        return icon;
    }

    public void setIcon(IModel<String> icon) {
        this.icon = icon;
    }

    public IModel<String> getIconCss() {
        return iconCss;
    }

    public void setIconCss(IModel<String> iconCss) {
        this.iconCss = iconCss;
    }

    public void setSubtitle(IModel<String> subtitle) {
        this.subtitle = subtitle;
    }

}
