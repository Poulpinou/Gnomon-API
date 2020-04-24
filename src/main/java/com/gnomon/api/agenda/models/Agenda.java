package com.gnomon.api.agenda.models;

import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.gnomon.api.models.audits.UserDateAudit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"connections","events"})
@NoArgsConstructor
@ToString(exclude = {"connections","events"})
@Table(name="agendas")
public class Agenda extends UserDateAudit{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@NonNull
	@Size(min = 3, max = 64)
	private String name;
	
	@Size(max = 256)
	private String description;
	
	private boolean shared;

	@OneToMany(mappedBy = "agenda")
	private Set<AgendaConnection> connections;
	
	@ManyToMany(mappedBy = "agendas", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<AgendaEvent> events;
	
	public Agenda(String name, String description, boolean isShared) {
		this.name = name;
		this.description = description;
		this.shared = isShared;
	}
	
	public void AddEvent(AgendaEvent event) {
		events.add(event);
	}
}
