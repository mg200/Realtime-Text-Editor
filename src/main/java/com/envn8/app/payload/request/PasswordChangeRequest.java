package com.envn8.app.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public class PasswordChangeRequest {
    @NotBlank
    private String username;
  
    @NotBlank
    @JsonProperty("Oldpassword")
    private String Oldpassword;
    @JsonProperty("Newpassword")
    @NotBlank
    private String Newpassword;
    @JsonProperty("Newpassword2")
    @NotBlank
    private String Newpassword2;

    public String getUsername() {
      return username;
    }
    // public void setPassword(String password) {
        // this. = password;
    //   }
    public String getOldpassword() {
      return Oldpassword;
    }

    public void setOldpassword(String Oldpassword) {
      this.Oldpassword = Oldpassword;
    }

    public String getNewpassword() {
      return Newpassword;
    }

    public void setNewpassword(String Newpassword) {
      this.Newpassword = Newpassword;
    }

    public String getNewpassword2() {
      return Newpassword2;
    }

    public void setNewpassword2(String Newpassword2) {
      this.Newpassword2 = Newpassword2;
    }
}

