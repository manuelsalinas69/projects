package py.com.global.educador.gui.managers;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;

import py.com.global.educador.gui.entity.Usuario;
import py.com.global.educador.gui.utils.GeneralHelper;

@Name("cambiarContrasenaController")
public class CambiarContrasenaController implements Serializable {

	private static final long serialVersionUID = 1L;

	@In StatusMessages statusMessages;
	@In EntityManager entityManager;
	@In	Usuario usuario;
	Logger log = Logger.getLogger(CambiarContrasenaController.class);
	private String contrasenaActual;
	private String contrasenaNueva;
	private String contrasenaConfirma;

	public void init() {
	}

	public String updateContrasena() {
		String contrasenaActualMd5=GeneralHelper.MD5(contrasenaActual);
		if (!contrasenaActualMd5.equals(usuario.getContrasena())) {
			statusMessages.add(Severity.ERROR,"Los datos ingresados son erróneos");
			return null;
		}
		if (!contrasenaNueva.equals(contrasenaConfirma)) {
			statusMessages.add(Severity.ERROR,
					"La contraseña nueva no coincide con su confirmación");
			return null;
		}
		usuario.setContrasena(GeneralHelper.MD5(contrasenaNueva));
		entityManager.merge(usuario);
		entityManager.flush();
		statusMessages.add(Severity.INFO,
				"La contraseña ha sido modificada satisfactoriamente");
		return "updated";

	}

	public String getContrasenaActual() {
		return contrasenaActual;
	}

	public void setContrasenaActual(String contrasenaActual) {
		this.contrasenaActual = contrasenaActual;
	}

	public String getContrasenaNueva() {
		return contrasenaNueva;
	}

	public void setContrasenaNueva(String contrasenaNueva) {
		this.contrasenaNueva = contrasenaNueva;
	}

	public String getContrasenaConfirma() {
		return contrasenaConfirma;
	}

	public void setContrasenaConfirma(String contrasenaConfirma) {
		this.contrasenaConfirma = contrasenaConfirma;
	}

}
