package com.example.mydrive.dto;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class UserDTO {

    public String key;
    private String uid;
    private String email;
    private List<FileDTO> files;

    private List<FileDTO> shared;

    public UserDTO() {
    }

    public UserDTO(String email, List<FileDTO> files, List<FileDTO> shared) {
        this.email = email;
        this.files = files;
        this.shared = shared;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<FileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileDTO> files) {
        this.files = files;
    }

    public List<FileDTO> getShared() {
        return shared;
    }

    public void setShared(List<FileDTO> shared) {
        this.shared = shared;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
