package com.envn8.app.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.envn8.app.models.CHAR;
import com.envn8.app.models.CRDT;
import com.envn8.app.models.Documents;
import com.envn8.app.models.User;
import com.envn8.app.service.DocumentService;
import com.envn8.app.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ext.OptionalHandlerFactory;

import lombok.RequiredArgsConstructor;

import com.envn8.app.payload.request.ContentRequest;
import com.envn8.app.payload.request.DocumentRequest;
import com.envn8.app.payload.request.ShareDocumentRequest;
import com.envn8.app.security.config.JwtService;

import org.apache.catalina.connector.Response;
import org.slf4j.Logger;

@RestController
@RequestMapping("/api/dc")
@RequiredArgsConstructor
public class DocumentController {
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    private DocumentService documentService;
    @Autowired // can NOT use @RequiredArgsConstructor here, @Autowired is needed
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<?> createDocument(@RequestBody DocumentRequest documentRequest,
            @RequestHeader("Authorization") String token) {
        System.out.println("at the start of createDocument_temp()");
        if (token == null || token.isEmpty()) {
            return new ResponseEntity<>("Token is null or empty", HttpStatus.UNAUTHORIZED);
        }
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);

        // Load the user details
        UserDetails userDetails = userService.getUserByUsername(username);
        if (userDetails == null) {
            return new ResponseEntity<>("User not found", HttpStatus.UNAUTHORIZED);
        }

        User user = (User) userDetails;
        List<CHAR> content = new ArrayList<>(); // as initially the document is empty

        Documents document = Documents.builder()
                .content(content)
                .title(documentRequest.getTitle())
                .type(documentRequest.getType())
                .owner(user)
                .permissions(new HashMap<>())
                .sharedWith(new ArrayList<>())
                .build();
        // Add the document to the user's documents
        user.getDocuments().add(document);

        // Save the document and the user
        documentService.createDocument(document);
        userService.saveUser(user);

        return new ResponseEntity<>(document, HttpStatus.CREATED);
    }

    // Open a document
    @GetMapping("/view/{id}")
    public ResponseEntity<Documents> viewDocument(@PathVariable String id,
            @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        if (actualToken == null || actualToken.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (id == null || id.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String username = jwtService.extractUsername(actualToken);
        Optional<Documents> documentOptional = documentService.getDocumentById(id);
        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            if (document.getType().equals("public") || document.getOwner().getUsername().equals(username)
                    || document.getSharedWith().contains(userService.getUserByUsername(username))) {
                return new ResponseEntity<>(document, HttpStatus.OK);
            } else {
                System.out.println("Forbidden access to document!");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            System.out.println("Document not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // @GetMapping("/viewAll")
    // public ResponseEntity<Iterable<Documents>>
    // viewAllDocuments(@RequestHeader("Authorization") String token) {
    // System.out.println("at the start of viewAllDocuments()");
    // String actualToken = token.replace("Bearer ", "");
    // String username = jwtService.extractUsername(actualToken);
    // System.out.println("username: " + username + " token: " + actualToken);
    // User user = userService.getUserByUsername(username); // Get
    // Iterable<Documents> documents = user.getDocuments();
    // // size of documents
    // int size = (int) documents.spliterator().getExactSizeIfKnown();
    // // System.out.println("Size of documents: " + size);
    // if (size != 0) {
    // System.out.println("Documents is not null");
    // for (Documents doc : documents) {
    // System.out.println("Document title: " + doc.getTitle());
    // }
    // } else {
    // logger.error("Documents is null");
    // }
    // // System.out.println("log at the end of viewAllDocuments()");

    // return new ResponseEntity<>(documents, HttpStatus.OK);
    // }

    @GetMapping("/viewAll")
    public ResponseEntity<Iterable<Map<String, String>>> viewAllDocuments(
            @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        User user = userService.getUserByUsername(username); // Get the current user
        Iterable<Documents> documents = user.getDocuments();
        List<Map<String, String>> result = new ArrayList<>();
        documents.forEach(doc -> {
            Map<String, String> map = new HashMap<>();
            map.put("id", doc.getId());
            map.put("title", doc.getTitle());
            result.add(map);
        });
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //get doc name from ID 
    @GetMapping("/getDocName/{id}")
    public ResponseEntity<?> getDocName(@PathVariable String id) {
        Optional<Documents> documentOptional = documentService.getDocumentById(id);
        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            return new ResponseEntity<>(document.getTitle(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Document not found", HttpStatus.NOT_FOUND);
        }
    }

    // function to delete a document
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable String id,
            @RequestHeader("Authorization") String token) {

        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        Optional<Documents> documentOptional = documentService.getDocumentById(id);
        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            if (document.getOwner().getUsername().equals(username)) {
                documentService.deleteDocument(id);
                // loop over the users with whom the document is shared and remove the document
                // from their shared documents
                for (User user : document.getSharedWith()) {
                    user.getSharedDocuments().remove(document);
                    userService.saveUser(user);
                }
                return new ResponseEntity<>("Document deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("You are not the owner of the document", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Document not found", HttpStatus.NOT_FOUND);
        }
    }

    // function to share a document
    @PostMapping("/share/{id}")
    public ResponseEntity<?> shareDocument(@PathVariable String id, @RequestBody ShareDocumentRequest shareRequest,
            @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String ownerUsername = jwtService.extractUsername(actualToken);
        Optional<Documents> documentOptional = documentService.getDocumentById(id);

        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            if (document.getOwner().getUsername().equals(ownerUsername)) {
                User user = userService.getUserByUsername(shareRequest.getUsername());

                if (user != null) {
                    // check if the user isn't sharing with himself
                    if (user.getUsername().equals(ownerUsername)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can't share with yourself");
                    }
                    document.getSharedWith().add(user);
                    document.getPermissions().put(user.getId(), shareRequest.getPermission());
                    documentService.createDocument(document);
                    user.getSharedDocuments().add(document);
                    userService.saveUser(user);
                    return new ResponseEntity<>("Document shared successfully", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>("You are not the owner of the document", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Document not found", HttpStatus.NOT_FOUND);
        }
    }

    // function to share a document
    @PostMapping("/unshare/{id}")
    public ResponseEntity<?> unshareDocument(@PathVariable String id, @RequestBody ShareDocumentRequest shareRequest,
            @RequestHeader("Authorization") String token) {

        if (token == null || token.isEmpty()) {
            return new ResponseEntity<>("Token is null or empty", HttpStatus.UNAUTHORIZED);
        }
        if (shareRequest == null) {
            return new ResponseEntity<>("ShareRequest is null", HttpStatus.BAD_REQUEST);
        }
        // if user doesn't even exist in the DB
        if (userService.getUserByUsername(shareRequest.getUsername()) == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        String actualToken = token.replace("Bearer ", "");
        String ownerUsername = jwtService.extractUsername(actualToken);
        // if user is trying to unshare with himself
        if (shareRequest.getUsername().equals(ownerUsername)) {
            return new ResponseEntity<>("You can't unshare with yourself", HttpStatus.BAD_REQUEST);
        }

        Optional<Documents> documentOptional = documentService.getDocumentById(id);

        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            // if user isn't shared with the document
            if (!document.getSharedWith().stream()
                    .anyMatch(user -> user.getUsername().equals(shareRequest.getUsername()))) {
                return new ResponseEntity<>("Document already isn't shared with the user", HttpStatus.BAD_REQUEST);
            }
            if (document.getOwner().getUsername().equals(ownerUsername)) {
                User user = userService.getUserByUsername(shareRequest.getUsername());

                if (user != null) {
                    // check if the user isn't sharing with himself
                    if (user.getUsername().equals(ownerUsername)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can't unshare with yourself");
                    }
                    document.getSharedWith().add(user);
                    document.getPermissions().put(user.getId(), shareRequest.getPermission());
                    documentService.createDocument(document);
                    user.getSharedDocuments().add(document);
                    userService.saveUser(user);
                    return new ResponseEntity<>("Document shared successfully", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>("You are not the owner of the document", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Document not found", HttpStatus.NOT_FOUND);
        }
    }

    // @GetMapping("/viewShared")
    // public ResponseEntity<Iterable<Documents>>
    // viewSharedDocuments(@RequestHeader("Authorization") String token) {
    // String actualToken = token.replace("Bearer ", "");
    // String username = jwtService.extractUsername(actualToken);
    // User user = userService.getUserByUsername(username); // Get the current user
    // if (user != null) {
    // // System.out.println("User is not null");
    // Iterable<Documents> sharedDocuments = user.getSharedDocuments();
    // // System.out.println("Shared documents size" +
    // // sharedDocuments.spliterator().getExactSizeIfKnown());
    // // sharedDocuments.forEach(doc -> System.out.println(doc.getTitle()));
    // return new ResponseEntity<>(sharedDocuments, HttpStatus.OK);
    // } else {
    // return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    // }
    // }

    @GetMapping("/viewShared")
    public ResponseEntity<Iterable<Map<String, String>>> viewSharedDocuments(
            @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        User user = userService.getUserByUsername(username); // Get the current user
        if (user != null) {
            Iterable<Documents> sharedDocuments = user.getSharedDocuments();
            List<Map<String, String>> result = new ArrayList<>();
            sharedDocuments.forEach(doc -> {
                Map<String, String> map = new HashMap<>();
                map.put("id", doc.getId());
                map.put("title", doc.getTitle());
                result.add(map);
            });
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/getDocumentOwner/{id}")
    public ResponseEntity<?> getDocumentOwner(@PathVariable String id) {
        // System.out.println("getDocumentOwner() called");
        Optional<Documents> documentOptional = documentService.getDocumentById(id);

        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            return new ResponseEntity<>(document.getOwner().getUsername(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Document not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getSharedWith/{id}")
    public ResponseEntity<Iterable<String>> getSharedWith(@PathVariable String id,
            @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        // System.out.println("Document ID: " + id);

        String ownerUsername = jwtService.extractUsername(actualToken);
        Optional<Documents> documentOptional = documentService.getDocumentById(id);

        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();

            // check if the user is in this document's sharedWith list or its owner
            if (!document.getType().equals("public") &&
                    !documentOptional.get().getOwner().getUsername().equals(ownerUsername)
                    && !documentOptional.get().getSharedWith().stream().anyMatch(user -> user.getUsername()
                            .equals(ownerUsername))) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            List<String> usernames = document.getSharedWith().stream()
                    .map(User::getUsername)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(usernames, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/isDocSharedWithUser/{id}")
    public boolean isDocSharedWithUser(@PathVariable String id,
            @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        if (actualToken == null || actualToken.isEmpty()) {
            return false;
        }
        Optional<Documents> documentOptional = documentService.getDocumentById(id);
        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            return document.getSharedWith().stream().anyMatch(user -> user.getUsername().equals(username));
        }
        return false;
    }

    // rename a document
    // might want to make a separate request for this, instead of using the same
    // DocumentRequest object
    @PutMapping("/rename/{id}")
    public ResponseEntity<?> renameDocument(@PathVariable String id,
            @RequestBody DocumentRequest documentRequest,
            @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        Optional<Documents> documentOptional = documentService.getDocumentById(id);
        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            if (document.getOwner().getUsername().equals(username)) {
                document.setTitle(documentRequest.getTitle());
                documentService.updateDocument(document);
                return new ResponseEntity<>("Document renamed successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("You are not the owner of the document", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Document not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/dc/view/{id}")
    public ResponseEntity<?> getDocumentContent(@PathVariable String id) {
        Optional<Documents> documentOptional = documentService.getDocumentById(id);
        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            // List<CRDT> content = document.getContent(); // Assuming content is a property
            // of Documents entity
            // return new ResponseEntity<>("aloooo ya habeby", HttpStatus.OK);
            return new ResponseEntity<>("Document exists, you can view it!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Document not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/dc/view2/{id}")
    public ResponseEntity<?> getDocumentContent2(@PathVariable String id) {
        Optional<Documents> documentOptional = documentService.getDocumentById(id);
        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            // List<CRDT> content = document.getContent(); // Assuming content is a property
            // of Documents entity
            return new ResponseEntity<>("aloooo ya habeby", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Document not found", HttpStatus.NOT_FOUND);
        }
    }

    // only owner can make document public or private
    @PutMapping("makePublic/{id}")
    public ResponseEntity<?> makeDocumentPublic(@PathVariable String id,
            @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        Optional<Documents> documentOptional = documentService.getDocumentById(id);
        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            if (document.getOwner().getUsername().equals(username)) {
                if (document.getType().equals("public")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Document is already public");
                }
                document.setType("public");
                documentService.updateDocument(document);
                return ResponseEntity.status(HttpStatus.OK).body("Document is now public");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of the document");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document not found");
        }
    }

    @PutMapping("makePrivate/{id}")
    public ResponseEntity<?> makeDocumentPrivate(@PathVariable String id,
            @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        Optional<Documents> documentOptional = documentService.getDocumentById(id);
        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            if (document.getOwner().getUsername().equals(username)) {
                if (document.getType().equals("private")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Document is already private");
                }
                document.setType("private");
                documentService.updateDocument(document);
                return ResponseEntity.status(HttpStatus.OK).body("Document is now private");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of the document");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document not found");
        }
    }
    // @PostMapping("/save/{id}")
    // public ResponseEntity<?> saveDocumentContent(@PathVariable String id,
    // @RequestBody String content,
    // @RequestHeader("Authorization") String token) throws JsonProcessingException
    // {
    // Optional<Documents> documentOptional = documentService.getDocumentById(id);
    // System.out.println("Alooooooooooooooooooooooo content eh "+content);
    // if (documentOptional.isPresent()) {
    // Documents document = documentOptional.get();

    // // Convert content to JSON string
    // ObjectMapper mapper = new ObjectMapper();
    // String contentJson = mapper.writeValueAsString(content);

    // document.setContent(contentJson);
    // documentService.updateDocument(document);
    // return new ResponseEntity<>("Document saved successfully", HttpStatus.OK);
    // } else {
    // return new ResponseEntity<>("Document not found", HttpStatus.NOT_FOUND);
    // }
    // }

    @GetMapping("/getUserNameFromToken")
    public ResponseEntity<?> getUserNameFromToken(@RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        return new ResponseEntity<>(username, HttpStatus.OK);
    }

    @GetMapping("/getUserAccessLevel/{id}")
    public ResponseEntity<?> getUserAccessLevel(@PathVariable String id,
            @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        if (actualToken == null || actualToken.isEmpty()) {
            return new ResponseEntity<>("Token is null or empty", HttpStatus.UNAUTHORIZED);
        }
        Optional<Documents> documentOptional = documentService.getDocumentById(id);
        if (documentOptional.isPresent()) {
            Documents document = documentOptional.get();
            if (document.getOwner().getUsername().equals(username)) {
                return new ResponseEntity<>("OWNER", HttpStatus.OK);
            } else if (document.getSharedWith().stream().anyMatch(user -> user.getUsername().equals(username))) {
                // if shared but their permission is EDITOR, return 'Editor', if Viewer, return
                // Viewer
                return new ResponseEntity<>(
                        document.getPermissions().get(userService.getUserByUsername(username).getId()), HttpStatus.OK);
                // return new ResponseEntity<>("Shared", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No access", HttpStatus.OK);
            }
        } else {

            return new ResponseEntity<>("Document not found", HttpStatus.NOT_FOUND);
        }
    }

}
