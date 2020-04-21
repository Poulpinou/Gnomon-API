package com.gnomon.api.agenda.payloads.responses;

import java.time.LocalDateTime;
import com.gnomon.api.agenda.models.AgendaEvent;
import com.gnomon.api.payloads.responses.UserDatedResponse;

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
	protected void MapObjectToResponse(AgendaEvent event) {
		super.MapObjectToResponse(event);
		
		this.id = event.getId();
		this.agendaId = event.getAgenda().getId();
		this.title = event.getTitle();
		this.description = event.getDescription();
		this.date = event.getFullDate();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAgendaId() {
		return agendaId;
	}

	public void setAgendaId(Long agendaId) {
		this.agendaId = agendaId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
}
