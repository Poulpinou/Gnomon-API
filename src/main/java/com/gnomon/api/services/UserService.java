package com.gnomon.api.services;

import java.util.Collections;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gnomon.api.agenda.services.AgendaService;
import com.gnomon.api.exceptions.AppException;
import com.gnomon.api.models.Role;
import com.gnomon.api.models.User;
import com.gnomon.api.models.enums.RoleName;
import com.gnomon.api.payloads.requests.SignUpRequest;
import com.gnomon.api.repositories.RoleRepository;
import com.gnomon.api.repositories.UserRepository;

@Service
public class UserService {
	
	private UserRepository userRepository;	
	
	private RoleRepository roleRepository;
	
	private AgendaService agendaService;
	
	private PasswordEncoder passwordEncoder;
	
	public UserService(
			UserRepository userRepository, 
			RoleRepository roleRepository,
			AgendaService agendaService,
			PasswordEncoder passwordEncoder
		) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.agendaService = agendaService;
		this.passwordEncoder = passwordEncoder;
	}
	
	public User createAccount(SignUpRequest signUpRequest) {
		User user = new User(
    		signUpRequest.getName(),
            signUpRequest.getEmail(), 
            signUpRequest.getPassword()
        );
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new AppException("Doesn't exist"));

        user.setRoles(Collections.singleton(userRole));
        
        user = userRepository.save(user);
        
        agendaService.createMainAgendaForUser(user);
		
		return user;
	}
}
