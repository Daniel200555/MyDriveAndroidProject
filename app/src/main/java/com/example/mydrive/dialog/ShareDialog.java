package com.example.mydrive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.mydrive.FragmentListOfFiles;
import com.example.mydrive.R;
import com.example.mydrive.dto.FileDTO;
import com.example.mydrive.dto.UserDTO;
import com.example.mydrive.service.FileGet;
import com.example.mydrive.service.FileService;
import com.example.mydrive.service.RegisterAndLogin;
import com.example.mydrive.util.UserCallback;

public class ShareDialog {

    private Dialog dialog;
    private Context context;
    private EditText shareEmail;
    private Button shareButton;


    public ShareDialog(Context context, FileDTO file) {
        Log.d("SHARE", "Open share dialog");
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.share_dialog);
        dialog.setTitle("Share File");
        dialog.setCancelable(true);
        shareEmail = (EditText) dialog.findViewById(R.id.shareEmailEditText);
        shareButton = (Button) dialog.findViewById(R.id.buttonShare);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FileGet().findUserByEmail(shareEmail.getText().toString(), new UserCallback() {
                    @Override
                    public void onUserReceive(UserDTO user) {
                        if (user == null) {
                            InfoDialog info = new InfoDialog(context, "Email: " + shareEmail.getText().toString() + ", not exist!!!");
                        } else {
                            FileService.shareFile(new RegisterAndLogin().getEmail(), shareEmail.getText().toString(), file);
                            try {
                                Thread.sleep(1000);
                                Bundle args = new Bundle();
                                args.putString("option", "all");
                                FragmentListOfFiles fragment = new FragmentListOfFiles(new RegisterAndLogin().getEmail());
                                fragment.setArguments(args);
                                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragmentListOfFile, fragment)
                                        .commit();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.show();
    }

}
