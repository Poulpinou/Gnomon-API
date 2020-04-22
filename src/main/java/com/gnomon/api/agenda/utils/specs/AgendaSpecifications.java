package com.gnomon.api.agenda.utils.specs;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.models.User;;


public class AgendaSpecifications {
	
	public static Specification<Agenda> isShared(){
		return (root, query, cb) -> cb.isTrue(root.get("shared"));
	}
	
	public static Specification<Agenda> userCanSee(Long userId){
		return (root, query, cb) -> {
			final Join<Agenda, AgendaConnection> join = root.join("connections", JoinType.INNER);
			return cb.and(
				cb.equal(join.<User>get("user").<Long>get("id"), userId),		
				cb.or(
					cb.notEqual(join.<AgendaConnectionType>get("connectionType"), AgendaConnectionType.VIEWER),
					cb.isTrue(root.get("shared"))
				)
			);
		};
	}
}
