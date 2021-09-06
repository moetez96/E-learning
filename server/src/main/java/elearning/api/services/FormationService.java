package elearning.api.services;

import elearning.api.models.*;
import elearning.api.payload.request.CommentRequest;
import elearning.api.payload.request.FormationRequest;
import elearning.api.repositories.CommentRepository;
import elearning.api.repositories.FileRepository;
import elearning.api.repositories.FormationRepository;
import elearning.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FormationService {
    @Autowired
    FormationRepository formationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    FileService fileService;
    @Autowired
    CommentRepository commentRepository;

    public void save(Formation formation) {
        formationRepository.save(formation);
    }

    public boolean existsById(String id) {
        return formationRepository.existsById(id);
    }

    public Formation participateCurrentUser(String id) {
        EncrypteUser euser = userDetailsService.getCurrentUser();
        Formation formation = formationRepository.findById(id).get();
        formation.getListParticipants().add(euser);
        formationRepository.save(formation);
        return formation;
    }

    public Formation removeUser(String uid, String id) {
        EncrypteUser euser = userDetailsService.getById(uid);
        Formation formation = formationRepository.findById(id).get();
        Set<EncrypteUser> users = formation.getListParticipants().stream()
                .filter(u -> !u.getId().equals(euser.getId())).collect(Collectors.toSet());
        users.remove(euser);
        formation.setListParticipants(users);
        formationRepository.save(formation);
        return formation;
    }

    public Formation cancelparticipation(String id) {
        EncrypteUser euser = userDetailsService.getCurrentUser();
        Formation formation = formationRepository.findById(id).get();
        Set<EncrypteUser> users = formation.getListParticipants().stream()
                .filter(u -> !u.getId().equals(euser.getId())).collect(Collectors.toSet());
        users.remove(euser);
        formation.setListParticipants(users);
        formationRepository.save(formation);
        return formation;
    }

    public EncrypteUser getFormationTeacher(String id) {
        return formationRepository.findById(id).get().getTeacher();
    }

    public void deleteFormation(String id) {
        Formation formation = formationRepository.findById(id).get();
        formationRepository.delete(formation);
    }

    public boolean existsByName(String name) {
        return formationRepository.existsByName(name);
    }

    public boolean existsInFormation(String idF) {
        EncrypteUser euser = userRepository.findById(userDetailsService.getCurrentUser().getId()).get();
        Formation formation = formationRepository.findById(idF).get();
        List<String> users = formation.getListParticipants().stream().map(EncrypteUser::getId).collect(Collectors.toList());
        return users.contains(euser.getId());
    }

    public boolean existsFormation(String idU, String idF) {
        EncrypteUser euser = userDetailsService.getById(idU);
        Formation formation = formationRepository.findById(idF).get();
        List<String> users = formation.getListParticipants().stream().map(EncrypteUser::getId).collect(Collectors.toList());
        return users.contains(euser.getId());
    }

    public Formation updateFormation(String id, FormationRequest formationRequest, String picture) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
        Formation formation = formationRepository.findById(id).get();
        formation.setName(formationRequest.getName());
        formation.setType(formationRequest.getType());
        formation.setDescription(formationRequest.getDescription());
        formation.setStartDate(LocalDate.parse(formationRequest.getStartDate(), formatter));
        formation.setEndDate(LocalDate.parse(formationRequest.getEndDate(), formatter));
        LoadFile pic = fileService.downloadFile(picture);
        pic.setId(picture);
        formation.setPicture(pic);
        formationRepository.save(formation);
        return formation;
    }

    public List<Formation> getAll() {
        return formationRepository.findAll();
    }

    public Formation getById(String id) {
        return formationRepository.findById(id).get();
    }

    public List<Formation> getByType(String type) {
        return formationRepository.findAll().stream().filter(formation -> formation.getType().equals(type))
                .collect(Collectors.toList());
    }

    public List<Formation> getByTeacher(String id) {
        return formationRepository.findAll().stream().filter(formation -> formation.getTeacher().getId().equals(id))
                .collect(Collectors.toList());
    }

    public boolean existsByType(String type) {
        return formationRepository.existsByType(type);
    }

    public void add(Formation formation) {
        EncrypteUser euser = userRepository.findById(userDetailsService.getCurrentUser().getId()).get();
        formation.setTeacher(euser);
        formationRepository.save(formation);
    }

    public Formation addMeeting(String id, Meet meeting) {
        Formation formation = formationRepository.findById(id).get();
        formation.setMeet(meeting);
        formationRepository.save(formation);
        return formation;
    }

    public Formation addComment(CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setFormationId(commentRequest.getFormationId());
        comment.setUserId(commentRequest.getUserId());
        comment.setDate(commentRequest.getDate());
        comment.setText(commentRequest.getText());
        comment.setUserName(commentRequest.getUserName());
        comment = commentRepository.insert(comment);
        Formation formation = formationRepository.findById(commentRequest.getFormationId()).get();
        formation.getComments().add(comment);
        formationRepository.save(formation);
        return formation;
    }

    public Formation removeComment(String idf, String idc) {
        Comment comment = commentRepository.findById(idc).get();
        Formation formation = formationRepository.findById(idf).get();
        Set<Comment> comments = formation.getComments().stream()
                .filter(u -> !u.getId().equals(comment.getId())).collect(Collectors.toSet());
        comments.remove(comment);
        formation.setComments(comments);
        formationRepository.save(formation);
        return formation;
    }
}
