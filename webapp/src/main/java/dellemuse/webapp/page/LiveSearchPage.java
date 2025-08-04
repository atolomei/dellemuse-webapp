package dellemuse.webapp.page;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.AttributeModifier;


import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;

import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

import io.wktui.struct.list.KeyboardBehavior;


@MountPath("test")
public class LiveSearchPage extends WebPage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(LiveSearchPage.class.getName());
	
	private String inputText = "a";
	private TextField<String> textField;
	

	public LiveSearchPage() {
	}
		
	public void onInitialize() {
		super.onInitialize();
		
		// Modelo compartido para el input y el label
		
        IModel<String> model = new PropertyModel<>(this, "inputText");

        Form<?> form = new Form<>("form");
        
        add(form);

        textField = new TextField<>("input", model);
        textField.setOutputMarkupId(true);

        Label liveLabel = new Label("liveLabel", model);
        liveLabel.setOutputMarkupId(true);

        
        form.add(textField);
        form.add(liveLabel);	        

        
        
        // AJAX: se dispara en cada cambio del texto (cada tecla)
        textField.add(new AjaxFormComponentUpdatingBehavior("change") {

        	private static final long serialVersionUID = 1L;
			
        	@Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(liveLabel);
            }

            @Override
            protected void onError(AjaxRequestTarget target, RuntimeException e) {
                e.printStackTrace(); // para debug
            }
        });

        
        textField.add(new KeyboardBehavior(true) {
            private static final long serialVersionUID = 1L;

			protected void onKey(AjaxRequestTarget target, String jsKeycode) {
                //AutoCompleteFieldV5.this.onKey(target, jsKeycode);
				try {
					Integer code = Integer.valueOf(jsKeycode);
					char character = (char) code.intValue();
				
					logger.debug(textField.getValue());
					
					if (code>=32 && code<127)
						setInputText( textField.getValue() + character);
					
					// delete
					// left, right, up, down
					// back
					
					if (code==127) {
						logger.debug("delete");
					}
					
					logger.debug(code);
					
					
				} catch (Exception e) {
					logger.error(e);
				}
				target.add(liveLabel);
			}
        });
        
		
	}
	  public String getInputText() {
	        return inputText;
	    }

	    public void setInputText(String inputText) {
	        this.inputText = inputText;
	    }
}
