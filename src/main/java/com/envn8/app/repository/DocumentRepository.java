package com.envn8.app.repository;
import com.envn8.app.models.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends MongoRepository<Documents, String>{

    
}
