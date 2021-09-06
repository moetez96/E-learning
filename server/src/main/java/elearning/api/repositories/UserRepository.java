package elearning.api.repositories;

import elearning.api.models.EncrypteUser;
import elearning.api.models.User;
import org.bson.BsonBinary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<EncrypteUser, String> {
    Optional<EncrypteUser> findByUsername(BsonBinary username);

    Boolean existsByUsername(BsonBinary username);

    Boolean existsByEmail(BsonBinary email);
}
