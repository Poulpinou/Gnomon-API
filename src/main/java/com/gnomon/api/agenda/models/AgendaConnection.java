package com.gnomon.api.agenda.models;

import javax.persistence.*;

import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.agenda.models.keys.AgendaConnectionKey;
import com.gnomon.api.models.User;
import com.gnomon.api.models.audits.DateAudit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name="agenda_connections")
public class AgendaConnection extends DateAudit  {
	
	@EmbeddedId
	private AgendaConnectionKey id;
	
	@ManyToOne
	@MapsId("user_id")
	@JoinColumn(name="user_id")
	@NonNull
	private User user;
	
	@ManyToOne
	@MapsId("agenda_id")
	@JoinColumn(name="agenda_id")
	@NonNull
	private Agenda agenda;
	
	private boolean shown;
	
	@NonNull
	@Enumerated(EnumType.STRING)
	private AgendaConnectionType connectionType;
	
	public AgendaConnection(User user, Agenda agenda, AgendaConnectionType connectionType, boolean isShown) {
		this.user = user;
		this.agenda = agenda;
		this.connectionType = connectionType;
		this.shown = isShown;
		this.id = new AgendaConnectionKey(user.getId(), agenda.getId());
	}
}
