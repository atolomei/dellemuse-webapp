package dellemuse.webapp.db;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.client.error.DelleMuseClientException;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.ref.RefResourceModel;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.webapp.BaseService;
import dellemuse.webapp.ServiceLocator;
import dellemuse.webapp.WebAppSettings;

import jakarta.annotation.PostConstruct;

@Service
public class DBModelService extends BaseService {

	 
    static private Logger logger = Logger.getLogger(DBService.class.getName());

    static private Logger startuplogger = Logger.getLogger("StartupLogger");

    @JsonIgnore
    private final WebAppSettings settings;

    @JsonIgnore
    final DBService dbService;
    
    
    public DBModelService(WebAppSettings settings,  DBService dbService) {
        this.settings = settings;
        this.dbService=dbService;
    }
    
    @PostConstruct
    public void onInit() {
        startuplogger.debug("Started -> " + this.getClass().getSimpleName());

    }
    public WebAppSettings getSettings() {
        return settings;
    }
    
     
    public String getPresignedUrl(RefResourceModel photo) {
    	
    	try {

      	   DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);    
           Long id = Long.valueOf(photo.getId());
           return db.getClient().getPresignedUrl(id);    
            
    	} catch (Exception e) {
             throw new RuntimeException(e);
         }
    	
    }
    
	public String getPresignedThumbnailUrl(RefResourceModel photo, ThumbnailSize size) {
        try {

     	   DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);    
           Long id = Long.valueOf(photo.getId());
            
            if (photo.isUsethumbnail()) {
         	   String url=db.getClient().getPresignedThumbnailUrl(id, size);
         	   return url;    
            }
            else {
         	   return db.getClient().getPresignedUrl(id);    
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
    
	
	
    public SiteModel getSiteByShortName(String shortName) {

    	if (shortName==null)
    		return null;
    	
    	try {
        	DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
            SiteModel item = db.getClient().getSiteByShortName(shortName);
            return item;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	public String getImageSrc(RefResourceModel photo) {

		try {
			DBService db = (DBService) ServiceLocator.getInstance().getBean(DBService.class);
			if (photo.isUsethumbnail())
				return db.getClient().getPresignedThumbnailUrl(photo.getId(), ThumbnailSize.MEDIUM);
			else
				return db.getClient().getPresignedUrl(photo.getId());
			
		} catch (DelleMuseClientException e) {
			logger.error(e);
			return null;
		}
	}
    
    public String getImageSrc(ResourceModel photo) {
		try {
				if (photo.isUsethumbnail())
				return getDBService().getClient().getPresignedThumbnailUrl(photo.getId(), ThumbnailSize.MEDIUM);
			else
				return getDBService().getClient().getPresignedUrl(photo.getId());
			
		} catch (DelleMuseClientException e) {
			logger.error(e);
			return null;
		}
	}

	public DBService getDBService() {
		return dbService;
	}
    
}
