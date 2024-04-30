package com.example.mydrive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mydrive.Home;
import com.example.mydrive.MainActivity;
import com.example.mydrive.R;
import com.example.mydrive.dto.FileDTO;
import com.example.mydrive.dto.UserListDTO;
import com.example.mydrive.service.ChangeActivity;
import com.example.mydrive.service.RegisterAndLogin;
import com.example.mydrive.service.UserService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Register implements View.OnClickListener {

    private Context context;

    private Dialog dialog;
    private EditText editTextEmailRegister;
    private EditText editTextPasswordRegister;
    private Button buttonRegister;

    private static final RegisterAndLogin registerAndLogin = new RegisterAndLogin();

    public Register(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
        this.dialog.setContentView(R.layout.register_layout);
        this.dialog.setTitle("Register");
        this.dialog.setCancelable(true);
        this.editTextEmailRegister = (EditText) this.dialog.findViewById(R.id.editTextEmailRegister);
        this.editTextPasswordRegister = (EditText) this.dialog.findViewById(R.id.editTextPasswordRegister);
        this.buttonRegister = (Button) this.dialog.findViewById(R.id.buttonRegister);
        this.buttonRegister.setOnClickListener(this);
        this.dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == this.buttonRegister) {
            registerAndLogin.register(getContext(), this.editTextEmailRegister.getText().toString(), editTextPasswordRegister.getText().toString(), this.dialog);
            List<FileDTO> files = new ArrayList<>();
            List<FileDTO> shares = new ArrayList<>();
            List<UserListDTO> sharedToUsers = new ArrayList<>();
            UserListDTO userListDTO = new UserListDTO("empty");
            sharedToUsers.add(userListDTO);
            FileDTO file = new FileDTO("empty","empty", "empty", "empty", 10L, "empty", false, false, null, sharedToUsers);
            FileDTO share = new FileDTO("empty","empty", "empty", "empty", 10L, "empty", false, false, null, sharedToUsers);
            files.add(file);
            shares.add(share);
            new UserService().addDetails(this.editTextEmailRegister.getText().toString(), files, shares);
            new ChangeActivity(getContext(), Home.class, this.editTextEmailRegister.getText().toString(), "all");

        }
    }

    public Context getContext() {
        return context;
    }
}
