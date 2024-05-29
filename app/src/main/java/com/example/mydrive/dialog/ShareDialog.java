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
                FileService.shareFile(context, new RegisterAndLogin().getEmail(), shareEmail.getText().toString(), file);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
