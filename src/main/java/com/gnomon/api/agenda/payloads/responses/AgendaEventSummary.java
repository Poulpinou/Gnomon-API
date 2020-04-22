package com.gnomon.api.agenda.payloads.responses;

import java.time.LocalDateTime;
import com.gnomon.api.agenda.models.AgendaEvent;
import com.gnomon.api.payloads.responses.UserDatedResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AgendaEventSummary extends UserDatedResponse<AgendaEvent> {
	private Long id;
	
	private Long agendaId;
	
	private String title;
	
	private String description;
	
	LocalDateTime date;
	
	public AgendaEventSummary(AgendaEvent event) {
		super(event);
	}
	
	@Override
	protected void mapObjectToResponse(AgendaEvent event) {
		super.mapObjectToResponse(event);
		
		this.id = event.getId();
		this.agendaId = event.getAgenda().getId();
		this.title = event.getTitle();
		this.description = event.getDescription();
		this.date = event.getDate();
	}
}
