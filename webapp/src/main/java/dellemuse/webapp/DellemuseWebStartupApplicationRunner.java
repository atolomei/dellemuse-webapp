package dellemuse.webapp;



import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;




@Component
public class DellemuseWebStartupApplicationRunner implements ApplicationRunner {

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(DellemuseWebStartupApplicationRunner.class.getName());
	static private Logger startupLogger = Logger.getLogger("StartupLogger");

	
	static public final String SEPARATOR = "---------------------------------";
	
	@Autowired
	@JsonIgnore
	private final ApplicationContext appContext;

	
	
	
	public DellemuseWebStartupApplicationRunner(ApplicationContext appContext) {
		this.appContext = appContext;
		
	}
	
	
	@Override
	public void run(ApplicationArguments args) throws Exception {

		if (startupLogger.isDebugEnabled()) {
			startupLogger.debug("Command line args:");
			args.getNonOptionArgs().forEach( item -> startupLogger.debug(item));
			//startupLogger.debug(ServerConstant.SEPARATOR);
		}

		Locale.setDefault(Locale.ENGLISH);
		
		//boolean iGeneral = initGeneral();
		//if(iGeneral)
		//	startupLogger.info(ServerConstant.SEPARATOR);
		
		startupLogger.info	(SEPARATOR);
		
		startupLogger.info	("Startup at -> " + DateTimeFormatter.RFC_1123_DATE_TIME.format(OffsetDateTime.now()));
		startupLogger.info	("done");
		
		
		
		
	}
	
	
	public ApplicationContext getAppContext() {
		return appContext;
	}
	
	
}
