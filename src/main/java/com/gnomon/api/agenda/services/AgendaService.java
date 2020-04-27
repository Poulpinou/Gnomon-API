package com.gnomon.api.agenda.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.data.jpa.domain.Specification.*;
import org.springframework.stereotype.Service;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.agenda.payloads.requests.AgendaRequest;
import com.gnomon.api.agenda.payloads.responses.AgendaSummary;
import com.gnomon.api.agenda.repositories.AgendaConnectionRepository;
import com.gnomon.api.agenda.repositories.AgendaRepository;

import static com.gnomon.api.agenda.utils.specs.AgendaSpecifications.*;
import com.gnomon.api.exceptions.BadRequestException;
import com.gnomon.api.exceptions.NotAllowedException;
import com.gnomon.api.exceptions.ResourceNotFoundException;
import com.gnomon.api.models.User;
import com.gnomon.api.repositories.UserRepository;

@Service
public class AgendaService {

	private AgendaRepository agendaRepository;
	
	private AgendaConnectionRepository connectionRepository;
	
	private UserRepository userRepository;
	
	private AgendaConnectionService connectionService;
	
	@Autowired
	public AgendaService(
			AgendaRepository agendaRepository,
			AgendaConnectionRepository connectionRepository,
			UserRepository userRepository,
			AgendaConnectionService connectionService
		){
		this.agendaRepository = agendaRepository;
		this.connectionRepository = connectionRepository;
		this.userRepository = userRepository;
		this.connectionService = connectionService;
	}
	
	public List<AgendaSummary> getAllAgendasByUserId(Long userId){
		final User user = userRepository.getOne(userId);
		final List<Agenda> agendas = agendaRepository.findAll(where(isVisibleByUser(user)));
		
		return agendas.stream()
				.map(AgendaSummary::new)
				.collect(Collectors.toList());
	}
	
	public AgendaSummary getMainAgenda(Long userId) {
		final User user = userRepository.getOne(userId);
		final Agenda agenda = agendaRepository.findOne(where(byUser(user)).and(byConnectionType(AgendaConnectionType.MAIN)))
				.orElseThrow(() -> new ResourceNotFoundException("Main Agenda", "user", user.getName()));
		
		return new AgendaSummary(agenda);
	}
	
	public AgendaSummary getAgendaById(Long agendaId, Long userId) throws ResourceNotFoundException, NotAllowedException {
		final AgendaConnection connection = connectionRepository.findByUserIdAndAgendaId(userId, agendaId)
				.orElseThrow(() -> new ResourceNotFoundException("Agenda", "agendaId", agendaId));
		
		final Agenda agenda = connection.getAgenda();
		
		if(!agenda.isShared() && connection.getConnectionType() == AgendaConnectionType.VIEWER)
			throw new NotAllowedException("target Agenda is private");		
		
		return new AgendaSummary(agenda);
	}
	
	public void createAgenda(Long userId, AgendaRequest request) throws BadRequestException {
		Agenda agenda = new Agenda(
        	request.getName(),
        	request.getDescription(),
        	request.isShared()
		);
		
		agenda = agendaRepository.save(agenda);
		
		connectionService.createConnection(userId, agenda, AgendaConnectionType.OWNER);
	}
	
	public void updateAgenda(Long userId, Long agendaId, AgendaRequest request) throws ResourceNotFoundException, NotAllowedException {
		final AgendaConnection connection = connectionRepository.findByUserIdAndAgendaId(userId, agendaId)
				.orElseThrow( () -> new ResourceNotFoundException("AgendaConnection", "userId | agendaId", userId + " | " + agendaId));
		
		if(connection.getConnectionType() == AgendaConnectionType.VIEWER)
			throw new NotAllowedException("user doesn't own this agenda");
		
		final Agenda agenda = connection.getAgenda();
		agenda.setName(request.getName());
		agenda.setDescription(request.getDescription());
		agenda.setShared(request.isShared());
			
		agendaRepository.save(agenda);
	}
	
	public void deleteAgenda(Long userId, Long agendaId) throws ResourceNotFoundException, NotAllowedException{		
		final AgendaConnection connection = connectionRepository.findByUserIdAndAgendaId(userId, agendaId)
				.orElseThrow( () -> new ResourceNotFoundException("AgendaConnection", "userId | agendaId", userId + " | " + agendaId));
		
		if(connection.getConnectionType() != AgendaConnectionType.OWNER) {
			if(connection.getConnectionType() == AgendaConnectionType.MAIN)
				throw new NotAllowedException("impossible to delete a main agenda");
			else
				throw new NotAllowedException("user doesn't own this agenda");
		}
		
		agendaRepository.delete(connection.getAgenda());
	}
	
	public Agenda createMainAgendaForUser(User user) throws BadRequestException {		
		Agenda agenda = new Agenda(
        	"Mon agenda",
        	"Agenda personnel de " + user.getName(),
        	false
		);
		
		//User is not connected yet, so we have to force those fields
		agenda.setCreatedBy(user.getId());
		agenda.setUpdatedBy(user.getId());
        
        agenda = agendaRepository.save(agenda);
        
        connectionService.createConnection(user.getId(), agenda, AgendaConnectionType.MAIN);
        
        return agenda;
	}
}
