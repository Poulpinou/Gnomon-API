package com.gnomon.api.agenda.services;

import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.agenda.models.enums.AgendaConnectionType;
import com.gnomon.api.agenda.payloads.responses.AgendaConnectionSummary;
import com.gnomon.api.agenda.repositories.AgendaConnectionRepository;
import com.gnomon.api.agenda.utils.specs.AgendaConnectionSpecifications;
import com.gnomon.api.exceptions.BadRequestException;
import com.gnomon.api.models.User;
import com.gnomon.api.repositories.UserRepository;

@Service
public class AgendaConnectionService {
	
	private AgendaConnectionRepository connectionRepository;
	
	private UserRepository userRepository;
	
	@Autowired
	public AgendaConnectionService(AgendaConnectionRepository connectionRepository, UserRepository userRepository) {
		this.connectionRepository = connectionRepository;
		this.userRepository = userRepository;
	}
	
	public List<AgendaConnectionSummary> getAllConnectionsByUserId(Long userId){
		final User user = userRepository.getOne(userId);
		final List<AgendaConnection> connections = connectionRepository.findAll(
					where(AgendaConnectionSpecifications.byUser(user))
					.and(
						AgendaConnectionSpecifications.connectionIsNot(AgendaConnectionType.VIEWER)
						.or(AgendaConnectionSpecifications.agendaIsShared())
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
}
