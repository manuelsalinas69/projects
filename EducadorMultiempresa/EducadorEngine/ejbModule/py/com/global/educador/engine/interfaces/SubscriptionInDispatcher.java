package py.com.global.educador.engine.interfaces;

import javax.ejb.Local;

import py.com.global.educador.engine.dto.QueueMessage;

@Local
public interface SubscriptionInDispatcher {

	public void dispatch(QueueMessage request);
	
		
	
	
}
