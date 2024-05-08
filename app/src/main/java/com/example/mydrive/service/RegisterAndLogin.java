package com.example.mydrive.service;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterAndLogin {

    private static FirebaseAuth firebaseAuth;

    static {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void register(Context context, String email, String password, Dialog dialog) {
        this.firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isComplete()) {
                    new FileManager().saveDir(new RegisterAndLogin().getEmail(), "",context, email);
                    Toast.makeText(context, "Successfully complete", Toast.LENGTH_LONG).show();
                    Log.i("Register ->", "User registered with email -> " + email);
                } else {
                    Toast.makeText(context, "Register Error", Toast.LENGTH_LONG).show();
                    Log.i("Register ->", "User could not login with email -> " + email);
                }
                dialog.dismiss();
            }
        });

    }

    public void login(Context context, String email, String password, Dialog dialog) {
        this.firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Successfully login", Toast.LENGTH_LONG).show();
                    Log.i("Login ->", "User login with email -> " + email);
                } else {
                    Toast.makeText(context, "Login Error", Toast.LENGTH_LONG).show();
                    Log.i("Login ->", "User could not login with email -> " + email);
                }
                dialog.dismiss();
            }
        });
    }

    public void logout() {
        this.firebaseAuth.signOut();
        Log.i( "Logout -> ", "ok");
    }

    public boolean checkIfUserInSystem() {
        FirebaseUser firebaseUser = this.firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            Log.i("Check user -> ", "false");
            return false;
        } else {
            Log.i("Check user -> ", "true");
            return true;
        }
    }

    public String getEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return (checkIfUserInSystem()) ? user.getEmail() : null;
    }

    public FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

}
