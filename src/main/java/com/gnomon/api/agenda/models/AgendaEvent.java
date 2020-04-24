package com.gnomon.api.agenda.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.gnomon.api.agenda.models.enums.RecurrenceRule;
import com.gnomon.api.models.audits.UserDateAudit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"agendas"})
@ToString(exclude = {"agendas"})
@Table(name="agenda_events")
public class AgendaEvent extends UserDateAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
			name = "agendas_events",
			joinColumns = @JoinColumn(name = "event_id"),
			inverseJoinColumns = @JoinColumn(name = "agenda_id")
		)
	private Set<Agenda> agendas;
	
	@NotBlank
	@Size(min = 3, max = 64)
	@NonNull
	private String title;
	
	@Size(max = 256)
	private String description;
	
	
	@NonNull
	LocalDateTime date;
	
	@Enumerated(EnumType.STRING)
	RecurrenceRule recurrenceRule;
	
	
	LocalDateTime lastRecurrence;
	
	public AgendaEvent(String title, String description, LocalDateTime date) {
		this.title = title;
		this.description = description;
		this.date = date;
	}
	
	public LocalDate getDay() {
		return date.toLocalDate();
	}
}
