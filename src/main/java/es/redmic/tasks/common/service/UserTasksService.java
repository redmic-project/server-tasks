package es.redmic.tasks.common.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.redmic.es.data.common.service.RWUserDataESService;
import es.redmic.models.es.common.dto.JSONCollectionDTO;
import es.redmic.models.es.data.common.model.DataHitWrapper;
import es.redmic.models.es.data.common.model.DataSearchWrapper;
import es.redmic.tasks.common.repository.UserTasksRepository;
import es.redmic.tasks.ingest.model.intervention.matching.RequestUserInterventionMatchingTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.CompletedTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.FailedTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.RegisteredTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.RemovedTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.RunningTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.StartedTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.UserTaskDTO;
import es.redmic.tasks.ingest.model.status.model.UserTasks;

@Service
public class UserTasksService extends RWUserDataESService<UserTasks, UserTaskDTO> {
	
	private final Logger LOGGER = LoggerFactory.getLogger(UserTasksService.class);
	
	UserTasksRepository repository;

	@Autowired
	public UserTasksService(UserTasksRepository repository) {
		super(repository);
		this.repository = repository;
	}
	
	public JSONCollectionDTO findByUser(Map<String, String> options, String user) {
		
		LOGGER.debug("Obtenteniendo tareas de usuario ", user);
		
		DataSearchWrapper<UserTasks> result = repository.findByUser(options, user);
		
		return mappingToSpecificDTO(result);
	}
	
	private JSONCollectionDTO mappingToSpecificDTO (DataSearchWrapper<UserTasks> result) {
		
		JSONCollectionDTO collection = new JSONCollectionDTO();
		
		for (DataHitWrapper<UserTasks> hit: result.getHits().getHits()) {
			
			UserTasks tasks = (UserTasks) hit.get_source();
			
			switch (tasks.getStatus()) {
			case REGISTERED:
				collection.addData(orikaMapper.getMapperFacade().map(tasks, RegisteredTaskDTO.class));
				break;
			case STARTED:
				collection.addData(orikaMapper.getMapperFacade().map(tasks, StartedTaskDTO.class));
				break;
			case RUNNING:
				collection.addData(orikaMapper.getMapperFacade().map(tasks, RunningTaskDTO.class));
				break;
			case WAITING_INTERVENTION:
				collection.addData(orikaMapper.getMapperFacade().map(tasks, RequestUserInterventionMatchingTaskDTO.class));
				break;
			case FAILED:
				collection.addData(orikaMapper.getMapperFacade().map(tasks, FailedTaskDTO.class));
				break;
			case COMPLETED:
				collection.addData(orikaMapper.getMapperFacade().map(tasks, CompletedTaskDTO.class));
				break;
			case REMOVED:
				collection.addData(orikaMapper.getMapperFacade().map(tasks, RemovedTaskDTO.class));
				break;
			default:
				collection.addData(orikaMapper.getMapperFacade().map(tasks, UserTaskDTO.class));
				break;
			
			}
		}
		collection.setTotal(result.getTotal());
		return collection;
	}
}