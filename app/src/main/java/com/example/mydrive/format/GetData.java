package com.example.mydrive.format;

import com.example.mydrive.dto.FileDTO;

import java.util.List;

public class GetData {

    private List<FileDTO> files;

    public GetData(List<FileDTO> files) {
        this.files = files;
    }

    public List<FileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileDTO> files) {
        this.files = files;
    }
}
