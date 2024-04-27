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

  private final AuthenticationService service;

  @PostMapping("/signup")
  public ResponseEntity<AuthenticationResponse> signup(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/signin")
  public ResponseEntity<AuthenticationResponse> signin(
      @RequestBody AuthenticationRequest request
  ) {
        System.out.println("inside authenticate"+request);
    return ResponseEntity.ok(service.authenticate(request));
  }

// TODO: to be removed to its own controller
//   @PostMapping("/change-password")
//         public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeRequest requestUtil) {
//                 Authentication authentication = authenticationManager.authenticate(
//                                 new UsernamePasswordAuthenticationToken(requestUtil.getUsername(),
//                                                 requestUtil.getOldpassword()));
//                 SecurityContextHolder.getContext().setAuthentication(authentication);
//                 UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//                 ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

//                 if (!requestUtil.getNewpassword().equals(requestUtil.getNewpassword2())) {
//                         return ResponseEntity.badRequest()
//                                         .body(new MessageResponse("Error: New passwords do not match!"));
//                 }

//                 if(requestUtil.getNewpassword().equals(requestUtil.getOldpassword())){
//                         return ResponseEntity.badRequest().body(new MessageResponse("Enter a new Password!"));
//                 }

//                 if (requestUtil.getNewpassword().length() < 8) {
//                         return ResponseEntity.badRequest()
//                                         .body(new MessageResponse(
//                                                         "Error: Password must be at least 8 characters long!"));
//                 }

//                 if (!requestUtil.getNewpassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$")) {
//                         return ResponseEntity.badRequest().body(new MessageResponse(
//                                         "Error: Password must contain at least one uppercase letter, one lowercase letter, and one number!"));
//                 }

//                 User user = userRepository.findByUsername(requestUtil.getUsername())
//                                 .orElseThrow(() -> new RuntimeException("User not found"));
//                 user.setPassword(encoder.encode(requestUtil.getNewpassword()));
//                 userRepository.save(user);
//                 return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
//                                 .body(new MessageResponse("Password changed successfully!"));
//         }


}