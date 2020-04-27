package com.gnomon.api.agenda.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.agenda.payloads.requests.AgendaRequest;
import com.gnomon.api.agenda.repositories.AgendaConnectionRepository;
import com.gnomon.api.agenda.repositories.AgendaRepository;
import com.gnomon.api.agenda.services.AgendaConnectionService;
import com.gnomon.api.agenda.services.AgendaService;
import com.gnomon.api.repositories.UserRepository;

import static org.assertj.core.api.Assertions.*;
import static com.gnomon.api.agenda.asserts.AgendaAssertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalAnswers.*;

@ExtendWith(MockitoExtension.class)
public class AgendaServiceTest {
	
	@Mock
	private AgendaRepository agendaRepository;
	
	@Mock
	private AgendaConnectionRepository connectionRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private AgendaConnectionService connectionService;
	
	@InjectMocks
	private AgendaService agendaService;
	
	@Test
	public void createAgenda(){
		//given
		AgendaRequest request = new AgendaRequest();
		request.setName("New Agenda");
		request.setDescription("An awsome agenda");
		request.setShared(true);
		
		//when
		when(agendaRepository.save(any(Agenda.class))).then(returnsFirstArg());
		Agenda agenda = agendaService.createAgenda(1L, request);
		
		//then
		assertThat(agenda).isFetchingToRequest(request);
	}
}
