package com.gnomon.api.agenda.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gnomon.api.agenda.models.AgendaEvent;

public interface AgendaEventRepository extends 
	JpaRepository<AgendaEvent, Long> ,
	JpaSpecificationExecutor<AgendaEvent>
{
	/*
	@Query("SELECT e FROM AgendaEvent e WHERE e.agenda.id IN :agendaIds AND e.date BETWEEN :from AND :to")
	Page<AgendaEvent> getEventsFromAgendasBetweenDates(
			@Param("agendaIds") List<Long> agendaIds, 
			@Param("from") LocalDateTime from, 
			@Param("to") LocalDateTime to,
			Pageable pageable
		);
		*/
}
