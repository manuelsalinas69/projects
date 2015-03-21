package py.com.global.educador.sms.manager;

import ie.omk.smpp.util.GSMConstants;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.cache.SystemParameterCache;
import py.com.global.educador.engine.configuration.EducadorConstants.SystemParameterKey;
import py.com.global.educador.sms.smpp.SmppReceiver;

@Singleton
@Startup
public class ReceiverManager {

	Logger log = Logger.getLogger(ReceiverManager.class);
	SmppReceiver smppReceiver;
	Hashtable<String, SmppReceiver> receivers;

	Map<String, Object> numbers= new HashMap<String, Object>(); 
	@EJB SystemParameterCache systemParameterCache;

	long inactivity=1800000l;//cada 30 minutos por defecto
	//long inactivity=30000l;//cada 30 segundos 
	@PostConstruct
	public void init() {
		startConection();
	}

	private void startConection() {
		try {
			log.debug("Starting ReceiverManager connection...");
			loadServersParameters();
			smppReceiver.Connect();
		} catch (Exception e) {
			log.error("startConection",e);
		}

	}

	private void loadServersParameters() {
		String server=systemParameterCache.getValue(SystemParameterKey.SYSTEM_SMPP_SERVER);
		String carrier=systemParameterCache.getValue(SystemParameterKey.SYSTEM_SMPP_CARRIER);
		String password=systemParameterCache.getValue(SystemParameterKey.SYSTEM_SMPP_PASSWORD);
		String serviceType=systemParameterCache.getValue(SystemParameterKey.SYSTEM_SMPP_SERVICE_TYPE);
		String systemId=systemParameterCache.getValue(SystemParameterKey.SYSTEM_SMPP_SYSTEM_ID);
		String systemType=systemParameterCache.getValue(SystemParameterKey.SYSTEM_SMPP_SYSTEM_TYPE);
		String shortNumber=systemParameterCache.getValue(SystemParameterKey.SYSTEM_SMPP_SHORT_NUMBER);
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
		log.debug("shortNumber--> "+shortNumber);


		receivers = new Hashtable<String, SmppReceiver>();
		// Cargar la direccion del SMSC server
		//		server = "10.90.0.14:5600";
		//		// Cargar la operadora
		//		carrier = "Tigo";
		smppReceiver = new SmppReceiver(server, 0,numbers);
		smppReceiver.setCarrier(carrier);
		// Cargar el SystemNumber
		smppReceiver.setSystemNumber(availablesShorts[0]);
		// systemNumber = smppReceiver.getSystemNumber();
		// log.info("System Number = " + smppReceiver.getSystemNumber());

		// Cargar el SystemID
		smppReceiver.setSystemID(systemId);

		// Cargar Address Range
		smppReceiver.setAddrRange(null);

		// Cargar el Password
		smppReceiver.setPassword(password);

		// Cargar el SystemType
		smppReceiver.setSystemType(systemType);
		//smppReceiver.setServiceType("RTMON");
		// Cargar el ServiceType
		// NO SE UTILIZA
		// smppReceiver.setServiceType(systemParameterManager
		// .getParameter(SMSManagerParameters.SERVICE_TYPE));
		// log.info("Service Type = " + smppReceiver.getServiceType());

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
		smppReceiver.setSourceTON(sourceTON > 0 ? sourceTON
				: GSMConstants.GSM_TON_UNKNOWN);
		log.info("Source TON = " + smppReceiver.getSourceTON());

		// Cargar el Source NPI
		String sourceNPIStr = "";
		int sourceNPI = -1;
		if (sourceNPIStr != null) {
			try {
				sourceNPI = Integer.parseInt(sourceTONStr);
			} catch (Exception e) {
				log.error("Invaid sourceNPI --> " + sourceNPIStr);
			}
		}
		smppReceiver.setSourceNPI(sourceNPI > 0 ? sourceNPI
				: GSMConstants.GSM_NPI_UNKNOWN);
		log.debug("Source NPI = " + smppReceiver.getSourceNPI());

		// Cargar el TON
		String systemTONStr = "";
		int systemTON = -1;
		if (systemTONStr != null) {
			try {
				systemTON = Integer.parseInt(systemTONStr);
			} catch (Exception e) {
				log.error("Invaid systemTON --> " + systemTONStr);
			}
		}
		smppReceiver.setTargetTON(systemTON > 0 ? systemTON
				: GSMConstants.GSM_TON_UNKNOWN);
		log.debug("System TON = " + smppReceiver.getTargetTON());

		// Cargar el Target NPI
		String targetNPIStr = "";
		int targetNPI = -1;
		if (targetNPIStr != null) {
			try {
				targetNPI = Integer.parseInt(targetNPIStr);
			} catch (Exception e) {
				log.error("Invaid targetNPI --> " + targetNPIStr);
			}
		}
		smppReceiver.setTargetNPI(targetNPI > 0 ? targetNPI
				: GSMConstants.GSM_NPI_UNKNOWN);
		log.debug("System NPI = " + smppReceiver.getTargetNPI());

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
		smppReceiver.setConnect(connectNum == 1);

		smppReceiver.setIndex(0);

		if (smppReceiver.isAttributesEstablished()) {
			if (!receivers.contains(smppReceiver.getCarrier())) {
				receivers.put(smppReceiver.getCarrier(), smppReceiver);
				log.info("SMPPReceiver registered --> "
						+ smppReceiver.toString());
			} else {
				log.error("SMPPReceiver already registered --> "
						+ smppReceiver.toString());
			}
		} else {
			log.error("SMPPReceiver NOT registered, incomplete parameters --> "
					+ smppReceiver.toString());
		}
	}

	@Schedule(hour="*",minute="*/1",persistent=false)
	public void checkConnection(){
		try {
			log.debug("Checking connection..");
			checkConnection0();
		} catch (Exception e) {
			log.error(e);
		}
	}

	private void checkConnection0() {
		SmppReceiver smppReceiver = null;
		Enumeration<SmppReceiver> receiversEnum = receivers.elements();
		while (receiversEnum.hasMoreElements()) {
			smppReceiver = receiversEnum.nextElement();
			if (smppReceiver != null) {
				if (!smppReceiver.isConnected() && smppReceiver.isConnect()) {
					// conectar
					try {
						// Iniciar conexion con el SMPP server
						synchronized (this) {
							if (!smppReceiver.Connect()) {
								log.error("Binding req error... --> "
										+ smppReceiver.toString());
							}
						}
						// this.close();
					} catch (Exception e) {
						log.error("Error --> " + e.getCause(), e);
					}
				}
				else if(smppReceiver.isConnect() && smppReceiver.isConnected()){
					long currentTime = System.currentTimeMillis() - smppReceiver.getLastActivityTime();
					log.debug("Current Inactivy time--> "+currentTime);
					
					if ((currentTime) > getInactivityParamerter()) {
						log.debug("Cerrando conexion al SMSC por inactividad... --> carrier="
								+ smppReceiver.getCarrier());
						
						smppReceiver.reconnect();
					}
				}


			}
		}
	}

	public long getInactivityParamerter(){
		try {
			long inac0= (Long.parseLong(systemParameterCache.getValue("system.smpp.receiver.check.inactivity")))*1000l*60l;
			log.debug("Inactivy Time--->"+inac0);
			return inac0;
		} catch (Exception e) {
			log.error("getInactivityParamerter"+e);
		}
		return inactivity;
	}
	
	@PreDestroy
	public void destroy(){
		log.info("Closing all connections...");
		try {
			SmppReceiver smppReceiver = null;
			Enumeration<SmppReceiver> receiversEnum = receivers.elements();
			while (receiversEnum.hasMoreElements()) {
				smppReceiver = receiversEnum.nextElement();
				if (smppReceiver!=null) {
					try {
						smppReceiver.close();
					} catch (Exception e) {
						log.error(e);
					}
					
				}
			}
			
		} catch (Exception e) {
			log.error(e);
		}
	}
}
