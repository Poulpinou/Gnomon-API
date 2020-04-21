package com.gnomon.api.agenda.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gnomon.api.AppConstants;
import com.gnomon.api.agenda.payloads.responses.AgendaEventSummary;
import com.gnomon.api.agenda.payloads.responses.Day;
import com.gnomon.api.agenda.repositories.AgendaEventRepository;
import com.gnomon.api.agenda.services.AgendaEventService;
import com.gnomon.api.exceptions.ResourceNotFoundException;
import com.gnomon.api.payloads.responses.PagedResponse;
import com.gnomon.api.security.CurrentUser;
import com.gnomon.api.security.UserPrincipal;
import com.gnomon.api.utils.editors.LocalDateEditor;

@RestController
@RequestMapping("/api/agendas/events")
public class AgendaEventController {

	@Autowired
	private AgendaEventRepository eventRepository;
	
	@Autowired
	private AgendaEventService agendaEventService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {
	    binder.registerCustomEditor(LocalDate.class, new LocalDateEditor());
	}
	
	@GetMapping
	public PagedResponse<Day> getAll(
			@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "from", defaultValue = "today") LocalDate from,
			@RequestParam(value = "to", defaultValue = "endOfLife") LocalDate to,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size
		){
		return agendaEventService.getAllEvents(currentUser.getId(), from, to, page, size);
	}
	
	@GetMapping("/{eventId}")
    public AgendaEventSummary getEventById(
    		@CurrentUser UserPrincipal currentUser, 
    		@PathVariable Long eventId
		) {
        return new AgendaEventSummary(
        		eventRepository.findById(eventId)
        		.orElseThrow(() -> new ResourceNotFoundException("Agenda Event", "Id", eventId))
    		);
    }
}
