package py.com.global.educador.engine.interfaces;

import javax.ejb.Local;

import py.com.global.educador.engine.dto.QueueMessage;

@Local
public interface SmsOutDispatcher {

	public void dispatch(QueueMessage message);
	
}
