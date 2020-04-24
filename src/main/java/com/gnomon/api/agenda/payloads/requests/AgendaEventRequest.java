package com.gnomon.api.agenda.payloads.requests;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class AgendaEventRequest {
	
	@NotBlank
	@Size(min = 3, max = 64)
	private String title;
	
	@Size(max = 256)
	private String description;
	
	@DateTimeFormat(pattern="dd-MMM-yyyy hh:mm:ss")
	LocalDateTime date;
	
	private List<Long> agendaIds;
}
