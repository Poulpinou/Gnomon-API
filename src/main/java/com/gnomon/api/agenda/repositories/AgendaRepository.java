package com.gnomon.api.agenda.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gnomon.api.agenda.models.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
	Optional<Agenda> findById(Long id);
	
	Page<Agenda> findByCreatedBy(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);
}
