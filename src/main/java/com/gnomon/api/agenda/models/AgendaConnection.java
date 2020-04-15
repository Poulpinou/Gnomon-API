package com.gnomon.api.agenda.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.agenda.models.keys.AgendaConnectionKey;
import com.gnomon.api.models.User;
import com.gnomon.api.models.audits.DateAudit;

@Entity
@Table(name="agenda_connections")
public class AgendaConnection extends DateAudit  {
	
	@EmbeddedId
	AgendaConnectionKey id;
	
	@ManyToOne
	@MapsId("user_id")
	@JoinColumn(name="user_id")
	User user;
	
	@ManyToOne
	@MapsId("agenda_id")
	@JoinColumn(name="agenda_id")
	Agenda agenda;
	
	@NotBlank
	AgendaConnectionType connectionType;
	
	public AgendaConnection() {}
	
	public AgendaConnection(User user, Agenda agenda, AgendaConnectionType connectionType) {
		this.user = user;
		this.agenda = agenda;
		this.connectionType = connectionType;
		this.id = new AgendaConnectionKey(user.getId(), agenda.getId());
	}

	public AgendaConnectionKey getId() {
		return id;
	}

	public void setId(AgendaConnectionKey id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Agenda getAgenda() {
		return agenda;
	}

	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}

	public AgendaConnectionType getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(AgendaConnectionType connectionType) {
		this.connectionType = connectionType;
	}
	
	
}
