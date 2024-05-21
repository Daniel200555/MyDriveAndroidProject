package com.example.mydrive.util;

import com.example.mydrive.dto.FileDTO;
import com.example.mydrive.dto.UserListDTO;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public interface UsersCallback {

    void onUsersReceived(List<UserListDTO> users);

    void onCancelled(DatabaseError databaseError);

}
