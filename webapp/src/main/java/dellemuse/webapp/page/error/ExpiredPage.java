package dellemuse.webapp.page.error;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.http.WebResponse;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketExpiredPage;

import dellemuse.webapp.page.BasePage;

@WicketExpiredPage
@MountPath("expired")
public class ExpiredPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	
	
	
	
	protected void setHeaders(final WebResponse response)
	{
		// response.setStatus(HttpServletResponse.SC_GONE);
	}
	
	
	

}
