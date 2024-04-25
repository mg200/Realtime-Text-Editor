package com.envn8.app.repository;


import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

// import com.bezkoder.spring.security.mongodb.models.ERole;
// import com.bezkoder.spring.security.mongodb.models.Role;
import com.envn8.app.models.Role;
import com.envn8.app.models.ERole;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
