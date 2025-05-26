/*
 * Odilon Object Storage
 * (C) Novamens 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dellemuse.webapp;


import java.util.Properties;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import dellemuse.model.logging.Logger;






/**
 * @author atolomei@novamens.com (Alejandro Tolomei)
 */
public class DellemuseWebPropertiesListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
			
	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(DellemuseWebPropertiesListener.class.getName());
	
	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
		    
		ConfigurableEnvironment environment = event.getEnvironment();
		    Properties props = new Properties();
		    props.put("app.name", "Dellemuse");
		    props.put("spring.servlet.multipart.max-file-size", "100GB");
		    props.put("spring.servlet.multipart.max-request-size", "100GB");
		    props.put("spring.main.banner-mode", "off");
		    props.put("spring.main.log-startup-info", "false");
		    props.put("server.error.whitelabel.enabled", "false");
		    props.put("spring.main.lazy-initialization", "false");
		    props.put("spring.output.ansi.enabled", "DETECT");
		    props.put("server.error.whitelabel.enabled", "false");

		    //props.put("server.compression.enabled", "true");
		    //props.put("server.compression.mime-types", "text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json");
		    //props.put("server.compression.min-response-size", "1024");
		    //props.put("spring.resources.cache.cachecontrol.max-age", "120");
		    //props.put("spring.resources.cache.cachecontrol.must-revalidate", "true");
		    //props.put("spring.servlet.multipart.file-size-threshold", "24KB");
		    
		    environment.getPropertySources().addFirst(new PropertiesPropertySource("dellemuseProps", props));
		    
		    
	}
	 
}
