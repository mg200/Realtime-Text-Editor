package com.envn8.app.payload.request;

import javax.print.Doc;

import com.envn8.app.models.DocPermissions;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//recall the existence of these annotations does the following:
// @Data: generates getters and setters for all fields, a useful toString method, and an equals and hashcode method
// @Builder: provides a builder for the object
// @AllArgsConstructor: generates a constructor with all arguments
// @NoArgsConstructor: generates a constructor with no arguments
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareDocumentRequest {
    private String username; // This represents the user that the document is shared with
    // private String permission; // This represents the permission of the shared
    // user
    @Enumerated(EnumType.STRING)
    private DocPermissions permission=DocPermissions.VIEWER;
}
