package elearning.api.services;

import elearning.api.models.EncrypteUser;
import elearning.api.models.Formation;
import elearning.api.models.Invitation;
import elearning.api.repositories.FormationRepository;
import elearning.api.repositories.InvitationRepository;
import elearning.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationService {
    @Autowired
    InvitationRepository invitationRepository;

    @Autowired
    FormationRepository formationRepository;

    @Autowired
    UserRepository userRepository;

    public Invitation sendInvite(String ids, String idt, String idf) {
        EncrypteUser student = userRepository.findById(ids).get();
        EncrypteUser teacher = userRepository.findById(idt).get();
        Formation formation = formationRepository.findById(idf).get();
        Invitation invite = new Invitation(teacher, student, formation);
        invitationRepository.save(invite);
        return invite;
    }
}
