package com.gnomon.api.agenda.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.agenda.payloads.responses.AgendaSummary;
import com.gnomon.api.agenda.repositories.AgendaConnectionRepository;
import com.gnomon.api.agenda.repositories.AgendaRepository;
import com.gnomon.api.models.User;
import com.gnomon.api.repositories.UserRepository;
import com.gnomon.api.security.CurrentUser;
import com.gnomon.api.security.UserPrincipal;

@RestController
@RequestMapping("/api/agendas")
public class AgendaController {
	
	@Autowired
	private AgendaRepository agendaRepository;
	
	@Autowired
	private AgendaConnectionRepository connectionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/main")
	public AgendaSummary getMainAgenda(@CurrentUser UserPrincipal currentUser){

		AgendaConnection connection = connectionRepository.findByUserIdAndConnectionType(currentUser.getId(), AgendaConnectionType.MAIN).get();
		Agenda agenda = connection.getAgenda();
		
		return new AgendaSummary(agenda);
	}
}
