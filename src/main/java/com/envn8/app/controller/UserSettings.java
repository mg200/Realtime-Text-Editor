package com.envn8.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.envn8.app.payload.request.PasswordChangeRequest;
import com.envn8.app.service.UserSettingsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserSettings {
    private final UserSettingsService userSettingsService;

    @PostMapping("/changePassword")
    public ResponseEntity<?>changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest, 
    @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(userSettingsService.changePassword(passwordChangeRequest, token));
    }
}
