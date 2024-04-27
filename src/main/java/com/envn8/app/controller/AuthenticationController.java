package com.envn8.app.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.envn8.app.payload.request.AuthenticationRequest;
import com.envn8.app.payload.request.RegisterRequest;
import com.envn8.app.payload.response.AuthenticationResponse;
import com.envn8.app.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/signup")
  public ResponseEntity<AuthenticationResponse> signup(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(authenticationService.register(request));
  }
  @PostMapping("/signin")
  public ResponseEntity<AuthenticationResponse> signin(
      @RequestBody AuthenticationRequest request
  ) {
        System.out.println("inside authenticate"+request);
    return ResponseEntity.ok(authenticationService.authenticate(request));
  }
}