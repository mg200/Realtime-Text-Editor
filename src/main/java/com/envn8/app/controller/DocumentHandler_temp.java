package com.envn8.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import com.envn8.app.models.Documents;
import com.envn8.app.models.User;
import com.envn8.app.security.jwt.JwtUtils;
import com.envn8.app.service.DocumentService;
import com.envn8.app.service.userService;
import com.envn8.app.payload.request.DocumentRequest;

@Controller
public class DocumentHandler_temp {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private userService userService;
    @Autowired
    JwtUtils jwtUtils;

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
        System.out.println("User info: " + user);
        Documents document = new Documents();
        document.setContent(documentRequest.getContent());
        document.setTitle(documentRequest.getTitle());
        document.setType(documentRequest.getType());
        document.setOwner(user);
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
        String username = jwtUtils.getUserNameFromJwtToken(actualToken);
        Optional<Documents> documentOptional = documentService.getDocumentById(id);
        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            if (document.getType().equals("public") || document.getOwner().getUsername().equals(username)
                    || document.getSharedWith().contains(userService.getUserByUsername(username))) {
                System.out.println("***********Document info:************");
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
        System.out.println("username: " + username + " token: " + actualToken);
        User user = userService.getUserByUsername(username); // Get
        Iterable<Documents> documents = user.getDocuments();
        if (documents != null) {
            for (Documents doc : documents) {
                System.out.println("Document title: " + doc.getTitle());
            }
        } else {
            System.out.println("Documents is null");
        }
        System.out.println("log at the end of viewAllDocuments()");

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/dc/sayHello")
    public void sayHello() {
        System.out.println("Hello from the server!");
    }
}
