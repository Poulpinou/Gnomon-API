package com.gnomon.api.agenda.models.keys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AgendaConnectionKey implements Serializable {
	@Column(name = "user_id")
	@NonNull
	private Long userId;
	
	@Column(name = "agenda_id")
	@NonNull
	private Long agendaId;
}
