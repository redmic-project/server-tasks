package es.redmic.tasks.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import es.redmic.exception.common.ExceptionType;
import es.redmic.exception.common.InternalException;

public class ResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

	// TODO: pasar las traducciones a base de datos y usar
	// ReloadableResourceBundleMessageSource para traerlas

	private List<String> resourceBaseNames = new ArrayList<>();

	// @formatter:off
	
	private static final String MESSAGE_SOURCE_BASE_NAME = "i18n/messages",
			MESSAGE_SOURCE_BASE_CLASSPATH = "classpath*:" + MESSAGE_SOURCE_BASE_NAME + "*",
			HOST_PATH = "jar";

	// @formatter:on

	public ResourceBundleMessageSource() {

		// Se añade los recursos del proyecto principal, por si existiera.
		resourceBaseNames.add(MESSAGE_SOURCE_BASE_NAME);
		init();
	}

	private void init() {

		try {
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources(MESSAGE_SOURCE_BASE_CLASSPATH);
			// Para todos los recursos en el classpath
			for (Resource resource : resources) {
				String uri = resource.getURI().toString();

				// Si es de tipo jar (es donde se encuentran las librerías)
				if (uri.contains(HOST_PATH)) {

					String[] basePathSplit = uri.split(MESSAGE_SOURCE_BASE_NAME);

					// Si es de tipo traducción
					if (basePathSplit != null && basePathSplit.length > 0) {
						String basePath = basePathSplit[0] + MESSAGE_SOURCE_BASE_NAME;

						if (!resourceBaseNames.contains(basePath)) {
							resourceBaseNames.add(basePath);
						}
					}
				}
			}

		} catch (IOException e) {
			throw new InternalException(ExceptionType.INTERNAL_EXCEPTION, e);
		}

		this.addBasenames(resourceBaseNames.toArray(new String[resourceBaseNames.size()]));

		this.setUseCodeAsDefaultMessage(true);
	}
}
