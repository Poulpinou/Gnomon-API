package com.gnomon.api.agenda.models.keys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Embeddable
@Data
@EqualsAndHashCode(callSuper = false)
public class AgendaConnectionKey implements Serializable {
	@Column(name = "user_id")
	@NonNull
	private Long userId;
	
	@Column(name = "agenda_id")
	@NonNull
	private Long agendaId;
}
