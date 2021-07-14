package es.redmic.test.tasks.integration.report.species;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.redmic.brokerlib.avro.common.MessageWrapper;
import es.redmic.es.administrative.taxonomy.service.SpeciesESService;
import es.redmic.es.administrative.taxonomy.service.TaxonESService;
import es.redmic.models.es.administrative.dto.ActivityDTO;
import es.redmic.models.es.administrative.dto.DocumentDTO;
import es.redmic.models.es.administrative.taxonomy.dto.SpeciesDTO;
import es.redmic.models.es.administrative.taxonomy.dto.TaxonDTO;
import es.redmic.models.es.administrative.taxonomy.model.Species;
import es.redmic.models.es.common.dto.JSONCollectionDTO;
import es.redmic.models.es.common.model.DomainES;
import es.redmic.models.es.data.common.model.DataHitWrapper;
import es.redmic.models.es.maintenance.taxonomy.dto.RankDTO;
import es.redmic.test.tasks.integration.report.common.ReportBaseTest;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = { "schema.registry.port=18091" })
public class ReportSpeciesTest extends ReportBaseTest {

	private static final Long SPECIES_ID = 15L;

	@MockBean
	SpeciesESService service;

	@MockBean
	TaxonESService taxonService;

	@Value("${broker.topic.task.report.species.run}")
	String REPORT_SPECIES_RUN_TOPIC;

	@Before
	public void config() {

		SpeciesDTO speciesDTO = new SpeciesDTO();
		speciesDTO.setId(SPECIES_ID);
		speciesDTO.setPath("r.1." + SPECIES_ID);
		speciesDTO.setScientificName("scientificname");

		RankDTO rank = new RankDTO();
		rank.setId(10L);
		rank.setName("Especie");
		rank.setName_en("Species");
		speciesDTO.setRank(rank);

		when(service.get(any())).thenReturn(speciesDTO);

		TaxonDTO parent = new TaxonDTO();
		parent.setId(1L);
		RankDTO rankParent = new RankDTO();
		rankParent.setId(1L);
		parent.setRank(rankParent);

		JSONCollectionDTO ancestors = new JSONCollectionDTO();
		ancestors.addData(parent);
		when(taxonService.mget(any())).thenReturn(ancestors);

		JSONCollectionDTO activities = new JSONCollectionDTO();
		List<ActivityDTO> data = new ArrayList<>();
		ActivityDTO activity = new ActivityDTO();
		activity.setId(12L);
		activity.setName("Prueba");
		data.add(activity);
		activities.setData(data);

		when(service.getActivities(any(), any())).thenReturn(activities);

		JSONCollectionDTO documents = new JSONCollectionDTO();
		List<DocumentDTO> docList = new ArrayList<>();
		DocumentDTO document = new DocumentDTO();
		document.setId(123L);
		document.setTitle("Documento de prueba");
		document.setLanguage("es");
		docList.add(document);
		documents.setData(docList);
		when(service.getDocuments(any(), any())).thenReturn(documents);
	}

	@Override
	@Test
	public void runIngestData_ReturnStatusMsg_IfBrokerIsListen() throws Exception {

		super.runIngestData_ReturnStatusMsg_IfBrokerIsListen();
	}

	@KafkaListener(topics = "${broker.topic.task.report.species.status}")
	public void run(MessageWrapper payload) {

		blockingQueue.offer(payload);
	}

	@Override
	protected String getRunTopic() {
		return REPORT_SPECIES_RUN_TOPIC;
	}

	@Override
	protected Map<String, String> createRunTaskRequest() {

		Map<String, String> request = new HashMap<>();
		request.put("selectId", SPECIES_ID.toString());
		return request;
	}
}
