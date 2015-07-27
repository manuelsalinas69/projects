package py.com.global.educador.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the SUSCRIPTOR_PROYECTO database table.
 * 
 */
@Embeddable
public class SuscriptorProyectoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private long idProyecto;
	private long idSuscriptor;

	public SuscriptorProyectoPK() {
	}

	@Column(name="ID_PROYECTO")
	public long getIdProyecto() {
		return this.idProyecto;
	}
	public void setIdProyecto(long idProyecto) {
		this.idProyecto = idProyecto;
	}

	@Column(name="ID_SUSCRIPTOR")
	public long getIdSuscriptor() {
		return this.idSuscriptor;
	}
	public void setIdSuscriptor(long idSuscriptor) {
		this.idSuscriptor = idSuscriptor;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SuscriptorProyectoPK)) {
			return false;
		}
		SuscriptorProyectoPK castOther = (SuscriptorProyectoPK)other;
		return 
			(this.idProyecto == castOther.idProyecto)
			&& (this.idSuscriptor == castOther.idSuscriptor);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idProyecto ^ (this.idProyecto >>> 32)));
		hash = hash * prime + ((int) (this.idSuscriptor ^ (this.idSuscriptor >>> 32)));
		
		return hash;
	}
}