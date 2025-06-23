package dellemuse.webapp.db;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.client.DelleMuseClient;
import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Constant;
import dellemuse.model.util.TimerThread;
import dellemuse.webapp.BaseService;
import dellemuse.webapp.Settings;
import jakarta.annotation.PostConstruct;

@Service
public class DBService extends BaseService {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(DBService.class.getName());

    static private Logger startuplogger = Logger.getLogger("StartupLogger");

    @JsonIgnore
    private final Settings settings;

    @JsonIgnore
    private DelleMuseClient client;

    public DBService(Settings settings) {
        this.settings = settings;
    }

    @JsonIgnore
    private TimerThread timerConnect;

    private synchronized void connect() {

        this.client = new DelleMuseClient(getSettings().getDellemuseServerEndpoint(), getSettings().getDellemuseServerPort(),
                getSettings().getDellemuseServerAccessKey(), getSettings().getDellemuseServerSecretKey());

        try {
            if (!client.ping().equals("ok")) {
                throw new RuntimeException(client.ping());
            } else {
                if (this.client.getHttpUrl() != null) {
                    startuplogger.info(Constant.SEPARATOR);
                    startuplogger.info("Connected to Server -> " + this.client.getHttpUrl().toString());
                    startuplogger.info(Constant.SEPARATOR);
                }
            }
        } catch (DelleMuseClientException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void onInit() {

        if (getSettings().isSimulateServer()) {
            startuplogger.info("IMPORTANT -> No connection for a simulated server");
            return;
        }

        connect();

        try {
            startuplogger.info("Institutions:");

            this.client.getInstitutionClientHandler().findAll().forEach(item -> startuplogger.info(item.getDisplayname()));

            startuplogger.info(Constant.SEPARATOR);

        } catch (DelleMuseClientException e) {
            startuplogger.error(e);
            throw new RuntimeException(e);
        }

        this.timerConnect = new TimerThread() {
            public long getSleepTimeMillis() {
                return Constant.DEFAULT_SLEEP_TIME * 1;
            }

            @Override
            public void onTimer() {

                String ping;
                boolean requireReconnect = false;
                try {
                    ping = getClient().ping();
                    if (ping != null && ping.equals("ok")) {
                        logger.info(((getClient().getHttpUrl() != null) ? (getClient().getHttpUrl().toString().trim() + " ") : "")
                                + "  -> ping " + ping);
                        requireReconnect = false;
                    } else {
                        requireReconnect = true;
                    }

                } catch (DelleMuseClientException e) {
                    logger.error(e);
                    logger.error("WebApp will try to reconnect to the server");
                    requireReconnect = true;
                }

                if (requireReconnect) {
                    try {
                        connect();
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
            }
        };

        Thread thread = new Thread(timerConnect);
        thread.setDaemon(true);
        thread.setName("Dellemuse Server " + getSettings().getDellemuseServerEndpoint() + " - connection timer ");
        thread.start();

        startuplogger.debug("Started -> " + this.timerConnect.getClass().getSimpleName());

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
