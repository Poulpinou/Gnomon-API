package com.gnomon.api.agenda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gnomon.api.agenda.models.AgendaEvent;

public interface AgendaEventRepository extends 
	JpaRepository<AgendaEvent, Long> ,
	JpaSpecificationExecutor<AgendaEvent>
{
	
}
