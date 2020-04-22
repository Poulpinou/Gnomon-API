package com.gnomon.api.agenda.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.gnomon.api.agenda.models.enums.RecurrenceRule;
import com.gnomon.api.models.audits.UserDateAudit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name="agenda_events")
public class AgendaEvent extends UserDateAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@MapsId("agenda_id")
	@JoinColumn(name="agenda_id")
	@NonNull
	private Agenda agenda;
	
	@NotBlank
	@Size(min = 3, max = 64)
	@NonNull
	private String title;
	
	@Size(max = 256)
	private String description;
	
	@NotBlank
	@NonNull
	LocalDateTime date;
	
	@Enumerated(EnumType.STRING)
	RecurrenceRule recurrenceRule;
	
	@NotBlank
	LocalDateTime lastRecurrence;
	
	public LocalDate getDay() {
		return date.toLocalDate();
	}
}
