package com.example.mydrive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mydrive.dialog.Login;
import com.example.mydrive.dialog.Register;
import com.example.mydrive.service.ChangeActivity;
import com.example.mydrive.service.RegisterAndLogin;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonMainRegister;
    private Button buttonMainLogin;

    public FirebaseAuth firebaseAuth;

    private Register register;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.buttonMainRegister = (Button) findViewById(R.id.buttonMainRegister);
        this.buttonMainRegister.setOnClickListener(this);

        this.buttonMainLogin = (Button)  findViewById(R.id.buttonMainLogin);
        this.buttonMainLogin.setOnClickListener(this);

        if (new RegisterAndLogin().checkIfUserInSystem()) {
            new ChangeActivity(this, Home.class);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == this.buttonMainRegister) {
            this.register = new Register(this);
        }
        if(v == this.buttonMainLogin) {
            this.login = new Login(this);
        }
    }
}