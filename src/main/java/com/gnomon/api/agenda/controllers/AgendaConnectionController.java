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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gnomon.api.agenda.payloads.requests.AgendaConnectionRequest;
import com.gnomon.api.agenda.payloads.responses.AgendaConnectionSummary;
import com.gnomon.api.agenda.services.AgendaConnectionService;
import com.gnomon.api.payloads.responses.ApiResponse;
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
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> connectAgenda(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody AgendaConnectionRequest request){
		connectionService.connectAgenda(currentUser.getId(), request);
		
		return ResponseEntity.ok()
				.body(new ApiResponse(true, "Event successfully connected"));
	}
	
	@DeleteMapping("/{agendaId}")
	@Transactional
	public ResponseEntity<?> disconnectAgenda(@CurrentUser UserPrincipal currentUser, @PathVariable Long agendaId, @RequestParam(name = "userId", defaultValue = "0") Long userId){
		connectionService.disconnectAgenda(currentUser.getId(), userId == 0 ? currentUser.getId() : userId, agendaId);
		
		return ResponseEntity.ok()
				.body(new ApiResponse(true, "Event successfully disconnected"));
	}
}
