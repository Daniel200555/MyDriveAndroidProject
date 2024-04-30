package com.example.mydrive.dto;

public class UserListDTO {

    private String email;

    public UserListDTO() {
    }

    public UserListDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
