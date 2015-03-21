package py.com.global.educador.engine.managers;

import javax.ejb.Stateless;
import javax.jms.BytesMessage;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.interfaces.ListaServiceManager;

@Stateless
public class ListaServiceManagerImpl implements ListaServiceManager {

	Logger log=Logger.getLogger(ListaServiceManagerImpl.class);
	
	public void process(QueueMessage message) {
		
		
	}

	@Override
	public void process(BytesMessage message) {
		// TODO Auto-generated method stub
		
	}

}
