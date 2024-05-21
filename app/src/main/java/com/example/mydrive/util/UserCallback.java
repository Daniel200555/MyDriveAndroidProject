package com.example.mydrive.util;

import com.example.mydrive.dto.UserDTO;

public interface UserCallback {
    void onUserReceive(UserDTO user);
}
