package com.dhbw.unternehmenb.ssp.repositories;

import com.dhbw.unternehmenb.ssp.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
}
