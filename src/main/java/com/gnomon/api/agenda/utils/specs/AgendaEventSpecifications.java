package com.gnomon.api.agenda.utils.specs;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.agenda.models.AgendaEvent;

public class AgendaEventSpecifications {
	
	public static Specification<AgendaEvent> byAgendas(List<Agenda> agendas){
		return (root, query, cb) -> root.join("agendas").in(agendas);
	}
	
	public static Specification<AgendaEvent> isBetweenDates(LocalDate from, LocalDate to){
		return (root, query, cb) -> cb.between(root.<LocalDate>get("day"), from, to);
	}
}
