package com.gnomon.api.agenda.payloads.responses;

import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.payloads.responses.UserSummary;

public class AgendaConnectionSummary {
	private UserSummary user;
	
	private AgendaSummary agenda;
	
	private String connectionType;
	
	private Boolean shown;

	public Boolean isShown() {
		return shown;
	}

	public void setShown(Boolean shown) {
		this.shown = shown;
	}

	public UserSummary getUser() {
		return user;
	}

	public void setUser(UserSummary user) {
		this.user = user;
	}

	public AgendaSummary getAgenda() {
		return agenda;
	}

	public void setAgenda(AgendaSummary agenda) {
		this.agenda = agenda;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}
	
	public void setConnectionType(AgendaConnectionType connectionType) {
		this.connectionType = connectionType.toString();
	}
}
