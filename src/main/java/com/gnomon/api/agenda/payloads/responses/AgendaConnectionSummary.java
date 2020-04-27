package com.gnomon.api.agenda.payloads.responses;

import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.payloads.responses.DatedResponse;
import com.gnomon.api.payloads.responses.UserSummary;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AgendaConnectionSummary extends DatedResponse<AgendaConnection> {
	
	private UserSummary user;
	
	private AgendaSummary agenda;
	
	private AgendaConnectionType connectionType;
	
	private boolean shown;	
	
	public AgendaConnectionSummary(AgendaConnection connection) {
		super(connection);
	}
	
	@Override
	protected void mapObjectToResponse(AgendaConnection connection) {
		super.mapObjectToResponse(connection);
		
		this.user = new UserSummary(connection.getUser());
		this.agenda = new AgendaSummary(connection.getAgenda());
		this.connectionType = connection.getConnectionType();
		this.shown = connection.isShown();
	}
}
