package dellemuse.webapp.db;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.client.DelleMuseClient;
import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.logging.Logger;
import dellemuse.webapp.BaseService;
import dellemuse.webapp.Settings;
import jakarta.annotation.PostConstruct;


@Service
public class DBService extends BaseService {

    static private Logger logger = Logger.getLogger(DBService.class.getName());
    static private Logger startuplogger = Logger.getLogger("StartupLogger");
    
    @JsonIgnore
    private final Settings settings;
    
    @JsonIgnore
    private DelleMuseClient client;
    
    public DBService(Settings settings) {
        this.settings=settings;
    }
    
    @PostConstruct
    public void onInit() {
        
        if (getSettings().isSimulateServer()) {
            startuplogger.info("No connection attempt for a simulated server" );
            return;
        }
        
        this.client = new DelleMuseClient(  getSettings().getDellemuseServerEndpoint(),
                                            getSettings().getDellemuseServerPort(),
                                            getSettings().getDellemuseServerAccessKey(),
                                            getSettings().getDellemuseServerSecretKey()
                                    );
        
        startuplogger.info("Connected to Server -> " + this.client.toString()) ;
        
        try {
            if (!client.ping().equals("ok")) {
                throw new RuntimeException(client.ping());
            }
        } catch (DelleMuseClientException e) {
            throw new RuntimeException(e);
        }
    }

    public DelleMuseClient getClient() {
        return client;
    }

    public void setClient(DelleMuseClient client) {
        this.client = client;
    }

    public Settings getSettings() {
        return settings;
    }
}
