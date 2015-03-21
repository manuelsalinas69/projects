package py.com.global.educador.sms.messages;

import ie.omk.smpp.message.SubmitSMResp;

import java.io.Serializable;

/**
 * 
 * @author Lino Chamorro
 * 
 */
public class SubmitSMRespWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9523435339945262L;

	private int sequenceNum;
	private int commandStatus;

	public SubmitSMRespWrapper() {
		super();
	}

	public SubmitSMRespWrapper(SubmitSMResp packetResponse) {
		super();
		this.sequenceNum = packetResponse.getSequenceNum();
		this.commandStatus = packetResponse.getCommandStatus();
	}

	public int getSequenceNum() {
		return sequenceNum;
	}

	public void setSequenceNum(int sequenceNum) {
		this.sequenceNum = sequenceNum;
	}

	public int getCommandStatus() {
		return commandStatus;
	}

	public void setCommandStatus(int commandStatus) {
		this.commandStatus = commandStatus;
	}

}
