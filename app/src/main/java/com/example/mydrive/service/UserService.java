package com.example.mydrive.service;

import android.util.Log;

import com.example.mydrive.dto.FileDTO;
import com.example.mydrive.dto.UserDTO;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class UserService {

    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference userReference;

    static {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void addDetails(String email, List<FileDTO> files, List<FileDTO> shared) {
        UserDTO user = new UserDTO(email, files, shared);
        userReference = firebaseDatabase.getReference("Users").push();
        user.key = userReference.getKey();
        userReference.setValue(user);
        Log.d("ADD USER", "Add user details!!!");
    }



}
