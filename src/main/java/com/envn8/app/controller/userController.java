package com.envn8.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class userController {
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("In login method");
        if ("user@example.com".equals(loginRequest.getEmail()) && "password".equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.OK).body("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }

        // Define a LoginRequest class to represent the request body
        static class LoginRequest {
            private String email;
            private String password;
    
            // Getters and setters
            public String getEmail() {
                return email;
            }
    
            public void setEmail(String email) {
                this.email = email;
            }
    
            public String getPassword() {
                return password;
            }
    
            public void setPassword(String password) {
                this.password = password;
            }
        }
    
}
