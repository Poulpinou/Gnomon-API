package com.gnomon.api.agenda.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.agenda.payloads.requests.AgendaRequest;
import com.gnomon.api.agenda.payloads.responses.AgendaSummary;
import com.gnomon.api.agenda.repositories.AgendaConnectionRepository;
import com.gnomon.api.agenda.services.AgendaService;
import com.gnomon.api.exceptions.ResourceNotFoundException;
import com.gnomon.api.models.User;
import com.gnomon.api.payloads.responses.ApiResponse;
import com.gnomon.api.repositories.UserRepository;
import com.gnomon.api.security.CurrentUser;
import com.gnomon.api.security.UserPrincipal;

@RestController
@RequestMapping("/api/agendas")
public class AgendaController {
	
	@Autowired
	private AgendaConnectionRepository connectionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AgendaService agendaService;
	
	@GetMapping("/connected")
	public List<AgendaSummary> getMyAgendas(@CurrentUser UserPrincipal currentUser){
		return agendaService.getAllAgendaSummariesByUserId(currentUser.getId());
	}
	
	@GetMapping("/main")
	public AgendaSummary getMainAgenda(@CurrentUser UserPrincipal currentUser){
		AgendaConnection connection = connectionRepository.findByUserIdAndConnectionType(currentUser.getId(), AgendaConnectionType.MAIN).get();
		Agenda agenda = connection.getAgenda();
		
		return new AgendaSummary(agenda);
	}
	
	@GetMapping("/{agendaId}")
	public AgendaSummary getAgendaById(@CurrentUser UserPrincipal currentUser, @PathVariable Long agendaId) {
		return agendaService.getAgendaSummaryById(agendaId, currentUser.getId());
	}
	
	@PostMapping
	public ResponseEntity<?> createAgenda(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody AgendaRequest request){
		User user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));
		
		Agenda agenda = agendaService.createAgenda(user, request);		
		
		URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{agendaId}")
                .buildAndExpand(agenda.getId()).toUri();
		
		return ResponseEntity.created(location)
				.body(new ApiResponse(true, "Agenda sucessfully created"));
	}
	
	@PutMapping("/{agendaId}")
	public ResponseEntity<?> updateAgenda(@CurrentUser UserPrincipal currentUser, @PathVariable Long agendaId, @Valid @RequestBody AgendaRequest request){
		agendaService.updateAgenda(currentUser.getId(), agendaId, request);
		
		return ResponseEntity.ok()
				.body(new ApiResponse(true, "Agenda sucessfully updated"));
	}
	
	@DeleteMapping("/{agendaId}")
	public ResponseEntity<?> deleteAgenda(@CurrentUser UserPrincipal currentUser, @PathVariable Long agendaId){
		agendaService.deleteAgenda(currentUser.getId(), agendaId);
		
		return ResponseEntity.ok()
				.body(new ApiResponse(true, "Agenda sucessfully deleted"));
	}
}
