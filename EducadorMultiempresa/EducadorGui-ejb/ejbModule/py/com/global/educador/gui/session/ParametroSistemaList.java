package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;
import java.util.List;

@Name("parametroSistemaList")
public class ParametroSistemaList extends EntityQuery<ParametroSistema> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select parametroSistema from ParametroSistema parametroSistema";

	private static final String[] RESTRICTIONS = {
			"lower(parametroSistema.parametro) like lower(concat('%',#{parametroSistemaList.parametroSistema.parametro},'%'))",
			"lower(parametroSistema.valor) like lower(concat('%',#{parametroSistemaList.parametroSistema.valor},'%'))",
			"lower(parametroSistema.descripcion) like lower(concat('%',#{parametroSistemaList.parametroSistema.descripcion},'%'))", 
			"parametroSistema.visible =#{parametroSistemaList.parametroSistema.visible}"};
	
	//company.idcompanyPk=#{companyList.company.idcompanyPk}

	private ParametroSistema parametroSistema = new ParametroSistema();

	public ParametroSistemaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		setOrderColumn("parametro");
		setOrderDirection("asc");
	}
	
	@Override
	@Transactional
	public List<ParametroSistema> getResultList() {
		getParametroSistema().setVisible(Boolean.TRUE);
		return super.getResultList();
	}
	
	

	public ParametroSistema getParametroSistema() {
		return parametroSistema;
	}
}
