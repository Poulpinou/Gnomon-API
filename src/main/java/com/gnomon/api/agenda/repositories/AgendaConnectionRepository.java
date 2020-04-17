package com.gnomon.api.agenda.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.agenda.models.keys.AgendaConnectionKey;

public interface AgendaConnectionRepository extends JpaRepository<AgendaConnection, AgendaConnectionKey> {
	Optional<AgendaConnection> findByUserId(Long userId);
	
	Optional<AgendaConnection> findByAgendaId(Long agendaId);
	
	Optional<AgendaConnection> findByUserIdAndConnectionType(Long userId, AgendaConnectionType connectionType);
	
	Boolean existsByUserIdAndConnectionType(Long userId, AgendaConnectionType connectionType);

	Boolean existsByUserIdAndAgendaId(Long userId, Long agendaId);
	
	List<AgendaConnection> findAllByUserId(Long userId);
	
	@Query("SELECT a.agenda_id FROM AgendaConnection c INNER JOIN Agenda a ON c.agenda.id  = a.id WHERE c.user.id = :userId AND ( a.is_public = TRUE OR NOT c.connection_type = 2 )")
	List<Long> findAgendaIdsByUserId(@Param("userId") Long userId);
}
