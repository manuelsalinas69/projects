package py.com.global.educador.engine.managers;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class SessionUpdater {

	@EJB SessionManager sessionManager;
	
	
	@Schedule(hour="*", minute="*/2",persistent=false)
	public void updateSessionTimes(){
		try {
			sessionManager.updateSessionsTime();
		} catch (Exception e) {
			
		}
	}
}
