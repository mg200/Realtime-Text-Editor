package com.envn8.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.envn8.app.models.Role;
import com.envn8.app.models.User;
import com.envn8.app.payload.request.AuthenticationRequest;
import com.envn8.app.payload.request.RegisterRequest;
import com.envn8.app.payload.response.AuthenticationResponse;
import com.envn8.app.repository.UserRepository;
import com.envn8.app.security.config.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    // public AuthenticationResponse authenticate(AuthenticationRequest request) {
    //     System.out.println("inside authenticate email "+request);
    //     authenticationManager.authenticate(
    //             new UsernamePasswordAuthenticationToken(
    //                     request.getEmail(),
    //                     request.getPassword()));
    //     var user = repository.findByEmail(request.getEmail())
    //             .orElseThrow();
    //     System.out.println("inside authenticate before var"+request.getEmail()+"\n");
    //     var jwtToken = jwtService.generateToken(user);
    //     System.out.println("JWT Token: " + jwtToken);
    //     return AuthenticationResponse.builder()
    //             .token(jwtToken)
    //             .build();
    // }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        System.out.println("before authenticationManager in username"+request); 
        System.out.println(""+request.getUsername()+"password "+request.getPassword());   
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        System.out.println("inside authenticate before var"+request.getUsername()+"\n");
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        System.out.println("JWT Token: " + jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}