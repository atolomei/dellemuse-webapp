package dellemuse.webapp;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import dellemuse.model.logging.Logger;
import jakarta.annotation.PostConstruct;


@Configuration
// @PropertySource("classpath:application.properties")
public class Settings extends BaseService implements SystemService {
	
	static private Logger logger = Logger.getLogger(Settings.class.getName());
	static private Logger startuplogger = Logger.getLogger("StartupLogger");
	
	private static final OffsetDateTime systemStarted = OffsetDateTime.now();
	
	
	/** DELLEMUSE SERVER ----------------------------------------------------- */
	
    @Value("${dellemuse.server.port:9876}")
    private int dellemuseServerPort;
	
    @Value("${dellemuse.server.endpoint:http://localhost}")
    private String dellemuseServerEndpoint;
	
    @Value("${dellemuse.server.ssl:false}")
    private String dellemuseServerSslStr;
    private boolean dellemuseServerSsl;
    
    @Value("${dellemuse.server.accessKey:dellemuse}")
    private String dellemuseServerAccessKey;
    
    @Value("${dellemuse.server.secretKey:dellemuse}")
    private String dellemuseServerSecretKey;

    @Value("${simulateServer:false}")
    private boolean simulateServer;

    public boolean isSimulateServer() {
        return this.simulateServer;
    }
    
    
    /** DELLEMUSE WEBAPP ----------------------------------------------------- */
    
    @Value("${dellemuse.webapp.accessKey:dellemuse}")
    @NonNull
    protected String accessKey;
        
    @Value("${dellemuse.webapp.secretKey:dellemuse}")
    @NonNull
    protected String secretKey;

    @Value("${server.port:8091}")
    protected int port;
    
    @Value("${dellemuse.webapp.endpoint:http://localhost}")
    @NonNull
    protected String endpoint;
    
	
	@Value("${scanFreqmillisecs:10000}")
	private int scanFreqMillisecs;

	public int getScanFreqMillisecs() {return this.scanFreqMillisecs;}
	
	@Value("${templates:null}")
	private String templatesDir;
	
	@Value("${export:null}")
	private String exportDir;
	
	@Value("${data:null}")
	private String dataDir;

	@Value("${indexexport:null}")
	private String indexExportDir;
	
	// SERVER ------------------------
		
		
	@Autowired
	public Settings() {
	}

	@PostConstruct
	protected void init() {

		if (templatesDir==null || (templatesDir.trim().length()==0) || templatesDir.trim().equals("null"))
			templatesDir="templates";
		templatesDir=templatesDir.trim();
		
		if (exportDir==null || exportDir.trim().length()==0 || exportDir.trim().equals("null"))
			exportDir="export";
		exportDir=exportDir.trim();
		
		if (dataDir==null || dataDir.trim().length()==0  || dataDir.trim().equals("null"))
			dataDir="data";
		dataDir=dataDir.trim();
		
		if (indexExportDir==null || indexExportDir.trim().length()==0 || indexExportDir.trim().equals("null"))
			indexExportDir="torneos-web";
		
		indexExportDir=indexExportDir.trim();
		
		dellemuseServerSsl=dellemuseServerSslStr.equals("true");
		
	}

    public String getTemplatesDir() {
        return templatesDir;
    }

    public void setTemplatesDir(String templatesDir) {
        this.templatesDir = templatesDir;
    }

    public String getExportDir() {
        return exportDir;
    }

    public void setExportDir(String exportDir) {
        this.exportDir = exportDir;
    }

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public String getIndexExportDir() {
        return indexExportDir;
    }

    public void setIndexExportDir(String indexExportDir) {
        this.indexExportDir = indexExportDir;
    }

    public static OffsetDateTime getSystemstarted() {
        return systemStarted;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setEndpoint(String url) {
        this.endpoint = url;
    }
    public OffsetDateTime getSystemStartTime() {
        return systemStarted;
    }
    
    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getEndpoint() {
        return endpoint;
    }
    
    public int getPort() {
        return port;
    }

    public int getDellemuseServerPort() {
        return dellemuseServerPort;
    }

    public void setDellemuseServerPort(int dellemuseServerPort) {
        this.dellemuseServerPort = dellemuseServerPort;
    }

    public String getDellemuseServerEndpoint() {
        return dellemuseServerEndpoint;
    }

    public void setDellemuseServerEndpoint(String dellemuseServerEndpoint) {
        this.dellemuseServerEndpoint = dellemuseServerEndpoint;
    }

    public boolean isDellemuseServerSsl() {
        return dellemuseServerSsl;
    }

    public void setDellemuseServerSsl(boolean dellemuseServerSsl) {
        this.dellemuseServerSsl = dellemuseServerSsl;
    }

    public String getDellemuseServerAccessKey() {
        return dellemuseServerAccessKey;
    }

    public void setDellemuseServerAccessKey(String dellemuseServerAccessKey) {
        this.dellemuseServerAccessKey = dellemuseServerAccessKey;
    }

    public String getDellemuseServerSecretKey() {
        return dellemuseServerSecretKey;
    }

    public void setDellemuseServerSecretKey(String dellemuseServerSecretKey) {
        this.dellemuseServerSecretKey = dellemuseServerSecretKey;
    }

    public String getAppName() {
        return "Web Application for Service Management";
    }
    

}
