package com.gnomon.api.agenda.utils.specs;

import org.springframework.data.jpa.domain.Specification;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.models.User;

public class AgendaConnectionSpecifications {

	public static Specification<AgendaConnection> byUser(User user){
		return (root, query, cb) -> cb.equal(root.<User>get("user"), user);
	}
	
	public static Specification<AgendaConnection> byAgendaId(Long agendaId){
		return (root, query, cb) -> cb.equal(root.<Long>get("agendaId"), agendaId);
	}
	
	public static Specification<AgendaConnection> connectionIs(AgendaConnectionType connectionType){
		return (root, query, cb) -> cb.equal(root.<AgendaConnectionType>get("connectionType"), connectionType);
	}
	
	public static Specification<AgendaConnection> connectionIsNot(AgendaConnectionType connectionType){
		return (root, query, cb) -> cb.notEqual(root.<AgendaConnectionType>get("connectionType"), connectionType);
	}
	
	public static Specification<AgendaConnection> agendaIsShared(){
		return (root, query, cb) -> cb.isTrue(root.<Agenda>get("agenda").get("shared"));
	}
}
