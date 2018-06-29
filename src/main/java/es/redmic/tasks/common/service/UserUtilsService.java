package es.redmic.tasks.common.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import es.redmic.es.common.service.UserUtilsServiceItfc;

@Service
public class UserUtilsService implements UserUtilsServiceItfc {

	protected static Logger logger = LogManager.getLogger();

	@Override
	public String getUserId() {
		logger.warn("Obteniendo identificador de usuario en un ámbito en el que no está disponible");
		return "REDMIC_TASK";
	}

	@Override
	public List<String> getUserRole() {
		logger.warn("Obteniendo rol de usuario en un ámbito en el que no está disponible");
		return null;
	}

	@Override
	public List<Long> getAccessibilityControl() {
		logger.warn("Obteniendo accessibilidad de usuario en un ámbito en el que no está disponible");
		return null;
	}
}