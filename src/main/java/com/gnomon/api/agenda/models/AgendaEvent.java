package com.gnomon.api.agenda.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.gnomon.api.agenda.models.enums.RecurrenceRule;
import com.gnomon.api.models.audits.UserDateAudit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"agendas"})
@ToString(exclude = {"agendas"})
@Table(name="agenda_events")
public class AgendaEvent extends UserDateAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToMany(mappedBy = "events")
	private List<Agenda> agendas;
	
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
