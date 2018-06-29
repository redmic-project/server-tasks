package es.redmic.tasks.ingest.model.common.dto;

import org.springframework.context.ApplicationEvent;

import es.redmic.tasks.ingest.model.status.dto.UserTaskDTO;

public class UserTaskDTOEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	private UserTaskDTO dto;

	public UserTaskDTOEvent(Object source, UserTaskDTO dto) {
		super(source);
		this.setDto(dto);
	}

	public UserTaskDTO getDto() {
		return dto;
	}

	public void setDto(UserTaskDTO dto) {
		this.dto = dto;
	}
}