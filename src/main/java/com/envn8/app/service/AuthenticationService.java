package com.envn8.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.envn8.app.models.Role;
import com.envn8.app.models.Token;
import com.envn8.app.models.TokenType;
import com.envn8.app.models.User;
import com.envn8.app.payload.request.AuthenticationRequest;
import com.envn8.app.payload.request.RegisterRequest;
import com.envn8.app.payload.response.AuthenticationResponse;
import com.envn8.app.repository.TokenRepository;
import com.envn8.app.repository.UserRepository;
import com.envn8.app.security.config.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final UserRepository repository;
        private final TokenRepository tokenRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                // check if the username exists in the database, throw an exception if it does
                // and print it to the console
                if (repository.existsByUsername(request.getUsername())) {
                        // throw new RuntimeException("Username already exists");
                        return AuthenticationResponse.builder()
                                        .token("Username already exists")
                                        .build();
                }
                // check if the email exists in the
                if (repository.existsByEmail(request.getEmail())) {
                        // throw new RuntimeException("Email already exists");
                        return AuthenticationResponse.builder()
                                        .token("Email already exists")
                                        .build();
                }
                Role r=Role.USER;
                if (request.getRole() == "ADMIN") {
                        r = Role.ADMIN;
                }
                var user = User.builder()
                                .firstName(request.getFirstname())
                                .lastName(request.getLastname())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .username(request.getUsername())
                                .role(r)
                                .build();
                var savedUser = repository.save(user);
                var jwtToken = jwtService.generateToken(user);
                saveUserToken(savedUser, jwtToken);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                // System.out.println("before authenticationManager in username" + request);
                // System.out.println("" + request.getUsername() + "password " + request.getPassword());
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getUsername(),
                                                request.getPassword()));
                // System.out.println("inside authenticate before var" + request.getUsername() + "\n");
                var user = repository.findByUsername(request.getUsername())
                                .orElseThrow();
                var jwtToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, jwtToken);
                // System.out.println("JWT Token: " + jwtToken);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }

        private void saveUserToken(User savedUser, String jwtToken) {
                var token = Token.builder()
                                .user(savedUser)
                                .token(jwtToken)
                                .tokenType(TokenType.BEARER)
                                .revoked(false)
                                .expired(false)
                                .build();
                tokenRepository.save(token);
        }

        private void revokeAllUserTokens(User user) {
                var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
                if (validUserTokens.isEmpty())
                        return;
                validUserTokens.forEach(t -> {
                        t.setExpired(true);
                        t.setRevoked(true);
                });
                tokenRepository.saveAll(validUserTokens);

        }

        // public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // System.out.println("inside authenticate email "+request);
        // authenticationManager.authenticate(
        // new UsernamePasswordAuthenticationToken(
        // request.getEmail(),
        // request.getPassword()));
        // var user = repository.findByEmail(request.getEmail())
        // .orElseThrow();
        // System.out.println("inside authenticate before var"+request.getEmail()+"\n");
        // var jwtToken = jwtService.generateToken(user);
        // System.out.println("JWT Token: " + jwtToken);
        // return AuthenticationResponse.builder()
        // .token(jwtToken)
        // .build();
        // }
}