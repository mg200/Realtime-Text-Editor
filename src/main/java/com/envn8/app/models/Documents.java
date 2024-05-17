package com.envn8.app.models;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Document
public class Documents {
    @Id
    private String id;
    @Field
    private String title;
    @Field
    private List<CHAR> content;
    @Field
    private String type;

    @DBRef
    // @JsonIgnore
    @JsonBackReference
    private User owner;

    @DBRef
    // @JsonManagedReference
    @JsonBackReference // this worked
    private List<User> sharedWith; // This represents the users that the document is shared with

    private Map<String, DocPermissions> permissions;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Documents documents = (Documents) o;
        return Objects.equals(id, documents.id); // compare only by id
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
