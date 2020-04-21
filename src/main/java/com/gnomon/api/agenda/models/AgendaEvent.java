package com.gnomon.api.agenda.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
	LocalDateTime date;
	
	@Enumerated(EnumType.STRING)
	RecurrenceRule recurrenceRule;
	
	@NotBlank
	LocalDateTime lastRecurrence;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Agenda getAgenda() {
		return agenda;
	}

	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getFullDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public RecurrenceRule getRecurrenceRule() {
		return recurrenceRule;
	}

	public void setRecurrenceRule(RecurrenceRule recurrenceRule) {
		this.recurrenceRule = recurrenceRule;
	}

	public LocalDateTime getLastRecurrence() {
		return lastRecurrence;
	}

	public void setLastRecurrence(LocalDateTime lastRecurrence) {
		this.lastRecurrence = lastRecurrence;
	}
	
	public LocalDate getDate() {
		return date.toLocalDate();
	}
	
	
	
}
