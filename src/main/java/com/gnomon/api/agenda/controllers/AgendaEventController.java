package com.gnomon.api.agenda.controllers;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gnomon.api.AppConstants;
import com.gnomon.api.agenda.payloads.requests.AgendaEventRequest;
import com.gnomon.api.agenda.payloads.responses.AgendaEventSummary;
import com.gnomon.api.agenda.services.AgendaEventService;
import com.gnomon.api.payloads.responses.ApiResponse;
import com.gnomon.api.payloads.responses.PagedResponse;
import com.gnomon.api.security.CurrentUser;
import com.gnomon.api.security.UserPrincipal;
import com.gnomon.api.utils.editors.LocalDateEditor;

@RestController
@RequestMapping("/api")
@Transactional(readOnly = true)
public class AgendaEventController {
	
	private AgendaEventService agendaEventService;
	
	@Autowired
	public AgendaEventController(AgendaEventService agendaEventService) {
		this.agendaEventService = agendaEventService;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {
	    binder.registerCustomEditor(LocalDate.class, new LocalDateEditor());
	}
	
	@GetMapping("/agendas/events")
	public PagedResponse<?> getAll(
			@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "from", defaultValue = "today") LocalDate from,
			@RequestParam(value = "to", defaultValue = "endOfLife") LocalDate to,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size
		){
		return agendaEventService.getAllEvents(currentUser.getId(), from, to, page, size);
	}
	
	@GetMapping("/agendas/events/{eventId}")
    public AgendaEventSummary getEventById(
    		@CurrentUser UserPrincipal currentUser, 
    		@PathVariable Long eventId
		) {
        return agendaEventService.getEventById(currentUser.getId(), eventId);
    }
	
	@PostMapping("/agendas/events")
	@Transactional
	public ResponseEntity<?> createEvent(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody AgendaEventRequest request){
		agendaEventService.createEvent(currentUser.getId(), request);
		
		return ResponseEntity.ok()
				.body(new ApiResponse(true, "Event successfully created"));
	}
	
	@PutMapping("/agendas/events/{eventId}")
	@Transactional
	public ResponseEntity<?> updateEvent(@CurrentUser UserPrincipal currentUser, @PathVariable Long eventId, @Valid @RequestBody AgendaEventRequest request){
		agendaEventService.updateEvent(currentUser.getId(), eventId, request);
		
		return ResponseEntity.ok()
				.body(new ApiResponse(true, "Event successfully updated"));
	}
	
	@DeleteMapping("/agendas/events/{eventId}")
	@Transactional
	public ResponseEntity<?> deleteEvent(@CurrentUser UserPrincipal currentUser, @PathVariable Long eventId){
		agendaEventService.deleteEvent(currentUser.getId(), eventId);
		
		return ResponseEntity.ok()
				.body(new ApiResponse(true, "Event successfully deleted"));
	}
	
	@PostMapping("/agendas/{agendaId}/events/{eventId}")
	@Transactional
	public ResponseEntity<?> connectEventToAgenda(@CurrentUser UserPrincipal currentUser, @PathVariable Long agendaId, @PathVariable Long eventId){
		agendaEventService.connectEventToAgenda(currentUser.getId(), agendaId, eventId);
		
		return ResponseEntity.ok()
				.body(new ApiResponse(true, "Event successfully connected"));
	}
	
	@DeleteMapping("/agendas/{agendaId}/events/{eventId}")
	@Transactional
	public ResponseEntity<?> disconnectEventFromAgenda(@CurrentUser UserPrincipal currentUser, @PathVariable Long agendaId, @PathVariable Long eventId){
		agendaEventService.disconnectEventFromAgenda(currentUser.getId(), agendaId, eventId);
		
		return ResponseEntity.ok()
				.body(new ApiResponse(true, "Event successfully disconnected"));
	}
	
}
