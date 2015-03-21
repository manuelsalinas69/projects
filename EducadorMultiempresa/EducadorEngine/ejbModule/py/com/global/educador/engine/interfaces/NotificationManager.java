package py.com.global.educador.engine.interfaces;

import javax.ejb.Local;
import javax.jms.BytesMessage;

import py.com.global.educador.engine.dto.QueueMessage;
@Local
public interface NotificationManager {

	public void process(QueueMessage message);
	public void process(BytesMessage message);
}
