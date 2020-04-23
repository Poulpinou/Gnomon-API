package com.gnomon.api.agenda.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gnomon.api.agenda.payloads.responses.AgendaConnectionSummary;
import com.gnomon.api.agenda.services.AgendaConnectionService;
import com.gnomon.api.security.CurrentUser;
import com.gnomon.api.security.UserPrincipal;

@RestController
@RequestMapping("/api/agendas/connections")
@Transactional(readOnly = true)
public class AgendaConnectionController {
	
	private AgendaConnectionService connectionService;
	
	@Autowired
	public AgendaConnectionController(AgendaConnectionService connectionService) {
		this.connectionService = connectionService;
	}
	
	@GetMapping
	public List<AgendaConnectionSummary> getMyAgendas(@CurrentUser UserPrincipal currentUser){
		return connectionService.getAllConnectionsByUserId(currentUser.getId());
	}
}
