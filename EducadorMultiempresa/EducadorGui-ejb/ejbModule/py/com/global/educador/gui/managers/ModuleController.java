package py.com.global.educador.gui.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;

import py.com.global.educador.gui.dto.ValidationResult;
import py.com.global.educador.gui.entity.EjecucionSuscriptorDetalle;
import py.com.global.educador.gui.entity.Evaluacion;
import py.com.global.educador.gui.entity.Modulo;
import py.com.global.educador.gui.entity.PlanificacionEnvio;
import py.com.global.educador.gui.entity.Pregunta;
import py.com.global.educador.gui.entity.Proyecto;
import py.com.global.educador.gui.entity.Respuesta;
import py.com.global.educador.gui.entity.Tip;
import py.com.global.educador.gui.entity.Usuario;
import py.com.global.educador.gui.enums.ABMActions;
import py.com.global.educador.gui.enums.EstadoEvaluacion;
import py.com.global.educador.gui.enums.EstadoModulo;
import py.com.global.educador.gui.enums.EstadoRegistro;
import py.com.global.educador.gui.enums.PlanificacionEnvioType;
import py.com.global.educador.gui.enums.ScheduleDaysOfWeek;
import py.com.global.educador.gui.session.ModuloHome;
import py.com.global.educador.gui.utils.EntityBaseController;
import py.com.global.educador.gui.utils.EntityInterface;
import py.com.global.educador.gui.utils.GeneralHelper;


@Name("moduleController")
@Scope(ScopeType.CONVERSATION)
public class ModuleController extends EntityBaseController<Modulo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@In EntityManager entityManager;
	@In StatusMessages statusMessages;

	@In ModuloHome moduloHome;
	@In PreguntaAdder preguntaAdder;
	@In PreguntaEditor preguntaEditor;
	@In Usuario usuario;
	@In(create=true) SessionManager sessionManager;
	List<Tip> tips;
	List<Evaluacion> evaluaciones;
	List<PlanificacionEnvio> planificacionEnvio;

	Long idProyecto;
	Long idEmpresa;
	String contenidoTip;
	String nombreEvaluacion;

	Integer evaluacionEditIndex;
	String[] daysOfWeek; 
	String hour;
	String minute;
	Map<String, String> configuracionEnvioMap;

	public ModuleController() {
		super(Modulo.class);
	}


	@Create
	public void init(){
		setInitialValues();

		if (moduloHome.isManaged() || moduloHome.isIdDefined()) {
			edit(moduloHome.getInstance().getId());
			loadDataFor(getInstance());
			idEmpresa=sessionManager.userFromSuperCompany()?getInstance().getProyecto().getEmpresa()==null?null:getInstance().getProyecto().getEmpresa().getIdEmpresa():sessionManager.getLoggedUserCompany();
			
		}
		else{
			setNewModule();
		}
	}


	private void setInitialValues() {
		tips= new ArrayList<Tip>();
		evaluaciones= new ArrayList<Evaluacion>();
		planificacionEnvio= new ArrayList<PlanificacionEnvio>();
		setInitialSchedule();

	}




	private void setNewModule() {
		createNew();
		if (!sessionManager.userFromSuperCompany()) {
			idEmpresa=sessionManager.getLoggedUserCompany();
		}

	}

	public void upPlanificacionEnvio(int index){
		if (index==0) {
			return;
		}

		Collections.swap(planificacionEnvio, index, index-1);
		reorderPlanificacion(planificacionEnvio);
	}

	public void downPlanificacionEnvio(int index){
		if (index>=(planificacionEnvio.size()-1)) {
			return;
		}
		Collections.swap(planificacionEnvio, index, index+1);
		reorderPlanificacion(planificacionEnvio);
	}

	private void reorderPlanificacion(
			List<PlanificacionEnvio> l) {
		for (int i = 0; i < planificacionEnvio.size(); i++) {
			planificacionEnvio.get(i).setOrden((long)i+1);			
		}

	}



	private void loadDataFor(Modulo instance) {
		loadProyecto(instance);
		loadTips(instance);
		loadEvaluaciones(instance);
		loadPlanificacionEnvio(instance);
		loadScheduleValues(instance);

	}

	private void loadProyecto(Modulo instance) {
		setIdProyecto(instance.getProyecto().getIdProyecto());
		
	}


	private void loadScheduleValues(Modulo instance) {
		configuracionEnvioMap=getMapOfScheduleStringConf(instance.getConfiguracionEnvioTip());
		daysOfWeek=parseDaysOfWeekToArray(configuracionEnvioMap.get("dayOfWeek"));
		hour=configuracionEnvioMap.get("hour");
		minute=configuracionEnvioMap.get("minute");
	}

	private void buildScheduleString(){
		String baseSchedule="year=*;month=*;dayOfWeek=<DOW>;hour=<HOUR>;minute=<MINUTE>;second=30";
		String dow=parseArrayToDayOfWeekString(daysOfWeek);
		baseSchedule=baseSchedule.replace("<DOW>", dow);
		baseSchedule=baseSchedule.replace("<HOUR>", hour);
		baseSchedule=baseSchedule.replace("<MINUTE>", minute);
		getInstance().setConfiguracionEnvioTip(baseSchedule);
	}

	private String[] parseDaysOfWeekToArray(String dayOfWeek) {
		if ("*".equalsIgnoreCase(dayOfWeek)) {
			return ScheduleDaysOfWeek.allDaysCodigo0();
		}
		return dayOfWeek!=null?dayOfWeek.split(","):null;
	}

	public String parseArrayToDayOfWeekString(String[] daysOfWeek){
		if (daysOfWeek==null || daysOfWeek.length==0) {
			return "*";
		}
		return Arrays.toString(daysOfWeek).replaceAll("\\[|\\]|\\s", "");
	}

	private Map<String, String> getMapOfScheduleStringConf(String conf) {
		HashMap<String, String> m= new HashMap<String, String>();
		if (conf==null || conf.isEmpty()) {
			return null;
		}
		String [] values=conf.split(";");
		for (String v : values) {
			String[] keyValue=v.split("=");
			m.put(keyValue[0].trim(), keyValue[1].trim());
		}

		return m;
	}


	private void loadTips(Modulo instance) {
		tips= getTips0(instance);

	}
	/*
	 * CONFIGURACION ENVIO
	 * */
	public void useDefaultScheduleValues(){
		setInitialSchedule();
	}
	private void setInitialSchedule() {
		setInititalDayOfWeek();
		hour="11,14,17";
		minute="10";
	}

	private void setInititalDayOfWeek() {
		daysOfWeek= new String[7];
		int i=0;
		for (ScheduleDaysOfWeek _dow : ScheduleDaysOfWeek.values()) {
			daysOfWeek[i]=_dow.getCodigo0();
			i++;
		}

	}


	/*
	 * PREGUNTAS
	 * */
	public void initPreguntaAdder(){
		preguntaAdder.createNewPregunta();
	}
	public void addNewPregunta(){
		ValidationResult vr=preguntaAdder.checkData();
		if (vr==null) {
			statusMessages.add(Severity.ERROR,"No se pudo validar los datos, por favor verifique");
			return;
		}
		if (!vr.isSuccessful()) {
			statusMessages.add(Severity.ERROR,vr.getMessage());
			return;
		}

		Evaluacion eva= new Evaluacion();
		eva.getPreguntas().add(preguntaAdder.getPreguntaToAdd());
		addToPlanificacionEnvio(eva);
	}



	public void initPreguntaEditor(int index){
		preguntaEditor.setPreguntaToEdit(evaluaciones.get(index).getPreguntaPrincipal());
		evaluacionEditIndex=index;

	}

	public void updateEditPregunta(){
		Pregunta p=preguntaEditor.getModifiedPregunta();
		if (p==null || evaluacionEditIndex==null) {
			return;
		}
		evaluaciones.get(evaluacionEditIndex.intValue()).getPreguntas().clear();
		evaluaciones.get(evaluacionEditIndex.intValue()).getPreguntas().add(p);
		evaluacionEditIndex=null;
	}

	/*
	 *DATA ACCESS 
	 * */
	@SuppressWarnings("unchecked")
	private List<Tip> getTips0(Modulo instance) {
		String hql="SELECT _pe.tip FROM PlanificacionEnvio _pe WHERE _pe.enviar=:envio AND _pe.modulo= :modulo ORDER BY _pe.orden";
		Query q= entityManager.createQuery(hql);
		q.setParameter("envio", PlanificacionEnvioType.TIP.name());
		q.setParameter("modulo", instance);
		return q.getResultList();
	}

	private void loadEvaluaciones(Modulo instance) {
		evaluaciones= getEvaluaciones0(instance);

	}

	@SuppressWarnings("unchecked")
	private List<Evaluacion> getEvaluaciones0(Modulo instance) {
		String hql="SELECT _pe.evaluacion FROM PlanificacionEnvio _pe WHERE _pe.enviar=:envio AND _pe.modulo= :modulo ORDER BY _pe.orden";
		Query q= entityManager.createQuery(hql);
		q.setParameter("envio", PlanificacionEnvioType.EVALUACION.name());
		q.setParameter("modulo", instance);
		return q.getResultList();
	}

	private void loadPlanificacionEnvio(Modulo instance) {
		planificacionEnvio=getPlanificacionEnvio0(instance);

	}

	@SuppressWarnings("unchecked")
	private List<PlanificacionEnvio> getPlanificacionEnvio0(Modulo instance) {
		String hql="SELECT _pe FROM PlanificacionEnvio _pe WHERE _pe.modulo= :modulo ORDER BY _pe.orden";
		Query q= entityManager.createQuery(hql);
		q.setParameter("modulo", instance);
		return q.getResultList();
	}

	@Override
	public void setDetailsData() {
		saveTips();
		saveEvaluaciones();
		savePlanificacionEnvio();

	}

	private void saveTips() {
		if (tips==null || tips.isEmpty()) {
			return;
		}

		for (Tip _t : tips) {
			_t.setModulo(getInstance());
			if (_t.getId()==null) {
				_t.setEstadoRegistro(EstadoRegistro.ACTIVO.name());
				_t.setFechaAlta(new Date());
				entityManager.persist(_t);
			}
			else{
				entityManager.merge(_t);
			}
		}

	}


	private void saveEvaluaciones() {
		if (evaluaciones==null || evaluaciones.isEmpty()) {
			return;
		}
		int i=1;
		for (Evaluacion _eva : evaluaciones) {

			_eva.setModulo(getInstance());
			if (_eva.getId()==null) {
				_eva.setConfiguracionEnvioModulo("ENVIO_INMEDIATO");
				_eva.setFechaAlta(new Date());
				_eva.setNombre("Eval."+i+"Modulo "+getInstance().getId());
				_eva.setEstadoRegistro(EstadoRegistro.ACTIVO.name());
				_eva.setUsuarioAlta(usuario);
				_eva.setEstadoEvaluacion(EstadoEvaluacion.CREADO.name());
				entityManager.persist(_eva);
				_eva.getPreguntaPrincipal().setEvaluacion(_eva);
				_eva.getPreguntaPrincipal().setEstadoRegistro(EstadoRegistro.ACTIVO.name());
				_eva.getPreguntaPrincipal().setFechaAlta(new Date());
				_eva.getPreguntaPrincipal().setUsuarioAlta(usuario);
				entityManager.persist(_eva.getPreguntaPrincipal());
				for (Respuesta _r : _eva.getPreguntaPrincipal().getRespuestasList()) {
					_r.setEstadoRegistro(EstadoRegistro.ACTIVO.name());
					_r.setPregunta(_eva.getPreguntaPrincipal());
					entityManager.persist(_r);
				}
			}
			else{
				entityManager.merge(_eva);
				entityManager.merge(_eva.getPreguntaPrincipal());
				for (Respuesta _r : _eva.getPreguntaPrincipal().getRespuestasList()) {
					_r.setEstadoRegistro(EstadoRegistro.ACTIVO.name());
					_r.setPregunta(_eva.getPreguntaPrincipal());
					entityManager.merge(_r);
				}

			}
			i++;
		}

	}


	private void savePlanificacionEnvio() {
		if (planificacionEnvio==null || planificacionEnvio.isEmpty()) {
			return;
		}
		//Powered by Wire
		for (PlanificacionEnvio _p : planificacionEnvio) {
			_p.setEnvioFinal(null);
		}
		
		int lastIndex=planificacionEnvio.size()-1;
		
		if (lastIndex>=0) {
			planificacionEnvio.get(lastIndex).setEnvioFinal(Boolean.TRUE);
		}
		
		
		for (PlanificacionEnvio _pe : planificacionEnvio) {
			_pe.setModulo(getInstance());
			if (_pe.getId()==null) {
				entityManager.persist(_pe);
			}
			else{
				entityManager.merge(_pe);
			}
		}

	}


	@Override
	public ValidationResult checkData(ABMActions action) {
		switch (action) {
		case PERSIST:
		case UPDATE:
			if (idProyecto==null) {
				return new ValidationResult(false, "Debe seleccionar el proyecto para el modulo");
			}
			
			if (existNombre(getInstance().getNombre())) {
				String field= "Nombre";
				return new ValidationResult(false, "msg_error_already_exists", field);
			}
			if (getInstance().getFechaInicio().before(new Date())) {
				return new ValidationResult(false, "La fecha de inicio no puede ser menor al dia/hora actual");
			}

			if (getInstance().getFechaFin().before(getInstance().getFechaInicio())) {
				return new ValidationResult(false, "La fecha de fin no puede ser menor a la fecha de inicio");
			}

			if (fechasSolapadas(getInstance().getFechaInicio(), getInstance().getFechaFin())) {
				return new ValidationResult(false, "Las fechas de inicio y fin se solapan con otros modulos ");
			}


			break;

		default:
			break;
		}

		return new ValidationResult(true, null);
	}

	private boolean fechasSolapadas(Date fechaInicio, Date fechaFin) {
		String hql="SELECT count(_m) FROM Modulo _m WHERE ((_m.fechaInicio<= :fechaInicio AND _m.fechaFin >=:fechaInicio) OR (_m.fechaInicio<=:fechaFin AND _m.fechaFin>=:fechaFin)) AND _m.proyecto.idProyecto= :idProyecto";
		if (isManaged()) {
			hql+=" AND _m.idModulo != :idModulo ";
		}
		Query q= entityManager.createQuery(hql);
		q.setParameter("fechaInicio", fechaInicio);
		q.setParameter("fechaFin", fechaFin);
		q.setParameter("idProyecto", idProyecto);
		if (isManaged()) {
			q.setParameter("idModulo", getInstance().getIdModulo());
		}
		Number r=(Number) q.getSingleResult();
		return r.intValue()>0;
	}


	/*
	 * CHECK DATA VALIDATION
	 * **/

	private boolean existNombre(String nombre) {
		String hql="SELECT count(_m) FROM Modulo _m WHERE lower(trim(_m.nombre))= lower(trim(:nombre)) ";
		if (isManaged()) {
			hql+=" AND _m != :instance ";
		}
		Query q= entityManager.createQuery(hql);
		q.setParameter("nombre", nombre);
		if (isManaged()) {
			q.setParameter("instance", getInstance());
		}
		Number r=(Number) q.getSingleResult();
		return r.intValue()>0;
	}


	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StatusMessages getStatusMessages() {
		return statusMessages;
	}

	@Override
	public void setCommonData(Modulo instance) {
		if (isNew()) {
			getInstance().setFechaAlta(new Date());
			getInstance().setUsuarioAlta(usuario);
			getInstance().setEstadoModulo(EstadoModulo.CREADO.name());
			getInstance().setEstadoRegistro(EstadoRegistro.ACTIVO.name());
			
		}
		getInstance().setProyecto(entityManager.find(Proyecto.class, idProyecto));
		buildScheduleString();
	}

	@Override
	public boolean canEdit() {
		return isNew() || EstadoModulo.CREADO.name().equalsIgnoreCase(getInstance().getEstadoModulo());
	}

	@Override
	public boolean canRemove() {
		return false;
	}


	public void addTip(){
		if (contenidoTip==null || contenidoTip.trim().isEmpty()) {
			statusMessages.add(Severity.ERROR,"No se puede agregar un contenido vacion para el tip");
			return;
		}
		boolean isAlreadyEntry=tipTextExist(contenidoTip);
		if (isAlreadyEntry) {
			statusMessages.add(Severity.ERROR,"El contenido que quiere ingresar ya se encuentra el la lista. Por favor verifique");
			return;
		}
		Tip t=new Tip();
		t.setContenido(contenidoTip);
		t.setEstadoRegistro(EstadoRegistro.ACTIVO.name());
		t.setFechaAlta(new Date());
		t.setModulo(getInstance());

		addToPlanificacionEnvio(t);

	}

	public void addEvaluacion(){
		Evaluacion ev=null;
		addToPlanificacionEnvio(ev);
	}



	private void addToPlanificacionEnvio(Tip t) {
		if(addToPlanificacionEnvio0(PlanificacionEnvioType.TIP, null, t)){
			tips.add(t);
		}

	}

	private void addToPlanificacionEnvio(Evaluacion ev) {
		if(addToPlanificacionEnvio0(PlanificacionEnvioType.EVALUACION, ev, null)){
			evaluaciones.add(ev);
		}

	}

	private boolean addToPlanificacionEnvio0(PlanificacionEnvioType type, Evaluacion ev, Tip t ){
		PlanificacionEnvio pe= new PlanificacionEnvio();
		pe.setEnviar(type.name());
		pe.setOrden((long) (planificacionEnvio==null?1:planificacionEnvio.size()+1));

		switch (type) {
		case EVALUACION:
			pe.setEvaluacion(ev);
			break;
		case TIP:
			pe.setTip(t);
			break;

		default:
			break;
		}
		//		if (planificacionEnvio==null) {
		//			planificacionEnvio= new ArrayList<PlanificacionEnvio>();
		//		}
		return planificacionEnvio.add(pe);

	}

	private boolean tipTextExist(String contenidoNuevo) {
		if (tips==null ||tips.isEmpty()) {
			return false;
		}

		for (Tip t : tips) {
			if (t.getContenido().trim().equalsIgnoreCase(contenidoNuevo.trim())) {
				return true;
			}
		}
		return false;
	}

	public void removeTip(int index){
		Tip t= tips.get(index);
		if (t.getIdTip()!=null) {
			if (GeneralHelper.tieneReferencias(t, EjecucionSuscriptorDetalle.class)) {
				statusMessages.add(Severity.ERROR,"No se puede eliminar el TIP, existen referencias");
				return;
			}
			entityManager.remove(t);
		}
		removePlanificacionEnvio(t);
		tips.remove(index);
	}
	


	public void removeEvaluacion(int index){
		Evaluacion eva= evaluaciones.get(index);
		if (eva.getIdEvaluacion()!=null) {
			return;
			//statusMessages.add(Severity.ERROR,"No se puede eliminar regsitro");
//			if (GeneralHelper.tieneReferencias(eva, EjecucionSuscriptorDetalle.class)) {
//				statusMessages.add(Severity.ERROR,"No se puede eliminar la evaluacion, existen referencias de ejecucion");
//				return;
//			}
//			
//			if (GeneralHelper.tieneReferencias(eva, EvaluacionSuscriptor.class)) {
//				statusMessages.add(Severity.ERROR,"No se puede eliminar la evaluacion, existen referencias de ejecucion");
//				return;
//			}
//			entityManager.remove(eva);
		}
		removePlanificacionEnvio(eva);
		evaluaciones.remove(index);
	}

	private void removePlanificacionEnvio(Tip t) {
		removePlanificacionEnvio0(PlanificacionEnvioType.TIP, t);
		
	}


	private void removePlanificacionEnvio(Evaluacion eva) {
		removePlanificacionEnvio0(PlanificacionEnvioType.EVALUACION, eva);
		
	}


	private void removePlanificacionEnvio0(PlanificacionEnvioType tipo,
			EntityInterface obj) {
		Evaluacion eva;
		Tip tip;
		switch (tipo) {
		case EVALUACION:
			eva=(Evaluacion) obj;
			for (Iterator<PlanificacionEnvio> iterator = planificacionEnvio.iterator(); iterator.hasNext();) {
				PlanificacionEnvio _pe = (PlanificacionEnvio) iterator.next();
				if (_pe.getEvaluacion()!=null && _pe.getEvaluacion().equals(eva)) {
					iterator.remove();
					break;
				}
			}
			break;
		case TIP:
			tip=(Tip) obj;
			for (Iterator<PlanificacionEnvio> iterator = planificacionEnvio.iterator(); iterator.hasNext();) {
				PlanificacionEnvio _pe = (PlanificacionEnvio) iterator.next();
				if (_pe.getTip()!=null && _pe.getTip().equals(tip)) {
					iterator.remove();
					break;
				}
			}
			break;
		default:
			break;
		}
		reorderPlanificacion(planificacionEnvio);
		
	}
	
	public void removePlanificacionEnvio(int index){
		PlanificacionEnvio _pe=planificacionEnvio.get(index);
		if (_pe.getIdPlanificacionEnvio()!=null) {
			entityManager.remove(_pe);
		}
		planificacionEnvio.remove(index);
		reorderPlanificacion(planificacionEnvio);
	}


	/*
	 * GETTERS && SETTERS
	 * */
	public List<Tip> getTips() {
		return tips;
	}

	public void setTips(List<Tip> tips) {
		this.tips = tips;
	}

	public List<Evaluacion> getEvaluaciones() {
		return evaluaciones;
	}

	public void setEvaluaciones(List<Evaluacion> evaluaciones) {
		this.evaluaciones = evaluaciones;
	}

	public List<PlanificacionEnvio> getPlanificacionEnvio() {
		return planificacionEnvio;
	}

	public void setPlanificacionEnvio(List<PlanificacionEnvio> planificacionEnvio) {
		this.planificacionEnvio = planificacionEnvio;
	}

	public String getContenidoTip() {
		return contenidoTip;
	}

	public void setContenidoTip(String contenidoTip) {
		this.contenidoTip = contenidoTip;
	}

	public String getNombreEvaluacion() {
		return nombreEvaluacion;
	}

	public void setNombreEvaluacion(String nombreEvaluacion) {
		this.nombreEvaluacion = nombreEvaluacion;
	}


	public String[] getDaysOfWeek() {
		return daysOfWeek;
	}


	public void setDaysOfWeek(String[] daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}


	public String getHour() {
		return hour;
	}


	public void setHour(String hour) {
		this.hour = hour;
	}


	public String getMinute() {
		return minute;
	}


	public void setMinute(String minute) {
		this.minute = minute;
	}


	public Long getIdProyecto() {
		return idProyecto;
	}


	public void setIdProyecto(Long idProyecto) {
		this.idProyecto = idProyecto;
	}


	public Long getIdEmpresa() {
		return idEmpresa;
	}


	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}









}
