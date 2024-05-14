package com.envn8.app.service;

import com.envn8.app.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.envn8.app.models.CHAR;
import com.envn8.app.models.CRDT;
import com.envn8.app.models.Documents;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    // Create
    public Documents createDocument(Documents document) {
        return documentRepository.save(document);
    }

    // Read
    public Optional<Documents> getDocumentById(String id) {
        return documentRepository.findById(id);
    }

    // Update
    public Documents updateDocument(Documents document) {
        return documentRepository.save(document);
    }

    // Delete
    public void deleteDocument(String id) {
        documentRepository.deleteById(id);
    }

    // Rename
    public Documents renameDocument(Documents document) {
        return documentRepository.save(document);
    }
    public List<CHAR> getContent(String documentId) {
        Optional<Documents> document = documentRepository.findById(documentId);
        if (document.isPresent()) {
            return document.get().getContent();
        } else {
            return null; 
}
}


}