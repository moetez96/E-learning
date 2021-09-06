package elearning.api.models;

import org.bson.BsonBinary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
public class EncrypteUser {
    @Id
    private String id;

    private BsonBinary username;

    private BsonBinary email;

    private String password;

    private String firstName;

    private String lastName;

    private String classe;

    private String speciality;

    private LocalDate birthDate;

    private LocalDateTime creationDate = LocalDateTime.now();
    private Contract contract;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    public EncrypteUser() {
    }

    public EncrypteUser(BsonBinary username, BsonBinary email, String password, String firstName, String lastName,
                        String classe, String speciality, String birthDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.classe = classe;
        this.speciality = speciality;
        this.birthDate = LocalDate.parse(birthDate, formatter);
        this.email = email;
        this.password = password;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BsonBinary getUsername() {
        return username;
    }

    public void setUsername(BsonBinary username) {
        this.username = username;
    }

    public BsonBinary getEmail() {
        return email;
    }

    public void setEmail(BsonBinary email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
