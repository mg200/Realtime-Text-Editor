package com.envn8.app.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordChangeRequest {
    // private String username;
    
    private String oldPassword;
    
    private String newPassword;

    private String newPassword2;
}

