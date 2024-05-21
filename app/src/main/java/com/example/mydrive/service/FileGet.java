package com.example.mydrive.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mydrive.dto.FileDTO;
import com.example.mydrive.dto.UserDTO;
import com.example.mydrive.dto.UserListDTO;
import com.example.mydrive.util.FilesCallback;
import com.example.mydrive.util.UserCallback;
import com.example.mydrive.util.UsersCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileGet {

    private static DatabaseReference databaseReference;

    public FileGet() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void findUserByEmail(String email, UserCallback callback) {
        databaseReference.child("Users").orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDTO userDTO = snapshot.getValue(UserDTO.class);
                callback.onUserReceive(userDTO);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getFiles(String email, final FilesCallback filesCallback) {
        Log.d("FILE", email);
        Query query = databaseReference.child("Users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<FileDTO> files = new ArrayList<>();
                Log.d("FILE GET", "Create list");
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
                    if (userDTO != null && userDTO.getFiles() != null)
                        for (FileDTO fileDTO : userDTO.getFiles()) {
                            addFilesRecursive(fileDTO, files);
                        }
                    Log.d("FILE GET", "Size of file " + files.size());

                }
                filesCallback.onFilesReceived(files);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                filesCallback.onCancelled(error);
            }
        });
        databaseReference = null;
    }

    private void addFilesRecursive(FileDTO fileDTO, List<FileDTO> files) {
        files.add(fileDTO);
        if (fileDTO.getFiles() != null) {
            for (FileDTO subFileDTO : fileDTO.getFiles()) {
                addFilesRecursive(subFileDTO, files);
            }
        }
    }

    public void getShared(String email, final FilesCallback filesCallback) {
        Log.d("FILE GET", email);
        Query query = databaseReference.child("Users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<FileDTO> files = new ArrayList<>();
                Log.d("FILE GET", "Create list");
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
                    if (userDTO != null && userDTO.getFiles() != null)
                        files.addAll(userDTO.getShared());
                    Log.d("FILE GET", "Size of file " + files.size());
                    filesCallback.onFilesReceived(files);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                filesCallback.onCancelled(error);
            }
        });
        databaseReference = null;
    }

    public void addFile(String email, FileDTO file) {
        Query query = databaseReference.child("Users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<FileDTO> files = new ArrayList<>();
                UserDTO userDTO = null;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    userDTO = dataSnapshot.getValue(UserDTO.class);
                    if (userDTO != null && userDTO.getFiles() != null)
                        files.addAll(userDTO.getFiles());
                    else
                        Log.d("UPLOAD FILE", "User is empty");
                    files.add(file);
                    userDTO.setFiles(files);
                    dataSnapshot.getRef().setValue(userDTO);
                }
                Log.d("UPLOAD FILE", "Size of list " + files.size());
                Log.d("FILE SAVE", "File saved with name " + file.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference = null;
    }

    public void shareFileToEmail(String emailShare, FileDTO file) {
        Query query = databaseReference.child("Users").orderByChild("email").equalTo(emailShare);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<FileDTO> shares = new ArrayList<>();
                List<FileDTO> files = new ArrayList<>();
                FileDTO temp = null;
                UserDTO userDTO = null;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    userDTO = dataSnapshot.getValue(UserDTO.class);
                    if (userDTO != null && userDTO.getFiles() != null) {
                        files.addAll(userDTO.getFiles());
                        shares.addAll(userDTO.getShared());
                    }
//                    for (int i = 0; i < files.size(); i++) {
//                        if (files.get(i).getDir().equals(path))
//                            temp = files.get(i);
//                    }
                    shares.add(file);
                    userDTO.setShared(shares);
                    dataSnapshot.getRef().setValue(userDTO);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference = null;
    }


    public void shareFile(String shareEmail, String ownerEmail, String path) {
        UserListDTO userL = new UserListDTO(shareEmail, path);
        Query query = databaseReference.child("Users").orderByChild("email").equalTo(ownerEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<FileDTO> files = new ArrayList<>();
                List<UserListDTO> usersList = new ArrayList<>();
                UserDTO userDTO = null;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    userDTO = dataSnapshot.getValue(UserDTO.class);
                    files.addAll(userDTO.getFiles());
                    for (int i = 0; i < files.size(); i++) {
                        FileDTO  file = files.get(i);
                        if (file.getDir().equals(path)) {
                            usersList.addAll(file.getSharedToUsers());
                            usersList.add(userL);
                            file.setSharedToUsers(usersList);
                            files.set(i, file);
                            Log.d("FILE SHARE", "Add file to list for user list");
                            break;
                        }
                    }
                    userDTO.setFiles(files);
                    dataSnapshot.getRef().setValue(userDTO);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference = null;
    }

    public void deleteFile(String email, String fileName) {
        Query query = databaseReference.child("Users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<FileDTO> files = new ArrayList<>();
                UserDTO user = null;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    user = dataSnapshot.getValue(UserDTO.class);
                    files.addAll(user.getFiles());
                    for (int i = 0; i < files.size(); i++) {
                        if (files.get(i).getName().equals(fileName)) {
                            files.remove(i);
                            break;
                        }
                    }
                    user.setFiles(files);
                    dataSnapshot.getRef().setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference = null;
    }

    public void getAllUsersShared(String email, String dir, UsersCallback usersCallback) {
        Query query = databaseReference.child("Users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDTO user = null;
                List<UserListDTO> users = new ArrayList<>();
                List<FileDTO> files = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    user = dataSnapshot.getValue(UserDTO.class);
                    files.addAll(user.getFiles());
                    for (FileDTO file:
                         files) {
                        if (file.getDir().equals(dir)) {
                            users.addAll(file.getSharedToUsers());
                            break;
                        }
                    }
                    usersCallback.onUsersReceived(users);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                usersCallback.onCancelled(error);
            }
        });
    }

    public void renameFile(String email, String newNameOfFile, String oldNameOfFile) {
        Query query = databaseReference.child("Users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<FileDTO> files = new ArrayList<>();
                UserDTO user = null;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    user = dataSnapshot.getValue(UserDTO.class);
                    files.addAll(user.getFiles());
                    for (int i = 0; i < files.size(); i++) {
                        FileDTO file = files.get(i);
                        if (file.getName().equals(oldNameOfFile)) {
                            file.setName(newNameOfFile);
                            files.remove(i);
                            files.add(file);
                            break;
                        }
                    }
                    user.setFiles(files);
                    dataSnapshot.getRef().setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteFromShareUserShareFile(String shareEmail, String dir) {
        Query query = databaseReference.child("Users").orderByChild("email").equalTo(shareEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDTO user = null;
                List<FileDTO> sharedFiles = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    user = dataSnapshot.getValue(UserDTO.class);
                    sharedFiles.addAll(user.getShared());
                    for (int i = 0; i < sharedFiles.size(); i++) {
                        if (sharedFiles.get(i).getDir().equals(dir)) {
                            sharedFiles.remove(i);
                            break;
                        }
                    }
                    user.setShared(sharedFiles);
                    dataSnapshot.getRef().setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteSharedFileFromOwner(String emailShared, String emailOwner, String dir) {
        Query query = databaseReference.child("Users").orderByChild("email").equalTo(emailOwner);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<FileDTO> files = new ArrayList<>();
                List<UserListDTO> users = new ArrayList<>();
                UserDTO user = null;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    user = dataSnapshot.getValue(UserDTO.class);
                    files.addAll(user.getFiles());
                    for (int i = 0; i < files.size(); i++) {
                        FileDTO file = files.get(i);
                        if (file.getDir().equals(dir)) {
                            users.addAll(file.getSharedToUsers());
                            for (int j = 0; j < users.size(); j++) {
                                if (users.get(j).getEmail().equals(emailShared)) {
                                    users.remove(j);
                                }
                            }
                            file.setSharedToUsers(users);
                            files.set(i, file);
                        }
                    }
                    user.setShared(files);
                    dataSnapshot.getRef().setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void starList(String email, String filePath) {
        Query query = databaseReference.child("Users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDTO userDTO = null;
                FileDTO file = null;
                List<FileDTO> files = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    userDTO = dataSnapshot.getValue(UserDTO.class);
                    files.addAll(userDTO.getFiles());
                    for (int i = 0; i < files.size(); i++) {
                        if (files.get(i).getDir().equals(filePath)) {
                            file = files.get(i);
                            file.setStar(true);
                            files.set(i, file);
                        }
                    }
                    userDTO.setFiles(files);
                    dataSnapshot.getRef().setValue(userDTO);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showPath(String email, String filePath, FilesCallback filesCallback) {
        Query query = databaseReference.child("Users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDTO userDTO = null;
                FileDTO file = null;
                List<FileDTO> files = new ArrayList<>();
                List<FileDTO> temp = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    userDTO = dataSnapshot.getValue(UserDTO.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void unStarList(String email, String filePath) {
        Log.d("FILE UN STAR", "in method");
        Query query = databaseReference.child("Users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDTO userDTO = null;
                FileDTO file = null;
                List<FileDTO> files = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    userDTO = dataSnapshot.getValue(UserDTO.class);
                    files.addAll(userDTO.getFiles());
                    for (int i = 0; i < files.size(); i++) {
                        if (files.get(i).getDir().equals(filePath)) {
                            file = files.get(i);
                            file.setStar(false);
                            files.set(i, file);
                        }
                    }
                    userDTO.setFiles(files);
                    dataSnapshot.getRef().setValue(userDTO);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
