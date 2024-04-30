package com.example.mydrive.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mydrive.dto.FileDTO;
import com.example.mydrive.dto.UserDTO;
import com.example.mydrive.format.GetData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

//    public String getNickname(String email) {
//        final String[] result = new String[1];
//        userReference = firebaseDatabase.getReference("Users");
//        userReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                    UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
//                    if (userDTO.getEmail().equals(email)) result[0] = userDTO.getNickname();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("GET NICKNAME", "poshol v jopu!!!!");
//            }
//        });
//        return result[0];
//    }

//    public void updateFile(String email, FileDTO file) {
//        userReference = firebaseDatabase.getReference("Users");
//        Query query = userReference.orderByChild("email").equalTo(email);
//        Log.d("UPLOAD FILE", "query user");
//        int count[] = new int[1];
//        count[0] = 0;
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                    UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
//                    if (userDTO.getEmail().equals(email)) {
//                        System.out.println(userDTO.getEmail());
//                        List<FileDTO> fileDTOS = userDTO.getFiles();
//                        if (count[0] >= 1) break;
//                        else {
//                            fileDTOS.add(file);
//                            count[0] = count[0] + 1;
//                        }
//                        dataSnapshot.getRef().child("files").setValue(fileDTOS);
//                        fileDTOS = null;
//                        Log.d("UPLOAD FILE", "file upload success");
//                    }
//                    break;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("UPLOAD FILE", "file upload error");
//            }
//        });
//    }
//
//    public List<FileDTO> getAllFiles(String email) {
//        userReference = firebaseDatabase.getReference("Users");
//        Query query = userReference.orderByChild("email").equalTo(email);
//        final List<FileDTO>[] files = new List[]{new ArrayList<>()};
//        Log.d("GET LIST", "Ready to get List");
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
////                    UserDTO user = dataSnapshot.getValue(UserDTO.class);
////                    Log.d("GET LIST", user.getEmail());
////                    files[0] = user.getFiles();
////                    Log.d("GET LIST", "Size of files: " + files[0].size());
//                    files[0] = dataSnapshot.child("files").getValue(List.class);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return files[0];
//    }



}
