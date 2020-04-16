package com.gnomon.api.agenda.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.bind.DefaultValue;

import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.agenda.models.keys.AgendaConnectionKey;
import com.gnomon.api.models.User;
import com.gnomon.api.models.audits.DateAudit;

@Entity
@Table(name="agenda_connections")
public class AgendaConnection extends DateAudit  {
	
	@EmbeddedId
	private AgendaConnectionKey id;
	
	@ManyToOne
	@MapsId("user_id")
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
	@MapsId("agenda_id")
	@JoinColumn(name="agenda_id")
	private Agenda agenda;
	
	private Boolean isShown;
	
	@Enumerated(EnumType.ORDINAL)
	AgendaConnectionType connectionType;
	
	public AgendaConnection() {}
	
	public AgendaConnection(User user, Agenda agenda, AgendaConnectionType connectionType, boolean isShown) {
		this.user = user;
		this.agenda = agenda;
		this.connectionType = connectionType;
		this.isShown = isShown;
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

	public Boolean isShown() {
		return isShown;
	}

	public void setShown(Boolean isShown) {
		this.isShown = isShown;
	}
	
	
}
