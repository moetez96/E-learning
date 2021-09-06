package elearning.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Invitation {
    @Id
    private String id;
    private EncrypteUser teacher;
    private EncrypteUser student;
    private Formation formation;

    public Invitation() {
    }

    public Invitation(EncrypteUser teacher, EncrypteUser student, Formation formation) {
        this.teacher = teacher;
        this.student = student;
        this.formation = formation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EncrypteUser getTeacher() {
        return teacher;
    }

    public void setTeacher(EncrypteUser teacher) {
        this.teacher = teacher;
    }

    public EncrypteUser getStudent() {
        return student;
    }

    public void setStudent(EncrypteUser student) {
        this.student = student;
    }

    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }
}
