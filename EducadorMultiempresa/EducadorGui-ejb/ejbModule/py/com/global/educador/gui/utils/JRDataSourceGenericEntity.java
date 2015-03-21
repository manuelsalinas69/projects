package py.com.global.educador.gui.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.log4j.Logger;

public class JRDataSourceGenericEntity implements JRDataSource {

	private transient Logger LOG = Logger
			.getLogger(JRDataSourceGenericEntity.class);
	Iterator<? extends EntityInterface> iter;
	private EntityInterface entity;

	public JRDataSourceGenericEntity() {

	}

	public<T extends EntityInterface> JRDataSourceGenericEntity(T entity) {
		List<T> collection = new LinkedList<T>();
		collection.add(entity);

		iter = collection.iterator();
	}


	public <T extends EntityInterface>JRDataSourceGenericEntity(List<T> entitycollection) {
		super();
		iter = null;

		if (!(entitycollection == null || entitycollection.isEmpty())) {
			iter =  entitycollection.iterator();
		}
	}

	public<T extends EntityInterface> JRDataSourceGenericEntity(Collection<T> entities) {
		super();
		iter = null;

		Collection<T> collection = null;
		if (List.class.isAssignableFrom(entities.getClass())) {
			collection = entities;
		} else {
			if (Set.class.isAssignableFrom(entities.getClass())) {
				collection = new LinkedList<T>();
				collection.addAll(entities);
			}
		}

		if (collection != null) {
			iter = collection.iterator();
		}
	}

	public Object getFieldValue(JRField jrField) throws JRException {

		Object returnvalue = null;
		if (entity != null) {
			LOG.debug(jrField.getName() + "-- " + jrField.getValueClassName()
					+ "-- " + entity.getProperties().get(jrField.getName()));
			returnvalue = entity.getProperties().get(jrField.getName());

		}

		return returnvalue;
	}

	public boolean next() throws JRException {
		if (iter.hasNext()) {
			entity = (EntityInterface) iter.next();
			return true;
		}
		return false;
	}

}
