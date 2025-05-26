package dellemuse.webapp;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * 
 */
public class ServiceLocator implements ApplicationContextAware {

	static ServiceLocator instance;
	
	private ApplicationContext applicationContext;
	
	static public  ServiceLocator getInstance() {
		if (instance == null)
			instance = new ServiceLocator();
		return instance;
	}
	
	protected ServiceLocator() {
		
	}
	
	public Object getBean(String name) {
		return getInstance().getApplicationContext().getBean(name);
	}
	
	public Object getBean(Class<?> clazz) {
		return getInstance().getApplicationContext().getBean(clazz);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public ApplicationContext getApplicationContext(){
		return this.applicationContext;
	}
	
	
}
