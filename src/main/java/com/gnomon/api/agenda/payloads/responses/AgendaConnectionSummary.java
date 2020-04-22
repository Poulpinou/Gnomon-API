package com.gnomon.api.agenda.payloads.responses;

import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.payloads.responses.UserSummary;

import lombok.Data;

@Data
public class AgendaConnectionSummary {
	private UserSummary user;
	
	private AgendaSummary agenda;
	
	private AgendaConnectionType connectionType;
	
	private boolean shown;	
}
