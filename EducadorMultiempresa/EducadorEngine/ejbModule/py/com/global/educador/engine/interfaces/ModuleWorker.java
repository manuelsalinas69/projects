package py.com.global.educador.engine.interfaces;

import java.util.Map;

import javax.ejb.Local;

@Local
public interface ModuleWorker {

	public void process(Map<String, Object> params);
}
