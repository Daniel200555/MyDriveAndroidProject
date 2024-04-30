package com.example.mydrive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mydrive.Home;
import com.example.mydrive.MainActivity;
import com.example.mydrive.R;
import com.example.mydrive.service.ChangeActivity;
import com.example.mydrive.service.RegisterAndLogin;

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
            registerAndLogin.login(getContext(), this.editTextEmailLogin.getText().toString(), this.editTextPasswordLogin.getText().toString(), dialog);
            new ChangeActivity(getContext(), Home.class, this.editTextEmailLogin.getText().toString(), "all");
        }
    }

    public Context getContext() {
        return context;
    }
}
