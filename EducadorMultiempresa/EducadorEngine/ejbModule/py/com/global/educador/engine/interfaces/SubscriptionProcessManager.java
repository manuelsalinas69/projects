package py.com.global.educador.engine.interfaces;

import javax.ejb.Local;

import py.com.global.educador.engine.dto.EducadorError;
import py.com.global.educador.engine.dto.QueueMessage;

@Local
public interface SubscriptionProcessManager {

	public EducadorError addSubscriber(QueueMessage message);

	public EducadorError deleteSubscriber(QueueMessage message);

	



}
