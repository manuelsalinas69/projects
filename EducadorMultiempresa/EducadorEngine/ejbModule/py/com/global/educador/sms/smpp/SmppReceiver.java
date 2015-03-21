package py.com.global.educador.sms.smpp;

import ie.omk.smpp.Connection;
import ie.omk.smpp.event.ConnectionObserver;
import ie.omk.smpp.event.ReceiverExitEvent;
import ie.omk.smpp.event.SMPPEvent;
import ie.omk.smpp.message.DeliverSM;
import ie.omk.smpp.message.SMPPPacket;
import ie.omk.smpp.message.Unbind;
import ie.omk.smpp.message.UnbindResp;
import ie.omk.smpp.version.SMPPVersion;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Map;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.Educador_Constants;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.utils.QueueManager;
import py.com.global.educador.sms.messages.DeliverSMWrapper;

/**
 * 
 * @author Lino Chamorro
 * 
 */
public class SmppReceiver extends SmppAttributes implements ConnectionObserver {

	// Atributos generales
	private Connection smppConnection = null;
	private long lastActivityTime = 0;
	public static final Logger log = Logger.getLogger(SmppReceiver.class);
	private Map<String, Object> numbers;

	// To send responses to SMSMessageTransmitter
	//private JMSSender queueToProcessMsgReceived = null;
	//private InitialContext initialContext = null;

	public SmppReceiver(String hostName, int port, Map<String, Object> numbers) {
		if (port > 0) {
			this.hostName = hostName;
			this.port = port;
		} else {
			if (hostName != null) {
				// Descomponer la direccion
				
				int off = hostName.indexOf(':', 0);
				// Extraer la direccion IP o el host name
				if (off > 0) {
					try {
						this.hostName = hostName.substring(0, off);
						this.port = Integer.parseInt(hostName.substring(
								off + 1, hostName.length()));
					} catch (Exception e) {
						this.hostName = null;
						this.port = 0;
						log.error("Error al cargar la direccion de conexion -"
								+ hostName + "-." + e.getCause(), e);
					}
				}
			}
		}
		this.numbers=numbers;
//		try {
////			log.debug("Iniciando el contexto...");
////			initialContext = new InitialContext();
//		} catch (NamingException e) {
//			log.error("Fallo al cargar el contexto del Contenedor --> error="
//					+ e.getCause(), e);
//		}
	}

	

	/**
	 * Conectarse al SMSC
	 * 
	 * @return
	 */
	public boolean Connect() {
		boolean ready = false;
		if (smppConnection == null || !smppConnection.isBound()) {
			try {
				if (hostName != null && port > 0 && systemID != null
						&& password != null && systemType != null) {
					smppConnection = new Connection(hostName, port, true);
					// Asignar el listener para recibir las respuestas
					// asyncronicas
					smppConnection.addObserver(this);
					// Automaticamente responder al ENQUIRE_LINK
					smppConnection.autoAckLink(true);
					smppConnection.autoAckMessages(true);
					//
					log.info("Binding to the SMSC.. --> " + this.toString());
					smppConnection.setVersion(SMPPVersion.V34);
					smppConnection.bind(Connection.RECEIVER, systemID,
							password, systemType, sourceTON, sourceNPI,
							addrRange);
					ready = true;
				}
				// Actualizar ultima fecha-hora de actividad
				lastActivityTime = System.currentTimeMillis();
			} catch (ConnectException ioEx) {
				log.error("Fallo al establecer conexion con el SMSC --> "
						+ ioEx.getCause(), ioEx);
				smppConnection = null;
				ready = false;
			} catch (Exception e) {
				log.error(
						"Fallo al establecer conexion con el SMSC --> "
								+ e.getCause(), e);
				smppConnection = null;
				ready = false;
			}
		} else {
			ready = smppConnection.isBound();
		}
		return ready;
	}

	/**
	 * Verificar si la conexion esta activa
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return (smppConnection != null) ? smppConnection.isBound() : false;
	}

	public boolean reconnect(){
		try {
			smppConnection.removeObserver(this);
			smppConnection.unbind();
			Thread.sleep(2000);
			Connect();
		} catch (Exception e) {
			log.error("reconnect",e);
			
		}
		return false;
	}
	
	/**
	 * Ultima fecha-hora de actividad
	 * 
	 * @return
	 */
	public long getLastActivityTime() {
		return lastActivityTime;
	}

	/**
	 * Cerrar conexion
	 */
	public void close() {
		if (smppConnection != null) {
			synchronized (smppConnection) {
				try {
					smppConnection.unbind();
					
				} catch (Exception e) {
					log.debug(
							"Fallo al desconectarse del SMSC --> "
									+ e.getCause(), e);
				}
			}
		}
	}

	public void closeLink() {
		if (smppConnection != null) {
			synchronized (smppConnection) {
				try {
					if (smppConnection != null) {
						smppConnection.closeLink();
					}
				} catch (Exception e) {
					log.debug(
							"Fallo al desconectarse del SMSC --> "
									+ e.getCause(), e);
				}
			}
		}
		// Limpiando referencias
		smppConnection = null;
	}

	public boolean sendUnbindResponse(UnbindResp pak) {
		boolean result = false;
		try {
			smppConnection.sendResponse(pak);
			result = true;
		} catch (IOException x) {
			log.debug(
					"Fallo al enviar unbind response al SMSC --> "
							+ x.getCause(), x);
			result = false;
		}
		return result;
	}

	public void packetReceived(Connection arg0, SMPPPacket pak) {
		log.info("Packet received --> SMSC Command_Id=0x"
				+ Integer.toHexString(pak.getCommandId()) + ", carrier="
				+ this.carrier);
		switch (pak.getCommandId()) {

		// Bind transmitter response. Check it's status for success...
		case SMPPPacket.BIND_RECEIVER_RESP:
			if (pak.getCommandStatus() != 0) {
				log.info("Error binding to the SMSC --> Command_Status="
						+ Integer.toHexString(pak.getCommandStatus()));
			} else {
				log.info("Successfully bound. Waiting for message delivery..");
			}
			break;

		// Submit message response...
		case SMPPPacket.DELIVER_SM:
			if (pak.getCommandStatus() != 0) {
				log.info("Deliver SM with an error! --> Command_Status="
						+ Integer.toHexString(pak.getCommandStatus()));

			} else {
				lastActivityTime = System.currentTimeMillis();
				this.processPacketReceived((DeliverSM) pak);
			}
			break;

		// Enquire link request
		case SMPPPacket.ENQUIRE_LINK:
			log.trace("SMSC has enquire link --> carrier=" + this.carrier);
			//EnquireLinkResp packResp= new EnquireLinkResp((EnquireLink)pak);
//			try {
//				log.info("Responding SMS ENQUIRE_LINK response");
//				smppConnection.sendResponse(packResp);
//			} catch (IOException e) {
//				log.error(e);
//			}
			break;

		// Unbind request received from server..
		case SMPPPacket.UNBIND:
			log.info("SMSC has requested unbind! Responding..");
			UnbindResp ubr = new UnbindResp((Unbind) pak);
			if (this.sendUnbindResponse(ubr)) {
				this.closeLink();
			}
			break;

		// Unbind response..
		case SMPPPacket.UNBIND_RESP:
			log.info("Unbound.. --> carrier=" + this.carrier);
			this.closeLink();
			break;

		default:
			log.info("Unexpected packet received! Command_Id = 0x"
					+ Integer.toHexString(pak.getCommandId()));
		}
	}

	private void processPacketReceived(DeliverSM pak) {
		log.info("Packet received, sending to SMSMessageReceiver --> SequenceNum="
				+ pak.getSequenceNum() + ", carrier=" + carrier);
		log.info(pak.getMessageText());
		DeliverSMWrapper submitSMRespWrap = new DeliverSMWrapper(pak, carrier);
		log.info("DeliverSMWrapper--> "+submitSMRespWrap);
		if (".t".equalsIgnoreCase(submitSMRespWrap.getMessageText().trim())) {
			log.debug("mensaje de test de conexio...");
			return;
		}
		if (!numbers.containsKey(submitSMRespWrap.getDestination())) {
			log.debug("Numero corto no conocido--> "+submitSMRespWrap.getDestination());
			return;
		}
		
		QueueMessage msg= new QueueMessage();
		msg.addParam("CARRIER", submitSMRespWrap.getCarrier());
		msg.addParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER, submitSMRespWrap.getDestination());
		msg.addParam("MESSAGE", submitSMRespWrap.getMessageText());
		//msg.addParam("SENT_DATE", submitSMRespWrap.getSentDate());
		msg.addParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER, getSuscriptorNro(submitSMRespWrap.getSource()));
//		QueueManager.sendObject(msg.getAllParams(), Educador_Constants.Queues.SMS_IN);
		QueueManager.sendObject(msg, Educador_Constants.Queues.SMS_IN);
		QueueManager.closeQueueConn(Educador_Constants.Queues.SMS_IN);
//		queueToProcessMsgReceived.send(submitSMRespWrap);
	}

	private String getSuscriptorNro(String source) {
		String prefix_595="5959";
		String prefix_09="09";
		if (source.trim().startsWith(prefix_595)) {
			return source.trim().replaceFirst("5959", "09");
		}
		if (source.trim().startsWith(prefix_09)) {
			return source.trim();
		}
		return source.trim();
	}



	public void update(Connection myConnection, SMPPEvent ev) {
		switch (ev.getType()) {
		case SMPPEvent.RECEIVER_EXIT:
			if (ev instanceof ReceiverExitEvent) {
				ReceiverExitEvent event = (ReceiverExitEvent) ev;
				if (event.getReason() != ReceiverExitEvent.EXCEPTION) {
					if (event.getReason() == ReceiverExitEvent.BIND_TIMEOUT) {
						log.error("Bind timed out waiting for response.");
					}
					log.debug("Receiver thread has exited normally.");
				} else {
					log.debug("Receiver thread died due to exception:");
				}
			}
			break;
		}

	}

	@Override
	public String toString() {
		return "SmppReceiver [index=" + index + ",carrier=" + carrier
				+ ", hostName=" + hostName + ", port=" + port + ", systemID="
				+ systemID + ", systemType=" + systemType + ", password="
				+ password + "]";
	}

}
