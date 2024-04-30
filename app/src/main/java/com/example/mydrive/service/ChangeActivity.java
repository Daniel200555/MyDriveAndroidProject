package com.example.mydrive.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

public class ChangeActivity {

    public ChangeActivity(Context contextFrom, Class<?> classTo) {
        Intent intent = new Intent(contextFrom, classTo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        contextFrom.startActivity(intent);
    }

    public ChangeActivity(Context contextFrom, Class<?> classTo, String email, String option) {
        Intent intent = new Intent(contextFrom, classTo);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("email", email);
        intent.putExtra("option", option);
        contextFrom.startActivity(intent);
    }

}
