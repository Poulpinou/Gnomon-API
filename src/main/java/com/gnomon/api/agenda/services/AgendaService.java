package com.gnomon.api.agenda.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.agenda.payloads.responses.AgendaSummary;
import com.gnomon.api.agenda.repositories.AgendaConnectionRepository;
import com.gnomon.api.agenda.repositories.AgendaRepository;
import com.gnomon.api.exceptions.BadRequestException;
import com.gnomon.api.models.User;
import com.gnomon.api.security.UserPrincipal;

@Service
public class AgendaService {
	@Autowired
	private AgendaRepository agendaRepository;
	
	@Autowired
	private AgendaConnectionRepository connectionRepository;
	
	public List<AgendaSummary> getAllAgendasForUser(UserPrincipal user){
		List<Long> agendaIds = connectionRepository.findAgendaIdsByUserId(user.getId());
		List<Agenda> agendas = agendaRepository.findByIdIn(agendaIds);
		
		List<AgendaSummary> agendaSummaries = agendas.stream().map(agenda -> {
			return new AgendaSummary(agenda);
		}).collect(Collectors.toList());
		
		return agendaSummaries;
	}
	
	public AgendaConnection connectUserToAgenda(User user, Agenda agenda, AgendaConnectionType connectionType) {
		
		if(connectionRepository.existsByUserIdAndAgendaId(user.getId(), agenda.getId())) {
			throw new BadRequestException(user.getName() + " is already connected to " + agenda.getName());
		}
		
		if(connectionType == AgendaConnectionType.MAIN) {
			if(connectionRepository.existsByUserIdAndConnectionType(user.getId(), connectionType)) {
				throw new BadRequestException(user.getName() + "already have a main agenda");
			}
		}
		
		AgendaConnection connection = new AgendaConnection(
			user,
			agenda,
			connectionType,
			true
		);
		
		return connectionRepository.save(connection);
	}
	
	public Agenda createMainAgendaForUser(User user) {
		
		Agenda agenda = new Agenda(
        	"Mon agenda",
        	"Agenda personnel de " + user.getName(),
        	false
		);
		
		agenda.setCreatedBy(user.getId());
		agenda.setUpdatedBy(user.getId());
        
        agenda = agendaRepository.save(agenda);
        
        connectUserToAgenda(user, agenda, AgendaConnectionType.MAIN);
        
        return agenda;
	}
}
