package es.redmic.tasks.common.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import es.redmic.brokerlib.avro.common.MessageWrapper;
import es.redmic.brokerlib.utils.MessageWrapperUtils;
import es.redmic.exception.common.ExceptionType;
import es.redmic.exception.common.InternalException;
import es.redmic.models.es.common.dto.JSONCollectionDTO;
import es.redmic.tasks.ingest.model.status.common.TaskStatus;
import es.redmic.tasks.ingest.model.status.model.UserTasks;

@Controller
public class TaskController extends TaskBaseController {

	@Value("${broker.topic.task.status}")
	protected String STATUS_TOPIC;

	public TaskController() {
	}

	@KafkaListener(topics = "${broker.topic.task.getstatus}")
	public void getTasks(MessageWrapper payload) {

		JSONCollectionDTO tasks = userTasksService.findByUser(null, payload.getUserId());
		try {
			payload.setContent(MessageWrapperUtils.getContent(tasks));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new InternalException(ExceptionType.INTERNAL_EXCEPTION);
		}
		publishToBroker(STATUS_TOPIC, payload);
	}

	@KafkaListener(topics = "${broker.topic.task.remove}")
	public void deleteTask(MessageWrapper payload) throws JsonProcessingException {

		UserTasks task = userTasksService.findById(payload.getActionId());

		task.setStatus(TaskStatus.REMOVED);
		userTasksService.update(task);

		payload.setContent(MessageWrapperUtils.getContent(task));

		publishToBroker(STATUS_TOPIC, payload);
	}

	@Override
	protected boolean chkEventIsMine(String taskName) {
		return false;
	}
}