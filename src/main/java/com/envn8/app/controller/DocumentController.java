package com.envn8.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.envn8.app.models.Documents;
import com.envn8.app.models.User;
import com.envn8.app.security.jwt.JwtUtils;
import com.envn8.app.service.DocumentService;
import com.envn8.app.service.userService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;

// import java.util.List;
// import java.util.Map;
import java.util.Optional;

@Controller
public class DocumentController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private userService userService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/documents")
    public ResponseEntity<Documents> createDocument(@RequestBody DocumentRequest documentRequest,
            @RequestHeader("userId") String userId) {
        User user = userService.getUserById(userId); // Get the user who is creating the document
        Documents document = new Documents();
        document.setTitle(documentRequest.getTitle());
        document.setContent(documentRequest.getContent());
        document.setOwner(user); // Set the owner of the document to the user
        Documents savedDocument = documentService.createDocument(document);
        return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);
    }

    // Open a document
    @GetMapping("/documents/{id}")
    public ResponseEntity<Documents> viewDocument(@PathVariable String id, @RequestHeader("userId") String userId) {
        Optional<Documents> documentOptional = documentService.getDocumentById(id);
        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            if (document.getType().equals("public") || document.getOwner().getId().equals(userId)
                    || document.getSharedWith().contains(userService.getUserById(userId))) {
                return new ResponseEntity<>(documentOptional.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Rename a document
    @PutMapping("/documents/{id}/rename")
    public ResponseEntity<Documents> renameDocument(@PathVariable String id,
            @RequestBody DocumentRequest documentRequest, @RequestHeader("userId") String userId) {
        Optional<Documents> documentOptional = documentService.getDocumentById(id); // Get the document by id
        if (documentOptional.isPresent()) { // If the document exists
            Documents document = documentOptional.get();
            String allowedonly = "editor";
            if (document.getOwner().getId().equals(userId)
                    || allowedonly.equals(document.getPermissions().get(userId))) {
                document.setTitle(documentRequest.getTitle()); // Set the new title
                Documents updatedDocument = documentService.renameDocument(document);
                return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @MessageMapping("/documents/{id}/edit")
    public void editDocument(@DestinationVariable String id, @Payload String content, @Header("userId") String userId) {
        Optional<Documents> documentOptional = documentService.getDocumentById(id);
        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            String allowedonly = "editor";
            if (document.getOwner().getId().equals(userId)
                    || allowedonly.equals(document.getPermissions().get(userId))) {
                document.setContent(content); /// to be replaced when handling conflicts with OT
                Documents updatedDocument = documentService.updateDocument(document);
                // Notify all clients viewing the document that it has been updated
                messagingTemplate.convertAndSend("/topic/documents/" + id, updatedDocument);
            }
        }
    }

    // Delete a document
    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id, @RequestHeader("userId") String userId) {
        Optional<Documents> documentOptional = documentService.getDocumentById(id);
        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            if (document.getOwner().getId().equals(userId)) {
                documentService.deleteDocument(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/documents/{id}/share")
    public ResponseEntity<Documents> shareDocument(@PathVariable String id, @RequestBody ShareRequest shareRequest) {
        Optional<Documents> documentOptional = documentService.getDocumentById(id);
        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            User userToShareWith = userService.getUserByUsername(shareRequest.getUsername());
            document.getSharedWith().add(userToShareWith);
            document.getPermissions().put(userToShareWith.getId(), shareRequest.getPermission());
            // to save new sharedWith and permissions
            Documents updatedDocument = documentService.updateDocument(document);
            return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/dc/create")
    public ResponseEntity<Documents> createDocument_temp(@RequestBody DocumentRequest documentRequest,
            @RequestHeader("Authorization") String token) {
        System.out.println(
                "token recieved just after is: " + token);
        if (token == null || token.isEmpty()) {
            System.out.println("Token is null or empty");
            return null;
        }
        String actualToken = token.replace("Bearer ", "");
        String username_ = jwtUtils.getUserNameFromJwtToken(actualToken); // Replace with your method to get username
                                                                          // from token
        User user = userService.getUserByUsername(username_); // Get the user who is creating the document
        // print user info
        System.out.println("User info: " + user);
        Documents document = new Documents();
        document.setContent(documentRequest.getContent());
        document.setTitle(documentRequest.getTitle());
        document.setType(documentRequest.getType());
        document.setOwner(user);
        //add the document to the users documents and update the user's documents and save the user
        user.getDocuments().add(document);
        System.out.println("User documents: ******************");
        Documents savedDocument = documentService.createDocument(document);
        System.out.println("log at the end of createDocument_temp()");
        return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);
    }

    // Open a document
    @GetMapping("/dc/view/{id}")
    public ResponseEntity<Documents> viewDocument_temp(@PathVariable String id,
            @RequestHeader("ِAuthorization") String token) {
        System.out.println("hello it's me " + token);
        String actualToken = token.replace("Bearer ", "");
        String username= jwtUtils.getUserNameFromJwtToken(actualToken); // Replace with your method to get username
                                                                          // from token
        User user = userService.getUserByUsername(username); // Get
        Optional<Documents> documentOptional = documentService.getDocumentById(id);
        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            if (document.getType().equals("public") || document.getOwner().getUsername().equals(username)
                    || document.getSharedWith().contains(userService.getUserByUsername(username))) {
                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^Document info:");
                return new ResponseEntity<>(documentOptional.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/dc/viewAll")
    public ResponseEntity<Iterable<Documents>> viewAllDocuments(@RequestHeader("Authorization") String token) {
        System.out.println("at the start of viewAllDocuments()");
        String actualToken = token.replace("Bearer ", "");
        String username = jwtUtils.getUserNameFromJwtToken(actualToken); 
        System.out.println("username: " + username+" token: "+actualToken);
        User user = userService.getUserByUsername(username); // Get
        Iterable<Documents> documents = user.getDocuments();
        //print titles of all documents
        for (Documents doc : documents) {
            System.out.println("Document title: " + doc.getTitle());
        }
        System.out.println("log at the end of viewAllDocuments()");

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }


    @GetMapping("/dc/sayHello")
    public void sayHello() {
        System.out.println("Hello from the server!");
    }

    static class DocumentRequest {
        private String content;
        private String title;
        private String type;
        // Getters and setters

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

    static class ShareRequest {
        private String username; // This represents the user that the document is shared with
        private String permission; // This represents the permission of the shared user

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPermission() {
            return permission;
        }

        public void setPermission(String permission) {
            this.permission = permission;
        }
    }

    // //view all and view all shared documents
    // @GetMapping("/documents")

}