package ru.hogwarts.school.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.controller.AvatarController;

@Entity
public class Avatar {
    @Id
    @GeneratedValue
    Long id;
    @JsonIgnore
    private String filePath;
    private long fileSize;
    private String mediaType;
    private String url;
    @JsonIgnore
    private byte[] data;
    @Lob
    @JsonIgnore
    private byte[] preview;

    @OneToOne
    @JsonIgnore
    private Student student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public byte[] getPreview() {
        return preview;
    }

    public void setPreview(byte[] preview) {
        this.preview = preview;
    }

    public String getUrl() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(System.getProperty("server.port", "8080"))
                .pathSegment(AvatarController.BASE_PATH, id.toString())
                .toUriString();
    }
}
