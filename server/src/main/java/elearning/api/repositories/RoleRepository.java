package elearning.api.repositories;

import elearning.api.models.ERole;
import elearning.api.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);

}
