package com.gnomon.api.agenda.models;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.gnomon.api.models.audits.UserDateAudit;

@Entity
@Table(name="agendas")
public class Agenda extends UserDateAudit{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(min = 3, max = 64)
	private String name;
	
	@Size(max = 256)
	private String description;
	
	private Boolean isPublic;

	@OneToMany(mappedBy = "agenda")
	Set<AgendaConnection> connections;

	public Agenda() {}
	
	public Agenda(String name, String description, Boolean isPublic) {
		this.name = name;
		this.description = description;
		this.isPublic = isPublic;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<AgendaConnection> getConnections() {
		return connections;
	}

	public void setConnections(Set<AgendaConnection> connections) {
		this.connections = connections;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean isPublic() {
		return isPublic;
	}

	public void setPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
}
