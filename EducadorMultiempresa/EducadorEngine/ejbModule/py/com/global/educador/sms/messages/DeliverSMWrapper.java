package py.com.global.educador.sms.messages;

import ie.omk.smpp.message.DeliverSM;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author Lino Chamorro
 * 
 */
public class DeliverSMWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4128730702104038043L;

	private int sequenceNum;
	private String source;
	private String destination;
	private String messageText;
	private String carrier;
	private Date sentDate;

	public DeliverSMWrapper() {
		super();
	}

	public DeliverSMWrapper(DeliverSM pk, String carrier) {
		super();
		this.sequenceNum = pk.getSequenceNum();
		this.source = pk.getSource().getAddress();
		this.destination = pk.getDestination().getAddress();
		this.messageText = pk.getMessageText();
		this.carrier = carrier;
		// obtener del mensaje enviado
		this.sentDate = new Date();
	}

	public int getSequenceNum() {
		return sequenceNum;
	}

	public void setSequenceNum(int sequenceNum) {
		this.sequenceNum = sequenceNum;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	@Override
	public String toString() {
		return "DeliverSMWrapper [sequenceNum=" + sequenceNum + ", source="
				+ source + ", destination=" + destination + ", messageText="
				+ messageText + ", carrier=" + carrier + ", sentDate="
				+ sentDate + "]";
	}

}
