package com.gnomon.api.agenda.payloads.requests;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AgendaEventRequest {
	
	@NotBlank
	@Size(min = 3, max = 64)
	private String title;
	
	@Size(max = 256)
	private String description;
	
	@NotBlank
	LocalDateTime date;
}
