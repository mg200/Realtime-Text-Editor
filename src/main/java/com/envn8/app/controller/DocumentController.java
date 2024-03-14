package com.envn8.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.envn8.app.models.Documents;
import com.envn8.app.service.DocumentService;
import java.util.Optional;
@RestController
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/documents")
    public ResponseEntity<Documents> createDocument(@RequestBody DocumentRequest documentRequest) {
        Documents document = new Documents();
        document.setTitle(documentRequest.getTitle());
        document.setContent(documentRequest.getContent());
        Documents savedDocument = documentService.createDocument(document);
        return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);
    }
    //Get a document (Open)
    @GetMapping("/documents/{id}")
public ResponseEntity<Documents> openDocument(@PathVariable String id) {
    Optional<Documents> documentOptional = documentService.getDocumentById(id);
    if (documentOptional.isPresent()) {
        return new ResponseEntity<>(documentOptional.get(), HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
    //Edit a document
    @PutMapping("/documents/{id}")
    public ResponseEntity<Documents> editDocument(@PathVariable String id, @RequestBody DocumentRequest documentRequest) {
        Optional<Documents> documentOptional = documentService.getDocumentById(id); // Get the document by id
        if (documentOptional.isPresent()) { // If the document exists
            Documents document = documentOptional.get();
            document.setTitle(documentRequest.getTitle());
            document.setContent(documentRequest.getContent());
            Documents updatedDocument = documentService.updateDocument(document);
            return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //Delete a document
    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        documentService.deleteDocument(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    static class DocumentRequest {
        private String content;
        private String title;

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
    }
}