package com.example.mydrive.dto;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class FileDTO {

    private String owner;
    private String name;
    private String type;
    private String format;
    private long size;
    private String dir;
    private boolean isFile;
    private boolean star;
    private List<FileDTO> files;

    private List<UserListDTO> sharedToUsers;

    public FileDTO() {
    }

    public FileDTO(String owner, String name, String type, String format, long size, String dir, boolean isFile,
                   boolean star, List<FileDTO> files, List<UserListDTO> sharedToUsers) {
        this.owner = owner;
        this.name = name;
        this.type = type;
        this.format = format;
        this.size = size;
        this.dir = dir;
        this.isFile = isFile;
        this.star = star;
        this.files = files;
        this.sharedToUsers = sharedToUsers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public List<FileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileDTO> files) {
        this.files = files;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<UserListDTO> getSharedToUsers() {
        return sharedToUsers;
    }

    public void setSharedToUsers(List<UserListDTO> sharedToUsers) {
        this.sharedToUsers = sharedToUsers;
    }

}
