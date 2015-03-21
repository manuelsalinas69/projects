package py.com.global.educador.sms.smpp;

/**
 * @author Lino Chamorro
 */
public class SmppAttributes {

	protected String hostName = null;
	protected int port = 0;
	protected String systemID = null;
	protected String systemType = null;
	protected String serviceType = null;
	protected String password = null;
	// NO SE UTILIZA
	// protected String serviceType = null;
	protected int sourceTON = 0;
	protected int sourceNPI = 0;
	protected int targetTON = 0;
	protected int targetNPI = 0;
	// protected String sourceAddress = null;
	protected String systemNumber = null;
	protected String addrRange = null;
	protected String carrier = null;
	protected boolean connect = false;
	protected int index;

	/**
	 * @return Returns the hostName.
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return Returns the port.
	 */
	public int getPort() {
		return port;
	}

	public String getAddrRange() {
		return addrRange;
	}

	public void setAddrRange(String addrRange) {
		this.addrRange = addrRange;
	}

	/**
	 * @return Returns the sourceNPI.
	 */
	public int getSourceNPI() {
		return sourceNPI;
	}

	/**
	 * @param sourceNPI
	 *            The sourceNPI to set.
	 */
	public void setSourceNPI(int sourceNPI) {
		this.sourceNPI = sourceNPI;
	}

	/**
	 * @return Returns the sourceTON.
	 */
	public int getSourceTON() {
		return sourceTON;
	}

	/**
	 * @param sourceTON
	 *            The sourceTON to set.
	 */
	public void setSourceTON(int sourceTON) {
		this.sourceTON = sourceTON;
	}

	/**
	 * @return Returns the systemID.
	 */
	public String getSystemID() {
		return systemID;
	}

	/**
	 * @return the targetTON
	 */
	public int getTargetTON() {
		return targetTON;
	}

	/**
	 * @param targetTON
	 *            the targetTON to set
	 */
	public void setTargetTON(int targetTON) {
		this.targetTON = targetTON;
	}

	/**
	 * @return the targetNPI
	 */
	public int getTargetNPI() {
		return targetNPI;
	}

	/**
	 * @param targetNPI
	 *            the targetNPI to set
	 */
	public void setTargetNPI(int targetNPI) {
		this.targetNPI = targetNPI;
	}

	/**
	 * @param systemID
	 *            The systemID to set.
	 */
	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}

	/**
	 * @return Returns the systemType.
	 */
	public String getSystemType() {
		return systemType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * @param systemType
	 *            The systemType to set.
	 */
	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getSystemNumber() {
		return systemNumber;
	}

	public void setSystemNumber(String systemNumber) {
		this.systemNumber = systemNumber;
	}

	@Override
	public String toString() {
		return "SmppAttributes [hostName=" + hostName + ", port=" + port
				+ ", systemID=" + systemID + ", systemType=" + systemType
				+ ", password=" + password + ", sourceTON=" + sourceTON
				+ ", sourceNPI=" + sourceNPI + ", targetTON=" + targetTON
				+ ", targetNPI=" + targetNPI + ", systemNumber=" + systemNumber
				+ ", addrRange=" + addrRange + ", carrier=" + carrier
				+ ", connect=" + connect + ", index=" + index + "]";
	}

	public boolean isConnect() {
		return connect;
	}

	public void setConnect(boolean connect) {
		this.connect = connect;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isAttributesEstablished() {
		if (hostName == null || hostName.trim().length() == 0) {
			return false;
		}
		if (port < 1) {
			return false;
		}
		if (systemID == null || systemID.trim().length() == 0) {
			return false;
		}
		if (systemType == null || systemType.trim().length() == 0) {
			return false;
		}
		if (password == null || password.trim().length() == 0) {
			return false;
		}
		if (carrier == null || carrier.trim().length() == 0) {
			return false;
		}
		if (systemNumber == null || systemNumber.trim().length() == 0) {
			return false;
		}
		return true;
	}

}
