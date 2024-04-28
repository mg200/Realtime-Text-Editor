// package com.envn8.app.repository;
// import java.util.Optional;
// import java.util.List;
// import org.springframework.data.mongodb.repository.MongoRepository;
// import org.springframework.data.mongodb.repository.Query;

// import com.envn8.app.models.Token;
// public interface TokenRepository extends MongoRepository<Token, String> {

//   @Query("{ 'user.id': ?0, '$or': [ { 'expired': false }, { 'revoked': false } ] }")
//   List<Token> findAllValidTokenByUser(String id);

//   Token findByToken(String token);
// }// package com.envn8.app.repository;
// // import java.util.Optional;
// // import java.util.List;
// // import org.springframework.data.mongodb.repository.MongoRepository;
// // import org.springframework.data.mongodb.repository.Query;

// // import com.envn8.app.models.Token;
// // public interface TokenRepository extends MongoRepository<Token, String> {

// //   @Query("{ 'user.id': ?0, '$or': [ { 'expired': false }, { 'revoked': false } ] }")
// //   List<Token> findAllValidTokenByUser(String id);

// //   Token findByToken(String token);
// // }