package py.com.global.educador.gui.managers;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage.Severity;

import py.com.global.educador.gui.dto.ValidationResult;
import py.com.global.educador.gui.entity.Pregunta;

@Name("preguntaEditor")
@Scope(ScopeType.CONVERSATION)
public class PreguntaEditor extends PreguntaAdder{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Pregunta pregunta;
	
	@Override
	@Create
	public void init() {
		super.init();
	}
	
	public void setPreguntaToEdit(Pregunta p){
		createNewPregunta();
		pregunta=p;
		contenidoPregunta=pregunta.getContenidoPregunta();
		respuestas=pregunta.getRespuestasList();
	}
	
	public Pregunta getModifiedPregunta(){
		setHideModal(false);
		ValidationResult v= checkData();
		if (v==null) {
			return null;
		}
		if (!v.isSuccessful()) {
			statusMessages.add(Severity.ERROR,v.getMessage());
			return null;
		}
		pregunta.setContenidoPregunta(contenidoPregunta);
		pregunta.setRespuestasList(respuestas);
		setHideModal(true);
		return pregunta;
		
		

	}
	
	

}
