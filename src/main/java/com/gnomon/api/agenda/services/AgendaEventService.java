package com.gnomon.api.agenda.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.agenda.models.AgendaEvent;
import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.agenda.payloads.responses.Day;
import com.gnomon.api.agenda.payloads.requests.AgendaEventRequest;
import com.gnomon.api.agenda.payloads.responses.AgendaEventSummary;
import com.gnomon.api.agenda.repositories.AgendaConnectionRepository;
import com.gnomon.api.agenda.repositories.AgendaEventRepository;
import com.gnomon.api.agenda.repositories.AgendaRepository;
import com.gnomon.api.exceptions.NotAllowedException;
import com.gnomon.api.exceptions.ResourceNotFoundException;
import com.gnomon.api.models.User;
import com.gnomon.api.payloads.responses.PagedResponse;
import com.gnomon.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import static org.springframework.data.jpa.domain.Specification.*;
import static com.gnomon.api.agenda.utils.specs.AgendaEventSpecifications.*;
import static com.gnomon.api.agenda.utils.specs.AgendaSpecifications.isVisibleByUser;

@Service
@RequiredArgsConstructor
public class AgendaEventService {
		
	private final AgendaEventRepository eventRepository;
	
	private final UserRepository userRepository;
	
	private final AgendaRepository agendaRepository;
	
	private final AgendaConnectionRepository connectionRepository;
	
	public PagedResponse<?> getAllEvents(Long userId, LocalDate from, LocalDate to, int page, int size){
		final Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "date");
		final User user = userRepository.getOne(userId);
		final List<Agenda> agendas = agendaRepository.findAll(where(isVisibleByUser(user)));
		final Page<AgendaEvent> events = eventRepository.findAll(
				where(byAgendas(agendas)
						.and(isBetweenDates(from.atStartOfDay(), to.atStartOfDay().plusDays(1)))
				),pageable
			);
		
		if(events.getNumberOfElements() == 0) {
			return PagedResponse.emptyFromPage(events);
        }
		
		final List<Day> days = new ArrayList<Day>();
		
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
	
	public AgendaEventSummary getEventById(Long userId, Long eventId) {		
		//TODO check if user is allowed to see the event
		//final User user = userRepository.getOne(userId);
		
		final AgendaEvent event = eventRepository.getOne(eventId);
		
		return new AgendaEventSummary(event);
	}
	
	public void createEvent(Long userId, AgendaEventRequest request) {
		//TODO check if user is allowed to create the event
		//final User user = userRepository.getOne(userId);
		
		final AgendaEvent event = new AgendaEvent(
			request.getTitle(),
			request.getDescription(),
			request.getDate()
		);
		
		final List<Agenda> agendas = agendaRepository.findByIdIn(request.getAgendaIds());
		
		event.setAgendas(new HashSet<Agenda>(agendas));
		
		eventRepository.save(event);
	}
	
	public void updateEvent(Long userId, Long eventId, AgendaEventRequest request) {
		final AgendaEvent event = eventRepository.getOne(eventId);
		
		if(event.getCreatedBy() != userId) {
			throw new NotAllowedException("user can't update this event");
		}
		
		event.setTitle(request.getTitle());
		event.setDescription(request.getDescription());
		event.setDate(request.getDate());
		
		final List<Agenda> agendas = agendaRepository.findByIdIn(request.getAgendaIds());
		
		event.setAgendas(new HashSet<Agenda>(agendas));
		
		eventRepository.save(event);
	}
	
	public void deleteEvent(Long userId, Long eventId) {
		final AgendaEvent event = eventRepository.getOne(eventId);
		
		if(event.getCreatedBy() != userId) {
			throw new NotAllowedException("user can't delete this event");
		}
		
		eventRepository.delete(event);
	}
	
	public void connectEventToAgenda(Long userId, Long agendaId, Long eventId) {
		final AgendaConnection connection = connectionRepository.findByUserIdAndAgendaId(userId, agendaId)
				.orElseThrow(() -> new ResourceNotFoundException("AgendaConnection", "userId | agendaId", userId + " | " + agendaId));
		
		if(connection.getConnectionType() == AgendaConnectionType.VIEWER) {
			throw new NotAllowedException("user can't edit this agenda");
		}
		
		final AgendaEvent event = eventRepository.getOne(eventId);
		final Set<Agenda> agendas = event.getAgendas();
		
		agendas.add(connection.getAgenda());		
		event.setAgendas(agendas);
		
		eventRepository.save(event);
	}
	
	public void disconnectEventFromAgenda(Long userId, Long agendaId, Long eventId) {
		final AgendaConnection connection = connectionRepository.findByUserIdAndAgendaId(userId, agendaId)
				.orElseThrow(() -> new ResourceNotFoundException("AgendaConnection", "userId | agendaId", userId + " | " + agendaId));
		
		if(connection.getConnectionType() == AgendaConnectionType.VIEWER) {
			throw new NotAllowedException("user can't edit this agenda");
		}
		
		final AgendaEvent event = eventRepository.getOne(eventId);
		
		event.setAgendas(
				event.getAgendas()
				.stream()
				.filter((agenda) -> agenda.getId() != agendaId)
				.collect(Collectors.toSet())
			);
		
		eventRepository.save(event);
	}
	
}
