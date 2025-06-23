package dellemuse.webapp.website.page;


import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("ping")
public class DellemuseWebPingPage extends BasePage {

	private static final long serialVersionUID = 1L;
	public DellemuseWebPingPage(PageParameters parameters) {
		super(parameters);
	}
}
