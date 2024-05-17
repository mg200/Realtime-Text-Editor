package com.envn8.app.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
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
    @Builder.Default
    private List<Documents> documents = new ArrayList<>(); // This represents the documents that the user owns

    @DBRef
    @JsonManagedReference
    @Builder.Default
    private List<Documents> sharedDocuments = new ArrayList<>(); // This represents the documents that are shared with
                                                                 // the user

    @DBRef
    private List<Token> tokens;

    @Override
    public String toString() {
        return String.format("User[id='%s',firstname='%s',lastName='%s', password='%s']", id, firstName, lastName,
                password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
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
