package com.task1.take1.repository;

import com.task1.take1.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
//    Optional<User> findUserByActor(String actor);
}
