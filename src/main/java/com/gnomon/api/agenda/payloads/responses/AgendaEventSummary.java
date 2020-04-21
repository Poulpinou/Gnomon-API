package com.gnomon.api.agenda.payloads.responses;

import com.gnomon.api.agenda.models.AgendaEvent;
import com.gnomon.api.payloads.responses.UserDatedResponse;

public class AgendaEventSummary extends UserDatedResponse<AgendaEvent> {

	
	public AgendaEventSummary(AgendaEvent userDatedObject) {
		super(userDatedObject);
	}

	@Override
	protected void MapObjectToResponse(AgendaEvent objectToMap) {
		super.MapObjectToResponse(objectToMap);
		
		
	}
}
