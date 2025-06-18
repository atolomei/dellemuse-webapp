package dellemuse.webapp;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import dellemuse.model.logging.Logger;
import jakarta.annotation.PostConstruct;





@SpringBootApplication
@ImportAutoConfiguration(exclude = {
    com.giffing.wicket.spring.boot.starter.configuration.extensions.stuff.restannotations.RestAnnotationsConfig.class
})
public class DellemuseWebApplication {
			
	static private Logger logger = Logger.getLogger(DellemuseWebApplication.class.getName());
	static private Logger std_logger = Logger.getLogger("StartupLogger");
	
	static public String[] cmdArgs = null;
	static public final String SEPARATOR = "---------------------------------";
	

	public static void main(String[] args) throws Exception {
	    
		SpringApplication application = new SpringApplication(DellemuseWebApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		cmdArgs = args;
		application.run(args);
		
	  }
	
	@PostConstruct
	public void onInitialize() {
		
		std_logger.info("");
		for (String s : DellemuseWebVersion.getAppCharacterName())
			std_logger.info(s);
		
		
		
		std_logger.info(SEPARATOR);
		std_logger.info("This software is licensed under the Apache License, Version 2.0");
		std_logger.info("http://www.apache.org/licenses/LICENSE-2.0");

		initShutdownMessage();
	}
	
	/**
	 *
	 *
	 */
	private void initShutdownMessage() {
	    Runtime.getRuntime().addShutdownHook(new Thread() {
           public void run() {
           	std_logger.info("");
           	std_logger.info("As the roman legionaries used to say when falling in battle");
           	std_logger.info("'Dulce et decorum est pro patria mori'...Shuting down... goodbye.");
           	std_logger.info("");
           }
       });
	}
	

}
