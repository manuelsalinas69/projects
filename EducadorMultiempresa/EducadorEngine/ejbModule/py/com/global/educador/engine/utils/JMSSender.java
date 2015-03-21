package py.com.global.educador.engine.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.BytesMessage;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.dto.QueueMessage;

public class JMSSender {
	private String queueName = null;
	private QueueConnectionFactory queueConnectionFactory = null;
	private QueueConnection queueConnection = null;
	private QueueSession queueSession = null;
	private Queue queue = null;
	private QueueSender queueSender = null;
	private ObjectMessage objMessage = null;
	private TextMessage txtMessage = null;
	private boolean connected = false;
	private transient Logger log = Logger.getLogger(JMSSender.class);

	public static final String WEB_LOGIC_CONNECTION_FACTORY="weblogic/jms/ConnectionFactory";
	public static final String JBOSS_CONNECTION_FACTORY="ConnectionFactory";
	/*
	 * Constructor
	 */
	public JMSSender(InitialContext initialContext, String connectionFactory, String queueName) {

		// Nombre de la cola de datos
		this.queueName = queueName;

		// Iniciar el contexto del contenedo
		try {
			if (initialContext == null) {
				log.debug("Iniciando el contexto...");
				initialContext = new InitialContext();
			}
			// Buscar los recursos de connection factory y la cola de datos en el contenedor
			queueConnectionFactory = (QueueConnectionFactory) initialContext.lookup(connectionFactory);
			queue = (Queue) initialContext.lookup(queueName);
		} catch (Exception e) {
			log.error("Fallo al cargar el contexto del contenedor. " + e.getMessage(),e);
			
		}
	}

	/*
	 * Constructor
	 */
	public JMSSender(QueueConnectionFactory connectionFactory, Queue queue) {
		this.queueConnectionFactory = connectionFactory;
		this.queue = queue;
		try {
			this.queueName = ( queue != null)? queue.getQueueName() : null;
		} catch (JMSException e) {
			log.error("Fallo al inicializar cola de datos. "+ e.getMessage());
		}
	}

	/**
	 * Iniciar la conexion con la cola de datos
	 * 
	 * @return boolean - True si la conexion fue satisfactoriamente establecida
	 */
	public boolean init() {
		connected = false;

		/*
		 * Crear la conexion Crear una sesion sobre la conexion Crear el queue
		 * sender Crear el mensaje Finally, close connection.
		 */
		try {
			if (queueConnectionFactory != null && queue != null) {
				queueConnection = queueConnectionFactory.createQueueConnection();
				queueSession = queueConnection.createQueueSession(false, Session.DUPS_OK_ACKNOWLEDGE);
				//queueSession.
				
				
				queueSender = queueSession.createSender(queue);
				queueSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
				
				
				queueSender.setTimeToLive(3600000);//10 minutos //WIRE
				queueName = queue.getQueueName();
				connected = true;
			}
		} catch (Exception e) {
			log.error("Fallo al iniciar conexion con la cola de datos. " + e.getMessage());
		}

		return connected;
	}

	/**
	 * Verificar el estado de conexion
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * Cerrar el objeto
	 */
	public void close() {

		if (queueConnection != null) {
			try {
				if (queueSender != null) {
					queueSender.close();
					queueSender = null;
				}
				if (queueSession != null) {
					queueSession.close();
					queueSession = null;
				}
				queueConnection.close();
			} catch (Exception e) {
			}
		}
		connected = false;
	}

	/**
	 * Enviar un objeto por la cola de datos
	 * 
	 * @param obj
	 *            - Objeto a enviar
	 * @return
	 */
	public boolean send(Serializable obj) {
		boolean ready = false;

		// Verificar el objeto
		if (obj == null || queueSender == null)
			return ready;

		// Verificar la estructura objMessage
		if (objMessage == null) {
			try {
				objMessage = queueSession.createObjectMessage();
				//BytesMessage bytesMessage = queueSession.createBytesMessage();
				//createBytesMessage.
				//bytesMessage.setObjectProperty(arg0, arg1)
				queueSender.setDisableMessageID(true);
				queueSender.setDisableMessageTimestamp(true);
			} catch (Exception e) {
				log.error("Al crear el mensaje para la cola de datos. " + e.getMessage());
				return ready;
			}
		}

		try {
			objMessage.setObject(obj);
			queueSender.send(objMessage);
			ready = true;
		} catch (Exception e) {
			log.error("Al enviar el mensaje a la cola -" + queueName + "-." + e.getMessage());
		}

		return ready;
	}
	
	public boolean send(QueueMessage obj) {
		boolean ready = false;

		// Verificar el objeto
		if (obj == null || queueSender == null)
			return ready;

		// Verificar la estructura objMessage
		BytesMessage message=null;
			try {
				//objMessage = queueSession.createObjectMessage();
				message = queueSession.createBytesMessage();
				//bytesMessage.writeObject(obj);
				//createBytesMessage.
				//bytesMessage.setObjectProperty(arg0, arg1)
				for (Entry<String, Object> entry : obj.getAllParams().entrySet()) {
					if (entry.getValue() instanceof Date) {
						message.setLongProperty(entry.getKey(), ((Date) entry.getValue()).getTime());
					}
					else{
						message.setObjectProperty(entry.getKey(), entry.getValue());
					}
//					if (entry.getValue() instanceof Long) {
//						message.setLongProperty(entry.getKey(), (long) entry.getValue());
//					}
//					if (entry.getValue() instanceof String) {
//						message.setStringProperty(entry.getKey(), (String) entry.getValue());
//					}
//					if (entry.getValue() instanceof Integer) {
//						message.setIntProperty(entry.getKey(), (int) entry.getValue());
//					}
//					if (entry.getValue() instanceof Boolean ) {
//						message.setBooleanProperty(entry.getKey(), (Boolean) entry.getValue());
//					}
				}
				queueSender.setDisableMessageID(true);
				queueSender.setDisableMessageTimestamp(true);
			} catch (Exception e) {
				log.error("Al crear el mensaje para la cola de datos. " + e.getMessage());
				return ready;
			}
		

		try {
			//objMessage.setObject(obj);
			if (message!=null) {
				queueSender.send(message);
				ready = true;
			}
			return false;
			
		} catch (Exception e) {
			log.error("Al enviar el mensaje a la cola -" + queueName + "-." + e.getMessage());
		}

		return ready;
	}
	
	public boolean send(BytesMessage obj) {
		boolean ready = false;

		// Verificar el objeto
		if (obj == null || queueSender == null)
			return ready;

		// Verificar la estructura objMessage
		if (objMessage == null) {
			try {
				objMessage = queueSession.createObjectMessage();
				//BytesMessage bytesMessage = queueSession.createBytesMessage();
				//createBytesMessage.
				//bytesMessage.setObjectProperty(arg0, arg1)
				queueSender.setDisableMessageID(true);
				queueSender.setDisableMessageTimestamp(true);
			} catch (Exception e) {
				log.error("Al crear el mensaje para la cola de datos. " + e.getMessage());
				return ready;
			}
		}

		try {
			//objMessage.setObject(obj);
			queueSender.send(obj);
			ready = true;
		} catch (Exception e) {
			log.error("Al enviar el mensaje a la cola -" + queueName + "-." + e.getMessage());
		}

		return ready;
	}
	
	
	public boolean send(Map<String, Object> obj) {
		boolean ready = false;

		// Verificar el objeto
		if (obj == null || queueSender == null)
			return ready;

		// Verificar la estructura objMessage
		BytesMessage bytesMessage=null;
			try {
				objMessage = queueSession.createObjectMessage();
				 bytesMessage = queueSession.createBytesMessage();
				for (Entry<String, Object> entry : obj.entrySet()) {
					bytesMessage.setObjectProperty(entry.getKey(), entry.getValue());
				}
				//createBytesMessage.
				//bytesMessage.setObjectProperty(arg0, arg1)
				queueSender.setDisableMessageID(true);
				queueSender.setDisableMessageTimestamp(true);
			} catch (Exception e) {
				log.error("Al crear el mensaje para la cola de datos. " + e.getMessage());
				return ready;
			}
		

		try {
			//objMessage.setObject(obj);
			if (bytesMessage!=null) {
				queueSender.send(bytesMessage);
				ready = true;
			}
			return false;
			
		} catch (Exception e) {
			log.error("Al enviar el mensaje a la cola -" + queueName + "-." + e.getMessage());
		}

		return ready;
	}

	/**
	 * Enviar un mensaje por la cola de datos
	 * 
	 * @param obj
	 *            - Objeto a enviar
	 * @return
	 */
	public boolean send(String txt) {
		boolean ready = false;

		// Verificar el objeto
		if (txt == null)
			return ready;

		// Verificar la estructura objMessage
		if (txtMessage == null) {
			try {
				txtMessage = queueSession.createTextMessage();
			} catch (Exception e) {
				log.error("Al crear el mensaje para la cola de datos. " + e.getMessage());
				return ready;
			}
		}

		// Enviar el mensaje
		try {
			txtMessage.setText(txt);
			queueSender.send(txtMessage);
			ready = true;
		} catch (Exception e) {
			log.error("Al enviar el mensaje a la cola -" + queueName + "-." + e.getMessage());
		}

		return ready;
	}

	@Override
	public String toString() {
		return queueName;
	}
}
