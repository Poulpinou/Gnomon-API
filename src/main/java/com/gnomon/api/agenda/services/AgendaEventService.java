package com.gnomon.api.agenda.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.agenda.models.AgendaEvent;
import com.gnomon.api.agenda.payloads.responses.Day;
import com.gnomon.api.agenda.payloads.responses.AgendaEventSummary;
import com.gnomon.api.agenda.repositories.AgendaEventRepository;
import com.gnomon.api.agenda.repositories.AgendaRepository;
import com.gnomon.api.models.User;
import com.gnomon.api.payloads.responses.PagedResponse;
import com.gnomon.api.repositories.UserRepository;

import static org.springframework.data.jpa.domain.Specification.*;
import static com.gnomon.api.agenda.utils.specs.AgendaEventSpecifications.*;
import static com.gnomon.api.agenda.utils.specs.AgendaSpecifications.isVisibleByUser;

@Service
public class AgendaEventService {
		
	private AgendaEventRepository eventRepository;
	
	private UserRepository userRepository;
	
	private AgendaRepository agendaRepository;
	
	@Autowired
	public AgendaEventService(
			AgendaEventRepository eventRepository, 
			UserRepository userRepository,
			AgendaRepository agendaRepository
	) {
		this.eventRepository = eventRepository;
		this.userRepository = userRepository;
		this.agendaRepository = agendaRepository;
	}
	
	public PagedResponse<Day> getAllEvents(Long userId, LocalDate from, LocalDate to, int page, int size){
		final Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "date");
		final User user = userRepository.getOne(userId);
		final List<Agenda> agendas = agendaRepository.findAll(where(isVisibleByUser(user)));
		final Page<AgendaEvent> events = eventRepository.findAll(where(byAgendas(agendas)), pageable);
		
		if(events.getNumberOfElements() == 0) {
            return new PagedResponse<Day>(
        		Collections.emptyList(), 
        		events.getNumber(),
        		events.getSize(), 
        		events.getTotalElements(), 
        		events.getTotalPages(), 
        		events.isLast()
    		);
        }
		
		List<Day> days = new ArrayList<Day>();
		
		events.stream()
			.collect(Collectors.groupingBy(AgendaEvent::getDay))
			.forEach((date, eventList) -> {
				days.add(
					new Day(
						date, 
						eventList.stream()
							.map(AgendaEventSummary::new)
							.collect(Collectors.toList())
					)
				);
			});
		
		Collections.sort(days);
		
		return new PagedResponse<Day>(
			days,
			events.getNumber(),
    		events.getSize(), 
    		events.getTotalElements(), 
    		events.getTotalPages(), 
    		events.isLast()
		);
	}
}
