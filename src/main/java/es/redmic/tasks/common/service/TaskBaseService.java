package es.redmic.tasks.common.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import es.redmic.es.config.OrikaScanBeanESItfc;
import es.redmic.tasks.ingest.model.common.dto.UserTaskDTOEvent;
import es.redmic.tasks.ingest.model.status.dto.UserTaskDTO;

public abstract class TaskBaseService implements ApplicationEventPublisherAware {

	protected static Logger logger = LogManager.getLogger();

	@Autowired
	protected OrikaScanBeanESItfc orikaMapper;

	protected ApplicationEventPublisher eventPublisher;

	public TaskBaseService() {

	}

	protected void publishTaskStatusToUser(UserTaskDTO dto) {
		this.eventPublisher.publishEvent(new UserTaskDTOEvent(this, dto));
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
}