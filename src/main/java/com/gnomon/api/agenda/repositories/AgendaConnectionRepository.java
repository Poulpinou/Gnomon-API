package com.gnomon.api.agenda.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.agenda.models.keys.AgendaConnectionKey;

public interface AgendaConnectionRepository extends 
	JpaRepository<AgendaConnection, AgendaConnectionKey>,
	JpaSpecificationExecutor<AgendaConnection>
{		
	Optional<AgendaConnection> findByUserIdAndAgendaId(Long userId, Long agendaId);

	Boolean existsByUserIdAndConnectionType(Long userId, AgendaConnectionType connectionType);

	Boolean existsByUserIdAndAgendaId(Long userId, Long agendaId);
}
