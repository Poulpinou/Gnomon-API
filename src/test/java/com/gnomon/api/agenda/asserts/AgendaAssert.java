package com.gnomon.api.agenda.asserts;

import org.assertj.core.api.AbstractAssert;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.agenda.payloads.requests.AgendaRequest;

public class AgendaAssert extends AbstractAssert<AgendaAssert, Agenda> {

	public AgendaAssert(Agenda agenda) {
		super(agenda, AgendaAssert.class);
	}
	
	public AgendaAssert isFetchingToRequest(AgendaRequest request) {
		isNotNull();
		if(actual.getName() != request.getName())
			failWithMessage("Agenda's name doesn't fetch to request's name", request);
		if(actual.getDescription() != request.getDescription())
			failWithMessage("Agenda's decription doesn't fetch to request's decription", request);
		if(actual.isShared() != request.isShared())
			failWithMessage("Agenda's shared status doesn't fetch to request's shared status", request);
		
		return this;
	}
}
