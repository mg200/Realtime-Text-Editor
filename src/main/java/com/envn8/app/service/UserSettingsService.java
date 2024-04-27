package com.envn8.app.service;

import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.envn8.app.models.User;
import com.envn8.app.payload.request.PasswordChangeRequest;
import com.envn8.app.repository.UserRepository;
import com.envn8.app.security.config.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSettingsService {
    private final UserService userservice;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public Object changePassword(PasswordChangeRequest passwordChangeRequest,
            String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token is null or empty");
        }
        if (passwordChangeRequest.getNewPassword().isEmpty() || passwordChangeRequest.getOldPassword().isEmpty()
                || passwordChangeRequest.getNewPassword2().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is empty");
        }
        if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getNewPassword2())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        // Load the user details
        UserDetails userDetails = userservice.getUserByUsername(username);
        if (userDetails == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = (User) userDetails;
        if (user.getPassword().equals(passwordChangeRequest.getOldPassword())) {
            user.setPassword(passwordChangeRequest.getNewPassword());
            userRepository.save(user);
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.badRequest().body("Old password is incorrect");
        }

    }

    // public Object changePassword(PasswordChangeRequest passwordChangeRequest) {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'changePassword'");
    // }
}
