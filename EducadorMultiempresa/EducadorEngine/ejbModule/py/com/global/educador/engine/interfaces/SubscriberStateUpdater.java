package py.com.global.educador.engine.interfaces;

import javax.ejb.Local;

import py.com.global.educador.engine.enums.EstadoSuscriptorModulo;

@Local
public interface SubscriberStateUpdater {

	
	public void updateSuscriptorModulo(Long idProyecto, Long idModulo, Long idSuscriptor, EstadoSuscriptorModulo estadoSuscriptorModulo);
}
