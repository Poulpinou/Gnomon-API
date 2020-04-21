package com.gnomon.api.agenda.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.agenda.payloads.requests.AgendaRequest;
import com.gnomon.api.agenda.payloads.responses.AgendaSummary;
import com.gnomon.api.agenda.repositories.AgendaConnectionRepository;
import com.gnomon.api.agenda.repositories.AgendaRepository;
import com.gnomon.api.exceptions.BadRequestException;
import com.gnomon.api.exceptions.NotAllowedException;
import com.gnomon.api.exceptions.ResourceNotFoundException;
import com.gnomon.api.models.User;

@Service
public class AgendaService {
	@Autowired
	private AgendaRepository agendaRepository;
	
	@Autowired
	private AgendaConnectionRepository connectionRepository;
	
	public List<AgendaSummary> getAllAgendaSummariesByUserId(Long userId){
		List<AgendaSummary> agendaSummaries = getAllAgendasByUserId(userId).stream().map(agenda -> {
			return new AgendaSummary(agenda);
		}).collect(Collectors.toList());
		
		return agendaSummaries;
	}
	
	public AgendaSummary getAgendaSummaryById(Long agendaId, Long userId) throws ResourceNotFoundException, NotAllowedException {
		return new AgendaSummary(getAgendaById(agendaId, userId));
	}
	
	public List<Agenda> getAllAgendasByUserId(Long userId){
		List<Long> agendaIds = connectionRepository.findAgendaIdsByUserId(userId);
		List<Agenda> agendas = agendaRepository.findByIdIn(agendaIds);
		return agendas;
	}
	
	public Agenda getAgendaById(Long agendaId, Long userId) throws ResourceNotFoundException, NotAllowedException {
		AgendaConnection connection = connectionRepository.findByUserIdAndAgendaId(userId, agendaId)
				.orElseThrow(() -> new ResourceNotFoundException("Agenda", "agendaId", agendaId));
		
		Agenda agenda = connection.getAgenda();
		
		if(!agenda.isPublic() && connection.getConnectionType() == AgendaConnectionType.VIEWER) {
			throw new NotAllowedException("target Agenda is private");
		}
		
		return agenda;
	}
	
	public AgendaConnection connectUserToAgenda(User user, Agenda agenda, AgendaConnectionType connectionType) throws BadRequestException {
		
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
	
	public Agenda createAgenda(User user, AgendaRequest request) throws BadRequestException {
		Agenda agenda = new Agenda(
        	request.getName(),
        	request.getDescription(),
        	request.isPublic()
		);
		
		agenda = agendaRepository.save(agenda);
		
		connectUserToAgenda(user, agenda, AgendaConnectionType.OWNER);
		
		return agenda;
	}
	
	public Agenda updateAgenda(Long userId, Long agendaId, AgendaRequest request) throws ResourceNotFoundException, NotAllowedException {
		AgendaConnection connection = connectionRepository.findByUserIdAndAgendaId(userId, agendaId)
				.orElseThrow( () -> new ResourceNotFoundException("AgendaConnection", "userId | agendaId", userId + " | " + agendaId));
		
		if(connection.getConnectionType() == AgendaConnectionType.VIEWER) {
			throw new NotAllowedException("user doesn't own this agenda");
		}
		
		Agenda agenda = connection.getAgenda();
		agenda.setName(request.getName());
		agenda.setDescription(request.getDescription());
		agenda.setPublic(request.isPublic());
			
		return agendaRepository.save(agenda);
	}
	
	public void deleteAgenda(Long userId, Long agendaId) throws ResourceNotFoundException, NotAllowedException{		
		AgendaConnection connection = connectionRepository.findByUserIdAndAgendaId(userId, agendaId)
				.orElseThrow( () -> new ResourceNotFoundException("AgendaConnection", "userId | agendaId", userId + " | " + agendaId));
		
		if(connection.getConnectionType() != AgendaConnectionType.OWNER) {
			if(connection.getConnectionType() == AgendaConnectionType.MAIN) {
				throw new NotAllowedException("impossible to delete a main agenda");
			}
			else{
				throw new NotAllowedException("user doesn't own this agenda");
			}
			
		}
		
		Agenda agenda = connection.getAgenda();
		
		agendaRepository.delete(agenda);
	}
	
	public Agenda createMainAgendaForUser(User user) throws BadRequestException {
		
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
