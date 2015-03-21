package py.com.global.educador.sms.manager;

import ie.omk.smpp.util.GSMConstants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.AccessTimeout;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.cache.SystemParameterCache;
import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.configuration.EducadorConstants.SystemParameterKey;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.sms.smpp.SmppTransmitter;

@Singleton
@Startup
public class SenderManager implements Serializable{

	/**
	 * 
	 */
	
	SmppTransmitter smppSender;
	Long lastActivity=null;
	@EJB SystemParameterCache systemParameterCache;
	
	Map<String,Object> numbers= new HashMap<String, Object>();
	Map<Integer, Object> messages;
	
	
	Logger log=Logger.getLogger(SenderManager.class);
	
	private static final long serialVersionUID = 1L;
	
	@PostConstruct
	public void init(){
		log.info("Starting conection...");
		messages=new ConcurrentHashMap<Integer,Object>();
		startConnection();
	}

	private void startConnection() {
		try {
			loadParameters();
			smppSender.Connect();
		} catch (Exception e) {
			log.error("startConnection",e);
		}
		
		
	}

	private void loadParameters() {
		String server=systemParameterCache.getValue(SystemParameterKey.SYSTEM_SMPP_SERVER);
		String carrier=systemParameterCache.getValue(SystemParameterKey.SYSTEM_SMPP_CARRIER);
		String password=systemParameterCache.getValue(SystemParameterKey.SYSTEM_SMPP_PASSWORD);
		String serviceType=systemParameterCache.getValue(SystemParameterKey.SYSTEM_SMPP_SERVICE_TYPE);
		String systemId=systemParameterCache.getValue(SystemParameterKey.SYSTEM_SMPP_SYSTEM_ID);
		String systemType=systemParameterCache.getValue(SystemParameterKey.SYSTEM_SMPP_SYSTEM_TYPE);
		String shortsNumbers=systemParameterCache.getValue(SystemParameterKey.SYSTEM_SMPP_SHORT_NUMBER);
		
		String[] availablesShorts=shortsNumbers.split(",");
		
		for (String shortN : availablesShorts) {
			numbers.put(shortN.trim(), shortN.trim());
		}
		
		log.debug("SMPP PARAMETERS--");
		log.debug("server--> "+server);
		log.debug("carrier--> "+carrier);
		log.debug("password--> "+password);
		log.debug("serviceType--> "+serviceType);
		log.debug("systemId--> "+systemId);
		log.debug("systemType--> "+systemType);
		log.debug("shortNumber--> "+shortsNumbers);

		//transmitters = new Hashtable<String, SmppTransmitter>();
		// Cargar la direccion del SMSC server
		//server = "10.90.0.14:5600";
		// Cargar la operadora
		//carrier = "Tigo";
		if (server == null || server.trim().length() == 0) {
			log.error("Direccion IP y puerto del SMSC Requeridos");
			return;
		} else if (carrier == null || carrier.trim().length() == 0) {
			log.info("Operadora requerido");
			return;
		} else {
			carrier = carrier.trim().toUpperCase();
			log.info("Direccion IP y puerto del SMSC = " + server
					+ ", Operadora = " + carrier);
			// Instanciar sms sender
			smppSender = new SmppTransmitter(server, 0);

//			// Cargar Address Range
//			smppSender.setAddrRange(systemparameterManager
//					.getParameter(SMSManagerParameters.ADDR_RANGE));
//			log.info("Address Range = " + smppSender.getAddrRange());
//
//			// Carrier
			smppSender.setCarrier(carrier);

			// Cargar el SystemID
//			smppSender.setSystemID("tigo");
			smppSender.setSystemID(systemId);
			log.info("System ID = " + smppSender.getSystemID());

			// Cargar el Password
//			smppSender.setPassword("wpsd");
			smppSender.setPassword(password);

			// Cargar el SystemType
			//smppSender.setSystemType("tigo");
			smppSender.setSystemType(systemType);

			// Cargar el ServiceType
//			smppSender.setServiceType("RTMON");
//			smppSender.setServiceType("CTMON");
			smppSender.setServiceType(serviceType);
			log.debug("Service Type---> "+smppSender.getServiceType());
			// Cargar el TON
			String sourceTONStr = "";
			int sourceTON = -1;
			if (sourceTONStr != null) {
				try {
					sourceTON = Integer.parseInt(sourceTONStr);
				} catch (Exception e) {
					log.error("Invaid sourceTON --> " + sourceTONStr);
				}
			}
			smppSender.setSourceTON(sourceTON > 0 ? sourceTON
					: GSMConstants.GSM_TON_UNKNOWN);
			log.info("Source TON = " + smppSender.getSourceTON());

			// Cargar el Source NPI
			String sourceNPIStr ="";
			int sourceNPI = -1;
			if (sourceNPIStr != null) {
				try {
					sourceNPI = Integer.parseInt(sourceTONStr);
				} catch (Exception e) {
					log.error("Invaid sourceNPI --> " + sourceNPIStr);
				}
			}
			smppSender.setSourceNPI(sourceNPI > 0 ? sourceNPI
					: GSMConstants.GSM_NPI_UNKNOWN);
			log.info("Source NPI = " + smppSender.getSourceNPI());

			// Cargar el TON
			String systemTONStr = "";
			int systemTON = -1;
			if (systemTONStr != null) {
				try {
					systemTON = Integer.parseInt(systemTONStr);
				} catch (Exception e) {
					log.error("Invaid systemTON --> " + systemTONStr );
				}
			}
			smppSender.setTargetTON(systemTON > 0 ? systemTON
					: GSMConstants.GSM_TON_UNKNOWN);
			log.info("System TON = " + smppSender.getTargetTON());

			// Cargar el Target NPI
			String targetNPIStr = "";
			int targetNPI = -1;
			if (targetNPIStr != null) {
				try {
					targetNPI = Integer.parseInt(targetNPIStr);
				} catch (Exception e) {
					log.error("Invaid targetNPI --> " + targetNPIStr );
				}
			}
			smppSender.setTargetNPI(targetNPI > 0 ? targetNPI
					: GSMConstants.GSM_NPI_UNKNOWN);
			log.info("System NPI = " + smppSender.getTargetNPI());

			// ShortCode
			smppSender.setSystemNumber(availablesShorts[0]);

			// connect = 1 true
			// connect != 1 false
			String connect = "1";
			int connectNum = -1;
			if (connect != null) {
				try {
					connectNum = Integer.parseInt(connect);
				} catch (Exception e) {
					log.error("Invalid connect value --> " + connect );
				}
			}
			smppSender.setConnect(connectNum == 1);
			//this.timeLastConnectParametersLoad = System.currentTimeMillis();
			log.info("Connect = " + smppSender.isConnect());
			smppSender.setIndex(0);
			if (smppSender.isAttributesEstablished()) {
//				if (!transmitters.contains(smppSender.getCarrier())) {
//					transmitters.put(smppSender.getCarrier(), smppSender);
//					log.info("SMPPTransmitter registered --> "
//							+ smppSender.toString());
//				} else {
//					log.error("SMPPTransmitter already registered --> "
//							+ smppSender.toString());
//				}
			} else {
				log.error("SMPPTransmitter NOT registered, incomplete parameters --> "
						+ smppSender.toString());
			}
		}
		
	}
	@Lock(LockType.WRITE)
	@AccessTimeout(value=20,unit=TimeUnit.MINUTES)
	public void send(QueueMessage message){
		Long currentTime=System.currentTimeMillis();
		Long systemDelay=getSystemDelay();
		if (lastActivity!=null && (currentTime - lastActivity)<systemDelay) {
			try {
				Thread.sleep(systemDelay-(currentTime - lastActivity));
			} catch (Exception e) {
				log.error(e);
			}
		}
		lastActivity=System.currentTimeMillis();
		Long msgId=(Long) message.getParam(QueueMessageParamKey.MESSAGE_ID);
		String susNro=(String) message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER);
		String msg=(String) message.getParam(QueueMessageParamKey.MESSAGE);
		String shortCode=(String) message.getParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER);
		messages.put(msgId.intValue(),message);
		boolean _r=smppSender.send(msgId.intValue(), shortCode, susNro, msg, null);
		log.trace("Resultado Envio ["+susNro+"]----> "+_r);
	}
	private long getSystemDelay() {
		try {
			return Long.parseLong(systemParameterCache.getValue("system.engine.process.sender.delay"));
		} catch (Exception e) {
			
		}
		return 0;
	}

	@Lock(LockType.READ)
	public QueueMessage getMessage(Integer seqNum){
		return (QueueMessage) messages.remove(seqNum);
	}
	
	
	
	
//	@Schedule(hour="*",minute="*",second="0",persistent=false)
//	public void send(Timer timer){
//		log.info("---------------------Sending MSG....");
//		try {
//			String[] numeros={"0981697458","0985206823"};
//			
//			boolean _r=false;
//			long msgId=0;
//			for (String n : numeros) {
//				log.info("Sending to---> "+n);
//				_r=smppSender.send(++msgId, "606",
//						n, "Mensaje de Prueba. Responde a este mensaje con cualquier texto");
//				log.info("Resultado Envio ["+n+"]----> "+_r);
//			}
//		
//			log.info("-------------------MSG SENDED---------------------");
//		} catch (Exception e) {
//			log.error("send",e);
//		}
//		
//	}
	
	@PreDestroy
	public void destroy(){
		
		if (smppSender!=null) {
			try {
				log.info("Closing Sender Connection...");
				smppSender.close();
				log.info("Sender Connection Closed");
			} catch (Exception e) {
				log.info("SenderManager.destroy(): "+e);
			}
		}
	}

}
