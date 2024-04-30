package com.example.mydrive.util;

import com.example.mydrive.dto.FileDTO;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public interface FilesCallback {

    void onFilesReceived(List<FileDTO> files);

    void onCancelled(DatabaseError databaseError);

}
