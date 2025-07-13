package dellemuse.webapp.website.guide;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.DelleMuseModelObject;
import wktui.base.ModelPanel;

public class GridSimpleItemPanel<T extends DelleMuseModelObject> extends ModelPanel<T> {

    private static final long serialVersionUID = 1L;

    public GridSimpleItemPanel(String id) {
        this(id, null);
    }

    public GridSimpleItemPanel(String id, IModel<T> model) {
        super(id, model);
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        Link<T> imageLink = new Link<>("image-link", getModel()) {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                GridSimpleItemPanel.this.onClick();
            }
        };
        add(imageLink);

        Link<T> titleLink = new Link<>("title-link", getModel()) {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                GridSimpleItemPanel.this.onClick();
            }
        };
        add(titleLink);

        Label title = new Label("title", getModel().getObject().getDisplayname());
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

}
