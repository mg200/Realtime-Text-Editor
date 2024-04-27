package com.envn8.app.service;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.envn8.app.repository.UserRepository;
import com.envn8.app.security.config.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSettingsService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final DocumentService documentService;
    private final AuthenticationManager authenticationManager;
}
