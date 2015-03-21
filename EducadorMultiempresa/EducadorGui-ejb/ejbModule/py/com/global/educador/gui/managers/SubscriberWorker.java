package py.com.global.educador.gui.managers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.TransactionTimeout;
import org.jboss.seam.annotations.Name;

import py.com.global.educador.gui.utils.AppServicesClient;

@Name("subscriberWorker")
@Stateless
public class SubscriberWorker {

	@Asynchronous
	@TransactionTimeout(value=2,unit=TimeUnit.HOURS)
	public void processList(List<String> numbers, String operacion, String numeroCorto, String host, String port){
		Long t1=System.currentTimeMillis();
		System.out.println("Process list started.");
		for (String numeroCliente : numbers) {
			numeroCliente=numeroCliente.startsWith("0")?numeroCliente:"0"+numeroCliente;
			AppServicesClient.suscriptonProccess(operacion, numeroCorto, numeroCliente, host, port);
		}
		Long t2=System.currentTimeMillis();
		System.out.println("Finish time--> "+(t2-t1)+"ms.");
	}
}
