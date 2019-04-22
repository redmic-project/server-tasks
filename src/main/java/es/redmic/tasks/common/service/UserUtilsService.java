package es.redmic.tasks.common.service;

/*-
 * #%L
 * Tasks
 * %%
 * Copyright (C) 2019 REDMIC Project / Server
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
