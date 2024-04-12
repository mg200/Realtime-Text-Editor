package com.envn8.app.payload.request;
import jakarta.validation.constraints.*;
public class SignUpRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
  
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
  
  
    @NotBlank
    @Size(min = 8, max = 40)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$")
    private String password;


  @NotBlank
  String firstName;
  @NotBlank
  String lastName;

    public String getUsername() {
        return username;
      }
    
      public void setUsername(String username) {
        this.username = username;
      }
    
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

      public String getFirstName(){
        return firstName;
      }

      public void setFirstName(String firstName){
        this.firstName = firstName;
      }
      public String getLastName(){
        return lastName;
      }
      public void setLastName(String lastName){
        this.lastName = lastName;
      }
    }
