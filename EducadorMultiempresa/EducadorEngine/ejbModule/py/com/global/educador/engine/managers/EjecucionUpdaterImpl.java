package py.com.global.educador.engine.managers;

import java.util.Date;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.enums.EstadoEjecucionSuscriptor;
import py.com.global.educador.engine.enums.EstadoEnvio;
import py.com.global.educador.engine.enums.EstadoEnvioSmsc;
import py.com.global.educador.engine.enums.EstadoSuscriptorModulo;
import py.com.global.educador.engine.interfaces.EjecucionUpdater;
import py.com.global.educador.engine.interfaces.SubscriberStateUpdater;
import py.com.global.educador.jpa.entity.EjecucionSuscriptor;
import py.com.global.educador.jpa.entity.EjecucionSuscriptorDetalle;
import py.com.global.educador.jpa.entity.EvaluacionSuscriptor;
import py.com.global.educador.jpa.entity.LogSmsOut;
import py.com.global.educador.sms.manager.SenderManager;

@Stateless
public class EjecucionUpdaterImpl implements EjecucionUpdater {

	@PersistenceContext(unitName = "EducadorJpa")
	EntityManager entityManager;
	@EJB
	SessionManager sessionManager;
	@EJB
	SubscriberStateUpdater subscriberStateUpdater;
	@EJB SenderManager senderManager;

	Logger log = Logger.getLogger(EjecucionUpdaterImpl.class);


	@Override
	@Asynchronous
	public void process(QueueMessage m) {
		try {
			//userTrx.begin();
			if (m == null) {
				log.error("El mensaje enviado para procesar es nulo");
				//userTrx.rollback();
				return;
			}

			Integer commmandStautus = (Integer) m.getParam(QueueMessageParamKey.COMMAND_STATUS);
			if (commmandStautus == null) {
				log.error("No se recibio el parametro [COMMAND_STATUS]");
				//userTrx.rollback();
				return;
			}
			Integer seqNum = (Integer) m.getParam(QueueMessageParamKey.SEQ_NUM);
			if (seqNum==null) {
				log.error("No se recibio el parametro [SEQ_NUM]");
				//userTrx.rollback();
				return;
			}
			QueueMessage message=senderManager.getMessage(seqNum);
			if (message==null) {
				log.error("No se encontro el QueueMessage para el [SEQ_NUM]--> "+seqNum);
				//userTrx.rollback();
				return;
			}
			
			String smscr = "Command_Status[int=" + commmandStautus + "; hex=0x"
					+ Integer.toHexString(commmandStautus) + "]";
			Long logId = (Long) message.getParam(QueueMessageParamKey.LOG_ID);
			if (logId == null) {
				log.error("No se recibio el log de sms a actualizar");
			}
			LogSmsOut logSmsOut = entityManager.find(LogSmsOut.class, logId);
			if (logSmsOut == null) {
				log.error("No se encontro el log de sms out para el id---> "
						+ logId);
				try {
					//@Wire @Alambreter FIXME
					Thread.sleep(1000);
					logSmsOut = entityManager.find(LogSmsOut.class, logId);
					
					if (logSmsOut!=null) {
						logSmsOut.setRespuestaSmsc(smscr);
						if (commmandStautus.intValue() == 0) {
							logSmsOut.setEstado(EstadoEnvio.ENVIADO.name());
							logSmsOut.setFechaEnvio(new Date());
						} else {
							logSmsOut.setEstado(EstadoEnvio.FALLIDO.name());
						}
						entityManager.merge(logSmsOut);
					}
				} catch (InterruptedException e) {
					log.debug(e);
				}
				//log.warn("Esperando un tiempo para poder dejar persistir al log.");
			} else {
				logSmsOut.setRespuestaSmsc(smscr);
				if (commmandStautus.intValue() == 0) {
					logSmsOut.setEstado(EstadoEnvio.ENVIADO.name());
					logSmsOut.setFechaEnvio(new Date());
				} else {
					logSmsOut.setEstado(EstadoEnvio.FALLIDO.name());
				}
				entityManager.merge(logSmsOut);
			}
			String tipo = (String) message
					.getParam(EducadorConstants.QueueMessageParamKey.TYPE);
			if (tipo == null) {
				log.warn("No se encontro el parametro [TIPO], se asume que es un mensaje de envio unico");
				return;
			}

			switch (tipo.toUpperCase().trim()) {
			case "EVALUACION":
				Long idEvaSul = (Long) message.getParam(QueueMessageParamKey.SUBSCRIBER_EVALUATION_ID);
				EvaluacionSuscriptor evaluacionSuscriptor = entityManager.find(
						EvaluacionSuscriptor.class, idEvaSul);
				Long idEjecucionDet2 = (Long) message
						.getParam(QueueMessageParamKey.SUBSCRIBER_EXECUTION_DETAIL_ID);
				EjecucionSuscriptorDetalle ejeSusDet = entityManager.find(
						EjecucionSuscriptorDetalle.class, idEjecucionDet2);
				if (ejeSusDet == null) {
					log.error("No se encontro el detalle de ejecucion para el id---> "
							+ idEjecucionDet2);
					try {
						
						Thread.sleep(1000);
						ejeSusDet = entityManager.find(
								EjecucionSuscriptorDetalle.class, idEjecucionDet2);
						if (ejeSusDet==null) {
							return;
						}
					} catch (Exception e) {
						log.error(e);
						return;
					}
					
				}

				if (evaluacionSuscriptor == null) {
					log.error("No se encontro la evaluacion del suscriptor");
					try {
						Thread.sleep(1000);
						evaluacionSuscriptor = entityManager.find(
								EvaluacionSuscriptor.class, idEvaSul);
						if (evaluacionSuscriptor==null) {
							return;
						}
					} catch (Exception e) {
						log.error(e);
						return;
					}
					
				}
				//ejeSusDet.setFechaEjecucion(new Date());
				ejeSusDet.setRespuestaSmsc(smscr);
				ejeSusDet.setEstadoEnvioSmsc(EstadoEnvioSmsc.RESPONDIDO_SMSC.name());

				//evaluacionSuscriptor.setFechaEnvioPregunta(new Date());
				evaluacionSuscriptor.setRespuestaSenderSmsc(smscr);
				if (commmandStautus.intValue() == 0) {
					// log.debug("Envio exitoso");
					ejeSusDet.setFechaEnvio(new Date());
					evaluacionSuscriptor.setEstadoEnvio(EstadoEnvio.ENVIADO.name());
					evaluacionSuscriptor.setFechaEnvioPregunta(new Date());
					Long intento = evaluacionSuscriptor.getIntento();
					intento = intento == null ? 1 : ++intento;
					evaluacionSuscriptor.setIntento(intento);

				} else {
					evaluacionSuscriptor.setEstadoEnvio(EstadoEnvio.FALLIDO.name());
					
					String susNro = (String) message
							.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER);
					String shortNumber = (String) message
							.getParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER);
					sessionManager.removeSession(susNro, shortNumber);
				}
				entityManager.merge(ejeSusDet);
				entityManager.merge(evaluacionSuscriptor);
				break;
			case "TIP":
				Long idEjecucionDet = (Long) message
						.getParam(QueueMessageParamKey.SUBSCRIBER_EXECUTION_DETAIL_ID);
				EjecucionSuscriptorDetalle ejecucionSuscriptorDetalle = entityManager
						.find(EjecucionSuscriptorDetalle.class, idEjecucionDet);
				if (ejecucionSuscriptorDetalle == null) {
					log.error("No se encontro el detalle de ejecucion para el id---> "
							+ idEjecucionDet);
					try {
						Thread.sleep(1000);
						ejecucionSuscriptorDetalle = entityManager
								.find(EjecucionSuscriptorDetalle.class, idEjecucionDet);
						if (ejecucionSuscriptorDetalle==null) {
							return;
						}
					} catch (Exception e) {
						log.error(e);
						//userTrx.rollback();
						return;
					}
					
				}
				//ejecucionSuscriptorDetalle.setFechaEjecucion(new Date());
				ejecucionSuscriptorDetalle.setRespuestaSmsc(smscr);
				ejecucionSuscriptorDetalle.setEstadoEnvioSmsc(EstadoEnvioSmsc.RESPONDIDO_SMSC.name());
				if (commmandStautus.intValue() == 0) {
					ejecucionSuscriptorDetalle
							.setEstadoEjecucion(EstadoEnvio.ENVIADO.name());
					ejecucionSuscriptorDetalle.setFechaEnvio(new Date());
					if (ejecucionSuscriptorDetalle.getEnvioFinal() != null
							&& ejecucionSuscriptorDetalle.getEnvioFinal()) {
						EjecucionSuscriptor ejecucionSuscriptor = ejecucionSuscriptorDetalle
								.getEjecucionSuscriptor();
						ejecucionSuscriptor
								.setEstadoEjecucion(EstadoEjecucionSuscriptor.FINALIZADO
										.name());
						entityManager.merge(ejecucionSuscriptor);
						Long idProyecto = (Long) message
								.getParam(QueueMessageParamKey.PROJECT_ID);
						Long idModulo = (Long) message
								.getParam(QueueMessageParamKey.MODULE_ID);
						Long idSuscriptor = (Long) message
								.getParam(QueueMessageParamKey.SUBSCRIBER_ID);
						subscriberStateUpdater.updateSuscriptorModulo(idProyecto,
								idModulo, idSuscriptor,
								EstadoSuscriptorModulo.FINALIZADO);

					}
				}
				entityManager.merge(ejecucionSuscriptorDetalle);
				break;
			default:
				log.error("Tipo de request no conocido---> " + tipo);
				break;
			}
			//userTrx.commit();
		} catch (Exception e) {
			log.error(e);
			
		}

	}

	
		
	

}
