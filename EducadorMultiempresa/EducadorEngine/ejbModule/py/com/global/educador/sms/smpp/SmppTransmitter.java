package py.com.global.educador.sms.smpp;

import ie.omk.smpp.Address;
import ie.omk.smpp.Connection;
import ie.omk.smpp.event.ConnectionObserver;
import ie.omk.smpp.event.ReceiverExitEvent;
import ie.omk.smpp.event.SMPPEvent;
import ie.omk.smpp.message.BindTransmitterResp;
import ie.omk.smpp.message.SMPPPacket;
import ie.omk.smpp.message.SubmitSM;
import ie.omk.smpp.message.SubmitSMResp;
import ie.omk.smpp.message.Unbind;
import ie.omk.smpp.message.UnbindResp;
import ie.omk.smpp.version.SMPPVersion;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import javax.jms.BytesMessage;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.configuration.Educador_Constants;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.utils.QueueManager;

public class SmppTransmitter extends SmppAttributes implements
ConnectionObserver {

	// Atributos generales
	private Connection smppConnection = null;
	private SubmitSM submit = null;
	private long lastActivityTime = 0;
	private final Logger log = Logger.getLogger(SmppTransmitter.class);
	Map<Integer, Object> messages;

	// To send responses to SMSMessageTransmitter
	//private JMSSender queueMessageResponse = null;
	//private InitialContext initialContext = null;

	/**
	 * Constructor
	 * 
	 * @param address
	 * @param port
	 */
	public SmppTransmitter(String hostName, int port) {
		messages= new HashMap<Integer, Object>();
		if (port > 0) {
			this.hostName = hostName;
			this.port = port;
		} else {
			if (hostName != null) {
				// Descomponer la direccion
				int off = hostName.indexOf(':', 0);
				// Extrear la direccion IP o el host name
				if (off > 0) {
					try {
						this.hostName = hostName.substring(0, off);
						this.port = Integer.parseInt(hostName.substring(
								off + 1, hostName.length()));
					} catch (Exception e) {
						this.hostName = null;
						this.port = 0;
						log.error("Al cargar la direccion de conexion -"
								+ hostName + "-." + e.getCause());
					}
				}
			}
		}
		//		try {
		//			log.debug("Iniciando el contexto...");
		//			initialContext = new InitialContext();
		//		} catch (NamingException e) {
		//			log.error("Fallo al cargar el contexto del Contenedor --> error="
		//					+ e.getCause());
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
				if (hostName != null && port > 0) {
					smppConnection = new Connection(hostName, port, true);
					// Asignar el listener para recibir las respuestas
					// asyncronicas
					smppConnection.addObserver(this);
					// Automaticamente responder al ENQUIRE_LINK
					smppConnection.autoAckLink(true);
					// Bind to the SMSC (as a transmitter)
					if (systemID != null && password != null
							&& systemType != null) {
						log.info("Binding to the SMSC.. --> " + this.toString());
						smppConnection.setVersion(SMPPVersion.V34);
						smppConnection.bind(Connection.TRANSMITTER, systemID,
								password, systemType, sourceTON, sourceNPI,
								addrRange);
						// Preparar el paquete para envios
						submit = (SubmitSM) smppConnection
								.newInstance(SMPPPacket.SUBMIT_SM);

						ready = true;
					}
					// Actualizar ultima fecha-hora de actividad
					lastActivityTime = System.currentTimeMillis();
				}
			} catch (ConnectException ioEx) {
				log.error("Fallo al establecer conexion con el SMSC --> "
						+ ioEx.getCause(), ioEx);
				smppConnection = null;
			} catch (Exception e) {
				log.error(
						"Fallo al establecer conexion con el SMSC. "
								+ e.getCause(), e);
				smppConnection = null;
			}
		} else {
			ready = smppConnection.isBound();
		}
		return ready;
	}

	/**
	 * Enviar el mensaje de texto
	 * 
	 * @param subscriber
	 * @param text
	 * @return
	 */
	public synchronized boolean send(long messageID, String shortNumber, String subscriber,
			String text, QueueMessage message) {
		boolean ready = false;

		if (submit != null) {
			try {
				synchronized (smppConnection) {
					if (!isConnected()) {
						Connect();
					}
					
					Address source = new Address(sourceTON, sourceNPI,
							shortNumber);
					Address destination = new Address(targetTON, targetNPI,
							subscriber);
					submit.setSequenceNum((int) messageID);
					submit.setMessageId(messageID+"");
					submit.setSource(source);
					submit.setDestination(destination);
					submit.setMessageText(text);
					if (serviceType != null) {
						submit.setServiceType(serviceType);
					}
					log.debug("Sending (ServiceTye)--> "+getServiceType());
					smppConnection.sendRequest(submit);
//					messages.put((int)messageID, message);
//					if (!messages.containsKey((int)messageID)) {
//						log.error("No se pudo agregar");
//					}
					ready = true;
					// Actualizar ultima fecha-hora de actividad
					lastActivityTime = System.currentTimeMillis();
				}
			} catch (Exception e) {
				log.error("Fallo al enviar el mensaje.", e);
				ready = false;
			}
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
		try {
			if (smppConnection != null && smppConnection.isBound()) {
				synchronized (smppConnection) {
					smppConnection.unbind();
				}
			}
			//			if (this.queueMessageResponse != null) {
			//				queueMessageResponse.close();
			//			}
		} catch (Exception e) {
			log.error("Closing connections", e);
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

	@Override
	public String toString() {
		return "SmppTransmitter [index=" + index + ",carrier=" + carrier
				+ ", hostName=" + hostName + ", port=" + port + ", systemID="
				+ systemID + ", systemType=" + systemType + ", password="
				+ password + ", serviceType=" + serviceType + "]";
	}

	public synchronized void packetReceived(Connection myConnection, SMPPPacket pak) {
		log.debug("Packet received --> Command_Id(int)="+pak.getCommandId());
		log.debug("Packet received --> Command_Status(int)="+pak.getCommandStatus());
		log.debug("Packet received --> Command_Id=0x"
				+ Integer.toHexString(pak.getCommandId()) + ", carrier="
				+ this.carrier);
		switch (pak.getCommandId()) {
		// Bind transmitter response. Check it's status for success...
		case SMPPPacket.BIND_TRANSMITTER_RESP:
			if (pak.getCommandStatus() != 0) {
				log.error("Error de conexion al SMSC --> Command_Status=0x"
						+ Integer.toHexString(pak.getCommandStatus())
						+ ", carrier=" + this.carrier);
			} else {
				log.info("Conectado satisfactoriamente --> SMSC="
						+ ((BindTransmitterResp) pak).getSystemId()
						+ ", carrier=" + this.carrier);
			}
			break;

			// Submit message response...
		case SMPPPacket.SUBMIT_SM_RESP:
			processResponse((SubmitSMResp) pak);
			break;

			// Enquire link request
		case SMPPPacket.ENQUIRE_LINK:
			log.trace("SMSC has enquire link --> carrier=" + this.carrier);
			break;

			// Unbind request received from server..
		case SMPPPacket.UNBIND:
			log.info("SMSC has requested unbind! Responding.. --> carrier="
					+ this.carrier);
			UnbindResp ubr = new UnbindResp((Unbind) pak);
			if (this.sendUnbindResponse(ubr)) {
				this.closeLink();
			}
			break;

			// Unbind response..
		case SMPPPacket.UNBIND_RESP:
			log.debug("Unbound --> carrier=" + this.carrier);
			this.closeLink();
			break;
		default:
			log.debug("Unknown response received --> Command_Id=0x"
					+ Integer.toHexString(pak.getCommandId()));
		}
	}

	/**
	 * Procesar respuesta del smsc
	 * 
	 * @param packetResponse
	 */
	private void processResponse(SubmitSMResp packetResponse) {
		log.debug("Packet received, sending to SMSMessageTransmiter --> SequenceNum="
				+ packetResponse.getSequenceNum()
				+ ", Command_Status=0x"
				+ Integer.toHexString(packetResponse.getCommandStatus())
				+ ", carrier=" + carrier);
		//Object message=messages.get(packetResponse.getSequenceNum());
//		if (message==null) {
//			log.error("No se encontro el messageParams para el SequenceNum-->"+packetResponse.getSequenceNum());
//			log.error("Command Status recibido---> "+packetResponse.getCommandStatus());
//			return;
//		}
		
//		if (message instanceof QueueMessage) {
			QueueMessage m2= new QueueMessage();
			//m2=( message;
			m2.addParam(QueueMessageParamKey.COMMAND_STATUS, packetResponse.getCommandStatus());
			m2.addParam(QueueMessageParamKey.SEQ_NUM, packetResponse.getSequenceNum());
			QueueManager.sendObject(m2, Educador_Constants.Queues.EJECUTION_UPDATER);
			QueueManager.closeQueueConn(Educador_Constants.Queues.EJECUTION_UPDATER);
		//	messages.remove(packetResponse.getSequenceNum());
		////}
		
		//		SubmitSMRespWrapper submitSMRespWrap = new SubmitSMRespWrapper(
		//				packetResponse);
		//		queueMessageResponse.send(submitSMRespWrap);
	}

	public void update(Connection con, SMPPEvent ev) {
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



	public boolean send(Long msgId, String shortCode, String susNro,
			String msg, BytesMessage message) {
		// TODO Auto-generated method stub
		return false;
	}

}
