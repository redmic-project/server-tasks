package es.redmic.test.tasks.integration.common;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.exception.mediastorage.MSFileNotFoundException;
import es.redmic.mediastorage.service.FileUtils;
import es.redmic.mediastorage.service.MediaStorageServiceItfc;
import es.redmic.tasks.TasksApplication;
import es.redmic.testutils.kafka.KafkaBaseIntegrationTest;

@SpringBootTest(classes = { TasksApplication.class })
@ActiveProfiles("test")
@DirtiesContext
public abstract class IntegrationTestBase extends KafkaBaseIntegrationTest {

	protected final static String JOB_PARAMETER_KEY = "parameters";

	// En este contexto es necesario definir embeddedKafka para que la
	// configuración se pueda completar
	@ClassRule
	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1);

	@PostConstruct
	public void IntegrationTestBasePostConstruct() throws Exception {

		createSchemaRegistryRestApp(embeddedKafka.getZookeeperConnectionString(), embeddedKafka.getBrokersAsString());
	}

	@Autowired
	MediaStorageServiceItfc mediaStorage;

	@Autowired
	protected ObjectMapper jacksonMapper;

	static Log logger = LogFactory.getLog(IntegrationTestBase.class);

	protected String copyResourceToMediaStorage(String sourcePath, String fileName, String targetPath) {

		URI url = null;
		try {
			url = this.getClass().getResource(sourcePath + fileName).toURI();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		FileUtils.createDirectoryIfNotExist(targetPath);
		File file = new File(url);
		try {
			MultipartFile multipartFile = new MockMultipartFile("file", file.getName(),
					new MimetypesFileTypeMap().getContentType(file), Files.readAllBytes(file.toPath()));

			File fileResult = mediaStorage.uploadTempFile(multipartFile, targetPath);
			return fileResult.getName();

		} catch (IOException e) {
			throw new MSFileNotFoundException(file.getName(), sourcePath, e);
		}
	}
}
