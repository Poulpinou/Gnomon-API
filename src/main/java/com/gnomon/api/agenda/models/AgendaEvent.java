package com.gnomon.api.agenda.models;

import java.time.Instant;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.gnomon.api.agenda.models.enums.RecurrenceRule;
import com.gnomon.api.models.audits.UserDateAudit;

@Entity
@Table(name="agenda_events")
public class AgendaEvent extends UserDateAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@MapsId("agenda_id")
	@JoinColumn(name="agenda_id")
	private Agenda agenda;
	
	@NotBlank
	@Size(min = 3, max = 64)
	private String title;
	
	@Size(max = 256)
	private String description;
	
	@NotBlank
	Instant date;
	
	@Enumerated(EnumType.STRING)
	RecurrenceRule recurrenceRule;
	
	@NotBlank
	Instant lastRecurrence;
	
	
	
	
	
}
