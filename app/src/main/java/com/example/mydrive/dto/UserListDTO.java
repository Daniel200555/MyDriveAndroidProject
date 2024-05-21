package com.example.mydrive.dto;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserListDTO {

    private String email;
    private String dir;

    public UserListDTO() {
    }

    public UserListDTO(String email, String dir) {
        this.email = email;
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
