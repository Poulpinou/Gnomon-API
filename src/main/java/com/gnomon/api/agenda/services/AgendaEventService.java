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

import com.gnomon.api.agenda.models.AgendaEvent;
import com.gnomon.api.agenda.payloads.responses.Day;
import com.gnomon.api.agenda.payloads.responses.EventSummary;
import com.gnomon.api.agenda.repositories.AgendaConnectionRepository;
import com.gnomon.api.agenda.repositories.AgendaEventRepository;
import com.gnomon.api.payloads.responses.PagedResponse;

@Service
public class AgendaEventService {
	
	@Autowired
	AgendaEventRepository eventRepository;
	
	@Autowired
	private AgendaConnectionRepository connectionRepository;
	
	public PagedResponse<Day> getAllEvents(Long userId, LocalDate from, LocalDate to, int page, int size){
		List<Long> agendaIds = connectionRepository.findAgendaIdsByUserId(userId);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "date");
				
		Page<AgendaEvent> events = eventRepository.getEventsFromAgendasBetweenDates(agendaIds, from.atStartOfDay(), to.atStartOfDay().plusDays(1), pageable);
		
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
			.collect(Collectors.groupingBy(AgendaEvent::getDate))
			.forEach((date, eventList) -> {
				days.add(
					new Day(
						date, 
						eventList.stream().map(event -> {
							return new EventSummary(event);
						}).collect(Collectors.toList())
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
