package elearning.api.models;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class File {
    @Id
    private String id;

    private String title;

    private Binary document;

    public File() {
    }

    public File(String title, Binary document) {
        this.title = title;
        this.document = document;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Binary getDocument() {
        return document;
    }

    public void setDocument(Binary document) {
        this.document = document;
    }
}
