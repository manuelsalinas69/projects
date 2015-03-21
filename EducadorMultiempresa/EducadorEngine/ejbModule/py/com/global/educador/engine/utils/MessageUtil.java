package py.com.global.educador.engine.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.dto.QueueMessage;

public class MessageUtil {

	static Logger log=Logger.getLogger(MessageUtil.class);
	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return is.readObject();
	}
	
	public static QueueMessage getQueueMessage(byte[] data){
		try {
			return (QueueMessage) deserialize(data);
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}
}
