package com.gnomon.api.agenda.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gnomon.api.agenda.payloads.requests.AgendaRequest;
import com.gnomon.api.agenda.payloads.responses.AgendaSummary;
import com.gnomon.api.agenda.services.AgendaService;
import com.gnomon.api.payloads.responses.ApiResponse;
import com.gnomon.api.security.CurrentUser;
import com.gnomon.api.security.UserPrincipal;

@RestController
@RequestMapping("/api/agendas")
@Transactional(readOnly = true)
public class AgendaController {
	
	private AgendaService agendaService;
	
	@Autowired
	public AgendaController(AgendaService agendaService) {
		this.agendaService = agendaService;
	}
	
	@GetMapping("/connected")
	public List<AgendaSummary> getMyAgendas(@CurrentUser UserPrincipal currentUser){
		return agendaService.getAllAgendasByUserId(currentUser.getId());
	}
	
	@GetMapping("/main")
	public AgendaSummary getMainAgenda(@CurrentUser UserPrincipal currentUser){
		return agendaService.getMainAgenda(currentUser.getId());
	}
	
	@GetMapping("/{agendaId}")
	public AgendaSummary getAgendaById(@CurrentUser UserPrincipal currentUser, @PathVariable Long agendaId) {
		return agendaService.getAgendaById(agendaId, currentUser.getId());
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> createAgenda(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody AgendaRequest request){
		agendaService.createAgenda(currentUser.getId(), request);
		
		return ResponseEntity.ok()
				.body(new ApiResponse(true, "Agenda sucessfully created"));
	}
	
	@PutMapping("/{agendaId}")
	@Transactional
	public ResponseEntity<?> updateAgenda(@CurrentUser UserPrincipal currentUser, @PathVariable Long agendaId, @Valid @RequestBody AgendaRequest request){
		agendaService.updateAgenda(currentUser.getId(), agendaId, request);
		
		return ResponseEntity.ok()
				.body(new ApiResponse(true, "Agenda sucessfully updated"));
	}
	
	@DeleteMapping("/{agendaId}")
	@Transactional
	public ResponseEntity<?> deleteAgenda(@CurrentUser UserPrincipal currentUser, @PathVariable Long agendaId){
		agendaService.deleteAgenda(currentUser.getId(), agendaId);
		
		return ResponseEntity.ok()
				.body(new ApiResponse(true, "Agenda sucessfully deleted"));
	}
}
