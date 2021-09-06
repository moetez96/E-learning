package elearning.api.payload.request;

import org.springframework.web.multipart.MultipartFile;

public class FileRequest {
    private String title;

    private MultipartFile document;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MultipartFile getDocument() {
        return document;
    }

    public void setDocument(MultipartFile document) {
        this.document = document;
    }
}
