package py.com.global.educador.engine.dispatchers;

import java.util.Date;
import java.util.regex.Matcher;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.enums.TipoOperacionSuscripcion;
import py.com.global.educador.engine.interfaces.AltaServiceManager;
import py.com.global.educador.engine.interfaces.BajaServiceManager;
import py.com.global.educador.engine.interfaces.ListaServiceManager;
import py.com.global.educador.engine.interfaces.SubscriptionInDispatcher;

@Stateless
public class SubscriptionInDispatcherImpl implements SubscriptionInDispatcher {
	
	Logger log=Logger.getLogger(SubscriptionInDispatcherImpl.class);
	
	@EJB ListaServiceManager listaServiceManager;
	@EJB AltaServiceManager altaServiceManager;
	@EJB BajaServiceManager bajaServiceManager;
	
	@Asynchronous
	public void dispatch(QueueMessage message) {
		try {
			log.debug("Dispatch subscription request-->"+message);
			String msg=(String) message.getParam(EducadorConstants.QueueMessageParamKey.MESSAGE);
			if (msg==null) {
				msg="";
			}
			msg=msg.trim();
			Matcher lMatcher=EducadorConstants.Patterns.LISTA_PATTERN.matcher(msg);
			Matcher aMatcher=EducadorConstants.Patterns.ALTA_PATTERN.matcher(msg);
			Matcher bMatcher=EducadorConstants.Patterns.BAJA_PATTERN.matcher(msg);
			
			message.addParam(EducadorConstants.QueueMessageParamKey.RECEPTION_DATE, new Date());
			message.addParam(QueueMessageParamKey.MESSAGE, msg);
			if (lMatcher.matches()) {
				listaServiceManager.process(message);
				return;
			}
			if (aMatcher.matches()) {
				message.addParam(EducadorConstants.QueueMessageParamKey.OPERATION_TYPE,TipoOperacionSuscripcion.ALTA.name() );
				altaServiceManager.process(message);
				return;
			}
			if (bMatcher.matches()) {
				message.addParam(EducadorConstants.QueueMessageParamKey.OPERATION_TYPE,TipoOperacionSuscripcion.BAJA.name() );
				bajaServiceManager.process(message);
				return;
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

	

}
