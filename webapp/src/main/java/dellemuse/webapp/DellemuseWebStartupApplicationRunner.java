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
		}

		Locale.setDefault(Locale.forLanguageTag("es"));
	
		startupLogger.info(SEPARATOR);
		
		WebAppSettings settings=appContext.getBean(WebAppSettings.class);
		
		startupLogger.info    ("App name -> " + settings.getAppName());
		startupLogger.info    ("Endpoint -> " + settings.getEndpoint());
		startupLogger.info    ("Port -> "     + settings.getPort());
		
		if (settings.isSimulateServer()) {
		    startupLogger.info(SEPARATOR);
		    startupLogger.info    ("DELLEMUSE SERVER IS SIMULATED");
		}
		startupLogger.info(SEPARATOR);
		
		startupLogger.info	("Startup at -> " + DateTimeFormatter.RFC_1123_DATE_TIME.format(OffsetDateTime.now()));
	}
	
	public ApplicationContext getAppContext() {
		return appContext;
	}
}
