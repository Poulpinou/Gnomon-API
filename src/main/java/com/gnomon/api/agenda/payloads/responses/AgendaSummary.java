package com.gnomon.api.agenda.payloads.responses;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.payloads.responses.UserDatedResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AgendaSummary extends UserDatedResponse<Agenda> {
	
	private Long id;
	private String name;
	private String description;
	private boolean shared;
	
	public AgendaSummary(Agenda agenda) {
		super(agenda);
	}
	
	@Override
	protected void mapObjectToResponse(Agenda agenda) {
		super.mapObjectToResponse(agenda);
		
		this.id = agenda.getId();
		this.name = agenda.getName();
		this.description = agenda.getDescription();
		this.shared = agenda.isShared();
	}
}

