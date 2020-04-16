package com.gnomon.api.agenda.payloads.requests;

import javax.validation.constraints.NotNull;

public class AgendaConnectionRequest {
	@NotNull
	private Long agendaId;
	
	private boolean isShown;

	public Long getAgendaId() {
		return agendaId;
	}

	public void setAgendaId(Long agendaId) {
		this.agendaId = agendaId;
	}

	public boolean isShown() {
		return isShown;
	}

	public void setShown(boolean isShown) {
		this.isShown = isShown;
	}
}
