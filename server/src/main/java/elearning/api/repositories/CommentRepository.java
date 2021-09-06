package elearning.api.repositories;

import elearning.api.models.Comment;
import elearning.api.models.File;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {
}
