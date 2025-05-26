package dellemuse.webapp.website.error;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.http.WebResponse;

import com.giffing.wicket.spring.boot.context.scan.WicketExpiredPage;

import dellemuse.webapp.website.page.BasePage;
import jakarta.servlet.http.HttpServletResponse;

@WicketExpiredPage
public class ExpiredPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	
	
	
	
	protected void setHeaders(final WebResponse response)
	{
		response.setStatus(HttpServletResponse.SC_GONE);
	}
	
	
	

}
