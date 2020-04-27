package com.gnomon.api.agenda.payloads.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AgendaRequest {
	@NotBlank
	@Size(min = 3, max = 64)
	private String name;
	
	@Size(max = 256)
	private String description;
	
	private boolean shared;
}
