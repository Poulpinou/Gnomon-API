package com.gnomon.api.agenda.models.keys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AgendaConnectionKey implements Serializable {
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "agenda_id")
	private Long agendaId;
	
	public AgendaConnectionKey() {}
	
	public AgendaConnectionKey(Long userId, Long agendaId) {
		this.userId = userId;
		this.agendaId = agendaId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAgendaId() {
		return agendaId;
	}

	public void setAgendaId(Long agendaId) {
		this.agendaId = agendaId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agendaId == null) ? 0 : agendaId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AgendaConnectionKey other = (AgendaConnectionKey) obj;
		if (agendaId == null) {
			if (other.agendaId != null)
				return false;
		} else if (!agendaId.equals(other.agendaId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
}
