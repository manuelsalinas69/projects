package py.com.global.educador.gui.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class JpaResourceBean {
	private transient static Logger LOG = Logger.getLogger(JpaResourceBean.class);

	public static Connection getConnection() {
		String DATASOURCE_CONTEXT = "java:/EducadorGuiDatasource";

		Connection result = null;
		try {
			Context initialContext = new InitialContext();
			DataSource datasource = (DataSource) initialContext
					.lookup(DATASOURCE_CONTEXT);
			if (datasource != null) {
				result = datasource.getConnection();
			} else {
				LOG.debug("Failed to lookup datasource.");
			}
		} catch (NamingException ex) {
			LOG.debug("Cannot get connection: " + ex.getMessage(), ex);
		} catch (SQLException ex) {
			LOG.debug("Cannot get connection: " + ex.getMessage(), ex);
		}
		return result;
	}

	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException ex) {
				LOG.debug(ex.getMessage(), ex);
			}
		}
	}

	public static JpaResourceBean getInstance() {
		FacesContext fc = FacesContext.getCurrentInstance();
		return (JpaResourceBean) fc.getApplication().getELResolver().getValue(
				fc.getELContext(), null, "JpaResourceBean");
	}
}
