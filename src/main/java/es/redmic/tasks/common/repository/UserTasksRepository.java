package es.redmic.tasks.common.repository;

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

	private static String[] INDEX = { "user" };
	private static String[] TYPE = { "tasks" };

	public UserTasksRepository() {
		super(INDEX, TYPE);
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
