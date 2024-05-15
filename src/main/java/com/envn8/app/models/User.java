package com.envn8.app.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToMany;
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

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Builder
@NoArgsConstructor // might need to be removed
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
    // note as of 27/4, I initialized these lists to avoid null pointer exceptions,
    // might need to be removed
    @DBRef
    private List<Documents> documents = new ArrayList<>(); // This represents the documents that the user owns

    @DBRef
    @JsonManagedReference
    private List<Documents> sharedDocuments = new ArrayList<>(); // This represents the documents that are shared with
                                                                 // the user

    @DBRef
    private List<Token> tokens;

    @Override
    public String getUsername() {
        return username;
    }

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
    // return username;
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
        // System.out.println("Role: " + role+"exception here ");
        // System.out.println("Role: " + role.name());
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(id, user.id); // compare only by id
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
