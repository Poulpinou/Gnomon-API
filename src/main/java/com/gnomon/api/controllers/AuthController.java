package com.gnomon.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gnomon.api.models.User;
import com.gnomon.api.payloads.requests.LoginRequest;
import com.gnomon.api.payloads.requests.SignUpRequest;
import com.gnomon.api.payloads.responses.ApiResponse;
import com.gnomon.api.payloads.responses.JwtAuthenticationResponse;
import com.gnomon.api.repositories.UserRepository;
import com.gnomon.api.security.JwtTokenProvider;
import com.gnomon.api.services.UserService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthenticationManager authenticationManager;

    UserRepository userRepository;
    
    UserService userService;

    JwtTokenProvider tokenProvider;
    
    @Autowired
    public AuthController(
    		AuthenticationManager authenticationManager,
    		UserRepository userRepository,
    		UserService userService,
    		JwtTokenProvider tokenProvider
		) {
    	this.authenticationManager = authenticationManager;
    	this.userRepository = userRepository;
    	this.userService = userService;
    	this.tokenProvider = tokenProvider;
	}

    @PostMapping("/signin")
    @Transactional
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				loginRequest.getNameOrEmail(), 
				loginRequest.getPassword()
			)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByName(signUpRequest.getName())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"), HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
        }

        User user = userService.createAccount(signUpRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(user.getName())
                .toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
