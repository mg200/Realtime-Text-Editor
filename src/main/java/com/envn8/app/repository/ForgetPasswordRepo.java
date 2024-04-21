package com.envn8.app.repository;

import com.envn8.app.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface ForgetPasswordRepo extends MongoRepository<User, String>{
User findByEmail(String email);
User findByUsername(String username);
Boolean existsByEmail(String email);
Boolean existsByUsername(String username);
}
