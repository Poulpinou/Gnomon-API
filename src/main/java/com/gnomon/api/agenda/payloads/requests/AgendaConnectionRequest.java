package com.gnomon.api.agenda.payloads.requests;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AgendaConnectionRequest {
	@NotNull
	private Long agendaId;
	
	private boolean shared;
}
