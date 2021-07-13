package es.redmic.tasks.common.repository;

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

import java.util.Map;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Repository;

import es.redmic.es.data.common.repository.RWDataESRepository;
import es.redmic.models.es.data.common.model.DataSearchWrapper;
import es.redmic.tasks.ingest.model.status.model.UserTasks;

@Repository
public class UserTasksRepository extends RWDataESRepository<UserTasks> {

	private static String[] INDEX = { "tasks" };
	private static String TYPE = "_doc";

	public UserTasksRepository() {
		super(INDEX, TYPE);
	}

	@Override
	public UserTasks update(UserTasks modelToIndex) {

		return elasticPersistenceUtils.update(getIndex()[0], getType(), modelToIndex, modelToIndex.getId().toString());
	}

	@SuppressWarnings("unchecked")
	public DataSearchWrapper<UserTasks> findByUser(Map<String, String> options, String user) {

		BoolQueryBuilder filter = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userId", user))
				.mustNot(QueryBuilders.termQuery("status", "removed"));

		if (options != null) {
			for (Map.Entry<String, String> entry : options.entrySet()) {
				filter.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
			}
		}
		QueryBuilder query = QueryBuilders.matchAllQuery();

		return (DataSearchWrapper<UserTasks>) findBy(QueryBuilders.boolQuery().must(query).filter(filter),
				SortBuilders.fieldSort("updated").order(SortOrder.ASC));
	}
}
