package com.gnomon.api.agenda.payloads.requests;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AgendaConnectionRequest {
	@NotNull
	private Long userId;
	
	@NotNull
	private Long agendaId;
	
	private boolean owner = false;
	
	private boolean shown = true;
}
