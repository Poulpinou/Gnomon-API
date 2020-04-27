package com.gnomon.api.agenda.services;

import static org.springframework.data.jpa.domain.Specification.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.agenda.payloads.requests.AgendaConnectionRequest;
import com.gnomon.api.agenda.payloads.responses.AgendaConnectionSummary;
import com.gnomon.api.agenda.repositories.AgendaConnectionRepository;
import com.gnomon.api.agenda.repositories.AgendaRepository;
import com.gnomon.api.exceptions.BadRequestException;
import com.gnomon.api.exceptions.NotAllowedException;
import com.gnomon.api.exceptions.ResourceNotFoundException;
import com.gnomon.api.models.User;
import com.gnomon.api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import static com.gnomon.api.agenda.utils.specs.AgendaConnectionSpecifications.*;

@Service
@RequiredArgsConstructor
public class AgendaConnectionService {
	
	private final AgendaConnectionRepository connectionRepository;
	
	private final UserRepository userRepository;
	
	private final AgendaRepository agendaRepository;
	
	public List<AgendaConnectionSummary> getAllConnectionsByUserId(Long userId){
		final User user = userRepository.getOne(userId);
		final List<AgendaConnection> connections = connectionRepository.findAll(
					where(byUser(user))
					.and(
						connectionIsNot(AgendaConnectionType.VIEWER)
						.or(agendaIsShared())
					)
				);
		
		return connections.stream()
				.map(AgendaConnectionSummary::new)
				.collect(Collectors.toList());
	}
	
	public void createConnection(Long userId, Agenda agenda, AgendaConnectionType connectionType) throws BadRequestException {
		final User user = userRepository.getOne(userId);
		
		if(connectionRepository.existsByUserIdAndAgendaId(userId, agenda.getId())) {
			throw new BadRequestException(user.getName() + " is already connected to " + agenda.getName());
		}
		
		if(connectionType == AgendaConnectionType.MAIN) {
			if(connectionRepository.existsByUserIdAndConnectionType(user.getId(), connectionType)) {
				throw new BadRequestException(user.getName() + "already have a main agenda");
			}
		}
		
		final AgendaConnection connection = new AgendaConnection(
			user,
			agenda,
			connectionType,
			true
		);
		
		connectionRepository.save(connection);
	}
	
	public void connectAgenda(Long userId, AgendaConnectionRequest request) {
		final User user = userRepository.getOne(request.getUserId());
		final Agenda agenda = agendaRepository.getOne(request.getAgendaId());
		
		final AgendaConnection connection = connectionRepository.findByUserIdAndAgendaId(request.getUserId(), request.getAgendaId())
			.orElse(
				new AgendaConnection(
					user, 
					agenda,
					AgendaConnectionType.VIEWER,
					request.isShown()
				) 
			);
		
		if(request.isOwner()) {
			final AgendaConnection userConnection = connectionRepository.findByUserIdAndAgendaId(userId, request.getAgendaId())
					.orElseThrow(() -> new NotAllowedException("User should be connected to the agenda to create new connections"));
		
			if(userConnection.getConnectionType() != AgendaConnectionType.OWNER) {
				throw new NotAllowedException("User should own the agenda to add an owner");
			}
			
			connection.setConnectionType(AgendaConnectionType.OWNER);
		}
		
		connectionRepository.save(connection);
	}
	
	public void disconnectAgenda(Long currentUserId, Long userId, Long agendaId) {
		final AgendaConnection connection = connectionRepository.findByUserIdAndAgendaId(userId, agendaId)
				.orElseThrow(() -> new ResourceNotFoundException("AgendaConnection", "userId | agendaId", userId + " | " + agendaId));
		
		if(connection.getConnectionType() == AgendaConnectionType.OWNER) {
			final AgendaConnection userConnection = connectionRepository.findByUserIdAndAgendaId(currentUserId, agendaId)
					.orElseThrow(() -> new NotAllowedException("User should be connected to the agenda to delete an owner connection"));
		
			if(userConnection.getConnectionType() != AgendaConnectionType.OWNER) {
				throw new NotAllowedException("User should own the agenda to delete an owner connection");
			}
			
			if(connectionRepository.count(where(byAgendaId(agendaId))) < 2) {
				throw new NotAllowedException("User is the last owner of this agenda, it can't be removed");
			}
		}
		
		connectionRepository.delete(connection);
	}
}
