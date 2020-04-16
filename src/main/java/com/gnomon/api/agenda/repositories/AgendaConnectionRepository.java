package com.gnomon.api.agenda.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.agenda.models.keys.AgendaConnectionKey;

public interface AgendaConnectionRepository extends JpaRepository<AgendaConnection, AgendaConnectionKey> {
	Optional<AgendaConnection> findByUserId(Long userId);
	
	Optional<AgendaConnection> findByAgendaId(Long agendaId);
	
	Optional<AgendaConnection> findByUserIdAndConnectionType(Long userId, AgendaConnectionType connectionType);
	
	Boolean existsByUserIdAndConnectionType(Long userId, AgendaConnectionType connectionType);

	Boolean existsByUserIdAndAgendaId(Long userId, Long agendaId);
}
