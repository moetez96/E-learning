package elearning.api.repositories;

import elearning.api.models.EncrypteUser;
import elearning.api.models.Formation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FormationRepository extends MongoRepository<Formation, String> {

    boolean existsByName(String name);

    boolean existsByType(String type);
}
