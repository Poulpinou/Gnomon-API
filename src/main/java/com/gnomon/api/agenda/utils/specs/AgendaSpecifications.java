package com.gnomon.api.agenda.utils.specs;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.models.User;;


public class AgendaSpecifications {
	
	public static Specification<Agenda> byId(Long agendaId){
		return (root, query, cb) -> {
			return cb.equal(root.<Long>get("id"), agendaId);
		};
	}
	
	public static Specification<Agenda> isShared(){
		return (root, query, cb) -> cb.isTrue(root.get("shared"));
	}
	
	public static Specification<Agenda> byUser(User user){
		return (root, query, cb) -> {
			final Join<Agenda, AgendaConnection> join = root.join("connections");
			return cb.equal(join.<User>get("user"), user);
		};
	}
	
	public static Specification<Agenda> byConnectionType(AgendaConnectionType connectionType){
		return (root, query, cb) -> {
			final Join<Agenda, AgendaConnection> join = root.join("connections");
			return cb.equal(join.<AgendaConnectionType>get("connectionType"), connectionType);
		};
	}
	
	public static Specification<Agenda> isVisibleByUser(User user){
		return (root, query, cb) -> {
			final Join<Agenda, AgendaConnection> join = root.join("connections");
			return cb.and(
				cb.equal(join.<User>get("user"), user),		
				cb.or(
					cb.notEqual(join.<AgendaConnectionType>get("connectionType"), AgendaConnectionType.VIEWER),
					cb.isTrue(root.get("shared"))
				)
			);
		};
	}
}
