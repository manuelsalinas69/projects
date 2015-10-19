package py.com.global.educador.engine.configuration;

import java.util.regex.Pattern;

public class EducadorConstants {

	
	public static class Queues{
		public static final String SMS_IN="queue/QUEUE_SMS_IN";
		public static final String SUBSCRIPTION_IN="queue/QUEUE_SUBSCRIPTION_IN";
		public static final String CLIENT_RESPONSE_IN="queue/QUEUE_CLIENT_RESPONSE_IN";
		public static final String CLIENT_TASK_EVENT="queue/QUEUE_CLIENT_TASK_EVENT";
		public static final String NOTIFICATION_REQUEST="queue/QUEUE_NOTIFICATION_REQUEST";
		public static final String SMS_OUT="queue/QUEUE_SMS_OUT";
		public static final String BAJA_EVENT="queue/QUEUE_BAJA_EVENT";
	}
	
	public static final class Sequences{
		
	}
	
	public static final class Constants{
		public static final String ENVIO_INMEDIATO="ENVIO_INMEDIATO";
	}
	
	public static final class Patterns{
		public static final String LISTA_REGX="^\\s*LISTA";
		public static final String ALTA_REGEX="^\\s*(ALTA|EDUCA)";
		public static final String BAJA_REGEX="^\\s*(BAJA|SALIR)";
		public static final String SUSCRIPTION_REQUEST_REGEX=LISTA_REGX+"|"+ALTA_REGEX+"|"+BAJA_REGEX;
		
		public static final Pattern LISTA_PATTERN= Pattern.compile(LISTA_REGX,Pattern.CASE_INSENSITIVE);
		public static final Pattern ALTA_PATTERN= Pattern.compile(ALTA_REGEX,Pattern.CASE_INSENSITIVE);
		public static final Pattern BAJA_PATTERN= Pattern.compile(BAJA_REGEX,Pattern.CASE_INSENSITIVE);
		public static final Pattern SUSCRIPTION_REQUEST_PATTERN=Pattern.compile(SUSCRIPTION_REQUEST_REGEX,Pattern.CASE_INSENSITIVE);
	}
	
	public static final class SystemParameterKey{
		public static final String MESSAGE_FOR_WELCOME_1= "message.for.welcome_1";
		public static final String MESSAGE_FOR_WELCOME_2= "message.for.welcome_2";
		
		public static final String MESSAGE_FOR_SUCCESS_SUBSCRIPTION_AUTO= "message.for.success.subscription.auto";
		public static final String MESSAGE_FOR_SUCCESS_SUBSCRIPTION_MANUAL= "message.for.success.subscription.manual";
		
		public static final String MESSAGE_FOR_SUCCESS_UNSUBSCRIPTION_AUTO= "message.for.success.unsubscription.auto";
		public static final String MESSAGE_FOR_SUCCESS_UNSUBSCRIPTION_MANUAL= "message.for.success.unsubscription.manual";
		
		
		public static final String MESSAGE_FOR_SUBSCRIBER_EXIST_ERROR= "message.for.subscriber.exist.error";
		public static final String MESSAGE_FOR_SUBSCRIBER_DOES_NOT_EXIST_ERROR= "message.for.subscriber.does.not.exist.error";
		
		
		public static final String SYSTEM_SMPP_CARRIER="system.smpp.carrier";
		public static final String SYSTEM_SMPP_SERVER="system.smpp.server";
		public static final String SYSTEM_SMPP_SYSTEM_ID="system.smpp.system.id";
		public static final String SYSTEM_SMPP_PASSWORD="system.smpp.password";
		public static final String SYSTEM_SMPP_SYSTEM_TYPE="system.smpp.system.type";
		public static final String SYSTEM_SMPP_SERVICE_TYPE="system.smpp.service.type";
		public static final String SYSTEM_SMPP_SHORT_NUMBER="system.smpp.short.number";
		public static final String SYSTEM_ENGINE_PROCESS_FLOW_METHOD="system.engine.process.flow.method";
		public static final String SYSTEM_ENGINE_PROCESS_MODULE_SUBSCRITIONS_STATE="system.engine.process.module.subscritions.state";
		public static final String SYSTEM_ENGINE_PROCESS_SESSION_TIMEOUT_HOURS="system.engine.process.session.timeout.hours";
		public static final String SYSTEM_ENGINE_PROCESS_SESSION_TIMEOUT_MINUTES="system.engine.process.session.timeout.minutes";
		public static final String SYSTEM_ENGINE_PROCESS_FLOW_EVALUATION_MESSAGES_SUFFIX="system.engine.process.flow.evaluation.messages.suffix";
		public static final String SYSTEM_ENGINE_PROCESS_RESPONSE_ERRORS_UNKNOW="system.engine.process.response.errors.unknow";
		public static final String SYSTEM_ENGINE_PROCESS_RESPONSE_ERRORS_EMPTY="system.engine.process.response.errors.empty";
		public static final String SYSTEM_ENGINE_PROCESS_RESPONSE_ERRORS_MISMATCH="system.engine.process.response.errors.mismatch";
		public static final String SYSTEM_ENGINE_PROCESS_RESPONSE_SUCCESS_FINAL_MESSAGE="system.engine.process.response.success.final.message";
		public static final String SYSTEM_ENGINE_PROCESS_FLOW_ATTEMPS_SMSC_REPLYERROR="system.engine.process.flow.attemps.smsc.replyerror";
		public static final String SYSTEM_ENGINE_PROCESS_FLOW_ATTEMPS_SMSC_NOREPLY="system.engine.process.flow.attemps.smsc.noreply";
		public static final String SYSTEM_SERVER_HOST="system.server.host";
		public static final String SYSTEM_SERVER_PORT="system.server.port";
	}
	
	
	public static final class QueueMessageParamKey{
		public static final String MESSAGE="MESSAGE";
		public static final String MESSAGE_ID="MESSAGE_ID";
		public static final String TYPE="TYPE";
		public static final String MODULE_ID="MODULE_ID";
		public static final String SUBSCRIBER_ID="SUBSCRIBER_ID";
		public static final String EVALUATION_ID="EVALUATION_ID";
		public static final String SHORT_NUMBER="SHORT_NUMBER";
		public static final String CONFIGURATION="CONFIGURATION";
		public static final String SUBSCRIBER_NUMBER="SUBSCRIBER_NUMBER";
		public static final String SESSION_REQUIRED="SESSION_REQUIRED";
		public static final String SUBSCRIBER_EXECUTION_DETAIL_ID="SUBSCRIBER_EXECUTION_DETAIL_ID";
		public static final String SUBSCRIBER_EVALUATION_ID="SUBSCRIBER_EVALUATION_ID";
		public static final String SUBSCRIPTION_TYPE="SUBSCRIPTION_TYPE";
		public static final String FORCE_SEND="FORCE_SEND";
		public static final String JMS_MESSAGE_ID="JMS_MESSAGE_ID";
		public static final String RECEPTION_DATE="RECEPTION_DATE";
		public static final String OPERATION_TYPE="OPERATION_TYPE";
		public static final String PROJECT_ID="PROJECT_ID";
		public static final String START_DATE="START_DATE";
		public static final String END_DATE="END_DATE";
		public static final String LOG_ID="LOG_ID";
		public static final String COMMAND_STATUS="COMMAND_STATUS";
		public static final String SEQ_NUM="SEQ_NUM";
		
	}

	public static final class ErrorCode{
		public static final String SUCCESS="SUCCESS";
		public static final String DEFAULT_ERROR="DEFAULT_ERROR";
		public static final String SUBSCRIBER_EXIST_ERROR="SUBSCRIBER_EXIST_ERROR";
		public static final String SUBSCRIBER_DOES_NOT_EXIST_ERROR="SUBSCRIBER_DOES_NOT_EXIST_ERROR";
		
	}
}
