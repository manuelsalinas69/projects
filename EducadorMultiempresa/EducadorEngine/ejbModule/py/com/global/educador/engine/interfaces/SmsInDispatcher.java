package py.com.global.educador.engine.interfaces;

import javax.ejb.Local;
import javax.jms.BytesMessage;

import py.com.global.educador.engine.dto.QueueMessage;

@Local
public interface SmsInDispatcher {

	public void dispatch(QueueMessage request);
	public void dispatch(BytesMessage request);
	
	
}
