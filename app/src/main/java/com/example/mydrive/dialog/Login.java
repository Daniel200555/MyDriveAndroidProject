package com.example.mydrive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mydrive.Home;
import com.example.mydrive.MainActivity;
import com.example.mydrive.R;
import com.example.mydrive.dto.UserDTO;
import com.example.mydrive.service.ChangeActivity;
import com.example.mydrive.service.FileGet;
import com.example.mydrive.service.RegisterAndLogin;
import com.example.mydrive.util.AnswerCallback;
import com.example.mydrive.util.UserCallback;

public class Login implements View.OnClickListener {

    private Context context;
    private Dialog dialog;
    private EditText editTextEmailLogin;
    private EditText editTextPasswordLogin;
    private Button buttonLogin;

    private static final RegisterAndLogin registerAndLogin = new RegisterAndLogin();
    private static ChangeActivity changeActivity;

    public Login(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
        this.dialog.setContentView(R.layout.login_layout);
        this.dialog.setTitle("Login");
        this.dialog.setCancelable(true);
        this.editTextEmailLogin = (EditText) dialog.findViewById(R.id.editTextEmailLogin);
        this.editTextPasswordLogin = (EditText) dialog.findViewById(R.id.editTextPasswordLogin);
        this.buttonLogin = (Button) dialog.findViewById(R.id.buttonLogin);
        this.buttonLogin.setOnClickListener(this);
        this.dialog.show();
    }


    @Override
    public void onClick(View v) {
        if (v == this.buttonLogin) {
            new FileGet().findUserByEmail(this.editTextEmailLogin.getText().toString(), new UserCallback() {
                @Override
                public void onUserReceive(UserDTO user) {
                    if (user != null) {
                        registerAndLogin.login(getContext(), editTextEmailLogin.getText().toString(), editTextPasswordLogin.getText().toString(), dialog, new AnswerCallback() {
                            @Override
                            public void checkForErrors(boolean error) {
                                if (error) {
                                    InfoDialog infoDialog = new InfoDialog(context, "Password is wrong");
                                } else {
                                    new ChangeActivity(getContext(), Home.class, editTextEmailLogin.getText().toString(), "all");
                                }
                            }
                        });
                    } else {
                        InfoDialog infoDialog = new InfoDialog(context, "Email is not exist, register please");
                    }
                }
            });
//            registerAndLogin.login(getContext(), this.editTextEmailLogin.getText().toString(), this.editTextPasswordLogin.getText().toString(), dialog);

        }
    }

    public Context getContext() {
        return context;
    }
}
