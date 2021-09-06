package elearning.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Document
public class Formation {
    @Id
    private String id;
    private String name;
    private String type;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private LoadFile picture;
    private Meet meet;

    @DBRef
    private EncrypteUser teacher;

    @DBRef
    private Set<EncrypteUser> listParticipants = new HashSet<>();

    private Set<LoadFile> documents = new HashSet<>();
    private Set<Comment> comments = new HashSet<>();

    public Formation() {
    }


    public Formation(String type, String name, String startDate, String endDate, String description) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
        this.type = type;
        this.name = name;
        this.description = description;
        this.startDate = LocalDate.parse(startDate, formatter);
        this.endDate = LocalDate.parse(endDate, formatter);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Meet getMeet() {
        return meet;
    }

    public void setMeet(Meet meet) {
        this.meet = meet;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public LoadFile getPicture() {
        return picture;
    }

    public void setPicture(LoadFile picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public EncrypteUser getTeacher() {
        return teacher;
    }

    public void setTeacher(EncrypteUser teacher) {
        this.teacher = teacher;
    }

    public Set<EncrypteUser> getListParticipants() {
        return listParticipants;
    }

    public void setListParticipants(Set<EncrypteUser> listParticipants) {
        this.listParticipants = listParticipants;
    }

    public Set<LoadFile> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<LoadFile> documents) {
        this.documents = documents;
    }
}
