package com.envn8.app.service;
import com.envn8.app.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.envn8.app.models.Documents;
import java.util.Optional;

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
        // This method now also updates the sharedWith and permissions fields of a document
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
}