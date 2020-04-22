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
import com.gnomon.api.repositories.UserRepository;
import com.gnomon.api.security.UserPrincipal;
import com.gnomon.api.services.UserService;

@Service
public class AgendaService {

	private AgendaRepository agendaRepository;
	
	private AgendaConnectionRepository connectionRepository;
	
	private UserRepository userRepository;
	
	@Autowired
	public AgendaService(
			AgendaRepository agendaRepository,
			AgendaConnectionRepository connectionRepository,
			UserRepository userRepository
		){
		this.agendaRepository = agendaRepository;
		this.connectionRepository = connectionRepository;
		this.userRepository = userRepository;
	}
	
	public List<AgendaSummary> getAllAgendaSummariesByUserId(Long userId){
		List<Long> agendaIds = connectionRepository.findAgendaIdsByUserId(userId);
		List<Agenda> agendas = agendaRepository.findByIdIn(agendaIds);
		
		return agendas.stream()
				.map(AgendaSummary::new)
				.collect(Collectors.toList());
	}
	
	public AgendaSummary getMainAgenda(Long userId) {
		AgendaConnection connection = connectionRepository.findByUserIdAndConnectionType(userId, AgendaConnectionType.MAIN)
				.orElseThrow( () -> new ResourceNotFoundException("Main Agenda", "userId", userId)) ;

		return new AgendaSummary(connection.getAgenda());
	}
	
	public AgendaSummary getAgendaById(Long agendaId, Long userId) throws ResourceNotFoundException, NotAllowedException {
		AgendaConnection connection = connectionRepository.findByUserIdAndAgendaId(userId, agendaId)
				.orElseThrow(() -> new ResourceNotFoundException("Agenda", "agendaId", agendaId));
		
		Agenda agenda = connection.getAgenda();
		
		if(!agenda.isShared() && connection.getConnectionType() == AgendaConnectionType.VIEWER)
			throw new NotAllowedException("target Agenda is private");		
		
		return new AgendaSummary(agenda);
	}
	
	public AgendaConnection connectUserToAgenda(Long userId, Agenda agenda, AgendaConnectionType connectionType) throws BadRequestException {		
		User user = userRepository.getOne(userId);
		
		if(connectionRepository.existsByUserIdAndAgendaId(userId, agenda.getId())) {
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
	
	public void createAgenda(Long userId, AgendaRequest request) throws BadRequestException {
		Agenda agenda = new Agenda(
        	request.getName(),
        	request.getDescription(),
        	request.isShared()
		);
		
		agenda = agendaRepository.save(agenda);
		
		connectUserToAgenda(userId, agenda, AgendaConnectionType.OWNER);
	}
	
	public void updateAgenda(Long userId, Long agendaId, AgendaRequest request) throws ResourceNotFoundException, NotAllowedException {
		AgendaConnection connection = connectionRepository.findByUserIdAndAgendaId(userId, agendaId)
				.orElseThrow( () -> new ResourceNotFoundException("AgendaConnection", "userId | agendaId", userId + " | " + agendaId));
		
		if(connection.getConnectionType() == AgendaConnectionType.VIEWER)
			throw new NotAllowedException("user doesn't own this agenda");
		
		Agenda agenda = connection.getAgenda();
		agenda.setName(request.getName());
		agenda.setDescription(request.getDescription());
		agenda.setShared(request.isShared());
			
		agendaRepository.save(agenda);
	}
	
	public void deleteAgenda(Long userId, Long agendaId) throws ResourceNotFoundException, NotAllowedException{		
		AgendaConnection connection = connectionRepository.findByUserIdAndAgendaId(userId, agendaId)
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
        
        connectUserToAgenda(user.getId(), agenda, AgendaConnectionType.MAIN);
        
        return agenda;
	}
}
