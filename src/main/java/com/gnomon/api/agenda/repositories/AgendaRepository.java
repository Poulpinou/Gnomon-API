package com.gnomon.api.agenda.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gnomon.api.agenda.models.Agenda;

public interface AgendaRepository extends 
	JpaRepository<Agenda, Long>,
	JpaSpecificationExecutor<Agenda>
{
    List<Agenda> findByIdIn(List<Long> agendaIds);
}
