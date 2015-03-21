package py.com.global.educador.gui.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage.Severity;

import py.com.global.educador.gui.dto.ValidationResult;
import py.com.global.educador.gui.entity.Modulo;
import py.com.global.educador.gui.entity.ParametroProyecto;
import py.com.global.educador.gui.entity.ParametroProyectoId;
import py.com.global.educador.gui.entity.ParametroSistema;
import py.com.global.educador.gui.entity.Proyecto;
import py.com.global.educador.gui.entity.SuscriptorProyecto;
import py.com.global.educador.gui.entity.Usuario;
import py.com.global.educador.gui.enums.ABMActions;
import py.com.global.educador.gui.enums.EstadoRegistro;

@Name("proyectoHome")
public class ProyectoHome extends EntityHome<Proyecto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String[] params={"message.for.welcome_2","message.for.welcome_1","system.engine.process.response.errors.mismatch","system.engine.process.response.errors.empty",
			"system.engine.process.response.errors.null","system.engine.process.flow.evaluation.messages.suffix","system.engine.process.response.success.final.message",
			"message.for.success.unsubscription.manual","message.for.success.unsubscription.auto","message.for.success.subscription.manual","message.for.success.subscription.auto"};

	public static final String	PARAM1_PARAMETRO="message.for.success.subscription.auto";
	public static final String	PARAM1_DESCRIPCION="Mensaje de suscripcion automatica";
	public static final String PARAM1_VALOR= "Escriba el mensaje";

	public static final String PARAM2_PARAMETRO="message.for.success.subscription.manual";
	public static final String PARAM2_DESCRIPCION="Mensaje de suscripcion manual";
	public static final String PARAM2_VALOR= "Escriba el mensaje";

	public static final String PARAM3_PARAMETRO="message.for.success.unsubscription.auto";
	public static final String PARAM3_DESCRIPCION="Mensaje de baja automatica";
	public static final String PARAM3_VALOR= "Escriba el mensaje";

	public static final String PARAM4_PARAMETRO="message.for.success.unsubscription.manual";
	public static final String PARAM4_DESCRIPCION="Mensaje de baja manual";
	public static final String PARAM4_VALOR= "Escriba el mensaje";

	public static final String PARAM5_PARAMETRO="message.for.subscriber.exist.error";
	public static final String PARAM5_DESCRIPCION="Mensaje de existencia";
	public static final String PARAM5_VALOR= "Escriba el mensaje";

	public static final String PARAM6_PARAMETRO="message.for.subscriber.does.not.exist.error";
	public static final String PARAM6_DESCRIPCION="Mensaje de no existencia";
	public static final String PARAM6_VALOR= "Escriba el mensaje";




	@In
	Usuario usuario;

	@In
	EntityManager entityManager;

	private List<ParametroProyecto> parametros;


	public void init(){
		if (isIdDefined() || isManaged()) {
			loadParametros();
		}
		else{
			parametros=new ArrayList<ParametroProyecto>(cargarParametrosProyecto());
		}
		
	}
	
	private void loadParametros() {
		//List<ParametroProyecto> pp= new ArrayList<ParametroProyecto>(getInstance().getParametroProyectos());
		if (parametros==null) {
			parametros= new ArrayList<ParametroProyecto>();
		}
		ParametroProyectoId id;
		
		for (String _param : params) {
			id= new ParametroProyectoId(_param, getInstance().getIdProyecto());
			ParametroProyecto pp= entityManager.find(ParametroProyecto.class, id);
			if (pp==null) {
				pp=loadFromSystemParams(_param);
			}
			parametros.add(pp);
		}
		
		
	}

	private ParametroProyecto loadFromSystemParams(String _p) {
		ParametroSistema ps= entityManager.find(ParametroSistema.class, _p);
		ParametroProyecto param = new ParametroProyecto();
		param.setId(new ParametroProyectoId(_p, null));
		if (ps!=null) {
			param.setDescripcion(ps.getDescripcion());
			param.setValor(ps.getValor());
			param.setEditable(ps.getEditable());
			param.setVisible(ps.getVisible());
		}
		return param;
	}

	public void setProyectoIdProyecto(Long id) {
		setId(id);
	}

	public Long getProyectoIdProyecto() {
		return (Long) getId();
	}

	@Override
	protected Proyecto createInstance() {
		Proyecto proyecto = new Proyecto();
		proyecto.setParametroProyectos(cargarParametrosProyecto());
		return proyecto;
	}

	private Set<ParametroProyecto> cargarParametrosProyecto() {
		Set<ParametroProyecto> listaParam= new HashSet<ParametroProyecto>();

		ParametroProyecto param;

		for (String _p : params) {
			ParametroSistema ps= entityManager.find(ParametroSistema.class, _p);
			param= new ParametroProyecto();
			param.setId(new ParametroProyectoId(_p, null));
			if (ps!=null) {
				param.setDescripcion(ps.getDescripcion());
				param.setValor(ps.getValor());
			}
			listaParam.add(param);
		}

		return listaParam;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		//parametros= new ArrayList<ParametroProyecto>(getInstance().getParametroProyectos());
	}



	public boolean isWired() {
		return true;
	}

	public Proyecto getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Modulo> getModulos() {
		return getInstance() == null ? null : new ArrayList<Modulo>(
				getInstance().getModulos());
	}

	public List<SuscriptorProyecto> getSuscriptorProyectos() {
		return getInstance() == null ? null
				: new ArrayList<SuscriptorProyecto>(getInstance()
						.getSuscriptorProyectos());
	}

	public List<ParametroProyecto> getParametroProyectos() {
		return getInstance() == null ? null : new ArrayList<ParametroProyecto>(getInstance().getParametroProyectos());
	}

	public List<ParametroProyecto> getParametros() {
		return parametros;
	}

	public void setParametros(List<ParametroProyecto> parametros) {
		this.parametros = parametros;
	}


	@Override
	public String persist() {
		//throw new Exception("De onda");
		ValidationResult vr=checkData(ABMActions.PERSIST);
		//vr.getMessage().toString();
		if (vr!=null) {
			if (!vr.isSuccessful()) {
				getStatusMessages().add(Severity.ERROR, vr.getMessage());
				return null;
			}
		}
		else{
			getStatusMessages().add(Severity.ERROR, "No se pudo validar los datos ingresados, por favor, verifique con su administrador o intentelo de nuevo");
			return null;
		}
		
		getInstance().setUsuarioAlta(usuario);
		getInstance().setEstadoRegistro(EstadoRegistro.ACTIVO.name());
		getInstance().setEstadoProyecto(EstadoRegistro.ACTIVO.name());
		getInstance().setFechaAlta(new Date());

		if (super.persist().equals("persisted")){
			ParametroProyectoId paramId;

			for (ParametroProyecto param : parametros) {
				paramId=param.getId();
				paramId.setIdProyecto(getInstance().getIdProyecto());
				param.setId(paramId);
				param.setProyecto(getInstance());
				entityManager.persist(param);
			}
			entityManager.flush();
			getInstance().setParametroProyectos(new HashSet<>(parametros));
			return "persisted";
		}

		return null;
	}

	private ValidationResult checkData(ABMActions abmAction) {
		switch (abmAction) {
		case PERSIST:
		case UPDATE:
			if (existShortNumber()) {
				return new ValidationResult(false, "El numero corto ingresado ya se encuentra en uso por otro Proyecto Activo, por favor verifique");
			}
			if (!shortNumberIsParams()) {
				return new ValidationResult(false, "El numero corto ingresado no se encuentra validado por el sistema; debe estar agregado a la lista de numeros cortos en el parametro de sistema. Por favor consulte con su administrador sobre este error, luego intentelo de nuevo.");
				
			}
			break;

		default:
			break;
		}
		return new ValidationResult(true, null);
//		return null;
	}

	private boolean shortNumberIsParams() {
		String param=entityManager.find(ParametroSistema.class, "system.smpp.short.number").getValor();
		String[] shortNumbers=param.split(",");
		for (String _sn : shortNumbers) {
			if (_sn.trim().equalsIgnoreCase(getInstance().getNumeroCorto().trim())) {
				return true;
			}
		}
		return false;
	}

	private boolean existShortNumber() {
		String hql="SELECT count(_p) FROM Proyecto _p WHERE trim(_p.numeroCorto)= trim(:numeroCorto) AND _p.estadoRegistro= :estadoRegistro";
		if (isManaged() || isIdDefined()) {
			hql+=" AND _p != :instance";
		}
		
		Query q= entityManager.createQuery(hql);
		q.setParameter("numeroCorto", getInstance().getNumeroCorto());
		q.setParameter("estadoRegistro", EstadoRegistro.ACTIVO.name());
		if (isManaged() || isIdDefined()) {
			q.setParameter("instance", getInstance());
		}
		Number r=(Number) q.getSingleResult();
		return r.intValue()>0;
	}

	@Override
	public String update() {
		ValidationResult vr=checkData(ABMActions.UPDATE);
		//vr.getMessage().toString();
		if (vr!=null) {
			if (!vr.isSuccessful()) {
				getStatusMessages().add(Severity.ERROR, vr.getMessage());
				return null;
			}
		}
		else{
			getStatusMessages().add(Severity.ERROR, "No se pudo validar los datos ingresados, por favor, verifique con su administrador o intentelo de nuevo");
			return null;
		}
		
		getInstance().setUsuarioModificacion(usuario);
		getInstance().setFechaModificacion(new Date());
		getInstance().setParametroProyectos(new HashSet<>(parametros));
		if(super.update().equalsIgnoreCase("updated")){
			ParametroProyectoId paramId;
			for (ParametroProyecto param : parametros) {
				paramId=param.getId();
				if (paramId.getIdProyecto()==null) {
					paramId.setIdProyecto(getInstance().getIdProyecto());
					param.setId(paramId);
					param.setProyecto(getInstance());
					entityManager.persist(param);
					
				}
				else{
					entityManager.merge(param);
				}
			}
			entityManager.flush();
		};
		return "updated";
	}

	@Override
	public String remove() {
		getInstance().setEstadoRegistro(EstadoRegistro.ELIMINADO.name());
		getInstance().setUsuarioModificacion(usuario);
		getInstance().setFechaModificacion(new Date());

		return super.update();
	}



}
