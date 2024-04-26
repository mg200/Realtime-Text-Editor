package com.envn8.app.models;

import java.util.Collection;
// import java.util.HashSet;
import java.util.List;
// import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Data
@Builder
@NoArgsConstructor //might need to be removed 
@AllArgsConstructor
@Entity
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private String id;
    @Field
    private String firstName;
    @Field
    private String lastName;
    @Field
    private String username;

    @Field
    @NotBlank
    @Size(min = 8, max = 40)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$")
    private String password;

    @Field
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;
   
    @DBRef
    private List<Documents> documents; // This represents the documents that the user owns
    private List<Documents> sharedDocuments; // This represents the documents that are shared with the user

    // public User() {
        // this.documents = new java.util.ArrayList<Documents>();
        // this.sharedDocuments = new java.util.ArrayList<Documents>();
    // }

    // public User(String username, String password) {
    //     // this.username = username;
        
    //     this.password = password;
    //     this.documents = new java.util.ArrayList<Documents>();
    //     this.sharedDocuments = new java.util.ArrayList<Documents>();
    // }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // public String getUsername() {
    //     return username;
    // }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Documents> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Documents> documents) {
        this.documents = documents;
    }

    public List<Documents> getSharedDocuments() {
        return sharedDocuments;
    }

    public void setSharedDocuments(List<Documents> sharedDocuments) {
        this.sharedDocuments = sharedDocuments;
    }

    @Override
    public String toString() {
        return String.format("User[id='%s',firstname='%s',lastName='%s', password='%s']", id, firstName, lastName,
                password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("Role: " + role+"exception here ");
        System.out.println("Role: " + role.name());
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return username;
        // throw new UnsupportedOperationException("Unimplemented method 'getUsername'");
    }

}
