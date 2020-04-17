package com.gnomon.api.agenda.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.agenda.payloads.responses.AgendaSummary;
import com.gnomon.api.agenda.repositories.AgendaConnectionRepository;
import com.gnomon.api.agenda.services.AgendaService;
import com.gnomon.api.security.CurrentUser;
import com.gnomon.api.security.UserPrincipal;

@RestController
@RequestMapping("/api/agendas")
public class AgendaController {
	
	@Autowired
	private AgendaConnectionRepository connectionRepository;
	
	@Autowired
	private AgendaService agendaService;
	
	@GetMapping("/connected")
	public List<AgendaSummary> getMyAgendas(@CurrentUser UserPrincipal currentUser){
		return agendaService.getAllAgendasForUser(currentUser);
	}
	
	@GetMapping("/main")
	public AgendaSummary getMainAgenda(@CurrentUser UserPrincipal currentUser){
		AgendaConnection connection = connectionRepository.findByUserIdAndConnectionType(currentUser.getId(), AgendaConnectionType.MAIN).get();
		Agenda agenda = connection.getAgenda();
		
		return new AgendaSummary(agenda);
	}
}
