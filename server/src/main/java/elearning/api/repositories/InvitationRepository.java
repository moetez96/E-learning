package elearning.api.repositories;

import elearning.api.models.Invitation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvitationRepository extends MongoRepository<Invitation, String> {

}
