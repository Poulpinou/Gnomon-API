package com.gnomon.api.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gnomon.api.agenda.repositories.AgendaRepository;
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
	
	@Autowired
	UserRepository userRepository;	
	
	@Autowired
    RoleRepository roleRepository;
	
	@Autowired
	AgendaRepository agendaRepository;
	
	@Autowired
	AgendaService agendaService;
	
	@Autowired
    PasswordEncoder passwordEncoder;
	
	public User createAccount(SignUpRequest signUpRequest) {
		User user = new User(
    		signUpRequest.getName(),
            signUpRequest.getEmail(), 
            signUpRequest.getPassword()
        );
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));
        
        user = userRepository.save(user);
        
        agendaService.createMainAgendaForUser(user);
		
		return user;
	}
}
